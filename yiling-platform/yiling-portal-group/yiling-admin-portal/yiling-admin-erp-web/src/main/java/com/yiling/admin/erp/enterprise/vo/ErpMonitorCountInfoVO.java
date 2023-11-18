package com.yiling.admin.erp.enterprise.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * @author: houjie.sun
 * @date: 2022/2/9
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel("erp对接请求次数阈值信息VO")
public class ErpMonitorCountInfoVO {

    /**
     * 任务编号
     */
    @ApiModelProperty(value = "任务编号", example = "1")
    private String taskNo;

    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称", example = "2")
    private String taskName;

    /**
     * 请求次数阈值
     */
    @ApiModelProperty(value = "请求次数阈值", example = "3")
    private String value;

}
