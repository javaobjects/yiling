package com.yiling.framework.common.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Base VO
 *
 * @author: xuan.zhou
 * @date: 2020/6/18
 */
@Data
public class BaseVO {

    @ApiModelProperty("ID")
    private Long id;
}
