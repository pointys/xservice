package com.xuecheng.test.freemarker;

import com.xuecheng.test.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.util.*;

/**
 * @Author: 韩老魔
 * 根据模板生成静态页面
 * @Date: 2020/3/10 0010 21:19
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FreemakerTest {

    @Test
    public void test1() throws Exception {
        /**
         * 根据模板文件生成静态页面
         * @throws Exception
         */
        //定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //得到classpath路径
        String classPath = this.getClass().getResource("/").getPath();
        System.out.println("----------:" + classPath);
        //定义模板路径
        configuration.setDirectoryForTemplateLoading(new File(classPath + "/templates/"));
        //获取模板文件
        Template template = configuration.getTemplate("test1.html");
        //定义数据模型
        Map map = new HashMap<>();
        map.put("name", "张三");
        List<Student> stus = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Student stu = new Student();
            stu.setName("hallo" + i);
            stu.setAge(19 + i);
            stu.setMondy(6f);
            stu.setBirthday(new Date());
            stu.setBestFriend(new Student());
            stus.add(stu);
        }
        map.put("stus", stus);
        //获取静态化字符内容
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        System.out.println(content);

        //输出文件
        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream outputStream = new FileOutputStream(new File("f:/test1.ftl"));
        int copy = IOUtils.copy(inputStream, outputStream);
        System.out.println(copy > 0 ? "生成ok" : "生成失败");
        //先开的先关
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void test2() throws Exception {
        /**
         * 根据字符串模板生成静态页面,相比于test1不需要模板文件只需要字符内容方便存在数据库
         */
        //定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        String contentString = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>Hello World!</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "Hello ${name}\n" +
                "</body>\n" +
                "</html>";
        //实例模板内容加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        //向模板内容加载器中添加模板内容
        stringTemplateLoader.putTemplate("template", contentString);
        //在配置中设置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板内容
        Template template = configuration.getTemplate("template", "utf-8");
        //之后同test1()
        //静态化
        Map map = new HashMap();
        map.put("name", "啊啊啊啊");
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        System.out.println(content);
    }

    @Test
    public void code() {
        int[] nums=new int[]{3,3};
        int target=6;
                nums = new int[]{3, 3};
                int[] arr = new int[2];
                for (int i = 0; i < nums.length; i++) {
                    for (int j = 0; j < nums.length; j++) {
                        if (target == nums[i] + nums[j] && i!=j) {
                            arr = new int[]{i, j};
                        }
                    }
                }
        System.out.println(arr[0]+arr[1]);

    }
}