package com.yiling.hmc.remind.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 消息订阅入参
 * @author: fan.shen
 * @date: 2022/5/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MedsRemindSubscribeRequest extends BaseRequest {

    private static final long serialVersionUID = 2280448824232686833L;

    /**
     * 小程序id
     */
    private String appId;

    /**
     * openId
     */
    private String openId;

    /**
     * 模板id
     */
    private String templateId;

    /**
     * 订阅状态 1-正常订阅，2-取消订阅
     */
    private Integer subscribeStatus;

    /**
     * 用户id
     */
    private Long userId;

}