package com.yiling.marketing.strategy.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

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
public class QueryStrategyEnterpriseGoodsLimitPageRequest extends QueryPageListRequest {

    /**
     * 营销活动id
     */
    private Long marketingStrategyId;

    /**
     * 商家范围类型（1-全部商家；2-指定商家；）
     */
    private Integer conditionSellerType;

    /**
     * 商品ID-精确搜索
     */
    private Long goodsId;

    /**
     * 商品名称-模糊搜索
     */
    private String goodsName;

    /**
     * 企业id-精确搜索
     */
    private Long eid;

    /**
     * 企业名称-模糊搜索
     */
    private String ename;

    /**
     * 是否以岭商品 0-全部 1-以岭 2-非以岭
     */
    private Integer yilingGoodsFlag;

    /**
     * 商品状态：1上架 2下架 3待设置
     */
    private Integer goodsStatus;
}
