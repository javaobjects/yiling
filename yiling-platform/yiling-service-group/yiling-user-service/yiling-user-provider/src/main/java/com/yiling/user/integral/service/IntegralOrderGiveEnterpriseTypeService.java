package com.yiling.user.integral.service;

import java.util.List;

import com.yiling.user.integral.entity.IntegralOrderGiveEnterpriseTypeDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 订单送积分-指定企业类型表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
public interface IntegralOrderGiveEnterpriseTypeService extends BaseService<IntegralOrderGiveEnterpriseTypeDO> {

    /**
     * 保存指定企业类型
     *
     * @param giveRuleId
     * @param enterpriseTypeList
     * @param opUserId
     * @return
     */
    boolean saveEnterpriseType(Long giveRuleId, List<Integer> enterpriseTypeList, Long opUserId);

    /**
     * 获取企业类型
     *
     * @param giveRuleId
     * @return
     */
    List<Integer> getEnterpriseTypeList(Long giveRuleId);


}
