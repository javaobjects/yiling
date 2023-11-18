package com.yiling.marketing.promotion.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@Accessors(chain = true)
public class PromotionActivitySaveRequest extends BaseRequest implements Serializable {

    /**
     * 促销名称
     */
    private String        name;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    private Integer       sponsorType;

    /**
     * 促销预算金额
     */
    private BigDecimal    budgetAmount;

    /**
     * 费用承担方（1-平台；2-商家;3-分摊）
     */
    private Integer       bear;

    /**
     * 分摊-平台百分比
     */
    private BigDecimal    platformPercent;

    /**
     * 分摊-商户百分比
     */
    private BigDecimal    merchantPercent;

    /**
     * 活动类型（1-满赠；2-特价；3-秒杀; 4-组合包）
     */
    private Integer       type;

    /**
     * 生效类型 1-立即生效，2-固定生效时间
     */
    private Integer       effectType;

    /**
     * 开始时间时间
     */
    private Date          beginTime;

    /**
     * 结束时间
     */
    private Date          endTime;

    /**
     * 选择平台（1-B2B；2-销售助手）
     */
    private List<Integer> platformSelected;

    /**
     * 促销编码
     */
    private String        promotionCode;

    /**
     * 商家类型 1-以岭，2-非以岭
     */
    private Integer       merchantType;

    /**
     * 备注
     */
    private String       remark;

}
