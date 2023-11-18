package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 创建公众号用户信息
 *
 * @author fan.shen
 * @date 2022-03-26
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateGzhUserRequest extends BaseRequest {

    /**
     * 公众号appId
     */
    private String appId;

    /**
     * 公众号openId
     */
    private String gzhOpenId;

    /**
     * unionId
     */
    private String unionId;


}
