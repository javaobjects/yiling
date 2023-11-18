package com.yiling.admin.erp.enterprise.form;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
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
public class ClientTaskConfigForm extends BaseForm {

    private Long suId;

    private String taskNo;

    private String taskName;

    @ApiModelProperty(value = "调度周期", example = "1")
    private String taskInterval;

    @ApiModelProperty(value = "任务sql", example = "1")
    private String taskSql;

    @ApiModelProperty(value = "任务状态 0停止 1正常", example = "1")
    private String taskStatus;

    private String taskKey;

    private String springId;

    private String methodName;

    private String updateTime;

    private String createTime;

    private Date syncTime;

    private String syncStatus;

    @ApiModelProperty(value = "流向数据天数", example = "1")
    private String flowDateCount;
}