package com.yiling.admin.erp.enterprise.form;

import java.util.Date;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shuan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SqlExecuteClientTaskConfigForm extends BaseForm {

    @ApiModelProperty(value = "商业公司编号", example = "1")
    private Long suId;

    @ApiModelProperty(value = "任务编号", example = "1")
    private String taskNo;

    @ApiModelProperty(value = "任务sql", example = "1")
    private String taskSql;

    @ApiModelProperty(value = "流向数据天数", example = "1")
    private String flowDateCount;
}