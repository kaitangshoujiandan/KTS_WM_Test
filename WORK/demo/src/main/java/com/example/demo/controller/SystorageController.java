package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entity.Systorage;
import com.example.demo.service.SystorageService;
import com.QueryPageParam;
import com.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

@RestController
@RequestMapping("/systorage")
public class SystorageController {

    private final SystorageService systorageService;

    public SystorageController(SystorageService systorageService) {
        this.systorageService = systorageService;
    }

    // 下拉列表专用
    @GetMapping("/listAll")
    public Result listAll(){
        List<Systorage> list = systorageService.list();
        return Result.success("success",list);
    }

    @PostMapping("/save")
    public Result saveSystorage(@RequestBody Systorage systorage) {
        return systorageService.save(systorage) ? Result.success("保存成功！", 200) : Result.fail("保存失败！");
    }

    @PostMapping("/update")
    public Result updateSystorage(@RequestBody Systorage systorage) {
        return systorageService.updateById(systorage) ? Result.success("更新成功！", null) : Result.fail("更新失败！");
    }

    @PostMapping("/listpage")
    public Result listPage(@RequestBody QueryPageParam query) {
        Page<Systorage> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Systorage> wrapper = new LambdaQueryWrapper<>();

        wrapper.orderByAsc(Systorage::getId);
        wrapper.like(query.getName() != null, Systorage::getName, query.getName());

        IPage<Systorage> result = systorageService.page(page, wrapper);
        return Result.success("查询成功", result.getTotal(), result.getRecords());
    }

    @PostMapping("/delete")
    public Result deleteSystorage(@RequestBody Systorage systorage) {
        boolean b = systorageService.removeById(systorage.getId());
        return b ? Result.success("删除成功！", null) : Result.fail("删除失败！");
    }
}