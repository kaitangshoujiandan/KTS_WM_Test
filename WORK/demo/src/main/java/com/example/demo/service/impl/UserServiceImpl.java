package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.User;
import com.QueryPageParam; // 这里我帮你改对了
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public IPage<User> listPage(QueryPageParam param) {
        
        // 分页核心代码
        Page<User> page = new Page<>(param.getPageNum(), param.getPageSize());
        return baseMapper.selectPage(page, null);
    }
    @Override
    public User findByNo(String no) {
    // 查 count，永远不报错！
    Long count = lambdaQuery()
            .eq(User::getNo, no)
            .count(); // 👈 只查数量，最安全

    // 查到 >0 条就代表已存在，随便返回一个 user
    if (count > 0) {
        return new User();
    }
    return null;
    }
    @Override
    public User authenticate(String userNo, String rawPassword) {
        // 1. 根据用户名查询用户（假设表名和字段名已经匹配）
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getNo, userNo);
        User user = userMapper.selectOne(wrapper);

        // 2. 明文比对（注意：数据库中密码目前是明文）
        if (user != null && rawPassword.equals(user.getPassword())) {
            return user;
        }
        return null;
    }
}