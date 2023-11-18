package com.yiling.marketing.promotion.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 促销活动主表
 *
 * @author: yong.zhang
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
public class PromotionActivityDTO extends BaseDTO {
    /**
     * 促销活动名称
     */
    private String name;

    /**
     * 活动类型（1-平台活动；2-商家活动）
     */
    private Integer sponsorType;

    /**
     * 预算金额
     */
    private BigDecimal budgetAmount;

    /**
     * 费用承担方（1-平台；2-商家；3-分摊）
     */
    private Integer bear;

    /**
     * 分摊-平台百分比
     */
    private BigDecimal platformPercent;

    /**
     * 分摊-商户百分比
     */
    private BigDecimal merchantPercent;

    /**
     * 活动类型（1-满赠2-秒杀，3-特价，4-组合包）
     */
    private Integer type;

    /**
     * 生效类型 1-立即生效，2-固定生效时间
     */
    private Integer effectType;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 选择平台（1-B2B；2-销售助手）
     */
    private String platformSelected;

    /**
     * 活动状态（1-启用；2-停用；）
     */
    private Integer status;

    /**
     * 满赠方式：1-按总金额 2-按品类金额
     */
    private Integer promotionType;

    /**
     * 满赠金额
     */
    private BigDecimal promotionAmount;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 促销编码
     */
    private String promotionCode;

    /**

    /**
     * 商家类型 1-以岭，2-非以岭,3都可以
     */
    private Integer merchantType;

}
