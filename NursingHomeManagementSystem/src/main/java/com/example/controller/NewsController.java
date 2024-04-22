package com.example.controller;

import com.example.common.Result;
import com.example.entity.News;
import com.example.service.NewsService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/news")
public class NewsController {

    @Resource
    private NewsService newsService;


    @PostMapping("/add")
    public Result add(@RequestBody News news) {
        newsService.add(news);
        return Result.success();
    }


    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        newsService.deleteById(id);
        return Result.success();
    }


    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        newsService.deleteBatch(ids);
        return Result.success();
    }


    @PutMapping("/update")
    public Result updateById(@RequestBody News news) {
        newsService.updateById(news);
        return Result.success();
    }

    @PutMapping("/updateCount/{id}")
    public Result updateCount(@PathVariable Integer id) {
        newsService.updateCount(id);
        return Result.success();
    }


    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        News news = newsService.selectById(id);
        return Result.success(news);
    }


    @GetMapping("/selectAll")
    public Result selectAll(News news ) {
        List<News> list = newsService.selectAll(news);
        return Result.success(list);
    }


    @GetMapping("/selectPage")
    public Result selectPage(News news,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<News> page = newsService.selectPage(news, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/selectTopNews")
    public Result selectTopNews(@RequestParam String sort) {
        List<News> list = newsService.selectTopNews(sort);
        return Result.success(list);
    }

}