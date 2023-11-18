package com.yiling.admin.sales.assistant.task.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 行政区划 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/7
 */
@Data
@ApiModel
public class LocationVO {

    /**
     * 区域编码
     */
    @ApiModelProperty("区域编码")
    private String code;

    /**
     * 区域名称
     */
    @ApiModelProperty("区域名称")
    private String name;
}
