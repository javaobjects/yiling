package com.yiling.user.system.dto.request;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建Staff用户 Request
 *
 * @author: xuan.zhou
 * @date: 2022/9/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateStaffRequest extends BaseRequest {

    private static final long serialVersionUID = 3064131002524739290L;

    /**
     * 用户名
     */
    @Deprecated
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 出生年月日
     */
    private Date birthday;

    /**
     * 手机号
     */
    @NotEmpty
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 微信unionID
     */
    @Deprecated
    private String unionId;

    /**
     * 当用户信息存在时，选择忽略
     */
    private boolean ignoreExists = false;
}
