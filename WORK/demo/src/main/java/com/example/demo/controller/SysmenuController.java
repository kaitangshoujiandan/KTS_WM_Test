package com.example.demo.controller;

import com.example.demo.entity.Sysmenu;
import com.example.demo.service.SysmenuService;
import com.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sysmenu")
public class SysmenuController {

    @Autowired
    private SysmenuService sysmenuService;

    // 查询所有菜单（你原来的）
    @GetMapping("/list")
    public Result list() {
        List<Sysmenu> list = sysmenuService.list();
        return Result.success("成功", list);
    }

    // ======================== ✅ 核心：动态侧边栏菜单接口 ========================
    @GetMapping("/getUserMenu")
    public Result getUserMenu() {
        // 1. 查出所有菜单
        List<Sysmenu> allMenus = sysmenuService.list();

        // 2. 筛选一级菜单（parentCode = 0 或 null）
        List<Sysmenu> rootMenus = allMenus.stream()
                .filter(menu -> menu.getMenuParentCode() == null || menu.getMenuParentCode().equals("0"))
                .collect(Collectors.toList());

        // 3. 给一级菜单设置子菜单
        for (Sysmenu root : rootMenus) {
            List<Sysmenu> children = allMenus.stream()
                    .filter(menu -> root.getMenuCode().equals(menu.getMenuParentCode()))
                    .collect(Collectors.toList());

            root.setChildren(children);
            root.setHasChildren(!children.isEmpty());
        }

        return Result.success("获取菜单成功", rootMenus);
    }
}