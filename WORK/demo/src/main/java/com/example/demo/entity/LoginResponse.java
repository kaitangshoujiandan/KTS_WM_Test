package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String name;
    private Long userId;
    private String no;
    private Integer sex;
    private String phone;
    private Integer roleId;
//get/set全加上
}