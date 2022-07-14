package ru.example.auth.config.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.example.auth.exception.custom_exception.jwt_token.*;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtAccessTokenProvider {
    private final UserDetailsService userDetailsService;
    private String secretKey;
    private final long expirationInMs;

    @Autowired
    public JwtAccessTokenProvider(
            @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.access-expires-at}") long expirationInMs
    ) {
        this.userDetailsService = userDetailsService;
        this.secretKey = secretKey;
        this.expirationInMs = expirationInMs;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder()
                .encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(String username, String password) {
        final Claims claims = Jwts.claims().setSubject(username);
        claims.put("password", password);
        final Date dateNow = new Date();
        final Date dateExpiration = new Date(dateNow.getTime() + expirationInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(dateNow)
                .setExpiration(dateExpiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        final Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            // НЕ протух?
            return !claimsJws.getBody().getExpiration().before(new Date());

        } catch (SignatureException e) {
            throw new InvalidSignatureJwtTokenException();
        } catch (MalformedJwtException e) {
            throw new InvalidJwtTokenException();
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtTokenException();
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtTokenException();
        } catch (IllegalArgumentException e) {
            throw new EmptyClaimsJwtTokenException();
        }
    }

    public String resolveToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

    public Authentication getAuthentication(String token) {
        final String username = getUsername(token);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities()
        );
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
