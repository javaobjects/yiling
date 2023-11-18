package com.yiling.hmc.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单明细表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderDetailDTO extends BaseDTO {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 保险id
     */
    private Long insuranceRecordId;

    /**
     * 药品id
     */
    private Long hmcGoodsId;

    /**
     * 药品id
     */
    private Long goodsId;

    /**
     * 售卖规格id
     */
    private Long sellSpecificationsId;

    /**
     * 药品名称
     */
    private String goodsName;

    /**
     * 参保价
     */
    private BigDecimal insurancePrice;

    /**
     * 保司跟以岭结算单价
     */
    private BigDecimal settlePrice;

    /**
     * 保司跟以岭结算额
     */
    private BigDecimal settleAmount;

    /**
     * 以岭跟终端结算单价
     */
    private BigDecimal terminalSettlePrice;

    /**
     * 以岭跟终端结算额
     */
    private BigDecimal terminalSettleAmount;

    /**
     * 药品规格
     */
    private String goodsSpecifications;

    /**
     * 购买数量
     */
    private Long goodsQuantity;

    /**
     * 商品小计
     */
    private BigDecimal goodsAmount;

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
