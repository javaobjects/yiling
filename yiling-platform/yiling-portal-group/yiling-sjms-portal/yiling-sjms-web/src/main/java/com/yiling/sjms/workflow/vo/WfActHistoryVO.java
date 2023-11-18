package com.yiling.sjms.workflow.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工作流操作历史记录 VO
 *
 * @author: xuan.zhou
 * @date: 2023/2/15
 */
@Data
public class WfActHistoryVO {

    /**
     * 操作人姓名
     */
    @ApiModelProperty("操作人姓名")
    private String fromEmpName;

    /**
     * 接收人姓名
     */
    @ApiModelProperty("接收人姓名")
    private String toEmpNames;

    /**
     * 操作类型名称
     */
    @ApiModelProperty("操作类型名称")
    private String actTypeName;

    /**
     * 文字类型（审批意见/转发提示语/批注）
     */
    @ApiModelProperty("文字类型（审批意见/转发提示语/批注）")
    private String actTextTypeName;

    /**
     * 文字内容
     */
    @ApiModelProperty("文字内容")
    private String actText;

    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    private Date actTime;
}
