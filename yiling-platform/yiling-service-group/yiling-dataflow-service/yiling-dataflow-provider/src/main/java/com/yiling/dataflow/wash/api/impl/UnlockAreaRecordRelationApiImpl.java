package com.yiling.dataflow.wash.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.wash.api.UnlockAreaRecordRelationApi;
import com.yiling.dataflow.wash.dto.UnlockAreaRecordRelationDTO;
import com.yiling.dataflow.wash.service.UnlockAreaRecordRelationService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author fucheng.bai
 * @date 2023/5/16
 */
@DubboService
public class UnlockAreaRecordRelationApiImpl implements UnlockAreaRecordRelationApi {

    @Autowired
    private UnlockAreaRecordRelationService unlockAreaRecordRelationService;

    @Override
    public List<UnlockAreaRecordRelationDTO> getListByUnlockAreaRecordRelationId(Long unlockAreaRecordRelationId) {
        return PojoUtils.map(unlockAreaRecordRelationService.getByUnlockAreaRecordId(unlockAreaRecordRelationId), UnlockAreaRecordRelationDTO.class);
    }

    @Override
    public List<UnlockAreaRecordRelationDTO> getByClassAndCategoryId(Integer customerClassification, Long categoryId) {
        return PojoUtils.map(unlockAreaRecordRelationService.getByClassAndCategoryId(customerClassification, categoryId), UnlockAreaRecordRelationDTO.class);
    }


}
