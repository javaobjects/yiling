package com.yiling.open.erp.dto;

import java.io.Serializable;
import java.util.Date;

public class TaskJobRecord implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;



    private int    recordId;
    private String jobName;
    private String cronExpression;
    private String beanClass;
    public int     isConcurrent;
    private String springId;
    private String methodName;

    /**
     * 执行结果 1 成功 0 失败
     */
    private int    executeResult;
    /**
     * 错误信息
     */
    private String errorMsg;
    /**
     * 开始时间
     */
    private Date   startTime;
    /**
     * 执行任务耗时
     */
    private long   elapsedTime;
    /**
     * 记录日志时间
     */
    private Date   logTime;

    public int getRecordId() {
        return recordId;
    }

    public String getJobName() {
        return jobName;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public String getBeanClass() {
        return beanClass;
    }

    public int getIsConcurrent() {
        return isConcurrent;
    }

    public String getSpringId() {
        return springId;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getExecuteResult() {
        return executeResult;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public Date getStartTime() {
        return startTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    public void setIsConcurrent(int isConcurrent) {
        this.isConcurrent = isConcurrent;
    }

    public void setSpringId(String springId) {
        this.springId = springId;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setExecuteResult(int executeResult) {
        this.executeResult = executeResult;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

   
}
