package com.yiling.b2b.app.common.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 版本管理列表查询返回参数
 *
 * @author: yong.zhang
 * @date: 2021/10/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AppVersionVO extends BaseVO {

    @ApiModelProperty(value = "应用ID")
    private Long appId;

    @ApiModelProperty(value = "渠道号")
    private String channelCode;

    @ApiModelProperty(value = "版本名称")
    private String name;

    @ApiModelProperty(value = "版本说明")
    private String description;

    @ApiModelProperty(value = "安装包下载地址")
    private String packageUrl;

    @ApiModelProperty(value = "安装包MD5")
    private String packageMd5;

    @ApiModelProperty(value = "安装包大小(KB)")
    private Long packageSize;

    @ApiModelProperty(value = "APP地址")
    private String appUrl;

    @ApiModelProperty(value = "是否强制升级：1 是 ，2 否")
    private Integer forceUpgradeFlag;

    @ApiModelProperty(value = "升级提示语")
    private String upgradeTips;

    @ApiModelProperty(value = "APP名称-应用编号")
    private String appName;

    @ApiModelProperty(value = "客户端-App类型：1-android 2-ios")
    private Integer appType;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "上传时间")
    private Date createTime;
}
