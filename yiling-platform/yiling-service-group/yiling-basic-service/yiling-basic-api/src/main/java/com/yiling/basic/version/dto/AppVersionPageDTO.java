package com.yiling.basic.version.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AppVersionPageDTO extends BaseDTO {

    /**
     * 应用ID
     */
    private Long appId;

    /**
     * 渠道号
     */
    private String channelCode;

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
     * APP名称-应用编号
     */
    private String appName;

    /**
     * 客户端-App类型：1-android 2-ios
     */
    private Integer appType;

    /**
     * 版本号
     */
    private String version;

    /**
     * 上传时间
     */
    private Date createTime;
}
