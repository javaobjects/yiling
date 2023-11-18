package com.yiling.export.excel.handler;

import java.io.File;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;

/**
 * @author: shuang.zhang
 * @date: 2023/5/9
 */
public class EasyExcelVerifyHandlerResult {
    private boolean success;
    private String msg;
    private String field;
    private String value;

    private EasyExcelVerifyHandlerResult() {
    }

    public EasyExcelVerifyHandlerResult(boolean success) {
        this.success = success;
    }

    public EasyExcelVerifyHandlerResult(boolean success, String msg,String field,String value) {
        this.success = success;
        this.msg = msg;
        this.field= field;
        this.value=value;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
