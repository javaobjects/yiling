package com.yiling.user.integral.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单送积分-添加指定方案会员 Request
 *
 * @author: lun.yu
 * @date: 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddIntegralGiveMemberRequest extends BaseRequest {

    /**
     * 发放规则ID
     */
    @NotNull
    private Long giveRuleId;

    /**
     * 会员ID
     */
    @NotNull
    private Long memberId;

}
