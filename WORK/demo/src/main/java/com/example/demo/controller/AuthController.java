package com.example.demo.controller;

import com.example.demo.entity.LoginRequest;
import com.example.demo.entity.LoginResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 1. 调用 Service 验证用户名和密码（明文比对）
        User user = userService.authenticate(loginRequest.getNo(), loginRequest.getPassword());

        // 2. 认证失败返回 401
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("账号或密码错误");
        }

        // 3. 认证成功，生成 JWT token
        String token = jwtUtils.generateToken(Long.valueOf(user.getId()), user.getName());

        // 4. 返回 token 及用户信息（密码不要返回）
        LoginResponse response = new LoginResponse(token, user.getName(), Long.valueOf(user.getId()));
        return ResponseEntity.ok(response);
    }

    // 可选：测试接口，验证 JWT 是否有效（需要带 token 访问）
    @GetMapping("/test")
    public String test() {
        return "认证成功，Token 有效";
    }
}