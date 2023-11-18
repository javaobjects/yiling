package com.yiling.marketing.presale.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.marketing.strategy.dto.StrategyAmountLadderDTO;

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
public class PresaleActivityDTO extends BaseDTO {


    /**
     * 活动名称
     */
    private String name;

    /**
     * 活动分类（1-平台活动；2-商家活动；）
     */
    private Integer sponsorType;

    /**
     * 生效开始时间
     */
    private Date beginTime;

    /**
     * 生效结束时间
     */
    private Date endTime;

    /**
     * 状态：1-启用 2-停用 3-废弃
     */
    private Integer status;

    /**
     * 运营备注
     */
    private String operatingRemark;

    /**
     * 费用承担方（1-平台承担；2-商家承担；）
     */
    private Integer bear;

    /**
     * 平台承担比例
     */
    private BigDecimal platformRatio;

    /**
     * 商家承担比例
     */
    private BigDecimal businessRatio;

    /**
     * 选择平台（1-B2B；2-销售助手）逗号隔开
     */
    private String platformSelected;

    /**
     * 选择端口（1-B端；2-c端）逗号隔开
     */
    private String portSelected;

    /**
     * 商户范围类型（1-全部客户；2-指定客户；3-指定范围客户）
     */
    private Integer conditionBuyerType;

    /**
     * 指定企业类型(1:全部类型 2:指定类型)
     */
    private Integer conditionEnterpriseType;

    /**
     * 指定企业类型值，多个值用逗号隔开，字典enterprise_type（3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药店；7-医疗机构；8-诊所）
     */
    private String conditionEnterpriseTypeValue;

    /**
     * 指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-指定方案会员；5-指定推广方会员）
     */
    private Integer conditionUserType;

    /**
     * 其他(1-新客适用,多个值用逗号隔开)
     */
    private String conditionOther;

    /**
     * 预售类型（1-定金预售；2-全款预售）
     */
    private Integer presaleType;

    /**
     * 支付定金开始时间
     */
    private Date depositBeginTime;

    /**
     * 支付定金结束时间
     */
    private Date depositEndTime;

    /**
     * 支付尾款开始时间
     */
    private Date finalPayBeginTime;

    /**
     * 支付尾款结束时间
     */
    private Date finalPayEndTime;

    /**
     * 预算金额
     */
    private BigDecimal budgetAmount;

    /**
     * 商品信息
     */
    private List<PresaleGoodsLimitDTO> presaleGoodsLimitForms;

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

    /**
     * GOODSID
     */
    private Long goodsId;

    /**
     * 每人最小预定量
     */
    private Integer minNum;

    /**
     * 当前用户已经购买的数量
     */
    private Integer currentHasBuyNum;

    /**
     * 所有用户已经购买的数量
     */
    private Integer allHasBuyNum;

    /**
     * 每人最大预定量
     */
    private Integer maxNum;

    /**
     * 某个活动下面指定商品所有人最大预定量
     */
    private Integer allNum;

    /**
     * 某个活动下面关联的订单数量
     */
    private Integer presaleOrderNum;

    /**
     * 单个商品 预售价
     */
    private BigDecimal  presaleAmount;
    /**
     * '定金比例'
     */
    private BigDecimal  depositRatio;
    /**
     * 促销方式：0-无 1-定金膨胀 2-尾款立减'
     */
    private Integer  goodsPresaleType;
    /**
     * '定金膨胀倍数'
     */
    private BigDecimal  expansionMultiplier;
    /**
     * '尾款立减金额'
     */
    private BigDecimal  finalPayDiscountAmount;

    /**
     * '指定客户数量'
     */
    private Integer buyerNum;

    /**
     * '指定方案会员数量'
     */
    private Integer memberNum;

    /**
     * '指定推广方会员数量'
     */
    private Integer promoterNnm;


    /**
     * 定金
     */
    private BigDecimal  deposit;

    /**
     * 尾款
     */
    private BigDecimal  finalPayment;

    /**
     * orderUnm
     */
    private Integer orderNum;
}
