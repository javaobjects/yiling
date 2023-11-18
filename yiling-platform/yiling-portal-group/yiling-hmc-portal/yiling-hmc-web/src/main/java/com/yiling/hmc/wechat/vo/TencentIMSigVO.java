package com.yiling.hmc.wechat.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 *
 * @Description
 * @Author fan.shen
 * @Date 2023/4/27
 */
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TencentIMSigVO {

    /**
     * 腾讯IM通讯密码
     */
    @ApiModelProperty("腾讯IM通讯密码")
    private String tencentIMUserSig;

    /**
     * userId
     */
    @ApiModelProperty("userId")
    private String userId;

    /**
     * 腾讯IM appId
     */
    @ApiModelProperty("腾讯IM appId")
    private Integer tencentIMAppId;

}
