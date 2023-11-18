package com.yiling.admin.b2b.member.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员购买记录 VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberBuyRecordVO extends BaseVO {

    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String orderNo;

    /**
     * 会员ID
     */
    @ApiModelProperty("会员ID")
    private Long memberId;

    /**
     * 会员名称
     */
    @ApiModelProperty("会员名称")
    private String memberName;

    /**
     * 终端ID
     */
    @ApiModelProperty("终端ID")
    private Long eid;

    /**
     * 终端名称
     */
    @ApiModelProperty("终端名称")
    private String ename;

    /**
     * 终端地址
     */
    @ApiModelProperty("终端地址")
    private String address;

    /**
     * 企业管理员手机号
     */
    @ApiModelProperty("企业管理员手机号")
    private String contactorPhone;

    /**
     * 购买规则
     */
    @ApiModelProperty("购买规则")
    private String buyRule;

    /**
     * 购买时间
     */
    @ApiModelProperty("购买时间")
    private Date createTime;

    /**
     * 推广方ID
     */
    @ApiModelProperty("推广方ID")
    private Long promoterId;

    /**
     * 推广方名称
     */
    @ApiModelProperty("推广方名称")
    private String promoterName;

    /**
     * 推广人ID
     */
    @ApiModelProperty("推广人ID")
    private Long promoterUserId;

    /**
     * 推广方人名称
     */
    @ApiModelProperty("推广方人名称")
    private String promoterUserName;

    /**
     * 会员开始时间
     */
    @ApiModelProperty("会员开始时间")
    private Date startTime;

    /**
     * 会员结束时间
     */
    @ApiModelProperty("会员结束时间")
    private Date endTime;

    /**
     * 支付方式
     */
    @ApiModelProperty("支付方式")
    private Integer payMethod;

    /**
     * 支付渠道
     */
    @ApiModelProperty("支付渠道")
    private String payChannel;

    /**
     * 支付方式名称
     */
    @ApiModelProperty("支付方式名称")
    private String payMethodName;

    /**
     * 原价
     */
    @ApiModelProperty("原价")
    private BigDecimal originalPrice;

    /**
     * 优惠金额
     */
    @ApiModelProperty("优惠金额")
    private BigDecimal discountAmount;

    /**
     * 支付金额
     */
    @ApiModelProperty("支付金额")
    private BigDecimal payAmount;

    /**
     * 退款金额
     */
    @ApiModelProperty("退款金额")
    private BigDecimal returnAmount;

    /**
     * 是否展示退款按钮
     */
    @ApiModelProperty("是否展示退款按钮")
    private Boolean showReturnFlag;

    /**
     * 是否退款：1-未退款 2-已退款（为了与查询保持统一，故使用1和2）
     */
    @ApiModelProperty("是否退款：1-未退款 2-已退款")
    private Integer returnFlag;

    /**
     * 是否过期：1-未过期 2-已过期（为了与查询保持统一，故使用1和2）
     */
    @ApiModelProperty("是否过期：1-未过期 2-已过期")
    private Integer expireFlag;

    /**
     * 开通类型：1-首次开通 2-续费开通
     */
    @ApiModelProperty("开通类型：1-首次开通 2-续费开通（见字典：member_open_type）")
    private Integer openType;

    /**
     * 数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手
     */
    @ApiModelProperty("数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手（见字典：member_data_source）")
    private Integer source;

    /**
     * 是否取消：0-否 1-是
     */
    @ApiModelProperty("是否取消：0-否 1-是")
    private Integer cancelFlag;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人", hidden = true)
    private Long updateUser;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private String updateUserName;

    /**
     * 取消时间
     */
    @ApiModelProperty("取消时间")
    private Date updateTime;

}
