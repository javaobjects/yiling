package com.yiling.b2b.admin.enterprisecustomer.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 向分组中添加查询结果的客户 Form
 *
 * @author: lun.yu
 * @date: 2022-04-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddResultGroupCustomersForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "分组ID", required = true)
    private Long groupId;

    @ApiModelProperty("客户名称")
    private String name;

    @ApiModelProperty("省份编码")
    private String provinceCode;

    @ApiModelProperty("城市编码")
    private String cityCode;

    @ApiModelProperty("区域编码")
    private String regionCode;

    @ApiModelProperty("执业许可证号/社会信用统一代码")
    private String licenseNumber;

    @ApiModelProperty("企业类型：具体类型见字典")
    private Integer type;

}
