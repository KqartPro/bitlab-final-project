package kz.pryahin.bitlabFinalProject.security.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TokensDto {
	@NotNull
	private String token;
	@NotNull
	private String refreshToken;
}
