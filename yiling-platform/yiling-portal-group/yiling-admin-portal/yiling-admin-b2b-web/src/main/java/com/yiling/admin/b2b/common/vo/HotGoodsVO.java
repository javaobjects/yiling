package com.yiling.admin.b2b.common.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 精选商品返回信息
 *
 * @author: yong.zhang
 * @date: 2021/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HotGoodsVO extends BaseVO {

//    @ApiModelProperty(value = "id")
//    private String id;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品图片地址")
    private String pic;

    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    @ApiModelProperty(value = "销售规格")
    private String sellSpecifications;

    @ApiModelProperty(value = "挂网价")
    private BigDecimal price;

    @ApiModelProperty(value = "在售商家数量")
    private Integer sellerCount;
}
