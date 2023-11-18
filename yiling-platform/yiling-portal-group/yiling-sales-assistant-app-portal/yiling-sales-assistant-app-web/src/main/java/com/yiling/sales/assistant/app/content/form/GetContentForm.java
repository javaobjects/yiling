package com.yiling.sales.assistant.app.content.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/7/28
 */
@Data
@Accessors(chain = true)
public class GetContentForm {

    @NotNull
    private Long id;

    /**
     * 引用业务线id
     */
    @ApiModelProperty(value = "引用业务线id 1-健康管理中心，2-IH医生端，3-IH患者端 6-销售助手 7-大运河", required = true)
    @NotNull
    private Integer lineId;
}