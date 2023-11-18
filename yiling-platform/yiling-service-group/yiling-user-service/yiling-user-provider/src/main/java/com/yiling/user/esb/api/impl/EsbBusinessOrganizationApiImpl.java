package com.yiling.user.esb.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.user.esb.api.EsbBusinessOrganizationApi;
import com.yiling.user.esb.bo.EsbBusinessOrgTreeBO;
import com.yiling.user.esb.bo.SimpleEsbBzOrgBO;
import com.yiling.user.esb.dto.request.DeleteBusinessOrganizationRequest;
import com.yiling.user.esb.dto.request.SaveBusinessOrganizationRequest;
import com.yiling.user.esb.dto.request.UpdateTargetStatusRequest;
import com.yiling.user.esb.enums.EsbBusinessOrganizationTagTypeEnum;
import com.yiling.user.esb.service.EsbBusinessOrganizationService;

/**
 * ESB业务架构 API 实现
 *
 * @author: lun.yu
 * @date: 2023-04-12
 */
@DubboService
public class EsbBusinessOrganizationApiImpl implements EsbBusinessOrganizationApi {

    @Autowired
    EsbBusinessOrganizationService esbBusinessOrganizationService;

    @Override
    public boolean saveBusinessOrg(SaveBusinessOrganizationRequest request) {
        return esbBusinessOrganizationService.saveBusinessOrg(request);
    }

    @Override
    public boolean setTargetStatus(UpdateTargetStatusRequest request) {
        return esbBusinessOrganizationService.setTargetStatus(request);
    }

    @Override
    public List<EsbBusinessOrgTreeBO> queryBzOrgTree() {
        return esbBusinessOrganizationService.queryBzOrgTree();
    }

    @Override
    public List<SimpleEsbBzOrgBO> getBzOrgListByTagType(EsbBusinessOrganizationTagTypeEnum tagTypeEnum) {
        return esbBusinessOrganizationService.getBzOrgListByTagType(tagTypeEnum);
    }

    @Override
    public List<SimpleEsbBzOrgBO> getBzOrgListByOrgId(Long orgId, EsbBusinessOrganizationTagTypeEnum tagTypeEnum) {
        return esbBusinessOrganizationService.getBzOrgListByOrgId(orgId, tagTypeEnum);
    }

    @Override
    public boolean deleteTag(DeleteBusinessOrganizationRequest request) {
        return esbBusinessOrganizationService.deleteTag(request);
    }

}
