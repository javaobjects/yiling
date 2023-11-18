package com.yiling.hmc.address.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddressSaveOrUpdateForm extends BaseForm {

    @ApiModelProperty("id")
    private Long id;

    @NotBlank
    @ApiModelProperty("收货人名称")
    private String name;

    @NotBlank
    @ApiModelProperty("收货人手机号")
    private String mobile;

    @NotBlank
    @ApiModelProperty("省份编码")
    private String provinceCode;

    @NotBlank
    @ApiModelProperty("城市编码")
    private String cityCode;

    @NotBlank
    @ApiModelProperty("区县编码")
    private String regionCode;

    @NotBlank
    @ApiModelProperty("省份名称")
    private String provinceName;

    @NotBlank
    @ApiModelProperty("城市名称")
    private String cityName;

    @NotBlank
    @ApiModelProperty("区县名称")
    private String regionName;

    @NotBlank
    @ApiModelProperty("详细地址")
    private String address;

    @NotNull
    @ApiModelProperty("是否默认：0-否 1-是")
    private Integer defaultFlag;
}
