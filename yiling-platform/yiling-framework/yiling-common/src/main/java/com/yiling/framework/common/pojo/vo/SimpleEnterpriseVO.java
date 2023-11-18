package com.yiling.framework.common.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 简单企业信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/10
 */
@Data
public class SimpleEnterpriseVO {

    @ApiModelProperty("企业ID")
    private Long id;

    @ApiModelProperty("企业名称")
    private String name;

    @ApiModelProperty("备注")
    private String remark;
}
