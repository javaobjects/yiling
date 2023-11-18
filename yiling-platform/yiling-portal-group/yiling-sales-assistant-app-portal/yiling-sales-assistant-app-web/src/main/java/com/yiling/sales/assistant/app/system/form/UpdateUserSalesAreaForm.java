package com.yiling.sales.assistant.app.system.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改自然人销售区域 Form
 *
 * @author: xuan.zhou
 * @date: 2022/3/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateUserSalesAreaForm extends BaseForm {

    @ApiModelProperty(value = "销售区域（为空表示“全国”）")
    private List<LocationTreeForm> salesAreaTree;
}
