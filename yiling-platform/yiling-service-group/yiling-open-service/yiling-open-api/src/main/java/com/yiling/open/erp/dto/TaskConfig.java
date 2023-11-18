package com.yiling.open.erp.dto;

import java.io.Serializable;

/**
 * @author shuan
 */
public class TaskConfig implements Serializable {

    public static final String STATUS_RUNNING     = "1";
    public static final String STATUS_NOT_RUNNING = "0";
    public static final int    CONCURRENT_IS      = 1;
    public static final int    CONCURRENT_NOT     = 0;

    private Long id;
    private Long suId;
    private String  taskNo;
    private String  taskName;
    private String  taskInterval;
    private String  taskSQL;
    private String  taskKey;
    private String  taskStatus;
    private String  springId;
    private String  methodName;
    private String  syncStatus;
    private String  flowDateCount;
    private String  updateTime;
    private String  createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSuId() {
        return suId;
    }

    public void setSuId(Long suId) {
        this.suId = suId;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskInterval() {
        return taskInterval;
    }

    public void setTaskInterval(String taskInterval) {
        this.taskInterval = taskInterval;
    }

    public String getTaskSQL() {
        return taskSQL;
    }

    public void setTaskSQL(String taskSQL) {
        this.taskSQL = taskSQL;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public String getSpringId() {
        return springId;
    }

    public void setSpringId(String springId) {
        this.springId = springId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFlowDateCount() {
        return flowDateCount;
    }

    public void setFlowDateCount(String flowDateCount) {
        this.flowDateCount = flowDateCount;
    }
}
