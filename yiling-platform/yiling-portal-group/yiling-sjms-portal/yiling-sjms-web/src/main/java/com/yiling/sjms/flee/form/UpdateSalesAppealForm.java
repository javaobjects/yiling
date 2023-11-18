package com.yiling.sjms.flee.form;


import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.sjms.flee.vo.AppendixDetailVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shixing.sun
 * @date: 2023/2/25 0025
 */
@Data
public class UpdateSalesAppealForm {

    @ApiModelProperty("列表id")
    @NotNull(message = "列表id")
    private Long id;

    @ApiModelProperty("补传的月流向")
    @NotNull(message = "补传的月流向")
    private String appealMonth;
}
