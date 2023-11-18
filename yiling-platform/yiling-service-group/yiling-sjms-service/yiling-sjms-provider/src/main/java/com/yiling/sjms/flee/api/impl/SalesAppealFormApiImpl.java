package com.yiling.sjms.flee.api.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.flee.api.SaleaAppealFormApi;
import com.yiling.sjms.flee.bo.SalesAppealFormBO;
import com.yiling.sjms.flee.dto.FleeingGoodsFormExtDTO;
import com.yiling.sjms.flee.dto.SalesAppealExtFormDTO;
import com.yiling.sjms.flee.dto.SalesAppealFormDTO;
import com.yiling.sjms.flee.dto.request.CreateSalesAppealFlowRequest;
import com.yiling.sjms.flee.dto.request.QuerySalesAppealPageRequest;
import com.yiling.sjms.flee.dto.request.RemoveFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.RemoveSelectAppealFlowFormRequest;
import com.yiling.sjms.flee.dto.request.SaveSalesAppealFormDetailRequest;
import com.yiling.sjms.flee.dto.request.SaveSalesAppealFormRequest;
import com.yiling.sjms.flee.dto.request.SaveSalesAppealSubmitFormRequest;
import com.yiling.sjms.flee.dto.request.SubmitFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.UpdateSalesAppealFormRequest;
import com.yiling.sjms.flee.service.FleeingGoodsFormExtService;
import com.yiling.sjms.flee.service.FleeingGoodsFormService;
import com.yiling.sjms.flee.service.SalesAppealFormService;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 窜货申报表单
 *
 * @author: shixing.sun
 * @date: 2023/3/10 0010
 */
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SalesAppealFormApiImpl implements SaleaAppealFormApi {

    private final FleeingGoodsFormService fleeingGoodsFormService;

    private final FleeingGoodsFormExtService fleeingGoodsFormExtService;

    private final SalesAppealFormService salesAppealFormService;

    @Override
    public Page<SalesAppealFormBO> pageForm(QuerySalesAppealPageRequest request) {
        return salesAppealFormService.pageForm(request);
    }

    @Override
    public List<SalesAppealFormDTO> pageList(Long formId, Integer type) {
        return PojoUtils.map(salesAppealFormService.ListByFormId(formId, type), SalesAppealFormDTO.class);
    }

    @Override
    public Long saveUpload(SaveSalesAppealFormRequest request) {
        return salesAppealFormService.saveUpload(request);
    }

    @Override
    public boolean removeById(RemoveFleeingGoodsFormRequest request) {
        return salesAppealFormService.removeById(request);
    }

    @Override
    public FleeingGoodsFormExtDTO queryExtByFormId(Long formId) {
        return PojoUtils.map(fleeingGoodsFormExtService.queryExtByFormId(formId), FleeingGoodsFormExtDTO.class);
    }

    @Override
    public Long saveDraft(SaveSalesAppealSubmitFormRequest request) {
        return salesAppealFormService.saveDraft(request);
    }

    @Override
    public boolean submit(SubmitFleeingGoodsFormRequest request) {
        return salesAppealFormService.submit(request);
    }

    @Override
    public String checkFlowFileName(String fileName) {
        return salesAppealFormService.checkFlowFileName(fileName);
    }

    @Override
    public Long saveSalesConfirm(SaveSalesAppealFormDetailRequest request) {
        return salesAppealFormService.saveSalesConfirm(request);
    }

    @Override
    public String createFleeFlowForm(CreateSalesAppealFlowRequest flowRequest) {
        return salesAppealFormService.createFleeFlowForm(flowRequest);
    }

    @Override
    public Integer getDateType(String fileName) {
        return salesAppealFormService.getDataType(fileName);
    }

    @Override
    public SalesAppealFormDTO getByFileName(String fileName) {
        return PojoUtils.map(salesAppealFormService.getByFileName(fileName), SalesAppealFormDTO.class);
    }

    @Override
    public SalesAppealFormDTO getByTaskId(Long taskId) {
        return PojoUtils.map(salesAppealFormService.getByTaskId(taskId), SalesAppealFormDTO.class);
    }


    @Override
    public Boolean updateSalesConfirm(UpdateSalesAppealFormRequest appealFormRequest) {
        return salesAppealFormService.updateSalesConfirm(appealFormRequest);
    }

    @Override
    public List<SalesAppealFormBO> queryToDoList(QuerySalesAppealPageRequest request) {
        return salesAppealFormService.queryToDoList(request);
    }

    @Override
    public SalesAppealExtFormDTO queryAppendix(Long formId) {
        return salesAppealFormService.queryAppendix(formId);
    }

    @Override
    public SalesAppealFormDTO getById(Long id) {
        return PojoUtils.map(salesAppealFormService.getById(id), SalesAppealFormDTO.class);
    }

    @Override
    public Long newSaveDraft(SaveSalesAppealSubmitFormRequest request) {
        return salesAppealFormService.newSaveDraft(request);
    }

    @Override
    public boolean removeByIds(RemoveSelectAppealFlowFormRequest request) {
        return salesAppealFormService.deleteByIds(request);
    }

    @Override
    public String compatibleCreateFleeFlowForm(CreateSalesAppealFlowRequest request) {
        return salesAppealFormService.compatibleCreateFleeFlowForm(request);
    }

    @Override
    public boolean valid(Long formId) {
        return salesAppealFormService.valid(formId);
    }

    @Override
    public boolean removeAllSelectByFormId(Long formId) {
        return salesAppealFormService.removeAllSelectByFormId(formId);
    }

    @Override
    public boolean removeAllSelectFlowByFormId(Long formId) {
        return salesAppealFormService.removeAllSelectFlowByFormId(formId);
    }

    @Override
    public boolean washSaleAppealFlowData() {
        return salesAppealFormService.washSaleAppealFlowData();
    }

}
