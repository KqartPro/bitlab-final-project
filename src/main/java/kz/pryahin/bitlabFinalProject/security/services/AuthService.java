package kz.pryahin.bitlabFinalProject.security.services;


import kz.pryahin.bitlabFinalProject.security.dtos.RefreshTokenDto;
import kz.pryahin.bitlabFinalProject.security.dtos.TokensDto;
import kz.pryahin.bitlabFinalProject.security.dtos.UserCredentialsDto;

import javax.naming.AuthenticationException;

public interface AuthService {
	TokensDto login(UserCredentialsDto userCredentialsDto) throws AuthenticationException;

	TokensDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception;
}
