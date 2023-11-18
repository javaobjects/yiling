package com.yiling.user.member.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员购买生成订单DTO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberOrderDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 交易单号
     */
    private String tradeNo;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 购买企业ID
     */
    private Long eid;

    /**
     * 购买企业名称
     */
    private String ename;

    /**
     * 购买条件ID
     */
    private Long buyStageId;

    /**
     * 支付方式
     */
    private Integer payMethod;

    /**
     * 第三方支付方式，如：yeePay
     */
    private String payWay;

    /**
     * 支付渠道
     */
    private String payChannel;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 订单金额（支付金额）
     */
    private BigDecimal payAmount;

    /**
     * 订单状态：10-待支付 20-支付成功 30-支付失败
     */
    private Integer status;

    /**
     * 是否取消：0-否 1-是
     */
    private Integer cancelFlag;

    /**
     * 推广方ID
     */
    private Long promoterId;

    /**
     * 推广方名称
     */
    private String promoterName;

    /**
     * 推广人ID
     */
    private Long promoterUserId;

    /**
     * 推广方人名称
     */
    private String promoterUserName;

    /**
     * 创建人id
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人id
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

}
