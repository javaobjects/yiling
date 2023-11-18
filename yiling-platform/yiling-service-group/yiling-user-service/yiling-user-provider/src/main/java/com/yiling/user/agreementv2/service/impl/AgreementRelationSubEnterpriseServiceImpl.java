package com.yiling.user.agreementv2.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.yiling.framework.common.base.BaseDO;
import com.yiling.user.agreementv2.dto.request.AddRelationSubEnterpriseRequest;
import com.yiling.user.agreementv2.entity.AgreementRelationSubEnterpriseDO;
import com.yiling.user.agreementv2.dao.AgreementRelationSubEnterpriseMapper;
import com.yiling.user.agreementv2.service.AgreementRelationSubEnterpriseService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.service.EnterpriseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议乙方关联子公司表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-13
 */
@Service
public class AgreementRelationSubEnterpriseServiceImpl extends BaseServiceImpl<AgreementRelationSubEnterpriseMapper, AgreementRelationSubEnterpriseDO> implements AgreementRelationSubEnterpriseService {

    @Autowired
    EnterpriseService enterpriseService;

    @Override
    public Boolean addRelation(AddRelationSubEnterpriseRequest request) {
        Map<Long, String> map = enterpriseService.listByIds(request.getSubEidList()).stream().collect(Collectors.toMap(BaseDO::getId, EnterpriseDO::getName, (k1, k2) -> k2));

        List<AgreementRelationSubEnterpriseDO> subEnterpriseDOList = request.getSubEidList().stream().map(id -> {
            AgreementRelationSubEnterpriseDO subEnterpriseDO = new AgreementRelationSubEnterpriseDO();
            subEnterpriseDO.setAgreementId(request.getAgreementId());
            subEnterpriseDO.setSecondEid(request.getSecondEid());
            subEnterpriseDO.setRelationEid(id);
            subEnterpriseDO.setRelationEname(map.get(id));
            subEnterpriseDO.setOpUserId(request.getOpUserId());
            return subEnterpriseDO;
        }).collect(Collectors.toList());

        return this.saveBatch(subEnterpriseDOList);
    }
}
