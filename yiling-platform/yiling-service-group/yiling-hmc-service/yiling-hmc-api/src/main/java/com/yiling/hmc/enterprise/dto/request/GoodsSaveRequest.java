package com.yiling.hmc.enterprise.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@Accessors(chain = true)
public class GoodsSaveRequest implements Serializable {

    /**
     * C端保险药品商家提成设置表id
     */
    private Long id;

    /**
     * 保险药品名称
     */
    private String goodsName;

    /**
     * 标准库商品id
     */
    private Long standardId;

    /**
     * 售卖规格ID
     */
    private Long sellSpecificationsId;

    /**
     * 商品工业品还是商业品
     */
    private Integer enterpriseType;

    /**
     * 商家售卖金额/盒
     */
    private BigDecimal salePrice;

    /**
     * 给终端结算额/盒
     */
    private BigDecimal terminalSettlePrice;
}
