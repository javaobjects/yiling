package com.yiling.sjms.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.sjms.demo.dto.request.ApproveDemoFormRequest;
import com.yiling.sjms.demo.dto.request.CreateDemoFormRequest;
import com.yiling.sjms.demo.dto.request.RejectDemoFormRequest;
import com.yiling.sjms.demo.dto.request.SubmitDemoFormRequest;
import com.yiling.sjms.demo.service.DemoFormService;
import com.yiling.sjms.form.dto.request.ApproveFormRequest;
import com.yiling.sjms.form.dto.request.CreateFormRequest;
import com.yiling.sjms.form.dto.request.RejectFormRequest;
import com.yiling.sjms.form.dto.request.SubmitFormRequest;
import com.yiling.sjms.form.service.impl.AbstractBizFormServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2023/2/24
 */
@Slf4j
@Service
public class DemoFormServiceImpl extends AbstractBizFormServiceImpl implements DemoFormService {

    @Autowired
    com.yiling.sjms.gb.service.GbFormService gbFormService;

    @Override
    protected void doCreate(CreateFormRequest request, Long formId) {
        CreateDemoFormRequest request1 = (CreateDemoFormRequest) request;
        log.info("gbType={}, formId={}", request1.getName1(), formId);
    }

    @Override
    protected <T extends SubmitFormRequest> void doSubmit(T request) {
        SubmitDemoFormRequest request1 = (SubmitDemoFormRequest) request;
        log.info("gbType={}", request1.getName2());
    }

    @Override
    protected <T extends RejectFormRequest> void doReject(T request) {
        RejectDemoFormRequest request1 = (RejectDemoFormRequest) request;
        log.info("gbType={}", request1.getName4());
    }

    @Override
    protected <T extends ApproveFormRequest> void doApprove(T request) {
        ApproveDemoFormRequest request1 = (ApproveDemoFormRequest) request;
        log.info("gbType={}", request1.getName3());
    }
}
