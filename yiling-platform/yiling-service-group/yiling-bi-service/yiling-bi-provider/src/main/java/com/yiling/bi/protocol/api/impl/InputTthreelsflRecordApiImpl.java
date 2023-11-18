package com.yiling.bi.protocol.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.bi.protocol.api.InputTthreelsflRecordApi;
import com.yiling.bi.protocol.dto.request.InputTthreelsflRecordRequest;
import com.yiling.bi.protocol.service.InputTthreelsflRecordService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/1/5
 */
@DubboService
@Slf4j
public class InputTthreelsflRecordApiImpl implements InputTthreelsflRecordApi {

    @Autowired
    private InputTthreelsflRecordService inputTthreelsflRecordService;

    @Override
    public boolean saveOrUpdateByUnique(InputTthreelsflRecordRequest request) {
        return inputTthreelsflRecordService.saveOrUpdateByUnique(request);
    }
}
