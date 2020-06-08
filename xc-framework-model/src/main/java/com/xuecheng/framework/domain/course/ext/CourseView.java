package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 韩浩辰
 * @date 2020/6/2 7:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseView implements Serializable {
    private CourseBase courseBase;//基础信息
    CoursePic coursePic;//课程图片信息
    CourseMarket courseMarket;//课程营销
    TeachplanNode teachplanNode;//教学计划节点
}
