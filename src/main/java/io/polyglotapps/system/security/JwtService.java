package io.polyglotapps.system.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String createToken(final String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(createExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Optional<String> getUsernameFromToken(final String token) {
        try {
            final Claims claims = getClaimsFromToken(token);
            return Optional.of(claims.getSubject());
        } catch (Exception e) {
            throw new InvalidJwtException("Unable to parse token", e);
        }
    }

    public Date getExpirationDateFromToken(final String token) {
        try {
            final Claims claims = getClaimsFromToken(token);
            return claims.getExpiration();
        } catch (Exception e) {
            throw new InvalidJwtException("Unable to parse token", e);
        }
    }

    public boolean isTokenValid(final String token) {
        return !isTokenExpired(token);
    }

    private Boolean isTokenExpired(final String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(final Date created, final Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }


    private Date createExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Claims getClaimsFromToken(final String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new InvalidJwtException("Unable to parse token", e);
        }
    }

}
