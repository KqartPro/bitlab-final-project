package kz.pryahin.bitlabFinalProject.security.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCredentialsDto {
	@NotNull
	@Email
	private String email;
	@NotNull
	private String password;
}
