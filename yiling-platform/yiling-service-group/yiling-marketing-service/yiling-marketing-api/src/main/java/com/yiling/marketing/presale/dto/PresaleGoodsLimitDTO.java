package com.yiling.marketing.presale.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 营销活动主表
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PresaleGoodsLimitDTO extends BaseDTO {

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
