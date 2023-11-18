package com.yiling.marketing.strategy.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteStrategyBuyerLimitRequest extends BaseRequest {

    /**
     * 营销活动id
     */
    private Long marketingStrategyId;

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
