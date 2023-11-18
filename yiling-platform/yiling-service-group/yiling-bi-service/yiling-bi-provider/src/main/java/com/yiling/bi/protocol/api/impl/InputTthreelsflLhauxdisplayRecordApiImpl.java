package com.yiling.bi.protocol.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.bi.protocol.api.InputTthreelsflLhauxdisplayRecordApi;
import com.yiling.bi.protocol.dto.request.InputTthreelsflLhauxdisplayRecordRequest;
import com.yiling.bi.protocol.service.InputTthreelsflLhauxdisplayRecordService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/1/9
 */
@DubboService
@Slf4j
public class InputTthreelsflLhauxdisplayRecordApiImpl implements InputTthreelsflLhauxdisplayRecordApi {

    @Autowired
    private InputTthreelsflLhauxdisplayRecordService inputTthreelsflLhauxdisplayRecordService;

    @Override
    public boolean saveOrUpdateByUnique(InputTthreelsflLhauxdisplayRecordRequest request) {
        return inputTthreelsflLhauxdisplayRecordService.saveOrUpdateByUnique(request);
    }
}
