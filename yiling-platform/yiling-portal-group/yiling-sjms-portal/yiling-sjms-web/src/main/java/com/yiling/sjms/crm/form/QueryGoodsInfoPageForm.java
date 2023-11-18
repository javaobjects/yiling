package com.yiling.sjms.crm.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryGoodsInfoPageForm
 * @描述
 * @创建时间 2023/4/10
 * @修改人 shichen
 * @修改时间 2023/4/10
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsInfoPageForm extends QueryPageListForm {
    /**
     * crm商品编码
     */
    @ApiModelProperty(value = "crm商品编码")
    private Long goodsCode;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String goodsName;

    /**
     * 是否团购 0：否 1：是
     */
    @ApiModelProperty(value = "是否团购 0：否 1：是")
    private Integer isGroupPurchase;

    /**
     * 品类id
     */
    @ApiModelProperty(value = "品类id")
    private Long categoryId;

    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品类名称")
    private String category;

    /**
     * 0:有效，1：失效
     */
    @ApiModelProperty(value = "0:有效，1：失效")
    private Integer status;
}
