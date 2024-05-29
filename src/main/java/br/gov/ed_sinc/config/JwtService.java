package br.gov.ed_sinc.config;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.gov.ed_sinc.model.Usuario;
import br.gov.ed_sinc.service.UsuarioService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
	
	@Autowired
	@Lazy
	UsuarioService usuarioService;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		Optional <Usuario> usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
		
		if (usuario.isPresent()) {
		    extraClaims.put("categoria", usuario.get().getCategoria());
		} else {
		    extraClaims.put("categoria", "COM");
		}
		 
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				// Tempo de Expiração do Token (+ 1000 * 60 * 10) = 10minutos
				.setExpiration(new Date(System.currentTimeMillis() + 48000 * 60 * 60))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String extractUsernameWithoutValidation(String token) {
	    String[] splitToken = token.split("\\.");
	    String unsignedToken = splitToken[1];
	    String decodedUnsignedToken = new String(Base64.getDecoder().decode(unsignedToken));
	    Map<String, Object> jwtClaims = new Gson().fromJson(decodedUnsignedToken, Map.class);
	    return jwtClaims.get("sub").toString();
	}
}