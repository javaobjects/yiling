package com.yiling.data.center.admin.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 检查新员工手机号结果 VO
 *
 * @author: xuan.zhou
 * @date: 2021/8/10
 */
@Data
public class CheckNewEmployeeMobileResultVO {

    @ApiModelProperty("用户姓名")
    private String name;
}
