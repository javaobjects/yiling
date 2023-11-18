package com.yiling.dataflow.wash.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/3/9 0009
 */
@Data
public class UnlockSaleGoodsTagBO implements Serializable {

    /**
     * ID
     */
    private Long id;

    private Long tagCode;

    /**
     * crm系统对应客户名称
     */
    private String name;

}
