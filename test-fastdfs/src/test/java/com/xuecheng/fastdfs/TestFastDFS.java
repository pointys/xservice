package com.xuecheng.fastdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author 韩浩辰
 * @date 2020/5/14 15:24
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFastDFS {

    /**
     * 文件上传下载
     */
    @Test
    public void uploadAndDownload() {
        try {
            //加载fastdfs-client.properties配置文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //初始化fastdfs环境
//            ClientGlobal.initByTrackers("47.111.242.221:22122");
//            ClientGlobal.setG_charset("UTF-8");
//            ClientGlobal.setG_network_timeout(30000);
//            ClientGlobal.setG_connect_timeout(5000);

            //定义TrackerClient，用于请求TrackerServer
            TrackerClient trackerClient = new TrackerClient();
            //连接tracker
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取Stroage
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
            //创建stroageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer,storeStorage);
            //向stroage服务器上传文件
            //本地文件的路径
            String upload_filePath = "d:/img/y.png";
//            String download_filePath = "d:/y.png";
            //上传成功后拿到文件Id
            String fileId = storageClient1.upload_file1(upload_filePath, "mp4", null);
            System.out.println("上传文件id："+fileId);
            //通过文件id下载文件
          /*  byte[] bytes = storageClient1.download_file1(fileId);
            FileOutputStream outputStream=new FileOutputStream(download_filePath);
            outputStream.write(bytes);
            outputStream.close();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
