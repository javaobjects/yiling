package com.yiling.admin.hmc.doctor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/05
 */
@Data
@Accessors(chain = true)
public class HmcVocationalHospitalVO {

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("医院名称")
    private String designation;

}
