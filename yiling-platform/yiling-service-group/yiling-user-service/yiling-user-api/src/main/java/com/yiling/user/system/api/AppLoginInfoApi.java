package com.yiling.user.system.api;

import com.yiling.user.system.dto.AppLoginInfoDTO;
import com.yiling.user.system.dto.request.SaveAppLoginInfoRequest;

/**
 * APP登录信息 API
 *
 * @author: xuan.zhou
 * @date: 2021/11/26
 */
public interface AppLoginInfoApi {

    public static final String GRANT_TYPE_PASSWORD = "password";
    public static final String GRANT_TYPE_VERIFY_CODE = "verifyCode";

    /**
     * 获取最近的登录信息
     *
     * @param appId 应用ID
     * @param userId 用户ID
     * @return
     */
    AppLoginInfoDTO getLatestLoginInfoByUserId(Long appId, Long userId);

    /**
     * 保存登录信息
     *
     * @param request
     * @return
     */
    Boolean saveOrUpdate(SaveAppLoginInfoRequest request);
}
