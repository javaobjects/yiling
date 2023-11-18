package com.yiling.open.webservice.json;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/8/3
 */
@Data
public class GoodsCustomerPriceJson {
    private String     custno;
    private String     orgno;
    private BigDecimal disprice;
    private String     matno;
}
