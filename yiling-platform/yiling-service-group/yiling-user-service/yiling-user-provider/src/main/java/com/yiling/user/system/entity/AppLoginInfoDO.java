package com.yiling.user.system.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 登录信息
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-11-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_login_info")
public class AppLoginInfoDO extends BaseDO {

    private static final long serialVersionUID = 1L;

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

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
