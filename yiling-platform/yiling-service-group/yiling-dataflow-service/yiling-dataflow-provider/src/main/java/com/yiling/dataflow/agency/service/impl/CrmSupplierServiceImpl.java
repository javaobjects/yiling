package com.yiling.dataflow.agency.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.dataflow.agency.dto.CrmSupplierDTO;
import com.yiling.framework.common.util.PojoUtils;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.yiling.dataflow.agency.dao.CrmSupplierMapper;
import com.yiling.dataflow.agency.dto.CrmSupplierDTO;
import com.yiling.dataflow.agency.entity.CrmSupplierDO;
import com.yiling.dataflow.agency.service.CrmSupplierService;
import com.yiling.framework.common.annotations.DynamicName;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

/**
 * <p>
 * 商业公司扩展表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-14
 */
@Service
public class CrmSupplierServiceImpl extends BaseServiceImpl<CrmSupplierMapper, CrmSupplierDO> implements CrmSupplierService {

    @Override
    public List<Long> listByLevelAndGroup(Integer supplierLevel, Integer businessSystem) {
        if(Objects.isNull(supplierLevel)&&Objects.isNull(businessSystem)){
            return Lists.newArrayList();
        }
        QueryWrapper<CrmSupplierDO> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().select(CrmSupplierDO::getCrmEnterpriseId);
        if(Objects.nonNull(supplierLevel)){
            queryWrapper.lambda().eq(CrmSupplierDO::getSupplierLevel,supplierLevel);
        }
        if(Objects.nonNull(businessSystem)){
            queryWrapper.lambda().eq(CrmSupplierDO::getBusinessSystem,businessSystem);
        }
        return Optional.ofNullable(list(queryWrapper).stream().map(CrmSupplierDO::getCrmEnterpriseId).distinct().collect(Collectors.toList())).orElse(Lists.newArrayList());
    }

    @Override
    public List<CrmSupplierDTO> getSupplierInfoByCrmEnterId(List<Long> crmEnterIds) {
        if(crmEnterIds ==null || crmEnterIds.size()==0){
            return Lists.newArrayList();
        }
        QueryWrapper<CrmSupplierDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CrmSupplierDO::getDelFlag,0).in(CrmSupplierDO::getCrmEnterpriseId, crmEnterIds);
        return PojoUtils.map(list(queryWrapper),CrmSupplierDTO.class);
    }

    @Override
    public CrmSupplierDO getCrmSupplierByCrmId(Long crmId) {

        LambdaQueryWrapper<CrmSupplierDO> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.eq(CrmSupplierDO::getCrmEnterpriseId, crmId).last("limit 1");

        return this.getOne(lambdaQuery);
    }

    @Override
    public List<CrmSupplierDO> getCrmSupplierByCrmIds(List<Long> crmIds) {

        LambdaQueryWrapper<CrmSupplierDO> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.in(CrmSupplierDO::getCrmEnterpriseId, crmIds);

        return this.baseMapper.selectList(lambdaQuery);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<CrmSupplierDO> listSuffixByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList, String tableSuffix) {
        LambdaQueryWrapper<CrmSupplierDO> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.in(CrmSupplierDO::getCrmEnterpriseId, crmEnterpriseIdList);
        return this.list(lambdaQuery);
    }

    @Override
    public List<Long> getCrmEnterIdListByCommerceJobNumber(String empId) {
        LambdaQueryWrapper<CrmSupplierDO> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.select(CrmSupplierDO::getCrmEnterpriseId);
        lambdaQuery.eq(CrmSupplierDO::getCommerceJobNumber, empId);
        return Optional.ofNullable(list(lambdaQuery).stream().map(CrmSupplierDO::getCrmEnterpriseId).distinct().collect(Collectors.toList())).orElse(Lists.newArrayList());
    }
}
