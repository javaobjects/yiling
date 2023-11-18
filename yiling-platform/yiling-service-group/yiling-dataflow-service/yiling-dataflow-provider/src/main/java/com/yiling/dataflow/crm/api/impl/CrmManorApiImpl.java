package com.yiling.dataflow.crm.api.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.hutool.core.collection.ListUtil;
import com.yiling.dataflow.crm.dto.request.UpdateManorNumRequest;
import com.yiling.dataflow.crm.service.CrmManorRepresentativeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.dataflow.crm.api.CrmManorApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationManorDTO;
import com.yiling.dataflow.crm.dto.CrmManorDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorParamRequest;
import com.yiling.dataflow.crm.dto.request.RemoveManorRequest;
import com.yiling.dataflow.crm.dto.request.SaveCrmManorInfoRequest;
import com.yiling.dataflow.crm.entity.CrmManorDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationManorService;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmManorService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;

@DubboService
@Slf4j
public class CrmManorApiImpl implements CrmManorApi {
    @Resource
    private CrmManorService crmManorService;
    @Resource
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;
    @Resource
    private CrmEnterpriseRelationManorService crmEnterpriseRelationManorService;
    @Resource
    private CrmManorRepresentativeService crmManorRepresentativeService;

    @Override
    public Page<CrmManorDTO> pageList(QueryCrmManorPageRequest request) {
        QueryWrapper<CrmManorDO> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getManorNo())) {
            queryWrapper.lambda().eq(CrmManorDO::getManorNo, request.getManorNo());
        }
        if (StringUtils.isNotBlank(request.getName())) {
            queryWrapper.lambda().like(CrmManorDO::getName, request.getName());
        }
        if (ObjectUtil.isNotEmpty(request.getBeginTime())) {
            queryWrapper.lambda().ge(CrmManorDO::getUpdateTime, DateUtil.parse(DateUtil.format(request.getBeginTime(), "yyyy-MM-dd 00:00:00")));
        }
        if (ObjectUtil.isNotEmpty(request.getEndTime())) {
            queryWrapper.lambda().le(CrmManorDO::getUpdateTime, DateUtil.parse(DateUtil.format(request.getEndTime(), "yyyy-MM-dd 23:59:59")));
        }
        queryWrapper.lambda().orderByDesc(CrmManorDO::getCreateTime);
        return PojoUtils.map(crmManorService.page(request.getPage(), queryWrapper), CrmManorDTO.class);
    }

    @Override
    public CrmManorDTO getManorById(Long id) {
        return PojoUtils.map(crmManorService.getById(id), CrmManorDTO.class);
    }

    @Override
    public Long saveOrUpdate(SaveCrmManorInfoRequest request) {
        CrmManorDO manorDO = new CrmManorDO();
        PojoUtils.map(request, manorDO);
        //
        if (Objects.isNull(request.getId()) || request.getId().longValue() == 0L) {
            manorDO.setCreateTime(request.getOpTime());
            manorDO.setCreateUser(request.getOpUserId());
        }
        manorDO.setUpdateTime(request.getOpTime());
        manorDO.setUpdateUser(request.getOpUserId());
        boolean flag = crmManorService.saveOrUpdate(manorDO);
        return manorDO.getId();
    }

    @Override
    public List<CrmManorDTO> getByName(String name) {
        if (StringUtils.isBlank(name)) {
            return Lists.newArrayList();
        }
        QueryWrapper<CrmManorDO> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.lambda().eq(CrmManorDO::getName, name);
        }
        return PojoUtils.map(crmManorService.list(queryWrapper), CrmManorDTO.class);
    }

    @Override
    public List<CrmManorDTO> listByParam(QueryCrmManorParamRequest crmManorParamRequest) {
        QueryWrapper<CrmManorDO> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(crmManorParamRequest.getName())) {
            queryWrapper.lambda().eq(CrmManorDO::getName, crmManorParamRequest.getName());
        }
        if (StringUtils.isNotBlank(crmManorParamRequest.getManorNo())) {
            queryWrapper.lambda().eq(CrmManorDO::getManorNo, crmManorParamRequest.getManorNo());
        }
        if (Objects.nonNull(crmManorParamRequest.getId())) {
            queryWrapper.lambda().eq(CrmManorDO::getId, crmManorParamRequest.getId());
        }
        if (CollUtil.isNotEmpty(crmManorParamRequest.getIdList())) {
            queryWrapper.lambda().in(CrmManorDO::getId, crmManorParamRequest.getIdList());
        }
        queryWrapper.lambda().last(" limit 50");
        return PojoUtils.map(crmManorService.list(queryWrapper), CrmManorDTO.class);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int removeById(RemoveManorRequest request) {
        //先删除关联的机构和对应的三者关系
        List<CrmEnterpriseRelationManorDTO> relationManorDOList = crmEnterpriseRelationManorService.getByManorId(request.getId());
        if (CollUtil.isNotEmpty(relationManorDOList)) {

            List<Long> cEnIdList = relationManorDOList.stream().map(CrmEnterpriseRelationManorDTO::getCrmEnterpriseId).collect(Collectors.toList());
            List<Long> idList = relationManorDOList.stream().map(CrmEnterpriseRelationManorDTO::getId).collect(Collectors.toList());
            log.info("cEnIdList:{},idList{}",cEnIdList,idList);
            crmEnterpriseRelationShipService.batchDeleteWithCrmEnterIds(cEnIdList, request.getOpUserId(), "辖区ID删除" + request.getOpUserId(),request.getId());
            crmEnterpriseRelationManorService.batchDeleteWithIds(idList, request.getOpUserId(), "辖区ID删除" + request.getOpUserId());
        }
        //删除辖区代表
        crmManorRepresentativeService.batchDeleteWithManorId(request.getId(),request.getOpUserId(),"辖区ID删除"+ request.getOpUserId());
        //删除辖区数据
        CrmManorDO manorDO = new CrmManorDO();
        manorDO.setId(request.getId());
        manorDO.setUpdateTime(request.getOpTime());
        manorDO.setUpdateUser(request.getOpUserId());
        return crmManorService.deleteByIdWithFill(manorDO);
    }

    @Override
    public void updateNum(UpdateManorNumRequest request) {
        //辖区机构关系
        List<CrmEnterpriseRelationManorDTO> relationManorDOList = crmEnterpriseRelationManorService.getByManorId(request.getCrmManorId());
        List<Long> cEnIdList = Optional.ofNullable(relationManorDOList.stream().map(CrmEnterpriseRelationManorDTO::getCrmEnterpriseId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList())).orElse(ListUtil.empty());
        List<Long> categoryList = Optional.ofNullable(relationManorDOList.stream().map(CrmEnterpriseRelationManorDTO::getCategoryId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList())).orElse(ListUtil.empty());
        CrmManorDO crmManorDO=new CrmManorDO();
        crmManorDO.setId(request.getCrmManorId());
        crmManorDO.setAgencyNum(cEnIdList.size());
        crmManorDO.setCategoryNum(categoryList.size());
        crmManorDO.setOpUserId(request.getOpUserId());
        crmManorDO.setUpdateTime(request.getOpTime());
        log.info("辖区ID:{},更新机构数量{}分类数量{}",request.getCrmManorId(),cEnIdList.size(),categoryList.size());
        crmManorService.updateById(crmManorDO);
    }

    public static void main(String[] args) {
        List list=ListUtil.empty();
        System.out.println(list.size());
    }
}
