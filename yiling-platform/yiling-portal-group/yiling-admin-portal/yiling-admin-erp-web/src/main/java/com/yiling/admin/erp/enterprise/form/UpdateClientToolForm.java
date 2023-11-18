package com.yiling.admin.erp.enterprise.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/6/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateClientToolForm extends BaseForm {

    @ApiModelProperty(value = "商业公司编号", example = "1")
    private Long suId;
}
