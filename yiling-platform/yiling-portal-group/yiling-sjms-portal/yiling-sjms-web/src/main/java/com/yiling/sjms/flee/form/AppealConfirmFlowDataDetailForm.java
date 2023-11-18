package com.yiling.sjms.flee.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 选择流向申诉数据
 * @author: xinxuan.jia
 * @date: 2023/6/25
 */
@Data
public class AppealConfirmFlowDataDetailForm {

    /**
     * 选择申诉流向数据id
     */
    @ApiModelProperty("选择申诉流向数据id")
    private Long selectFlowId;
    /**
     * 选择申诉源流向数据key
     */
    @ApiModelProperty("选择申诉源流向数据key")
    private String flowKey;



}
