package com.yiling.user.agreement.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/7/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ClearAgreementConditionCalculateRequest extends BaseRequest {
    private static final long serialVersionUID = -5045331341880627054L;

    /**
     * 满足条件的订单集合
     */
    private List<Long> orderIds;

    /**
     * 协议条件
     */
    private List<Long> agreementConditionIds;

    /**
     * 协议条件
     */
    private List<Long> agreementIds;


}
