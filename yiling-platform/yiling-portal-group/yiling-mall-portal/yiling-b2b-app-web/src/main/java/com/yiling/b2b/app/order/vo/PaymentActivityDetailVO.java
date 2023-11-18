package com.yiling.b2b.app.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: zhigang.guo
 * @date: 2022/08/08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PaymentActivityDetailVO extends BaseVO {
    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private Long eid;

    @ApiModelProperty(value = "是否选中")
    private Boolean isSelected;

    @ApiModelProperty(value = "差值金额")
    private BigDecimal diffAmount;

    /**
     * 匹配的支付方式列表
     */
    @ApiModelProperty(value = "匹配的支付方式列表", hidden = true)
    @JsonIgnore
    private List<Integer> paymentMethodList;

    /**
     * 是否可用
     */
    @ApiModelProperty(value = "是否可用")
    private Boolean enabled;

    /**
     * 活动类型（1-满减；2-满折）
     */
    @ApiModelProperty(value = "活动类型（1-满减；2-满折)")
    private Integer type;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String name;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    @ApiModelProperty(value = "活动分类（1-平台活动；2-商家活动)")
    private Integer sponsorType;

    /**
     * 优惠规则-xx元/xx折
     */
    @ApiModelProperty(value = "优惠规则-xx元/xx折")
    private String discountValueRules;

    /**
     * 优惠活动id
     */
    @ApiModelProperty(value = "优惠活动Id")
    private Long activityId;

    /**
     * 活动区间Id
     */
    @ApiModelProperty(value = "活动区间Id")
    private Long activityRuleIdId;

    /**
     * 实时实际优惠金额
     */
    @ApiModelProperty(value = "预计优惠金额")
    private BigDecimal paymentDiscount;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;


    /**
     * 活动创建时间
     */
    @ApiModelProperty(value = "活动创建时间")
    private Date createTime;


}
