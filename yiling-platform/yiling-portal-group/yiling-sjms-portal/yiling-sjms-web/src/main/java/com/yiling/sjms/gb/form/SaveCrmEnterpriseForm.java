package com.yiling.sjms.gb.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/1/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCrmEnterpriseForm extends BaseForm {

    /**
     * 名称
     */
    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 统一信用代码
     */
    @NotBlank
    @ApiModelProperty(value = "统一信用代码")
    private String licenseNumber;
    /**
     * 省
     */
    @NotBlank
    @ApiModelProperty(value = "省名称Code")
    private String provinceCode;
    /**
     * 市
     */
    @NotBlank
    @ApiModelProperty(value = "市名称Code")
    private String cityCode;
    /**
     * 区
     */
    @NotBlank
    @ApiModelProperty(value = "区市名称Code")
    private String regionCode;
    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;

    /**
     * 类型 企业类型EnterpriseTypeNameEnum.name
     */
    @NotNull
    @ApiModelProperty(value = "类型 1-出库终端，2-出库商业")
    private Integer type;

    /**
     * 出库终端类型
     */
    @ApiModelProperty(value = "类型 2-终端医院，3-终端药店")
    private Integer roleType;

}
