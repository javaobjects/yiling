package com.yiling.b2b.admin.goods.form;


import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteGoodsPriceLimitForm extends BaseForm {

    @ApiModelProperty(value = "条件ID集合", example = "1111")
    private List<Long> ids;
}
