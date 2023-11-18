package com.yiling.sjms.crm.service.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.sjms.crm.service.HospitalDrugstoreRelationFormService;
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
 * @author fucheng.bai
 * @date 2023/6/8
 */
@Slf4j
@Service("hospitalDrugstoreRelationFormServiceStrategy")
public class HospitalDrugstoreRelationFormServiceStrategy extends AbstractFormStatusHandler {

    @Autowired
    private FormService formService;

    @Autowired
    private HospitalDrugstoreRelationFormService hospitalDrugstoreRelationFormService;

    @Override
    public Integer getCurrentType() {
        return FormTypeEnum.HOSPITAL_PHARMACY_BIND.getCode();
    }

    @Override
    public Boolean approve(UpdateGBFormInfoRequest request) {
        FormDO formDO = formService.getById(request.getId());
        if (formDO == null) {
            throw new BusinessException(GBErrorCode.GB_FORM_INFO_NOT_EXISTS);
        }
        if (request.getOriginalStatus() != null && FormStatusEnum.getByCode(formDO.getStatus()) != request.getOriginalStatus()) {
            throw new BusinessException(GBErrorCode.GB_FORM_STATUS_CHANGE);
        }
        boolean isSuccess = hospitalDrugstoreRelationFormService.approveToAdd(request.getId());
        if (isSuccess) {
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
        submitFormRequest.setId(request.getId()).setFlowId(request.getFlowId()).setFlowTplId(request.getFlowTplId()).setFlowTplName(request.getFlowName()).setFlowVersion(request.getFlowVersion());
        return formService.submit(submitFormRequest);
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
}
