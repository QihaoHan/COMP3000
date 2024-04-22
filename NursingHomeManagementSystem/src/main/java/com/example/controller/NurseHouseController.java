package com.example.controller;

import com.example.common.Result;
import com.example.entity.NurseHouse;
import com.example.service.NurseHouseService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/nurseHouse")
public class NurseHouseController {

    @Resource
    private NurseHouseService nurseHouseService;


    @PostMapping("/add")
    public Result add(@RequestBody NurseHouse nurseHouse) {
        nurseHouseService.add(nurseHouse);
        return Result.success();
    }


    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        nurseHouseService.deleteById(id);
        return Result.success();
    }


    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        nurseHouseService.deleteBatch(ids);
        return Result.success();
    }


    @PutMapping("/update")
    public Result updateById(@RequestBody NurseHouse nurseHouse) {
        nurseHouseService.updateById(nurseHouse);
        return Result.success();
    }

    @PutMapping("/updateCount/{id}")
    public Result updateCount(@PathVariable Integer id) {
        nurseHouseService.updateCount(id);
        return Result.success();
    }



    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        NurseHouse nurseHouse = nurseHouseService.selectById(id);
        return Result.success(nurseHouse);
    }


    @GetMapping("/selectAll")
    public Result selectAll(NurseHouse nurseHouse ) {
        List<NurseHouse> list = nurseHouseService.selectAll(nurseHouse);
        return Result.success(list);
    }


    @GetMapping("/selectPage")
    public Result selectPage(NurseHouse nurseHouse,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<NurseHouse> page = nurseHouseService.selectPage(nurseHouse, pageNum, pageSize);
        return Result.success(page);
    }

}