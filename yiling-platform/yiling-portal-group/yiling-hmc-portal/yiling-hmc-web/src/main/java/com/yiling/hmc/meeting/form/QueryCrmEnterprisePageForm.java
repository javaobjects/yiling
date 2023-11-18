package com.yiling.hmc.meeting.form;


import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryCrmEnterprisePageForm extends QueryPageListForm {

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String likeName;

}
