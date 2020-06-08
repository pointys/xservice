package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 韩浩辰
 * 自定义异常
 * @date 2019/11/25 23:48
 */
@Data
@AllArgsConstructor
public class MyException extends RuntimeException {
    //错误代码
    ResultCode resultCode;

    //该静态方法减少调用端throw的代码冗余
    public static void throwException(ResultCode resultCode) {
        throw new MyException(resultCode);
    }
}
