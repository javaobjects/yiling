package com.yiling.search.goods.dto.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author: shuang.zhang
 * @date: 2021/11/8
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EsGoodsInventoryIndexRequest implements Serializable {

    private static final long serialVersionUID = 291479953511743718L;

    private Long goodsId;

    private Long availableQty;

    private Integer hasB2bStock;
}
