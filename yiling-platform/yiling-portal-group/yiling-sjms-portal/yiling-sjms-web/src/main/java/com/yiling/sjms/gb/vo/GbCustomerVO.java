package com.yiling.sjms.gb.vo;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data

public class GbCustomerVO extends BaseVO {

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "省名称Name")
    private String provinceName;
    /**
     * 市
     */
    @ApiModelProperty(value = "市名称Name")
    private String cityName;
    /**
     * 区
     */
    @ApiModelProperty(value = "区市名称Name")
    private String regionName;

    @ApiModelProperty(value = "省code")
    private String provinceCode;

    @ApiModelProperty(value = "市名称code")
    private String cityCode;

    @ApiModelProperty(value = "区市名code")
    private String regionCode;
}


