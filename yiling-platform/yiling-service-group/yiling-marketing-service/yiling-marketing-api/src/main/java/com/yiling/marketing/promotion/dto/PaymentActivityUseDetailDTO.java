package com.yiling.marketing.promotion.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
public class PaymentActivityUseDetailDTO extends BaseDTO {

    /**
     * 活动企业eid
     */
    private Long eid;

    /**
     * 优惠活动id
     */
    private Long activityId;

    /**
     * 支付促销计算规则id
     */
    private Long ruleId;


    /**
     * 优惠活动类型（1-满减；2-满折）
     */
    private Integer type;

    /**
     * 优惠活动名称
     */
    private String name;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    private Integer sponsorType;

    /**
     * 优惠内容（减xx元/打xx折）
     */
    private BigDecimal discountValue;

    /**
     * 优惠规则-xx元/xx折
     */
    private String discountValueRules;

    /**
     * 如果不满足阶梯,差值金额
     */
    private BigDecimal diffAmount;

    /**
     * 预计优惠金额
     */
    private BigDecimal paymentDiscount;

    /**
     * 匹配的支付方式列表
     */
    private List<Integer> paymentMethodList;

    /**
     * 是否可用
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;


}
