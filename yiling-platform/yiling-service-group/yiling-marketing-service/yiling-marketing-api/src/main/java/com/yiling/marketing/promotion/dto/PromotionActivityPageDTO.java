package com.yiling.marketing.promotion.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动列表查询
 *
 * @author: yong.zhang
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionActivityPageDTO extends PromotionActivityDTO {

    /**
     * 选择平台（1-B2B；2-销售助手）
     */
    private List<Integer> platformSelectedList;

    /**
     * 活动创建人名称
     */
    private String createUserName;

    /**
     * 创建人手机号
     */
    private String createUserTel;

    /**
     * 活动使用数量
     */
    private Integer activityQuantity;

    /**
     * 活动进度 1-待开始 2-进行中 3-已结束
     */
    private Integer progress;
}
