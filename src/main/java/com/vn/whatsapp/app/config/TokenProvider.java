package com.vn.whatsapp.app.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;


@Service
public class TokenProvider {
    SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    // tạo 1 khóa bị mật từ 1 chuỗi bí mật
    public String generateToken(Authentication authentication) {
        String jwt = Jwts.builder().setIssuer("Code with WhatsAppVn")
                .setIssuedAt(new Date()).setExpiration(new Date(new Date().getTime()+86400000))
                .claim("email", authentication.getName())
                .signWith(key)
                .compact();
        /*
        Tạo ra một token JWT cho người dùng, bằng cách mã hóa các thông tin về người dùng (như email) và
         các thông tin khác (như issuer, issuedAt, expiration) bằng thuật toán JSON Web Signature (JWS).
          Thuật toán JWS sử dụng khóa bí mật để tạo ra một chữ ký cho token JWT,
          để xác nhận rằng token JWT được tạo ra bởi máy chủ đáng tin cậy.
         */

        return jwt;
    }

    public String getEmailFromToken(String jwt) {
        jwt = jwt.substring(7);

        Claims claims  = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        String email = String.valueOf(claims.get("email"));

        return email;
    }





}
