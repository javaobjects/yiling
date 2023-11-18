package com.yiling.marketing.presale.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shixing.sun
 * @date: 2022/10/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PresaleGoodsLimitRequest extends BaseRequest {

    /**
     * 预售活动id
     */
    private Long marketingStrategyId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;


    /**
     * 预售价
     */
    private BigDecimal presaleAmount;

    /**
     * 定金比例
     */
    private BigDecimal depositRatio;

    /**
     * 促销方式：0-无 1-定金膨胀 2-尾款立减
     */
    private Integer presaleType;

    /**
     * 定金膨胀倍数
     */
    private BigDecimal expansionMultiplier;

    /**
     * 尾款立减金额
     */
    private BigDecimal finalPayDiscountAmount;

    /**
     * 每人最小预定量
     */
    private Integer minNum;

    /**
     * 每人最大预定量
     */
    private Integer maxNum;

    /**
     * 合计最大预定量
     */
    private Integer allNum;
}
