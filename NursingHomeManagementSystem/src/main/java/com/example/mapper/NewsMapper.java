package com.example.mapper;

import com.example.entity.News;
import org.apache.ibatis.annotations.Update;

import java.util.List;


public interface NewsMapper {


    int insert(News news);


    int deleteById(Integer id);


    int updateById(News news);


    News selectById(Integer id);


    List<News> selectAll(News news);

    List<News> selectTopNews(String sort);

    @Update("update news set count = count + 1 where id = #{id}")
    void updateCount(Integer id);
}