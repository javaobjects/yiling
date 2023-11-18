package com.yiling.marketing.integral.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单送积分-店铺SKU-待添加店铺SKU分页列表查询 Request
 *
 * @author: lun.yu
 * @date: 2023-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralEnterpriseGoodsPageRequest extends QueryPageListRequest {

    /**
     * 发放规则ID
     */
    private Long giveRuleId;

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
