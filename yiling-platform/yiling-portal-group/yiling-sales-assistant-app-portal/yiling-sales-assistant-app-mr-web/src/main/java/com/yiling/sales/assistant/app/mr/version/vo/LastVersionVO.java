package com.yiling.sales.assistant.app.mr.version.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/12/26 0026
 */
@Data
@Accessors(chain = true)
public class LastVersionVO implements Serializable {

    @ApiModelProperty("ios的最新版本")
    private AppVersionVO iosVersion;

    @ApiModelProperty("安卓的最新版本")
    private AppVersionVO androidVersion;
}
