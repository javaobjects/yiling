package com.yiling.ih.patient.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * HMC处方药品信息VO
 *
 * @author: fan.shen
 * @data: 2023/05/10
 */
@Data
public class HmcPrescriptionCommodityDTO implements java.io.Serializable {

    /**
     * ih平台药品id
     */
    private Integer ihGoodsId;

    /**
     * hmc药品id
     */
    private Integer hmcGoodsId;

    /**
     * hmc规格id
     */
    private Integer hmcSellSpecificationsId;

    /**
     * 药品名称
     */
    private String goodsName;

    /**
     * 药品规格
     */
    private String specifications;

    /**
     * 药品数量
     */
    private BigDecimal num;

    /**
     * 药品单价
     */
    private String goodsPrice;

    /**
     * 药品图片
     */
    private String picture;

    /**
     * 煎法-中药
     */
    private String decoction;


}
