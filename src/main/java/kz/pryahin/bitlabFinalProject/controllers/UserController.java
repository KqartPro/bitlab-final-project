package kz.pryahin.bitlabFinalProject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import kz.pryahin.bitlabFinalProject.security.dtos.*;
import kz.pryahin.bitlabFinalProject.security.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	private final UserService userService;

	// Get

	@Operation(summary = "Получить текущего User (principal)", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/get")
	public GetUserDto getUser(Principal principal) {
		return userService.getUserDto(principal.getName());
	}

	@Operation(summary = "Повторно отправляет код для верификации пользователя User на почту")
	@GetMapping("/resend-verify-code")
	public String resendVerifyCode(@Valid @RequestBody UserEmailDto userEmailDto) {
		return userService.resendVerifyCode(userEmailDto);
	}

	@Operation(summary = "Отправляет на почту код для смены пароля", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/send-update-password-code")
	public String sendCodeForPasswordUpdate(@Valid @RequestBody UserEmailDto userEmailDto) {
		return userService.sendCodeToUpdatePassword(userEmailDto);
	}

	@Operation(summary = "Отправляет на почту код для удаления User", security = @SecurityRequirement(name =
		"bearerAuth"))
	@GetMapping("/send-delete-user-code")
	public String sendDeleteUserCode(@Valid @RequestBody UserCredentialsDto userCredentialsDto) throws AuthenticationException {
		return userService.sendDeleteUserCode(userCredentialsDto);
	}

	// Post

	@Operation(summary = "Создает (регистрирует) нового пользователя User")
	@PostMapping("/register")
	public String createUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
		return userService.createUser(registerUserDto);
	}

	@Operation(summary = "User получает статус verify = true")
	@PostMapping("/verify")
	public String verifyUser(@Valid @RequestBody VerifyUserDto verifyUserDto) {
		return userService.verifyUser(verifyUserDto);
	}

	@Operation(summary = "Позволяет восстановить пользователя User после удаления в течении 30 дней")
	@PostMapping("/restore")
	public String restoreUser(@Valid @RequestBody UserCredentialsDto userCredentialsDto) throws AuthenticationException {
		return userService.restoreUser(userCredentialsDto);
	}
	// Put

	@Operation(summary = "Изменяет пароль пользователя User", security = @SecurityRequirement(name =
		"bearerAuth"))
	@PutMapping("/update-password")
	public String updateUserPassword(@Valid @RequestBody UpdateUserPasswordDto updateUserPasswordDto,
	                                 Principal principal) {
		return userService.updateUserPassword(updateUserPasswordDto, principal);
	}

	@Operation(summary = "Изменяет имя (если NotNull) или фамилию (если NotNull), или же сразу два параметра если оба (NotNull)")
	@PatchMapping("/update-full-name")
	public String updateFullName(@RequestBody UserFullNameDto userFullNameDto, Principal principal) {
		return userService.updateUserFullName(userFullNameDto, principal);
	}

	// Delete

	@Operation(summary = "Удаляет пользователя User с возможностью восстановления в течении 30 дней", security =
	@SecurityRequirement(name =
		"bearerAuth"))
	@DeleteMapping("/delete")
	public String sendDeleteUserCode(@Valid @RequestBody VerifyUserDto verifyUserDto) throws AuthenticationException {
		return userService.deleteUser(verifyUserDto);
	}


}
