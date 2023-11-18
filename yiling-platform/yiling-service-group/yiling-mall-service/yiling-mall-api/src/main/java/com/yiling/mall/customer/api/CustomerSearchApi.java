package com.yiling.mall.customer.api;

import com.yiling.mall.customer.dto.request.CustomerVerificationRequest;

/**
 * 客户选择
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.order.api
 * @date: 2021/9/15
 */
public interface CustomerSearchApi {

    /**
     *  验证是否是我的用户
     * @param request 企业信息
     * @return
     */
    Boolean checkIsMyCustomer(CustomerVerificationRequest request);


}
