package com.yiling.b2b.admin.goods.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCustomerControlForm extends BaseForm {

    @ApiModelProperty(value = "客户设置 0没有设置 1全部 2部分设置", example = "1111")
    private Integer customerSet;

    @ApiModelProperty(value = "客户id集合", example = "1111")
    private Long customerId;

    @ApiModelProperty(value = "商品id", example = "1111")
    private Long goodsId;

}
