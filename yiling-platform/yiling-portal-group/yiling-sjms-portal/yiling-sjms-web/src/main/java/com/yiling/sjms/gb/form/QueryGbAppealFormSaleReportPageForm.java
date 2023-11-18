package com.yiling.sjms.gb.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/29
 */
@Data
public class QueryGbAppealFormSaleReportPageForm extends QueryPageListForm {

    @NotEmpty
    @ApiModelProperty(value = "所属年月份 格式:yyyy-MM", example = "2023-05", required = true)
    @Pattern(regexp = "^\\d{4}-((0([1-9]))|(1(0|1|2)))$", message = "请填写正确的年月")
    private String soMonth;

    /**
     * 经销商编码
     */
    @ApiModelProperty(value = "经销商编码,用户模糊搜索经销商后,带过来经销商编码")
    private Long crmId;

    /**
     * 原始客户名称
     */
    @ApiModelProperty(value = "原始客户名称")
    private String originalEnterpriseName;

    /**
     * 标准产品编码
     */
    @ApiModelProperty(value = "标准产品编码")
    private Long goodsCode;

    /**
     * 标准机构编码
     */
    @ApiModelProperty(value = "标准机构编码")
    private Long crmEnterpriseId;

}
