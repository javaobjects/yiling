package com.yiling.user.system.bo;

import lombok.Data;

/**
 * 当前登录的用户信息
 *
 * @author: xuan.zhou
 * @date: 2022/11/23
 */
@Data
public class CurrentSjmsUserInfo implements java.io.Serializable {

    public static final String CURRENT_USER_ID = "currentUserId";
    public static final String CURRENT_USER_CODE = "currentUserCode";

    /**
     * 用户ID
     */
    private Long currentUserId;

    /**
     * 用户工号
     */
    private String currentUserCode;

}
