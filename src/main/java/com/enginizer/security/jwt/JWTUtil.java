package com.enginizer.security.jwt;

import com.enginizer.enums.TokenType;
import com.enginizer.model.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.*;

/**
 * Created by sorinavasiliu on 7/3/16.
 */
@Component
public class JWTUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_AUDIENCE = "audience";
    private static final String CLAIM_KEY_CREATED = "created";

    private static final String AUDIENCE_UNKNOWN = "unknown";
    private static final String AUDIENCE_WEB = "web";
    private static final String AUDIENCE_MOBILE = "mobile";
    private static final String AUDIENCE_TABLET = "tablet";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.forgotPasswordSecret}")
    private String forgotPasswordSecret;

    @Value("${jwt.forgotPasswordExpiration}")
    private Long forgotPasswordExpiration;

    public String getMailFromToken(JWTTokenHolder token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
         }
        catch (Exception e) {
            username = null;
          }
            return username;
    }


    public Date getExpirationDateFromToken(JWTTokenHolder token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public String getAudienceFromToken(JWTTokenHolder token) {
        String audience;
        try {
            final Claims claims = getClaimsFromToken(token);
            audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    public JWTTokenHolder generateAnonymosToken() {
        // generate token
        Claims claims = Jwts.claims().setSubject("anonymous@geemgi.com");
        claims.put("role", "ROLE_ANONYMOUS");
        claims.put("uuid", UUID.randomUUID().toString()); // set a uuid so that every time a token is generated it will be unique

        return new JWTTokenHolder(Jwts.builder()
                .setClaims(claims)
                .setExpiration(calculateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact());
    }

    public Boolean canTokenBeRefreshed(JWTTokenHolder token) {
        return !isTokenExpired(token) || ignoreTokenExpiration(token);
    }

    public String refreshToken(JWTTokenHolder token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(JWTTokenHolder token, String userName) {

        final String username = getMailFromToken(token);

        if(token.getTokenType()==TokenType.AUTH) {
            return (username.equals(userName)
                    && (!isTokenExpired(token) || ignoreTokenExpiration(token)));
        }

        return !isTokenExpired(token);
    }


    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String generateToken(UserDetails userDetails, Device device) {
        Map<String, Object> claims = generateClaims(userDetails.getUsername(),device);
        return generateToken(claims);
    }

    public String generateForgotPasswordToken(User user, Device device) {
        return Jwts.builder()
                .setClaims(generateClaims(user.getMail(),device))
                .setExpiration(new Date(System.currentTimeMillis() + forgotPasswordExpiration * 1000))
                .signWith(SignatureAlgorithm.HS256, forgotPasswordSecret)
                .compact();
    }

    private Map<String, Object> generateClaims(String email , Device device)
    {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, email);
        claims.put(CLAIM_KEY_AUDIENCE, generateAudience(device));
        claims.put(CLAIM_KEY_CREATED, new Date());

        return claims;
    }

    private Boolean isTokenExpired(JWTTokenHolder token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Claims getClaimsFromToken(JWTTokenHolder token) {
        Claims claims;
        String secretKey = "";
        try {

            switch (token.getTokenType()) {
                case AUTH:
                    secretKey = secret;
                    break;
                case PASSWORD:
                    secretKey = forgotPasswordSecret;
                    break;
            }

            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token.getToken())
                    .getBody();
        }
        catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private String generateAudience(Device device) {
        String audience = AUDIENCE_UNKNOWN;
        if (device.isNormal()) {
            audience = AUDIENCE_WEB;
        } else if (device.isTablet()) {
            audience = AUDIENCE_TABLET;
        } else if (device.isMobile()) {
            audience = AUDIENCE_MOBILE;
        }
        return audience;
    }

    private Boolean ignoreTokenExpiration(JWTTokenHolder token) {
        String audience = getAudienceFromToken(token);
        return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
    }

    private Date calculateExpirationDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, 100);
        return cal.getTime();
    }

}



