package com.example.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Record;
import com.example.demo.entity.RecordRes;
import com.QueryPageParam;

public interface RecordService extends IService<Record> {

    IPage<RecordRes> recordPage(QueryPageParam query);
}