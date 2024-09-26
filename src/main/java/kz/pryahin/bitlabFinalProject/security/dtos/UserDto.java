package kz.pryahin.bitlabFinalProject.security.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto {
	@NotBlank
	private String name;
	@NotBlank
	private String surname;
	@Email
	@NotNull
	private String email;
	@NotNull
	private String password;
	@NotNull
	private String rePassword;
	private String code;
	private boolean verificationStatus;
}
