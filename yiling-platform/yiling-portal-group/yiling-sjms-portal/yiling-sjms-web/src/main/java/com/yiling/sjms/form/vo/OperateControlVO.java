package com.yiling.sjms.form.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 操作按钮控制
 * @author: gxl
 * @date: 2023/3/1
 */
@Data
@Accessors(chain = true)
public class OperateControlVO {
    /**
     * 是否已办
     */
    @ApiModelProperty(value = "是否显示审批按钮")
    private Boolean showAuditButtonFlag;

    @ApiModelProperty("是否显示批注按钮")
    private Boolean showCommentButtonFlag;

}