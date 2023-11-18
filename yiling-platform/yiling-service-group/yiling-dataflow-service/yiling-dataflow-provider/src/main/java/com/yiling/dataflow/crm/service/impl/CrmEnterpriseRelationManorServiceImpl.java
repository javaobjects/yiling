package com.yiling.dataflow.crm.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.dataflow.crm.bo.CrmRelationManorBO;
import com.yiling.dataflow.crm.dao.CrmEnterpriseRelationManorMapper;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationManorDTO;
import com.yiling.dataflow.crm.dto.CrmManorRepresentativeDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseRelationManorPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorPageRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateManorRelationRequest;
import com.yiling.dataflow.crm.dto.request.UpdateManorNumRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationManorDO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationPinchRunnerDO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.dataflow.crm.entity.CrmManorDO;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationManorService;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationPinchRunnerService;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmManorRepresentativeService;
import com.yiling.dataflow.crm.service.CrmManorService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 辖区机构品类关系 服务实现类
 * </p>
 *
 * @author xueli.ji
 * @date 2023-05-09
 */
@Slf4j
@Service
public class CrmEnterpriseRelationManorServiceImpl extends BaseServiceImpl<CrmEnterpriseRelationManorMapper, CrmEnterpriseRelationManorDO> implements CrmEnterpriseRelationManorService {

    @Autowired
    private CrmManorService crmManorService;

    @Autowired
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;
    @Autowired
    private CrmManorRepresentativeService crmManorRepresentativeService;
    @Autowired
    private CrmEnterpriseRelationPinchRunnerService crmEnterpriseRelationPinchRunnerService;
    @Override
    public List<CrmEnterpriseRelationManorDTO> getByManorId(Long id) {
        if (Objects.isNull(id) || id == 0l) {
            return ListUtil.empty();
        }
        QueryWrapper<CrmEnterpriseRelationManorDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CrmEnterpriseRelationManorDO::getCrmManorId, id);
        return PojoUtils.map(list(queryWrapper), CrmEnterpriseRelationManorDTO.class);
    }

    @Override
    public int batchDeleteWithIds(List<Long> idList, Long opUserId, String message) {
        if (CollUtil.isEmpty(idList)) {
            return 0;
        }
        QueryWrapper<CrmEnterpriseRelationManorDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().in(CrmEnterpriseRelationManorDO::getId, idList);
        CrmEnterpriseRelationManorDO deleteDO = new CrmEnterpriseRelationManorDO();
        deleteDO.setRemark(message);
        deleteDO.setOpUserId(opUserId);
        return baseMapper.batchDeleteWithFill(deleteDO, queryWrapper);
    }

    @Override
    public Page<CrmEnterpriseRelationManorDTO> pageListByManorId(QueryCrmEnterpriseRelationManorPageRequest request) {
        QueryWrapper<CrmEnterpriseRelationManorDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(CrmEnterpriseRelationManorDO::getCrmManorId, request.getManorId());
        queryWrapper.lambda().orderByDesc(CrmEnterpriseRelationManorDO::getCreateTime);
        return PojoUtils.map(page(request.getPage(), queryWrapper), CrmEnterpriseRelationManorDTO.class);
    }

    @Override
    public boolean checkDuplicate(Long crmEnId, Long categoryId, Long id) {
        QueryWrapper<CrmEnterpriseRelationManorDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().select(CrmEnterpriseRelationManorDO::getId);
        queryWrapper.lambda().eq(CrmEnterpriseRelationManorDO::getCrmEnterpriseId, crmEnId);
        queryWrapper.lambda().eq(CrmEnterpriseRelationManorDO::getCategoryId, categoryId);
        //如果存在ID 查询过滤掉本ID的数据的数据查询
        if (Objects.nonNull(id) && id > 0L) {
            queryWrapper.lambda().ne(CrmEnterpriseRelationManorDO::getId, id);
        }
        queryWrapper.lambda().last(" limit 1");
        List list = list(queryWrapper);
        return !list.isEmpty();
    }

    @Override
    public List<CrmRelationManorBO> queryList(Long crmEnterpriseId) {
        LambdaQueryWrapper<CrmEnterpriseRelationManorDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(CrmEnterpriseRelationManorDO::getCrmEnterpriseId,crmEnterpriseId);
        List<CrmEnterpriseRelationManorDO> list = this.list(wrapper);
        if(CollUtil.isEmpty(list)){
            return Lists.newArrayList();
        }
        List<Long> manorIds = list.stream().map(CrmEnterpriseRelationManorDO::getCrmManorId).collect(Collectors.toList());
        List<CrmRelationManorBO> relationManorBOS = Lists.newArrayListWithExpectedSize(list.size());
        LambdaQueryWrapper<CrmManorDO> manorWrapper = Wrappers.lambdaQuery();
        manorWrapper.in(CrmManorDO::getId,manorIds);
        List<CrmManorDO> crmManorDOS = crmManorService.list(manorWrapper);
        if(CollUtil.isEmpty(crmManorDOS)){
            return Lists.newArrayList();
        }
        Map<Long, CrmManorDO> manorDOMap = crmManorDOS.stream().collect(Collectors.toMap(CrmManorDO::getId, Function.identity()));
        list.forEach(relation->{
            CrmRelationManorBO crmRelationManorBO = new CrmRelationManorBO();
            CrmManorDO crmManorDO = manorDOMap.get(relation.getCrmManorId());
            crmRelationManorBO.setCategoryId(relation.getCategoryId()).setCrmManorId(relation.getCrmManorId()).setManorName(crmManorDO.getName()).setManorNo(crmManorDO.getManorNo());
            relationManorBOS.add(crmRelationManorBO);
        });
        return relationManorBOS;
    }

    @Override
    public Page<CrmEnterpriseRelationManorDTO> pageExportList(QueryCrmManorPageRequest request) {
        //TODO:
        return baseMapper.pageExportList(request.getPage(),request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateBatch(List<SaveOrUpdateManorRelationRequest> request) {
        log.debug("辖区变更request={}",request.toString());
        List<SaveOrUpdateManorRelationRequest> finalReq = Lists.newArrayList();

        LambdaQueryWrapper<CrmEnterpriseRelationManorDO> wrapper = Wrappers.lambdaQuery();
        final List<Long> entIds = request.stream().map(SaveOrUpdateManorRelationRequest::getCrmEnterpriseId).collect(Collectors.toList());

        wrapper.in(CrmEnterpriseRelationManorDO::getCrmEnterpriseId,entIds);
        final List<Long> categoryIds = request.stream().map(SaveOrUpdateManorRelationRequest::getCategoryId).collect(Collectors.toList());
        final List<Long> manorIds = request.stream().map(SaveOrUpdateManorRelationRequest::getCrmManorId).collect(Collectors.toList());
        wrapper.in(CrmEnterpriseRelationManorDO::getCrmManorId,manorIds);
        wrapper.in(CrmEnterpriseRelationManorDO::getCategoryId,categoryIds);
        //旧数据
        List<CrmEnterpriseRelationManorDO> relationManorDOS = this.list(wrapper);
        //Map<Long, CrmEnterpriseRelationManorDO> relationManorDOMap = relationManorDOS.stream().collect(Collectors.toMap(CrmEnterpriseRelationManorDO::getCategoryId, Function.identity()));

        //List<Long> cEnIdList = relationManorDOS.stream().map(CrmEnterpriseRelationManorDO::getCrmEnterpriseId).collect(Collectors.toList());
        //List<Long> categoryIdList = relationManorDOS.stream().map(CrmEnterpriseRelationManorDO::getCategoryId).collect(Collectors.toList());
        //查询辖区代表
        //三者关系
        LambdaQueryWrapper<CrmEnterpriseRelationShipDO> relWrapper = Wrappers.lambdaQuery();
        relWrapper.in(CrmEnterpriseRelationShipDO::getCrmEnterpriseId,entIds).in(CrmEnterpriseRelationShipDO::getCategoryId,categoryIds).in(CrmEnterpriseRelationShipDO::getManorId,manorIds)
                .eq(CrmEnterpriseRelationShipDO::getSupplyChainRoleType, CrmSupplyChainRoleEnum.HOSPITAL.getCode());
        List<CrmEnterpriseRelationShipDO> relationShipDOS = crmEnterpriseRelationShipService.list(relWrapper);
        List<CrmEnterpriseRelationShipDO> updateList = Lists.newArrayListWithExpectedSize(relationShipDOS.size());
        relationShipDOS.stream().forEach(crmEnterpriseRelationShipDO -> {
            CrmEnterpriseRelationShipDO update = new CrmEnterpriseRelationShipDO();
            PojoUtils.map(crmEnterpriseRelationShipDO,update);
            update.setId(crmEnterpriseRelationShipDO.getId());
            SaveOrUpdateManorRelationRequest crmEnterpriseRelationManorDO = request.stream().filter(c -> c.getCrmEnterpriseId().equals(crmEnterpriseRelationShipDO.getCrmEnterpriseId()) && c.getCategoryId().equals(crmEnterpriseRelationShipDO.getCategoryId())
            && c.getCrmManorId().equals(crmEnterpriseRelationShipDO.getManorId())).findAny().orElse(null);
            if(Objects.isNull(crmEnterpriseRelationManorDO)){
                return;
            }
            CrmManorRepresentativeDTO newRel = crmManorRepresentativeService.getByManorId(crmEnterpriseRelationManorDO.getNewManorId());
            update.setManorId(crmEnterpriseRelationManorDO.getNewManorId()).setPostCode(newRel.getRepresentativePostCode()).setPostName(newRel.getRepresentativePostName()).setRemark("辖区变更流程插入").setOpTime(new Date());
            update.setOpUserId(1L);
            updateList.add(update);
        });
        //删除代跑三者关系
        List<Long> delIds = updateList.stream().map(CrmEnterpriseRelationShipDO::getId).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(delIds)){
            CrmEnterpriseRelationPinchRunnerDO deleteDO1 = new CrmEnterpriseRelationPinchRunnerDO();
            deleteDO1.setRemark("辖区变更删除");
            deleteDO1.setOpUserId(1L);
            QueryWrapper<CrmEnterpriseRelationPinchRunnerDO> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.lambda().in(CrmEnterpriseRelationPinchRunnerDO::getEnterpriseCersId, delIds);
            crmEnterpriseRelationPinchRunnerService.batchDeleteWithFill(deleteDO1, queryWrapper2);
        }
        QueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().in(CrmEnterpriseRelationShipDO::getId, delIds);
        CrmEnterpriseRelationShipDO deleteDO = new CrmEnterpriseRelationShipDO();
        deleteDO.setRemark("辖区变更删除");
        deleteDO.setOpUserId(1L);
        deleteDO.setUpdateTime(new Date());
        //删除三者关系
        crmEnterpriseRelationShipService.batchDeleteWithFill(deleteDO,queryWrapper);
        crmEnterpriseRelationShipService.saveBatch(updateList);
        request.forEach(d->{
            CrmEnterpriseRelationManorDO crmEnterpriseRelationManorDO = relationManorDOS.stream().filter(r -> r.getCrmManorId().equals(d.getCrmManorId()) && r.getCrmEnterpriseId().equals(d.getCrmEnterpriseId()) && r.getCategoryId().equals(d.getCategoryId())).findAny().orElse(null);
            d.setId(crmEnterpriseRelationManorDO.getId());
            SaveOrUpdateManorRelationRequest s = new  SaveOrUpdateManorRelationRequest();
            s.setCrmManorId(d.getCrmManorId()).setNewManorId(d.getNewManorId());
            finalReq.add(s);
            d.setCrmManorId(d.getNewManorId());
        });
        List<CrmEnterpriseRelationManorDO> doList = PojoUtils.map(request,CrmEnterpriseRelationManorDO.class);
        boolean result = this.updateBatchById(doList);
        //更新辖区的机构和品种统计数量
        finalReq.forEach(req->{
            UpdateManorNumRequest updateManorNumRequest = new UpdateManorNumRequest();
            updateManorNumRequest.setOpUserId(1L);
            updateManorNumRequest.setOpTime(new Date());
            updateManorNumRequest.setCrmManorId(req.getNewManorId());
            this.updateNum(updateManorNumRequest);
            updateManorNumRequest.setCrmManorId(req.getCrmManorId());
            this.updateNum(updateManorNumRequest);
        });
        return result;
    }

    public void updateNum(UpdateManorNumRequest request) {
        //辖区机构关系
        List<CrmEnterpriseRelationManorDTO> relationManorDOList = this.getByManorId(request.getCrmManorId());
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

}
