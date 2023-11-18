package com.yiling.admin.erp.enterprise.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/1/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "查询ERP对接企业管理分页参数")
public class QueryErpEnterpriseParentPageForm extends QueryPageListForm {

    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID", example = "1")
    private Long rkSuId;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称", example = "2")
    private String clientName;

}
