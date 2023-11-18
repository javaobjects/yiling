package com.yiling.marketing.paypromotion.dto;

import java.math.BigDecimal;
import java.util.Date;

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
public class PayPromotionCalculateRuleDTO extends BaseDTO {

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
    private Integer delFlag;

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
}
