package com.yiling.sjms.gb.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询选择申诉流向数据
 * @author: xinxuan.jia
 * @date: 2023/6/25
 */
@Data
public class QueryFormWashSaleReportPageForm extends QueryPageListForm {

    @NotEmpty(message = "销售时间不能为空")
    @ApiModelProperty(value = "销售时间所属年月日 格式:yyyy-MM-dd", example = "2023-05-23", required = true)
    private String soTime;

    /**
     * 经销商编码
     */
    @ApiModelProperty(value = "经销商编码,用户模糊搜索经销商后,带过来经销商编码")
    private Long crmId;

    /**
     * 标准产品编码
     */
    @NotNull(message = "产品品名不能为空")
    @ApiModelProperty(value = "标准产品编码", required = true)
    private Long goodsCode;

    /**
     * 标准机构编码
     */
    @NotNull(message = "机构名称不能为空")
    @ApiModelProperty(value = "标准机构编码")
    private Long crmEnterpriseId;

}
