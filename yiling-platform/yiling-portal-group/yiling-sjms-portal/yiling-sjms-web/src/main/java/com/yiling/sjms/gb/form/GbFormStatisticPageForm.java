package com.yiling.sjms.gb.form;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GbFormStatisticPageForm extends QueryPageListForm {

    /**
     * 开始时间
     */
    @NotNull
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @NotNull
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     *省区名称
     */
    @ApiModelProperty(value = "省区名称")
    private String provinceName;

    /**
     * 产品编号
     */
    @ApiModelProperty(value = "产品编号")
    private String code;

    @Min(0)
    @Max(1)
    @NotNull
    @ApiModelProperty(value = "团购月份 0-不勾选 1-勾选")
    private Integer monthType;

    @Min(0)
    @Max(1)
    @NotNull
    @ApiModelProperty(value = "省区 0-不勾选 1-勾选")
    private Integer provinceType;

    @Min(0)
    @Max(1)
    @NotNull
    @ApiModelProperty(value = "产品 0-不勾选 1-勾选")
    private Integer goodsType;

    /**
     * 开始月份时间
     */
    @ApiModelProperty(value = "开始月份时间")
    private String startMonthTime;

    /**
     * 结束月份时间
     */
    @ApiModelProperty(value = "结束月份时间")
    private String endMonthTime;
}
