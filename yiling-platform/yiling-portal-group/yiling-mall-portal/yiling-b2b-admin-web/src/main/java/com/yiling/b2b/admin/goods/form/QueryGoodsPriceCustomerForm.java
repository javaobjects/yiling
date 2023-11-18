package com.yiling.b2b.admin.goods.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 客户定价查询 form
 * </p>
 *
 * @author lun.yu
 * @date 2021-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsPriceCustomerForm extends BaseForm {

    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    @NotNull
    private Long eid;

    /**
     * 客户企业ID
     */
    @ApiModelProperty(value = "客户企业ID")
    @NotNull(message = "客户企业ID不能为空")
    private Long customerEid;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    @NotNull(message = "商品ID不能为空")
    private Long goodsId;

}
