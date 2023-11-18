package com.yiling.open.erp.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.open.erp.validation.group.Delete;

import lombok.ToString;

/**
 * @author shuan
 */
public abstract class BaseErpEntity extends BaseDO implements ErpEntity, Serializable {

    private static final long serialVersionUID = 5002356135511701385L;

    /**
     * 供应商id
     */
    @JSONField(name = "su_id")
    protected Long suId;

    /**
     * 供应商部门
     */
    @ToString.Include
    @JSONField(name = "su_dept_no")
    protected String suDeptNo="";

    /**
     * 记录md5签名
     */
    protected String dataMd5;

    /**
     * 添加时间
     */
    protected Date addTime;

    /**
     * 编辑时间
     */
    protected Date editTime;

    /**
     * 操作类型，1新增，2修改，3删除
     */
    @NotNull(message="不能为空", groups = Delete.class)
    @JSONField(name = "oper_type")
    protected Integer operType;

    /**
     * 同步状态，0未同步，1正在同步 2同步成功 3同步失败
     */
    protected Integer syncStatus;

    /**
     * 同步时间
     */
    protected Date syncTime;

    /**
     * 同步信息
     */
    protected String syncMsg;

    /**
     * 失败次数
     */
    protected Integer failedCount;

    @TableField(exist = false)
    private Integer cnt=1;

    /**
     * 保存校验错误信息
     */
    @TableField(exist = false)
    protected String exceptionMessage;

    public Long getSuId() {
        return suId;
    }

    public void setSuId(Long suId) {
        this.suId = suId;
    }

    public String getSuDeptNo() {
        return Optional.ofNullable(suDeptNo).orElse("");
    }

    public void setSuDeptNo(String suDeptNo) {
        this.suDeptNo = suDeptNo;
    }

    @Override
    public String getDataMd5() {
        return Optional.ofNullable(this.sign()).orElse("");
    }

    public void setDataMd5(String dataMd5) {
        this.dataMd5 = dataMd5;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public Integer getOperType() {
        return operType;
    }

    public void setOperType(Integer operType) {
        this.operType = operType;
    }

    public Integer getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(Integer syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Date getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Date syncTime) {
        this.syncTime = syncTime;
    }

    public String getSyncMsg() {
        return syncMsg;
    }

    public void setSyncMsg(String syncMsg) {
        this.syncMsg = syncMsg;
    }

    public Integer getFailedCount() {
        return failedCount == null ? 0 : failedCount;
    }

    public void setFailedCount(Integer failedCount) {
        this.failedCount = failedCount;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public String getFlowKey(){return null;}

    public String getFlowCacheFileMd5(){return null;}

    @Override
    public abstract String getErpPrimaryKey();

    @Override
    public abstract String sign();

    @Override
    public abstract String getTaskNo();

}


