package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Record;
import com.example.demo.entity.RecordRes;
import com.QueryPageParam;
import org.apache.ibatis.annotations.Param;

public interface RecordMapper extends BaseMapper<Record> {

    // 只改这一行！完全适配你的现有结构
    IPage<RecordRes> selectRecordPage(Page<RecordRes> page, @Param("query") QueryPageParam query);

}