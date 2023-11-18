package com.yiling.open.erp.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.open.erp.api.EasIncrementStampApi;
import com.yiling.open.erp.dto.EasIncrementStampDTO;
import com.yiling.open.erp.dto.request.SaveEasIncrementStampRequest;
import com.yiling.open.erp.service.EasIncrementStampService;

/**
 * @author: shuang.zhang
 * @date: 2021/8/2
 */
@DubboService
public class EasIncrementStampApiImpl implements EasIncrementStampApi {

    @Autowired
    private EasIncrementStampService easIncrementStampService;

    @Override
    public EasIncrementStampDTO findEasIncrementStampBySuIdAndTaskNo(Long suId, String taskNo) {
        return easIncrementStampService.findEasIncrementStampBySuIdAndTaskNo(suId,taskNo);
    }

    @Override
    public boolean saveOrUpdateEasIncrementStamp(SaveEasIncrementStampRequest request) {
        return easIncrementStampService.saveOrUpdateEasIncrementStamp(request);
    }

}
