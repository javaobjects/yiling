package com.yiling.sjms.agency.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.api.AgencyFormApi;
import com.yiling.sjms.agency.dto.AgencyFormDTO;
import com.yiling.sjms.agency.dto.request.QueryAgencyFormPageRequest;
import com.yiling.sjms.agency.dto.request.QueryFirstAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.RemoveAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.SubmitAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.UpdateAgencyFormArchiveRequest;
import com.yiling.sjms.agency.service.AgencyFormService;

import lombok.RequiredArgsConstructor;

/**
 * 机构新增修改表单Api
 *
 * @author: yong.zhang
 * @date: 2023/2/22 0022
 */
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AgencyFormApiImpl implements AgencyFormApi {

    private final AgencyFormService agencyFormService;

    @Override
    public Long saveAgencyForm(SaveAgencyFormRequest request) {
        return agencyFormService.saveAgencyForm(request);
    }

    @Override
    public Page<AgencyFormDTO> pageList(QueryAgencyFormPageRequest request) {
        return PojoUtils.map(agencyFormService.pageList(request), AgencyFormDTO.class);
    }

    @Override
    public AgencyFormDTO queryById(Long id) {
        return PojoUtils.map(agencyFormService.getById(id), AgencyFormDTO.class);
    }

    @Override
    public List<AgencyFormDTO> listByFormIdAndCrmEnterpriseId(Long formId, Long crmEnterpriseId) {
        return PojoUtils.map(agencyFormService.listByFormIdAndCrmEnterpriseId(formId, crmEnterpriseId), AgencyFormDTO.class);
    }

    @Override
    public AgencyFormDTO getFirstInfo(QueryFirstAgencyFormRequest request) {
        return PojoUtils.map(agencyFormService.getFirstInfo(request), AgencyFormDTO.class);
    }

    @Override
    public boolean removeById(RemoveAgencyFormRequest request) {
        return agencyFormService.removeById(request);
    }

    @Override
    public boolean submit(SubmitAgencyFormRequest request) {
        return agencyFormService.submit(request);
    }

    @Override
    public void updateArchiveStatusById(UpdateAgencyFormArchiveRequest request) {
        agencyFormService.updateArchiveStatusById(request);
    }
}
