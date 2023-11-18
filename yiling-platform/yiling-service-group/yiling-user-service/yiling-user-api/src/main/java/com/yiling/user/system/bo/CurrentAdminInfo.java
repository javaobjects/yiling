package com.yiling.user.system.bo;

import lombok.Data;

/**
 * 当前登录的运营后台管理员信息
 *
 * @author: xuan.zhou
 * @date: 2021/5/13
 */
@Data
public class CurrentAdminInfo implements java.io.Serializable {

    public static final String CURRENT_USER_ID = "currentUserId";

    /**
     * 用户ID
     */
    private Long currentUserId;
}
