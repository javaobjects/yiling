package com.yiling.b2b.admin.goods.form;

import com.yiling.b2b.admin.enterprisecustomer.form.QueryCustomerPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shichen
 * @类名 QueryRestrictionCustomerPageForm
 * @描述
 * @创建时间 2022/12/8
 * @修改人 shichen
 * @修改时间 2022/12/8
 **/
@Data
public class QueryRestrictionCustomerPageForm extends QueryCustomerPageListForm {

    @ApiModelProperty("商品id")
    private Long goodsId;
}
