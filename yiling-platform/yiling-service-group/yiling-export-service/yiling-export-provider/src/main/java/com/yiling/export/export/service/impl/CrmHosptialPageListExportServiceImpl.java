package com.yiling.export.export.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.yiling.dataflow.agency.api.CrmDepartProductGroupApi;
import com.yiling.dataflow.agency.api.CrmHosptialApi;
import com.yiling.dataflow.agency.dto.CrmDepartmentProductRelationDTO;
import com.yiling.dataflow.agency.dto.CrmHospitalDTO;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyPageListRequest;
import com.yiling.dataflow.agency.dto.request.QueryDepartProductGroupByUploadNameRequest;
import com.yiling.dataflow.agency.enums.CrmHosptialAttributeEnum;
import com.yiling.dataflow.agency.enums.CrmHosptialTypeEnum;
import com.yiling.dataflow.agency.enums.CrmNationGradaEnum;
import com.yiling.dataflow.agency.enums.CrmYlLevelEnum;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationPostDTO;
import com.yiling.export.export.bo.AgencyDepartGrroupResolve;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * 销售流向导出
 *
 * @author: houjie.sun
 * @date: 2022/2/16
 */
@Service("crmHosptialPageListExportService")
@Slf4j
public class CrmHosptialPageListExportServiceImpl implements BaseExportQueryDataService<QueryCrmAgencyPageListRequest> {

    @DubboReference(timeout = 60 * 1000)
    CrmHosptialApi crmHosptialApi;

    @DubboReference(timeout = 60 * 1000)
    CrmEnterpriseApi crmEnterpriseApi;
    @DubboReference
    private CrmDepartProductGroupApi crmDepartProductGroupApi;
    @DubboReference
    private CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;
    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;
    @DubboReference
    private SjmsUserDatascopeApi sjmsUserDatascopeApi;
    private final static Map<String, List<AgencyDepartGrroupResolve>> agencyDepartGrroupResolveMap=new HashMap<>();
    private final static LinkedList<String> PRODUCT_GROUP=new LinkedList<>();

    static {
        PRODUCT_GROUP.add("心脑事业部通心络");
        PRODUCT_GROUP.add("心脑事业部参松养心");
        PRODUCT_GROUP.add("心脑事业部芪苈强心");
        PRODUCT_GROUP.add("呼吸事业部产品组");
        PRODUCT_GROUP.add("内分泌事业部产品组");
        PRODUCT_GROUP.add("泌尿事业部产品组");
        PRODUCT_GROUP.add("新品事业部产品组(不含参灵蓝)");
        PRODUCT_GROUP.add("社区部标准产品组");
        PRODUCT_GROUP.add("社区部标准产品组(不含连花)");
        PRODUCT_GROUP.add("社区部产品组(TSQLJ)");
        PRODUCT_GROUP.add("社区部通心络");
        PRODUCT_GROUP.add("社区部津力达");
        PRODUCT_GROUP.add("社区部连花清瘟");
        PRODUCT_GROUP.add("社区部参松养心");
        PRODUCT_GROUP.add("社区部芪苈强心");
        PRODUCT_GROUP.add("社区部养正消积");
        PRODUCT_GROUP.add("社区部夏荔芪");
        PRODUCT_GROUP.add("社区特部标准产品组");
        PRODUCT_GROUP.add("社区特部标准产品组(不含连花)");
        PRODUCT_GROUP.add("社区特部产品组(TSQLJ)");
        PRODUCT_GROUP.add("社区特部通心络");
        PRODUCT_GROUP.add("社区特部津力达");
        PRODUCT_GROUP.add("社区特部连花清瘟");
        PRODUCT_GROUP.add("社区特部参松养心");
        PRODUCT_GROUP.add("社区特部芪苈强心");
        PRODUCT_GROUP.add("社区特部养正消积");
        PRODUCT_GROUP.add("社区特部夏荔芪");
        PRODUCT_GROUP.add("社区部连花清咳");
        PRODUCT_GROUP.add("社区部连花清瘟(不含连花清咳)");
        PRODUCT_GROUP.add("城乡部连花清咳");
        PRODUCT_GROUP.add("城乡部连花清瘟(不含连花清咳)");
        PRODUCT_GROUP.add("城乡部通心络");
        PRODUCT_GROUP.add("城乡部津力达");
        PRODUCT_GROUP.add("城乡部连花清瘟");
        PRODUCT_GROUP.add("城乡部参松养心");
        PRODUCT_GROUP.add("城乡部芪苈强心");
        PRODUCT_GROUP.add("城乡部养正消积");
        PRODUCT_GROUP.add("城乡部夏荔芪");
        PRODUCT_GROUP.add("新品事业部产品组");
        PRODUCT_GROUP.add("数字化医疗终端产品组");
        PRODUCT_GROUP.add("医疗中药饮片产品组");
        int rowIndexStart=26;
        for (int j = 0; j < PRODUCT_GROUP.size(); j++) {
            List<AgencyDepartGrroupResolve> g=new ArrayList<>();
            g.add(AgencyDepartGrroupResolve.builder().tagetFiled(getKey(rowIndexStart++)).sourceFiled("yxArea").build());
            g.add(AgencyDepartGrroupResolve.builder().tagetFiled(getKey(rowIndexStart++)).sourceFiled("empId").build());
            g.add(AgencyDepartGrroupResolve.builder().tagetFiled(getKey(rowIndexStart++)).sourceFiled("empName").build());
            g.add(AgencyDepartGrroupResolve.builder().tagetFiled(getKey(rowIndexStart++)).sourceFiled("noExists").build());
            agencyDepartGrroupResolveMap.put(PRODUCT_GROUP.get(j),g);
        }
    }


    @Override
    public QueryExportDataDTO queryData(QueryCrmAgencyPageListRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        ExportDataDTO exportDataDTO = new ExportDataDTO();
        List<Map<String, Object>> oMap = new ArrayList<>();
        if(null==request.getSjmsUserDatascopeBO()){
            return setResultParam(result,exportDataDTO,oMap);
        }
        QueryDepartProductGroupByUploadNameRequest departRequest=new QueryDepartProductGroupByUploadNameRequest();
        departRequest.setUploadNames( PRODUCT_GROUP.stream().collect(Collectors.toList()));
        departRequest.setSupplyChainRole(2);
        List<CrmDepartmentProductRelationDTO> departProductGroupListByUploadName = crmDepartProductGroupApi.getDepartProductGroupListByUploadName(departRequest);
        //产品组和导出产品组对应关系
        Map<String,CrmDepartmentProductRelationDTO> departmentProductRelationDTOMap=departProductGroupListByUploadName.stream().collect(Collectors.toMap(CrmDepartmentProductRelationDTO::getProductGroup,m->m,(v1,v2)->v1));

        int current = 1;
        int size = 300;
        Page<CrmEnterpriseDTO> page;
        do {
            request.setCurrent(current);
            request.setSize(size);
            request.setSupplyChainRole(2);
            page = crmEnterpriseApi.getCrmEnterpriseInfoPage(request);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            List<Long> crmEnterIds = page.getRecords().stream().map(CrmEnterpriseDTO::getId).collect(Collectors.toList());
            //产品组
            //获取三者关系并分组map
            List<CrmEnterpriseRelationPostDTO> enterpriseRelationPostByProductGroup = crmEnterpriseRelationShipApi.getEnterpriseRelationPostByProductGroup(crmEnterIds);
            //岗位编码
            List<Long> postCodeList=Optional.ofNullable(enterpriseRelationPostByProductGroup.stream().distinct().map(m->m.getPostCode()).collect(Collectors.toList())).orElse(Lists.newArrayList());
            //人员信息
            List<EsbEmployeeDTO> esbEmployeeDTOS = CollectionUtil.isEmpty(postCodeList)?Lists.newArrayList():esbEmployeeApi.listByJobIdsForAgency(postCodeList);
            //1、人员转化map 基于岗位编码
            Map<Long,EsbEmployeeDTO> jobIdMap=esbEmployeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getJobId,Function.identity()));
            //2、获取三者关系并分组map key 产品组+企业ID
            Map<String, CrmEnterpriseRelationPostDTO> relationPostMap=  Optional.ofNullable( enterpriseRelationPostByProductGroup.stream().collect(Collectors.toMap(this::getFormReationMapKey,m->m))).orElse(Maps.newHashMap());
            //产品组
            List<CrmHospitalDTO> crmHospitalDTOS = crmHosptialApi.getHosptialInfoByCrmEnterId(crmEnterIds);
            Map<Long, CrmHospitalDTO> hospitalDTOMap = new HashMap<>(300);
            if (CollectionUtils.isNotEmpty(crmHospitalDTOS)) {
                hospitalDTOMap = crmHospitalDTOS.stream().collect(Collectors.toMap(CrmHospitalDTO::getCrmEnterpriseId, Function.identity()));
            }
            Map<Long, CrmHospitalDTO> finalHospitalDTOMap = hospitalDTOMap;
            page.getRecords().forEach(item -> {
                CrmHospitalDTO crmHospitalDTO = finalHospitalDTOMap.get(item.getId());
                Map map = new HashMap(190);
                map.put("id", item.getId());
                map.put("code", item.getCode());
                map.put("ylCode", item.getYlCode());
                map.put("name", item.getName());
                map.put("shortName", item.getShortName());
                map.put("businessCode", 1==item.getBusinessCode() ? "有效" : "失效");
                map.put("provinceName", item.getProvinceName());
                map.put("regionName", item.getRegionName());
                map.put("address", item.getAddress());
                map.put("postalCode", item.getPostalCode());
                map.put("phone", item.getPhone());
                map.put("fax", item.getFax());
                map.put("remark", item.getBusinessRemark());
                if (ObjectUtil.isNotEmpty(crmHospitalDTO)) {
                    map.put("countyCustomerCode", crmHospitalDTO.getCountyCustomerCode());
                    map.put("countyCustomerName", crmHospitalDTO.getCountyCustomerName());
                    CrmHosptialTypeEnum crmHosptialTypeEnum = CrmHosptialTypeEnum.getByCode(crmHospitalDTO.getMedicalNature());
                    map.put("medicalNature", ObjectUtil.isNull(crmHosptialTypeEnum)?"":crmHosptialTypeEnum.getName());
                    CrmHosptialAttributeEnum byCode = CrmHosptialAttributeEnum.getByCode(crmHospitalDTO.getMedicalType());
                    map.put("medicalType", ObjectUtil.isNull(byCode)?"":byCode.getName());
                    CrmYlLevelEnum byCode1 = CrmYlLevelEnum.getByCode(crmHospitalDTO.getYlLevel());
                    map.put("ylLevel",  ObjectUtil.isNull(byCode1)?"":byCode1.getName());
                    CrmNationGradaEnum byCode2 = CrmNationGradaEnum.getByCode(crmHospitalDTO.getNationalGrade());
                    map.put("nationalGrade", ObjectUtil.isNull(byCode2)?"":byCode2.getName());
                    map.put("medicalNature", CrmHosptialTypeEnum.getByCode(crmHospitalDTO.getMedicalNature())==null?"":CrmHosptialTypeEnum.getByCode(crmHospitalDTO.getMedicalNature()).getName());
                    map.put("medicalType", CrmHosptialAttributeEnum.getByCode(crmHospitalDTO.getMedicalType())==null?"":CrmHosptialAttributeEnum.getByCode(crmHospitalDTO.getMedicalType()).getName());
                    map.put("ylLevel",  CrmYlLevelEnum.getByCode( crmHospitalDTO.getYlLevel())==null?"":CrmYlLevelEnum.getByCode( crmHospitalDTO.getYlLevel()).getName());
                    map.put("nationalGrade", CrmNationGradaEnum.getByCode(crmHospitalDTO.getNationalGrade())==null?"":CrmNationGradaEnum.getByCode(crmHospitalDTO.getNationalGrade()).getName());
                    map.put("baseMedicineFlag", 1 == crmHospitalDTO.getBaseMedicineFlag() ? "是" : "否");
                    map.put("totalBedNumber", crmHospitalDTO.getTotalBedNumber());
                    map.put("mnemonicCode", crmHospitalDTO.getMnemonicCode());
                    map.put("contractFlag", 1 == crmHospitalDTO.getContractFlag() ? "是" : "否");
                    map.put("additionalInfoTwo", crmHospitalDTO.getAdditionalInfoTwo());
                    map.put("additionalInfoThree", crmHospitalDTO.getAdditionalInfoThree());
                    map.put("additionalInfoFour", crmHospitalDTO.getAdditionalInfoFour());
                    map.put("additionalInfoFive", crmHospitalDTO.getAdditionalInfoFive());

                }
                PRODUCT_GROUP.stream().forEach(a->{
                    if (null != departmentProductRelationDTOMap.get(a) &&
                            null != relationPostMap.get(departmentProductRelationDTOMap.get(a).getProductGroup() + String.valueOf(item.getId()))
                    ) {
                        CrmEnterpriseRelationPostDTO crmEnterpriseRelationPostDTO= relationPostMap.get(departmentProductRelationDTOMap.get(a).getProductGroup()+String.valueOf(item.getId()));
                        if(Objects.nonNull(jobIdMap.get(crmEnterpriseRelationPostDTO.getPostCode()))){
                            //获取当前档案三者关系的产品组名称
                            //获取人信息通过岗位编码
                            EsbEmployeeDTO esbEmployeeDTO=jobIdMap.get(crmEnterpriseRelationPostDTO.getPostCode());
                            List<AgencyDepartGrroupResolve> resolves= agencyDepartGrroupResolveMap.get(a);
                            resolves.stream().forEach(b->{
                                mapResolveEmpToBoField(b,esbEmployeeDTO,map);
                            });
                        }
                    }
                });
                oMap.add(map);
            });
            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));

        return setResultParam(result,exportDataDTO,oMap);
    }


    @Override
    public QueryCrmAgencyPageListRequest getParam(Map<String, Object> map) {
        QueryCrmAgencyPageListRequest request=  PojoUtils.map(map, QueryCrmAgencyPageListRequest.class);
        String empId=map.getOrDefault("currentUserCode","").toString();
        SjmsUserDatascopeBO byEmpId = sjmsUserDatascopeApi.getByEmpId(empId);
        request.setSjmsUserDatascopeBO(getLongs(byEmpId));
        return request;
    }
    public String getFormReationMapKey(CrmEnterpriseRelationPostDTO form){
        return StringUtils.isEmpty(form.getProductGroup())?"":form.getProductGroup() + toStringNpe(form.getCrmEnterpriseId());
    }

    public  void mapResolveEmpToBoField(AgencyDepartGrroupResolve agencyDepartGrroupResolve,Object sourceObject,Map tagetObject){
        try {
            if(!"noExists".equals(agencyDepartGrroupResolve.getSourceFiled())){
                Field sourceDeclaredField = EsbEmployeeDTO.class.getDeclaredField(agencyDepartGrroupResolve.getSourceFiled());
                if(sourceDeclaredField!=null){
                    sourceDeclaredField.setAccessible(true);
                    Object sourceFiledValue = sourceDeclaredField.get(sourceObject);
                    tagetObject.put(agencyDepartGrroupResolve.getTagetFiled(),sourceFiledValue);
                }
            }
        }catch (Exception e){
            log.error("导出属性转化失败:{}",e);
        }
    }
    public static String getKey(int index){
        String colCode = "";
        char key='A';
        int loop = index / 26;
        if(loop>0){
            colCode += getKey(loop-1);
        }
        key = (char) (key+index%26);
        colCode += key;
        return colCode;
    }
    public static void main(String[] args) {
        //截止到GH
        int rowIndexStart=26;
        for (int j = 0; j < PRODUCT_GROUP.size(); j++) {
            List<AgencyDepartGrroupResolve> g=new ArrayList<>();
            g.add(AgencyDepartGrroupResolve.builder().tagetFiled(getKey(rowIndexStart++)).sourceFiled("yxArea").build());
            g.add(AgencyDepartGrroupResolve.builder().tagetFiled(getKey(rowIndexStart++)).sourceFiled("empId").build());
            g.add(AgencyDepartGrroupResolve.builder().tagetFiled(getKey(rowIndexStart++)).sourceFiled("empName").build());
            g.add(AgencyDepartGrroupResolve.builder().tagetFiled(getKey(rowIndexStart++)).sourceFiled("noExists").build());
            agencyDepartGrroupResolveMap.put(PRODUCT_GROUP.get(j),g);
        }
        System.out.println(getKey(189));
    }

    private QueryExportDataDTO setResultParam(QueryExportDataDTO result,ExportDataDTO exportDataDTO, List<Map<String, Object>> oMap){
        List<ExportDataDTO> exportDataDTOList = new ArrayList<>();
        exportDataDTO.setData(oMap);
        exportDataDTO.setFristRow(3);
        LinkedList linkedList = new LinkedList<>();
        linkedList.add("id");
        linkedList.add("code");
        linkedList.add("ylCode");
        linkedList.add("name");
        linkedList.add("shortName");
        linkedList.add("businessCode");
        linkedList.add("provinceName");
        linkedList.add("regionName");
        linkedList.add("countyCustomerCode");
        linkedList.add("countyCustomerName");
        linkedList.add("address");
        linkedList.add("postalCode");
        linkedList.add("phone");
        linkedList.add("fax");
        linkedList.add("remark");
        linkedList.add("medicalNature");
        linkedList.add("medicalType");
        linkedList.add("ylLevel");
        linkedList.add("nationalGrade");
        linkedList.add("baseMedicineFlag");
        linkedList.add("totalBedNumber");
        linkedList.add("mnemonicCode");
        linkedList.add("contractFlag");
        linkedList.add("additionalInfoTwo");
        linkedList.add("additionalInfoThree");
        linkedList.add("additionalInfoFour");
        linkedList.add("additionalInfoFive");
        for (int i = 26; i <190 ; i++) {
            linkedList.add(getKey(i));
        }
        exportDataDTO.setTempleteParamList(linkedList);
        exportDataDTOList.add(exportDataDTO);
        result.setSheets(exportDataDTOList);
        result.setUseExcelTemplete(true);
        result.setExcelTempletePath("templete/excel/crm_hosptial_templete.xlsx");
        return  result;
    }
    private  SjmsUserDatascopeBO getLongs(SjmsUserDatascopeBO byEmpId) {
        if (OrgDatascopeEnum.NONE == OrgDatascopeEnum.getFromCode(byEmpId.getOrgDatascope())) {
            return null;
        }
        return byEmpId;
    }
    public String toStringNpe(Long var){
        return ObjectUtil.isNull(var) ? "" : var.toString();
    }

}
