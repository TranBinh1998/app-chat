package com.vn.whatsapp.app.controller;

import com.vn.whatsapp.app.config.TokenProvider;
import com.vn.whatsapp.app.exception.UserException;
import com.vn.whatsapp.app.modal.User;
import com.vn.whatsapp.app.repository.UserRepository;
import com.vn.whatsapp.app.request.LoginRequest;
import com.vn.whatsapp.app.response.AuthResponse;
import com.vn.whatsapp.app.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserService customUserService;


    private TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
        String email = user.getEmail();
        String fullName = user.getFullName();
        String password =  user.getPassword();

        User isUser = userRepository.findByEmail(email);

        if (isUser != null) {
            throw new UserException("Email is used with another acount "+email); // Nếu tồn tại thì yêu cầu đăng nhập
        }
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFullName(fullName);
        createdUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication); // Tạo mới 1 token

        AuthResponse response = new AuthResponse(jwt, true);
        return new ResponseEntity<AuthResponse>(response, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password =  loginRequest.getPassword();

        Authentication authentication =authenticate(email, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        AuthResponse response = new AuthResponse(jwt, true);
        return new ResponseEntity<AuthResponse>(response, HttpStatus.ACCEPTED);
    }


    public Authentication authenticate (String username, String password) {
        //Mục đích của đoạn code này là để xác thực người dùng bằng tên đăng nhập và mật khẩu
        UserDetails userDetails = customUserService.loadUserByUsername(username);
        // Tìm kiếm đối tượng username

        if (userDetails==null) {
            throw new BadCredentialsException("Invalid UserName");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("invalid password or username");
        }
        // kiểm tra mật khẩu có khớp với người dùng trong cơ sở đữ liệu hay không


        // Xác thực đã thành công
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }



}
