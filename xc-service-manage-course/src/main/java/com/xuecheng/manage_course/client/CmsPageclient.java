package com.xuecheng.manage_course.client;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 韩浩辰
 * @date 2020/5/28 15:04
 */
@FeignClient(value="XC-SERVICE-MANAGE-CMS")//远程调用的服务名
public interface CmsPageclient {
    //http远程调用XC-SERVICE-MANAGE-CMS服务下的接口
    @GetMapping("/cms/page/get/{id}")
    public CmsPage findCmsPageById(@PathVariable("id") String id);

    //添加cms页面,用于课程预览
    @PostMapping("/cms/page/save")
    public CmsPageResult save(@RequestBody CmsPage cmsPage);

    //一键发布
    @PostMapping("/cms/page/postPageQuick")
    public CmsPostPageResult publsih(@RequestBody CmsPage cmsPage);
}

