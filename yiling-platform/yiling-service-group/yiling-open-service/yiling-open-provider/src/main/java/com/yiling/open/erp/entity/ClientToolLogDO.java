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
@TableName("client_tool_log")
public class ClientToolLogDO extends BaseDO {
    private Long suId;
    private String clientLog;
    private String mothedNo;
    private Integer logType;
    private Date createTime;

    public Long getSuId() {
        return suId;
    }

    public void setSuId(Long suId) {
        this.suId = suId;
    }

    public String getClientLog() {
        return clientLog;
    }

    public void setClientLog(String clientLog) {
        this.clientLog = clientLog;
    }

    public String getMothedNo() {
        return mothedNo;
    }

    public void setMothedNo(String mothedNo) {
        this.mothedNo = mothedNo;
    }

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
