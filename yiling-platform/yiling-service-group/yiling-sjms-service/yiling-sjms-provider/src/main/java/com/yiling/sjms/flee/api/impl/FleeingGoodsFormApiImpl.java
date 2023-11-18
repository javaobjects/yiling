package com.yiling.sjms.flee.api.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.flee.api.FleeingGoodsFormApi;
import com.yiling.sjms.flee.bo.FleeingFormBO;
import com.yiling.sjms.flee.dto.FleeingGoodsFormDTO;
import com.yiling.sjms.flee.dto.FleeingGoodsFormExtDTO;
import com.yiling.sjms.flee.dto.request.CreateFleeFlowRequest;
import com.yiling.sjms.flee.dto.request.QueryFleeingFormPageRequest;
import com.yiling.sjms.flee.dto.request.QueryFleeingGoodsFormListRequest;
import com.yiling.sjms.flee.dto.request.RemoveFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.SaveBatchFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.SaveFleeingFormDraftRequest;
import com.yiling.sjms.flee.dto.request.SaveFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.SubmitFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.UpdateFleeingGoodsFormRequest;
import com.yiling.sjms.flee.service.FleeingGoodsFormExtService;
import com.yiling.sjms.flee.service.FleeingGoodsFormService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 窜货申报表单
 *
 * @author: yong.zhang
 * @date: 2023/3/10 0010
 */
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FleeingGoodsFormApiImpl implements FleeingGoodsFormApi {

    private final FleeingGoodsFormService fleeingGoodsFormService;

    private final FleeingGoodsFormExtService fleeingGoodsFormExtService;

    @Override
    public Page<FleeingFormBO> pageForm(QueryFleeingFormPageRequest request) {
        return fleeingGoodsFormService.pageForm(request);
    }

    @Override
    public List<FleeingGoodsFormDTO> pageList(QueryFleeingGoodsFormListRequest request) {
        return PojoUtils.map(fleeingGoodsFormService.pageList(request), FleeingGoodsFormDTO.class);
    }

    @Override
    public FleeingGoodsFormDTO getFirst(QueryFleeingGoodsFormListRequest request) {
        return PojoUtils.map(fleeingGoodsFormService.getFirst(request), FleeingGoodsFormDTO.class);
    }

    @Override
    public FleeingGoodsFormDTO getByFileName(String fileName) {
        return PojoUtils.map(fleeingGoodsFormService.getByFileName(fileName), FleeingGoodsFormDTO.class);
    }

    @Override
    public FleeingGoodsFormDTO getByTaskId(Long taskId) {
        return PojoUtils.map(fleeingGoodsFormService.getByTaskId(taskId), FleeingGoodsFormDTO.class);
    }

    @Override
    public Long saveBatchUpload(SaveBatchFleeingGoodsFormRequest request) {
        return fleeingGoodsFormService.saveBatchUpload(request);
    }

    @Override
    public Long saveUpload(SaveFleeingGoodsFormRequest request) {
        return fleeingGoodsFormService.saveUpload(request);
    }

    @Override
    public Long saveUploadRecord(SaveFleeingGoodsFormRequest request) {
        return fleeingGoodsFormService.saveUploadRecord(request);
    }

    @Override
    public boolean updateUploadRecord(UpdateFleeingGoodsFormRequest request) {
        return fleeingGoodsFormService.updateUploadRecord(request);
    }

    @Override
    public Long saveDraft(SaveFleeingFormDraftRequest request) {
        return fleeingGoodsFormService.saveDraft(request);
    }

    @Override
    public boolean submit(SubmitFleeingGoodsFormRequest request) {
        return fleeingGoodsFormService.submit(request);
    }

    @Override
    public boolean removeById(RemoveFleeingGoodsFormRequest request) {
        return fleeingGoodsFormService.removeById(request);
    }

    @Override
    public FleeingGoodsFormExtDTO queryExtByFormId(Long formId) {
        return PojoUtils.map(fleeingGoodsFormExtService.queryExtByFormId(formId), FleeingGoodsFormExtDTO.class);
    }

    @Override
    public String checkUploadFileName(String fileName) {
        return fleeingGoodsFormService.checkUploadFileName(fileName);
    }

    @Override
    public boolean createFleeFlowForm(CreateFleeFlowRequest request) {
        return fleeingGoodsFormService.createFleeFlowForm(request);
    }

    @Override
    public FleeingGoodsFormDTO getById(Long id) {
        return PojoUtils.map(fleeingGoodsFormService.getById(id), FleeingGoodsFormDTO.class);
    }
}
