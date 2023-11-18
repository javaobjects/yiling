package com.yiling.user.web.user.vo;

import com.yiling.user.system.enums.PermissionAppEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 顶部菜单 VO
 *
 * @author: xuan.zhou
 * @date: 2021/7/7
 */
@Data
public class TopMenuVO {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("系统ID")
    private Integer appId;

    public TopMenuVO(PermissionAppEnum appEnum) {
        this.name = appEnum.getName();
        this.appId = appEnum.getCode();
    }
}
