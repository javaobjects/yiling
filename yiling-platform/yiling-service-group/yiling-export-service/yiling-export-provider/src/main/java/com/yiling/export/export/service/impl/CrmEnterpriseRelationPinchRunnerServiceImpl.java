package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.enums.AgencySupplyChainRoleEnum;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationPinchRunnerApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.api.CrmGoodsGroupApi;
import com.yiling.dataflow.crm.api.CrmManorApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationPinchRunnerDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.dto.CrmManorDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseRelationPinchRunnerPageListRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorParamRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.export.export.bo.ExportCrmEnterpriseRelationPinchRunnerBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 代跑三者关系导出
 *
 * @author: houjie.sun
 * @date: 2023/4/23
 */
@Slf4j
@Service("crmEnterpriseRelationPinchRunnerService")
public class CrmEnterpriseRelationPinchRunnerServiceImpl implements BaseExportQueryDataService<QueryCrmEnterpriseRelationPinchRunnerPageListRequest> {

    @DubboReference
    private SjmsUserDatascopeApi sjmsUserDatascopeApi;
    @DubboReference
    CrmEnterpriseRelationPinchRunnerApi crmEnterpriseRelationPinchRunnerApi;
    @DubboReference
    CrmEnterpriseApi crmEnterpriseApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;
    @DubboReference
    CrmGoodsGroupApi crmGoodsGroupApi;
    @DubboReference
    EsbEmployeeApi esbEmployeeApi;
    @DubboReference
    CrmManorApi crmManorApi;
    @DubboReference
    CrmGoodsCategoryApi crmGoodsCategoryApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("crmEnterpriseId", "机构编码");
        FIELD.put("crmEnterpriseName", "机构名称");
        FIELD.put("crmSupplyChainRole", "供应链角色");
        FIELD.put("productGroupName", "产品组");
        FIELD.put("categoryName", "品种");
        FIELD.put("manorName", "辖区");
        FIELD.put("representativeCode", "业务代表工号");
        FIELD.put("representativeName", "业务代表姓名");
        FIELD.put("representativePostCode", "岗位代码");
        FIELD.put("representativePostName", "岗位名称");
        FIELD.put("businessSuperiorCode", "销量计入主管工号");
        FIELD.put("businessSuperiorName", "销量计入主管姓名");
        FIELD.put("businessSuperiorDepartment", "销量计入业务部门");
        FIELD.put("businessSuperiorProvince", "销量计入业务省区");
        FIELD.put("opTime", "最后操作时间");
        FIELD.put("opUser", "操作人");
    }


    @Override
    public QueryExportDataDTO queryData(QueryCrmEnterpriseRelationPinchRunnerPageListRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        List<Map<String, Object>> data = new ArrayList<>();

        // 需要循环调用
        Page<CrmEnterpriseRelationPinchRunnerDTO> page;
        log.info("代跑三者关系EXCEL模板 crmEnterpriseRelationShipExportService 查询条件为:[{}]", request);
        int current = 1;
        int size = 500;
        do {
            request.setCurrent(current);
            request.setSize(size);
            page = crmEnterpriseRelationPinchRunnerApi.page(request);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }

            // 操作人：key -> userId， value -> name
            Map<Long, String> userNameMap = getUserNameMap(page.getRecords());
            // 机构信息：key -> 机构id， value -> CrmEnterpriseDTO
            Map<Long, CrmEnterpriseDTO> crmEnterpriseMap = new HashMap<>();
            List<Long> crmEnterpriseIdList = page.getRecords().stream().filter(o -> ObjectUtil.isNotNull(o.getCrmEnterpriseId()) && o.getCrmEnterpriseId().intValue() > 0).map(CrmEnterpriseRelationPinchRunnerDTO::getCrmEnterpriseId).distinct().collect(Collectors.toList());
            List<CrmEnterpriseDTO> crmEnterpriseList = crmEnterpriseApi.getCrmEnterpriseListById(crmEnterpriseIdList);
            if (MapUtil.isNotEmpty(crmEnterpriseMap)) {
                crmEnterpriseMap = crmEnterpriseList.stream().collect(Collectors.toMap(CrmEnterpriseDTO::getId, Function.identity()));
            }
            // 三者关系，key -> 三者关系id，value -> CrmEnterpriseRelationShipDTO
            Map<Long, CrmEnterpriseRelationShipDTO> crmEnterpriseRelationShipMap = new HashMap<>();
            List<Long> enterpriseCersIdList = page.getRecords().stream().filter(o -> ObjectUtil.isNotNull(o.getEnterpriseCersId()) && o.getEnterpriseCersId().intValue() > 0).map(CrmEnterpriseRelationPinchRunnerDTO::getEnterpriseCersId).distinct().collect(Collectors.toList());
            List<CrmEnterpriseRelationShipDTO> crmEnterpriseRelationShipList = crmEnterpriseRelationShipApi.listSuffixByIdList(enterpriseCersIdList, null);
            if (CollUtil.isNotEmpty(crmEnterpriseRelationShipList)) {
                crmEnterpriseRelationShipMap = crmEnterpriseRelationShipList.stream().collect(Collectors.toMap(CrmEnterpriseRelationShipDTO::getId, Function.identity()));
            }
            // 产品组：key -> 三者关系id，value -> productGroupName
            Map<Long, String> productGroupMap = getProductGroupMap(crmEnterpriseRelationShipList);
            // 岗位，key -> jobId，value -> EsbEmployeeDTO
            List<Long> superiorPostCodeList = page.getRecords().stream().filter(o -> ObjectUtil.isNotNull(o.getBusinessSuperiorPostCode()) && o.getBusinessSuperiorPostCode().intValue() > 0).map(CrmEnterpriseRelationPinchRunnerDTO::getBusinessSuperiorPostCode).distinct().collect(Collectors.toList());
            Map<Long, EsbEmployeeDTO> esbEmployeeMap = getEsbEmployeeMap(superiorPostCodeList, crmEnterpriseRelationShipList);

            // 品种
            Map<Long, String> categoryMap = new HashMap<>();
            List<Long> categoryIds = page.getRecords().stream().map(CrmEnterpriseRelationPinchRunnerDTO::getCategoryId).collect(Collectors.toList());
            List<CrmGoodsCategoryDTO> categoryList = crmGoodsCategoryApi.findByIds(categoryIds);
            if (CollUtil.isNotEmpty(categoryList)) {
                categoryMap = categoryList.stream().collect(Collectors.toMap(CrmGoodsCategoryDTO::getId, o -> o.getName(), (k1, k2) -> k1));
            }
            // 辖区
            Map<Long, String> manorMap = new HashMap<>();
            List<Long> manorIds = page.getRecords().stream().map(CrmEnterpriseRelationPinchRunnerDTO::getManorId).collect(Collectors.toList());
            QueryCrmManorParamRequest manorRequest= new QueryCrmManorParamRequest();
            manorRequest.setIdList(manorIds);
            List<CrmManorDTO> manorList =crmManorApi.listByParam(manorRequest);
            if (CollUtil.isNotEmpty(manorList)) {
                manorMap = manorList.stream().collect(Collectors.toMap(CrmManorDTO::getId, o -> o.getName(), (k1, k2) -> k1));
            }

            for (CrmEnterpriseRelationPinchRunnerDTO record : page.getRecords()) {
                ExportCrmEnterpriseRelationPinchRunnerBO pinchRunnerBO = PojoUtils.map(record, ExportCrmEnterpriseRelationPinchRunnerBO.class);
                pinchRunnerBO.setBusinessSuperiorPostCode(record.getBusinessSuperiorPostCode());
                pinchRunnerBO.setCrmSupplyChainRole(getSupplyChainRole(record.getCrmSupplyChainRole()));
                // 操作人、操作时间
                buildOperateInfo(record, pinchRunnerBO, userNameMap);
                // 产品组信息
                String goodsGroupName = productGroupMap.get(record.getEnterpriseCersId());
                pinchRunnerBO.setProductGroupName(goodsGroupName);

                // 主管岗位信息
                Long superiorPostCode = record.getBusinessSuperiorPostCode();
                buildBusinessSuperiorInfo(pinchRunnerBO, superiorPostCode, esbEmployeeMap);

                // 三者关系信息
                CrmEnterpriseRelationShipDTO crmEnterpriseRelationShipDTO = crmEnterpriseRelationShipMap.get(record.getEnterpriseCersId());
                if (ObjectUtil.isNotNull(crmEnterpriseRelationShipDTO)) {
                    // 业务代表主管岗位信息
                    Long postCode = crmEnterpriseRelationShipDTO.getPostCode();
                    buildBusinessRepresentative(pinchRunnerBO, postCode, esbEmployeeMap);
                }

                // 品种
                pinchRunnerBO.setCategoryName(Optional.ofNullable(categoryMap.get(record.getCategoryId())).orElse(""));
                // 辖区
                pinchRunnerBO.setManorName(Optional.ofNullable(manorMap.get(record.getManorId())).orElse(""));

                data.add(BeanUtil.beanToMap(pinchRunnerBO));
            }

            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("代跑三者关系导出");
        // 页签字段
        exportDataDTO.setFieldMap(FIELD);
        // 页签数据
        exportDataDTO.setData(data);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }

    @Override
    public QueryCrmEnterpriseRelationPinchRunnerPageListRequest getParam(Map<String, Object> map) {
        QueryCrmEnterpriseRelationPinchRunnerPageListRequest request = PojoUtils.map(map, QueryCrmEnterpriseRelationPinchRunnerPageListRequest.class);
        // 数据权限
        String empId = map.getOrDefault("currentUserCode", "").toString();
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(empId);
        log.info("代跑三者关系管理, 导出, 当前用户数据权限:empId={}, userDatascopeBO={}", empId, JSONUtil.toJsonStr(userDatascopeBO));
        checkUserDatascope(userDatascopeBO);
        request.setUserDatascopeBO(userDatascopeBO);
        return request;
    }

    private void buildBusinessRepresentative(ExportCrmEnterpriseRelationPinchRunnerBO pinchRunnerBO, Long postCode, Map<Long, EsbEmployeeDTO> esbEmployeeMap) {
        if (ObjectUtil.isNull(postCode) || 0 == postCode.intValue()) {
            return;
        }
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeMap.get(postCode);
        if (ObjectUtil.isNull(esbEmployeeDTO)) {
            return;
        }
        // 业务代表工号、业务代表姓名、业务代表岗位代码、业务代表岗位名称
        pinchRunnerBO.setRepresentativeCode(esbEmployeeDTO.getEmpId());
        pinchRunnerBO.setRepresentativeName(esbEmployeeDTO.getEmpName());
        pinchRunnerBO.setRepresentativePostCode(postCode);
        pinchRunnerBO.setRepresentativePostName(esbEmployeeDTO.getJobName());
    }

    private void buildBusinessSuperiorInfo(ExportCrmEnterpriseRelationPinchRunnerBO pinchRunnerBO, Long superiorPostCode, Map<Long, EsbEmployeeDTO> esbEmployeeMap) {
        if (ObjectUtil.isNull(superiorPostCode) || 0 == superiorPostCode.intValue()) {
            return;
        }
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeMap.get(superiorPostCode);
        if (ObjectUtil.isNull(esbEmployeeDTO)) {
            return;
        }
        // 销量计入主管岗位名称、销量计入主管工号、销量计入主管姓名、量计入业务部门、销量计入业务省区
        pinchRunnerBO.setBusinessSuperiorPostName(esbEmployeeDTO.getJobName());
        pinchRunnerBO.setBusinessSuperiorCode(esbEmployeeDTO.getEmpId());
        pinchRunnerBO.setBusinessSuperiorName(esbEmployeeDTO.getEmpName());
        pinchRunnerBO.setBusinessSuperiorDepartment(esbEmployeeDTO.getYxDept());
        pinchRunnerBO.setBusinessSuperiorProvince(esbEmployeeDTO.getYxProvince());
    }

    private void buildOperateInfo(CrmEnterpriseRelationPinchRunnerDTO record, ExportCrmEnterpriseRelationPinchRunnerBO pinchRunnerBO, Map<Long, String> userNameMap) {
        // 操作人
        Long createUser = record.getCreateUser();
        Long updateUser = record.getUpdateUser();
        Long opUser;
        if (ObjectUtil.isNull(updateUser) || 0 == updateUser.intValue()) {
            opUser = createUser;
        } else {
            opUser = updateUser;
        }
        String name = userNameMap.get(opUser);
        pinchRunnerBO.setOpUser(name);

        // 操作时间
        Date createTime = record.getCreateTime();
        Date updateTime = record.getUpdateTime();
        Date opTime;
        if (ObjectUtil.isNull(updateTime) || ObjectUtil.equal("1970-01-01 00:00:00", DateUtil.format(updateTime, "yyyy-MM-dd HH:mm:ss"))) {
            opTime = createTime;
        } else {
            opTime = updateTime;
        }
        pinchRunnerBO.setOpTime(opTime);
    }

    private Map<Long, EsbEmployeeDTO> getEsbEmployeeMap(List<Long> superiorPostCodeList, List<CrmEnterpriseRelationShipDTO> crmEnterpriseRelationShipList) {
        Map<Long, EsbEmployeeDTO> esbEmployeeMap = new HashMap<>();
        if (CollUtil.isEmpty(crmEnterpriseRelationShipList)) {
            return esbEmployeeMap;
        }
        List<Long> jobIds = new ArrayList<>();
        jobIds.addAll(superiorPostCodeList);
        // 业务代表岗位代码
        if (CollUtil.isNotEmpty(crmEnterpriseRelationShipList)) {
            List<Long> postCodeList = crmEnterpriseRelationShipList.stream().filter(o -> ObjectUtil.isNotNull(o.getPostCode()) && o.getPostCode().intValue() > 0).map(CrmEnterpriseRelationShipDTO::getPostCode).distinct().collect(Collectors.toList());
            jobIds.addAll(postCodeList);
        }

        List<EsbEmployeeDTO> esbEmployeeDTOS = esbEmployeeApi.listByJobIds(jobIds);
        if (CollUtil.isNotEmpty(esbEmployeeDTOS)) {
            esbEmployeeMap = esbEmployeeDTOS.stream().collect(Collectors.toMap(o -> o.getJobId(), o -> o, (k1, k2) -> k1));
        }
        return esbEmployeeMap;
    }

    private Map<Long, String> getProductGroupMap(List<CrmEnterpriseRelationShipDTO> crmEnterpriseRelationShipList) {
        Map<Long, String> productGroupMap = new HashMap<>();
        if (CollUtil.isEmpty(crmEnterpriseRelationShipList)) {
            return productGroupMap;
        }
        List<Long> productGroupIdList = crmEnterpriseRelationShipList.stream().filter(o -> ObjectUtil.isNotNull(o.getProductGroupId()) && o.getProductGroupId() > 0).map(CrmEnterpriseRelationShipDTO::getProductGroupId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(productGroupIdList)) {
            // 产品组
            List<CrmGoodsGroupDTO> productGroupList = crmGoodsGroupApi.findGroupByIds(productGroupIdList);
            if (CollUtil.isNotEmpty(productGroupList)) {
                Map<Long, CrmGoodsGroupDTO> productGroupMapTemp = productGroupList.stream().collect(Collectors.toMap(CrmGoodsGroupDTO::getId, Function.identity()));
                crmEnterpriseRelationShipList.forEach(o -> {
                    CrmGoodsGroupDTO crmGoodsGroupDTO = productGroupMapTemp.get(o.getProductGroupId());
                    if (ObjectUtil.isNotNull(crmGoodsGroupDTO)) {
                        productGroupMap.put(o.getId(), crmGoodsGroupDTO.getName());
                    }
                });
            }
        }
        return productGroupMap;
    }

    private Map<Long, String> getUserNameMap(List<CrmEnterpriseRelationPinchRunnerDTO> list) {
        Map<Long, String> userNameMap = new HashMap<>();
        Set<Long> opUserIdSet = new HashSet<>();
        List<Long> createUserIdList = list.stream().filter(o -> ObjectUtil.isNotNull(o.getCreateUser()) && o.getCreateUser() > 0).map(CrmEnterpriseRelationPinchRunnerDTO::getCreateUser).distinct().collect(Collectors.toList());
        List<Long> updateUserIdList = list.stream().filter(o -> ObjectUtil.isNotNull(o.getUpdateUser()) && o.getUpdateUser() > 0).map(CrmEnterpriseRelationPinchRunnerDTO::getUpdateUser).distinct().collect(Collectors.toList());
        opUserIdSet.addAll(createUserIdList);
        if (CollUtil.isNotEmpty(updateUserIdList)) {
            opUserIdSet.addAll(updateUserIdList);
        }
        if (CollUtil.isEmpty(opUserIdSet)) {
            return userNameMap;
        }
        List<UserDTO> userList = userApi.listByIds(ListUtil.toList(opUserIdSet));
        if (CollUtil.isNotEmpty(userList)) {
            userNameMap = userList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o.getName(), (k1, k2) -> k1));
        }
        return userNameMap;
    }

    private String getSupplyChainRole(Integer supplyChainRole) {
        AgencySupplyChainRoleEnum supplyChainRoleEnum = AgencySupplyChainRoleEnum.getByCode(supplyChainRole);
        if (ObjectUtil.isNull(supplyChainRoleEnum)) {
            return "";
        }
        return supplyChainRoleEnum.getName();
    }

    private boolean checkUserDatascope(SjmsUserDatascopeBO userDatascopeBO){
        // 数据权限
        if (ObjectUtil.isNull(userDatascopeBO) || ObjectUtil.isNull(userDatascopeBO.getOrgDatascope()) || OrgDatascopeEnum.NONE.equals(OrgDatascopeEnum.getFromCode(userDatascopeBO.getOrgDatascope()))) {
            return false;
        }
        if (OrgDatascopeEnum.PORTION.equals(OrgDatascopeEnum.getFromCode(userDatascopeBO.getOrgDatascope()))) {
            SjmsUserDatascopeBO.OrgPartDatascopeBO orgPartDatascopeBO = userDatascopeBO.getOrgPartDatascopeBO();
            List<Long> crmEids = orgPartDatascopeBO.getCrmEids();
            List<String> provinceCodes = orgPartDatascopeBO.getProvinceCodes();
            if (CollUtil.isEmpty(crmEids) && CollUtil.isEmpty(provinceCodes)) {
                return false;
            }
        }
        return true;
    }

}
