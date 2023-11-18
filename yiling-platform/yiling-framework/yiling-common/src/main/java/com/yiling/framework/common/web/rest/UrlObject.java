package com.yiling.framework.common.web.rest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: shuang.zhang
 * @date: 2021/6/17
 */
@Getter
@Setter
@AllArgsConstructor
@ApiModel("Url字符串对象")
public class UrlObject<Serializable> {

    @ApiModelProperty(value = "Url字符串对象")
    private Serializable url;
}
