package com.yiling.order.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/** 创建下单设备信息
 * @author zhigang.guo
 * @date: 2022/10/20
 */
@Data
@Accessors(chain = true)
public class CreateOrderDeviceInfoRequest extends BaseRequest {

    private static final long serialVersionUID = 1l;

    /**
     * 订单批次号
     */
    private String batchNo;

    /**
     * IP地址
     */
    private String ip;

    /**
     * IP归属地
     */
    private String ipLocation;

    /**
     * IP归属地-省
     */
    private String ipProvinceName;

    /**
     * IP归属地-市
     */
    private String ipCityName;

    /**
     * IP归属地-区
     */
    private String ipRegionName;

    /**
     * IP运营商
     */
    private String ipOperatorName;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 是否为手机：1-是 2-否
     */
    private Integer mobileFlag;

    /**
     * 终端类型：1-安卓 2-苹果
     */
    private Integer terminalType;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 型号
     */
    private String model;

    /**
     * 操作系统版本
     */
    private String osVersion;

    /**
     * SDK版本
     */
    private String sdkVersion;

    /**
     * 屏幕尺寸
     */
    private String screenSize;

    /**
     * 分辨率
     */
    private String resolution;

    /**
     * 设备唯一标识
     */
    private String udid;

    /**
     * APP版本号
     */
    private String appVersion;

    /**
     * 渠道号
     */
    private String channelCode;


}
