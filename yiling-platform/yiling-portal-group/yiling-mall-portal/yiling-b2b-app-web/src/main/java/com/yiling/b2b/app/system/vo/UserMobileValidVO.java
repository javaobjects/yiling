package com.yiling.b2b.app.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录虚拟手机号校验信息 VO
 *
 * @author: lun.yu
 * @date: 2023-05-19
 */
@Data
@ApiModel
public class UserMobileValidVO {

    /**
     * 是否为特殊号段
     */
    @ApiModelProperty("是否为特殊号段")
    private Boolean specialPhone;

    /**
     * 是否必须换绑
     */
    @ApiModelProperty("是否必须换绑")
    private Boolean mustChangeBind;

}
