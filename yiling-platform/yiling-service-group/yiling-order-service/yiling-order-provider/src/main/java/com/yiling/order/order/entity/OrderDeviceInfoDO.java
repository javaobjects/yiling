package com.yiling.order.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单-下单设备信息
 * </p>
 *
 * @author zhigang.guo
 * @date 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("order_device_info")
public class OrderDeviceInfoDO extends BaseDO {

    private static final long serialVersionUID = 1L;

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
