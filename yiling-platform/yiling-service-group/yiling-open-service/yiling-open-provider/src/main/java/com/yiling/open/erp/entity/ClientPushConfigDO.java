package com.yiling.open.erp.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shuan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("client_push_config")
public class ClientPushConfigDO extends BaseDO {

    private Long suId;

    private String dbName;

    private String status;

    private String dbType;

    private String oracleType;

    private String dbCharset;

    private String dbLoginName;

    private String dbLoginPw;

    private String dbIp;

    private String dbPort;

    private String orderSql;

    private String orderDetailSql;

    private String updateTime;

    private String createTime;

    private Date syncTime;

    private String oracleSid;

    private String syncStatus;

    private Integer delFlag;

    public Date getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Date syncTime) {
        this.syncTime = syncTime;
    }

    public Long getSuId() {
        return suId;
    }

    public void setSuId(Long suId) {
        this.suId = suId;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getDbLoginPw() {
        return dbLoginPw;
    }

    public void setDbLoginPw(String dbLoginPw) {
        this.dbLoginPw = dbLoginPw;
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
}