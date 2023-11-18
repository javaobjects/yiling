package com.yiling.sjms.agency.service.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.sjms.agency.dto.request.ApproveAgencyLockFormRequest;
import com.yiling.sjms.agency.service.AgencyLockFormService;
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
import com.yiling.sjms.gb.enums.GBErrorCode;

import lombok.extern.slf4j.Slf4j;

/**
 * 机构信息扩展表单
 * @author: gxl
 * @date: 2023/2/27
 */
@Slf4j
@Service("agencyLockFormServiceStrategy")
public class AgencyLockFormServiceStrategy extends AbstractFormStatusHandler {
    @Autowired
    FormService formService;

    @Autowired
    AgencyLockFormService agencyLockFormService;

    @Override
    public Integer getCurrentType() {
        return FormTypeEnum.EXTEND_INFO_CHANGE.getCode();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean approve(UpdateGBFormInfoRequest request) {
        FormDO formDO = formService.getById(request.getId());
        if (formDO == null) {
            throw new BusinessException(GBErrorCode.GB_FORM_INFO_NOT_EXISTS);
        }
        if (request.getOriginalStatus() != null && FormStatusEnum.getByCode(formDO.getStatus()) != request.getOriginalStatus()) {
            throw new BusinessException(GBErrorCode.GB_FORM_STATUS_CHANGE);
        }
        ApproveAgencyLockFormRequest agencyLockFormRequest = new ApproveAgencyLockFormRequest();
        agencyLockFormRequest.setId(request.getId());
        Boolean change = agencyLockFormService.approveToChange(agencyLockFormRequest);
        if(change){
            ApproveFormRequest approveFormRequest = new ApproveFormRequest();
            approveFormRequest.setId(request.getId());
            formService.approve(approveFormRequest);
            return true;
        }
        return false;
    }

    @Override
    public Boolean submit(UpdateGBFormInfoRequest request) {
        FormDO formDO = formService.getById(request.getId());
        if (formDO == null) {
            throw new BusinessException(GBErrorCode.GB_FORM_INFO_NOT_EXISTS);
        }
        if (request.getOriginalStatus() != null && FormStatusEnum.getByCode(formDO.getStatus()) != request.getOriginalStatus()) {
            throw new BusinessException(GBErrorCode.GB_FORM_STATUS_CHANGE);
        }
        SubmitFormRequest submitFormRequest = new SubmitFormRequest();
        submitFormRequest.setId(request.getId()).setFlowId(request.getFlowId()).setFlowTplId(request.getFlowTplId())
        .setFlowTplName(request.getFlowName()).setFlowVersion(request.getFlowVersion());
        return formService.submit(submitFormRequest);
    }

    @Override
    public Boolean reject(UpdateGBFormInfoRequest request) {
        FormDO formDO = formService.getById(request.getId());
        if (formDO == null) {
            throw new BusinessException(GBErrorCode.GB_FORM_INFO_NOT_EXISTS);
        }
        if (request.getOriginalStatus() != null && FormStatusEnum.getByCode(formDO.getStatus()) != request.getOriginalStatus()) {
            throw new BusinessException(GBErrorCode.GB_FORM_STATUS_CHANGE);
        }
        RejectFormRequest rejectFormRequest = new RejectFormRequest();
        rejectFormRequest.setId(request.getId());
        return formService.reject(rejectFormRequest);
    }

    @Override
    public Boolean reSubmit(UpdateGBFormInfoRequest request) {
        FormDO formDO = formService.getById(request.getId());
        if (formDO == null) {
            throw new BusinessException(GBErrorCode.GB_FORM_INFO_NOT_EXISTS);
        }
        if (request.getOriginalStatus() != null && FormStatusEnum.getByCode(formDO.getStatus()) != request.getOriginalStatus()) {
            throw new BusinessException(GBErrorCode.GB_FORM_STATUS_CHANGE);
        }
        ResubmitFormRequest resubmitFormRequest = new ResubmitFormRequest();
        resubmitFormRequest.setId(request.getId());
        return formService.resubmit(resubmitFormRequest);
    }
}