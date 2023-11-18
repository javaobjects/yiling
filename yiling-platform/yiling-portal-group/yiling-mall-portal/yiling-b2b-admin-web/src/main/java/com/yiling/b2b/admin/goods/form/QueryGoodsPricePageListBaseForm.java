package com.yiling.b2b.admin.goods.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询客户/客户分组定价 基础Form
 * @author: yuecheng.chen
 * @date: 2021/6/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsPricePageListBaseForm extends QueryPageListForm {

    @ApiModelProperty(value = "商品Id（调整客户定价/客户分组定价必传）")
    private Long goodsId;

    @ApiModelProperty(value = "商品所属企业Id（调整客户定价/客户分组定价必传）")
    private Long eid;

}
