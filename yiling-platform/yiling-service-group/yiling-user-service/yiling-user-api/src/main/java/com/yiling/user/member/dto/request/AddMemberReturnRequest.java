package com.yiling.user.member.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-新增会员退款 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddMemberReturnRequest extends BaseRequest {

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 购买企业ID
     */
    private Long eid;

    /**
     * 购买企业名称
     */
    private String ename;

    /**
     * 支付方式
     */
    private Integer payMethod;

    /**
     * 支付渠道
     */
    private String payChannel;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 申请人
     */
    private Long applyUser;

    /**
     * 申请时间
     */
    private Date applyTime;

    /**
     * 退款原因
     */
    private String returnReason;

    /**
     * 退款备注
     */
    private String returnRemark;

    /**
     * 退款金额
     */
    private BigDecimal returnAmount;

}
