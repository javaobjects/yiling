package com.yiling.user.system.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存APP登录信息 Request
 *
 * @author: xuan.zhou
 * @date: 2021/11/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveAppLoginInfoRequest extends BaseRequest {

    /**
     * 应用ID（参考AppEnum）
     */
    private Long appId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 登录方式：password/verifyCode
     */
    private String grantType;

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

    /**
     * 登录时间
     */
    private Date loginTime;

}
