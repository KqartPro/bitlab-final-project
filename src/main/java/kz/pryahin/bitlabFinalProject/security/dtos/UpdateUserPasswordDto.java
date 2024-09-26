package kz.pryahin.bitlabFinalProject.security.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserPasswordDto {
	@NotNull
	private String password;
	@NotNull
	private String updatedPassword;
	@NotNull
	private String code;
}
