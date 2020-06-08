package com.xuecheng.framework.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author 韩浩辰
 * @date 2019/12/21 15:09
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SyncForce {
    private String method;
    private String action;
    private String transid;
    private String user;
    private String device;
    private String[] orders;

}
