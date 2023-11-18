package com.yiling.f2b.web.enterprise.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/6/18
 */
@Data
public class DistributorInfoVO {

    @ApiModelProperty(value = "配送商EId")
    private Long distributorEid;

    @ApiModelProperty(value = "配送商名称")
    private String ename;

    @ApiModelProperty(value = "大件数量")
    private Integer bigPackage;

    @ApiModelProperty(value = "库存")
    private Long qty;

    @ApiModelProperty(value = "冻结库存")
    private Long frozenQty;
}
