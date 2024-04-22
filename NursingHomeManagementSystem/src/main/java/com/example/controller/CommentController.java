package com.example.controller;

import com.example.common.Result;
import com.example.entity.Comment;
import com.example.service.CommentService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;


    @PostMapping("/add")
    public Result add(@RequestBody Comment comment) {
        commentService.add(comment);
        return Result.success();
    }


    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        commentService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deepDelete/{id}")
    public Result deepDelete(@PathVariable Integer id) {
        commentService.deepDelete(id);
        return Result.success();
    }


    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        commentService.deleteBatch(ids);
        return Result.success();
    }


    @PutMapping("/update")
    public Result updateById(@RequestBody Comment comment) {
        commentService.updateById(comment);
        return Result.success();
    }


    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Comment comment = commentService.selectById(id);
        return Result.success(comment);
    }


    @GetMapping("/selectAll")
    public Result selectAll(Comment comment ) {
        List<Comment> list = commentService.selectAll(comment);
        return Result.success(list);
    }


    @GetMapping("/selectPage")
    public Result selectPage(Comment comment,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Comment> page = commentService.selectPage(comment, pageNum, pageSize);
        return Result.success(page);
    }


    @GetMapping("/selectTree/{fid}/{module}")
    public Result selectTree(@PathVariable Integer fid, @PathVariable String module) {
        List<Comment> list = commentService.selectTree(fid, module);
        return Result.success(list);
    }


    @GetMapping("/selectCount/{fid}/{module}")
    public Result selectCount(@PathVariable Integer fid, @PathVariable String module) {
        Integer count = commentService.selectCount(fid, module);
        return Result.success(count);
    }

}