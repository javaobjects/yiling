package com.yiling.user.integral.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单送积分-商家范围添加商家 Request
 *
 * @author: lun.yu
 * @date: 2023-01-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddIntegralOrderSellerRequest extends BaseRequest {

    /**
     * 发放规则ID
     */
    private Long giveRuleId;

    /**
     * 企业id-单独添加时使用
     */
    private Long eid;

    /**
     * 企业id集合-添加当前页时使用
     */
    private List<Long> eidList;
    
}
