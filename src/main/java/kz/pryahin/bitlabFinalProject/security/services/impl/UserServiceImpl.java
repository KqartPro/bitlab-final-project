package kz.pryahin.bitlabFinalProject.security.services.impl;

import jakarta.transaction.Transactional;
import kz.pryahin.bitlabFinalProject.mapper.UserMapper;
import kz.pryahin.bitlabFinalProject.security.dtos.*;
import kz.pryahin.bitlabFinalProject.security.entities.BackupUser;
import kz.pryahin.bitlabFinalProject.security.entities.Role;
import kz.pryahin.bitlabFinalProject.security.entities.User;
import kz.pryahin.bitlabFinalProject.security.repositories.BackupUserRepository;
import kz.pryahin.bitlabFinalProject.security.repositories.RoleRepository;
import kz.pryahin.bitlabFinalProject.security.repositories.UserRepository;
import kz.pryahin.bitlabFinalProject.security.services.BackupUserService;
import kz.pryahin.bitlabFinalProject.security.services.SendCodeService;
import kz.pryahin.bitlabFinalProject.security.services.UserService;
import kz.pryahin.bitlabFinalProject.security.services.ValidateUserDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final RoleRepository roleRepository;
	private final SendCodeService sendCodeService;
	private final PasswordEncoder passwordEncoder;
	private final ValidateUserDataService validateUserDataService;
	private final BackupUserRepository backupUserRepository;
	private final BackupUserService backupUserService;

	@Override
	public User getUserEntity(String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format(" User %s " +
			"not found", email)));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
	}

	@Override
	public GetUserDto getUserDto(String email) throws UsernameNotFoundException {
		User user = getUserEntity(email);
		return userMapper.mapUserEntityToGetUserDto(user);
	}

	@Override
	public User getUserByCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
		Optional<User> optionalUser = userRepository.findByEmail(userCredentialsDto.getEmail());
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			if (passwordEncoder.matches(userCredentialsDto.getPassword(), user.getPassword())) {
				return user;
			}
		}
		throw new AuthenticationException("Email or password is not correct");
	}

	@Override
	public String createUser(RegisterUserDto registerUserDto) {
		if (!validateUserDataService.isEmailValid(registerUserDto.getEmail())) {
			return "Incorrect email";
		}
		if (!validateUserDataService.isNameValid(registerUserDto.getName()) || !validateUserDataService.isNameValid(registerUserDto.getSurname())) {
			return validateUserDataService.writeNameRulesDescription();
		}
		if (!validateUserDataService.isPasswordValid(registerUserDto.getPassword())) {
			return validateUserDataService.writePasswordRulesDescription();
		}
		if (userRepository.findByEmail(registerUserDto.getEmail()).isPresent()) {
			return "User already exists";
		}
		if (!registerUserDto.getPassword().equals(registerUserDto.getRePassword())) {
			return "Passwords do not match";
		}

		if (backupUserRepository.findByEmail(registerUserDto.getEmail()).isPresent()) {
			return "You recently deleted this account, so you can't create the new one by using the same email, but you can" +
				" restore account";
		}

		User user = userMapper.mapRegisterUserDtoToUserEntity(registerUserDto);
		String verificationCode = UUID.randomUUID().toString().substring(0, 4);

		user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

		Optional<Role> optionalRole = roleRepository.findByName("ROLE_GUEST");
		if (optionalRole.isPresent()) {
			Role role = optionalRole.get();
			user.setRoles(List.of(role));
		}

		user.setCode(verificationCode);
		sendCodeService.sendVerificationCode(user.getName(), user.getSurname(), verificationCode, user.getEmail());

		userRepository.save(user);
		return """ 
			User added successfully!
			We have send verification code for you, please check your email and enter the code to verify your account
			""";

	}

	@Override
	public String verifyUser(VerifyUserDto verifyUserDto) {
		User user = getUserEntity(verifyUserDto.getEmail());

		if (!passwordEncoder.matches(verifyUserDto.getPassword(), user.getPassword())) {
			return "Incorrect Password";
		}

		if (user.isVerificationStatus()) {
			return "Your account already verified";
		}

		if (!verifyUserDto.getCode().equals(user.getCode())) {
			return "Incorrect Verification Code";
		}

		user.setVerificationStatus(true);

		Optional<Role> roleUserOptional = roleRepository.findByName("ROLE_USER");
		Optional<Role> roleQuestOptional = roleRepository.findByName("ROLE_GUEST");

		if (roleUserOptional.isPresent() && roleQuestOptional.isPresent()) {
			List<Role> roles = new ArrayList<>();
			roles.add(roleUserOptional.get());
			roles.add(roleQuestOptional.get());
			user.setRoles(roles);
		} else {
			System.out.println("Role not found");
		}

		user.setCode(null);
		userRepository.save(user);
		return "Your account verified";
	}

	@Override
	public String resendVerifyCode(UserEmailDto userEmailDto) {
		User user = getUserEntity(userEmailDto.getEmail());
		String verificationCode = UUID.randomUUID().toString().substring(0, 4);
		user.setCode(verificationCode);
		userRepository.save(user);
		sendCodeService.resendVerificationCode(userEmailDto.getEmail(), verificationCode);
		return "We have send verification code for you, please check your email and enter the code to verify your account";
	}

	@Override
	public String sendCodeToUpdatePassword(UserEmailDto userEmailDto) {

		User user = getUserEntity(userEmailDto.getEmail());

		String code = UUID.randomUUID().toString().substring(0, 8);
		sendCodeService.sendPasswordUpdateCode(userEmailDto.getEmail(), code);
		user.setCode(code);
		userRepository.save(user);
		return "We have send code for you, please check your email and enter the code to update your password";

	}

	@Override
	public String updateUserPassword(UpdateUserPasswordDto updateUserPasswordDto, Principal principal) {

		User user = getUserEntity(principal.getName());

		if (!passwordEncoder.matches(updateUserPasswordDto.getPassword(), user.getPassword())) {
			return "Incorrect Password";
		}
		if (updateUserPasswordDto.getPassword().equals(updateUserPasswordDto.getUpdatedPassword())) {
			return "Password and new password must be different";
		}
		if (!validateUserDataService.isPasswordValid(updateUserPasswordDto.getUpdatedPassword())) {
			return validateUserDataService.writePasswordRulesDescription();
		}
		if (!updateUserPasswordDto.getCode().equals(user.getCode())) {
			return "Incorrect Code";
		}

		user.setPassword(passwordEncoder.encode(updateUserPasswordDto.getUpdatedPassword()));
		user.setCode(null);
		userRepository.save(user);
		return "Success! You changed the password";
	}

	@Override
	public String updateUserFullName(UserFullNameDto userFullNameDto, Principal principal) {

		User user = getUserEntity(principal.getName());

		if (userFullNameDto.getName() != null) {
			if (!validateUserDataService.isNameValid(userFullNameDto.getName())) {
				return validateUserDataService.writeNameRulesDescription();
			} else {
				user.setName(userFullNameDto.getName());
				userRepository.save(user);
			}
		}

		if (userFullNameDto.getSurname() != null) {
			if (!validateUserDataService.isNameValid(userFullNameDto.getSurname())) {
				return validateUserDataService.writeNameRulesDescription();
			} else {
				user.setSurname(userFullNameDto.getSurname());
				userRepository.save(user);
			}
		}
		return "Success! You changed your name";
	}

	@Override
	public String sendDeleteUserCode(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
		User user = getUserByCredentials(userCredentialsDto);
		String deleteUserCode = UUID.randomUUID().toString().substring(0, 8);
		sendCodeService.sendDeleteUserCode(userCredentialsDto.getEmail(), deleteUserCode);
		user.setCode(deleteUserCode);
		userRepository.save(user);
		return "We have send code for you, please check your email and enter the code to delete your account";
	}

	@Override
	@Transactional
	public String deleteUser(VerifyUserDto verifyUserDto) throws AuthenticationException {
		UserCredentialsDto userCredentialsDto = new UserCredentialsDto(verifyUserDto.getEmail(), verifyUserDto.getPassword());
		User user = getUserByCredentials(userCredentialsDto);

		if (!verifyUserDto.getCode().equals(user.getCode())) {
			return "Incorrect Code";
		}
		Date date = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());

		BackupUser backupUser = userMapper.mapUserEntityToBackupUser(user);
		backupUser.setExpiredDate(date);
		backupUser.setVerificationStatus(false);
		backupUser.setCode(null);

		backupUserRepository.save(backupUser);
		userRepository.deleteByEmail(verifyUserDto.getEmail());

		return "Your account has been deleted. You can't restore your account after 30 days";
	}

	@Override
	@Transactional
	public String restoreUser(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
		BackupUser backupUser = backupUserService.getUserByCredentials(userCredentialsDto);
		User user = userMapper.mapBackupUserToUserEntity(backupUser);
		user.setCode(null);
		userRepository.save(user);
		backupUserRepository.deleteByEmail(userCredentialsDto.getEmail());
		return "Your account has been restored, Now please login";
	}

}
