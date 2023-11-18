package com.yiling.sales.assistant.app.enterprise.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业信息 VO
 *
 * @author: lun.yu
 * @date: 2021/9/23
 */
@Data
public class SimpleEnterpriseVO {

    @ApiModelProperty("企业ID")
    private Long id;

    @ApiModelProperty("企业名称")
    private String name;

    @ApiModelProperty("职位名称")
    private String positionName;

    @ApiModelProperty("上级领导")
    private String parentName;
}
