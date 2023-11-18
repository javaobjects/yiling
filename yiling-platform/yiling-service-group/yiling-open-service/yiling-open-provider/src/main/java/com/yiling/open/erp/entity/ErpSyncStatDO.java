package com.yiling.open.erp.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author: qi.xiong
 * @Date: 2018/10/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("erp_sync_stat")
public class ErpSyncStatDO extends BaseDO {

    private static final long serialVersionUID = -109415089964388618L;
    //任务编号
    private String taskNo;
    //供应商id
    private Long suId;
    //分公司编码
    private String suDeptNo;
    //统计日期
    private Date statDate;
    //统计小时
    private Integer statHour;
    //新增数
    private Integer addNum;
    //更新数
    private Integer updateNum;
    //删除数
    private Integer deleteNum;
    //创建时间
    private Date createTime;
    //修改时间
    private Date modifyTime;

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
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

    public Integer getAddNum() {
        return addNum;
    }

    public void setAddNum(Integer addNum) {
        this.addNum = addNum;
    }

    public Integer getUpdateNum() {
        return updateNum;
    }

    public void setUpdateNum(Integer updateNum) {
        this.updateNum = updateNum;
    }

    public Integer getDeleteNum() {
        return deleteNum;
    }

    public void setDeleteNum(Integer deleteNum) {
        this.deleteNum = deleteNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
