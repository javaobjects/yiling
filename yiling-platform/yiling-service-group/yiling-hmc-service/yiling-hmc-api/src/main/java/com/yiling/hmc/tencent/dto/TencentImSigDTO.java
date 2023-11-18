package com.yiling.hmc.tencent.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TencentImSigDTO implements Serializable {

    /**
     * 腾讯IM通讯密码
     */
    private String tencentIMUserSig;

    /**
     * userId
     */
    private String userId;

    /**
     * 腾讯IM appId
     */
    private Integer tencentIMAppId;
}
