package com.yiling.basic.version.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

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
public class AppVersionDTO extends BaseDTO {

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

    //===================================================

    /**
     * 版本平台：0-安卓 1-IOS
     */
    private Integer appType;
}
