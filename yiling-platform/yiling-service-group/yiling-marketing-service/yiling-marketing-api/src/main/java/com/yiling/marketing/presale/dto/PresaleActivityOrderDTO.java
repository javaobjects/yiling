package com.yiling.marketing.presale.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 营销活动主表
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PresaleActivityOrderDTO extends BaseDTO {

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 活动名称
     */
    private String name;


    /**
     * 创建人
     */
    private Long eid;

    /**
     * 创建人名称
     */
    private String ename;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单id
     */
    private Long goodsId;

    /**
     * 订单id
     */
    private String goodsName;

    /**
     * 规格
     */
    private String sellSpecifications;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 商家-卖家
     */
    private String sellerEname;

    /**
     * 商家-卖家
     */
    private BigDecimal presalePrice;

    /**
     * 购买数量
     */
    private Integer goodsQuantity;

    /**
     * 预售类型
     */
    private Integer presaleType;

    /**
     * 预售类型
     */
    private String presaleTypeStr;

    /**
     * 定金比例
     */
    private BigDecimal depositRatio;

    private String goodsPresaleType;

    private BigDecimal depositAmount;

    private BigDecimal balanceAmount;

    private BigDecimal discountAmount;

    private BigDecimal totalAmount;

    /**
     * 商家-卖家
     */
    private String status;

    /**
     * 定金比例
     */
    private String depositRatioStr;
}
