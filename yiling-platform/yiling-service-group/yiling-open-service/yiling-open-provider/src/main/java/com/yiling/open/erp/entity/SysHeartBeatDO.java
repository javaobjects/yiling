package com.yiling.open.erp.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**  
 * 表sys_heart_beat对应的Model信息
 *
 *@author wanfei.zhang
 *@date 2016-07-21 10:23:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_heart_beat")
public class SysHeartBeatDO extends BaseDO {
    private static final long serialVersionUID = 1L;

    private Long  suId;

    private String  suName;

    private String  suPrefix;

    private String  mac;

    private String  ip;

    private String  computerName;

    private String  processId;

    private String  runtaskIds;

    private String  versions;

    private String  runPath;

    private Date  createTime;

    public Long getSuId() {
        return suId;
    }

    public void setSuId(Long suId) {
        this.suId = suId;
    }

    public String getSuName() {
        return suName;
    }

    public void setSuName(String suName) {
        this.suName = suName;
    }

    public String getSuPrefix() {
        return suPrefix;
    }

    public void setSuPrefix(String suPrefix) {
        this.suPrefix = suPrefix;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getRuntaskIds() {
        return runtaskIds;
    }

    public void setRuntaskIds(String runtaskIds) {
        this.runtaskIds = runtaskIds;
    }

    public String getVersions() {
        return versions;
    }

    public void setVersions(String versions) {
        this.versions = versions;
    }

    public String getRunPath() {
        return runPath;
    }

    public void setRunPath(String runPath) {
        this.runPath = runPath;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}