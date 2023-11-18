package com.yiling.sales.assistant.task.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @author:gxl
 * @description: 任务关联商品
 * @date: Created in 19:55 2021/9/13
 * @modified By:
 */
@Data
public class TaskGoodsDTO implements Serializable {

    private static final long serialVersionUID = -310436594196259360L;
    private Long taskGoodsId;

    private Long goodsId;

    private BigDecimal price;
    private BigDecimal commission;

    private String  commissionRate;

    // 商品其他属性调用远程接口查询
    private String goodsName;
    private String specifications;

    /**
     * 销售规格
     */
    private String sellSpecifications;

    private String manufacturer;
    private String supplier;
    private Integer canSplit;

    /**
     * 商业调拨价格
     */
    private BigDecimal businessPrice;

    private String goodsPic;

    private Integer middlePackage;

    private Integer bigPackage;

    private Integer stock;

    private List<Long> taskIds;

    private Integer taskType;

    /**
     * 出货价
     */
    private BigDecimal outPrice;

    /**
     * 商销价
     */
    private BigDecimal  sellPrice;

    private Date createTime;
}
