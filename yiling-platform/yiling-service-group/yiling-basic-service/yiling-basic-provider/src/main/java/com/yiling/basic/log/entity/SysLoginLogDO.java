package com.yiling.basic.log.entity;

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
 * 系统登录日志记录 DO
 * </p>
 *
 * @author lun.yu
 * @date 2021-12-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_login_log")
public class SysLoginLogDO extends BaseDO {

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

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
