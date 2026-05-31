package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Record;
import com.example.demo.entity.RecordRes;
import com.example.demo.mapper.RecordMapper;
import com.example.demo.service.RecordService;
import com.QueryPageParam;
import org.springframework.stereotype.Service;

@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    private final RecordMapper recordMapper;

    public RecordServiceImpl(RecordMapper recordMapper) {
        this.recordMapper = recordMapper;
    }

    @Override
    public IPage<RecordRes> recordPage(QueryPageParam query) {
        Page<RecordRes> page = new Page<>(query.getPageNum(), query.getPageSize());
        
        // 👇 这里自动接收前端传的 userId，传给 Mapper
        return recordMapper.selectRecordPage(page, query);
    }
}