package com.yiling.marketing.promotion.dto.request;

import java.io.Serializable;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 秒杀特价
 *
 * @author: fan.shen
 * @date: 2022/1/14
 */
@Data
@Accessors(chain = true)
public class PromotionCombinationPackageRequest extends BaseRequest implements Serializable {

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

}
