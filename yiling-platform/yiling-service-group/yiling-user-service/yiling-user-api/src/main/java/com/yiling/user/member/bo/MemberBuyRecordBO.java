package com.yiling.user.member.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员购买记录 BO
 * </p>
 *
 * @author lun.yu
 * @date 2022-10-18
 */
@Data
@Accessors(chain = true)
public class MemberBuyRecordBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员名称
     */
    private String memberName;

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
     * 数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手
     */
    private Integer source;

    /**
     * 开通类型：1-首次开通 2-续费开通
     */
    private Integer openType;

    /**
     * 购买规则ID
     */
    private Long buyStageId;

    /**
     * 购买规则
     */
    private String buyRule;

    /**
     * 会员开始时间
     */
    private Date startTime;

    /**
     * 会员结束时间
     */
    private Date endTime;

    /**
     * 有效天数
     */
    private Integer validDays;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 提交退款金额
     */
    private BigDecimal submitReturnAmount;

    /**
     * 退款金额
     */
    private BigDecimal returnAmount;

    /**
     * 是否取消：0-否 1-是
     */
    private Integer cancelFlag;

    /**
     * 创建人id
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

}
