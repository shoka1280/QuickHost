package com.Project.QuickHost.Security;


import com.Project.QuickHost.Entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.security.SecureRandom;
import java.util.*;


import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.Keys.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;


@Service
public class JWTservice {
    @Value("${jwt.secretKey}")//for secrecy we dont hardcode
    private String jwtsecretKey;

    /*The getSecretKey() method converts the jwtsecretKey string (injected from application properties) into a SecretKey
     object using HMAC SHA, which is required for signing and verifying JWT tokens
     */
    private SecretKey getSecretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(jwtsecretKey);
        return Keys.hmacShaKeyFor(decodedKey);//This is used to sign or verify JWT tokens with HMAC algorithms (like HS256). The method ensures the key is of a valid length for the algorithm.
    }

    public String generateAccessToken(User user) {


        return Jwts.builder()
                .subject(user.getId().toString())//claim that typically represents the principal or user the token refers to
                .claim("email", user.getEmail())//key value pair
                .claim("roles", user.getRoles().toString())//JWT, a claim is a key-value pair in the token payload that carries information about the user or context.
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 10000L * 6000*200))
                .signWith(getSecretKey()) // JWT signature create by them(secretKey)
                .compact(); //compact is used to convert the token into string
    }
    //signature ensure that token was acutally create by user ,payload hasnt tampered with
    //For validating the token,getting user id
    public String generateRefreshToken(User user) {


        return Jwts.builder()
                .subject(user.getId().toString())//laim that typically represents the principal or user the token refers to
                //JWT, a claim is a key-value pair in the token payload that carries information about the user or context.
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60*60*24*30*6))
                .signWith(getSecretKey()) // JWT signature create by them(secretKey)
                .compact(); //compact is used to convert the token into string
    }
    //

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()//take raw token and intprets its structure(used for reading jwt string)(read,validate;decode:email,roles,id etc,; retun the structure data)
                .verifyWith(getSecretKey()) // Accepts javax.crypto.SecretKey,ensures the token has not been tampered with,Set the key to verify signature,“Use this secret key to check if the token’s signature is valid.”
                .build() //.bu    return ResponseEntity.ok(token);ild() method is used to finalize the parser configuration and create a parser instance that can actually parse and validate JWTs.
                .parseSignedClaims(token)// // 3. Parse + verify the token,Jwt<Header, Claims>(invalid if tampered or incorrect)
                .getPayload(); // p//payLoad cointans subject ,issue,claims at etc

        return Long.valueOf(claims.getSubject());
    }
    public void  KeyGenerator() {

        byte[] keyBytes = new byte[64]; // 512 bits
        new SecureRandom().nextBytes(keyBytes);
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println(base64Key);

    }






}
