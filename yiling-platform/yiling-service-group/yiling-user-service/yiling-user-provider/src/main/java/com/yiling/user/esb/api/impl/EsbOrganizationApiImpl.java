package com.yiling.user.esb.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.bo.EsbOrgInfoTreeBO;
import com.yiling.user.esb.bo.SimpleEsbOrgInfoBO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.esb.dto.request.SaveOrUpdateEsbOrganizationRequest;
import com.yiling.user.esb.service.EsbOrganizationService;

/**
 * ESB组织架构 API 实现
 *
 * @author: xuan.zhou
 * @date: 2022/11/25
 */
@DubboService
public class EsbOrganizationApiImpl implements EsbOrganizationApi {

    @Autowired
    EsbOrganizationService esbOrganizationService;

    @Override
    public Boolean saveOrUpdate(SaveOrUpdateEsbOrganizationRequest request) {
        return esbOrganizationService.saveOrUpdate(request);
    }

    @Override
    public EsbOrganizationDTO getByOrgId(Long orgId) {
        return PojoUtils.map(esbOrganizationService.getByOrgId(orgId), EsbOrganizationDTO.class);
    }

    @Override
    public List<EsbOrganizationDTO> listByOrgIds(List<Long> orgIds) {
        return PojoUtils.map(esbOrganizationService.listByOrgIds(orgIds), EsbOrganizationDTO.class);
    }

    @Override
    public EsbOrganizationDTO getByPidAndName(Long orgPid, String orgName) {
        return PojoUtils.map(esbOrganizationService.getByPidAndName(orgPid, orgName), EsbOrganizationDTO.class);
    }

    @Override
    public EsbOrganizationDTO getByPid(Long orgPid) {
        return PojoUtils.map(esbOrganizationService.getByPid(orgPid), EsbOrganizationDTO.class);
    }
    @Override
    public EsbOrganizationDTO getByPidListAndName(List<Long> orgPidList, String orgName) {
        return PojoUtils.map(esbOrganizationService.getByPidListAndName(orgPidList, orgName), EsbOrganizationDTO.class);

    }

    @Override
    public List<EsbOrganizationDTO> listByPid(Long orgPid) {
        return PojoUtils.map(esbOrganizationService.listByPid(orgPid), EsbOrganizationDTO.class);
    }

    @Override
    public List<SimpleEsbOrgInfoBO> listAll(boolean setParentOrgInfoFlag) {
        return esbOrganizationService.listAll(setParentOrgInfoFlag);
    }

    @Override
    public List<EsbOrganizationDTO> findByOrgName(String orgName, String tableSuffix) {
        return PojoUtils.map(esbOrganizationService.findByOrgName(orgName,tableSuffix),EsbOrganizationDTO.class);
    }

    @Override
    public List<EsbOrgInfoTreeBO> listTree() {
        return esbOrganizationService.listTree();
    }
}
