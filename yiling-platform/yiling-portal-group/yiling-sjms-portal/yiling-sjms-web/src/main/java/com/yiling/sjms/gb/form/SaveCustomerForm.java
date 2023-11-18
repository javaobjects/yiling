package com.yiling.sjms.gb.form;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author wei.wang
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCustomerForm extends BaseForm {
    /**
     * 团购单位名称
     */
    @Length(max = 50)
    @NotBlank
    @ApiModelProperty(value = "团购单位名称")
    private String name;


    /**
     * 省份编号
     */
    @NotBlank
    @ApiModelProperty(value = "省份编号")
    private String provinceCode;

    /**
     * 城市编码
     */
    @NotBlank
    @ApiModelProperty(value = "城市编码")
    private String cityCode;

    /**
     * 区县编码
     */
    @NotBlank
    @ApiModelProperty(value = "区县编码")
    private String regionCode;

    /**
     * 详细地址
     */
    @Length(max = 100)
    @ApiModelProperty(value = "详细地址")
    private String address;

    /**
     * 社会统一信用代码
     */
    @Length(max = 18)
    @NotBlank
    @ApiModelProperty(value = "社会统一信用代码")
    private String creditCode;




}
