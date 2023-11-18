package com.yiling.user.agreement.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.api.AgreementRebateDictionariesApi;
import com.yiling.user.agreement.dto.AgreementRebateDictionariesDTO;
import com.yiling.user.agreement.enums.AgreementRebateDictionariesStatusEnum;
import com.yiling.user.agreement.service.AgreementRebateDictionariesService;

/**
 * @author: dexi.yao
 * @date: 2021/7/29
 */
@DubboService
public class AgreementRebateDictionariesApiImpl implements AgreementRebateDictionariesApi {
    @Autowired
    private AgreementRebateDictionariesService agreementRebateDictionariesService;

    @Override
    public List<AgreementRebateDictionariesDTO> listByIds(List<Long> ids, AgreementRebateDictionariesStatusEnum code) {
        return PojoUtils.map(agreementRebateDictionariesService.listByIds(ids,code),AgreementRebateDictionariesDTO.class);
    }

    @Override
    public Map<Long, List<AgreementRebateDictionariesDTO>> listByParentIds(List<Long> parentIds, AgreementRebateDictionariesStatusEnum code) {
        return agreementRebateDictionariesService.listByParentIds(parentIds,code);
    }

    @Override
    public List<AgreementRebateDictionariesDTO> listByNameList(List<String> nameList, AgreementRebateDictionariesStatusEnum code) {
        return PojoUtils.map(agreementRebateDictionariesService.listByNameList(nameList,code),AgreementRebateDictionariesDTO.class);
    }
}