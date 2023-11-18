package com.yiling.basic.log.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统登录日志记录 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2021-12-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysLoginLogDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 应用ID（参考AppEnum）
     */
    private String appId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 登录账号：手机号/用户名
     */
    private String loginAccount;

    /**
     * 登录方式：password/verifyCode
     */
    private String grantType;

    /**
     * 登录终端：mobile/pc
     */
    private String grantTerminal;

    /**
     * 登录IP
     */
    private String ipAddress;

    /**
     * 登录浏览器
     */
    private String loginBrowser;

    /**
     * 操作系统
     */
    private String osInfo;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 登录状态：success/fail
     */
    private String loginStatus;

    /**
     * 返回内容
     */
    private String returnContent;

}
