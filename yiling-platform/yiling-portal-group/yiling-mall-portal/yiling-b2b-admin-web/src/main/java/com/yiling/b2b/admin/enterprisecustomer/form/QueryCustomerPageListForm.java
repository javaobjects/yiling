package com.yiling.b2b.admin.enterprisecustomer.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业客户分页列表 Form
 *
 * @author: lun.yu
 * @date: 2021/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryCustomerPageListForm extends QueryPageListForm {

    @ApiModelProperty("企业名称")
    private String name;

    @ApiModelProperty("ERP编码")
    private String customerCode;

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

    @ApiModelProperty("客户分组ID")
    private Long customerGroupId;

}
