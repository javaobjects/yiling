package com.yiling.marketing.promotion.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 促销活动-秒杀&特价表
 * </p>
 *
 * @author: fan.shen
 * @date: 2022/1/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionCombinationPackDTO extends BaseDTO {

    /**
     * 促销活动表主键
     */
    private Long promotionActivityId;

    /**
     * 组合包名称
     */
    private String packageName;

    /**
     * 组合包起购数量
     */
    private Integer initialNum;

    /**
     * 退货要求
     */
    private String returnRequirement;

    /**
     * 组合包商品简称
     */
    private String packageShortName;

    /**
     * 总数量
     */
    private Integer totalNum;

    /**
     * 每人最大数量
     */
    private Integer perPersonNum;

    /**
     * 每人每天数量
     */
    private Integer perDayNum;

    /**
     * 活动图片
     */
    private String pic;

    /**
     * 组合包与其他营销活动说明
     */
    private String descriptionOfOtherActivity;

    /**
     * 备注
     */
    private String remark;

    /**
     * 限购信息
     */
    private Boolean reachLimit=false;

    /**
     * 组合包剩余最大购买套数
     */
    private Integer surplusBuyNum;
}
