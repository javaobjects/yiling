package com.yiling.dataflow.crm.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.dataflow.agency.entity.CrmDepartmentProductRelationDO;
import com.yiling.dataflow.agency.service.CrmDepartmentProductRelationService;
import com.yiling.dataflow.crm.dao.CrmEnterpriseRelationShipMapper;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseRelationShipRequest;
import com.yiling.dataflow.crm.dto.request.RemoveCrmEnterpriseRelationPinchRunnerRequest;
import com.yiling.dataflow.crm.dto.request.RemoveCrmEnterpriseRelationShipRequest;
import com.yiling.dataflow.crm.dto.request.UpdateCrmEnterpriseRelationShipRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationPinchRunnerDO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationPinchRunnerService;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.crm.service.CrmGoodsCategoryService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupService;
import com.yiling.dataflow.sale.dto.request.QueryRelationShipByPoCodeRequest;
import com.yiling.dataflow.crm.service.CrmGoodsInfoService;
import com.yiling.framework.common.annotations.DynamicName;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-09-16
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "dataflow:crmEnterpriseRelationShip")
public class CrmEnterpriseRelationShipServiceImpl extends BaseServiceImpl<CrmEnterpriseRelationShipMapper, CrmEnterpriseRelationShipDO> implements CrmEnterpriseRelationShipService {

    /**
     * 当月无销售的企业，Redis缓存key
     */
    public static final String ERP_CRM_ENTERPRISE_RELATION_SHIP = "erp_crm_enterprise_relation_ship";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CrmDepartmentProductRelationService crmDepartmentProductRelationService;

    @Autowired
    private CrmEnterpriseService crmEnterpriseService;

    @Autowired
    @Lazy
    private CrmEnterpriseRelationShipServiceImpl crmEnterpriseRelationShipService;

    @Autowired
    private CrmGoodsGroupService crmGoodsGroupService;

    @Autowired
    private CrmEnterpriseRelationPinchRunnerService crmEnterpriseRelationPinchRunnerService;

    @Autowired
    private CrmGoodsCategoryService crmGoodsCategoryService;

    @Autowired
    private CrmGoodsInfoService crmGoodsInfoService;

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @Override
    public boolean isBreathingDepartmentByNameCode(List<String> nameCode) {
        QueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CrmEnterpriseRelationShipDO::getCustomerCode, nameCode)
                .eq(CrmEnterpriseRelationShipDO::getDepartment, "事业三部(呼吸)")
                .eq(CrmEnterpriseRelationShipDO::getProductGroup, "呼吸事业部产品组");
        List<CrmEnterpriseRelationShipDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return false;
        }
        return true;
    }

    @Override
    public List<CrmEnterpriseRelationShipDTO> getCrmEnterpriseRelationShipByNameList(List<String> nameList) {
        QueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CrmEnterpriseRelationShipDO::getCustomerName, nameList);
        return PojoUtils.map(this.list(queryWrapper), CrmEnterpriseRelationShipDTO.class);
    }

    @Override
    public CrmEnterpriseRelationShipDTO getCrmEnterpriseRelationShipByGroupNameAndCustomerCode(List<String> groupNameList, String customerCode) {
        if (CollUtil.isEmpty(groupNameList) || StrUtil.isEmpty(customerCode)) {
            return null;
        }
        for (String groupName : groupNameList) {
            CrmEnterpriseRelationShipDTO crmEnterpriseRelationShipDTO = null;
            Object jsonObject = stringRedisTemplate.opsForHash().get(ERP_CRM_ENTERPRISE_RELATION_SHIP, groupName + "-" + customerCode);
            if (jsonObject != null) {
                crmEnterpriseRelationShipDTO = JSON.parseObject(String.valueOf(jsonObject), CrmEnterpriseRelationShipDTO.class);
                return crmEnterpriseRelationShipDTO;
            } else {
                QueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(CrmEnterpriseRelationShipDO::getCustomerCode, customerCode).eq(CrmEnterpriseRelationShipDO::getProductGroup, groupName);
                queryWrapper.last("limit 1");
                crmEnterpriseRelationShipDTO = PojoUtils.map(this.getOne(queryWrapper), CrmEnterpriseRelationShipDTO.class);

                if (crmEnterpriseRelationShipDTO != null) {
                    stringRedisTemplate.opsForHash().put(ERP_CRM_ENTERPRISE_RELATION_SHIP, groupName + "-" + customerCode, JSON.toJSONString(crmEnterpriseRelationShipDTO));
                    return crmEnterpriseRelationShipDTO;
                }
            }
        }
        return null;
    }

    @Override
    public Page<CrmEnterpriseRelationShipDTO> getCrmEnterpriseRelationShipPage(QueryCrmEnterpriseRelationShipRequest request) {
        QueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new QueryWrapper<>();
        Page<CrmEnterpriseRelationShipDO> page = new Page<>(request.getCurrent(), request.getSize());
        return PojoUtils.map(this.page(page, queryWrapper), CrmEnterpriseRelationShipDTO.class);
    }

    @Override
    public Boolean remove(RemoveCrmEnterpriseRelationShipRequest request) {
        CrmEnterpriseRelationShipDO crmEnterpriseRelationShipDO = PojoUtils.map(request, CrmEnterpriseRelationShipDO.class);
        //同时删除代跑关系
        Boolean deleteRelationFlag = this.deleteByIdWithFill(crmEnterpriseRelationShipDO) > 0;
        Boolean deletePinchFlag = true;
        Integer substituteRunning = crmEnterpriseRelationShipDO.getSubstituteRunning();
        if (ObjectUtil.isNotNull(substituteRunning) && substituteRunning == 2) {
            RemoveCrmEnterpriseRelationPinchRunnerRequest pinchRunnerRequest = new RemoveCrmEnterpriseRelationPinchRunnerRequest();
            pinchRunnerRequest.setOpUserId(request.getOpUserId());
            pinchRunnerRequest.setEnterpriseCersIdList(Arrays.asList(crmEnterpriseRelationShipDO.getId()));
            deletePinchFlag = crmEnterpriseRelationPinchRunnerService.removeByEnterpriseCersId(pinchRunnerRequest) > 0;
        }
        return deleteRelationFlag && deletePinchFlag;
    }


    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<CrmEnterpriseRelationShipDO> listSuffixByIdList(List<Long> idList, String tableSuffix) {
        LambdaQueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CrmEnterpriseRelationShipDO::getId, idList);
        List<CrmEnterpriseRelationShipDO> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(item -> {
                if (StringUtils.equals(item.getDutyGredeId(), "2")) {
                    item.setPostCode(null);
                }
            });
        }
        return list;
    }

    @Override
    public Long listSuffixByCrmEnterpriseIdList(List<Long> idList, Long crmEnterpriseId, String tableSuffix) {
        if (CollectionUtil.isEmpty(idList) || ObjectUtil.isNull(crmEnterpriseId) || StringUtils.isEmpty(tableSuffix)) {
            return 0L;
        }
        List<CrmDepartmentProductRelationDO> departmentProductRelationDOList = crmDepartmentProductRelationService.getByIds(idList, tableSuffix);
        if (CollectionUtil.isNotEmpty(departmentProductRelationDOList)) {
            List<Long> productGroupIds = departmentProductRelationDOList.stream().map(CrmDepartmentProductRelationDO::getProductGroupId).collect(Collectors.toList());
            List<CrmEnterpriseRelationShipDO> relationShipDOS = crmEnterpriseRelationShipService.listSuffixByNameList(productGroupIds, crmEnterpriseId, tableSuffix);
            if (CollectionUtil.isNotEmpty(relationShipDOS)) {
                return relationShipDOS.get(0).getId();
            }
        }
        return 0L;
    }

    @Override
    public Long listSuffixByOrgIdAndGoodsCode(Long goodsCode, Long orgId, String tableSuffix) {
        if (ObjectUtil.isEmpty(goodsCode) || ObjectUtil.isNull(orgId) || StringUtils.isEmpty(tableSuffix)) {
            return 0L;
        }
        CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getSuffixByCrmEnterpriseId(orgId, tableSuffix);
        if (crmEnterpriseDO == null || crmEnterpriseDO.getSupplyChainRole() == 0) {
            return 0L;
        }
        if (crmEnterpriseDO.getSupplyChainRole() == 2) {
            List<CrmGoodsInfoDTO> crmGoodsInfoDTOList = crmGoodsInfoService.findBakByCodeList(Arrays.asList(goodsCode), tableSuffix);
            if (CollUtil.isNotEmpty(crmGoodsInfoDTOList)) {
                Long categoryId = crmGoodsCategoryService.findFirstCategoryByFinal(crmGoodsInfoDTOList.get(0).getCategoryId(), tableSuffix);
                if(categoryId!=null&&categoryId!=0){
                    CrmEnterpriseRelationShipDO crmEnterpriseRelationShipDO=this.listSuffixByCategoryId(categoryId,orgId,tableSuffix);
                    if(crmEnterpriseRelationShipDO!=null){
                        return crmEnterpriseRelationShipDO.getId();
                    }
                }
            }
        }

        List<Long> groupIdList = crmGoodsGroupService.findBakCrmDepartProductByGoodsCode(goodsCode, tableSuffix);
        if(CollUtil.isEmpty(groupIdList)){
            return 0L;
        }
        return listSuffixByCrmEnterpriseIdList(groupIdList,orgId,tableSuffix);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<CrmEnterpriseRelationShipDO> listSuffixByNameList(List<Long> productGroupIds, Long id, String tableSuffix) {
        LambdaQueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CrmEnterpriseRelationShipDO::getProductGroupId, productGroupIds);
        queryWrapper.eq(CrmEnterpriseRelationShipDO::getCrmEnterpriseId, id);
        return this.list(queryWrapper);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public CrmEnterpriseRelationShipDO listSuffixByCategoryId(Long categoryId, Long id, String tableSuffix) {
        LambdaQueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CrmEnterpriseRelationShipDO::getCategoryId, categoryId);
        queryWrapper.eq(CrmEnterpriseRelationShipDO::getCrmEnterpriseId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public Boolean updateBackUpBatchById(List<CrmEnterpriseRelationShipDO> relationShipDOS, String tableSuffix) {
        if (CollectionUtil.isNotEmpty(relationShipDOS)) {
            log.info("更新备份表数据,第一个" + relationShipDOS.get(0));
            return this.updateBatchById(relationShipDOS);
        }
        return false;
    }

    @Override
    public List<Long> getCrmEnterprisePostCode() {
        return this.baseMapper.getCrmEnterprisePostCode();
    }

    @Override
    public boolean updateBackUpBatchByPostCode(UpdateCrmEnterpriseRelationShipRequest relationShipRequest, String tableSuffix) {
        return this.baseMapper.updateBackUpBatchByPostCode(relationShipRequest, tableSuffix);
    }

    @Override
    public List<Long> getCrmEnterIdListByPostCode(String postCode) {
        LambdaQueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(CrmEnterpriseRelationShipDO::getCrmEnterpriseId);
        queryWrapper.eq(CrmEnterpriseRelationShipDO::getPostCode, postCode);
        queryWrapper.gt(CrmEnterpriseRelationShipDO::getCrmEnterpriseId, 0);
        return Optional.ofNullable(list(queryWrapper).stream().map(CrmEnterpriseRelationShipDO::getCrmEnterpriseId).distinct().collect(Collectors.toList())).orElse(Lists.newArrayList());
    }

    @Override
    public Long listByCrmEnterpriseIdList(List<Long> idList, Long crmEnterpriseId) {
        if (CollectionUtil.isEmpty(idList) || ObjectUtil.isNull(crmEnterpriseId)) {
            return 0L;
        }
        List<CrmDepartmentProductRelationDO> departmentProductRelationDOList = crmDepartmentProductRelationService.getByIdList(idList);
        if (CollectionUtil.isNotEmpty(departmentProductRelationDOList)) {
            List<Long> productGroupIds = departmentProductRelationDOList.stream().map(CrmDepartmentProductRelationDO::getProductGroupId).collect(Collectors.toList());
            List<CrmEnterpriseRelationShipDO> relationShipDOS = crmEnterpriseRelationShipService.listByProductGroupIdsList(productGroupIds, crmEnterpriseId);
            if (CollectionUtil.isNotEmpty(relationShipDOS)) {
                return relationShipDOS.get(0).getId();
            }
        }
        return 0L;
    }

    @Override
    public Map<Long, Boolean> getUseByDepartRelationIds(List<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return new HashMap<>();
        }
        List<CrmDepartmentProductRelationDO> list = crmDepartmentProductRelationService.listByIds(ids);
        if (CollectionUtil.isEmpty(list)) {
            return new HashMap<>();
        }
        List<Long> groupIds = list.stream().map(CrmDepartmentProductRelationDO::getProductGroupId).distinct().collect(Collectors.toList());
        List<Long> departIds = list.stream().map(CrmDepartmentProductRelationDO::getDepartmentId).distinct().collect(Collectors.toList());
        Long groupId = groupIds.get(0);
        Map<Long, Boolean> result = new HashMap<>();
        if (groupId.equals(0L)) {
            ids.forEach(item -> {
                result.put(item, false);
            });
            return result;
        }
        // 获取三者关系表里面产品组id对应的岗位id，在获取部门id
        List<Long> postCodeIds = this.baseMapper.getByGroupIdS(groupId);
        Map<Long, List<EsbEmployeeDTO>> objectObjectHashMap = new HashMap<>();
        departIds.forEach(item -> {
            List<EsbEmployeeDTO> esbEmployeeDTOS = esbEmployeeApi.listByDeptId(item);
            objectObjectHashMap.put(item, esbEmployeeDTOS);
        });
        list.forEach(item -> {
            List<EsbEmployeeDTO> esbEmployeeDTOS = objectObjectHashMap.get(item.getDepartmentId());
            if (CollectionUtil.isNotEmpty(esbEmployeeDTOS)) {
                //找到deptid对应的岗位id集合，和三者关系对应的岗位id求交际
                List<Long> jobIds = esbEmployeeDTOS.stream().map(EsbEmployeeDTO::getJobId).collect(Collectors.toList());
                if (CollectionUtils.containsAny(jobIds, postCodeIds)) {
                    result.put(item.getId(), true);
                } else {
                    result.put(item.getId(), false);
                }
            } else {
                result.put(item.getId(), false);
            }

        });
        return result;
    }

    @Override
    public List<CrmEnterpriseRelationShipDTO> getCrmEnterpriseRelationShipByCrmenterpriseIdList(List<Long> ids) {
        LambdaQueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CrmEnterpriseRelationShipDO::getCrmEnterpriseId, ids);
        List<CrmEnterpriseRelationShipDO> list = this.list(queryWrapper);
        // 用产品组id获取产品组名称,并赋值
        if (CollectionUtil.isNotEmpty(list)) {
            List<Long> productGroupIds = list.stream().map(CrmEnterpriseRelationShipDO::getProductGroupId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(productGroupIds)) {
                Map<Long, String> groupMap = new HashMap<>();
                List<CrmGoodsGroupDTO> group = crmGoodsGroupService.findGroupByIds(productGroupIds);
                if (CollectionUtil.isNotEmpty(group)) {
                    groupMap = group.stream().collect(Collectors.toMap(CrmGoodsGroupDTO::getId, CrmGoodsGroupDTO::getName));
                }
                Map<Long, String> finalGroupMap = groupMap;
                list.forEach(item -> {
                    if (StringUtils.isNotEmpty(finalGroupMap.get(item.getProductGroupId()))) {
                        item.setProductGroup(finalGroupMap.get(item.getProductGroupId()));
                    }
                });
            }
        }
        return PojoUtils.map(list, CrmEnterpriseRelationShipDTO.class);
    }

    @Override
    public List<CrmEnterpriseRelationShipDO> listByProductGroupIdsList(List<Long> productGroupId, Long id) {
        LambdaQueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CrmEnterpriseRelationShipDO::getProductGroupId, productGroupId);
        queryWrapper.eq(CrmEnterpriseRelationShipDO::getCrmEnterpriseId, id);
        return this.list(queryWrapper);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<CrmEnterpriseRelationShipDO> listByPostCodeList(List<Long> postCode, String tableSuffix) {
        LambdaQueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CrmEnterpriseRelationShipDO::getPostCode, postCode);
        return this.list(queryWrapper);
    }

    @DynamicName(spel = "#tableSuffix")
    @Override
    public Page<CrmEnterpriseRelationShipDO> pageByPostCodeList(QueryRelationShipByPoCodeRequest request, String tableSuffix) {
        LambdaQueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CrmEnterpriseRelationShipDO::getPostCode, request.getPostCodeList());

        return page(request.getPage(),queryWrapper);
    }

    @Override
    public Boolean getExistByCrmEnterpriseId(Long crmEnterpriseId) {
        LambdaQueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CrmEnterpriseRelationShipDO::getCrmEnterpriseId, crmEnterpriseId).last("limit 1");
        CrmEnterpriseRelationShipDO relationShipDO = crmEnterpriseRelationShipService.getOne(queryWrapper);
        if (Objects.nonNull(relationShipDO)) {
            return true;
        }
        return false;
    }


    @Override
    public int batchDeleteWithCrmEnterIds(List<Long> cEnIdList, Long opUserId, String message, Long manorId) {
        log.info("辖区删除三者关系:cEnIdList:{},manorId:{},opUserId:{}",cEnIdList,manorId,opUserId);
        if (CollUtil.isEmpty(cEnIdList)||ObjectUtil.isEmpty(manorId)||manorId.longValue()==0) {
            return 0;
        }
        //删除带跑三者关系
        QueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper1 = new QueryWrapper();
        queryWrapper1.lambda().select(CrmEnterpriseRelationShipDO::getId);
        queryWrapper1.lambda().in(CrmEnterpriseRelationShipDO::getCrmEnterpriseId, cEnIdList);
        queryWrapper1.lambda().eq(CrmEnterpriseRelationShipDO::getManorId, manorId);
        List<CrmEnterpriseRelationShipDO> relationShipDOS = baseMapper.selectList(queryWrapper1);
        List<Long> relIds=Optional.ofNullable(relationShipDOS.stream().map(CrmEnterpriseRelationShipDO::getId).collect(Collectors.toList())).orElse(ListUtil.empty());
        if(CollUtil.isNotEmpty(relIds)){
            CrmEnterpriseRelationPinchRunnerDO deleteDO1 = new CrmEnterpriseRelationPinchRunnerDO();
            deleteDO1.setRemark(message);
            deleteDO1.setOpUserId(opUserId);
            QueryWrapper<CrmEnterpriseRelationPinchRunnerDO> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.lambda().in(CrmEnterpriseRelationPinchRunnerDO::getEnterpriseCersId, relIds);
            crmEnterpriseRelationPinchRunnerService.batchDeleteWithFill(deleteDO1, queryWrapper2);
        }
        QueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().in(CrmEnterpriseRelationShipDO::getCrmEnterpriseId, cEnIdList);
        queryWrapper.lambda().eq(CrmEnterpriseRelationShipDO::getManorId, manorId);
        CrmEnterpriseRelationShipDO deleteDO = new CrmEnterpriseRelationShipDO();
        deleteDO.setRemark(message);
        deleteDO.setOpUserId(opUserId);
        return baseMapper.batchDeleteWithFill(deleteDO, queryWrapper);
    }

    @Override
    public List<CrmEnterpriseRelationShipDO> listByEidsAndRole(List<Long> crmEIds, int supplyChainRole) {
        if (CollUtil.isEmpty(crmEIds)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CrmEnterpriseRelationShipDO::getCrmEnterpriseId, crmEIds);
        queryWrapper.eq(CrmEnterpriseRelationShipDO::getSupplyChainRoleType, supplyChainRole);
        return this.list(queryWrapper);
    }


    @Override
    public void updateBackUpBatchForHospital(String tableSuffix) {
        this.baseMapper.updateBackUpBatchForHospital(tableSuffix);
    }
}
