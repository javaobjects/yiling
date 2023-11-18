package com.yiling.b2b.app.goods.form;

import java.util.List;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEsGoodsSearchForm extends QueryPageListForm {
    /**
     * 搜索词
     */
    @ApiModelProperty(value = "搜索词")
    private String key;


    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID集合")
    private List<Long> eids;

    /**
     * 是否有库存 0查看所有商品 1查询有库存商品
     */
    @ApiModelProperty(value = "是否有库存 false查看所有商品 true查询有库存商品")
    private Boolean hasStock;

    /**
     * 价格排序
     */
    @ApiModelProperty(value = "价格排序 true倒叙 false正序")
    private Boolean priceDesc;

    /**
     * 销量排序
     */
    @ApiModelProperty(value = "销量排序 true倒叙 false正序")
    private Boolean saleDesc;

}
