package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 韩浩辰
 * @date 2020/5/15 17:04
 */
@Api(value = "文件管理接口", description = "提供文件上传、下载")
public interface FileSystemControllerApi {
    //上传文件
    @ApiOperation("上传文件接口，上传成功后调用钩子函数请求/course/coursepic/add,将上传图片的信息保存到mysql")
    public UploadFileResult upload(MultipartFile multipartFile, String filetag, String businesskey, String metadata);
}
