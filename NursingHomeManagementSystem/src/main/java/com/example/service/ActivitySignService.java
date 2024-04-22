package com.example.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.ActivitySign;
import com.example.exception.CustomException;
import com.example.mapper.ActivitySignMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ActivitySignService {

    @Resource
    private ActivitySignMapper activitySignMapper;


    public void add(ActivitySign activitySign) {
        activitySign.setDate(DateUtil.today());
        // See if the user signed up
        ActivitySign sign = activitySignMapper.selectByActivityIdAndUserId(activitySign.getActivityId(), activitySign.getUserId());
        if (sign != null) {
            throw new CustomException(ResultCodeEnum.SIGN_ERROR);
        }
        activitySignMapper.insert(activitySign);
    }


    public void deleteById(Integer id) {
        activitySignMapper.deleteById(id);
    }


    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            activitySignMapper.deleteById(id);
        }
    }


    public void updateById(ActivitySign activitySign) {
        activitySignMapper.updateById(activitySign);
    }


    public ActivitySign selectById(Integer id) {
        return activitySignMapper.selectById(id);
    }


    public List<ActivitySign> selectAll(ActivitySign activitySign) {
        return activitySignMapper.selectAll(activitySign);
    }


    public PageInfo<ActivitySign> selectPage(ActivitySign activitySign, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser.getRole().equals(RoleEnum.USER.name())) {  //  users only query their own data
            activitySign.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<ActivitySign> list = activitySignMapper.selectAll(activitySign);
        return PageInfo.of(list);
    }

    public ActivitySign selectByActivityIdAndUserId(Integer activityId, Integer userId) {
        return activitySignMapper.selectByActivityIdAndUserId(activityId, userId);
    }

    public List<Dict> selectCount() {
        List<ActivitySign> activitySigns = activitySignMapper.selectAll(null);
        activitySigns = activitySigns.stream().filter(activitySign -> "Approved".equals(activitySign.getStatus())
                || "Pending".equals(activitySign.getStatus())).collect(Collectors.toList());
        Set<String> set = activitySigns.stream().map(ActivitySign::getActivityName).collect(Collectors.toSet());
        List<Dict> list = CollUtil.newArrayList();
        for (String name : set) {
            long count = activitySigns.stream().filter(activitySign -> activitySign.getActivityName().equals(name)).count();
            Dict dict = Dict.create().set("name", name).set("value", count);
            list.add(dict);
        }
        return list;
    }
}