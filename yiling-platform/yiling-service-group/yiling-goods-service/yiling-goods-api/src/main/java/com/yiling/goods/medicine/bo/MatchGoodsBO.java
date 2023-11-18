package com.yiling.goods.medicine.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * 匹配结果
 * @author shuan
 */
@Data
public class MatchGoodsBO implements Serializable {

    private static final long serialVersionUID = -4672345329942342009L;
    /**
     * 匹配的状态
     */
    private Integer matchStatus;

    /**
     * 标准库商品ID
     */
    private Long standardId;

    /**
     * 售卖规格ID
     */
    private Long sellSpecificationsId;
}
