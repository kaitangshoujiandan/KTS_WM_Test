package com.example.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.entity.User;
import com.QueryPageParam;

public interface UserService extends com.baomidou.mybatisplus.extension.service.IService<User> {
    IPage<User> listPage(QueryPageParam param);
    User findByNo(String no);
}