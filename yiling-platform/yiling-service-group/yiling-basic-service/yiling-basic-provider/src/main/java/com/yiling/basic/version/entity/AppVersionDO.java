package com.yiling.basic.version.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 应用版本信息
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_version")
public class AppVersionDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 应用ID
     */
    private Long appId;

    /**
     * 渠道号
     */
    private String channelCode;

    /**
     * 版本号
     */
    @Version
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
     * 安装包MD5
     */
    private String packageMd5;

    /**
     * 安装包大小(KB)
     */
    private Long packageSize;

    /**
     * APP地址
     */
    private String appUrl;


    /**
     * 是否强制升级：1 是 ，2 否
     */
    private Integer forceUpgradeFlag;

    /**
     * 升级提示语
     */
    private String upgradeTips;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
