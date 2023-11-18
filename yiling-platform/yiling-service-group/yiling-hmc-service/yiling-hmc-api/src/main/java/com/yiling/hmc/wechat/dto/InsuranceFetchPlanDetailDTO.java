package com.yiling.hmc.wechat.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 拿药计划详情DTO
 *
 * @author fan.shen
 * @date 2022/4/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceFetchPlanDetailDTO extends BaseDTO {

    /**
     * 参保记录表id
     */
    private Long insuranceRecordId;

    /**
     * 保单支付记录表id
     */
    private Long recordPayId;

    /**
     * eId
     */
    private Long eId;

    /**
     * hmcGoodsId
     */
    private Long hmcGoodsId;

    /**
     * goodsId
     */
    private Long goodsId;

    /**
     * 售卖规格ID
     */
    private Long sellSpecificationsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 保司给以岭结算价格
     */
    private BigDecimal settlePrice;

    /**
     * 以岭跟终端结算单价
     */
    private BigDecimal terminalSettlePrice;


    /**
     * 商品市场价
     */
    private BigDecimal marketPrice;

    /**
     * 参保价
     */
    private BigDecimal insurancePrice;

    /**
     * 保司药品编码
     */
    private String insuranceGoodsCode;

    /**
     * 每月拿药量
     */
    private Long perMonthCount;

    /**
     * 规格信息
     */
    private String specificInfo;

}
