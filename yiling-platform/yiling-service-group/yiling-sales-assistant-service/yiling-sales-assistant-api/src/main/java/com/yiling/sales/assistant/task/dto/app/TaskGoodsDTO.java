package com.yiling.sales.assistant.task.dto.app;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * app端任务详情页-商品
 * @author ray
 */
@Data
@Accessors(chain = true)
public class TaskGoodsDTO implements Serializable {


    private static final long serialVersionUID = -2331968434393579239L;
    private BigDecimal price;
    private BigDecimal commission;

    private String name;
    private String specifications;

    private String sellSpecifications;

    private String manufacturer;

    private Integer canSplit;

    private Integer stock;

    private Integer middlePackage;

    private String goodsPic;


}
