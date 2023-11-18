package com.yiling.sales.assistant.app.task.vo;

import lombok.Data;

/**
 * 多品销售-每个商品销售情况
 * @author gaoxinlei
 */
@Data
public class GoodsProgressVO {


    private String goodsName;

    private String specifications;

    private String finishValue;

    private String goalValue;

    private String percent;

}
