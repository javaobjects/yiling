package com.yiling.order.order.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 首单信息表
 * </p>
 *
 * @author zhigang.guo
 * @date 2022-05-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("order_first_info")
public class OrderFirstInfoDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 订单Id
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单类型：1-POP订单,2-B2B订单
     */
    private Integer orderType;

    /**
     * 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private Integer orderSource;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 买家企业ID
     */
    private Long buyerEid;

    /**
     * 买家企业ID
     */
    private String buyerEname;

    /**
     * 支付类型：1-线下支付 2-在线支付
     */
    private Integer paymentType;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
     */
    private Integer paymentMethod;

    /**
     * 支付金额
     */
    private BigDecimal paymentAmount;

    /**
     * 订单金额
     */
    private BigDecimal totalAmount;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

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


}
