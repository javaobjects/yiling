package com.yiling.dataflow.wash.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.UnlockAreaRecordApi;
import com.yiling.dataflow.wash.dto.UnlockAreaRecordDTO;
import com.yiling.dataflow.wash.dto.UnlockAreaRecordRelationDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockAreaRecordPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockAreaRecordRequest;
import com.yiling.dataflow.wash.service.UnlockAreaRecordService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author fucheng.bai
 * @date 2023/5/12
 */
@DubboService
public class UnlockAreaRecordApiImpl implements UnlockAreaRecordApi {

    @Autowired
    private UnlockAreaRecordService unlockAreaRecordService;

    @Override
    public Page<UnlockAreaRecordDTO> listPage(QueryUnlockAreaRecordPageRequest request) {
        return PojoUtils.map(unlockAreaRecordService.listPage(request), UnlockAreaRecordDTO.class);
    }

    @Override
    public void add(SaveOrUpdateUnlockAreaRecordRequest request) {
        unlockAreaRecordService.add(request);
    }

    @Override
    public void update(SaveOrUpdateUnlockAreaRecordRequest request) {
        unlockAreaRecordService.update(request);
    }

    @Override
    public UnlockAreaRecordDTO getById(Long id) {
        return PojoUtils.map(unlockAreaRecordService.getById(id), UnlockAreaRecordDTO.class);
    }

    @Override
    public UnlockAreaRecordDTO getByClassAndCategoryIdAndRegionCode(Integer customerClassification, Long categoryId, String RegionCode) {
        return PojoUtils.map(unlockAreaRecordService.getByClassAndCategoryIdAndRegionCode(customerClassification, categoryId, RegionCode), UnlockAreaRecordDTO.class);
    }

    @Override
    public void delete(Long id) {
        unlockAreaRecordService.delete(id);
    }


}
