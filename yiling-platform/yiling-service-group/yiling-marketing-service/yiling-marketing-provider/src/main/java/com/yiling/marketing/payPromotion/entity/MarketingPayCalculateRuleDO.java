package com.yiling.marketing.payPromotion.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 支付促销计算规则表
 * </p>
 *
 * @author shixing.sun
 * @date 2022-09-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_pay_calculate_rule")
public class MarketingPayCalculateRuleDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 支付促销活动id
     */
    private Long marketingPayId;

    /**
     * 促销规则类型（1-满减券；2-满折券）
     */
    private Integer type;

    /**
     * 门槛金额/件数
     */
    private BigDecimal thresholdValue;

    /**
     * 最高优惠金额
     */
    private BigDecimal discountMax;

    /**
     * 优惠内容（金额/折扣比例）
     */
    private BigDecimal discountValue;

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
