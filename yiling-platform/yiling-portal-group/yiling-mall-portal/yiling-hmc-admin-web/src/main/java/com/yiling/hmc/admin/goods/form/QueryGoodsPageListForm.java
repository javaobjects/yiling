package com.yiling.hmc.admin.goods.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "检索商品参数")
public class QueryGoodsPageListForm extends QueryPageListForm {


    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", example = "莲花")
    private String name;


    /**
     * 商品状态 1上架 2下架
     */
    @ApiModelProperty(value = "商品状态 1上架 2下架", example = "1")
    private Integer goodsStatus;

}