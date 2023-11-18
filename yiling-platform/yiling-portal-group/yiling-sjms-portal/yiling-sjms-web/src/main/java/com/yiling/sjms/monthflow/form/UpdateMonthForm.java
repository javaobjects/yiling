package com.yiling.sjms.monthflow.form;


import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: gxl
 * @date: 2023/6/25
 */
@Data
public class UpdateMonthForm {

    @ApiModelProperty("列表id")
    @NotNull(message = "列表id")
    private Long id;

    @ApiModelProperty("补传的月流向")
    @NotNull(message = "补传的月流向")
    private String appealMonth;
}
