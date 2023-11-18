package com.yiling.user.enterprise.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListRequest;
import com.yiling.user.enterprise.dto.request.UpdateOpenPayRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerPaymentMethodDO;
import com.yiling.user.system.entity.PaymentMethodDO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-03
 */
public interface EnterpriseCustomerPaymentMethodService extends BaseService<EnterpriseCustomerPaymentMethodDO> {

    /**
     * 获取客户支付方式列表
     *
     * @param eid 企业ID
     * @param customerEids 客户企业ID列表
     * @param platformEnum 平台枚举
     * @return key：客户企业ID，value：支付方式列表
     */
    Map<Long, List<PaymentMethodDO>> listByEidAndCustomerEids(Long eid, List<Long> customerEids, PlatformEnum platformEnum);

    /**
     * 获取客户支付方式列表
     *
     * @param customerEid 客户企业ID
     * @param eids 企业ID列表
     * @param platformEnum 平台枚举
     * @return key：企业ID，value：支付方式列表
     */
    Map<Long, List<PaymentMethodDO>> listByCustomerEidAndEids(Long customerEid, List<Long> eids, PlatformEnum platformEnum);

    /**
     * 获取客户支付方式列表
     *
     * @param eid 企业ID
     * @param customerEid 客户企业ID
     * @param platformEnum 平台类型
     * @return
     */
    List<PaymentMethodDO> listByEidAndCustomerEid(Long eid, Long customerEid, PlatformEnum platformEnum);

    /**
     * 保存客户支付方式
     *
     * @param eid 企业ID
     * @param customerEid 客户企业ID
     * @param paymentMethodIds 支付方式ID列表
     * @param platformEnum 平台类型
     * @param opUserId 操作人ID
     * @return
     */
    boolean saveCustomerPaymentMethods(Long eid, Long customerEid, List<Long> paymentMethodIds, PlatformEnum platformEnum, Long opUserId);

    /**
     * 开通线下支付
     *
     * @param request
     * @return
     */
    Boolean openOfflinePay(UpdateOpenPayRequest request);

    /**
     * 获取批量客户支付方式
     *
     * @param eid 企业ID
     * @param customerEidList 客户企业ID
     * @param platformEnum 平台
     * @return key为客户企业ID，value为对应的支付方式
     */
    Map<Long, List<EnterpriseCustomerPaymentMethodDO>> listMapCustomerPaymentMethods(Long eid, List<Long> customerEidList, PlatformEnum platformEnum);
}
