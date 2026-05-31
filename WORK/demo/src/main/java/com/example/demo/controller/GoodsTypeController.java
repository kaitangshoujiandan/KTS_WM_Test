package com.example.demo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entity.GoodsType;
import com.example.demo.service.GoodsTypeService;
import com.QueryPageParam;
import com.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@RestController
@RequestMapping("/goodsType")
public class GoodsTypeController {

    private final GoodsTypeService goodsTypeService;

    public GoodsTypeController(GoodsTypeService goodsTypeService) {
        this.goodsTypeService = goodsTypeService;
    }

    @GetMapping("/listAll")
    public Result listAll(){
        List<GoodsType> list = goodsTypeService.list();
        return Result.success("success",list);
    }

    @PostMapping("/save")
    public Result saveGoodsType(@RequestBody GoodsType goodsType) {
        return goodsTypeService.save(goodsType) ? Result.success("保存成功！", 200) : Result.fail("保存失败！");
    }

    @PostMapping("/update")
    public Result updateGoodsType(@RequestBody GoodsType goodsType) {
        return goodsTypeService.updateById(goodsType) ? Result.success("更新成功！", null) : Result.fail("更新失败！");
    }

    @PostMapping("/listpage")
    public Result listPage(@RequestBody QueryPageParam query) {
        Page<GoodsType> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<GoodsType> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(GoodsType::getId);
        wrapper.like(query.getName() != null, GoodsType::getName, query.getName());
        IPage<GoodsType> result = goodsTypeService.page(page, wrapper);
        return Result.success("查询成功", result.getTotal(), result.getRecords());
    }

    @PostMapping("/delete")
    public Result deleteGoodsType(@RequestBody GoodsType goodsType) {
        boolean b = goodsTypeService.removeById(goodsType.getId());
        return b ? Result.success("删除成功！", null) : Result.fail("删除失败！");
    }
}