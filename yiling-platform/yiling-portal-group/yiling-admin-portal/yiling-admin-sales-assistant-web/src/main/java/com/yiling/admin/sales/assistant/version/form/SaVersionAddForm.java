package com.yiling.admin.sales.assistant.version.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2021/9/16
 */
@Data
public class SaVersionAddForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "版本名称")
    private String name;

    @NotNull
    @ApiModelProperty(value = "是否强制升级：1 是 ，2 否")
    private Integer forceUpgradeFlag;

    @NotNull
    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "版本描述")
    private String description;

    @NotNull
    @ApiModelProperty(value = "版本平台：1-安卓 2-IOS")
    private Integer appType;

    @ApiModelProperty(value = "APP安装包地址")
    private String packageUrl;

    @ApiModelProperty(value = "文件大小(KB)-新增")
    private long packageSize;

    @ApiModelProperty(value = "安装包MD5-新增")
    private String packageMd5;
}
