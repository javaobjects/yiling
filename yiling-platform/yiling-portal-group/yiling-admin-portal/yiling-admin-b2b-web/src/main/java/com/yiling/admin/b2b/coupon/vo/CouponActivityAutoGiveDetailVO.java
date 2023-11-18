package com.yiling.admin.b2b.coupon.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("自动发放优惠券活动详情VO")
public class CouponActivityAutoGiveDetailVO extends BaseVO {

    /**
     * 自动发券活动名称
     */
    @ApiModelProperty(value = "自动发券活动名称", position = 2)
    private String name;

    /**
     * 活动开始时间
     */
    @ApiModelProperty(value = "活动开始时间", position = 3)
    private Date beginTime;

    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间", position = 4)
    private Date endTime;

    /**
     * 自动发券类型（1-订单累计金额；2-会员自动发券）
     */
    @ApiModelProperty(value = "自动发券类型（1-订单累计金额；2-会员自动发券）", position = 5)
    private Integer type;

    /**
     * 指定商品(1:全部 2:指定)
     */
    @ApiModelProperty(value = "指定商品(1:全部 2:指定)", position = 6)
    private Integer conditionGoods;

    /**
     * 指定支付方式(1:全部 2:指定)
     */
    @ApiModelProperty(value = "指定支付方式(1:全部 2:指定)", position = 7)
    private Integer conditionPaymethod;

    /**
     * 已选支付方式，多个值用逗号隔开（1-在线支付；2-货到付款；3-账期）
     */
    @ApiModelProperty(value = "已选支付方式，多个值用逗号隔开（1-在线支付；2-货到付款；3-账期）", position = 8)
    private List<String> conditionPaymethodValueList;

    /**
     * 指定订单状态(1:全部 2:指定)
     */
    @ApiModelProperty(value = "指定订单状态(1:全部 2:指定)", position = 9)
    private Integer conditionOrderStatus;

    /**
     * 已选订单状态，多个值用逗号隔开（1-已发货）
     */
    @ApiModelProperty(value = "已选订单状态，多个值用逗号隔开（1-已发货）", position = 10)
    private List<String> conditionOrderStatusValueList;

    /**
     * 指定下单平台(1:全部 2:指定)
     */
    @ApiModelProperty(value = "指定下单平台(1:全部 2:指定)", position = 11)
    private Integer conditionOrderPlatform;

    /**
     * 已选下单平台，多个值用逗号隔开（1-B2B；2-销售助手）
     */
    @ApiModelProperty(value = "已选下单平台，多个值用逗号隔开（1-B2B；2-销售助手）", position = 12)
    private List<String> conditionOrderPlatformValueList;

    /**
     * 指定企业类型(1:全部 2:指定)
     */
    @ApiModelProperty(value = "指定企业类型(1:全部 2:指定)", position = 13)
    private Integer conditionEnterpriseType;

    /**
     * 已选企业类型，多个值用逗号隔开，字典enterprise_type（1-工业；2-商业；3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药房；8-诊所；9-民营医院；10-三级医院；11-二级医院；12-社区中心;13-县镇卫生院;14-社区站/村卫生所;15-县人民/中医院）
     */
    @ApiModelProperty(value = "已选企业类型，多个值用逗号隔开，字典enterprise_type", position = 14)
    private List<String> conditionEnterpriseTypeValueList;

    /**
     * 是否有推广码 1-是，2-否
     */
    @ApiModelProperty(value = "是否有推广码 1-是，2-否", position = 15)
    private Integer conditionPromotionCode;

    /**
     * 指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分指定会员）
     */
    @ApiModelProperty(value = "指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分指定会员）", position = 16)
    private Integer conditionUserType;

    /**
     * 累计金额/数量（如果是订单累计金额，则表示累计金额。如果是订单累计数量，则表示累计订单数量。）
     */
    @ApiModelProperty(value = "累计金额/数量（如果是订单累计金额，则表示累计金额。如果是订单累计数量，则表示累计订单数量。）", position = 17)
    private Integer cumulative;

    /**
     * 是否重复发放(1:仅发一次 2:重复发放多次)
     */
    @ApiModelProperty(value = "是否重复发放(1:仅发一次 2:重复发放多次)", position = 18)
    private Integer repeatGive;

    /**
     * 最多发放次数
     */
    @ApiModelProperty(value = "最多发放次数", position = 19)
    private Integer maxGiveNum;

    /**
     * 已发放数量
     */
    @ApiModelProperty(value = "已发放数量", position = 20)
    private Integer giveCount;

    /**
     * 状态：1-启用 2-停用 3-废弃
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用 3-废弃", position = 21)
    private Integer status;

    /**
     * 是否删除：0-否 1-是
     */
    @ApiModelProperty(value = "是否删除：0-否 1-是", position = 22)
    private Integer delFlag;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", position = 23)
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", position = 24)
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人", position = 25)
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", position = 26)
    private Date updateTime;

    /**
     * 创建人姓名
     */
    @ApiModelProperty("创建人姓名")
    private String createUserName;

    /**
     * 修改人姓名
     */
    @ApiModelProperty("修改人姓名")
    private String updateUserName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", position = 27)
    private String remark;

    /**
     * 关联优惠券列表
     */
    @ApiModelProperty(value = "关联优惠券列表", position = 28)
    private List<CouponActivityAutoGiveCouponDetailVO> couponActivityList;

    /**
     * 活动是否已开始：true-已开始 false-未开始
     */
    @ApiModelProperty(value = "活动是否已开始：true-已开始 false-未开始")
    private Boolean running;

}
