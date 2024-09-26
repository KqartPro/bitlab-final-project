package kz.pryahin.bitlabFinalProject.security.services.impl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kz.pryahin.bitlabFinalProject.security.dtos.TokensDto;
import kz.pryahin.bitlabFinalProject.security.services.JwtService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {
	private static final Logger LOGGER = LogManager.getLogger(JwtServiceImpl.class);
	private final Map<String, Object> claims = new HashMap<>();

	@Value("66a949f28f9a99817083f984fd0304478af860ebbe939a9b1b38017b63dbfcbcf61d237a4c6b775fc7054e0696df66fb735e7abd25ee51ec95d8259ebbb4a531")
	private String jwtSecret;

	@Override
	public TokensDto generateAuthTokens(UserDetails userDetails) {
		TokensDto tokensDto = new TokensDto();
		tokensDto.setToken(generateToken(userDetails));
		tokensDto.setRefreshToken(generateRefreshToken(userDetails));
		return tokensDto;
	}

	public TokensDto refreshAuthToken(UserDetails userDetails, String refreshToken) {
		TokensDto tokensDto = new TokensDto();
		tokensDto.setToken(generateToken(userDetails));
		tokensDto.setRefreshToken(refreshToken);
		return tokensDto;
	}

	public String generateToken(UserDetails userDetails) {
		Date date = Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant());
		List<String> rolesList = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
		claims.put("roles", rolesList);
		return Jwts.builder()
			.subject(userDetails.getUsername())
			.claims(claims)
			.expiration(date)
			.signWith(getSign())
			.compact();

	}

	public String generateRefreshToken(UserDetails userDetails) {
		Date date = Date.from(LocalDateTime.now().plusHours(5).atZone(ZoneId.systemDefault()).toInstant());
		List<String> rolesList = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
		claims.put("roles", rolesList);
		return Jwts.builder()
			.subject(userDetails.getUsername())
			.claims(claims)
			.expiration(date)
			.signWith(getSign())
			.compact();

	}

	public boolean validateJwtToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(getSign())
				.build()
				.parseSignedClaims(token)
				.getPayload();
			return true;
		} catch (ExpiredJwtException expEx) {
			LOGGER.error("Expired JwtException", expEx);
		} catch (UnsupportedJwtException expEx) {
			LOGGER.error("Unsupported JwtException", expEx);
		} catch (MalformedJwtException expEx) {
			LOGGER.error("Malformed JwtException", expEx);
		} catch (SecurityException expEx) {
			LOGGER.error("Security Exception", expEx);
		} catch (Exception expEx) {
			LOGGER.error("invalid token", expEx);
		}
		return false;
	}

	public String getEmailFromToken(String token) {

		return Jwts.parser()
			.verifyWith(getSign())
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.getSubject();
	}

	private SecretKey getSign() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
