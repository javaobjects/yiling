package com.yiling.sjms.wash.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fucheng.bai
 * @date 2023/3/1
 */
@Data
public class QueryFlowMonthWashTaskPageForm extends QueryPageListForm {

    @NotNull(message = "流向类型不可为空")
    @ApiModelProperty(value = "流向清洗任务类型 字典：flow_month_wash_task_type 1-采购 2-销售 3-库存", required = true)
    private Integer flowType;

    @ApiModelProperty(value = "流向清洗任务分类 字典：flow_month_wash_task_classify 1-正常 2-销量申诉 3-窜货申报")
    private Integer flowClassify;

    @ApiModelProperty(value = "年份")
    private Integer year;

    @ApiModelProperty(value = "月份")
    private Integer month;

    @ApiModelProperty(value = "经销商id")
    private Long crmEid;

    @ApiModelProperty(value = "流向清洗任务状态 字典：flow_month_wash_task_status 1-未清洗 2-清洗中 3-已完成 4-清洗失败")
    private Integer washStatus;

    @ApiModelProperty(value = "流向清洗任务确认状态 0-未确认 1-已确认")
    private Integer confirmStatus;
}
