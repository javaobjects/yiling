package com.yiling.sales.assistant.app.system.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 区域选择树 VO
 *
 * @author: xuan.zhou
 * @date: 2022/3/9
 */
@Data
@Accessors(chain = true)
public class LocationSelectTreeVO {

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
     * 是否选中
     */
    @ApiModelProperty("是否选中")
    private Boolean selectedFlag;

    /**
     * 下级区域列表
     */
    @ApiModelProperty("下级区域列表")
    private List<LocationSelectTreeVO> children;

}
