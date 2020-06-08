package xuecheng.manage_cms_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;


/**
 * @author 韩浩辰
 * @date 2019/11/28 19:38
 */
@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain.cms")//扫描实体类
@ComponentScan(basePackages={"com.xuecheng.framework.exception"})//扫描common下的异常类，该cms组件遇到异常需要common组件处理
@ComponentScan(basePackages={"com.xuecheng.manage_cms_client"})//扫描本项目下的所有类
public class ManageCmsClientApplication {
    StringBuffer sbuffer=new StringBuffer("aaa");
    public static void main(String[] args) {
        SpringApplication.run(ManageCmsClientApplication.class,args);
    }
}
