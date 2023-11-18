package com.yiling.dataflow.crm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyCountRequest;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyPageListRequest;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseByNamePageListRequest;
import com.yiling.dataflow.agency.entity.CrmHospitalDO;
import com.yiling.dataflow.agency.entity.CrmPharmacyDO;
import com.yiling.dataflow.agency.entity.CrmSupplierDO;
import com.yiling.dataflow.agency.enums.CrmBusinessCodeEnum;
import com.yiling.dataflow.agency.service.CrmHospitalService;
import com.yiling.dataflow.agency.service.CrmPharmacyService;
import com.yiling.dataflow.agency.service.CrmSupplierService;
import com.yiling.dataflow.crm.bo.CrmEnterpriseIdAndNameBO;
import com.yiling.dataflow.crm.bo.CrmEnterprisePartBO;
import com.yiling.dataflow.crm.dao.CrmEnterpriseMapper;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseSimpleDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseSimpleRegionInfoDTO;
import com.yiling.dataflow.crm.dto.request.PermitAgencyLockApplyRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseBackUpPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterprisePageRequest;
import com.yiling.dataflow.crm.dto.request.QueryDataScopeRequest;
import com.yiling.dataflow.crm.dto.request.SaveAgencyEnterpriseRequest;
import com.yiling.dataflow.crm.dto.request.SaveCrmEnterpriseRequest;
import com.yiling.dataflow.crm.dto.request.UpdateAgencyEnterpriseRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.dataflow.crm.enums.CrmEnterpriseErrorCode;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.framework.common.annotations.DynamicName;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-06-21
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "dataflow:CrmEnterprise")
public class CrmEnterpriseServiceImpl extends BaseServiceImpl<CrmEnterpriseMapper, CrmEnterpriseDO> implements CrmEnterpriseService {

    @Autowired
    private CrmEnterpriseMapper crmEnterpriseMapper;

    @Lazy
    @Autowired
    CrmSupplierService               crmSupplierService;
    @Lazy
    @Autowired
    CrmPharmacyService               crmPharmacyService;
    @Lazy
    @Autowired
    CrmHospitalService               crmHospitalService;
    @Lazy
    @Autowired
    CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;

    @Override
    public Page<CrmEnterpriseSimpleDTO> getCrmEnterpriseSimplePage(QueryCrmEnterprisePageRequest request) {
        Page<CrmEnterpriseDO> page = new Page<>(request.getCurrent(), request.getSize());
        return PojoUtils.map(crmEnterpriseMapper.page(page, request), CrmEnterpriseSimpleDTO.class);
    }

    @Override
    public Long saveCrmEnterpriseSimple(SaveCrmEnterpriseRequest request) {
        //判断是否存在
        CrmEnterpriseDO crmEnterpriseDO = PojoUtils.map(request, CrmEnterpriseDO.class);
        if (this.save(crmEnterpriseDO)) {
            return crmEnterpriseDO.getId();
        }
        return 0L;
    }

    @Override
    public Integer updateCrmEnterpriseSimple(UpdateAgencyEnterpriseRequest request) {
        return this.getBaseMapper().updateCrmEnterpriseSimple(request);
    }

    @Override
    public void saveAgencyEnterpriseList(List<SaveAgencyEnterpriseRequest> requestList) {
        List<CrmEnterpriseDO> crmEnterpriseDOList = new ArrayList<>();
        for (SaveAgencyEnterpriseRequest request : requestList) {
            if (StrUtil.isEmpty(request.getLicenseNumber()) || StrUtil.isEmpty(request.getName())) {
                throw new BusinessException(CrmEnterpriseErrorCode.VERIFY_ERROR_CODE);
            }
            CrmEnterpriseDTO crmEnterpriseByLicenseNumber = null;
            if (!"0".equals(request.getLicenseNumber())) {
                crmEnterpriseByLicenseNumber = this.getCrmEnterpriseByLicenseNumber(request.getLicenseNumber(),true);
            }
            CrmEnterpriseDTO crmEnterpriseByName = this.getCrmEnterpriseCodeByName(request.getName(),true);
            if (crmEnterpriseByLicenseNumber != null || crmEnterpriseByName != null) {
                throw new BusinessException(CrmEnterpriseErrorCode.REPEAT_ERROR_CODE);
            }
            CrmEnterpriseDO crmEnterpriseDO = PojoUtils.map(request, CrmEnterpriseDO.class);
            crmEnterpriseDOList.add(crmEnterpriseDO);
        }
        if (CollUtil.isNotEmpty(crmEnterpriseDOList)) {
            this.saveBatch(crmEnterpriseDOList);
        }
    }

    @Override
    public void updateAgencyEnterpriseList(List<UpdateAgencyEnterpriseRequest> requestList) {
        for (UpdateAgencyEnterpriseRequest request : requestList) {
            this.getBaseMapper().updateCrmEnterpriseSimple(request);
        }
    }

    @Override
    public CrmEnterpriseDTO getCrmEnterpriseCodeByName(String name, boolean isEffect) {
        QueryWrapper<CrmEnterpriseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CrmEnterpriseDO::getName, name);
        if (isEffect) {
            queryWrapper.lambda().eq(CrmEnterpriseDO::getBusinessCode, 1);
        }
        queryWrapper.last("limit 1");
        return PojoUtils.map(this.getOne(queryWrapper), CrmEnterpriseDTO.class);
    }

    @Override
    public CrmEnterpriseDTO getCrmEnterpriseByLicenseNumber(String licenseNumber, boolean isEffect) {
        QueryWrapper<CrmEnterpriseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CrmEnterpriseDO::getLicenseNumber, licenseNumber);
        if (isEffect) {
            queryWrapper.lambda().eq(CrmEnterpriseDO::getBusinessCode, 1);
        }
        queryWrapper.last("limit 1");
        return PojoUtils.map(this.getOne(queryWrapper), CrmEnterpriseDTO.class);
    }

    @Override
    public Page<CrmEnterpriseDTO> getCrmEnterprisePage(QueryCrmEnterprisePageRequest request) {
        QueryWrapper<CrmEnterpriseDO> queryWrapper = new QueryWrapper<>();
        Page<CrmEnterpriseDO> page = new Page<>(request.getCurrent(), request.getSize());
        return PojoUtils.map(this.page(page, queryWrapper), CrmEnterpriseDTO.class);
    }


    @Override
    public List<CrmEnterpriseDTO> getCrmEnterpriseCodeByNameList(List<String> nameList) {
        QueryWrapper<CrmEnterpriseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CrmEnterpriseDO::getName, nameList);
        return PojoUtils.map(this.list(queryWrapper), CrmEnterpriseDTO.class);
    }

    @Override
    public List<CrmEnterpriseDTO> getCrmEnterpriseByCodeList(List<String> codeList) {
        Assert.notEmpty(codeList, "参数 codeList 不能为空");
        LambdaQueryWrapper<CrmEnterpriseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CrmEnterpriseDO::getCode, codeList);
        wrapper.eq(CrmEnterpriseDO::getBusinessCode, 1);
        wrapper.eq(CrmEnterpriseDO::getSupplyChainRole, CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode());
        List list = this.list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return PojoUtils.map(list, CrmEnterpriseDTO.class);
    }

    @Override
    @Deprecated
    public List<CrmEnterpriseDTO> listByProvincialAreas(List<String> provincialAreas) {
        Assert.notEmpty(provincialAreas, "参数 provincialAreas 不能为空");
        return ListUtil.empty();
    }

    @Override
    public List<Long> listIdsByProvinceNames(List<String> provinceNames) {
        if (CollUtil.isEmpty(provinceNames)) {
            return ListUtil.empty();
        }

        LambdaQueryWrapper<CrmEnterpriseDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(CrmEnterpriseDO::getId);
        queryWrapper.in(CrmEnterpriseDO::getProvinceName, provinceNames);
        queryWrapper.eq(CrmEnterpriseDO::getBusinessCode, 1);

        List<CrmEnterpriseDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list.stream().map(CrmEnterpriseDO::getId).collect(Collectors.toList());
    }

    @Override
    public List<CrmEnterpriseDTO> listByIdsAndName(List<Long> ids, String name) {
        if (null == ids) {
            return ListUtil.empty();
        }
//        LambdaQueryWrapper<CrmEnterpriseDO> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(CrmEnterpriseDO::getSupplyChainRole, CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode());
//        wrapper.isNotNull(CrmEnterpriseDO::getCode);
//        wrapper.isNotNull(CrmEnterpriseDO::getLicenseNumber);
//        wrapper.like(CrmEnterpriseDO::getName, name);
//        if (CollUtil.isNotEmpty(ids)) {
//            wrapper.in(CrmEnterpriseDO::getId, ids);
//        }
//        // 关联扩展表 flow_distribution_enterprise
//        wrapper.exists("select 1 from flow_distribution_enterprise fde where fde.crm_enterprise_id = id and fde.code != ''");
//        wrapper.last("limit 50");


        List<CrmEnterpriseDO> list = this.baseMapper.listByIdsAndName(ids, name);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return PojoUtils.map(list, CrmEnterpriseDTO.class);
    }

    @Override
    public int getCountBySupplyChainRole(Integer supplyChainRole) {
        LambdaQueryWrapper<CrmEnterpriseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmEnterpriseDO::getSupplyChainRole, supplyChainRole);
        return this.count(wrapper);
    }

    @Override
    public List<CrmEnterpriseDTO> listByEidList(List<Long> eidList) {
        if (CollUtil.isEmpty(eidList) || eidList.size() == 0) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<CrmEnterpriseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmEnterpriseDO::getSupplyChainRole, CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode());
        if (CollUtil.isNotEmpty(eidList)) {
            wrapper.in(CrmEnterpriseDO::getEid, eidList);
        }
        List list = this.list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return PojoUtils.map(list, CrmEnterpriseDTO.class);
    }

    @Override
    public List<CrmEnterpriseDTO> getDistributorEnterpriseByIds(SjmsUserDatascopeBO datascopeBO) {
        if (ObjectUtil.isNull(datascopeBO) || ObjectUtil.equal(OrgDatascopeEnum.NONE.getCode(), datascopeBO.getOrgDatascope())) {
            return ListUtil.empty();
        }

        LambdaQueryWrapper<CrmEnterpriseDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CrmEnterpriseDO::getSupplyChainRole, CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode());
        lambdaQueryWrapper.eq(CrmEnterpriseDO::getBusinessCode, 1);

        if (ObjectUtil.equal(OrgDatascopeEnum.PORTION.getCode(), datascopeBO.getOrgDatascope())) {
            SjmsUserDatascopeBO.OrgPartDatascopeBO orgPartDatascopeBO = datascopeBO.getOrgPartDatascopeBO();
            List<Long> crmIds = orgPartDatascopeBO.getCrmEids();
            List<String> provinceCodes = orgPartDatascopeBO.getProvinceCodes();
            if (CollUtil.isEmpty(crmIds) && CollUtil.isEmpty(provinceCodes)) {
                return ListUtil.empty();
            }

            boolean idsFlag = CollUtil.isNotEmpty(crmIds) ? true : false;
            boolean provinceCodesFlag = CollUtil.isNotEmpty(provinceCodes) ? true : false;
            if (idsFlag && provinceCodesFlag) {
                lambdaQueryWrapper.and((wrapper) -> {
                    wrapper.in(CrmEnterpriseDO::getId, crmIds)
                            .or()
                            .in(CrmEnterpriseDO::getProvinceCode, provinceCodes);
                });
            } else if (idsFlag) {
                lambdaQueryWrapper.in(CrmEnterpriseDO::getId, crmIds);
            } else if (provinceCodesFlag) {
                lambdaQueryWrapper.in(CrmEnterpriseDO::getProvinceCode, provinceCodes);
            }
        }

        List list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return PojoUtils.map(list, CrmEnterpriseDTO.class);
    }

    @Override
    public List<Long> listById(List<Long> crmEnterIdList) {
        LambdaQueryWrapper<CrmEnterpriseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CrmEnterpriseDO::getEid);
        wrapper.in(CrmEnterpriseDO::getId, crmEnterIdList);
        return Optional.ofNullable(list(wrapper).stream().map(CrmEnterpriseDO::getEid).distinct().collect(Collectors.toList())).orElse(Lists.newArrayList());
    }

    @Override
    public Page<CrmEnterpriseDTO> getCrmEnterpriseByName(QueryCrmEnterpriseByNamePageListRequest request) {
        LambdaQueryWrapper<CrmEnterpriseDO> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(request.getName())) {
            queryWrapper.like(CrmEnterpriseDO::getName, request.getName());
        }
        if (Objects.nonNull(request.getCrmEnterpriseId()) && 0 != request.getCrmEnterpriseId()) {
            queryWrapper.eq(CrmEnterpriseDO::getId, request.getCrmEnterpriseId());
        }
        if (Objects.nonNull(request.getSupplyChainRole())) {
            queryWrapper.eq(CrmEnterpriseDO::getSupplyChainRole, request.getSupplyChainRole());
        } else {
            queryWrapper.gt(Objects.nonNull(request.getFilterBySupplyChainRole()) && request.getFilterBySupplyChainRole(), CrmEnterpriseDO::getSupplyChainRole, 0);
        }
        return PojoUtils.map(this.page(request.getPage(), queryWrapper), CrmEnterpriseDTO.class);
    }

    @Override
    public List<CrmEnterpriseDTO> getCrmEnterpriseListById(List<Long> crmEnterPriseIds) {
        if (crmEnterPriseIds == null || crmEnterPriseIds.size() == 0) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<CrmEnterpriseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CrmEnterpriseDO::getId, crmEnterPriseIds);
        return PojoUtils.map(this.list(wrapper), CrmEnterpriseDTO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean permitAgencyLockApply(List<PermitAgencyLockApplyRequest> request) {
        if (CollUtil.isEmpty(request)) {
            return Boolean.FALSE;
        }
        //更新businessCode
        LambdaQueryWrapper<CrmEnterpriseDO> queryCrmEntWrapper = Wrappers.lambdaQuery();
        queryCrmEntWrapper.in(CrmEnterpriseDO::getId, request.stream().map(PermitAgencyLockApplyRequest::getCrmEnterpriseId).collect(Collectors.toList()));
        List<CrmEnterpriseDO> crmEnterpriseDOList = list(queryCrmEntWrapper);
        //需要更新businessCode的list
        List<CrmEnterpriseDO> crmEntUpdateList = crmEnterpriseDOList.stream().filter(e -> ObjectUtil.equal(e.getBusinessCode(), CrmBusinessCodeEnum.SIMPLE.getCode()) || ObjectUtil.equal(e.getBusinessCode(), 0)).collect(Collectors.toList());
        crmEntUpdateList.stream().forEach(e -> {
            e.setBusinessCode(CrmBusinessCodeEnum.CHAIN.getCode());
        });
        boolean isSuccess;
        if (CollUtil.isNotEmpty(crmEntUpdateList)) {
            isSuccess = updateBatchById(crmEntUpdateList);
            if (!isSuccess) {
                log.error("机构锁定通过后，更新crm_enterprise表的business_code失败，参数={}", crmEntUpdateList);
                throw new ServiceException(ResultCode.FAILED);
            }
        }
        //更新机构拓展信息
        //忽略掉拓展信息原本存在的机构
        updateAgencyInfo(request.stream().filter(e -> ObjectUtil.notEqual(e.getDataType(), 1)).collect(Collectors.toList()));

        //更新商业公司的锁定类型
        List<PermitAgencyLockApplyRequest> disList = request.stream().filter(e -> ObjectUtil.equal(e.getDataType(), 1) && ObjectUtil.equal(e.getSupplyChainRole(), CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(disList)) {
            updateDistributorLockType(disList);
        }
        //追加三者关系
        List<CrmEnterpriseRelationShipDO> relationList = ListUtil.toList();
        request.forEach(e -> {
            relationList.addAll(PojoUtils.map(e.getRelationList(), CrmEnterpriseRelationShipDO.class));
        });
        //如果三者关系列表为空，则到此结束
        if (CollUtil.isEmpty(relationList)) {
            return Boolean.TRUE;
        }
        isSuccess = crmEnterpriseRelationShipService.saveBatch(relationList);
        if (!isSuccess) {
            log.error("机构锁定审批成功后追加三者关系失败，参数={}", relationList);
            throw new ServiceException(ResultCode.FAILED);
        }
        return Boolean.TRUE;
    }

    @Override
    public List<CrmEnterpriseSimpleRegionInfoDTO> listByNameAndSupplyChainRoleWithLimit(Long id, String name, List<Integer> supplyChainRoleList) {
        if (CollUtil.isEmpty(supplyChainRoleList)) {
            throw new ServiceException("erp供应链角色类型，不能为空");
        }
        if (ObjectUtil.isNull(id) && StrUtil.isBlank(name)) {
            throw new ServiceException("机构编码、机构名称，不能同时为空");
        }
        if (ObjectUtil.isNotNull(id) && id.intValue() == 0) {
            throw new ServiceException("机构编码，不能为0");
        }

        LambdaQueryWrapper<CrmEnterpriseDO> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(id)) {
            queryWrapper.eq(CrmEnterpriseDO::getId, id);
        }
        if (StrUtil.isNotBlank(name)) {
            queryWrapper.like(CrmEnterpriseDO::getName, name);
        }
        queryWrapper.in(CrmEnterpriseDO::getSupplyChainRole, supplyChainRoleList);
        queryWrapper.last("limit 50");
        List<CrmEnterpriseDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return PojoUtils.map(list, CrmEnterpriseSimpleRegionInfoDTO.class);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<CrmEnterpriseDO> listSuffixByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList, String tableSuffix) {
        LambdaQueryWrapper<CrmEnterpriseDO> queryCrmEntWrapper = Wrappers.lambdaQuery();
        queryCrmEntWrapper.in(CrmEnterpriseDO::getId, crmEnterpriseIdList);
        return this.list(queryCrmEntWrapper);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public Page<CrmEnterpriseDO> pageListSuffixBackUpInfo(QueryCrmEnterpriseBackUpPageRequest request, String tableSuffix) {
        LambdaQueryWrapper<CrmEnterpriseDO> queryCrmEntWrapper = Wrappers.lambdaQuery();
        if (Objects.nonNull(request.getCrmEnterpriseId()) && 0L != request.getCrmEnterpriseId()) {
            queryCrmEntWrapper.eq(CrmEnterpriseDO::getId, request.getCrmEnterpriseId());
        }
        if (StringUtils.isNotBlank(request.getName())) {
            queryCrmEntWrapper.like(CrmEnterpriseDO::getName, request.getName());
        }
        if (Objects.nonNull(request.getSupplyChainRole()) && 0 != request.getSupplyChainRole()) {
            queryCrmEntWrapper.eq(CrmEnterpriseDO::getSupplyChainRole, request.getSupplyChainRole());
        }
        if (Objects.nonNull(request.getBusinessCode()) && 0 != request.getBusinessCode()) {
            queryCrmEntWrapper.eq(CrmEnterpriseDO::getBusinessCode, request.getBusinessCode());
        }
        return this.page(new Page<>(request.getCurrent(), request.getSize()), queryCrmEntWrapper);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    @Cacheable(key = "#crmId+'+'+#tableSuffix+'+getSuffixByCrmEnterpriseId'")
    public CrmEnterpriseDO getSuffixByCrmEnterpriseId(Long crmId, String tableSuffix) {

        QueryWrapper<CrmEnterpriseDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CrmEnterpriseDO::getId, crmId)
                .last("limit 1");

        return this.getOne(wrapper);
    }

    @Override
    public List<Long> getCrmEnterpriseListByEidsAndProvinceCode(List<Long> eids, List<String> provinceCodes) {
        QueryWrapper<CrmEnterpriseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(CrmEnterpriseDO::getId);
        if (CollUtil.isNotEmpty(eids) && CollUtil.isNotEmpty(provinceCodes)) {
            queryWrapper.lambda().and(i -> i.in(CrmEnterpriseDO::getId, eids)
                    .or().in(CrmEnterpriseDO::getProvinceCode, provinceCodes));
        } else if (CollUtil.isNotEmpty(eids) && CollUtil.isEmpty(provinceCodes)) {
            return eids;
        } else {
            queryWrapper.lambda().in(CrmEnterpriseDO::getProvinceCode, provinceCodes);
        }
        queryWrapper.lambda().orderByDesc(CrmEnterpriseDO::getId);
        return Optional.ofNullable((list(queryWrapper)).stream().map(CrmEnterpriseDO::getId).collect(Collectors.toList())).orElse(ListUtil.empty());
    }

    @Override
    public List<CrmEnterpriseDTO> getCrmEnterpriseListByDataScope(QueryDataScopeRequest request) {
        QueryWrapper<CrmEnterpriseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(CrmEnterpriseDO::getId, CrmEnterpriseDO::getEid);
        queryWrapper.lambda().eq(CrmEnterpriseDO::getSupplyChainRole, 1);
        if (CollUtil.isNotEmpty(request.getSjmsUserDatascopeBO().getOrgPartDatascopeBO().getCrmEids()) && CollUtil.isNotEmpty(request.getSjmsUserDatascopeBO().getOrgPartDatascopeBO().getProvinceCodes())) {
            queryWrapper.lambda().and(i -> i.in(CrmEnterpriseDO::getId, request.getSjmsUserDatascopeBO().getOrgPartDatascopeBO().getCrmEids())
                    .or().in(CrmEnterpriseDO::getProvinceCode, request.getSjmsUserDatascopeBO().getOrgPartDatascopeBO().getProvinceCodes()));
        } else if (CollUtil.isNotEmpty(request.getSjmsUserDatascopeBO().getOrgPartDatascopeBO().getCrmEids()) && CollUtil.isEmpty(request.getSjmsUserDatascopeBO().getOrgPartDatascopeBO().getProvinceCodes())) {
            queryWrapper.lambda().in(CrmEnterpriseDO::getId, request.getSjmsUserDatascopeBO().getOrgPartDatascopeBO().getCrmEids());
            ;
        } else {
            queryWrapper.lambda().in(CrmEnterpriseDO::getProvinceCode, request.getSjmsUserDatascopeBO().getOrgPartDatascopeBO().getProvinceCodes());
        }
        queryWrapper.lambda().orderByDesc(CrmEnterpriseDO::getId);
        return PojoUtils.map(list(queryWrapper), CrmEnterpriseDTO.class);
    }

    @Cacheable(key = "#current+'+'+#size+'+getIdAndNameListPage'")
    @Override
    public Page<CrmEnterpriseIdAndNameBO> getIdAndNameListPage(Integer current, Integer size) {
        Page<CrmEnterpriseDO> page = new Page<>(current, size);

        LambdaQueryWrapper<CrmEnterpriseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CrmEnterpriseDO::getId, CrmEnterpriseDO::getName);
        return PojoUtils.map(baseMapper.selectPage(page, wrapper), CrmEnterpriseIdAndNameBO.class);
    }

    @Override
    public Page<CrmEnterpriseDTO> getCrmEnterpriseInfoPage(QueryCrmAgencyPageListRequest request) {
        QueryWrapper<CrmEnterpriseDO> queryWrapper = new QueryWrapper<>();
        log.info("CrmEnterpriseServiceImpl.getCrmEnterpriseInfoPage->start");
        log.info("CrmEnterpriseServiceImpl.getCrmEnterpriseInfoPage:{}", request);
        if (StringUtils.isNotEmpty(request.getId())) {
            queryWrapper.lambda().eq(CrmEnterpriseDO::getId, request.getId());
        }
        if (StringUtils.isNotEmpty(request.getCode())) {
            queryWrapper.lambda().eq(CrmEnterpriseDO::getCode, request.getCode());
        }
        if (ObjectUtil.isNotEmpty(request.getSupplyChainRole())) {
            queryWrapper.lambda().eq(CrmEnterpriseDO::getSupplyChainRole, request.getSupplyChainRole());
        }
        if (StringUtils.isNotBlank(request.getYlCode())) {
            queryWrapper.lambda().eq(CrmEnterpriseDO::getYlCode, request.getYlCode());
        }
        if (StringUtils.isNotEmpty(request.getName())) {
            queryWrapper.lambda().like(CrmEnterpriseDO::getName, request.getName());
        }
        if (StringUtils.isNotEmpty(request.getShortName())) {
            queryWrapper.lambda().like(CrmEnterpriseDO::getShortName, request.getShortName());
        }
        if (StringUtils.isNotEmpty(request.getProvinceCode())) {
            queryWrapper.lambda().eq(CrmEnterpriseDO::getProvinceCode, request.getProvinceCode());
        }
        if (StringUtils.isNotEmpty(request.getCityCode())) {
            queryWrapper.lambda().eq(CrmEnterpriseDO::getCityCode, request.getCityCode());
        }
        if (StringUtils.isNotEmpty(request.getRegionCode())) {
            queryWrapper.lambda().eq(CrmEnterpriseDO::getRegionCode, request.getRegionCode());
        }
        if (StringUtils.isNotEmpty(request.getBusinessCode()) && !"0".equals(request.getBusinessCode())) {
            queryWrapper.lambda().eq(CrmEnterpriseDO::getBusinessCode, request.getBusinessCode());
        }
        if (ObjectUtil.isNotEmpty(request.getBeginTime())) {
            queryWrapper.lambda().ge(CrmEnterpriseDO::getCreateTime, DateUtil.parse(DateUtil.format(request.getBeginTime(), "yyyy-MM-dd 00:00:00")));
        }
        if (ObjectUtil.isNotEmpty(request.getEndTime())) {
            queryWrapper.lambda().le(CrmEnterpriseDO::getCreateTime, DateUtil.parse(DateUtil.format(request.getEndTime(), "yyyy-MM-dd 23:59:59")));
        }
        if (ObjectUtil.isNotNull(request.getSjmsUserDatascopeBO())) {
            SjmsUserDatascopeBO sjmsUserDatascopeBO = request.getSjmsUserDatascopeBO();
            if (OrgDatascopeEnum.PORTION == OrgDatascopeEnum.getFromCode(sjmsUserDatascopeBO.getOrgDatascope())) {
                //省区和crmEnterIds2个都不为空的时候时或的关系
                if (CollUtil.isNotEmpty(sjmsUserDatascopeBO.getOrgPartDatascopeBO().getCrmEids()) && CollUtil.isNotEmpty(sjmsUserDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes())) {
                    queryWrapper.lambda().and(i -> i.in(CrmEnterpriseDO::getId, sjmsUserDatascopeBO.getOrgPartDatascopeBO().getCrmEids())
                            .or().in(CrmEnterpriseDO::getProvinceCode, sjmsUserDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes()));
                } else if (CollUtil.isNotEmpty(sjmsUserDatascopeBO.getOrgPartDatascopeBO().getCrmEids())) {
                    queryWrapper.lambda().in(CrmEnterpriseDO::getId, sjmsUserDatascopeBO.getOrgPartDatascopeBO().getCrmEids());
                } else {
                    queryWrapper.lambda().in(CrmEnterpriseDO::getProvinceCode, sjmsUserDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes());
                }
            }
        }
        queryWrapper.lambda().orderByDesc(CrmEnterpriseDO::getId);
        Page<CrmEnterpriseDO> page = this.page(request.getPage(), queryWrapper);
        return PojoUtils.map(page, CrmEnterpriseDTO.class);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public CrmEnterpriseDO getBakFirstCrmEnterpriseInfo(QueryCrmAgencyCountRequest request,String tableSuffix) {
        QueryWrapper<CrmEnterpriseDO> wrapper = new QueryWrapper<>();
        if (Objects.nonNull(request.getNotId())) {
            wrapper.lambda().ne(CrmEnterpriseDO::getId, request.getNotId());
        }
        if (ObjectUtil.isNotEmpty(request.getSupplyChainRole())) {
            wrapper.lambda().eq(CrmEnterpriseDO::getSupplyChainRole, request.getSupplyChainRole());
        }
        if (StringUtils.isNotBlank(request.getCode())) {
            wrapper.lambda().eq(CrmEnterpriseDO::getCode, request.getCode());
        }
        if (StringUtils.isNotBlank(request.getYlCode())) {
            wrapper.lambda().eq(CrmEnterpriseDO::getYlCode, request.getYlCode());
        }
        if (ObjectUtil.isNotEmpty(request.getLicenseNumber())) {
            wrapper.lambda().eq(CrmEnterpriseDO::getLicenseNumber, request.getLicenseNumber());
        }
        if (ObjectUtil.isNotEmpty(request.getName())) {
            wrapper.lambda().eq(CrmEnterpriseDO::getName, request.getName());
        }
        if (ObjectUtil.isNotEmpty(request.getDistributionLicenseNumber())) {
            wrapper.lambda().eq(CrmEnterpriseDO::getDistributionLicenseNumber, request.getDistributionLicenseNumber());
        }
        if (ObjectUtil.isNotEmpty(request.getInstitutionPracticeLicense())) {
            wrapper.lambda().eq(CrmEnterpriseDO::getInstitutionPracticeLicense, request.getInstitutionPracticeLicense());
        }
        wrapper.lambda().last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public CrmEnterpriseDO getFirstCrmEnterpriseInfo(QueryCrmAgencyCountRequest request) {
        QueryWrapper<CrmEnterpriseDO> wrapper = new QueryWrapper<>();
        if (Objects.nonNull(request.getNotId())) {
            wrapper.lambda().ne(CrmEnterpriseDO::getId, request.getNotId());
        }
        if (ObjectUtil.isNotEmpty(request.getSupplyChainRole())) {
            wrapper.lambda().eq(CrmEnterpriseDO::getSupplyChainRole, request.getSupplyChainRole());
        }
        if (StringUtils.isNotBlank(request.getCode())) {
            wrapper.lambda().eq(CrmEnterpriseDO::getCode, request.getCode());
        }
        if (StringUtils.isNotBlank(request.getYlCode())) {
            wrapper.lambda().eq(CrmEnterpriseDO::getYlCode, request.getYlCode());
        }
        if (ObjectUtil.isNotEmpty(request.getLicenseNumber())) {
            wrapper.lambda().eq(CrmEnterpriseDO::getLicenseNumber, request.getLicenseNumber());
        }
        if (ObjectUtil.isNotEmpty(request.getName())) {
            wrapper.lambda().eq(CrmEnterpriseDO::getName, request.getName());
        }
        if (ObjectUtil.isNotEmpty(request.getDistributionLicenseNumber())) {
            wrapper.lambda().eq(CrmEnterpriseDO::getDistributionLicenseNumber, request.getDistributionLicenseNumber());
        }
        if (ObjectUtil.isNotEmpty(request.getInstitutionPracticeLicense())) {
            wrapper.lambda().eq(CrmEnterpriseDO::getInstitutionPracticeLicense, request.getInstitutionPracticeLicense());
        }
        wrapper.lambda().last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public Page<CrmEnterpriseIdAndNameBO> getCrmEnterpriseIdAndNameByName(QueryCrmEnterpriseByNamePageListRequest request, String tableSuffix) {
        LambdaQueryWrapper<CrmEnterpriseDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(CrmEnterpriseDO::getId, CrmEnterpriseDO::getName);
        if (StringUtils.isNotBlank(request.getName())) {
            queryWrapper.like(CrmEnterpriseDO::getName, request.getName());
        }
        if (Objects.nonNull(request.getCrmEnterpriseId()) && 0 != request.getCrmEnterpriseId()) {
            queryWrapper.eq(CrmEnterpriseDO::getId, request.getCrmEnterpriseId());
        }
        if (Objects.nonNull(request.getSupplyChainRole()) && 0 != request.getSupplyChainRole()) {
            queryWrapper.eq(CrmEnterpriseDO::getSupplyChainRole, request.getSupplyChainRole());
        }
        if (ObjectUtil.isNotNull(request.getSjmsUserDatascopeBO())) {
            SjmsUserDatascopeBO sjmsUserDatascopeBO = request.getSjmsUserDatascopeBO();
            if (OrgDatascopeEnum.PORTION == OrgDatascopeEnum.getFromCode(sjmsUserDatascopeBO.getOrgDatascope())) {
                //省区和crmEnterIds2个都不为空的时候时或的关系
                if (CollUtil.isNotEmpty(sjmsUserDatascopeBO.getOrgPartDatascopeBO().getCrmEids()) && CollUtil.isNotEmpty(sjmsUserDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes())) {
                    queryWrapper.and(i -> i.in(CrmEnterpriseDO::getId, sjmsUserDatascopeBO.getOrgPartDatascopeBO().getCrmEids())
                            .or().in(CrmEnterpriseDO::getProvinceCode, sjmsUserDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes()));
                } else if (CollUtil.isNotEmpty(sjmsUserDatascopeBO.getOrgPartDatascopeBO().getCrmEids())) {
                    queryWrapper.in(CrmEnterpriseDO::getId, sjmsUserDatascopeBO.getOrgPartDatascopeBO().getCrmEids());
                } else {
                    queryWrapper.in(CrmEnterpriseDO::getProvinceCode, sjmsUserDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes());
                }
            }
        }

        if (Objects.nonNull(request.getBusinessCode()) && 0 != request.getBusinessCode()) {
            queryWrapper.eq(CrmEnterpriseDO::getBusinessCode, request.getBusinessCode());
        }

        queryWrapper.gt(Objects.nonNull(request.getFilterBySupplyChainRole()) && request.getFilterBySupplyChainRole(), CrmEnterpriseDO::getSupplyChainRole, 0);
        return PojoUtils.map(this.page(request.getPage(), queryWrapper), CrmEnterpriseIdAndNameBO.class);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public Page<CrmEnterprisePartBO> getCrmEnterprisePartInfoByName(QueryCrmEnterpriseByNamePageListRequest request, String tableSuffix) {
        LambdaQueryWrapper<CrmEnterpriseDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(CrmEnterpriseDO::getId, CrmEnterpriseDO::getSupplyChainRole, CrmEnterpriseDO::getName, CrmEnterpriseDO::getLicenseNumber, CrmEnterpriseDO::getProvinceCode, CrmEnterpriseDO::getCityCode, CrmEnterpriseDO::getRegionCode, CrmEnterpriseDO::getProvinceName, CrmEnterpriseDO::getCityName, CrmEnterpriseDO::getRegionName, CrmEnterpriseDO::getPhone, CrmEnterpriseDO::getAddress);
        if (StringUtils.isNotBlank(request.getName())) {
            queryWrapper.like(CrmEnterpriseDO::getName, request.getName());
        }
        if (Objects.nonNull(request.getCrmEnterpriseId()) && 0 != request.getCrmEnterpriseId()) {
            queryWrapper.eq(CrmEnterpriseDO::getId, request.getCrmEnterpriseId());
        }
        if (Objects.nonNull(request.getSupplyChainRole()) && 0 != request.getSupplyChainRole()) {
            queryWrapper.eq(CrmEnterpriseDO::getSupplyChainRole, request.getSupplyChainRole());
        }
        if (Objects.nonNull(request.getBusinessCode()) && 0 != request.getBusinessCode()) {
            queryWrapper.eq(CrmEnterpriseDO::getBusinessCode, request.getBusinessCode());
        }
        if(StrUtil.isNotEmpty(request.getProvinceCode())){
            queryWrapper.eq(CrmEnterpriseDO::getProvinceCode, request.getProvinceCode());
        }
        if(StrUtil.isNotEmpty(request.getCityCode())){
            queryWrapper.eq(CrmEnterpriseDO::getCityCode, request.getCityCode());
        }
        if(StrUtil.isNotEmpty(request.getRegionCode())){
            queryWrapper.eq(CrmEnterpriseDO::getRegionCode, request.getRegionCode());
        }
        if (ObjectUtil.isNotNull(request.getSjmsUserDatascopeBO())) {
            SjmsUserDatascopeBO sjmsUserDatascopeBO = request.getSjmsUserDatascopeBO();
            if (OrgDatascopeEnum.PORTION == OrgDatascopeEnum.getFromCode(sjmsUserDatascopeBO.getOrgDatascope())) {
                //省区和crmEnterIds2个都不为空的时候时或的关系
                if (CollUtil.isNotEmpty(sjmsUserDatascopeBO.getOrgPartDatascopeBO().getCrmEids()) && CollUtil.isNotEmpty(sjmsUserDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes())) {
                    queryWrapper.and(i -> i.in(CrmEnterpriseDO::getId, sjmsUserDatascopeBO.getOrgPartDatascopeBO().getCrmEids())
                            .or().in(CrmEnterpriseDO::getProvinceCode, sjmsUserDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes()));
                } else if (CollUtil.isNotEmpty(sjmsUserDatascopeBO.getOrgPartDatascopeBO().getCrmEids())) {
                    queryWrapper.in(CrmEnterpriseDO::getId, sjmsUserDatascopeBO.getOrgPartDatascopeBO().getCrmEids());
                } else {
                    queryWrapper.in(CrmEnterpriseDO::getProvinceCode, sjmsUserDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes());
                }
            }
        }
        queryWrapper.gt(Objects.nonNull(request.getFilterBySupplyChainRole()) && request.getFilterBySupplyChainRole(), CrmEnterpriseDO::getSupplyChainRole, 0);
        if (StringUtils.isNotBlank(request.getName())) {
            queryWrapper.last(" order by CHAR_LENGTH(name)");
        }
        return PojoUtils.map(this.page(request.getPage(), queryWrapper), CrmEnterprisePartBO.class);
    }

    @Override
    public Page<CrmEnterpriseDO> getCrmEnterpriseByNameLikeRight(QueryCrmEnterpriseByNamePageListRequest request) {
        LambdaQueryWrapper<CrmEnterpriseDO> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(request.getName())) {
            queryWrapper.likeRight(CrmEnterpriseDO::getName, request.getName());
        }
        if (Objects.nonNull(request.getSupplyChainRole()) && 0 != request.getSupplyChainRole()) {
            queryWrapper.eq(CrmEnterpriseDO::getSupplyChainRole, request.getSupplyChainRole());
        }
        queryWrapper.gt(Objects.nonNull(request.getFilterBySupplyChainRole()) && request.getFilterBySupplyChainRole(), CrmEnterpriseDO::getSupplyChainRole, 0);
        return this.page(request.getPage(), queryWrapper);
    }

    /**
     * 更新机构信息
     *
     * @param request
     */
    private void updateAgencyInfo(List<PermitAgencyLockApplyRequest> request) {
        Map<Integer, List<PermitAgencyLockApplyRequest>> roleMap =
                request.stream().collect(Collectors.groupingBy(PermitAgencyLockApplyRequest::getSupplyChainRole));
        Map<Long, PermitAgencyLockApplyRequest> requestMap = request.stream().collect(Collectors.toMap(e -> e.getCrmEnterpriseId(), e -> e));

        roleMap.forEach((role, list) -> {
            List<Long> crmIdList = list.stream().map(PermitAgencyLockApplyRequest::getCrmEnterpriseId).collect(Collectors.toList());

            //商业公司
            if (ObjectUtil.equal(role, CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode())) {
                LambdaQueryWrapper<CrmSupplierDO> wrapper = Wrappers.lambdaQuery();
                wrapper.in(CrmSupplierDO::getCrmEnterpriseId, crmIdList);
                List<CrmSupplierDO> dbData = crmSupplierService.list(wrapper);
                Map<Long, Long> dbKeyMap = MapUtil.newHashMap();
                if (CollUtil.isNotEmpty(dbData)) {
                    dbKeyMap = dbData.stream().collect(Collectors.toMap(CrmSupplierDO::getCrmEnterpriseId, CrmSupplierDO::getId));
                }
                List<CrmSupplierDO> var = PojoUtils.map(list, CrmSupplierDO.class);
                Map<Long, Long> finalDbKeyMap = dbKeyMap;
                //判断数据库存在则更新，反之新增
                var.stream().forEach(e -> {
                    PermitAgencyLockApplyRequest var2 = requestMap.get(e.getCrmEnterpriseId());
                    if (ObjectUtil.isNotNull(var2)) {
                        e.setLockType(var2.getLockType());
                    }
                    Long id = finalDbKeyMap.get(e.getCrmEnterpriseId());
                    if (ObjectUtil.isNotNull(id) && ObjectUtil.notEqual(id, 0L)) {
                        e.setId(id);
                    } else {
                        e.setId(null);
                    }
                });
                boolean r = crmSupplierService.saveOrUpdateBatch(var);
                if (!r) {
                    log.error("机构锁定审批成功后更新商业公司拓展信息失败，参数={}", var);
                    throw new ServiceException(ResultCode.FAILED);
                }
            }
            //零售机构
            if (ObjectUtil.equal(role, CrmSupplyChainRoleEnum.PHARMACY.getCode())) {
                LambdaQueryWrapper<CrmPharmacyDO> wrapper = Wrappers.lambdaQuery();
                wrapper.in(CrmPharmacyDO::getCrmEnterpriseId, crmIdList);
                List<CrmPharmacyDO> dbData = crmPharmacyService.list(wrapper);
                Map<Long, Long> dbKeyMap = MapUtil.newHashMap();
                if (CollUtil.isNotEmpty(dbData)) {
                    dbKeyMap = dbData.stream().collect(Collectors.toMap(CrmPharmacyDO::getCrmEnterpriseId, CrmPharmacyDO::getId));
                }
                List<CrmPharmacyDO> var = PojoUtils.map(list, CrmPharmacyDO.class);
                Map<Long, Long> finalDbKeyMap = dbKeyMap;
                //判断数据库存在则更新，反之新增
                var.stream().forEach(e -> {
                    PermitAgencyLockApplyRequest var2 = requestMap.get(e.getCrmEnterpriseId());
                    if (ObjectUtil.isNotNull(var2)) {
                        e.setParentCompanyCode(var2.getParentSupplierCode());
                        e.setParentCompanyName(var2.getParentSupplierName());
                    }
                    Long id = finalDbKeyMap.get(e.getCrmEnterpriseId());
                    if (ObjectUtil.isNotNull(id) && ObjectUtil.notEqual(id, 0L)) {
                        e.setId(id);
                    } else {
                        e.setId(null);
                    }
                });
                boolean r = crmPharmacyService.saveOrUpdateBatch(var);
                if (!r) {
                    log.error("机构锁定审批成功后更新医疗机构拓展信息失败，参数={}", var);
                    throw new ServiceException(ResultCode.FAILED);
                }
            }
            //医疗机构
            if (ObjectUtil.equal(role, CrmSupplyChainRoleEnum.HOSPITAL.getCode())) {
                LambdaQueryWrapper<CrmHospitalDO> wrapper = Wrappers.lambdaQuery();
                wrapper.in(CrmHospitalDO::getCrmEnterpriseId, crmIdList);
                List<CrmHospitalDO> dbData = crmHospitalService.list(wrapper);
                Map<Long, Long> dbKeyMap = MapUtil.newHashMap();
                if (CollUtil.isNotEmpty(dbData)) {
                    dbKeyMap = dbData.stream().collect(Collectors.toMap(CrmHospitalDO::getCrmEnterpriseId, CrmHospitalDO::getId));
                }
                List<CrmHospitalDO> var = PojoUtils.map(list, CrmHospitalDO.class);
                Map<Long, Long> finalDbKeyMap = dbKeyMap;
                //判断数据库存在则更新，反之新增
                var.stream().forEach(e -> {
                    Long id = finalDbKeyMap.get(e.getCrmEnterpriseId());
                    if (ObjectUtil.isNotNull(id) && ObjectUtil.notEqual(id, 0L)) {
                        e.setId(id);
                    } else {
                        e.setId(null);
                    }
                });
                boolean r = crmHospitalService.saveOrUpdateBatch(var);
                if (!r) {
                    log.error("机构锁定审批成功后更新零售机构拓展信息失败，参数={}", var);
                    throw new ServiceException(ResultCode.FAILED);
                }
            }
        });
    }

    /**
     * 更新商业公司锁定类型
     *
     * @param request
     */
    private void updateDistributorLockType(List<PermitAgencyLockApplyRequest> request) {
        Map<Long, PermitAgencyLockApplyRequest> requestMap = request.stream().collect(Collectors.toMap(e -> e.getCrmEnterpriseId(), e -> e));
        //商业公司
        LambdaQueryWrapper<CrmSupplierDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CrmSupplierDO::getCrmEnterpriseId, requestMap.keySet());
        List<CrmSupplierDO> dbData = crmSupplierService.list(wrapper);

        if (CollUtil.isEmpty(dbData)) {
            return;
        }
        List<CrmSupplierDO> var = ListUtil.toList();
        dbData.stream().forEach(e -> {
            if (ObjectUtil.notEqual(e.getLockType(), requestMap.get(e.getCrmEnterpriseId()).getLockType())) {
                e.setLockType(requestMap.get(e.getCrmEnterpriseId()).getLockType());
                var.add(e);
            }
        });
        if (CollUtil.isNotEmpty(var)) {
            boolean r = crmSupplierService.saveOrUpdateBatch(var);
            if (!r) {
                log.error("机构锁定审批成功后更新商业公司锁定类型失败，参数={}", var);
                throw new ServiceException(ResultCode.FAILED);
            }
        }
    }
}
