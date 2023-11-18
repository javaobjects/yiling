package com.yiling.admin.system.system.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统登录日志记录 VO
 * </p>
 *
 * @author lun.yu
 * @date 2021-12-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysLoginLogVO extends BaseVO {

    /**
     * 应用ID（参考AppEnum）
     */
    @ApiModelProperty("应用ID")
    private String appId;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String userName;

    /**
     * 登录账号：手机号/用户名
     */
    @ApiModelProperty("登录账号：手机号/用户名")
    private String loginAccount;

    /**
     * 登录方式：password/verifyCode
     */
    @ApiModelProperty("登录方式")
    private String grantType;

    /**
     * 登录终端：mobile/pc
     */
    @ApiModelProperty("登录终端")
    private String grantTerminal;

    /**
     * 登录IP
     */
    @ApiModelProperty("登录IP")
    private String ipAddress;

    /**
     * 登录浏览器
     */
    @ApiModelProperty("登录浏览器")
    private String loginBrowser;

    /**
     * 操作系统
     */
    @ApiModelProperty("操作系统")
    private String osInfo;

    /**
     * 用户代理
     */
    @ApiModelProperty("用户代理")
    private String userAgent;

    /**
     * 登录时间
     */
    @ApiModelProperty("登录时间")
    private Date loginTime;

    /**
     * 登录状态：success/fail
     */
    @ApiModelProperty("登录状态")
    private String loginStatus;

    /**
     * 返回内容
     */
    @ApiModelProperty("返回内容")
    private String returnContent;

}
