package com.example.mapper;

import com.example.entity.Activity;

import java.util.List;


public interface ActivityMapper {


    int insert(Activity activity);


    int deleteById(Integer id);


    int updateById(Activity activity);


    Activity selectById(Integer id);


    List<Activity> selectAll(Activity activity);

}