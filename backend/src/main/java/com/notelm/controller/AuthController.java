package com.notelm.controller;

import com.notelm.dto.request.LoginRequest;
import com.notelm.dto.request.RegisterRequest;
import com.notelm.dto.response.ApiResponse;
import com.notelm.dto.response.LoginResponse;
import com.notelm.model.User;
import com.notelm.security.CustomUserDetailsService;
import com.notelm.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // 使用新的generateTokenWithUserId方法
            User user = userDetailsService.findByUsername(loginRequest.getUsername());
            if (user != null) {
                String token = jwtTokenProvider.generateTokenWithUserId(user.getId());
                LoginResponse response = new LoginResponse(token, "Login successful");
                return ResponseEntity.ok(ApiResponse.success(response));
            } else {
                LoginResponse response = new LoginResponse(null, "User not found");
                return ResponseEntity.ok(ApiResponse.error(response.getMessage()));
            }
        } catch (Exception e) {
            LoginResponse response = new LoginResponse(null, "Invalid credentials");
            return ResponseEntity.ok(ApiResponse.error(response.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        // 检查用户名和邮箱是否已存在
        if (userDetailsService.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.ok(ApiResponse.error("Username already exists"));
        }

        if (userDetailsService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.ok(ApiResponse.error("Email already exists"));
        }

        // 创建新用户
        User newUser = userDetailsService.createUser(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword())
        );

        return ResponseEntity.ok(ApiResponse.success("User registered successfully"));
    }
}