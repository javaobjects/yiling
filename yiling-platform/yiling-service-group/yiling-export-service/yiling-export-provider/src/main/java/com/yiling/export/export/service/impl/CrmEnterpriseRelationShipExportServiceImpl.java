package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.google.common.collect.Lists;
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.api.CrmManorApi;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.CrmManorDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorParamRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseRelationShipPageListRequest;
import com.yiling.dataflow.agency.enums.AgencySupplyChainRoleEnum;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.export.export.bo.ExportCrmEnterpriseRelationShipBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 三者关系合并版EXCEL模板
 *
 * @author: yong.zhang
 * @date: 2023/2/18 0018
 */
@Slf4j
@Service("crmEnterpriseRelationShipExportService")
public class CrmEnterpriseRelationShipExportServiceImpl implements BaseExportQueryDataService<QueryCrmEnterpriseRelationShipPageListRequest> {

    @DubboReference
    CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;

    @DubboReference
    CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    EsbOrganizationApi esbOrganizationApi;

    @DubboReference
    BusinessDepartmentApi businessDepartmentApi;
    @DubboReference
    private SjmsUserDatascopeApi sjmsUserDatascopeApi;
    @DubboReference
    private CrmManorApi crmManorApi;
    @DubboReference
    private CrmGoodsCategoryApi crmGoodsCategoryApi;
    private final static LinkedList<String> FIELD = new LinkedList<>();

    static {
        FIELD.add("department");//部门
        FIELD.add("businessDepartment");//业务部门
        FIELD.add("provincialArea");//省区
        FIELD.add("businessProvince");//业务省区
        FIELD.add("businessArea");//业务区域
        FIELD.add("superiorSupervisorCode");//上级主管编码
        FIELD.add("superiorSupervisorName");//上级主管名称
        FIELD.add("representativeCode");//代表编码
        FIELD.add("representativeName");//代表名称
        FIELD.add("customerCode");//客户编码
        FIELD.add("customerName");//客户名称
        FIELD.add("supplyChainRole");//供应链角色
        FIELD.add("provinceName");//省份
        FIELD.add("cityName");//城市
        FIELD.add("regionName");//区县
        FIELD.add("productGroup");//产品组
        FIELD.add("categoryName");//分类名称
        FIELD.add("manorName");//品种名称
        FIELD.add("substituteRunningDesc");//是否带跑
    }

    public static final HashMap<Long, String> orgIdAndOrgName = new HashMap<>();

    static {
        orgIdAndOrgName.put(12294L, "事业一部（心脑）");
        orgIdAndOrgName.put(12296L, "事业二部（内分泌）");
        orgIdAndOrgName.put(10515L, "事业三部（呼吸）");
        orgIdAndOrgName.put(12297L, "事业四部（泌尿）");
        orgIdAndOrgName.put(11026L, "事业五部（肿瘤）");
        orgIdAndOrgName.put(9792L, "事业六部（城乡）");
        orgIdAndOrgName.put(12930L, "事业七部（社区）");
        orgIdAndOrgName.put(13591L, "事业八部（精神）");
        orgIdAndOrgName.put(10328L, "线下分销部");
        orgIdAndOrgName.put(13512L, "线下分销部");
        orgIdAndOrgName.put(13798L, "零售一部");
        orgIdAndOrgName.put(13799L, "零售二部");
        orgIdAndOrgName.put(12298L, "市场运营部");
        orgIdAndOrgName.put(10410L, "商务管理部");
    }

    @Override
    public QueryExportDataDTO queryData(QueryCrmEnterpriseRelationShipPageListRequest request) {

        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        List<Map<String, Object>> data = new ArrayList<>();
        if(null==request.getSjmsUserDatascopeBO()){
            ExportDataDTO exportDataDTO = new ExportDataDTO();
            exportDataDTO.setSheetName("Sheet1");
            // 页签字段
            exportDataDTO.setTempleteParamList(FIELD);
            // 页签数据
            exportDataDTO.setData(data);
            exportDataDTO.setFristRow(1);
            List<ExportDataDTO> sheets = new ArrayList<>();
            sheets.add(exportDataDTO);
            result.setSheets(sheets);
            result.setUseExcelTemplete(true);
            result.setExcelTempletePath("templete/excel/crm_enterprise_relation_ship_template.xlsx");
            return result;
        }
        //需要循环调用

        Page<CrmEnterpriseRelationShipDTO> page;
        log.info("三者关系合并版EXCEL模板 crmEnterpriseRelationShipExportService 查询条件为:[{}]", request);
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            page = crmEnterpriseRelationShipApi.getCrmRelationPage(request);
            List<CrmEnterpriseRelationShipDTO> enterpriseRelationShipDTOList = page.getRecords();
            if (CollectionUtils.isEmpty(enterpriseRelationShipDTOList)) {
                continue;
            }
            //处理品种名称等
            buildManorAndCategoryName(enterpriseRelationShipDTOList);
            for (CrmEnterpriseRelationShipDTO relationShipDTO : enterpriseRelationShipDTOList) {
                ExportCrmEnterpriseRelationShipBO enterpriseRelationShipBO = PojoUtils.map(relationShipDTO, ExportCrmEnterpriseRelationShipBO.class);
                enterpriseRelationShipBO.setSupplyChainRole(getSupplyChainRole(relationShipDTO.getSupplyChainRoleType()));
                enterpriseRelationShipBO.setCustomerCode(Long.toString(relationShipDTO.getCrmEnterpriseId()));
                CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(relationShipDTO.getCrmEnterpriseId());
                if (Objects.nonNull(crmEnterpriseDTO)) {
                    enterpriseRelationShipBO.setCustomerName(crmEnterpriseDTO.getName());
                    enterpriseRelationShipBO.setCustomerName(crmEnterpriseDTO.getName());
                    enterpriseRelationShipBO.setProvinceName(crmEnterpriseDTO.getProvinceName());
                    enterpriseRelationShipBO.setCityName(crmEnterpriseDTO.getCityName());
                    enterpriseRelationShipBO.setRegionName(crmEnterpriseDTO.getRegionName());
                }
                Long postCode = relationShipDTO.getPostCode();
                EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpIdOrJobId(null, postCode.toString(),null);
                if (ObjectUtil.isNotNull(esbEmployeeDTO)) {
                    enterpriseRelationShipBO.setBusinessDepartment(esbEmployeeDTO.getYxDept());
                    enterpriseRelationShipBO.setBusinessProvince(esbEmployeeDTO.getYxProvince());
                    enterpriseRelationShipBO.setBusinessArea(esbEmployeeDTO.getYxArea());
                    enterpriseRelationShipBO.setSuperiorSupervisorCode(esbEmployeeDTO.getSuperior());
                    enterpriseRelationShipBO.setSuperiorSupervisorName(esbEmployeeDTO.getSuperiorName());
                    enterpriseRelationShipBO.setRepresentativeCode(esbEmployeeDTO.getEmpId());
                    enterpriseRelationShipBO.setRepresentativeName(esbEmployeeDTO.getEmpName());
                    //                enterpriseRelationShipBO.setCustomerCode(esbEmployeeDTO.getEmpId());
                    //                enterpriseRelationShipBO.setCustomerName(esbEmployeeDTO.getEmpName());
                    enterpriseRelationShipBO.setCityName(esbEmployeeDTO.getEmpName());
                    //通过循环获取部门。
                    Long deptId = esbEmployeeDTO.getDeptId();
                    EsbOrganizationDTO organizationDTO = businessDepartmentApi.getByOrgId(esbEmployeeDTO.getDeptId());
                    if (Objects.nonNull(organizationDTO)) {
                        relationShipDTO.setDepartment(organizationDTO.getOrgName());
                    }
                    //通过部门，业务部门，业务省区获取省区
                    String provinceArea = crmEnterpriseRelationShipApi.getProvinceAreaByThreeParms(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince());
                    enterpriseRelationShipBO.setProvincialArea(provinceArea);
                }
                //设置带跑
                enterpriseRelationShipBO.setSubstituteRunningDesc(transAndNoFlag(relationShipDTO.getSubstituteRunning()));
                Map<String, Object> dataPojo = BeanUtil.beanToMap(enterpriseRelationShipBO);
                data.add(dataPojo);
            }
            current = current + 1;
        } while (CollectionUtils.isNotEmpty(page.getRecords()));
        log.info("三者关系合并版EXCEL模板 crmEnterpriseRelationShipExportService 总页码为:[{}]，总数量为:[{}]", current, page.getTotal());
        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("Sheet1");
        // 页签字段
        exportDataDTO.setTempleteParamList(FIELD);
        // 页签数据
        exportDataDTO.setData(data);
        exportDataDTO.setFristRow(1);
        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        result.setUseExcelTemplete(true);
        result.setExcelTempletePath("templete/excel/crm_enterprise_relation_ship_template.xlsx");
        return result;
    }

    @Override
    public QueryCrmEnterpriseRelationShipPageListRequest getParam(Map<String, Object> map) {
        QueryCrmEnterpriseRelationShipPageListRequest request= PojoUtils.map(map, QueryCrmEnterpriseRelationShipPageListRequest.class);
        String empId=map.getOrDefault("currentUserCode","").toString();
        SjmsUserDatascopeBO byEmpId = sjmsUserDatascopeApi.getByEmpId(empId);
        request.setSjmsUserDatascopeBO(getLongs(byEmpId));
        return  request;
    }
    /**
     * 1-商业公司 2-医疗机构 3-零售机构
     *
     * @param supplyChainRole 供应链角色
     * @return 字符串
     */
    private String getSupplyChainRole(Integer supplyChainRole) {
        AgencySupplyChainRoleEnum supplyChainRoleEnum = AgencySupplyChainRoleEnum.getByCode(supplyChainRole);
        if (null == supplyChainRoleEnum) {
            return "";
        }
        return supplyChainRoleEnum.getName();
    }
    private  SjmsUserDatascopeBO getLongs(SjmsUserDatascopeBO byEmpId){
        if(OrgDatascopeEnum.NONE==OrgDatascopeEnum.getFromCode(byEmpId.getOrgDatascope())){
            return  null;
        }
        return  byEmpId;
    }
    private void buildManorAndCategoryName(List<CrmEnterpriseRelationShipDTO> records) {
        //品种List
        List<Long> categoryIds = records.stream().filter(e->e.getCategoryId().longValue()>0L).map(CrmEnterpriseRelationShipDTO::getCategoryId).collect(Collectors.toList());
        List<Long> manorIds = records.stream().filter(e->e.getCrmEnterpriseId().longValue()>0L).map(CrmEnterpriseRelationShipDTO::getManorId).collect(Collectors.toList());
        //分类
        if(CollUtil.isNotEmpty(categoryIds)){
            List<CrmGoodsCategoryDTO> categoryDTOS = crmGoodsCategoryApi.findByIds(categoryIds);
            if(CollUtil.isNotEmpty(categoryDTOS)){
                Map<Long,CrmGoodsCategoryDTO> crmGoodsCategoryDTOMap=categoryDTOS.stream().collect(Collectors.toMap(CrmGoodsCategoryDTO::getId,m->m,(v1,v2)->v1));
                records.stream().filter(e->e.getCategoryId().longValue()>0L).forEach(e->{
                    if(crmGoodsCategoryDTOMap.get(e.getCategoryId())!=null){
                        e.setCategoryName(crmGoodsCategoryDTOMap.get(e.getCategoryId()).getName());
                    }
                });
            }
        }
        //辖区
        if(CollUtil.isNotEmpty(manorIds)){
            QueryCrmManorParamRequest request= new QueryCrmManorParamRequest();
            request.setIdList(manorIds);
            List<CrmManorDTO> manorDTOS=crmManorApi.listByParam(request);
            if(CollUtil.isNotEmpty(manorDTOS)){
                Map<Long,CrmManorDTO> manorDTOMap=manorDTOS.stream().collect(Collectors.toMap(CrmManorDTO::getId,m->m,(v1,v2)->v1));
                records.stream().filter(e->e.getManorId().longValue()>0L).forEach(e->{
                    if(manorDTOMap.get(e.getManorId())!=null){
                        e.setManorName(manorDTOMap.get(e.getManorId()).getName());
                    }
                });
            }

        }
    }
    private String transAndNoFlag(Integer flag){
        if(flag==null ||flag==0)return null;
        else if (flag.intValue()==2) {
            return "是";
        } else if (flag.intValue()==1) {
            return "否";
        }
        return null;
    }
}
