package com.example.mapper;

import com.example.entity.Reserve;
import java.util.List;

public interface ReserveMapper {


    int insert(Reserve reserve);


    int deleteById(Integer id);


    int updateById(Reserve reserve);


    Reserve selectById(Integer id);


    List<Reserve> selectAll(Reserve reserve);

}