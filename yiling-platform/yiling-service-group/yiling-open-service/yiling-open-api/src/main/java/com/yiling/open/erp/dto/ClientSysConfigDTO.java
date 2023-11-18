package com.yiling.open.erp.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shuan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ClientSysConfigDTO extends BaseDTO {

    private Long suId;

    private String name;

    private String key;

    private String secret;

    private String urlPath;

    private String address;

    private String dbName;

    private String dbType;

    private String oracleSid;

    private String oracleType;

    private String dbCharset;

    private String dbLoginName;

    private String dbLoginPw;

    private String dbIp;

    private String dbPort;

    private String tabPane;

    private String version;

    private String syncStatus;

    private String updateTime;

    private String createTime;

    private Date syncTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getTabPane() {
        return tabPane;
    }

    public void setTabPane(String tabPane) {
        this.tabPane = tabPane;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
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

    public Date getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Date syncTime) {
        this.syncTime = syncTime;
    }
}