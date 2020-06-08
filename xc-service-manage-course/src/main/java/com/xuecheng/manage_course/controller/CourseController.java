package com.xuecheng.manage_course.controller;

import com.xuecheng.api.courser.CourseControllerApi;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.CoursePublicResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author 韩浩辰
 * @date 2020/5/12 22:26
 */
@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {
    @Autowired
    CourseService courseService;

    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findCourseList(@PathVariable("courseId") String courseId) {
        return courseService.findCourseListByCourseId(courseId);
    }

    @PostMapping("/teachplan/add")
    @Override
    public ResponseResult addTeachPlan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachPlan(teachplan);
    }

    @PostMapping("/coursepic/add")
    @Override
    public ResponseResult addCoursePic(String courseId, String pic) {
        return courseService.addCoursePic(courseId, pic);
    }

    @GetMapping("/coursepic/list/{courseId}")
    @Override
    public CoursePic findCoursePic(@PathVariable("courseId") String courseId) {
        return courseService.findCoursePicByCourseId(courseId);
    }

    @Override
    @DeleteMapping("/coursepic/delete")
    public ResponseResult delCoursePic(@RequestParam("courseId") String courseId) {
        return courseService.deleteCoursePic(courseId);
    }

    @Override
    @GetMapping("/courseview/{courseId}")
    public CourseView courseView(@PathVariable("courseId") String courseId) {
        return courseService.getCourseView(courseId);
    }

    @Override
    @PostMapping("/preview/{id}")
    public CoursePublicResult preview(@PathVariable("id") String id) {
        return courseService.preview(id);
    }
}
