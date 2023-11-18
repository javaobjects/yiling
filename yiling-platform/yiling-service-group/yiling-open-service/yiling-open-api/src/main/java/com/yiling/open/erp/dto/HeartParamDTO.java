package com.yiling.open.erp.dto;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2022/4/18
 */
@Data
public class HeartParamDTO {

    private String envName;

    private String endpoint;

    private String protocol;

    private String domain;

    private Long expires;

    private String accessKey;

    private String accessKeySecret;

    private String redisPassWord;

    private String redisAddr;

}
