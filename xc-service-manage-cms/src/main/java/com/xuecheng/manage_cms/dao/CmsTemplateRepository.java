package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author: 韩老魔
 * 类似data JPA
 * @Date: 2019/8/17 0017 0:55
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {

}
