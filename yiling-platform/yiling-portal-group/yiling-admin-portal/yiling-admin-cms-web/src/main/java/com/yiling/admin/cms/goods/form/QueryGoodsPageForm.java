package com.yiling.admin.cms.goods.form;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.QueryPageListForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/6/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsPageForm extends QueryPageListForm {
    @NotEmpty
    private String goodsName;
}