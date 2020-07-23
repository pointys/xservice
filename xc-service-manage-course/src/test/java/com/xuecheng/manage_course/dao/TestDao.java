package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.manage_course.service.CourseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.Soundbank;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CourseMapper courseMapper2;
    @Autowired
    TeachPlanMapper teachPlanMapper;
    @Autowired
    CourseService courseService;

    @Test
    public void testCourseBaseRepository() {


        Optional<CourseBase> optional = courseBaseRepository.findById("402885816240d276016240f7e5000002");
        if (optional.isPresent()) {
            CourseBase courseBase = optional.get();
            System.out.println(courseBase);
        }

    }

    @Test
    public void testCourseMapper() {
        CourseBase courseBase1 = courseMapper.findCourseBaseById("402885816240d276016240f7e5000002");
        CourseBase courseBase2 = courseMapper2.findCourseBaseById("402885816240d276016240f7e5000002");
        System.out.println("---"+courseBase1);
        System.out.println("---"+courseBase2);
    }

    @Test
    public void findTeachPlan() {
        TeachplanNode teachplanNode = teachPlanMapper.selectList("4028e581617f945f01617f9dabc40000");
        System.out.println(teachplanNode.getChildren().get(1).getPname());
    }

    /*mybatis*/
    @Test
    public void findCourseBase1() {
//        PageHelper.startPage(1, 10);
        Page<CourseBase> courseList = courseMapper.findCourseList();
        System.out.println("size:" + courseList.size());

    }

    /*jpa*/
    @Test
    public void findCourseBase2() {
        org.springframework.data.domain.Page<CourseBase> datas = courseService.findCourseBaseNoCriteria(0, 5);
        System.out.println("totalPage:" + datas.getTotalPages());

    }

    @Test
    public void addCourseBase() {
//        ExecutorService executorService = Executors.newFixedThreadPool(20);
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
                for (int i = 80457; i < 46631094; i++) {
                    String j = i + "";
                    courseBaseRepository.save(new CourseBase(j, j, j, j, j, j, j, j, j, j, j, j));
                }
//            }
//        });
    }
}