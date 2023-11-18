package com.yiling.sjms.agency.form;

import javax.validation.constraints.NotBlank;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/14 0014
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmEnterpriseBackUpPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "标准机构编码")
    private String crmEnterpriseId;

    @ApiModelProperty(value = "机构名称")
    private String name;

    /**
     * 年月(yyyyMM 格式)
     */
    @ApiModelProperty(value = "年月(yyyyMM 格式)")
    @NotBlank(message = "年月不能为空")
    private String soMonth;
}
