package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 韩浩辰
 * 异常捕获类
 * 写的太复杂而且垃圾 重构
 * @date 2019/11/25 23:59
 */
@Slf4j
@ControllerAdvice //控制器增强
public class ExceptionCatch {

    //捕获Exception此类异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e) {
        //自定义异常
        if (e instanceof MyException) {
            log.error("进入自定义异常捕获类", e.getMessage());
            ResultCode resultCode = ((MyException) e).getResultCode();
            return new ResponseResult(resultCode);
        } else {
            e.printStackTrace();
            log.error("进入全局异常捕获类", e.getMessage());
            return new ResponseResult(CommonCode.ERROR_OTHER);
        }
    }
}
