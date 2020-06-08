package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author: 韩老魔
 * 类似data JPA
 * 对前端freemaker的数据进行处理
 * @Date: 2019/8/17 0017 0:55
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {

}
