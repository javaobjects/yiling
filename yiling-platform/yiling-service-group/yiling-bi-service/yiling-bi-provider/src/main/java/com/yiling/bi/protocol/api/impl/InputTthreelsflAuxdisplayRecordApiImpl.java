package com.yiling.bi.protocol.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.bi.protocol.api.InputTthreelsflAuxdisplayRecordApi;
import com.yiling.bi.protocol.dto.request.InputTthreelsflAuxdisplayRecordRequest;
import com.yiling.bi.protocol.service.InputTthreelsflAuxdisplayRecordService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/1/9
 */
@DubboService
@Slf4j
public class InputTthreelsflAuxdisplayRecordApiImpl implements InputTthreelsflAuxdisplayRecordApi {

    @Autowired
    private InputTthreelsflAuxdisplayRecordService inputTthreelsflAuxdisplayRecordService;

    @Override
    public boolean saveOrUpdateByUnique(InputTthreelsflAuxdisplayRecordRequest request) {
        return inputTthreelsflAuxdisplayRecordService.saveOrUpdateByUnique(request);
    }
}
