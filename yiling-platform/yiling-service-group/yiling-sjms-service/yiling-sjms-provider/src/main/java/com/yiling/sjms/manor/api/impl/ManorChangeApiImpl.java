package com.yiling.sjms.manor.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.manor.api.ManorChangeApi;
import com.yiling.sjms.manor.bo.ManorChangeBO;
import com.yiling.sjms.manor.dto.HospitalManorChangeFormDTO;
import com.yiling.sjms.manor.dto.request.DeleteManorChangeFormRequest;
import com.yiling.sjms.manor.dto.request.ManorChangeFormRequest;
import com.yiling.sjms.manor.dto.request.QueryChangePageRequest;
import com.yiling.sjms.manor.dto.request.UpdateArchiveRequest;
import com.yiling.sjms.manor.service.HospitalManorChangeFormService;
import com.yiling.sjms.workflow.dto.request.SubmitFormBaseRequest;

/**
 * 辖区变更
 * @author: gxl
 * @date: 2023/5/12
 */
@DubboService
public class ManorChangeApiImpl implements ManorChangeApi {
    @Autowired
    private HospitalManorChangeFormService  hospitalManorChangeFormService;

    @Override
    public Long save(ManorChangeFormRequest request) {
        return hospitalManorChangeFormService.save(request);
    }

    @Override
    public Page<HospitalManorChangeFormDTO> listPage(QueryChangePageRequest request) {
        return hospitalManorChangeFormService.listPage(request);
    }

    @Override
    public void deleteById(DeleteManorChangeFormRequest request) {
        hospitalManorChangeFormService.deleteById(request);
    }

    @Override
    public Boolean submit(SubmitFormBaseRequest request) {
        return hospitalManorChangeFormService.submit(request);
    }

    @Override
    public Boolean updateArchiveStatusById(UpdateArchiveRequest request) {
        return hospitalManorChangeFormService.updateArchiveStatusById(request);
    }

    @Override
    public ManorChangeBO queryByFormId(Long formId) {
        return hospitalManorChangeFormService.queryByFormId(formId);
    }
}