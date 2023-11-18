package com.yiling.user.system.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.system.dto.request.CreateMrUserRegisterRequest;
import com.yiling.user.system.entity.MrUserRegisterDO;

/**
 * <p>
 * 医药代表用户注册信息 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2023-01-31
 */
public interface MrUserRegisterService extends BaseService<MrUserRegisterDO> {

    /**
     * 根据手机号获取用户最近的注册信息
     *
     * @param mobile 手机号
     * @return
     */
    MrUserRegisterDO getByMobile(String mobile);

    /**
     * 创建医药代表用户注册信息
     *
     * @param request
     * @return
     */
    Boolean create(CreateMrUserRegisterRequest request);
}
