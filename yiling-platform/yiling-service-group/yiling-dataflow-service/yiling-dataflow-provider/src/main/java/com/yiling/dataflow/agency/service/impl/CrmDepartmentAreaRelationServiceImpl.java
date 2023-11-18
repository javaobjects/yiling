package com.yiling.dataflow.agency.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.agency.dao.CrmDepartmentAreaRelationMapper;
import com.yiling.dataflow.agency.entity.CrmDepartmentAreaRelationDO;
import com.yiling.dataflow.agency.entity.CrmDepartmentProductRelationDO;
import com.yiling.dataflow.agency.service.CrmDepartmentAreaRelationService;
import com.yiling.dataflow.agency.service.CrmDepartmentProductRelationService;
import com.yiling.dataflow.crm.dto.CrmDepartmentAreaRelationDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.framework.common.annotations.DynamicName;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 省区与业务省区对应关系表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-16
 */
@Service
public class CrmDepartmentAreaRelationServiceImpl extends BaseServiceImpl<CrmDepartmentAreaRelationMapper, CrmDepartmentAreaRelationDO> implements CrmDepartmentAreaRelationService {

    @Autowired
    private CrmDepartmentProductRelationService productRelationService;

    @Override
    public String getProvinceAreaByThreeParms(String yxDept, String yxProvince) {
        QueryWrapper<CrmDepartmentAreaRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CrmDepartmentAreaRelationDO::getDepartmentBusiness, yxDept).eq(CrmDepartmentAreaRelationDO::getProvincialAreaBusiness, yxProvince).last("limit 1");
        CrmDepartmentAreaRelationDO relationDO = this.getOne(queryWrapper);
        return ObjectUtil.isEmpty(relationDO) ? "" : relationDO.getProvincialArea();
    }

    @Override
    public List<CrmDepartmentAreaRelationDTO> getGoodsGroup(String ywbm, Integer supplyChainRole) {
        QueryWrapper<CrmDepartmentProductRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CrmDepartmentProductRelationDO::getDepartment, ywbm);
        //dih-243 去掉供应链角色，作为查询条件,产品组只跟业务部门有关系。
        //.eq(CrmDepartmentProductRelationDO::getSupplyChainRole,supplyChainRole);
        List<CrmDepartmentProductRelationDO> result = productRelationService.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(result)) {
            // 找到生效的产品组信息
            List<Long> productGroupIds = result.stream().map(CrmDepartmentProductRelationDO::getProductGroupId).distinct().collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(productGroupIds)) {
                List<Long> availableProductGroupIds = this.baseMapper.getAvailableProductGroupIds(productGroupIds);
                if (CollectionUtil.isNotEmpty(availableProductGroupIds)) {
                    result = result.stream().filter(item -> availableProductGroupIds.contains(item.getProductGroupId())).collect(Collectors.toList());
                }else {
                    return PojoUtils.map(new ArrayList<CrmDepartmentProductRelationDO>(), CrmDepartmentAreaRelationDTO.class);
                }
            }
        }
        return PojoUtils.map(result, CrmDepartmentAreaRelationDTO.class);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public String getBackUpProvinceAreaByThreeParms(String yxDept, String yxProvince, String tableSuffix) {
        QueryWrapper<CrmDepartmentAreaRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CrmDepartmentAreaRelationDO::getDepartmentBusiness, yxDept).eq(CrmDepartmentAreaRelationDO::getProvincialAreaBusiness, yxProvince).last("limit 1");
        CrmDepartmentAreaRelationDO relationDO = this.getOne(queryWrapper);
        return ObjectUtil.isEmpty(relationDO) ? "" : relationDO.getProvincialArea();
    }
}
