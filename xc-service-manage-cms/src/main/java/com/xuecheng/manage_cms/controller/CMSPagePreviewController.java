package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * @Author: 韩老魔
 * @Date: 2019/12/19 0019 22:43
 * 页面预览
 */
@Controller
public class CMSPagePreviewController extends BaseController {
    @Autowired
    PageService pageService;

    //pageId:5a795ac7dd573c04508f3a56
    @GetMapping("/cms/preview/{pageId}")
    public void preview(@PathVariable String pageId) {
        try {
            //执行静态化
            String pageHtml = pageService.getPageHtmlByPageId(pageId);
            //通过response将内容输出
            ServletOutputStream outputStream = response.getOutputStream();
            //使用SSI必须设置header响应头
            response.setHeader("Content-type","text/html;charset=utf-8");
            outputStream.write(pageHtml.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
