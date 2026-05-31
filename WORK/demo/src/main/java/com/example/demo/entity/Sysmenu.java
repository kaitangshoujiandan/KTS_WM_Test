package com.example.demo.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Sysmenu")
public class Sysmenu {

    // 主键 ID → 只保留这一个 @TableId
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("menuCode")
    private String menuCode;

    @TableField("menuName")
    private String menuName;

    @TableField("menuLevel")
    private String menuLevel;

    @TableField("menuParentCode")
    private String menuParentCode;

    @TableField("menuClick")
    private String menuClick;

    @TableField("menuRight")
    private String menuRight;

    @TableField("menuComponent")
    private String menuComponent;

    @TableField("menuIcon")
    private String menuIcon;

     @TableField(exist = false)
    private List<Sysmenu> children;

    @TableField(exist = false)
    private Boolean hasChildren;
}