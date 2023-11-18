package com.yiling.admin.erp.enterprise.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/3/24
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel("crm企业信息VO")
public class ErpCrmEnterpriseInfoVO {

    /**
     * crm企业ID
     */
    @ApiModelProperty(value = "crm企业ID", example = "1")
    private Long crmEnterpriseId;

    /**
     * crm企业名称
     */
    @ApiModelProperty(value = "crm企业名称", example = "")
    private String crmEnterpriseName;

}
