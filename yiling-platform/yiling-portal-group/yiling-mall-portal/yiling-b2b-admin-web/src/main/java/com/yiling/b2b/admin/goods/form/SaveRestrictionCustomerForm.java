package com.yiling.b2b.admin.goods.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveRestrictionCustomerForm
 * @描述
 * @创建时间 2022/12/8
 * @修改人 shichen
 * @修改时间 2022/12/8
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveRestrictionCustomerForm extends BaseForm {

    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Long goodsId;

    /**
     * 客户eid
     */
    @ApiModelProperty("客户eid")
    private Long customerEid;
}
