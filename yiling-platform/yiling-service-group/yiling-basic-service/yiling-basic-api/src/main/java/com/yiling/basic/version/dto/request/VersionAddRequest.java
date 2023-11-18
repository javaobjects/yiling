package com.yiling.basic.version.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class VersionAddRequest extends BaseRequest {

    private Long id;

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
}
