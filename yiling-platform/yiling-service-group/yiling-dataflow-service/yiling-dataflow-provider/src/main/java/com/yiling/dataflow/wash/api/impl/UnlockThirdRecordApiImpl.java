package com.yiling.dataflow.wash.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.UnlockThirdRecordApi;
import com.yiling.dataflow.wash.dto.UnlockThirdRecordDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockThirdRecordPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockThirdRecordRequest;
import com.yiling.dataflow.wash.service.UnlockThirdRecordService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author fucheng.bai
 * @date 2023/5/11
 */
@DubboService
public class UnlockThirdRecordApiImpl implements UnlockThirdRecordApi {

    @Autowired
    private UnlockThirdRecordService unlockThirdRecordService;


    @Override
    public Page<UnlockThirdRecordDTO> listPage(QueryUnlockThirdRecordPageRequest request) {
        return PojoUtils.map(unlockThirdRecordService.listPage(request), UnlockThirdRecordDTO.class);
    }

    @Override
    public void add(SaveOrUpdateUnlockThirdRecordRequest request) {
        unlockThirdRecordService.add(request);
    }

    @Override
    public void update(SaveOrUpdateUnlockThirdRecordRequest request) {
        unlockThirdRecordService.update(request);
    }

    @Override
    public UnlockThirdRecordDTO getById(Long id) {
        return PojoUtils.map(unlockThirdRecordService.getById(id), UnlockThirdRecordDTO.class);
    }

    @Override
    public UnlockThirdRecordDTO getByOrgCrmId(Long orgCrmId) {
        return PojoUtils.map(unlockThirdRecordService.getByOrgCrmId(orgCrmId), UnlockThirdRecordDTO.class);
    }

    @Override
    public void delete(Long id) {
        unlockThirdRecordService.delete(id);
    }
}
