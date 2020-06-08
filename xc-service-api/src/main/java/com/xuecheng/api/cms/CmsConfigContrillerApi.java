package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author: 韩老魔
 * @Date: 2019/12/5 0005 22:43
 */
@Api(value = "cms配置管理接口",description = "提供数据模型的管理,查询接口")
public interface CmsConfigContrillerApi {
    @ApiOperation("根据id查询cmsConfig")
    public CmsConfig getModel(String id);
}
