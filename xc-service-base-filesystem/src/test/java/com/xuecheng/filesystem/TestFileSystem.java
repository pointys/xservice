package com.xuecheng.filesystem;

import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author 韩浩辰
 * @date 2020/5/14 15:24
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFileSystem {
    @Value("${xuecheng.fastdfs.tracker_servers}")
    String tracker_servers;
    @Value("${xuecheng.fastdfs.charset}")
    String charset;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    int network_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    int connect_timeout_in_seconds;

    @Autowired
    FileSystemRepository fileSystemRepository;

    /**
     * 文件上传下载
     */
    @Test
    public void uploadAndDownload() throws IOException, MyException {
        String filePath = "D:/img/y.png";
        byte[] buffer = null;
        File file = new File(filePath);
        FileInputStream in = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            in = new FileInputStream(file);
            buffer = new byte[in.available()];
            in.read(buffer);
            out.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        //初始化fastdfs环境
        ClientGlobal.initByTrackers(tracker_servers);
        ClientGlobal.setG_charset(charset);
        ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
        ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);

        //定义TrackerClient，用于请求TrackerServer
        TrackerClient trackerClient = new TrackerClient();

        //通过trackerClient获取trackerServer服务器
        TrackerServer trackerServer = trackerClient.getConnection();
        //通过trackerClien获取storageServer服务器
        StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
        //创建storageClient来上传文件
        StorageClient1 storageClient1 = new StorageClient1(trackerServer, storeStorage);
        //上传
        String fileId = storageClient1.upload_file1(buffer, "png", null);
        System.out.println("文件id："+fileId);
    }


}
