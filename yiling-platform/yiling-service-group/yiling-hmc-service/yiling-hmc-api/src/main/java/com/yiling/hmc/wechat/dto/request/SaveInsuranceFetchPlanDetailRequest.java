package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 创建拿药计划明细
 *
 * @author fan.shen
 * @date 2022-03-28
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveInsuranceFetchPlanDetailRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 参保记录表id
     */
    private Long insuranceRecordId;

    /**
     * 保单支付记录表id
     */
    private Long recordPayId;

    /**
     * hmcGoodsId
     */
    private Long hmcGoodsId;

    /**
     * goodsId
     */
    private Long goodsId;

    /**
     * eId
     */
    private Long eid;

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
     * 以岭给终端结算价格
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
