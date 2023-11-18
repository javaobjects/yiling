package com.yiling.user.system.service;

import com.yiling.user.system.entity.UserAuthsDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 用户授权信息表 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-09-23
 */
public interface UserAuthsService extends BaseService<UserAuthsDO> {

    /**
     * 根据登录类型及标识获取用户授权信息
     *
     * @param appId 应用ID
     * @param identityType 登录类型
     * @param identifier 标识
     * @return com.yiling.user.system.entity.UserAuthsDO
     * @author xuan.zhou
     * @date 2022/9/23
     **/
    UserAuthsDO getByIdentifier(String appId, String identityType, String identifier);

    /**
     * 获取用户授权信息
     *
     * @param userId 用户ID
     * @param appId 应用ID
     * @param identityType 登录类型
     * @return com.yiling.user.system.entity.UserAuthsDO
     * @author xuan.zhou
     * @date 2022/9/27
     **/
    UserAuthsDO getByUser(Long userId, String appId, String identityType);

    /**
     * 创建用户名授权信息
     *
     * @param userId 用户ID
     * @param appId 应用ID
     * @param username 用户名
     * @param password 密码
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2022/9/26
     **/
    Boolean createUsernameIdentifier(Long userId, String appId, String username, String password);

    /**
     * 创建手机号授权信息
     *
     * @param userId 用户ID
     * @param appId 应用ID
     * @param mobile 手机号
     * @param password 密码
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2022/9/26
     **/
    Boolean createMobileIdentifier(Long userId, String appId, String mobile, String password);

    /**
     * 创建微信授权信息
     *
     * @param userId 用户ID
     * @param appId 应用ID
     * @param unionId 微信unionID
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2022/9/26
     **/
    Boolean createWeixinIdentifier(Long userId, String appId, String unionId);

    /**
     * 修改密码
     *
     * @param userId 用户ID
     * @param appId 应用ID
     * @param password 新密码（明文）
     * @param opUserId 操作人ID
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2022/9/23
     **/
    Boolean updatePassword(Long userId, String appId, String password, Long opUserId);

    /**
     * 修改用户名
     *
     * @param userId 用户ID
     * @param appId 应用ID
     * @param username 新用户名
     * @param opUserId 操作人ID
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2022/9/23
     **/
    Boolean updateUsername(Long userId, String appId, String username, Long opUserId);

    /**
     * 修改手机号
     *
     * @param userId 用户ID
     * @param appId 应用ID
     * @param mobile 新手机号
     * @param opUserId 操作人ID
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2022/9/23
     **/
    Boolean updateMobile(Long userId, String appId, String mobile, Long opUserId);

    /**
     * 清除用户所有授权信息
     *
     * @param userId 用户ID
     * @param opUserId 操作人ID
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2022/9/28
     **/
    Boolean clear(Long userId, Long opUserId);
}
