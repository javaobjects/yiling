package com.yiling.user.integral.api;

/**
 * 积分 API
 *
 * @author: lun.yu
 * @date: 2021/10/20
 */
public interface IntegralApi {

    /**
     * 根据企业ID获取企业积分
     * @param eid
     * @return
     */
    @Deprecated
    Integer getEnterpriseIntegral(Long eid);

}
