package com.yiling.sjms.flow.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/2/7
 */
@Data
public class FlowDistributionEnterpriseVO {

    /**
     * 经销商编码
     */
    @ApiModelProperty(value = "经销商编码", example = "1")
    private Long crmEnterpriseId;

    /**
     * 客户编码
     */
    @ApiModelProperty(value = "客户编码", example = "2")
    private String code;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "经销商名称", example = "3")
    private String clientName;

}
