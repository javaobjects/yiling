package com.yiling.order.order.bo;

import com.yiling.framework.common.util.Constants;

import lombok.Builder;
import lombok.Data;

/**
 * @author zhigang.guo
 * @date: 2021/8/5
 */
@Data
@Builder
public class OrderDetailBatchBO implements java.io.Serializable {


    /**
     * 订单明细ID
     */

    private Long detailId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 数量
     */
    private Integer qty;



    /**
     * 获取发货批次关键字
     * @return
     */
    public String getBatchKey() {

        return this.detailId + Constants.SEPARATOR_MIDDLELINE + this.goodsId + Constants.SEPARATOR_MIDDLELINE + this.batchNo;
    }

}
