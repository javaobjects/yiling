package com.yiling.user.system.bo;

import lombok.Data;

/**
 * 当前登录的用户信息
 *
 * @author: xuan.zhou
 * @date: 2022/3/24
 */
@Data
public class CurrentUserInfo implements java.io.Serializable {

    public static final String CURRENT_USER_ID = "currentUserId";

    /**
     * 用户ID
     */
    private Long currentUserId;

}
