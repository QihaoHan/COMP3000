package com.example.controller;

import cn.hutool.core.lang.Dict;
import com.example.common.Result;
import com.example.entity.ActivitySign;
import com.example.service.ActivitySignService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/activitySign")
public class ActivitySignController {

    @Resource
    private ActivitySignService activitySignService;


    @PostMapping("/add")
    public Result add(@RequestBody ActivitySign activitySign) {
        activitySignService.add(activitySign);
        return Result.success();
    }


    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        activitySignService.deleteById(id);
        return Result.success();
    }


    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        activitySignService.deleteBatch(ids);
        return Result.success();
    }


    @PutMapping("/update")
    public Result updateById(@RequestBody ActivitySign activitySign) {
        activitySignService.updateById(activitySign);
        return Result.success();
    }


    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        ActivitySign activitySign = activitySignService.selectById(id);
        return Result.success(activitySign);
    }


    @GetMapping("/selectAll")
    public Result selectAll(ActivitySign activitySign) {
        List<ActivitySign> list = activitySignService.selectAll(activitySign);
        return Result.success(list);
    }


    @GetMapping("/selectPage")
    public Result selectPage(ActivitySign activitySign,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ActivitySign> page = activitySignService.selectPage(activitySign, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/selectCount")
    public Result selectCount() {
        List<Dict> list = activitySignService.selectCount();
        return Result.success(list);
    }


}