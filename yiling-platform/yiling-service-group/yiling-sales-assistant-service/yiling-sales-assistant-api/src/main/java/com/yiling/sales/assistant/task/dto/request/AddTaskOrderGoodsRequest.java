package com.yiling.sales.assistant.task.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *  任务订单商品添加实体
 * </p>
 *
 * @author gxl
 * @since 2020-04-25
 */
@Data
@Accessors(chain = true)
public class AddTaskOrderGoodsRequest implements Serializable {


    private static final long serialVersionUID = -2521236673402278318L;
    /**
     *商品主键
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodName;

    /**
     *商品数量
     */
    private Integer amount;

    /**
     *商品单价
     */
    private BigDecimal price;

    /**
     *商品规格
     */
    private String specifications;
}
