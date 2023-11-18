package com.yiling.open.erp.dto;


import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ErpSql")
public class ErpSql
        implements Serializable {
    private String sqlType;
    private String sqlIp;
    private String sqlPort;
    private String sqlCharset;
    private String sqlName;
    private String sqlLoginName;
    private String sqlLoginPW;
    private String oracleType;
    private String servicePath;
    private String pharmacyName;
    private String secret;
    private String key;

    public String getSqlType() {
        return this.sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getSqlIp() {
        return this.sqlIp;
    }

    public void setSqlIp(String sqlIp) {
        this.sqlIp = sqlIp;
    }

    public String getSqlPort() {
        return this.sqlPort;
    }

    public void setSqlPort(String sqlPort) {
        this.sqlPort = sqlPort;
    }

    public String getSqlCharset() {
        return this.sqlCharset;
    }

    public void setSqlCharset(String sqlCharset) {
        this.sqlCharset = sqlCharset;
    }

    public String getSqlName() {
        return this.sqlName;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public String getSqlLoginName() {
        return this.sqlLoginName;
    }

    public void setSqlLoginName(String sqlLoginName) {
        this.sqlLoginName = sqlLoginName;
    }

    public String getSqlLoginPW() {
        return this.sqlLoginPW;
    }

    public void setSqlLoginPW(String sqlLoginPW) {
        this.sqlLoginPW = sqlLoginPW;
    }

    public String getOracleType() {
        return this.oracleType;
    }

    public void setOracleType(String oracleType) {
        this.oracleType = oracleType;
    }

    public String getServicePath() {
        return this.servicePath;
    }

    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }

    public String getPharmacyName() {
        return this.pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
