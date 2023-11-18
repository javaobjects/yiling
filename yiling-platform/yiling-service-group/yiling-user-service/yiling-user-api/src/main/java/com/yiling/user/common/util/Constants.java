package com.yiling.user.common.util;

/**
 * User服务系统常量
 *
 * @author: lun.yu
 * @date: 2022/1/4
 */
public class Constants {


    /**
     * 登录状态：success/fail
     */
    public static final String LOGIN_STATUS_SUCCESS = "success";
    public static final String LOGIN_STATUS_FAIL = "fail";

    /**
     * 登录方式：password/verifyCode/weChat
     */
    public static final String GRANT_TYPE_PASSWORD = "password";
    public static final String GRANT_TYPE_VERIFY_CODE = "verifyCode";
    public static final String GRANT_TYPE_WECHAT = "weChat";

    /**
     * HMC系统登录
     */
    public static final String TOPIC_HMC_LOGIN = "topic_hmc_login";


    /**
     * 患者端昵称前缀
     */
    public static final String HZ_NICKNAME_PREFIX = "HZ";

    /**
     * 患者端默认头像
     */
    public static final String HZ_DEFAULT_AVATAR = "https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/picture/hz-default-avatar.png";


}
