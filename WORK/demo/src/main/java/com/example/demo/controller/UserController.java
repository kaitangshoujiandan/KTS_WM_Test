package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.QueryPageParam;
import com.Result;
import com.example.demo.entity.Sysmenu;
import com.example.demo.entity.User;
import com.example.demo.service.SysmenuService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SysmenuService sysmenuService;

    @GetMapping("/hello")
    public String hello() {
        return "项目启动成功！";
    }

    // 查询所有
    @GetMapping("/user/list")
    public List<User> getUserList() {
        return userService.list();
    }

    // 判断账号是否存在
    @GetMapping("/user/findByNo")
    public Result findByNo(@RequestParam String no) {
        User user = userService.findByNo(no);
        if (user == null) {
            return Result.success("成功", null);
        } else {
            return Result.fail("失败");
        }
    }

    // 保存
    @PostMapping("/save")
    public Result saveUserList(@RequestBody User user) {
        return userService.save(user) ? Result.success("保存成功！", 200) : Result.fail("保存失败！");
    }

    // 修改
    @PostMapping("/update")
    public Result updateUserList(@RequestBody User user) {
        return userService.updateById(user) ? Result.success("更新成功！", null) : Result.fail("更新失败！");
    }

    // 新增或修改
    @PostMapping("/saveOrUpdate")
    public boolean saveOrUpdateUser(@RequestBody User user) {
        return userService.saveOrUpdate(user);
    }

    // 删除
    @PostMapping("/delete")
    public boolean deleteUserList(@RequestBody User user) {
        return userService.removeById(user);
    }

    // 登录
//    @PostMapping("/login")
//    public Result login(@RequestBody User user) {
//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(User::getNo, user.getNo());
//        wrapper.eq(User::getPassword, user.getPassword());
//        User result = userService.getOne(wrapper);
//
//        if (result != null) {
//            LambdaQueryWrapper<Sysmenu> menuWrapper = new LambdaQueryWrapper<>();
//            menuWrapper.like(Sysmenu::getMenuRight, result.getRoleId());
//
//            List<Sysmenu> menuList = sysmenuService.list(menuWrapper);
//
//            Map<String, Object> responseData = new HashMap<>();
//            responseData.put("user", result);
//            responseData.put("menuList", menuList);
//
//            return Result.success("登录成功！", responseData);
//        } else {
//            return Result.fail("登录失败！");
//        }
//    }

    // 分页查询（已修复）
    @PostMapping("/listpage")
    public Result listPage(@RequestBody QueryPageParam query) {
        Page<User> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        wrapper.orderByAsc(User::getId);
        wrapper.like(query.getName() != null, User::getName, query.getName());
        wrapper.eq(query.getSex() != null, User::getSex, query.getSex());

        // ====================== 修复 roleId 过滤 ======================
        if (query.getRoleId() != null) {
            if (query.getRoleId() instanceof List) {
                wrapper.in(User::getRoleId, (List<?>) query.getRoleId());
            } else {
                wrapper.eq(User::getRoleId, query.getRoleId());
            }
        }

        IPage<User> result = userService.page(page, wrapper);
        return Result.success("查询成功", result.getTotal(), result.getRecords());
    }
}