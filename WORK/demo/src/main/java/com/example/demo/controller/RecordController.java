package com.example.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.entity.Record;
import com.example.demo.entity.RecordRes;
import com.example.demo.service.RecordService;
import com.QueryPageParam;
import com.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/record")
public class RecordController {

    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping("/save")
    public Result saveRecord(@RequestBody Record record) {
        boolean save = recordService.save(record);
        return save ? Result.success("记录保存成功！", null) : Result.fail("保存失败！");
    }

    // 分页查询（已改为联表查询，返回用户名、管理员名）
    @PostMapping("/listpage")
    public Result listPage(@RequestBody QueryPageParam query) {
        IPage<RecordRes> result = recordService.recordPage(query);
        return Result.success("查询成功", result.getTotal(), result.getRecords());
    }
}