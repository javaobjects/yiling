package com.yiling.admin.cms.content.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 栏目引用业务线
 * </p>
 *
 * @author gxl
 * @date 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddCategoryDisplayLineForm extends BaseForm {


    /**
     * 引用业务线id
     */
    @ApiModelProperty(value = "引用业务线id")
    private Long lineId;

    /**
     * 业务线名称
     */
    @ApiModelProperty(value = "引用业务线")
    private String lineName;


}
