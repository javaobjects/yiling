package com.yiling.admin.erp.statistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业信息VO
 *
 * @author: houjie.sun
 * @date: 2022/1/14
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel("企业信息VO")
public class EnterpriseInfoVO {

    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业ID", example = "1")
    private Long suId;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称", example = "2")
    private String clientName;

}
