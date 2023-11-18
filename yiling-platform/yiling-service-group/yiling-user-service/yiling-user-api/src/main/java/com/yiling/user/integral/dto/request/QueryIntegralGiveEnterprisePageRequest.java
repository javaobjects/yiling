package com.yiling.user.integral.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询订单送积分-指定客户分页列表 Request
 *
 * @author: lun.yu
 * @date: 2023-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralGiveEnterprisePageRequest extends QueryPageListRequest {

    /**
     * 发放规则id
     */
    private Long giveRuleId;

    /**
     * 企业ID-精确搜索
     */
    private Long eid;

    /**
     * 企业ID-精确搜索
     */
    private List<Long> eidList;

    /**
     * 企业名称-模糊搜索
     */
    private String ename;
}
