package com.yiling.framework.common.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 终端设备信息
 *
 * @author xuan.zhou
 * @date 2021/11/26
 */
@Data
@ApiModel(value = "终端设备信息")
public class TerminalInfo implements java.io.Serializable {

    @ApiModelProperty(value = "终端类型：1-Android 2-iOS")
    private Integer terminalType;
    @ApiModelProperty(value = "生产厂商", example = "Xiaomi")
    private String manufacturer;
    @ApiModelProperty(value = "品牌", example = "Xiaomi")
    private String brand;
    @ApiModelProperty(value = "型号", example = "MI 6")
    private String model;
    @ApiModelProperty(value = "操作系统版本", example = "8.0.0")
    private String osVersion;
    @ApiModelProperty(value = "SDK版本", example = "26")
    private String sdkVersion;
    @ApiModelProperty(value = "屏幕尺寸", example = "6.4英寸")
    private String screenSize;
    @ApiModelProperty(value = "分辨率", example = "3040*1440 Full HD+")
    private String resolution;
    @ApiModelProperty(value = "设备唯一标识", example = "b3143a966d59a34123944c2a1cc453d4ea4b2fcd")
    private String udid;
    @ApiModelProperty(value = "APP版本号", example = "1.0.0")
    private String appVersion;
    @ApiModelProperty(value = "渠道号", example = "google_store")
    private String channelCode;
}
