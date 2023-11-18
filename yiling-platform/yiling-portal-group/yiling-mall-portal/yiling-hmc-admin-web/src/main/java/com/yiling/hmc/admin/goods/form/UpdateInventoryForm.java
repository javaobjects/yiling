package com.yiling.hmc.admin.goods.form;

import com.yiling.framework.common.base.form.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/4/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateInventoryForm extends BaseForm {
    private   Long skuId;
    private Long inventoryQty;
}