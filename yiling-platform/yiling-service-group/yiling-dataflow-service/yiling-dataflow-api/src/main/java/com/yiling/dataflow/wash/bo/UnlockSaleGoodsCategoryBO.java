package com.yiling.dataflow.wash.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2023/5/18
 */
@Data
public class UnlockSaleGoodsCategoryBO implements Serializable {
    private Long id;

    private Long categoryId;

    private String code;

    private String name;

    private Long goodsCount;

    private Integer categoryLevel;
}
