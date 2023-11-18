package com.yiling.admin.hmc.gzh.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@ApiModel("公众号菜单对象")
public class MenuVO {

    /**
     * button
     */
    @ApiModelProperty("按钮")
    private List<ButtonVO> button;
}
