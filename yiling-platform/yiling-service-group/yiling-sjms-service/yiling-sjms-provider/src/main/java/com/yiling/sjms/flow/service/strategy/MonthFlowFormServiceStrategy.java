package com.yiling.sjms.flow.service.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.sjms.flow.service.MonthFlowFormService;
import com.yiling.sjms.form.FleeFormErrorCode;
import com.yiling.sjms.form.dto.request.ApproveFormRequest;
import com.yiling.sjms.form.dto.request.RejectFormRequest;
import com.yiling.sjms.form.dto.request.ResubmitFormRequest;
import com.yiling.sjms.form.dto.request.SubmitFormRequest;
import com.yiling.sjms.form.entity.FormDO;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.form.service.FormService;
import com.yiling.sjms.form.service.strategy.AbstractFormStatusHandler;
import com.yiling.sjms.gb.dto.request.UpdateGBFormInfoRequest;

/**
 * @author: gxl
 * @date: 2023/6/28
 */
@Service
public class MonthFlowFormServiceStrategy extends AbstractFormStatusHandler {

    @Autowired
    private FormService formService;

    @Autowired
    private MonthFlowFormService monthFlowFormService;

    @Override
    public Integer getCurrentType() {
        return FormTypeEnum.FIX_MONTH_FLOW.getCode();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean approve(UpdateGBFormInfoRequest request) {
        FormDO formDO = formService.getById(request.getId());
        if (formDO == null) {
            throw new BusinessException(FleeFormErrorCode.FORM_NOT_FIND);
        }
        if (request.getOriginalStatus() != null && FormStatusEnum.getByCode(formDO.getStatus()) != request.getOriginalStatus()) {
            throw new BusinessException(FleeFormErrorCode.FORM_STATUS_CHANGE);
        }
        monthFlowFormService.approveTo(formDO.getId());
        ApproveFormRequest approveFormRequest = new ApproveFormRequest();
        approveFormRequest.setId(request.getId());
        return formService.approve(approveFormRequest);
    }

    @Override
    public Boolean submit(UpdateGBFormInfoRequest request) {
        FormDO formDO = formService.getById(request.getId());
        if (formDO == null) {
            throw new BusinessException(FleeFormErrorCode.FORM_NOT_FIND);
        }
        if (request.getOriginalStatus() != null && FormStatusEnum.getByCode(formDO.getStatus()) != request.getOriginalStatus()) {
            throw new BusinessException(FleeFormErrorCode.FORM_STATUS_CHANGE);
        }
        SubmitFormRequest submitFormRequest = new SubmitFormRequest();
        submitFormRequest.setId(request.getId()).setFlowId(request.getFlowId()).setFlowTplId(request.getFlowTplId()).setFlowTplName(request.getFlowName()).setFlowVersion(request.getFlowVersion());
        return formService.submit(submitFormRequest);
    }

    @Override
    public Boolean reSubmit(UpdateGBFormInfoRequest request) {
        FormDO formDO = formService.getById(request.getId());
        if (formDO == null) {
            throw new BusinessException(FleeFormErrorCode.FORM_NOT_FIND);
        }
        if (request.getOriginalStatus() != null && FormStatusEnum.getByCode(formDO.getStatus()) != request.getOriginalStatus()) {
            throw new BusinessException(FleeFormErrorCode.FORM_STATUS_CHANGE);
        }
        ResubmitFormRequest resubmitFormRequest = new ResubmitFormRequest();
        resubmitFormRequest.setId(request.getId());
        return formService.resubmit(resubmitFormRequest);
    }

    @Override
    public Boolean reject(UpdateGBFormInfoRequest request) {
        FormDO formDO = formService.getById(request.getId());
        if (formDO == null) {
            throw new BusinessException(FleeFormErrorCode.FORM_NOT_FIND);
        }
        if (request.getOriginalStatus() != null && FormStatusEnum.getByCode(formDO.getStatus()) != request.getOriginalStatus()) {
            throw new BusinessException(FleeFormErrorCode.FORM_STATUS_CHANGE);
        }
        RejectFormRequest rejectFormRequest = new RejectFormRequest();
        rejectFormRequest.setId(request.getId());
        return formService.reject(rejectFormRequest);
    }
}