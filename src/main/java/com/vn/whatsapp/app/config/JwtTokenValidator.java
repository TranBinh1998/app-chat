package com.vn.whatsapp.app.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.security.sasl.SaslClient;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator  extends OncePerRequestFilter {

//    Đoạn code này để tạo một lớp JwtTokenValidator kế thừa từ lớp OncePerRequestFilter của Spring Security. Lớp này là một bộ lọc (filter) được sử dụng để xác thực và xác thực token JWT trong các yêu cầu HTTP đến máy chủ ứng dụng
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("Authorization");

        if (jwt != null) {
            try {
                // Bear token
                jwt = jwt.substring(7);
                // lấy  token jwt từ tiêu đề. lấy 7 ký tự đầu tiên

                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes()); // Tạo một khóa bí mật (SecretKey) từ một chuỗi bí mật (JwtConstant.SECRET_KEY) bằng cách sử dụng thuật toán HMAC-SHA.
                Claims claims  = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
                String username = String.valueOf(claims.get("username"));
                String authorities = String.valueOf(claims.get("authorities"));
//                Giải mã token JWT bằng cách sử dụng khóa bí mật và lấy các thông tin được chứa trong token, gọi là claims.
//                Các thông tin này bao gồm tên người dùng (username) và các quyền truy cập (authorities).

                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                // Tạo một danh sách các quyền truy cập (GrantedAuthority) từ chuỗi authorities bằng cách sử dụng phương thức AuthorityUtils.commaSeparatedStringToAuthorityList.
                Authentication authentication = new UsernamePasswordAuthenticationToken(username,null, auths);

                // Đặt đối tượng xác thực vào SecurityContextHolder,
                // để Spring Security có thể sử dụng nó để kiểm soát quyền truy cập cho các yêu cầu tiếp theo.
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }catch (Exception e) {
                throw  new BadCredentialsException("Invalid token recieved...");
            }
        }
        filterChain.doFilter(request, response);
    }
}
