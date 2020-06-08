package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRibbon {
    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testRibbon(){
        //测试前在启动类把@LoadBalanced注释打开
        String serverName= "XC-SERVICE-MANAGE-CMS";
        System.out.println("serverName:"+serverName);
        for(int i=0;i<10;i++){
            //ribbon客户端根据服务名从eurekaServer获取服务列表
            ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://"+serverName+"/cms/page/get/5a754adf6abb500ad05688d9", Map.class);
            Map body = forEntity.getBody();
            System.out.println("远程调用返回数据：\n"+body);
        }
    }
}
