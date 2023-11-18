package com.yiling.sjms.agency.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.api.AgencyLockFormApi;
import com.yiling.sjms.agency.api.AgencyUnLockFormApi;
import com.yiling.sjms.agency.dto.AgencyLockFormDTO;
import com.yiling.sjms.agency.dto.AgencyUnLockFormDTO;
import com.yiling.sjms.agency.dto.AgencyUnLockRelationShipFormDTO;
import com.yiling.sjms.agency.dto.request.DeleteAgencyLockFormRequest;
import com.yiling.sjms.agency.dto.request.EnterpriseUnLockApproveRequest;
import com.yiling.sjms.agency.dto.request.QueryAgencyFormPageRequest;
import com.yiling.sjms.agency.dto.request.QueryAgencyLockFormPageRequest;
import com.yiling.sjms.agency.dto.request.RemoveAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyLockFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyUnlockFormRequest;
import com.yiling.sjms.agency.dto.request.UpdateAgencyLockArchiveRequest;
import com.yiling.sjms.agency.entity.AgencyUnlockFormDO;
import com.yiling.sjms.agency.entity.AgencyUnlockRelationShipDO;
import com.yiling.sjms.agency.service.AgencyLockFormService;
import com.yiling.sjms.agency.service.AgencyUnlockFormService;
import com.yiling.sjms.agency.service.AgencyUnlockRelationShipService;
import com.yiling.sjms.workflow.dto.request.SubmitFormBaseRequest;

import cn.hutool.core.util.ObjectUtil;

/**
 * 机构拓展信息修改表单api
 *
 * @author: shixing.sun
 * @date: 2023/2/22
 */
@DubboService
public class AgencyUnLockFormApiImpl implements AgencyUnLockFormApi {

    @Autowired
    private AgencyUnlockFormService agencyUnlockFormService;

    @Autowired
    private AgencyUnlockRelationShipService unlockRelationShipService;

    @Override
    public Long save(SaveAgencyUnlockFormRequest request) {
        return agencyUnlockFormService.save(request);
    }

    @Override
    public boolean deleteById(RemoveAgencyFormRequest request) {
        return agencyUnlockFormService.deleteById(request);
    }

    @Override
    public Page<AgencyUnLockFormDTO> pageList(QueryAgencyFormPageRequest request) {
        return agencyUnlockFormService.pageList(request);
    }

    @Override
    public AgencyUnLockFormDTO queryById(Long id) {
        AgencyUnlockFormDO unlockFormDOunlockFormDO = agencyUnlockFormService.getById(id);
        return PojoUtils.map(unlockFormDOunlockFormDO,AgencyUnLockFormDTO.class);
    }

    @Override
    public Boolean submit(SubmitFormBaseRequest request) {
        return agencyUnlockFormService.submit(request);
    }

    @Override
    public Boolean approved(EnterpriseUnLockApproveRequest request) {
        return agencyUnlockFormService.approved(request);
    }

    @Override
    public List<Long> queryRelationListByAgencyFormId(Long id) {
        LambdaQueryWrapper<AgencyUnlockRelationShipDO> unlockRelation = new LambdaQueryWrapper<>();
        unlockRelation.eq(AgencyUnlockRelationShipDO::getAgencyFormId, id);
        List<AgencyUnlockRelationShipDO> relationShipDOS = unlockRelationShipService.list(unlockRelation);
        if(CollectionUtils.isNotEmpty(relationShipDOS)){
            List<Long> ids = relationShipDOS.stream().map(AgencyUnlockRelationShipDO::getSrcRelationShipIp).collect(Collectors.toList());
            return ids;
        }
        return new ArrayList<>();
    }

    @Override
    public List<AgencyUnLockRelationShipFormDTO>  queryListByAgencyFormId(Long id) {
        LambdaQueryWrapper<AgencyUnlockRelationShipDO> unlockRelation = new LambdaQueryWrapper<>();
        unlockRelation.eq(AgencyUnlockRelationShipDO::getAgencyFormId, id);
        List<AgencyUnlockRelationShipDO> relationShipDOS = unlockRelationShipService.list(unlockRelation);
        return PojoUtils.map(relationShipDOS,AgencyUnLockRelationShipFormDTO.class);
    }

    @Override
    public void updateArchiveStatusById(UpdateAgencyLockArchiveRequest request) {
        unlockRelationShipService.updateArchiveStatusById(request);
    }
}