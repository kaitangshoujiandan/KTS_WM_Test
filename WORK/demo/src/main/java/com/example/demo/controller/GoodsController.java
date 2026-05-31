package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entity.Goods;
import com.example.demo.service.GoodsService;
import com.QueryPageParam;
import com.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    // 保存
    @PostMapping("/save")
    public Result saveGoods(@RequestBody Goods goods) {
        boolean save = goodsService.save(goods);
        return save ? Result.success("保存成功！",null) : Result.fail("保存失败！");
    }

    // 修改
    @PostMapping("/update")
    public Result updateGoods(@RequestBody Goods goods) {
        boolean update = goodsService.updateById(goods);
        return update ? Result.success("更新成功！",null) : Result.fail("更新失败！");
    }

    // 分页查询
    @PostMapping("/listpage")
public Result listPage(@RequestBody QueryPageParam query) {
    Page<Goods> page = new Page<>(query.getPageNum(), query.getPageSize());
    LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();

    wrapper.orderByAsc(Goods::getId);
    // 货名模糊查询
    wrapper.like(query.getName() != null && !query.getName().isEmpty(), Goods::getName, query.getName());
    // 新增：仓库 精确匹配查询
    wrapper.eq(query.getStorage() != null && !query.getStorage().isEmpty(), Goods::getStorage, query.getStorage());
    // 新增：分类 精确匹配查询
    wrapper.eq(query.getGoodsType() != null && !query.getGoodsType().isEmpty(), Goods::getGoodsType, query.getGoodsType());

    IPage<Goods> result = goodsService.page(page, wrapper);
    return Result.success("查询成功", result.getTotal(), result.getRecords());
}

    // 删除
    @PostMapping("/delete")
    public Result deleteGoods(@RequestBody Goods goods) {
        boolean b = goodsService.removeById(goods.getId());
        return b ? Result.success("删除成功！",null) : Result.fail("删除失败！");
    }
}