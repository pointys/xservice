package com.xuecheng.manage_cms.dao;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.manage_cms.controller.CMSPagePreviewController;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author 鸡巴蟹
 * @date 2020/04/15 10:50
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFsTest {
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    CMSPagePreviewController cMSPagePreviewController;

    /**
     * 存文件
     */
    @Test
    public void store() throws Exception {
//        String filePath = "D:\\workspace\\xservice\\test‐freemarker\\src\\main\\resources\\templates\\index_banner.ftl";
       String filePath="D:\\workspace\\xservice\\xc-service-manage-cms\\src\\main\\resources\\templates\\course.ftl";
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        //返回fs.files集合的主键_id以及fs.chunks集合的files_id
        ObjectId objectId = gridFsTemplate.store(fileInputStream, "course.ftl");
        //5e9678771fba7d20bc78ab50,对应templateFileId
        System.out.println("fs.files集合的主键_id:"+objectId);//5ed5f0c39c3799408ca911c4
    }

    /**
     * 取文件
     */
    @Test
    public void test2() throws Exception {
        //根据fs.files中的_id获取文件对象
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5e9678771fba7d20bc78ab50")));

        //打开下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建gridFsResource对象，获取流
        GridFsResource gridFsResource =new GridFsResource(gridFSFile,gridFSDownloadStream);
        String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        System.out.println("文件内容："+"\n"+content);
    }

    @Test
    public void test3(){
        cMSPagePreviewController.preview("5a795ac7dd573c04508f3a56");
    }
}
