package com.yiling.open.erp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
    * 抽取工具任务编号表
 * @author shuan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("erp_task_interface")
public class ErpTaskInterfaceDO extends BaseDO {

    /**
    * 任务名称
    */
    private String taskName;

    /**
    * 任务编号
    */
    private String taskNo;

    /**
    * 任务唯一主键
    */
    private String taskKey;

    /**
    * 任务类型：1、数据同步2、数据推送3、模版上传下载4、流向数据接口5、其它（心跳和告警）
    */
    private Boolean taskType;

    /**
    * 任务状态：0、正常1、停止
    */
    private Boolean taskStatus;

    /**
    * 处理类名称
    */
    private String mappingUri;

    /**
    * 任务描述
    */
    private String taskDescription;

    /**
    * 任务对应的数据库表
    */
    private String tableName;

    private String jsonMapping;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public Boolean getTaskType() {
        return taskType;
    }

    public void setTaskType(Boolean taskType) {
        this.taskType = taskType;
    }

    public Boolean getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getMappingUri() {
        return mappingUri;
    }

    public void setMappingUri(String mappingUri) {
        this.mappingUri = mappingUri;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}