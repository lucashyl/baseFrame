package com.lucas.admin.util;


import com.lucas.shop.entity.UserToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 */
public class JwtUtils {
    public static String generateToken(UserToken userToken, int expire){
        String token = Jwts.builder()
                .claim(Constants.CONTEXT_USER_ID, userToken.getMemberId())
                .claim(Constants.CONTEXT_NAME, userToken.getUserName())
                .claim(Constants.RENEWAL_TIME,new Date(System.currentTimeMillis()+expire))
                .setExpiration(new Date(System.currentTimeMillis()+expire))
                .signWith(SignatureAlgorithm.HS256, Constants.JWT_PRIVATE_KEY)
                .compact();
        return token;
    }


    public static UserToken getInfoFromToken(String token) throws Exception {
        Claims claims = Jwts.parser()
                .setSigningKey(Constants.JWT_PRIVATE_KEY).parseClaimsJws(token)
                .getBody();
        return new UserToken(Long.valueOf(claims.get(Constants.CONTEXT_USER_ID).toString()),claims.get(Constants.CONTEXT_NAME).toString());
    }

}
