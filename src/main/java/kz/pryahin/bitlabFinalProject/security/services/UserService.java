package kz.pryahin.bitlabFinalProject.security.services;

import kz.pryahin.bitlabFinalProject.security.dtos.*;
import kz.pryahin.bitlabFinalProject.security.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.naming.AuthenticationException;
import java.security.Principal;

public interface UserService {
	User getUserEntity(String email);

	String createUser(RegisterUserDto registerUserDto);

	String verifyUser(VerifyUserDto verifyUserDto);

	User getUserByCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException;

	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

	String updateUserPassword(UpdateUserPasswordDto updateUserPasswordDto, Principal principal);

	String sendCodeToUpdatePassword(UserEmailDto userEmailDto);

	String updateUserFullName(UserFullNameDto userFullNameDto, Principal principal);

	GetUserDto getUserDto(String email);

	String resendVerifyCode(UserEmailDto userEmailDto);

	String sendDeleteUserCode(UserCredentialsDto userCredentialsDto) throws AuthenticationException;

	String deleteUser(VerifyUserDto verifyUserDto) throws AuthenticationException;

	String restoreUser(UserCredentialsDto userCredentialsDto) throws AuthenticationException;

}
