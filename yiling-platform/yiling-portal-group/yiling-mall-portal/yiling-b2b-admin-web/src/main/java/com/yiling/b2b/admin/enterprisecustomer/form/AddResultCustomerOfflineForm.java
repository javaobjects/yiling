package com.yiling.b2b.admin.enterprisecustomer.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 批量添加查询结果开通线下支付 Form
 *
 * @author: lun.yu
 * @date: 2022-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class AddResultCustomerOfflineForm extends BaseForm {

    @ApiModelProperty("企业名称")
    private String name;

    @ApiModelProperty("企业省份编码")
    private String provinceCode;

    @ApiModelProperty("企业城市编码")
    private String cityCode;

    @ApiModelProperty("企业区域编码")
    private String regionCode;

    @ApiModelProperty("执业许可证号/社会信用统一代码")
    private String licenseNumber;

    @ApiModelProperty("企业类型")
    private Integer type;

    @NotNull
    @Range(min = 1, max = 2)
    @ApiModelProperty(value = "操作类型：1-开通 2-关闭", required = true)
    private Integer opType;

}
