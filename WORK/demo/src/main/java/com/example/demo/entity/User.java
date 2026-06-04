package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@TableName("Sysuser") // 👈 加上 dbo. 前缀
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String no;
    private String name;
    @JsonIgnore
    private String password;
    private Integer age;
    private Integer sex;
    private String phone;
    @TableField("role_id")
    private Integer roleId;
    private String isvalid; // 注意：数据库是 isValid，实体类建议用 isvalid 或 @TableField 映射
}