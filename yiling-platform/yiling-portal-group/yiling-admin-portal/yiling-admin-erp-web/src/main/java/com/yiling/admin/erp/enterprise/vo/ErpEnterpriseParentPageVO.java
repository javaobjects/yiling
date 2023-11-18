package com.yiling.admin.erp.enterprise.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ERP对接企业管理查询列表分页
 *
 * @author: houjie.sun
 * @date: 2022/1/13
 */
@Data
public class ErpEnterpriseParentPageVO {

    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业ID", example = "1")
    private Long rkSuId;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称", example = "1")
    private String clientName;

}
