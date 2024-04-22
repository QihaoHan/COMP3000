package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Serve;
import com.example.mapper.ServeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class ServeService {

    @Resource
    private ServeMapper serveMapper;


    public void add(Serve serve) {
        serve.setDate(DateUtil.today());
        serveMapper.insert(serve);
    }


    public void deleteById(Integer id) {
        serveMapper.deleteById(id);
    }


    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            serveMapper.deleteById(id);
        }
    }


    public void updateById(Serve serve) {
        serveMapper.updateById(serve);
    }


    public Serve selectById(Integer id) {
        return serveMapper.selectById(id);
    }


    public List<Serve> selectAll(Serve serve) {
        return serveMapper.selectAll(serve);
    }


    public PageInfo<Serve> selectPage(Serve serve, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Serve> list = serveMapper.selectAll(serve);
        return PageInfo.of(list);
    }

}