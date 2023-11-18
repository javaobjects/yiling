package com.yiling.sjms.form.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.sjms.form.dto.request.ApproveFormRequest;
import com.yiling.sjms.form.dto.request.CreateFormRequest;
import com.yiling.sjms.form.dto.request.RejectFormRequest;
import com.yiling.sjms.form.dto.request.SubmitFormRequest;
import com.yiling.sjms.form.service.BizFormService;
import com.yiling.sjms.form.service.FormService;

import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 抽象业务表单实现
 *
 * @author: xuan.zhou
 * @date: 2023/2/24
 */
@Slf4j
public abstract class AbstractBizFormServiceImpl implements BizFormService {

    @Autowired
    FormService formService;

    @Override
    @GlobalTransactional
    public <T extends CreateFormRequest> Long create(T request) {
        Long formId = formService.create(request);
        this.doCreate(request, formId);
        return formId;
    }

    protected abstract <T extends CreateFormRequest> void doCreate(T request, Long formId);

    @Override
    @GlobalTransactional
    public <T extends SubmitFormRequest> Boolean submit(T request) {
        formService.submit(request);
        this.doSubmit(request);
        return true;
    }

    protected abstract <T extends SubmitFormRequest> void doSubmit(T request);

    @Override
    @GlobalTransactional
    public <T extends RejectFormRequest> Boolean reject(T request) {
        formService.reject(request);
        this.doReject(request);
        return true;
    }

    protected abstract <T extends RejectFormRequest> void doReject(T request);

    @Override
    @GlobalTransactional
    public <T extends ApproveFormRequest> Boolean approve(T request) {
        formService.approve(request);
        this.doApprove(request);
        return true;
    }

    protected abstract <T extends ApproveFormRequest> void doApprove(T request);

    @Override
    public Boolean delete(Long id, Long opUserId) {
        return formService.delete(id, opUserId);
    }
}
