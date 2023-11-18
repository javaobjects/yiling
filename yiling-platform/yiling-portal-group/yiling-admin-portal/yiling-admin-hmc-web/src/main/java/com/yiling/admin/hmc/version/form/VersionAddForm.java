package com.yiling.admin.hmc.version.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
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
public class VersionAddForm extends BaseForm {
    
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "应用ID")
    @NotNull(message = "应用不能为空")
    private Long appId;

    @ApiModelProperty(value = "渠道号")
    @NotNull(message = "渠道号不能为空")
    private String channelCode;

    @ApiModelProperty(value = "版本号")
    @NotNull(message = "版本号不能为空")
    private String version;

    @ApiModelProperty(value = "版本名称")
    @NotNull(message = "版本名称不能为空")
    private String name;

    @ApiModelProperty(value = "版本说明")
    @Size(message = "版本说明不能超过200个字符", max = 200)
    private String description;

    @ApiModelProperty(value = "App类型：1-android 2-ios")
    @NotNull(message = "App类型不能为空")
    private Integer appType;

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
    @Size(message = "升级提示语长度不能超过200个字符", max = 200)
    private String upgradeTips;
}
