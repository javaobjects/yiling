package com.yiling.open.erp.dto;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ErpLog")
public class ErpLog
{
    private Integer id;
    private String supplierId;
    private String clientLog;
    private Integer logApiType;
    private Integer logSourceType;
    private Integer logType;
    private String createTime;
    private String remark;

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSupplierId() {
        return this.supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getClientLog() {
        return this.clientLog;
    }

    public void setClientLog(String clientLog) {
        this.clientLog = clientLog;
    }

    public Integer getLogApiType() {
        return this.logApiType;
    }

    public void setLogApiType(Integer logApiType) {
        this.logApiType = logApiType;
    }

    public Integer getLogSourceType() {
        return this.logSourceType;
    }

    public void setLogSourceType(Integer logSourceType) {
        this.logSourceType = logSourceType;
    }

    public Integer getLogType() {
        return this.logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String toString()
    {
        return "ErpLog{id=" + this.id + ", supplierId='" + this.supplierId + '\'' + ", clientLog='" + this.clientLog + '\'' + ", logApiType=" + this.logApiType + ", logSourceType=" + this.logSourceType + ", logType=" + this.logType + ", createTime='" + this.createTime + '\'' + ", remark='" + this.remark + '\'' + '}';
    }
}