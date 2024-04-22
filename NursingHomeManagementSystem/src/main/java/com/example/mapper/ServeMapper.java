package com.example.mapper;

import com.example.entity.Serve;

import java.util.List;


public interface ServeMapper {


    int insert(Serve serve);


    int deleteById(Integer id);


    int updateById(Serve serve);


    Serve selectById(Integer id);


    List<Serve> selectAll(Serve serve);

}