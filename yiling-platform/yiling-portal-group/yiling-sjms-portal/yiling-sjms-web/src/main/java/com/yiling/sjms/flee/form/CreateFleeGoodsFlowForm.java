package com.yiling.sjms.flee.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/16 0016
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateFleeGoodsFlowForm extends BaseForm {

    @ApiModelProperty("ID")
    private Long id;

    /**
     * 调整流向的月份
     */
    @ApiModelProperty(value = "调整流向的月份")
    private String toMonth;
}
