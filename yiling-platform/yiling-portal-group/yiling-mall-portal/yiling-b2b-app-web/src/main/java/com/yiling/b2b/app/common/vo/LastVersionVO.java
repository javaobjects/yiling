package com.yiling.b2b.app.common.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LastVersionVO extends BaseVO {

    @ApiModelProperty("B2B ios的最新版本")
    private AppVersionVO iosVersion;

    @ApiModelProperty("B2B 安卓的最新版本")
    private AppVersionVO androidVersion;
}
