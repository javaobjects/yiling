package com.yiling.marketing.strategy.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询商品参与活动参数
 *
 * @author: yong.zhang
 * @date: 2022/9/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsStrategyInfoRequest extends BaseRequest {

    /**
     * 销售渠道-选择平台（1-B2B；2-销售助手）（必传）
     */
    private Integer platformSelected;

    /**
     * 买家企业id（必传）
     */
    private Long buyerEid;

    /**
     * 商品id(商品id集合必须是同一个规格的)
     */
    private List<Long> goodsIdList;

    /**
     * 规格id
     */
    private Long sellSpecificationsId;

    private Long sellerEid;
}
