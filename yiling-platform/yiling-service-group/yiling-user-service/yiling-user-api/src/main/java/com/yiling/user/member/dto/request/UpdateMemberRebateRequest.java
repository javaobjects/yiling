package com.yiling.user.member.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 开通会员发送返利消息 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UpdateMemberRebateRequest implements Serializable {

    private static final long serialVersionUID = 7845235094969161173L;

    /**
     *  开通企业id
     */
    private Long eid;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员订单编号
     */
    private String orderNo;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 会员订单创建时间
     */
    private Date createTime;

    /**
     * 推广方id
     */
    private Long promoterId;

    /**
     * 数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手（枚举：MemberSourceEnum）
     */
    private Integer source;

}
