package com.yiling.user.member.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员退款 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberReturnDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

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
     * 审核状态：1-待审核 2-已审核 3-已驳回
     */
    private Integer authStatus;

    /**
     * 审核人
     */
    private Long authUser;

    /**
     * 审核时间
     */
    private Date authTime;

    /**
     * 退款金额
     */
    private BigDecimal returnAmount;

    /**
     * 退款状态：1-待退款 2-退款中 3-退款成功 4-退款失败
     */
    private Integer returnStatus;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
