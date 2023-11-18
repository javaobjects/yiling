package com.yiling.sales.assistant.app.system.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户销售区域信息 VO
 *
 * @author: xuan.zhou
 * @date: 2022/3/7
 */
@Data
public class UserSalesAreaVO {

    /**
     * 销售区域描述
     */
    @ApiModelProperty("销售区域描述")
    private String description;

    /**
     * 销售区域是否全国：0-否 1-是
     */
    @ApiModelProperty("销售区域是否全国：0-否 1-是")
    private Integer salesAreaAllFlag;

    /**
     * 销售区域选择树信息
     */
    @ApiModelProperty("销售区域选择树信息")
    private List<LocationSelectTreeVO> salesAreaSelectTree;
}
