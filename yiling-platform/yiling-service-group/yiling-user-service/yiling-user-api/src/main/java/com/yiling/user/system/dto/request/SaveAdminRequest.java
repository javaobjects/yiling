package com.yiling.user.system.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存用户 Request
 *
 * @author: xuan.zhou
 * @date: 2021/5/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveAdminRequest extends BaseRequest {

    private static final long serialVersionUID = -3337103042833235608L;

    /**
     * ID
     */
    private Long id;

    /**
     * 用户名
     */
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
     * 性别：1-男 2-女
     */
    private Integer gender;

    /**
     * 出生年月日
     */
    private Date birthday;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 角色ID集合
     */
    private List<Long> roleIdList;
}
