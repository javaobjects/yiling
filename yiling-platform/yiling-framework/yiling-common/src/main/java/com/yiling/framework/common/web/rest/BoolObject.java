package com.yiling.framework.common.web.rest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 布尔对象
 *
 * @author xuan.zhou
 * @date 2020/12/14
 */
@Getter
@Setter
@AllArgsConstructor
@ApiModel("布尔对象")
@Deprecated
public class BoolObject {

    @ApiModelProperty(value = "布尔对象")
    private Boolean result;

}
