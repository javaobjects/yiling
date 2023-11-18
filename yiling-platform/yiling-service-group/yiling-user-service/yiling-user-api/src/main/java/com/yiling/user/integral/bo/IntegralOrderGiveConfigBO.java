package com.yiling.user.integral.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单送积分配置表 BO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-03
 */
@Data
@Accessors(chain = true)
public class IntegralOrderGiveConfigBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 发放规则ID
     */
    private Long giveRuleId;

    /**
     * 商家范围：1-全部商家 2-指定商家
     */
    private Integer merchantScope;

    /**
     * 商品范围：1-全部商品 2-指定平台SKU 3-指定店铺SKU
     */
    private Integer goodsScope;

    /**
     * 客户范围：1-全部客户 2-指定客户 3-指定客户范围
     */
    private Integer customerScope;

    /**
     * 指定客户范围的企业类型：1-全部类型 2-指定类型
     */
    private Integer enterpriseType;

    /**
     * 指定客户范围的用户类型：1-全部用户 2-普通用户 3-全部会员 4-部分会员
     */
    private Integer userType;

    /**
     * 是否区分支付方式：1-全部支付方式 2-指定支付方式
     */
    private Integer paymentMethodFlag;

    /**
     * 指定范围企业类型集合（企业类型：3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所，参考企业类型的字典即可）
     */
    private List<Integer> enterpriseTypeList;

    /**
     * 支付方式：1-线上支付 2-线下支付 3-账期支付
     */
    private List<Integer> paymentMethodList;

    /**
     * 指定商家数量
     */
    private Integer sellerNum;

    /**
     * 平台SKU数量
     */
    private Integer platformGoodsNum;

    /**
     * 指定店铺SKU数量
     */
    private Integer enterpriseGoodsNum;

    /**
     * 指定客户数量
     */
    private Integer customerNum;

    /**
     * 指定会员方案数量
     */
    private Integer memberNum;

}
