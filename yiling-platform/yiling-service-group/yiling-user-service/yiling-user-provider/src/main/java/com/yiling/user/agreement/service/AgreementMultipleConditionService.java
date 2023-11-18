package com.yiling.user.agreement.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.agreement.entity.AgreementMultipleConditionDO;

/**
 * <p>
 * 协议多选条件表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-06-21
 */
public interface AgreementMultipleConditionService extends BaseService<AgreementMultipleConditionDO> {

    /**
     * 查询某一个协议下面的类型多选值
     * @param agreementId
     * @param type
     * @return
     */
    List<Integer> getConditionValueByTypeAndAgreementId(Long agreementId, String type);

    /**
     * 查询某一个协议下面的所有多选
     * @param agreementId
     * @return
     */
    Map<String,List<Integer>> getConditionValueByAgreementId(Long agreementId);

    /**
     * 查询某一个协议下面的所有多选
     * @param agreementIds
     * @return
     */
    Map<Long,Map<String,List<Integer>>> getConditionValueByAgreementId(List<Long> agreementIds);
}
