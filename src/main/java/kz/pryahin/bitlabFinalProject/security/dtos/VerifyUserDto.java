package kz.pryahin.bitlabFinalProject.security.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerifyUserDto {
	@NotNull
	@Email
	private String email;
	@NotNull
	private String password;
	@NotNull
	private String code;
}
