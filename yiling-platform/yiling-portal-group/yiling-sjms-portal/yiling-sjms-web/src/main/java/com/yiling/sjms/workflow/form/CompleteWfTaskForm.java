package com.yiling.sjms.workflow.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 流程任务业务对象
 *
 * @author KonBAI
 * @createTime 2022/3/10 00:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CompleteWfTaskForm extends BaseForm {

    /**
     * 任务Id
     */
    @ApiModelProperty(value = "任务id",required = true)
    @NotEmpty
    private String taskId;


    @ApiModelProperty(value = "是否同意",required = true)
    @NotNull
    private Boolean isAgree;

    /**
     * 任务意见
     */
    @ApiModelProperty(value = "审批意见")
    private String comment;

    /**
     * 团购ID
     */
    @ApiModelProperty(value = "团购ID",required = true)
    private Long gbId;

    /**
     * 核实团购性质：1-普通团购 2-政府采购
     */
    @ApiModelProperty(value = "核实团购性质：1-普通团购 2-政府采购")
    private Integer  gbReviewType;

    /**
     * 是否地级市下机构：1-是 2-否
     */
    @ApiModelProperty(value = "是否地级市下机构：1-是 2-否")
    private Integer gbCityBelow;

    @ApiModelProperty(value = "驳回节点id(驳回到发起人不用传或传空)")
    private String nodeId;
}
