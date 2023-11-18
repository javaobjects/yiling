package com.yiling.export.export.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.ListUtil;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.yiling.dataflow.agency.api.CrmDepartProductGroupApi;
import com.yiling.dataflow.agency.api.CrmPharmacyApi;
import com.yiling.dataflow.agency.dto.CrmDepartmentProductRelationDTO;
import com.yiling.dataflow.agency.dto.CrmPharmacyDTO;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyPageListRequest;
import com.yiling.dataflow.agency.dto.request.QueryDepartProductGroupByUploadNameRequest;
import com.yiling.dataflow.agency.enums.CrmPharmacyAttributeEnum;
import com.yiling.dataflow.agency.enums.CrmPharmacyLabelAttributeEnum;
import com.yiling.dataflow.agency.enums.CrmPharmacyLevelEnum;
import com.yiling.dataflow.agency.enums.CrmPharmacyMedicalInsuranceEnum;
import com.yiling.dataflow.agency.enums.CrmPharmacyTypeEnum;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationPostDTO;
import com.yiling.export.export.bo.AgencyDepartGrroupResolve;
import com.yiling.export.export.bo.ExportCrmPharmacyBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 零售机构档案excel导出
 *
 * @author: yong.zhang
 * @date: 2023/2/17 0017
 */
@Slf4j
@Service("crmPharmacyExportService")
public class CrmPharmacyExportServiceImpl implements BaseExportQueryDataService<QueryCrmAgencyPageListRequest> {

    @DubboReference
    CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    CrmPharmacyApi crmPharmacyApi;

    @DubboReference
    private CrmDepartProductGroupApi crmDepartProductGroupApi;

    @DubboReference
    private CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;
    @DubboReference
    private SjmsUserDatascopeApi sjmsUserDatascopeApi;
    private final static LinkedList<String> FIELD = new LinkedList<>();
    private final static Map<String, List<AgencyDepartGrroupResolve>> agencyDepartGroupResolveMap = new HashMap<>();
    private final static LinkedList<String> PRODUCT_GROUP = new LinkedList<>();

    static {
        FIELD.add("code");//客户代码
        FIELD.add("crmCode");//CRM编码
        FIELD.add("ylCode");//以岭编码
        FIELD.add("name");//客户名称
        FIELD.add("businessCode");//状态名称
        FIELD.add("provinceName");//省份名称
        FIELD.add("regionName");//区县名称
        FIELD.add("address");//地址
        FIELD.add("postalCode");//邮编
        FIELD.add("phone");//办公电话
        FIELD.add("fax");//传真
        FIELD.add("labelAttribute");//标签属性
        FIELD.add("storeManager");//店经理
        FIELD.add("turnover");//药店营业额
        FIELD.add("ylAnnualSales");//以岭年销售额
        FIELD.add("businessArea");//营业面积
        FIELD.add("agreement");//是否协议
        FIELD.add("pharmacyAttribute");//客户属性
        FIELD.add("pharmacyType");//连锁属性 -- 药店类型 1-直营；2-加盟
        FIELD.add("pharmacyLevel");//药店级别
        FIELD.add("parentCompanyCode");//上级公司编码
        FIELD.add("parentCompanyName");//上级公司名称
        FIELD.add("countyCustomerCode");//县域客户编码
        FIELD.add("countyCustomerName");//县域客户名称
        FIELD.add("parity");//是否平价
        FIELD.add("medicalInsurance");//是否医保
        FIELD.add("baseMedicineFlag");//是否基药终端
        FIELD.add("mnemonicCode");//助记码
        FIELD.add("contractFlag");//是否承包
        FIELD.add("flowJobNumber");//流向打取人工号
        FIELD.add("flowLiablePerson");//流向打取人姓名
        FIELD.add("residentCommissionerCode");//驻商专员工号
        FIELD.add("residentCommissionerName");//驻商专员姓名

        FIELD.add("ag");
        FIELD.add("ah");
        FIELD.add("ai");
        FIELD.add("aj");
        FIELD.add("ak");
        FIELD.add("al");
        FIELD.add("am");
        FIELD.add("an");
        FIELD.add("ao");
        FIELD.add("ap");
        FIELD.add("aq");
        FIELD.add("ar");
        FIELD.add("as");
        FIELD.add("at");
        FIELD.add("au");
        FIELD.add("av");
        FIELD.add("aw");
        FIELD.add("ax");
        FIELD.add("ay");
        FIELD.add("az");
        FIELD.add("ba");
        FIELD.add("bb");
        FIELD.add("bc");
        FIELD.add("bd");
        FIELD.add("be");

        PRODUCT_GROUP.add("零售部标准产品组");
        PRODUCT_GROUP.add("零售部标准产品组(不含双花)");
        PRODUCT_GROUP.add("零售部连花颗粒产品组");
        PRODUCT_GROUP.add("零售部连花清咳产品组");
        PRODUCT_GROUP.add("数字化药店终端产品组");
    }

    static {
        List<AgencyDepartGrroupResolve> g1 = Arrays.asList(AgencyDepartGrroupResolve.builder().tagetFiled("ag").sourceFiled("targetFlag").build(), AgencyDepartGrroupResolve.builder().tagetFiled("ah").sourceFiled("yxArea").build(), AgencyDepartGrroupResolve.builder().tagetFiled("ai").sourceFiled("empId").build(), AgencyDepartGrroupResolve.builder().tagetFiled("aj").sourceFiled("empName").build());
        List<AgencyDepartGrroupResolve> g2 = Arrays.asList(AgencyDepartGrroupResolve.builder().tagetFiled("al").sourceFiled("targetFlag").build(), AgencyDepartGrroupResolve.builder().tagetFiled("am").sourceFiled("yxArea").build(), AgencyDepartGrroupResolve.builder().tagetFiled("an").sourceFiled("empId").build(), AgencyDepartGrroupResolve.builder().tagetFiled("ao").sourceFiled("empName").build());
        List<AgencyDepartGrroupResolve> g3 = Arrays.asList(AgencyDepartGrroupResolve.builder().tagetFiled("aq").sourceFiled("targetFlag").build(), AgencyDepartGrroupResolve.builder().tagetFiled("ar").sourceFiled("yxArea").build(), AgencyDepartGrroupResolve.builder().tagetFiled("as").sourceFiled("empId").build(), AgencyDepartGrroupResolve.builder().tagetFiled("at").sourceFiled("empName").build());
        List<AgencyDepartGrroupResolve> g4 = Arrays.asList(AgencyDepartGrroupResolve.builder().tagetFiled("av").sourceFiled("targetFlag").build(), AgencyDepartGrroupResolve.builder().tagetFiled("aw").sourceFiled("yxArea").build(), AgencyDepartGrroupResolve.builder().tagetFiled("ax").sourceFiled("empId").build(), AgencyDepartGrroupResolve.builder().tagetFiled("ay").sourceFiled("empName").build());
        List<AgencyDepartGrroupResolve> g5 = Arrays.asList(AgencyDepartGrroupResolve.builder().tagetFiled("ba").sourceFiled("targetFlag").build(), AgencyDepartGrroupResolve.builder().tagetFiled("bb").sourceFiled("yxArea").build(), AgencyDepartGrroupResolve.builder().tagetFiled("bc").sourceFiled("empId").build(), AgencyDepartGrroupResolve.builder().tagetFiled("bd").sourceFiled("empName").build());
        agencyDepartGroupResolveMap.put("零售部标准产品组", g1);
        agencyDepartGroupResolveMap.put("零售部标准产品组(不含双花)", g2);
        agencyDepartGroupResolveMap.put("零售部连花颗粒产品组", g3);
        agencyDepartGroupResolveMap.put("零售部连花清咳产品组", g4);
        agencyDepartGroupResolveMap.put("数字化药店终端产品组", g5);
    }

    @Override
    public QueryExportDataDTO queryData(QueryCrmAgencyPageListRequest request) {

        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
       /* if(null==request.getSjmsUserDatascopeBO()){
            return setResultParam(result,data);
        }*/
        QueryDepartProductGroupByUploadNameRequest departRequest = new QueryDepartProductGroupByUploadNameRequest();
        departRequest.setUploadNames(PRODUCT_GROUP.stream().collect(Collectors.toList()));
        departRequest.setSupplyChainRole(3);
        List<CrmDepartmentProductRelationDTO> departProductGroupListByUploadName = crmDepartProductGroupApi.getDepartProductGroupListByUploadName(departRequest);
        //产品组和导出产品组对应关系
        Map<String, CrmDepartmentProductRelationDTO> departmentProductRelationDTOMap = departProductGroupListByUploadName.stream().collect(Collectors.toMap(CrmDepartmentProductRelationDTO::getProductGroup, m -> m, (v1, v2) -> v1));
        request.setSupplyChainRole(3);
        Page<CrmEnterpriseDTO> page;
        int current = 1;
        log.info("零售机构档案excel导出 crmPharmacyExportService 查询条件为:[{}]", request);
        do {
            request.setCurrent(current);
            request.setSize(500);
            page = crmEnterpriseApi.getCrmEnterpriseInfoPage(request);
            List<CrmEnterpriseDTO> crmEnterpriseDTOList = page.getRecords();
            if (CollectionUtils.isEmpty(crmEnterpriseDTOList)) {
                continue;
            }
            List<Long> idList = page.getRecords().stream().map(CrmEnterpriseDTO::getId).collect(Collectors.toList());
            List<CrmPharmacyDTO> pharmacyDTOList = crmPharmacyApi.listByCrmEnterpriseId(idList);
            Map<Long, CrmPharmacyDTO> pharmacyDTOMap = pharmacyDTOList.stream().collect(Collectors.toMap(CrmPharmacyDTO::getCrmEnterpriseId, e -> e, (k1, k2) -> k1));

            //获取三者关系并分组map
            List<CrmEnterpriseRelationPostDTO> enterpriseRelationPostByProductGroup = crmEnterpriseRelationShipApi.getEnterpriseRelationPostByProductGroup(idList);
            //岗位编码
            List<Long> postCodeList = Optional.ofNullable(enterpriseRelationPostByProductGroup.stream().distinct().map(m -> m.getPostCode()).collect(Collectors.toList())).orElse(Lists.newArrayList());
            //人员信息
            List<EsbEmployeeDTO> esbEmployeeDTOS = CollectionUtil.isEmpty(postCodeList) ? Lists.newArrayList() : esbEmployeeApi.listByJobIdsForAgency(postCodeList);
            //1、人员转化map 基于岗位编码
            Map<Long, EsbEmployeeDTO> jobIdMap = esbEmployeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getJobId, Function.identity()));
            //2、获取三者关系并分组map key 产品组+企业ID
            Map<String, CrmEnterpriseRelationPostDTO> relationPostMap = Optional.ofNullable(enterpriseRelationPostByProductGroup.stream().collect(Collectors.toMap(this::getFormRelationMapKey, m -> m))).orElse(Maps.newHashMap());


            for (CrmEnterpriseDTO crmEnterpriseDTO : crmEnterpriseDTOList) {
                ExportCrmPharmacyBO crmPharmacyBO = PojoUtils.map(crmEnterpriseDTO, ExportCrmPharmacyBO.class);
                crmPharmacyBO.setCode(String.valueOf(crmEnterpriseDTO.getId()));
                crmPharmacyBO.setCrmCode(crmEnterpriseDTO.getCode());
                CrmPharmacyDTO crmPharmacyDTO = pharmacyDTOMap.get(crmEnterpriseDTO.getId());
                if (Objects.nonNull(crmPharmacyDTO)) {
                    PojoUtils.map(crmPharmacyDTO, crmPharmacyBO);
                    // 标签属性 1-社区店；2-商圈店；3-院边店；4-电商店
                    crmPharmacyBO.setLabelAttribute(getLabelAttributeString(crmPharmacyDTO.getLabelAttribute()));
                    // 是否协议 1-是；2-否
                    crmPharmacyBO.setAgreement(null != crmPharmacyDTO.getAgreement() && 1 == crmPharmacyDTO.getAgreement() ? "是" : "否");
                    // 药店属性 1-连锁分店；2-单体药店
                    crmPharmacyBO.setPharmacyAttribute(getPharmacyAttributeString(crmPharmacyDTO.getPharmacyAttribute()));
                    // 药店类型 1-直营；2-加盟
                    crmPharmacyBO.setPharmacyType(getPharmacyTypeString(crmPharmacyDTO.getPharmacyType()));
                    // 药店级别 1-A级；2-B级；3-C级
                    crmPharmacyBO.setPharmacyLevel(getPharmacyLevelString(crmPharmacyDTO.getPharmacyLevel()));
                    // 是否平价 1-是；2-否
                    crmPharmacyBO.setParity(null != crmPharmacyDTO.getParity() && 1 == crmPharmacyDTO.getParity() ? "是" : "否");
                    // 是否医保 1-医保药店；2-慢保药店；3-否医保
                    crmPharmacyBO.setMedicalInsurance(getMedicalInsuranceString(crmPharmacyDTO.getMedicalInsurance()));
                    // 是否基药终端 1是 2否
                    crmPharmacyBO.setBaseMedicineFlag(null != crmPharmacyDTO.getBaseMedicineFlag() && 1 == crmPharmacyDTO.getBaseMedicineFlag() ? "是" : "否");
                    // 是否承包 1是 2否
                    crmPharmacyBO.setContractFlag(null != crmPharmacyDTO.getContractFlag() && 1 == crmPharmacyDTO.getContractFlag() ? "是" : "否");

                    String parentCompanyCode = crmPharmacyDTO.getParentCompanyCode();
                    crmPharmacyBO.setParentCompanyName("");
                    if (StringUtils.isNotBlank(parentCompanyCode)) {
                        CrmEnterpriseDTO enterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(Long.parseLong(parentCompanyCode));
                        if (Objects.nonNull(enterpriseDTO)) {
                            crmPharmacyBO.setParentCompanyName(enterpriseDTO.getName());
                        }
                    }
                }

                String targetFlagStr = "";
                if (Objects.nonNull(crmPharmacyDTO)) {
                    targetFlagStr = getTargetFlagStr(crmPharmacyDTO.getTargetFlag());
                }

                PRODUCT_GROUP.forEach(a -> {
                    if (null != departmentProductRelationDTOMap.get(a) && null != relationPostMap.get(departmentProductRelationDTOMap.get(a).getProductGroup() + String.valueOf(crmEnterpriseDTO.getId()))) {
                        CrmEnterpriseRelationPostDTO crmEnterpriseRelationPostDTO = relationPostMap.get(departmentProductRelationDTOMap.get(a).getProductGroup() + String.valueOf(crmEnterpriseDTO.getId()));
                        if (Objects.nonNull(jobIdMap.get(crmEnterpriseRelationPostDTO.getPostCode()))) {
                            //获取当前档案三者关系的产品组名称
                            //获取人信息通过岗位编码
                            EsbEmployeeDTO esbEmployeeDTO = jobIdMap.get(crmEnterpriseRelationPostDTO.getPostCode());
                            List<AgencyDepartGrroupResolve> resolves = agencyDepartGroupResolveMap.get(a);
                            resolves.stream().forEach(b -> {
                                mapResolveEmpToBoField(b, esbEmployeeDTO, crmPharmacyBO);
                            });
                        }
                    }
                });

                if (StringUtils.isNotBlank(crmPharmacyBO.getAh())) {
                    crmPharmacyBO.setAg(targetFlagStr);
                }
                if (StringUtils.isNotBlank(crmPharmacyBO.getAm())) {
                    crmPharmacyBO.setAl(targetFlagStr);
                }
                if (StringUtils.isNotBlank(crmPharmacyBO.getAr())) {
                    crmPharmacyBO.setAq(targetFlagStr);
                }
                if (StringUtils.isNotBlank(crmPharmacyBO.getAw())) {
                    crmPharmacyBO.setAv(targetFlagStr);
                }
                if (StringUtils.isNotBlank(crmPharmacyBO.getBb())) {
                    crmPharmacyBO.setBa(targetFlagStr);
                }

                // 业务状态 1有效 2失效
                crmPharmacyBO.setBusinessCode(null != crmEnterpriseDTO.getBusinessCode() && 1 == crmEnterpriseDTO.getBusinessCode() ? "有效" : "无效");

                crmPharmacyBO.setResidentCommissionerCode("");
                crmPharmacyBO.setResidentCommissionerName("");
                Map<String, Object> dataPojo = BeanUtil.beanToMap(crmPharmacyBO);
                data.add(dataPojo);
            }
            current = current + 1;
        } while (CollectionUtils.isNotEmpty(page.getRecords()));
        log.info("零售机构档案excel导出 crmPharmacyExportService 总页码为:[{}]，总数量为:[{}]", current, page.getTotal());

        return setResultParam(result,data);
    }

    @Override
    public QueryCrmAgencyPageListRequest getParam(Map<String, Object> map) {
        QueryCrmAgencyPageListRequest request= PojoUtils.map(map, QueryCrmAgencyPageListRequest.class);
        String empId=map.getOrDefault("currentUserCode","").toString();
        SjmsUserDatascopeBO byEmpId = sjmsUserDatascopeApi.getByEmpId(empId);
        request.setSjmsUserDatascopeBO(getLongs(byEmpId));
        return request;
    }

    /**
     * 是否目标 1-是；2-否
     *
     * @param targetFlag 是否目标
     * @return 字符串
     */
    private String getTargetFlagStr(Integer targetFlag) {
        if (Objects.isNull(targetFlag) || 0 == targetFlag) {
            return "";
        } else if (1 == targetFlag) {
            return "是";
        } else if (2 == targetFlag) {
            return "否";
        }
        return "";
    }

    /**
     * 标签属性 1-社区店；2-商圈店；3-院边店；4-电商店
     *
     * @param labelAttribute 标签属性
     * @return 字符串
     */
    private String getLabelAttributeString(Integer labelAttribute) {
        CrmPharmacyLabelAttributeEnum labelAttributeEnum = CrmPharmacyLabelAttributeEnum.getByCode(labelAttribute);
        if (null == labelAttributeEnum) {
            return "";
        }
        return labelAttributeEnum.getName();
    }

    /**
     * 药店属性 1-连锁分店；2-单体药店
     *
     * @param pharmacyAttribute 药店属性
     * @return 字符串
     */
    private String getPharmacyAttributeString(Integer pharmacyAttribute) {
        CrmPharmacyAttributeEnum pharmacyAttributeEnum = CrmPharmacyAttributeEnum.getByCode(pharmacyAttribute);
        if (null == pharmacyAttributeEnum) {
            return "";
        }
        return pharmacyAttributeEnum.getName();
    }

    /**
     * 药店级别 1-A级；2-B级；3-C级
     *
     * @param pharmacyLevel 药店属性
     * @return 字符串
     */
    private String getPharmacyLevelString(Integer pharmacyLevel) {
        CrmPharmacyLevelEnum pharmacyLevelEnum = CrmPharmacyLevelEnum.getByCode(pharmacyLevel);
        if (null == pharmacyLevelEnum) {
            return "";
        }
        return pharmacyLevelEnum.getName();
    }

    /**
     * 药店类型 1-直营；2-加盟
     *
     * @param pharmacyType 药店类型
     * @return 字符串
     */
    private String getPharmacyTypeString(Integer pharmacyType) {
        CrmPharmacyTypeEnum pharmacyTypeEnum = CrmPharmacyTypeEnum.getByCode(pharmacyType);
        if (null == pharmacyTypeEnum) {
            return "";
        }
        return pharmacyTypeEnum.getName();
    }

    /**
     * 是否医保 1-医保药店；2-慢保药店；3-否医保
     *
     * @param medicalInsurance 是否医保
     * @return 字符串
     */
    private String getMedicalInsuranceString(Integer medicalInsurance) {
        CrmPharmacyMedicalInsuranceEnum medicalInsuranceEnum = CrmPharmacyMedicalInsuranceEnum.getByCode(medicalInsurance);
        if (null == medicalInsuranceEnum) {
            return "";
        }
        return medicalInsuranceEnum.getName();
    }

    public String getFormRelationMapKey(CrmEnterpriseRelationPostDTO form) {
        return StringUtils.isEmpty(form.getProductGroup())?"":form.getProductGroup() + toStringNpe(form.getCrmEnterpriseId());
    }

    public void mapResolveEmpToBoField(AgencyDepartGrroupResolve agencyDepartGrroupResolve, Object sourceObject, Object targetObject) {
        try {
            Field sourceDeclaredField = EsbEmployeeDTO.class.getDeclaredField(agencyDepartGrroupResolve.getSourceFiled());
            sourceDeclaredField.setAccessible(true);
            Field targetDeclaredField = ExportCrmPharmacyBO.class.getDeclaredField(agencyDepartGrroupResolve.getTagetFiled());
            targetDeclaredField.setAccessible(true);
            Object sourceFiledValue = sourceDeclaredField.get(sourceObject);

            targetDeclaredField.set(targetObject, sourceFiledValue);
        } catch (Exception e) {
            log.error("导出属性转化失败:{}", e);
        }
    }

    public String toStringNpe(Long var){
        return ObjectUtil.isNull(var) ? "" : var.toString();
    }
    private QueryExportDataDTO setResultParam(QueryExportDataDTO result,List<Map<String, Object>> data){
        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("dsinfo");
        // 页签字段
        exportDataDTO.setTempleteParamList(FIELD);
        // 页签数据
        exportDataDTO.setData(data);
        exportDataDTO.setFristRow(3);
        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        result.setUseExcelTemplete(true);
        result.setExcelTempletePath("templete/excel/crm_pharmacy_template.xlsx");
        return result;
    }
    private  SjmsUserDatascopeBO getLongs(SjmsUserDatascopeBO byEmpId){
        if(OrgDatascopeEnum.NONE==OrgDatascopeEnum.getFromCode(byEmpId.getOrgDatascope())){
            return  null;
        }
        return  byEmpId;
    }
}
