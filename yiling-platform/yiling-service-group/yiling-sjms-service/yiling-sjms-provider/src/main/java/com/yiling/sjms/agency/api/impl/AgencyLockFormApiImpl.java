package com.yiling.sjms.agency.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.agency.api.AgencyLockFormApi;
import com.yiling.sjms.agency.dto.AgencyLockFormDTO;
import com.yiling.sjms.agency.dto.request.DeleteAgencyLockFormRequest;
import com.yiling.sjms.agency.dto.request.QueryAgencyLockDetailPageRequest;
import com.yiling.sjms.agency.dto.request.QueryAgencyLockFormPageRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyLockFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyLockRequest;
import com.yiling.sjms.agency.dto.request.UpdateAgencyLockArchiveRequest;
import com.yiling.sjms.agency.service.AgencyLockFormService;
import com.yiling.sjms.workflow.dto.request.SubmitFormBaseRequest;

/**
 * 机构拓展信息修改表单api
 * @author: gxl
 * @date: 2023/2/22
 */
@DubboService
public class AgencyLockFormApiImpl implements AgencyLockFormApi {

    @Autowired
    private AgencyLockFormService agencyLockFormService;

    @Override
    public Long save(SaveAgencyLockFormRequest request) {
       return agencyLockFormService.save(request);
    }

    @Override
    public Page<AgencyLockFormDTO> queryPage(QueryAgencyLockFormPageRequest request) {
        return agencyLockFormService.queryPage(request);
    }

    @Override
    public AgencyLockFormDTO getAgencyLockForm(Long id) {
        return agencyLockFormService.getAgencyLockForm(id);
    }

    @Override
    public void deleteById(DeleteAgencyLockFormRequest request) {
        agencyLockFormService.deleteById(request);
    }

    @Override
    public Long saveAgencyLock(SaveAgencyLockRequest request) {
        return agencyLockFormService.saveAgencyLock(request);
    }

    @Override
    public Page<AgencyLockFormDTO> queryAgencyLockDetailPage(QueryAgencyLockDetailPageRequest request) {
        return agencyLockFormService.queryAgencyLockDetailPage(request);
    }

    @Override
    public Boolean submit(SubmitFormBaseRequest request) {
        return agencyLockFormService.submit(request);
    }

    @Override
    public void updateArchiveStatusById(UpdateAgencyLockArchiveRequest request) {
        agencyLockFormService.updateArchiveStatusById(request);
    }

    @Override
    public Boolean submitAgencyLockForm(SubmitFormBaseRequest request) {
        return agencyLockFormService.submitAgencyLockForm(request);
    }

}