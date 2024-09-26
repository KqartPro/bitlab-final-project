package kz.pryahin.bitlabFinalProject.security.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserEmailDto {
	@NotNull
	@Email
	private String email;
}
