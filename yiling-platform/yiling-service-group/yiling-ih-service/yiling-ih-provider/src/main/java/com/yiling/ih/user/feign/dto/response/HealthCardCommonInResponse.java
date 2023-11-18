package com.yiling.ih.user.feign.dto.response;

import com.yiling.ih.common.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 健康卡参数
 *
 * @author: fan.shen
 * @date: 2022/9/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HealthCardCommonInResponse extends BaseResponse {

    private String appToken;

    private String requestId;

    private String hospitalId;

    private int channelNum;

    private String appSecret;

}
