package com.example.controller;

import com.example.common.Result;
import com.example.entity.Activity;
import com.example.service.ActivityService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Resource
    private ActivityService activityService;


    @PostMapping("/add")
    public Result add(@RequestBody Activity activity) {
        activityService.add(activity);
        return Result.success();
    }


    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        activityService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        activityService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Activity activity) {
        activityService.updateById(activity);
        return Result.success();
    }


    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Activity activity = activityService.selectById(id);
        return Result.success(activity);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Activity activity ) {
        List<Activity> list = activityService.selectAll(activity);
        return Result.success(list);
    }


    @GetMapping("/selectPage")
    public Result selectPage(Activity activity,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Activity> page = activityService.selectPage(activity, pageNum, pageSize);
        return Result.success(page);
    }

}
