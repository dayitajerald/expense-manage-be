package com.expense.app.middleware;

import com.expense.app.entity.AuthEntity;
import com.expense.app.model.TokenModel;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final SecretKey SECRET_KEY = Jwts.SIG.HS512.key().build(); // Replace with your secret key
    private static final long EXPIRATION_TIME = 7200000; // 2 hours in milliseconds

    public String generateToken(AuthEntity user) {
        TokenModel token = new TokenModel();
        BeanUtils.copyProperties(user, token);
        return Jwts.builder()
                .subject(token.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public TokenModel getTokenModelfromToken(String token) {
        TokenModel tokenModel = new TokenModel();
        tokenModel.parse(Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build().parseSignedClaims(token.subSequence(0, token.length())).getPayload().getSubject());
        return tokenModel;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token.subSequence(0, token.length()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser().verifyWith(SECRET_KEY).build()
                .parseSignedClaims(token.subSequence(0, token.length())).getPayload().getExpiration();
        return expiration.before(new Date());
    }

    public Date getExpirationDate(String token) {
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token.subSequence(0, token.length()))
                .getPayload().getExpiration();
    }
}
