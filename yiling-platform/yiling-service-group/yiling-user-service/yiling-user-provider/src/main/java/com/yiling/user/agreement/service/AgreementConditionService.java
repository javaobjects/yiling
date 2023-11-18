package com.yiling.user.agreement.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.agreement.entity.AgreementConditionDO;

/**
 * <p>
 * 协议返利条件 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-06-21
 */
public interface AgreementConditionService extends BaseService<AgreementConditionDO> {

    /**
     * 通过协议Id获取所有的条件列表
     * @param agreementIds
     * @return
     */
    Map<Long,List<AgreementConditionDO>> getAgreementConditionListByAgreementId(List<Long> agreementIds);

//    Boolean

    /**
     * 根据协议id查询协议购进额条件
     *
     * @param agreementIds
     * @return
     */
    List<AgreementConditionDO> queryBuyConditionByAgreementId(List<Long> agreementIds);
}
