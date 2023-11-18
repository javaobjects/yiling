package com.yiling.user.system.bo;

import java.util.Date;

import lombok.Data;

/**
 * 用户基础信息
 *
 * @author: xuan.zhou
 * @date: 2021/5/18
 */
@Data
public class BaseUser implements java.io.Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

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
     * 状态（参见UserStatusEnum）
     */
    private Integer status;
}
