package com.yiling.user.enterprise.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.entity.EnterpriseChannelPaymentMethodDO;
import com.yiling.user.system.entity.PaymentMethodDO;

/**
 * <p>
 * 渠道与支付方式关联表 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-03
 */
public interface EnterpriseChannelPaymentMethodService extends BaseService<EnterpriseChannelPaymentMethodDO> {

    /**
     * 获取渠道定义的支付方式列表
     *
     * @param channelId 渠道ID
     * @return
     */
    List<PaymentMethodDO> listByChannelId(Long channelId);

    /**
     * 批量获取渠道定义的支付方式列表
     *
     * @param channelIds 渠道ID列表
     * @return key：渠道ID，value：支付方式列表
     */
    Map<Long, List<PaymentMethodDO>> mapByChannelIds(List<Long> channelIds);
}
