package com.yiling.sales.assistant.task.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 任务选择商品
 * @author ray
 * @date 2021-9-28
 */
@Data
@Accessors(chain=true)
public class TaskSelectGoodsDTO implements Serializable {


    private static final long serialVersionUID = -6314172433576686882L;
    private Long goodsId;



    private String goodsName;

    /**
     * 销售规格
     */
    private String sellSpecifications;

    private String manufacturer;

    private String manufacturerAddress;

    //private String supplier;

    private Boolean canSplit;

    private String price;

    private String goodsPic;

    private Integer middlePackage;

    private Integer bigPackage;

   // private Long stock;

    private String sellUnit;

    private String licenseNo;
    /**
     * 出货价
     */
    private BigDecimal outPrice;

    /**
     * 商销价
     */
    private BigDecimal  sellPrice;
}
