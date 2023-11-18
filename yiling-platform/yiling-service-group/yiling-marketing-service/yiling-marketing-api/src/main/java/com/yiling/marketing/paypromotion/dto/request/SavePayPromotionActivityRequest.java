package com.yiling.marketing.paypromotion.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SavePayPromotionActivityRequest extends BaseRequest {

    /**
     * ("活动id-修改时需要")
     */
    private Long id;

    /**
     * 预算金额
     */
    private BigDecimal budgetAmount;

    private List<Integer> payType;
    /**
     * ("活动名称")
     */
    private String name;

    /**
     * ("活动分类（1-平台活动；2-商家活动；）")
     */
    private Integer sponsorType;

    /**
     * ("生效开始时间")
     */
    private Date beginTime;

    /**
     * ("生效开始时间")
     */
    private Integer bear;

    /**
     * ("生效结束时间")
     */
    private Date endTime;

    /**
     * ("运营备注")
     */
    private String operatingRemark;

    /**
     * ("商家范围类型（1-全部商家；2-指定商家；）")
     */
    private Integer conditionSellerType;

    /**
     * ("商品范围类型（1-全部商品；2-指定平台SKU；3-指定店铺SKU；）")
     */
    private Integer conditionGoodsType;

    /**
     * 指定部分会员用户类型（1-指定方案会员；2-指定推广方会员）逗号隔开
     */
    private List<Integer> conditionUserMemberType;

    /**
     * ("商户范围类型（1-全部客户；2-指定客户；3-指定范围客户）")
     */
    private Integer conditionBuyerType;

    /**
     * ("指定企业类型(1:全部类型 2:指定类型)")
     */
    private Integer conditionEnterpriseType;

    /**
     * ("指定企业类型值，多个值用逗号隔开，字典enterprise_type（3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药店；7-医疗机构；8-诊所）")
     */
    private List<Integer> conditionEnterpriseTypeValue;

    /**
     * ("指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-指定方案会员；5-指定推广方会员）")
     */
    private Integer conditionUserType;

    /**
     * ("其他(1-新客适用,多个值用逗号隔开)")
     */
    private List<Integer> conditionOther;

    /**
     * ("订单累计金额-限制支付方式值(支付方式：1-在线支付 2-线下支付 3-账期 ),逗号隔开")
     */
    private List<Integer> orderAmountPaymentType;

    /**
     * 生效条件(1-按金额)
     */
    private Integer conditionEffective;

    /**
     * 每个客户参数次数
     */
    private Integer maxGiveNum;

    /**
     * 每个客户参数次数
     */
    private List<SavePayPromotionRulesActivityRequest> calculateRules;
}
