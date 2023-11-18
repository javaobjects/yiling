package com.yiling.sales.assistant.task.dto.app;

import java.io.Serializable;

import lombok.Data;

/**
 * 多品销售-每个商品销售情况
 * @author gaoxinlei
 */
@Data
public class GoodsProgressDTO implements Serializable {


    private static final long serialVersionUID = 4805837297446686961L;
    private String goodsName;

    private String specifications;


    private String finishValue;

    private String goalValue;

    private String percent;


}
