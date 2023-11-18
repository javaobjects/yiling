package com.yiling.hmc.insurance.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 保险商品明细新增
 *
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@Accessors(chain = true)
public class InsuranceDetailSaveRequest implements Serializable {

    /**
     * 药品id
     */
    private Long controlId;

    /**
     * 药品名称
     */
    private String goodsName;

    /**
     * 保司跟以岭的结算单价
     */
    private BigDecimal settlePrice;

    /**
     * 每月1次，每次拿多少盒
     */
    private Integer monthCount;

    /**
     * 保司药品编码
     */
    private String insuranceGoodsCode;
}
