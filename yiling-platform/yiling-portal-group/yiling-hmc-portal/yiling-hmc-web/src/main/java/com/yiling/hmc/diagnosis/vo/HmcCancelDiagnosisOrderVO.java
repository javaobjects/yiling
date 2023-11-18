package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 取消咨询VO
 *
 * @author: fan.shen
 * @date: 2023/05/15
 */
@Data
public class HmcCancelDiagnosisOrderVO {

    @ApiModelProperty(value = "状态 1问诊单不存在 2问诊单状态异常无法取消 3问诊单错误无法取消 4成功")
    private Integer status;


}