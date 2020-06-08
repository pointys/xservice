package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 韩浩辰
 * @date 2020/5/18 16:53
 */
public interface CoursePicRepository extends JpaRepository<CoursePic,String> {
    long deleteByCourseid(String courseid);
}
