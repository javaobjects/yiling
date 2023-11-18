package com.yiling.goods.medicine.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author shichen
 * @类名 GoodsLineBO
 * @描述
 * @创建时间 2022/11/10
 * @修改人 shichen
 * @修改时间 2022/11/10
 **/
@Data
public class GoodsLineBO implements Serializable {

    private Long goodsId;

    /**
     * 0:未启用产品线  1：启用产品线
     */
    private Integer mallStatus;

    /**
     * 0:未启用产品线  1：启用产品线
     */
    private Integer popStatus;
}
