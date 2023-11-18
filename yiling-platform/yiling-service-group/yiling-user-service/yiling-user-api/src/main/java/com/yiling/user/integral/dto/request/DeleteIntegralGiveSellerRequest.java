package com.yiling.user.integral.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单送积分-删除商家 Request
 *
 * @author: lun.yu
 * @date: 2023-01-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteIntegralGiveSellerRequest extends BaseRequest {

    /**
     * 发放规则ID
     */
    private Long giveRuleId;

    /**
     * 企业id-单独删除时使用
     */
    private Long eid;

    /**
     * 企业id集合-批量删除时使用
     */
    private List<Long> eidList;
}
