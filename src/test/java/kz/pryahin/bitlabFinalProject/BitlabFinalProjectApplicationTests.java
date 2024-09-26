package kz.pryahin.bitlabFinalProject;

import kz.pryahin.bitlabFinalProject.mapper.UserMapper;
import kz.pryahin.bitlabFinalProject.security.dtos.RegisterUserDto;
import kz.pryahin.bitlabFinalProject.security.entities.User;
import kz.pryahin.bitlabFinalProject.security.repositories.UserRepository;
import kz.pryahin.bitlabFinalProject.security.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class BitlabFinalProjectApplicationTests {
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserMapper userMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void shouldGetUserEntity() {
		String email = "kqartpro@gmail.com";
		addTestUser(email);

		Optional<User> userR = userRepository.findByEmail(email);

		if (userR.isPresent()) {
			User user = userService.getUserEntity(email);
			Assertions.assertNotNull(user);
		}

		userRepository.delete(userR.get());
	}

	@Test
	void shouldCreateUser() {

		String email = "kqart@icloud.com";
		User user = createTestUser(email);

		RegisterUserDto registerUserDto = new RegisterUserDto();

		registerUserDto.setRePassword(user.getPassword());
		registerUserDto.setEmail(email);
		registerUserDto.setPassword(user.getPassword());
		registerUserDto.setName(user.getName());
		registerUserDto.setSurname(user.getSurname());

		userService.createUser(registerUserDto);

		Assertions.assertNotNull(userRepository.findByEmail(email));

		userRepository.delete(userRepository.findByEmail(email).get());
	}

	private void addTestUser(String email) {
		RegisterUserDto registerUserDto = new RegisterUserDto();
		registerUserDto.setEmail(email);
		registerUserDto.setPassword("&ItsPassword123");
		registerUserDto.setRePassword("&ItsPassword123");
		registerUserDto.setName("Andrey");
		registerUserDto.setSurname("Pryahin");

		User user = userMapper.mapRegisterUserDtoToUserEntity(registerUserDto);

		userRepository.save(user);
	}

	private User createTestUser(String email) {
		User user = new User();
		user.setEmail(email);
		user.setPassword("&ItsPassword123");
		user.setName("Andrey");
		user.setSurname("Pryahin");

		return user;
	}


}
