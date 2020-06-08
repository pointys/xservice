package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.MyException;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author 韩浩辰
 * @date 2020/5/15 18:59
 */
@Service
public class FileSystemService {
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
     * 上传文件
     *
     * @param multipartFile
     * @param filetag
     * @param businessKey
     * @param metadate
     * @return
     */
    public UploadFileResult upload(MultipartFile multipartFile, String filetag, String businessKey, String metadate) {
        if (multipartFile == null) {
            MyException.throwException(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        //上传到fastDFS获取文件id
        String fileId = this.upload_fastDFS(multipartFile);
        if (StringUtils.isBlank(fileId)) {
            MyException.throwException(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }

        FileSystem fileSystem = new FileSystem(fileId, fileId, multipartFile.getSize(), multipartFile.getOriginalFilename(), multipartFile.getContentType(), 0, 0, null, businessKey, filetag, null);
        if (StringUtils.isNotBlank(metadate)) {
            Map map = JSON.parseObject(metadate,Map.class);
            fileSystem.setMetadata(map);
        }
        //将文件id及其他文件信息保存到mongodb中
        fileSystemRepository.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS, fileSystem);
    }

    /**
     * 上传到fastDFS
     *
     * @param multipartFile
     * @return 文件id
     */
    private String upload_fastDFS(MultipartFile multipartFile) {
        try {
            //初始化fastdfs环境
            ClientGlobal.initByTrackers(tracker_servers);
            ClientGlobal.setG_charset(charset);
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
            ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
        } catch (Exception e) {
            e.printStackTrace();
            MyException.throwException(FileSystemCode.FS_UPLOADFILE_INITCLIENT);
        }
        TrackerClient trackerClient = new TrackerClient();
        try {
            //通过trackerClient获取trackerServer服务器
            TrackerServer trackerServer = trackerClient.getConnection();
            //通过trackerClien获取storageServer服务器
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
            //创建storageClient来上传文件
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storeStorage);
            //上传
            String fileId = storageClient1.upload_file1(multipartFile.getBytes(), multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1), null);
            return fileId;
        } catch (Exception e) {
            e.printStackTrace();
            MyException.throwException(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }
        return null;
    }
}
