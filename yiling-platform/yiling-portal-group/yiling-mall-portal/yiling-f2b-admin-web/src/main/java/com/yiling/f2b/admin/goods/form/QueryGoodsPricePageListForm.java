package com.yiling.f2b.admin.goods.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询商品定价 Form
 * @author: yuecheng.chen
 * @date: 2021/6/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsPricePageListForm extends QueryGoodsPricePageListBaseForm {

    /**
     * 注册证号
     */
    @ApiModelProperty(value = "批准文号", example = "Z109090")
    private String licenseNo;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", example = "莲花")
    private String name;

    /**
     * 商品状态：1上架 2下架 5待审核 6驳回
     */
    @ApiModelProperty(value = "品状态：1上架 2下架 5待审核 6驳回（只有1是上架状态，其余全为下架）", example = "1")
    private Integer goodsStatus;

    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

}
