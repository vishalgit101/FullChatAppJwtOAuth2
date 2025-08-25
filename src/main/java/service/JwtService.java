package service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private String secretKey = "";
	
	@Value("${spring.app.jwtSecret}")
	private String jwtSecret;

	public String generateToken(String username) {
		return Jwts.builder()
				.subject(username)
				//.claim("key", value)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 8))
				.signWith(getKey())
				.compact();
				
	}
	
	private Key getKey() {
		byte [] keyBytes = Decoders.BASE64.decode(this.jwtSecret);
		return Keys.hmacShaKeyFor(keyBytes);
	}


	public Claims extractAllClaims(String token) {
	    return Jwts
	        .parser()
	        .verifyWith((SecretKey) getKey())
	        .build()
	        .parseSignedClaims(token) // JJWT 0.12.x
	        .getPayload();            // Get claims body
	}


	public String extractUsername(String token) {
	    return extractAllClaims(token).getSubject();
	}

	
	public boolean validateToken(String token, UserDetails userDetails) {
	    final String username = extractUsername(token);
	    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	
	public Date extractExpiration(String token) {
	    return extractAllClaims(token).getExpiration();
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	    final Claims claims = extractAllClaims(token);
	    return claimsResolver.apply(claims);
	}


	
	public boolean isTokenExpired(String token) {
	    return extractExpiration(token).before(new Date());
	}


}
