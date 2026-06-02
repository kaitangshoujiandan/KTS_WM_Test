package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.LoginRequest;
import com.example.demo.entity.LoginResponse;
import com.example.demo.entity.Sysmenu;
import com.example.demo.entity.User;
import com.example.demo.service.SysmenuService;
import com.example.demo.service.UserService;
import com.example.demo.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private SysmenuService sysmenuService;   // 菜单服务

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 1. 验证用户名密码（明文比对，适配你现在的数据库）
        User user = userService.authenticate(loginRequest.getNo(), loginRequest.getPassword());
        if (user == null) {
            return ResponseEntity.status(401).body("用户名或密码错误");
        }

        // 2. 生成 JWT token
        String token = jwtUtils.generateToken(Long.valueOf(user.getId()), user.getNo());  // 假设用户账号字段是 no

        // 3. 查询菜单（复用旧登录逻辑）
        List<Sysmenu> menuList = null;
        if (sysmenuService != null && user.getRoleId() != null) {
            // 根据角色ID查询菜单（和原来的写法一致）
            menuList = sysmenuService.list(
                    new LambdaQueryWrapper<Sysmenu>()
                            .like(Sysmenu::getMenuRight, user.getRoleId())
            );
        }

        // 4. 封装返回数据（模仿旧接口的格式，方便前端不改）
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);
        data.put("menuList", menuList);

        // 如果你原来 Result 类有固定格式，可以改成 Result.success(data)
        return ResponseEntity.ok(data);
    }
}