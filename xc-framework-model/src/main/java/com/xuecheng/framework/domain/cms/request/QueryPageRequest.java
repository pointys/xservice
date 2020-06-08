package com.xuecheng.framework.domain.cms.request;

import lombok.Data;

/**
 * @Author: 韩老魔
 * 将页面请求参数封装为实体类
 * @Date: 2019/8/15 0015 22:09
 */
@Data
public class QueryPageRequest {
    //站点id
    private String siteId;
    //页面id
    private String pageId;
    //页面名称
    private String pageName;
    //站点别名
    private String siteAliase;
    //模板id
    private String templateId;
}
