package com.yiling.sales.assistant.app.system.form;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 区域树
 */
@Data
@Accessors(chain = true)
public class LocationTreeForm {

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

    /**
     * 下级区域列表
     */
    @ApiModelProperty("下级区域列表")
    private List<LocationTreeForm> children;
}