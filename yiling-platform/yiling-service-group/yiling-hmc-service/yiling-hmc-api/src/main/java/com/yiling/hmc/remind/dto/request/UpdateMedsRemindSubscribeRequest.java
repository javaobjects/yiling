package com.yiling.hmc.remind.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 更新订阅状态入参
 * @author: fan.shen
 * @date: 2022/8/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateMedsRemindSubscribeRequest extends BaseRequest {

    private static final long serialVersionUID = 2280448824232686833L;

    /**
     * id
     */
    private Long id;

    /**
     * 订阅字符串
     */
    private String subscribeStr;


}