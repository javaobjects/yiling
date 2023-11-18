package com.yiling.sjms.agency.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 校验返回结果
 *
 * @author: yong.zhang
 * @date: 2023/2/18 0018
 */
@Data
public class CheckResultVO {

    /**
     * 校验是否成功
     */
    @ApiModelProperty("校验是否成功")
    private Boolean isSuccess;

    /**
     * 校验失败原因
     */
    @ApiModelProperty("校验失败原因")
    private String errorMessage;
}
