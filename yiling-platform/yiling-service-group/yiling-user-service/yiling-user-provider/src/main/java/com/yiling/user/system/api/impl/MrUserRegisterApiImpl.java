package com.yiling.user.system.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.api.MrUserRegisterApi;
import com.yiling.user.system.dto.MrUserRegisterDTO;
import com.yiling.user.system.dto.request.CreateMrUserRegisterRequest;
import com.yiling.user.system.service.MrUserRegisterService;

/**
 * 医药代表用户注册信息 API 实现
 *
 * @author: xuan.zhou
 * @date: 2023/1/31
 */
@DubboService
public class MrUserRegisterApiImpl implements MrUserRegisterApi {

    @Autowired
    MrUserRegisterService mrUserRegisterService;

    @Override
    public MrUserRegisterDTO getByMobile(String mobile) {
        return PojoUtils.map(mrUserRegisterService.getByMobile(mobile), MrUserRegisterDTO.class);
    }

    @Override
    public Boolean create(CreateMrUserRegisterRequest request) {
        return mrUserRegisterService.create(request);
    }
}
