package com.yiling.open.erp.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
    * 应用版本信息
 * @author shuan
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("client_tool_version")
public class ClientToolVersionDO extends BaseDO {

    private static final long serialVersionUID = 4479109693871998639L;


    /**
    * 版本号
    */
    private String version;

    /**
    * 版本名称
    */
    private String name;

    /**
    * 版本说明
    */
    private String description;

    /**
    * 安装包下载地址
    */
    private String packageUrl;

    /**
     * 安装包下载地址
     */
    private String packageMd5;

    /**
    * 是否删除：0-否 1-是
    */
    private Boolean delFlag;

    /**
    * 创建人
    */
    private Long createUser;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 修改人
    */
    private Long updateUser;

    /**
    * 修改时间
    */
    private Date updateTime;

    /**
    * 备注
    */
    private String remark;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPackageUrl() {
        return packageUrl;
    }

    public void setPackageUrl(String packageUrl) {
        this.packageUrl = packageUrl;
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}