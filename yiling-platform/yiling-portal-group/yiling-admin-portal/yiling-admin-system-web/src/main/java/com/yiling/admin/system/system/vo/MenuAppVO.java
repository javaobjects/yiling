package com.yiling.admin.system.system.vo;

import com.yiling.user.system.enums.PermissionAppEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 菜单所属的应用信息 VO
 *
 * @author: xuan.zhou
 * @date: 2022/11/23
 */
@Data
public class MenuAppVO {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("系统ID")
    private Integer appId;

    public MenuAppVO(PermissionAppEnum appEnum) {
        this.name = appEnum.getName();
        this.appId = appEnum.getCode();
    }
}
