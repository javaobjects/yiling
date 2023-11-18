package com.yiling.sales.assistant.app.message.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/17
 */
@Data
@Accessors(chain = true)
public class LastVersionVO implements Serializable {

    @ApiModelProperty("B2B ios的最新版本")
    private AppVersionVO iosVersion;

    @ApiModelProperty("B2B 安卓的最新版本")
    private AppVersionVO androidVersion;
}
