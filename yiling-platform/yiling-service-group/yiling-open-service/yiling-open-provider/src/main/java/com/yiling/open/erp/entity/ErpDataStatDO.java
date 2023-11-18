package com.yiling.open.erp.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shuan
 */
public class ErpDataStatDO implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long id;// int(11) NOT NULL AUTO_INCREMENT,
    private Long suId;// int(11) NOT NULL,
    private String suDeptNo; // 分公司编号
    private String taskNo;// 任务类型
    private Date statDate;// date NOT NULL,
    private Integer statHour;// int(11) NOT NULL,
    private Integer operType;
    private Date createTime;// datetime DEFAULT NULL,
    
    public String getTaskNo() {
        return taskNo;
    }
    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getSuId() {
        return suId;
    }
    public void setSuId(Long suId) {
        this.suId = suId;
    }

    public String getSuDeptNo() {
        return suDeptNo;
    }

    public void setSuDeptNo(String suDeptNo) {
        this.suDeptNo = suDeptNo;
    }

    public Date getStatDate() {
        return statDate;
    }
    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }
    public Integer getStatHour() {
        return statHour;
    }
    public void setStatHour(Integer statHour) {
        this.statHour = statHour;
    }
    
    public Integer getOperType() {
        return operType;
    }
    public void setOperType(Integer operType) {
        this.operType = operType;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    
}
