package com.yiling.user.system.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.user.system.entity.PaymentMethodDO;

/**
 * <p>
 * 支付方式表 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-03
 */
public interface PaymentMethodService extends BaseService<PaymentMethodDO> {

    /**
     * 根据状态获取支付方式列表
     *
     * @param platformEnum 平台枚举
     * @param statusEnum 状态枚举
     * @return
     */
    List<PaymentMethodDO> listByPlatform(PlatformEnum platformEnum, EnableStatusEnum statusEnum);
}
