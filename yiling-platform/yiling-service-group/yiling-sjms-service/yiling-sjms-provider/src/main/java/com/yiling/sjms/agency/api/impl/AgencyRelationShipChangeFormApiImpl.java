package com.yiling.sjms.agency.api.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.api.AgencyRelationShipChangeFormApi;
import com.yiling.sjms.agency.dto.AgencyRelationShipChangeFormDTO;
import com.yiling.sjms.agency.dto.AgencyUnLockFormDTO;
import com.yiling.sjms.agency.dto.request.QueryAgencyFormPageRequest;
import com.yiling.sjms.agency.dto.request.RemoveAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyRelationChangeFormRequest;
import com.yiling.sjms.agency.dto.request.UpdateAgencyLockArchiveRequest;
import com.yiling.sjms.agency.entity.AgencyChangeRelationShipDO;
import com.yiling.sjms.agency.entity.AgencyRelationShipChangeFormDO;
import com.yiling.sjms.agency.entity.AgencyUnlockRelationShipDO;
import com.yiling.sjms.agency.service.AgencyChangeRelationShipService;
import com.yiling.sjms.agency.service.AgencyRelationShipChangeFormService;
import com.yiling.sjms.workflow.dto.request.SubmitFormBaseRequest;

import cn.hutool.core.util.ObjectUtil;


/**
 * 三者关系变更表单api
 *
 * @author: shixing.sun
 * @date: 2023/2/22
 */
@DubboService
public class AgencyRelationShipChangeFormApiImpl implements AgencyRelationShipChangeFormApi {

    @Autowired
    private AgencyRelationShipChangeFormService relationShipChangeFormService;

    @Autowired
    private AgencyChangeRelationShipService agencyChangeRelationShipService;


    @Override
    public Long save(SaveAgencyRelationChangeFormRequest request) {
        return relationShipChangeFormService.save(request);
    }

    @Override
    public boolean deleteById(RemoveAgencyFormRequest request) {
        return relationShipChangeFormService.deleteById(request);
    }

    @Override
    public Page<AgencyRelationShipChangeFormDTO> pageList(QueryAgencyFormPageRequest request) {
        return relationShipChangeFormService.pageList(request);
    }

    @Override
    public AgencyRelationShipChangeFormDTO queryById(Long id) {
        AgencyRelationShipChangeFormDO relationShipChangeFormDO = relationShipChangeFormService.getById(id);
        return PojoUtils.map(relationShipChangeFormDO,AgencyRelationShipChangeFormDTO.class);
    }

    @Override
    public Boolean submit(SubmitFormBaseRequest request) {
        return relationShipChangeFormService.submit(request);
    }

    @Override
    public List<Long> queryRelationListByAgencyFormId(Long id) {
        LambdaQueryWrapper<AgencyChangeRelationShipDO> unlockRelation = new LambdaQueryWrapper<>();
        unlockRelation.eq(AgencyChangeRelationShipDO::getChangeRelationShipId, id);
        List<AgencyChangeRelationShipDO> relationShipDOS = agencyChangeRelationShipService.list(unlockRelation);
        if(CollectionUtils.isNotEmpty(relationShipDOS)){
            List<Long> ids = relationShipDOS.stream().map(AgencyChangeRelationShipDO::getSrcRelationShipIp).collect(Collectors.toList());
            return ids;
        }
        return new ArrayList<>();
    }

    @Override
    public Boolean updateArchiveStatusById(UpdateAgencyLockArchiveRequest request) {
        return relationShipChangeFormService.updateArchiveStatusById(request);
    }

    @Override
    public List<String> getNameByFormId(Long id) {
        return relationShipChangeFormService.getNameByFormId(id);
    }
}