package com.example.service;

import com.example.entity.NurseHouse;
import com.example.mapper.NurseHouseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class NurseHouseService {

    @Resource
    private NurseHouseMapper nurseHouseMapper;


    public void add(NurseHouse nurseHouse) {
        nurseHouseMapper.insert(nurseHouse);
    }


    public void deleteById(Integer id) {
        nurseHouseMapper.deleteById(id);
    }


    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            nurseHouseMapper.deleteById(id);
        }
    }


    public void updateById(NurseHouse nurseHouse) {
        nurseHouseMapper.updateById(nurseHouse);
    }


    public NurseHouse selectById(Integer id) {
        return nurseHouseMapper.selectById(id);
    }

    public List<NurseHouse> selectAll(NurseHouse nurseHouse) {
        return nurseHouseMapper.selectAll(nurseHouse);
    }


    public PageInfo<NurseHouse> selectPage(NurseHouse nurseHouse, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<NurseHouse> list = nurseHouseMapper.selectAll(nurseHouse);
        return PageInfo.of(list);
    }

    public void updateCount(Integer id) {
        nurseHouseMapper.updateCount(id);
    }

}