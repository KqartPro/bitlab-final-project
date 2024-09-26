package kz.pryahin.bitlabFinalProject.security.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class RefreshTokenDto {
	@NotNull
	private String refreshToken;
}
