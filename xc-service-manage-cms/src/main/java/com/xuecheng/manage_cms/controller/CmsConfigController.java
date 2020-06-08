package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsConfigContrillerApi;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 韩老魔
 * @Date: 2019/12/5 0005 22:57
 */
@RestController
@RequestMapping("/cms/config")
public class CmsConfigController implements CmsConfigContrillerApi {
    @Autowired
    private PageService pageService;

    @Override
    @GetMapping("/getmodel/{id}")
    public CmsConfig getModel(@PathVariable("id") String id) {
        return pageService.getCmsConfigById(id);
    }
}
