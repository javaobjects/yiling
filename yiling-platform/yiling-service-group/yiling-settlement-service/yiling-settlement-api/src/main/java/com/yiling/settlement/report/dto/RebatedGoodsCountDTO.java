package com.yiling.settlement.report.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RebatedGoodsCountDTO extends BaseDTO {

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 以岭商品id
     */
    private Long ylGoodsId;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 购进渠道：1-大运河采购 2-京东采购 3-库存不足
     */
    private Integer purchaseChannel;
}
