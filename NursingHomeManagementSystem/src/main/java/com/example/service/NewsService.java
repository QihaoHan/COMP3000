package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.News;
import com.example.mapper.NewsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class NewsService {

    @Resource
    private NewsMapper newsMapper;


    public void add(News news) {
        news.setTime(DateUtil.now());
        newsMapper.insert(news);
    }


    public void deleteById(Integer id) {
        newsMapper.deleteById(id);
    }


    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            newsMapper.deleteById(id);
        }
    }


    public void updateById(News news) {
        newsMapper.updateById(news);
    }


    public News selectById(Integer id) {
        return newsMapper.selectById(id);
    }


    public List<News> selectAll(News news) {
        return newsMapper.selectAll(news);
    }


    public PageInfo<News> selectPage(News news, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<News> list = newsMapper.selectAll(news);
        return PageInfo.of(list);
    }

    public List<News> selectTopNews(String sort) {
        return newsMapper.selectTopNews(sort);
    }

    public void updateCount(Integer id) {
        newsMapper.updateCount(id);
    }
}