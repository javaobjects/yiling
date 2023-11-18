package com.yiling.admin.hmc.enterprise.form;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@Accessors(chain = true)
public class GoodsSaveForm implements Serializable {

    @ApiModelProperty("C端保险药品商家提成设置表id")
    private Long id;

    @ApiModelProperty("保险药品名称")
    private String goodsName;

    @ApiModelProperty("标准库商品id")
    private Long standardId;

    @ApiModelProperty("售卖规格ID")
    private Long sellSpecificationsId;

    @ApiModelProperty("商家售卖金额/盒")
    private BigDecimal salePrice;

    @ApiModelProperty("给终端结算额/盒")
    private BigDecimal terminalSettlePrice;
}
