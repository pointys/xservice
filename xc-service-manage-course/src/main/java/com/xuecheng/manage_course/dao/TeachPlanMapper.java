package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 韩浩辰
 * @date 2020/5/12 21:40
 */
@Mapper
public interface TeachPlanMapper {
    public TeachplanNode selectList(String courseId);
}
