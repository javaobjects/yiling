package com.yiling.admin.erp.enterprise.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2022/5/26
 */
@Data
public class ClientTaskConfigVO{

    @ApiModelProperty(value = "商业公司编码", example = "1")
    private Long suId;

    @ApiModelProperty(value = "任务编码", example = "1")
    private String taskNo;

    @ApiModelProperty(value = "任务名称", example = "1")
    private String taskName;

    @ApiModelProperty(value = "调度周期", example = "1")
    private String taskInterval;

    @ApiModelProperty(value = "任务sql", example = "1")
    private String taskSql;

    @ApiModelProperty(value = "任务状态", example = "1")
    private String taskStatus;

    @ApiModelProperty(value = "ID", example = "1")
    private String taskKey;

    @ApiModelProperty(value = "程序Id", example = "1")
    private String springId;

    @ApiModelProperty(value = "任务名称", example = "1")
    private String methodName;

    @ApiModelProperty(value = "对接字段描述", example = "1")
    private String describe;

    private String updateTime;

    private String createTime;

    private Date syncTime;

    private String syncStatus;

    @ApiModelProperty(value = "ID", example = "1")
    private String flowDateCount;
}
