package com.yiling.open.erp.dto;


import java.io.Serializable;

/**
 * @Description:
 * @Author: qi.xiong
 * @Date: 2019/4/30
 */
public class ErpErrorEntity implements Serializable {

    private String erpPrimaryKey;
    private String error;

    public ErpErrorEntity() {
        super();
    }

    public ErpErrorEntity(String erpPrimaryKey, String error) {
        this.erpPrimaryKey = erpPrimaryKey;
        this.error = error;
    }

    public String getErpPrimaryKey() {
        return erpPrimaryKey;
    }

    public void setErpPrimaryKey(String erpPrimaryKey) {
        this.erpPrimaryKey = erpPrimaryKey;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
