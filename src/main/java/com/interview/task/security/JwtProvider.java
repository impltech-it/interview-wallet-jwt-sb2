package com.interview.task.security;

import com.interview.task.config.JwtProperties;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Class represents jwt provider implementation.
 */
@Component
public class JwtProvider {

    private final JwtProperties jwtProperties;

    @Autowired
    public JwtProvider(final JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * Method generate access token for certain user.
     *
     * @param userPrincipal user principal
     * @return access token
     */
    public String generateAccessToken(final UserPrincipal userPrincipal) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + jwtProperties.getExpirationTimeMs());
        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getUserId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretKey())
                .compact();
    }

    /**
     * Method get the access token from http request.
     *
     * @param request request
     * @return access token
     */
    public String getTokenFromRequest(final HttpServletRequest request) {
        final String jwt = request.getHeader("Authorization");
        if (StringUtils.hasText(jwt) && jwt.startsWith("Bearer ")) {
            return jwt.substring(7);
        }
        return null;
    }

    /**
     * Method get user id from access token.
     *
     * @param token access token.
     * @return user id
     */
    public Long getUserIdFromToken(final String token) {
        final Claims claims = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    /**
     * Method performs access token validation.
     *
     * @param token access token
     * @return true if token is valid, false otherwise
     */
    public Boolean validateToken(final String token) {
        try {
            Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException
                | UnsupportedJwtException | IllegalArgumentException ex) {
            return false;
        }
    }
}
