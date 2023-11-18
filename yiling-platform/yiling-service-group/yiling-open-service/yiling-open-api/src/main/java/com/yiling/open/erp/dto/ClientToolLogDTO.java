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
public class ClientToolLogDTO extends BaseDTO {

    /**
     * 商业公司编码
     */
    private Long suId;

    /**
     * 客户端日志
     */
    private String  clientLog;

    /**
     * 方法编码
     */
    private String  mothedNo;

    /**
     * 日志类型：1客户端运行日志2守护程序运行日志3错误日志告警
     */
    private Integer logType;

    /**
     * 创建时间
     */
    private Date    createTime;

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
