package kz.pryahin.bitlabFinalProject.security.services;

import kz.pryahin.bitlabFinalProject.security.dtos.TokensDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
	TokensDto generateAuthTokens(UserDetails userDetails);

	TokensDto refreshAuthToken(UserDetails userDetails, String refreshToken);

	String generateToken(UserDetails userDetails);

	String generateRefreshToken(UserDetails userDetails);

	String getEmailFromToken(String token);

	boolean validateJwtToken(String token);
}
