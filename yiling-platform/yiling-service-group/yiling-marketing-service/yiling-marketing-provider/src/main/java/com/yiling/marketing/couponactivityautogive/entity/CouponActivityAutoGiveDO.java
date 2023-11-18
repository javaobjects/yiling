package com.yiling.marketing.couponactivityautogive.entity;

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
 * 自动发券活动表
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_coupon_activity_auto_give")
public class CouponActivityAutoGiveDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 自动发券活动名称
     */
    private String name;

    /**
     * 活动开始时间
     */
    private Date beginTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 自动发券类型（1-订单累计金额；2-会员自动发券）
     */
    private Integer type;

    /**
     * 指定商品(1:全部 2:指定)
     */
    private Integer conditionGoods;

    /**
     * 指定支付方式(1:全部 2:指定)
     */
    private Integer conditionPaymethod;

    /**
     * 已选支付方式，多个值用逗号隔开（1-在线支付；2-货到付款；3-账期）
     */
    private String conditionPaymethodValue;

    /**
     * 指定订单状态(1:全部 2:指定)
     */
    private Integer conditionOrderStatus;

    /**
     * 已选订单状态，多个值用逗号隔开（1-已发货）
     */
    private String conditionOrderStatusValue;

    /**
     * 指定下单平台(1:全部 2:指定)
     */
    private Integer conditionOrderPlatform;

    /**
     * 已选下单平台，多个值用逗号隔开（1-B2B；2-销售助手）
     */
    private String conditionOrderPlatformValue;

    /**
     * 指定企业类型(1:全部 2:指定)
     */
    private Integer conditionEnterpriseType;

    /**
     * 已选企业类型，多个值用逗号隔开，字典enterprise_type（1-工业；2-商业；3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药房；8-诊所；9-民营医院；10-三级医院；11-二级医院；12-社区中心;13-县镇卫生院;14-社区站/村卫生所;15-县人民/中医院）
     */
    private String conditionEnterpriseTypeValue;

    /**
     * 是否有推广码 1-是，2-否
     */
    private Integer conditionPromotionCode;

    /**
     * 指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分指定会员）
     */
    private Integer conditionUserType;

    /**
     * 累计金额/数量（0表示无限制。如果是订单累计金额，则表示累计金额。如果是订单累计数量，则表示累计订单数量。）
     */
    private Integer cumulative;

    /**
     * 是否重复发放(1:仅发一次 2:重复发放多次)
     */
    private Integer repeatGive;

    /**
     * 最多发放次数
     */
    private Integer maxGiveNum;

    /**
     * 已发放数量
     */
    private Integer giveCount;

    /**
     * 状态：1-启用 2-停用 3-废弃
     */
    private Integer status;

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
