package kz.pryahin.bitlabFinalProject.security.services.impl;

import kz.pryahin.bitlabFinalProject.security.dtos.RefreshTokenDto;
import kz.pryahin.bitlabFinalProject.security.dtos.TokensDto;
import kz.pryahin.bitlabFinalProject.security.dtos.UserCredentialsDto;
import kz.pryahin.bitlabFinalProject.security.services.AuthService;
import kz.pryahin.bitlabFinalProject.security.services.JwtService;
import kz.pryahin.bitlabFinalProject.security.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserService userService;
	private final JwtService jwtService;

	@Override
	public TokensDto login(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
		UserDetails user = userService.getUserByCredentials(userCredentialsDto);
		return jwtService.generateAuthTokens(user);
	}

	@Override
	public TokensDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception {
		String refreshToken = refreshTokenDto.getRefreshToken();
		if (refreshToken != null && jwtService.validateJwtToken(refreshToken)) {
			UserDetails user = userService.loadUserByUsername(jwtService.getEmailFromToken(refreshToken));
			return jwtService.refreshAuthToken(user, refreshToken);
		}
		throw new AuthenticationException("Invalid refresh token");
	}
}
