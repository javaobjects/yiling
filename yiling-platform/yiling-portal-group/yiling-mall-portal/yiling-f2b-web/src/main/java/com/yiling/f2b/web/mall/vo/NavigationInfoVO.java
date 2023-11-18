package com.yiling.f2b.web.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/6/16
 */
@Data
public class NavigationInfoVO {

    /**
     * 导航名称
     */
    @ApiModelProperty(value = "导航名称")
    private String name;

    /**
     * 链接
     */
    @ApiModelProperty(value = "链接")
    private String link;
}
