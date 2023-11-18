package com.yiling.user.system.api;

import com.yiling.user.system.dto.MrUserRegisterDTO;
import com.yiling.user.system.dto.request.CreateMrUserRegisterRequest;

/**
 * 医药代表用户注册信息 API
 *
 * @author: xuan.zhou
 * @date: 2023/1/31
 */
public interface MrUserRegisterApi {

    /**
     * 根据手机号获取用户最近的注册信息
     *
     * @param mobile 手机号
     * @return
     */
    MrUserRegisterDTO getByMobile(String mobile);

    /**
     * 创建医药代表用户注册信息
     *
     * @param request
     * @return
     */
    Boolean create(CreateMrUserRegisterRequest request);
}
