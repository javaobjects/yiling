package com.yiling.admin.system.excel.form;

import java.util.List;

import com.yiling.admin.system.export.form.SaveExportSearchConditionForm;
import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/5/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveExcelTaskRecordForm extends BaseForm {

    /**
     * 任务配置表ID
     */
    @ApiModelProperty(value = "任务配置表ID")
    private Long taskConfigId;

}
