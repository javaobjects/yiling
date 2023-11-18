package com.yiling.sjms.gb.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.sjms.gb.api.GbFormSubmitProcessApi;
import com.yiling.sjms.gb.dto.request.SaveGBCancelInfoRequest;
import com.yiling.sjms.gb.dto.request.SaveGBInfoRequest;
import com.yiling.sjms.gb.service.GbFormSubmitService;

/**
 * 团购表单提交流程
 *
 * @author: wei.wang
 * @date: 2022/11/28
 */
@DubboService
public class GbFormSubmitProcessApiImpl implements GbFormSubmitProcessApi {

    @Autowired
    private GbFormSubmitService gbFormSubmitService;

    @Override
    public Boolean submitFormProcess(SaveGBInfoRequest request) {

        return gbFormSubmitService.submitFormProcess(request);
    }

    @Override
    public Boolean cancelFormProcess(SaveGBCancelInfoRequest request) {
        return gbFormSubmitService.cancelFormProcess(request);
    }

    @Override
    public Boolean feeApplicationFormProcess(SaveGBCancelInfoRequest request) {
        return gbFormSubmitService.feeApplicationFormProcess(request);
    }
}
