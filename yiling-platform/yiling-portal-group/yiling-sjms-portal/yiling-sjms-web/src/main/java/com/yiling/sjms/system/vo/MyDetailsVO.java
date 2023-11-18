package com.yiling.sjms.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户信息 VO
 *
 * @author: xuan.zhou
 * @date: 2022/11/24
 */
@Data
public class MyDetailsVO {

    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    @ApiModelProperty(value = "工号", required = true)
    private String empId;
}
