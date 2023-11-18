package com.yiling.open.erp.dto;

public class PushConfig {
    private Long id;
    private Long suId;
    private String status;
    private String dbName;
    private String dbType;
    private String oracleType;
    private String oracleSid;
    private String dbCharset;
    private String dbLoginName;
    private String dbLoginPW;
    private String dbIp;
    private String dbPort;
    private String orderSql;
    private String orderDetailSql;
    private String syncStatus;
    private String updateTime;
    private String createTime;

    @Override
    public String toString() {
        return "PushConfig{" +
                "status='" + status + '\'' +
                ", dbName='" + dbName + '\'' +
                ", dbType='" + dbType + '\'' +
                ", oracleType='" + oracleType + '\'' +
                ", oracleSid='" + oracleSid + '\'' +
                ", dbCharset='" + dbCharset + '\'' +
                ", dbLoginName='" + dbLoginName + '\'' +
                ", dbLoginPW='" + dbLoginPW + '\'' +
                ", dbIp='" + dbIp + '\'' +
                ", dbPort='" + dbPort + '\'' +
                ", orderSql='" + orderSql + '\'' +
                ", orderDetailSql='" + orderDetailSql + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }

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

    public String getOrderSql() {
        return orderSql;
    }

    public void setOrderSql(String orderSql) {
        this.orderSql = orderSql;
    }

    public String getOrderDetailSql() {
        return orderDetailSql;
    }

    public void setOrderDetailSql(String orderDetailSql) {
        this.orderDetailSql = orderDetailSql;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getOracleType() {
        return oracleType;
    }

    public void setOracleType(String oracleType) {
        this.oracleType = oracleType;
    }

    public String getDbCharset() {
        return dbCharset;
    }

    public void setDbCharset(String dbCharset) {
        this.dbCharset = dbCharset;
    }

    public String getDbLoginName() {
        return dbLoginName;
    }

    public void setDbLoginName(String dbLoginName) {
        this.dbLoginName = dbLoginName;
    }

    public String getDbLoginPW() {
        return dbLoginPW;
    }

    public void setDbLoginPW(String dbLoginPW) {
        this.dbLoginPW = dbLoginPW;
    }

    public String getDbIp() {
        return dbIp;
    }

    public void setDbIp(String dbIp) {
        this.dbIp = dbIp;
    }

    public String getDbPort() {
        return dbPort;
    }

    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }

    public String getOracleSid() {
        return oracleSid;
    }

    public void setOracleSid(String oracleSid) {
        this.oracleSid = oracleSid;
    }
}
