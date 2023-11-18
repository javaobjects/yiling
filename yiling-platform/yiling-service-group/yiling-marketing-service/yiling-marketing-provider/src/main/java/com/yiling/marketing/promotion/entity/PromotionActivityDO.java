package com.yiling.marketing.promotion.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 促销活动主表
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_promotion_activity")
public class PromotionActivityDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 促销活动名称
     */
    private String name;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    private Integer sponsorType;

    /**
     * 预算金额
     */
    private BigDecimal budgetAmount;

    /**
     * 费用承担方（1-平台；2-商家;3-分摊）
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
     * 活动类型（1-满赠；2-特价；3-秒杀）
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
     * 选择平台（1-B2B；2-销售助手）逗号隔开如(1  2  1,2)
     */
    private String platformSelected;

    /**
     * 活动状态（1-启用；2-停用；）
     */
    private Integer status;

    /**
     * 促销编码
     */
    private String promotionCode;

    /**
     * 商家类型 1-以岭，2-非以岭
     */
    private Integer merchantType;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

}
