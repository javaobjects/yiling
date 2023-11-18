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
public class AddStrategyEnterpriseGoodsLimitRequest extends BaseRequest {

    /**
     * 营销活动id
     */
    private Long marketingStrategyId;

    /**
     * 商品id-单独添加时使用
     */
    private Long goodsId;

    /**
     * 商品id集合-添加当前页时使用
     */
    private List<Long> goodsIdList;

    /**
     * 商品ID-精确搜索
     */
    private Long goodsIdPage;

    /**
     * 商品名称-模糊搜索
     */
    private String goodsNamePage;

    /**
     * 企业名称-模糊搜索
     */
    private String enamePage;

    /**
     * 企业id-精确搜索
     */
    private Long eidPage;

    /**
     * 是否以岭商品 0-全部 1-以岭 2-非以岭
     */
    private Integer yilingGoodsFlag;

    /**
     * 商品状态：1上架 2下架 3待设置
     */
    private Integer goodsStatus;

    /**
     * 卖家企业id
     */
    private List<Long> sellerEidList;
}
