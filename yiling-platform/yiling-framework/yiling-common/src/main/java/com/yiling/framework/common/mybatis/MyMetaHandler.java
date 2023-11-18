package com.yiling.framework.common.mybatis;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

/**
 * 自动填充字段
 * 
 * @author xuan.zhou
 * @date 2020/10/29
 */
public class MyMetaHandler implements MetaObjectHandler {

    public static final String FIELD_CREATE_USER = "createUser";
    public static final String FIELD_UPDATE_USER = "updateUser";
    public static final String FIELD_CREATE_TIME = "createTime";
    public static final String FIELD_UPDATE_TIME = "updateTime";
    public static final String FIELD_OP_USER_ID = "opUserId";
    public static final String FIELD_OP_EID = "opEid";
    public static final String FIELD_OP_TIME = "opTime";

    @Override
    public void insertFill(MetaObject metaObject) {
        // 创建人和修改人
        Object createUser = this.getCreateUser(metaObject);
        this.setFieldValByName(FIELD_CREATE_USER, createUser, metaObject);
        this.setFieldValByName(FIELD_UPDATE_USER, createUser, metaObject);

        // 创建时间和修改时间
        Object createTime = this.getCreateTime(metaObject);
        this.setFieldValByName(FIELD_CREATE_TIME, createTime, metaObject);
        this.setFieldValByName(FIELD_UPDATE_TIME, createTime, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName(FIELD_UPDATE_USER, this.getUpdateUser(metaObject), metaObject);
        this.setFieldValByName(FIELD_UPDATE_TIME, this.getUpdateTime(metaObject), metaObject);
    }

    private Object getCreateUser(MetaObject metaObject) {
        Object opUserId = this.getFieldValByName(FIELD_OP_USER_ID, metaObject);
        if (opUserId != null) {
            return opUserId;
        }

        Object createUser = this.getFieldValByName(FIELD_CREATE_USER, metaObject);
        if (createUser != null) {
            return createUser;
        }

        return 0L;
    }

    private Object getUpdateUser(MetaObject metaObject) {
        Object opUserId = this.getFieldValByName(FIELD_OP_USER_ID, metaObject);
        if (opUserId != null) {
            return opUserId;
        }

        Object updateUser = this.getFieldValByName(FIELD_UPDATE_USER, metaObject);
        if (updateUser != null) {
            return updateUser;
        }

        return 0L;
    }

    private Object getCreateTime(MetaObject metaObject) {
        Object opTime = this.getFieldValByName(FIELD_OP_TIME, metaObject);
        if (opTime != null) {
            return opTime;
        }

        Object createTime = this.getFieldValByName(FIELD_CREATE_TIME, metaObject);
        if (createTime != null) {
            return createTime;
        }

        return new Date();
    }

    private Object getUpdateTime(MetaObject metaObject) {
        Object opTime = this.getFieldValByName(FIELD_OP_TIME, metaObject);
        if (opTime != null) {
            return opTime;
        }

        Object updateTime = this.getFieldValByName(FIELD_UPDATE_TIME, metaObject);
        if (updateTime != null) {
            return updateTime;
        }

        return new Date();
    }

}