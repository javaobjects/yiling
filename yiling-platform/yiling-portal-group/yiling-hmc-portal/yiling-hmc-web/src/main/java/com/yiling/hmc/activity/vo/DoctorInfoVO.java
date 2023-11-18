package com.yiling.hmc.activity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 医生信息
 *
 * @author: fan.shen
 * @date: 2022/9/9
 */
@Data
public class DoctorInfoVO implements java.io.Serializable {

    /**
     * 医生名称
     */
    @ApiModelProperty(value = "医生名称")
    private String doctorName;

    /**
     * 医生头像
     */
    @ApiModelProperty(value = "医生头像")
    private String picture;

}
