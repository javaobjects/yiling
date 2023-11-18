package com.yiling.user.integral.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单送积分-查询指定方案会员分页列表 Request
 *
 * @author: lun.yu
 * @date: 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralGiveMemberPageRequest extends QueryPageListRequest {

    /**
     * 发放规则ID
     */
    @NotNull
    private Long giveRuleId;
}
