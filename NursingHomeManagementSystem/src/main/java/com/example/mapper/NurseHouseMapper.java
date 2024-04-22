package com.example.mapper;

import com.example.entity.NurseHouse;
import org.apache.ibatis.annotations.Update;

import java.util.List;


public interface NurseHouseMapper {


    int insert(NurseHouse nurseHouse);


    int deleteById(Integer id);


    int updateById(NurseHouse nurseHouse);


    NurseHouse selectById(Integer id);


    List<NurseHouse> selectAll(NurseHouse nurseHouse);

    @Update("update nurse_house set count = count + 1 where id = #{id}")
    void updateCount(Integer id);

}