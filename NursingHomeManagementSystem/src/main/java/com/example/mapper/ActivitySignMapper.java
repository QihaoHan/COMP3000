package com.example.mapper;

import com.example.entity.ActivitySign;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface ActivitySignMapper {


    int insert(ActivitySign activitySign);


    int deleteById(Integer id);


    int updateById(ActivitySign activitySign);


    ActivitySign selectById(Integer id);


    List<ActivitySign> selectAll(ActivitySign activitySign);

    @Select("select * from activity_sign where activity_id = #{activityId} and user_id = #{userId} and status != '审核拒绝'")
    ActivitySign selectByActivityIdAndUserId(@Param("activityId") Integer activityId, @Param("userId") Integer userId);
}