package com.yiling.admin.b2b.strategy.vo;

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
public class StrategyActivityVO extends BaseVO {

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

    @ApiModelProperty("策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员")
    private Integer strategyType;

    @ApiModelProperty("状态：1-启用 2-停用 3-废弃")
    private Integer status;

    @ApiModelProperty("运营备注")
    private String operatingRemark;

    @ApiModelProperty("费用承担方（1-平台承担；2-商家承担；）")
    private Integer bear;

    @ApiModelProperty("选择平台（1-B2B；2-销售助手）")
    private List<Integer> platformSelected;

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

    @ApiModelProperty("订单累计金额-限制订单状态(1-发货计算；2-下单计算)")
    private Integer orderAmountStatusType;

    @ApiModelProperty("订单累计金额-限制订单状态值(1-已发货),逗号隔开")
    private String orderAmountStatus;

    @ApiModelProperty("订单累计金额-限制支付方式(1-全部支付方式;2-部分支付方式)")
    private Integer orderAmountPayType;

    @ApiModelProperty("订单累计金额-限制支付方式值(支付方式：1-线下支付 2-账期 3-预付款 4-在线支付),逗号隔开")
    private List<Integer> orderAmountPaymentType;

    @ApiModelProperty("订单累计金额-阶梯匹配方式(1-按单累计匹配;2-活动结束整体匹配;3-按单匹配)")
    private Integer orderAmountLadderType;

    @ApiModelProperty("时间周期-频率(1-按周;2-按月)")
    private Integer cycleRate;

    @ApiModelProperty("购买会员-是否每月固定日期重复执行(1-是；2-否；)")
    private Integer memberRepeat;

    @ApiModelProperty("购买会员-每月固定日期重复执行(逗号隔开)")
    private List<Integer> memberRepeatDay;

    @ApiModelProperty("购买会员-执行次数")
    private Integer memberTimes;

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


}
