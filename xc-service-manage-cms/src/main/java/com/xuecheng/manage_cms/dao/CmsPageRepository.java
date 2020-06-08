package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author: 韩老魔
 * 类似data JPA
 * @Date: 2019/8/17 0017 0:55
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
    //根据页面别名查对象
    CmsPage findByPageAliase(String pageAliase);

    //根据pageName、siteId、pageWebPath三个字段查询
    CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName, String siteId, String pageWebPath);
}
