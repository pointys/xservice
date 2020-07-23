package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublicResult;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.exception.MyException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageclient;
import com.xuecheng.manage_course.dao.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 韩浩辰
 * @date 2020/5/12 22:24
 */
@Service
@Slf4j
public class CourseService {
    @Autowired
    TeachPlanMapper teachPlanMapper;
    @Autowired
    TeachPlanRepository teachPlanRepository;
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CoursePicRepository coursePicRepository;
    @Autowired
    CourseMarketRepository courseMarketRepository;
    @Autowired
    CmsPageclient cmsPageclient;
    @Autowired
    CoursePubRepository coursePubRepository;
    @Autowired
    TeachplanMediaRepository teachplanMediaRepository;

    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;

    public TeachplanNode findCourseListByCourseId(String courseId) {
        return teachPlanMapper.selectList(courseId);
    }

    @Transactional
    public ResponseResult addTeachPlan(Teachplan teachplan) {
        if (teachplan == null || StringUtils.isBlank(teachplan.getCourseid()) || StringUtils.isBlank(teachplan.getPname())) {
            MyException.throwException(CommonCode.INVALID_PARAM);
        }
        String courseId = teachplan.getCourseid();
        String parentId = teachplan.getParentid();
        if (StringUtils.isBlank(parentId)) {
            //父节点不存在则创建根节点
            parentId = this.getTeachPlanRoot(courseId);
        }

        //获取父节点信息
        Optional<Teachplan> optional = teachPlanRepository.findById(parentId);
        Teachplan teachplanParentNode = optional.get();
        String grade = teachplanParentNode.getGrade();

//        Teachplan teachplanNew = new Teachplan();
//        BeanUtils.copyProperties(teachplan, teachplanNew);
//        teachplanNew.setGrade("1".endsWith(teachplanParentNode.getGrade()) == true ? "2" : "3");//根据父节点确定子节点级别
//        teachPlanRepository.save(teachplanNew);
        teachplan.setParentid(parentId);
        teachplan.setGrade("1".endsWith(teachplanParentNode.getGrade()) == true ? "2" : "3");//根据父节点确定子节点级别
        teachPlanRepository.save(teachplan);
        log.info("新增课程计划：" + teachplan.toString());
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //查询课程根节点，如果不存在则自动添加根节点
    private String getTeachPlanRoot(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            log.warn("根据courseId:" + courseId + "查不到CourseBase，无法获取课程根节点");
            return null;
        }
        CourseBase courseBase = optional.get();

        List<Teachplan> teachPlanList = teachPlanRepository.findByCourseidAndParentid(courseId, "0");
        if (CollectionUtils.isEmpty(teachPlanList)) {
            Teachplan teachplanParentNode = new Teachplan(null, courseBase.getName(), "0", "1", "", "", courseId, "0", null, null, "");
            teachPlanRepository.save(teachplanParentNode);
            return teachplanParentNode.getId();
        }
        return teachPlanList.get(0).getId();
    }

    //向课程管理添加课程与图片关联信息
    @Transactional
    public ResponseResult addCoursePic(String courseId, String pic) {
        CoursePic coursePic = null;
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if (optional.isPresent()) {
            coursePic = optional.get();
        }
        if (coursePic == null) {
            coursePic = new CoursePic(courseId, pic);
            coursePicRepository.save(coursePic);
            return new ResponseResult(CommonCode.SUCCESS);
        } else {
            coursePic.setCourseid(courseId);
            coursePic.setPic(pic);
            return new ResponseResult(CommonCode.REPLACE);
        }
    }

    public CoursePic findCoursePicByCourseId(String courseId) {
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if (optional.isPresent()) {
            CoursePic coursePic = optional.get();
            return coursePic;
        }
        return null;
    }

    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {
        long l = coursePicRepository.deleteByCourseid(courseId);
        return l > 0 ? new ResponseResult(CommonCode.SUCCESS) : new ResponseResult(CommonCode.FAIL);
    }

    //查询课程视图
    public CourseView getCourseView(String courseId) {
        CourseView courseView = new CourseView();

        //查询课程基本信息
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(courseId);
        if (courseBaseOptional.isPresent()) {
            CourseBase courseBase = courseBaseOptional.get();
            courseView.setCourseBase(courseBase);
        }

        //查询课程图片信息
        Optional<CoursePic> coursePicOptional = coursePicRepository.findById(courseId);
        if (coursePicOptional.isPresent()) {
            CoursePic coursePic = coursePicOptional.get();
            courseView.setCoursePic(coursePic);
        }

        //课程营销信息
        Optional<CourseMarket> marketOption = courseMarketRepository.findById(courseId);
        if (marketOption.isPresent()) {
            CourseMarket courseMarket = marketOption.get();
            courseView.setCourseMarket(courseMarket);
        }

        //课程计划
        TeachplanNode teachplanNode = teachPlanMapper.selectList(courseId);
        courseView.setTeachplanNode(teachplanNode);
        return courseView;
    }

    //根据id查询课程基本信息
    public CourseBase findCourseBaseById(String courseId) {
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(courseId);
        if (baseOptional.isPresent()) {
            CourseBase courseBase = baseOptional.get();
            return courseBase;
        }
        MyException.throwException(CourseCode.COURSE_GET_NOTEXISTS);
        return null;
    }

    //课程预览
    @Transactional
    public CoursePublicResult preview(String id) {
        //1.添加cmsPage页面，远程请求调用
        CourseBase courseBase = this.findCourseBaseById(id);
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setDataUrl(publish_dataUrlPre + id);
        cmsPage.setPageName(id + ".html");//页面名称
        cmsPage.setPageAliase(courseBase.getName());//页面别名，即课程名称
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setTemplateId(publish_templateId);//模板id
        CmsPageResult cmsPageResult = cmsPageclient.save(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            return new CoursePublicResult(CommonCode.FAIL, null);
        }

        //2.拼装的url
        String pageId = cmsPageResult.getCmsPage().getPageId();
        String url = previewUrl + pageId;
        System.out.println("------------返回给前端的课程预览请求地址：" + url);
        return new CoursePublicResult(CommonCode.SUCCESS, url);
    }

    //课程发布
    @Transactional
    public CoursePublicResult publish(String id) {
        //调用cms一键发布接口，将课程详情页面保存到服务器
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setDataUrl(publish_dataUrlPre + id);
        cmsPage.setPageName(id + ".html");//页面名称
        CourseBase courseBase = this.findCourseBaseById(id);
        cmsPage.setPageAliase(courseBase.getName());//页面别名，即课程名称
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setTemplateId(publish_templateId);//模板id
        CmsPostPageResult cmsPostPageResult = cmsPageclient.publsih(cmsPage);
        if (!cmsPostPageResult.isSuccess()) {
            return new CoursePublicResult(CommonCode.FAIL, null);
        }
        //将课程发布状态设置为已发布
        courseBase = this.updateCourseStatus(id);
        log.info("修改后的课程基本信息：" + courseBase.toString());
        if (courseBase == null) {
            return new CoursePublicResult(CommonCode.FAIL, null);
        }

        //保存课程索引信息
        CoursePub coursePub = createCoursePub(id,true);
        log.info("保存的coursePub信息为："+coursePub.toString());
        //缓存课程信息

        String pageUrl = cmsPostPageResult.getPageUrl();
        log.info("pageUrl:" + pageUrl);
        log.info(cmsPage.toString());
        return new CoursePublicResult(CommonCode.SUCCESS, pageUrl);
    }

    //创建coursePub对象,合成总表导入es
    public CoursePub createCoursePub(String id, boolean isSave) {
        CoursePub coursePub = new CoursePub();

        //1.课程营销信息
        Optional<CourseBase> optionalCourseBase = courseBaseRepository.findById(id);
        if (optionalCourseBase.isPresent()) {
            CourseBase courseBase = optionalCourseBase.get();
            BeanUtils.copyProperties(courseBase, coursePub);
        }

        //2.课程图片信息
        Optional<CoursePic> optionalCoursePic = coursePicRepository.findById(id);
        if(optionalCoursePic.isPresent()){
            CoursePic coursePic = optionalCoursePic.get();
            BeanUtils.copyProperties(coursePic,coursePub);
        }

        //3.课程营销信息
        Optional<CourseMarket> optionalCourseMarket = courseMarketRepository.findById(id);
        if(optionalCourseMarket.isPresent()){
            CourseMarket courseMarket = optionalCourseMarket.get();
            BeanUtils.copyProperties(courseMarket,coursePub);
        }

        //4.课程计划信息
        TeachplanNode teachplanNode = teachPlanMapper.selectList(id);
        String json = JSON.toJSONString(teachplanNode);
        coursePub.setTeachplan(json);
        //logstash需要时间
        coursePub.setTimestamp(new Date());//时间戳
        coursePub.setPubTime(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));//发布时间
        if (isSave) {
            coursePubRepository.save(coursePub);
        }
        return coursePub;
    }

    //更新课程状态码为已发布
    @Transactional
    public CourseBase updateCourseStatus(String courseId) {
        CourseBase courseBase = this.findCourseBaseById(courseId);
        courseBase.setStatus("202002");
        return courseBaseRepository.save(courseBase);
    }

    //分页查询
    public Page<CourseBase> findCourseBaseNoCriteria(Integer page, Integer size) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");
        return courseBaseRepository.findAll(pageable);
    }

    public ResponseResult saveMedia(TeachplanMedia teachplanMedia) {
            if (teachplanMedia == null || StringUtils.isEmpty(teachplanMedia.getTeachplanId())) {
                MyException.throwException(CommonCode.INVALID_PARAM);
            }
            //校验课程计划等级是否为三级（不允许在父节点保存视频文件）
            Optional<Teachplan> optional = teachPlanRepository.findById(teachplanMedia.getTeachplanId());
            if (!optional.isPresent()) {
                MyException.throwException(CommonCode.INVALID_PARAM);
            }
            Teachplan teachplan = optional.get();
            if (StringUtils.isEmpty(teachplan.getGrade()) || !"3".equals(teachplan.getGrade())) {
                MyException.throwException(CourseCode.COURSE_GET_NOTEXISTS);
            }
            Optional<TeachplanMedia> mediaOptional = teachplanMediaRepository.findById(teachplanMedia.getTeachplanId());
            if (mediaOptional.isPresent()) {
                teachplanMedia = mediaOptional.get();
                log.info("已存在的数据："+teachplanMedia.toString());
                return new ResponseResult(CommonCode.REPEAT);
            }else{
                //数据库没查到
                teachplanMedia.setCourseId(teachplan.getCourseid());
                teachplanMedia.setTeachplanId(teachplan.getId());
                teachplanMediaRepository.save(teachplanMedia);
                log.info("保存的数据："+teachplanMedia.toString());
                return new ResponseResult(CommonCode.SUCCESS);
            }
    }
}

