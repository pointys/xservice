package com.xuecheng.framework.domain.course.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 韩浩辰
 * @date 2020/6/4 22:02
 */
@Data
@ToString
@NoArgsConstructor
public class CoursePublicResult extends ResponseResult {
    private String previewUrl;
    public CoursePublicResult(ResultCode resultCode,String previewUrl){
        super(resultCode);
        this.previewUrl=previewUrl;
    }
}
