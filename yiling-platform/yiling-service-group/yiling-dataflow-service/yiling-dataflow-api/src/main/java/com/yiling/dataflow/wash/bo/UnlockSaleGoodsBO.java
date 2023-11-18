package com.yiling.dataflow.wash.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/3/9 0009
 */
@Data
public class UnlockSaleGoodsBO implements Serializable {

    /**
     * ID
     */
    private Long id;

    private Long goodsCode;

    /**
     * crm系统对应客户名称
     */
    private String goodsName;

    /**
     * 规格
     */
    private String goodsSpec;


}
