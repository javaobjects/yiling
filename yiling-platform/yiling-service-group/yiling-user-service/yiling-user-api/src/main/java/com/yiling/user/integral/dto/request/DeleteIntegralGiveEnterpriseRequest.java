package com.yiling.user.integral.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 删除订单送积分-指定客户 Request
 *
 * @author: lun.yu
 * @date: 2023-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteIntegralGiveEnterpriseRequest extends BaseRequest {

    /**
     * 发放规则
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

    /**
     * 企业ID-精确搜索-删除搜索结果时使用
     */
    private Long eidPage;

    /**
     * 企业名称-模糊搜索-删除搜索结果时使用
     */
    private String enamePage;
}
