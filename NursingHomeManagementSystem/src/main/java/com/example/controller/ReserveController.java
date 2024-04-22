package com.example.controller;

import cn.hutool.core.lang.Dict;
import com.example.common.Result;
import com.example.entity.Reserve;
import com.example.service.ReserveService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/reserve")
public class ReserveController {

    @Resource
    private ReserveService reserveService;


    @PostMapping("/add")
    public Result add(@RequestBody Reserve reserve) {
        reserveService.add(reserve);
        return Result.success();
    }


    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        reserveService.deleteById(id);
        return Result.success();
    }


    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        reserveService.deleteBatch(ids);
        return Result.success();
    }


    @PutMapping("/update")
    public Result updateById(@RequestBody Reserve reserve) {
        reserveService.updateById(reserve);
        return Result.success();
    }


    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Reserve reserve = reserveService.selectById(id);
        return Result.success(reserve);
    }


    @GetMapping("/selectAll")
    public Result selectAll(Reserve reserve) {
        List<Reserve> list = reserveService.selectAll(reserve);
        return Result.success(list);
    }


    @GetMapping("/selectPage")
    public Result selectPage(Reserve reserve,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Reserve> page = reserveService.selectPage(reserve, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/selectCount")
    public Result selectCount() {
        List<Dict> list = reserveService.selectCount();
        return Result.success(list);
    }


}