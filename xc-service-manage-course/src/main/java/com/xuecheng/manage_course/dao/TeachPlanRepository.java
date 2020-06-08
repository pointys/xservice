package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator.
 */
public interface TeachPlanRepository extends JpaRepository<Teachplan,String> {
    //根据courseId和parentId查询
    public List<Teachplan> findByCourseidAndParentid(String courseId,String parentId);
}
