package kz.pryahin.bitlabFinalProject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import kz.pryahin.bitlabFinalProject.security.dtos.RefreshTokenDto;
import kz.pryahin.bitlabFinalProject.security.dtos.TokensDto;
import kz.pryahin.bitlabFinalProject.security.dtos.UserCredentialsDto;
import kz.pryahin.bitlabFinalProject.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;

	@Operation(summary = "Получение Jwt Token и Refresh Token")
	@PostMapping("/login")
	public ResponseEntity<TokensDto> login(@RequestBody UserCredentialsDto userCredentialsDto) {
		try {
			TokensDto tokensDto = authService.login(userCredentialsDto);
			return ResponseEntity.ok(tokensDto);

		} catch (AuthenticationException e) {
			throw new RuntimeException("Authentication failed" + e.getMessage());
		}
	}

	@Operation(summary = "Возвращает новый Jwt Token и делает Set Refresh токена, чтобы он продолжал свою работу")
	@PostMapping("/refresh")
	public TokensDto refresh(@RequestBody RefreshTokenDto refreshTokenDto) throws Exception {
		return authService.refreshToken(refreshTokenDto);
	}


}
