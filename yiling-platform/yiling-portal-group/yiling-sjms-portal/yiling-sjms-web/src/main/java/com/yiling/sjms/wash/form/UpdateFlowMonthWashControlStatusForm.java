package com.yiling.sjms.wash.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/3/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateFlowMonthWashControlStatusForm extends BaseForm {

    @ApiModelProperty(value = "主键")
    private Long    id;
    @ApiModelProperty(value = "阶段序号（1,2,3,4,5,6,7,8）")
    private Integer    stage;
    @ApiModelProperty(value = "员工备份状态：1未开始2已完成")
    private Integer employeeBackupStatus;
    @ApiModelProperty(value = "基础状态：1未开始2进行中3已完成")
    private Integer basisStatus;
    @ApiModelProperty(value = "备份状态：1未开始2已完成")
    private Integer basisBackupStatus;
    @ApiModelProperty(value = "第一阶段清洗状态：1未开始2进行中3已完成")
    private Integer washStatus;
    @ApiModelProperty(value = "团购锁任务状态：1未开始2进行中3已完成")
    private Integer gbLockStatus;
    @ApiModelProperty(value = "非锁任务状态：1未开始2进行中3已完成")
    private Integer unlockStatus;
    @ApiModelProperty(value = "团购非锁任务状态：1未开始2进行中3已完成")
    private Integer gbUnlockStatus;
    @ApiModelProperty(value = "任务状态：1未处理2已完成")
    private Integer taskStatus;
}
