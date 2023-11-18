package com.yiling.settlement.report.bo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;

import lombok.Data;

/**
 * @author: dexi.yao
 * @date: 2022-05-20
 */
@Data
public class MemberOrderSyncBO {

    /**
     * id
     */
    private Long id;

    /**
     * 购买企业ID
     */
    private Long eid;

    /**
     * 推广方ID
     */
    private Long promoterId;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手
     */
    private Integer source;

    /**
     * 订单金额
     */
    private BigDecimal payAmount;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 实际金额
     */
    private BigDecimal amount;

    /**
     * 订单金额
     */
    private BigDecimal originalPrice;

    /**
     * 订单状态：10-待支付 20-支付成功 30-支付失败
     */
    private Integer status;

    /**
     * 退款状态：1-未退款 2-已退款
     */
    private Integer refundStatus;

    /**
     * 下单时间
     */
    private Date orderCreateTime;

    /**
     * 退款时间
     */
    private Date refundTime;

    /**
     * 报表计算状态：1-未计算 2-已计算
     */
    private Integer reportStatus;

    /**
     * 报表ID
     */
    private Long reportId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        MemberOrderSyncBO that = (MemberOrderSyncBO) o;
        return orderNo.equals(that.orderNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNo);
    }
}
