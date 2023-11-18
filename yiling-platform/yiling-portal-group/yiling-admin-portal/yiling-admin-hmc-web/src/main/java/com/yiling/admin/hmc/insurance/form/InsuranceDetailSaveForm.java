package com.yiling.admin.hmc.insurance.form;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 保险商品明细新增
 *
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@Accessors(chain = true)
public class InsuranceDetailSaveForm implements Serializable {

    @ApiModelProperty("药品id")
    private Long controlId;

    @ApiModelProperty("药品名称")
    private String goodsName;

    @ApiModelProperty("保司跟以岭的结算单价")
    private BigDecimal settlePrice;

    @ApiModelProperty("每月1次，每次拿多少盒")
    private Integer monthCount;
    
    @ApiModelProperty("保司药品编码")
    private String insuranceGoodsCode;
}
