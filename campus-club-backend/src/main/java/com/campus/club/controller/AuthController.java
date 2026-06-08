package com.campus.club.controller;

import com.campus.club.dto.LoginRequest;
import com.campus.club.dto.LoginResponse;
import com.campus.club.dto.RegisterRequest;
import com.campus.club.dto.Result;
import com.campus.club.entity.User;
import com.campus.club.service.AuthService;
import com.campus.club.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }

    @PostMapping("/register")
    public Result<User> register(@RequestBody RegisterRequest request) {
        return Result.success("注册成功", authService.register(request));
    }

    @GetMapping("/me")
    public Result<User> getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(token);
        return Result.success(authService.getCurrentUser(userId));
    }
}
