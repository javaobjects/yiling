package com.yiling.user.agreementv2.service;

import com.yiling.user.agreementv2.dto.request.AddRelationSubEnterpriseRequest;
import com.yiling.user.agreementv2.entity.AgreementRelationSubEnterpriseDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议乙方关联子公司表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-13
 */
public interface AgreementRelationSubEnterpriseService extends BaseService<AgreementRelationSubEnterpriseDO> {

    /**
     * 添加关联子公司
     *
     * @param request
     * @return
     */
    Boolean addRelation(AddRelationSubEnterpriseRequest request);
}
