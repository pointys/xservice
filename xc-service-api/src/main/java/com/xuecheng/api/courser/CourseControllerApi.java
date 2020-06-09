package com.xuecheng.api.courser;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.CoursePublicResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author 韩浩辰
 * @date 2020/5/12 21:16
 */
@Api(value = "课程管理接口",description = "提供课程curd的接口")
public interface CourseControllerApi {
    @ApiOperation("查询课程计划")
    public TeachplanNode findCourseList(String courseId);

    @ApiOperation("添加课程计划")
    public ResponseResult addTeachPlan(Teachplan teachplan);

    @ApiOperation("添加课程与图片关联信息")
    public ResponseResult addCoursePic(String courseId,String pic);

    @ApiOperation("通过courseid查询课程图片")
    public CoursePic findCoursePic(String courseId);

    @ApiOperation("删除课程图片")
    public ResponseResult delCoursePic(String courseId);

    @ApiOperation("课程视图预览")
    public CourseView courseView(String courseId);

    @ApiOperation("课程预览")
    public CoursePublicResult preview(String courseId);

    @ApiOperation("课程发布")
    public CoursePublicResult publish(String courseId);
}
