package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * @Author: 韩老魔
 * mongodb测试
 * @Date: 2019/8/17 0017 0:55
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Test
    public void test() {
        List<CmsPage> cmsPageList = cmsPageRepository.findAll();
        System.out.println(cmsPageList);


        //分页查询
        Pageable page=PageRequest.of(0,10);
        Page<CmsPage> cmsPages = cmsPageRepository.findAll(page);
        System.out.println(cmsPages.getSize());

        //主键查询
        //optional为jdk8出来的对象容器
        Optional<CmsPage> optional = cmsPageRepository.findById("5a961ff6b00ffc5a9cdebaea");
        if(optional.isPresent()){
            System.out.println(optional.get());
        }

        //自定义
        CmsPage cmsPage=cmsPageRepository.findByPageAliase("首页");
        System.out.println(cmsPage.toString());
    }
}