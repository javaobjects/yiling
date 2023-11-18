package com.yiling.data.center.admin.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 职位信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/11/4
 */
@Data
@ApiModel
public class PositionVO {

    @ApiModelProperty("职位ID")
    private Long id;

    @ApiModelProperty("职位名称")
    private String name;

    @ApiModelProperty("职位描述")
    private String description;
}
