package com.yiling.user.enterprise.api;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.user.system.dto.PaymentMethodDTO;

/**
 * 支付方式 API
 *
 * @author: xuan.zhou
 * @date: 2021/6/30
 */
public interface PaymentMethodApi {

    /**
     * 获取平台支付方式
     *
     * @param platformEnum 平台枚举
     * @return
     */
    List<PaymentMethodDTO> listByPlatform(PlatformEnum platformEnum);

    /**
     * 获取渠道定义的支付方式列表
     *
     * @param channelId 渠道ID
     * @return
     */
    List<PaymentMethodDTO> listByChannelId(Long channelId);

    /**
     * 根据店铺企业ID获取店铺支付方式列表
     *
     * @param eid 店铺企业ID
     * @return
     */
    List<PaymentMethodDTO> listByShopEid(Long eid);

    /**
     * 获取客户支付方式列表
     *
     * @param eid 企业ID
     * @param customerEids 客户企业ID列表
     * @param platformEnum 平台枚举
     * @return key：客户企业ID，value：支付方式列表
     */
    Map<Long, List<PaymentMethodDTO>> listByEidAndCustomerEids(Long eid, List<Long> customerEids, PlatformEnum platformEnum);

    /**
     * 获取客户支付方式列表
     *
     * @param customerEid 客户企业ID
     * @param eids 企业ID列表
     * @param platformEnum 平台枚举
     * @return key：企业ID，value：支付方式列表
     */
    Map<Long, List<PaymentMethodDTO>> listByCustomerEidAndEids(Long customerEid, List<Long> eids, PlatformEnum platformEnum);

    /**
     * 获取客户支付方式列表
     *
     * @param eid 企业ID
     * @param customerEid 客户企业ID
     * @param platformEnum 平台枚举
     * @return
     */
    List<PaymentMethodDTO> listByEidAndCustomerEid(Long eid, Long customerEid, PlatformEnum platformEnum);
}
