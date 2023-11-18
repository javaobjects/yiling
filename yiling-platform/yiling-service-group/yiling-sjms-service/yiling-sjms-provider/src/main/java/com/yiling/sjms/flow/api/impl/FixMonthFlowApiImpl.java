package com.yiling.sjms.flow.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.sjms.flow.api.FixMonthFlowApi;
import com.yiling.sjms.flow.dto.MonthFlowExtFormDTO;
import com.yiling.sjms.flow.dto.MonthFlowFormDTO;
import com.yiling.sjms.flow.dto.request.DeleteFormRequest;
import com.yiling.sjms.flow.dto.request.QueryMonthFlowFormRequest;
import com.yiling.sjms.flow.dto.request.SaveMonthFlowFormRequest;
import com.yiling.sjms.flow.dto.request.SaveSubFormRequest;
import com.yiling.sjms.flow.dto.request.SubmitMonthFlowFormRequest;
import com.yiling.sjms.flow.service.MonthFlowExtFormService;
import com.yiling.sjms.flow.service.MonthFlowFormService;

/**
 * @author: gxl
 * @date: 2023/6/26
 */
@DubboService
public class FixMonthFlowApiImpl implements FixMonthFlowApi {
    @Autowired
    private MonthFlowFormService monthFlowFormService;

    @Autowired
    private MonthFlowExtFormService monthFlowExtFormService;
    @Override
    public Long save(SaveMonthFlowFormRequest request) {
        return monthFlowFormService.save(request);
    }

    @Override
    public void submit(SubmitMonthFlowFormRequest request) {
        monthFlowFormService.submit(request);
    }

    @Override
    public List<MonthFlowFormDTO> list(QueryMonthFlowFormRequest request) {
        return monthFlowFormService.list(request);
    }

    @Override
    public void deleteById(DeleteFormRequest request) {
        monthFlowFormService.deleteById(request);
    }

    @Override
    public MonthFlowFormDTO getByRecordId(Long recordId) {
        return monthFlowFormService.getByRecordId(recordId);
    }

    @Override
    public void updateFlowMonthRecord(SaveSubFormRequest request) {
        monthFlowFormService.updateFlowMonthRecord(request);
    }

    @Override
    public MonthFlowExtFormDTO queryAppendix(Long formId) {
        return monthFlowExtFormService.queryAppendix(formId);
    }

    @Override
    public Boolean getByFileName(String fileName) {
        return monthFlowFormService.getByFileName(fileName);
    }
}