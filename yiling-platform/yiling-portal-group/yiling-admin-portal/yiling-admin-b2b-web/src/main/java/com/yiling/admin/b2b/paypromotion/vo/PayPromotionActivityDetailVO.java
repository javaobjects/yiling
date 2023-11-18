package com.yiling.admin.b2b.paypromotion.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
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
public class PayPromotionActivityDetailVO extends BaseVO {

    @ApiModelProperty("活动名称")
    private String name;

    @ApiModelProperty("活动类型（1-策略满赠,2-支付促销）")
    private Integer type;

    @ApiModelProperty("活动分类（1-平台活动；2-商家活动；）")
    private Integer sponsorType;

    @ApiModelProperty("生效开始时间")
    private Date beginTime;

    @ApiModelProperty("生效结束时间")
    private Date endTime;


    @ApiModelProperty("状态：1-启用 2-停用 3-废弃")
    private Integer status;

    @ApiModelProperty("运营备注")
    private String operatingRemark;

    @ApiModelProperty("费用承担方（1-平台承担；2-商家承担；）")
    private Integer bear;


    @ApiModelProperty("商家范围类型（1-全部商家；2-指定商家；）")
    private Integer conditionSellerType;

    @ApiModelProperty("商品范围类型（1-全部商品；2-指定平台SKU；3-指定店铺SKU；）")
    private Integer conditionGoodsType;

    @ApiModelProperty("商户范围类型（1-全部客户；2-指定客户；3-指定范围客户）")
    private Integer conditionBuyerType;

    @ApiModelProperty("指定企业类型(1:全部类型 2:指定类型)")
    private Integer conditionEnterpriseType;

    @ApiModelProperty("指定企业类型值，多个值用逗号隔开，字典enterprise_type（3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药店；7-医疗机构；8-诊所）")
    private List<Integer> conditionEnterpriseTypeValue;

    @ApiModelProperty("指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分会员）")
    private Integer conditionUserType;
    
    @ApiModelProperty("指定部分会员用户类型（1-指定方案会员；2-指定推广方会员）逗号隔开")
    private List<Integer> conditionUserMemberType;

    @ApiModelProperty("其他(1-新客适用,多个值用逗号隔开)")
    private List<Integer> conditionOther;

    @ApiModelProperty("订单累计金额-限制支付方式值(支付方式：1-线下支付 2-账期 3-预付款 4-在线支付),逗号隔开")
    private List<Integer> payType;

    @ApiModelProperty("生效条件(1-按金额)")
    private Integer conditionEffective;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("活动进度 0-全部 1-未开始 2-进行中 3-已结束")
    private Integer progress;

    @ApiModelProperty(value = "活动是否已开始：true-已开始 false-未开始")
    private Boolean running;

    @ApiModelProperty("策略满赠客户数量")
    private Integer strategyBuyerLimitCount;

    @ApiModelProperty("策略满赠店铺SKU数量")
    private Integer strategyEnterpriseGoodsLimitCount;

    @ApiModelProperty("策略满赠会员方案数量")
    private Integer strategyMemberLimitCount;

    @ApiModelProperty("策略满赠推广方会员数量")
    private Integer strategyPromoterMemberLimitCount;

    @ApiModelProperty("策略满赠平台SKU数量")
    private Integer strategyPlatformGoodsLimitCount;

    @ApiModelProperty("策略满赠商家数量")
    private Integer strategySellerLimitCount;

    @ApiModelProperty("生效条件&计算规则")
    private List<PayPromotionCalculateVO> calculateRules;

    /**
     * 预算金额
     */
    @ApiModelProperty("预算金额")
    private BigDecimal budgetAmount;

    /**
     * 单个支付促销一共使用多少次
     */
    private BigDecimal totalNum;

    /**
     * 每个客户参数次数
     */
    @ApiModelProperty("每个客户参数次数&计算规则")
    private Integer maxGiveNum;
}
