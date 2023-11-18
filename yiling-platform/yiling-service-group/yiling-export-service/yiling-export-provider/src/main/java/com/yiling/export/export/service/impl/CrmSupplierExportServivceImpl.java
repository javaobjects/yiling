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
import com.yiling.dataflow.agency.api.CrmSupplierApi;
import com.yiling.dataflow.agency.dto.CrmDepartmentProductRelationDTO;
import com.yiling.dataflow.agency.dto.CrmEnterpriseNameDTO;
import com.yiling.dataflow.agency.dto.CrmSupplierDTO;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyPageListRequest;
import com.yiling.dataflow.agency.dto.request.QueryDepartProductGroupByUploadNameRequest;
import com.yiling.dataflow.agency.enums.CrmBaseSupplierInfoEnum;
import com.yiling.dataflow.agency.enums.CrmGeneralMedicineLevelEnum;
import com.yiling.dataflow.agency.enums.CrmGeneralMedicineSaleTypeEnum;
import com.yiling.dataflow.agency.enums.CrmPatentAgreementTypeEnum;
import com.yiling.dataflow.agency.enums.CrmSupplierAttributeEnum;
import com.yiling.dataflow.agency.enums.CrmSupplierSaleTypeEnum;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationPostDTO;
import com.yiling.export.export.bo.AgencyDepartGrroupResolve;
import com.yiling.export.export.bo.ExportCrmSupplierInfoBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * DIH商业档案导出基于模版
 */
@Service("crmSupplierExportServivce")
@Slf4j
public class CrmSupplierExportServivceImpl implements BaseExportQueryDataService<QueryCrmAgencyPageListRequest> {
    private final static LinkedList<String> FILED= new LinkedList<>();
    @DubboReference
    private CrmEnterpriseApi crmEnterpriseApi;
    @DubboReference
    private CrmSupplierApi crmSupplierApi;
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
        FILED.add("id");//客户编码
        FILED.add("code");//CRM编码
        FILED.add("ylCode");//以岭编码
        FILED.add("name");//客户名称企业全称（客户全称）
        FILED.add("businessCodeEnum");//状态名称该公司是否在系统中可参与业务（有效/无效）
        FILED.add("provinceName");//省份名称该公司所在的省份
        FILED.add("regionName");//区县名称
        FILED.add("address");//单位注册地址
        FILED.add("postalCode");//邮编
        FILED.add("phone");//办公电话
        FILED.add("fax");//传真
        FILED.add("businessRemark");//备注
        FILED.add("supplierAttributeEnum");//商业属性 城市商业/县级商业 添加枚举类
        FILED.add("generalMedicineSaleTypeEnum");//普药销售模式分销模式、招商模式、底价承包模式、KA连锁模式 添加枚举类
        FILED.add("supplierImportFlag");//重点商业 填写1/2(1代表‘是’ 2代表‘否’)
        FILED.add("headChainFlag");//连锁 填写1/2(1代表‘是’ 2代表‘否’)? 是连锁总部？
        FILED.add("parentSupplierCode");// 上级公司编码
        FILED.add("parentSupplierName");//上级公司名称
        FILED.add("chainKaFlagEnum");//连锁是否KA 填写KA/非KA，当连锁属性填写“是”，连锁是否KA必填
        FILED.add("countyCustomerCode");//县域客户代码
        FILED.add("countyCustomerName");//县域客户名称
        FILED.add("baseSupplierInfoEnum");//基药商信息 基药配送商/基药促销商 添加枚举类
        FILED.add("supplierSaleTypeEnum");//商业销售类型 填写分销型/纯销型/综合型 添加枚举类
        FILED.add("patentAgreementTypeEnum");//专利药协议类型 全产品一级(六大专利药产品),专利一级(六大专利药产品之一),OTC直供(连花36粒),全产品二级(六大专利药产品),专利二级(六大专利药产品之一),不签协议 添加枚举类
        FILED.add("generalMedicineLevelEnum");//普药协议类型 协议普药一级/协议普药二级/非协议普药一级/非协议普药二级/不签协议 添加枚举类
        FILED.add("bindGeneralMedicineFlagEnum");//是否捆绑普药 填写是/否
        FILED.add("supplierAddress");//收货地址
        FILED.add("mnemonicCode");//助记码
        FILED.add("contractFlagEnum");//是否承包
        FILED.add("flowJobNumber");//流向打取人工号
        FILED.add("flowLiablePerson");//流向打取人姓名
        FILED.add("ae");
        FILED.add("af");
        FILED.add("ag");
        FILED.add("ah");
        FILED.add("ai");
        FILED.add("aj");
        FILED.add("ak");
        FILED.add("al");
        FILED.add("am");
        FILED.add("an");
        FILED.add("ao");
        FILED.add("ap");
        FILED.add("aq");
        FILED.add("ar");
        FILED.add("as");
        FILED.add("at");
        FILED.add("au");
        FILED.add("av");
        FILED.add("aw");
        FILED.add("ax");
        FILED.add("ay");
        FILED.add("az");
        FILED.add("ba");
        FILED.add("bb");
        FILED.add("bc");
        FILED.add("bd");
        PRODUCT_GROUP.add("商务管理部普药产品组");
        PRODUCT_GROUP.add("商务管理部专利药产品组");
        PRODUCT_GROUP.add("分销普药产品组");
        PRODUCT_GROUP.add("连锁处方药产品组");
        PRODUCT_GROUP.add("商务分销部产品组");
        PRODUCT_GROUP.add("数字化标准产品组");

    }
    static {
        List<AgencyDepartGrroupResolve> g1=Arrays.asList(
                AgencyDepartGrroupResolve.builder().tagetFiled("ag").sourceFiled("yxArea").build(),
                AgencyDepartGrroupResolve.builder().tagetFiled("ah").sourceFiled("empId").build(),
                AgencyDepartGrroupResolve.builder().tagetFiled("ai").sourceFiled("empName").build());
        List<AgencyDepartGrroupResolve> g2=Arrays.asList(
                AgencyDepartGrroupResolve.builder().tagetFiled("ak").sourceFiled("yxArea").build(),
                AgencyDepartGrroupResolve.builder().tagetFiled("al").sourceFiled("empId").build(),
                AgencyDepartGrroupResolve.builder().tagetFiled("am").sourceFiled("empName").build());
        List<AgencyDepartGrroupResolve> g3=Arrays.asList(
                AgencyDepartGrroupResolve.builder().tagetFiled("ao").sourceFiled("yxArea").build(),
                AgencyDepartGrroupResolve.builder().tagetFiled("ap").sourceFiled("empId").build(),
                AgencyDepartGrroupResolve.builder().tagetFiled("aq").sourceFiled("empName").build());
        List<AgencyDepartGrroupResolve> g4=Arrays.asList(
                AgencyDepartGrroupResolve.builder().tagetFiled("as").sourceFiled("yxArea").build(),
                AgencyDepartGrroupResolve.builder().tagetFiled("at").sourceFiled("empId").build(),
                AgencyDepartGrroupResolve.builder().tagetFiled("au").sourceFiled("empName").build());
        List<AgencyDepartGrroupResolve> g5=Arrays.asList(
                AgencyDepartGrroupResolve.builder().tagetFiled("aw").sourceFiled("yxArea").build(),
                AgencyDepartGrroupResolve.builder().tagetFiled("ax").sourceFiled("empId").build(),
                AgencyDepartGrroupResolve.builder().tagetFiled("ay").sourceFiled("empName").build());
        List<AgencyDepartGrroupResolve> g6=Arrays.asList(
                AgencyDepartGrroupResolve.builder().tagetFiled("ba").sourceFiled("yxArea").build(),
                AgencyDepartGrroupResolve.builder().tagetFiled("bb").sourceFiled("empId").build(),
                AgencyDepartGrroupResolve.builder().tagetFiled("bc").sourceFiled("empName").build());
        agencyDepartGrroupResolveMap.put("商务管理部普药产品组",g1);
        agencyDepartGrroupResolveMap.put("商务管理部专利药产品组",g2);
        agencyDepartGrroupResolveMap.put("分销普药产品组",g3);
        agencyDepartGrroupResolveMap.put("连锁处方药产品组",g4);
        agencyDepartGrroupResolveMap.put("商务分销部产品组",g5);
        agencyDepartGrroupResolveMap.put("数字化标准产品组",g6);
    }

    @Override
    public QueryExportDataDTO queryData(QueryCrmAgencyPageListRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        List<Map<String, Object>> data = new ArrayList<>();
        if(null==request.getSjmsUserDatascopeBO()){
           return setResultParam(result,data);
        }
        //
        QueryDepartProductGroupByUploadNameRequest departRequest=new QueryDepartProductGroupByUploadNameRequest();
        departRequest.setUploadNames( PRODUCT_GROUP.stream().collect(Collectors.toList()));
        departRequest.setSupplyChainRole(1);
        List<CrmDepartmentProductRelationDTO> departProductGroupListByUploadName = crmDepartProductGroupApi.getDepartProductGroupListByUploadName(departRequest);
        //产品组和导出产品组对应关系
        Map<String,CrmDepartmentProductRelationDTO> departmentProductRelationDTOMap=departProductGroupListByUploadName.stream().collect(Collectors.toMap(CrmDepartmentProductRelationDTO::getProductGroup,m->m,(v1,v2)->v1));
        //需要循环调用
        Page<CrmEnterpriseDTO> page=null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            //数据处理部分
            //  查询导出的数据填入data
            //供应商角色
            request.setSupplyChainRole(1);
            page = crmEnterpriseApi.getCrmEnterpriseInfoPage(request);
            List<CrmEnterpriseDTO> records = page.getRecords();
            if (CollUtil.isEmpty(records)) {
                continue;
            }
           //组装数据
            List<Long> crmEnterIds = page.getRecords().stream().map(CrmEnterpriseDTO::getId).collect(Collectors.toList());
            List<CrmSupplierDTO> crmSupplierDTOS = crmSupplierApi.getSupplierInfoByCrmEnterId(crmEnterIds);

            List<String> parentCrmEenterIds=Optional.ofNullable(crmSupplierDTOS.stream().filter(m-> StringUtils.isNotBlank(m.getParentSupplierCode())).map(CrmSupplierDTO::getParentSupplierCode).collect(Collectors.toList())).orElse(Lists.newArrayList());
            // 获取上级公司的名称
            List<CrmEnterpriseNameDTO>    crmEnterpriseNameDTOS=CollectionUtil.isEmpty(parentCrmEenterIds)?Lists.newArrayList():crmSupplierApi.getCrmEnterpriseNameListById(parentCrmEenterIds);
            //上级名称去重
            Map<Long,CrmEnterpriseNameDTO> crmEnterpriseNameMap= crmEnterpriseNameDTOS.stream().collect(Collectors.toMap(CrmEnterpriseNameDTO::getId,m->m,(v1,v2)->v1));
            //流向打取人工号获取批量
            List<String> flowJobEmpIds=Optional.ofNullable(crmSupplierDTOS.stream().filter(m->StringUtils.isNotBlank(m.getFlowJobNumber())).map(CrmSupplierDTO::getFlowJobNumber).collect(Collectors.toList())).orElse(Lists.newArrayList());
            //流向打取人人员列表
            List<EsbEmployeeDTO> flowEsbEmployeeDTOS=CollectionUtil.isEmpty(flowJobEmpIds)?Lists.newArrayList():esbEmployeeApi.listByEmpIds(flowJobEmpIds);
            //流向打取人人员列表Map
            Map<String,EsbEmployeeDTO> flowEsbEmployeeDTOSMap=flowEsbEmployeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getEmpId,Function.identity()));
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

            List<ExportCrmSupplierInfoBO> boList=new ArrayList<>();
            Map<Long, CrmSupplierDTO> crmSupplierMap = Optional.ofNullable(crmSupplierDTOS.stream().collect(Collectors.toMap(CrmSupplierDTO::getCrmEnterpriseId, Function.identity()))).orElse(Maps.newHashMap());
            //当前页面数据封装
            page.getRecords().forEach(item -> {
                //转化基本信息
                ExportCrmSupplierInfoBO bo = PojoUtils.map(item, ExportCrmSupplierInfoBO.class);
                //转化扩展信息
                CrmSupplierDTO crmSupplierDTO = crmSupplierMap.get(item.getId());
                if (ObjectUtil.isNotEmpty(crmSupplierDTO)) {
                    PojoUtils.map(crmSupplierDTO, bo);
                    bo.setId(item.getId());//机构编码
                }
                bo.setBusinessCodeEnum(null != item.getBusinessCode() && 1 == item.getBusinessCode() ? "有效" : "无效");
                bo.setSupplierAttributeEnum(CrmSupplierAttributeEnum.getNameByCode(bo.getSupplierAttribute()));
                bo.setGeneralMedicineSaleTypeEnum(CrmGeneralMedicineSaleTypeEnum.getNameByCode(bo.getGeneralMedicineSaleType()));
                //重点商业
                bo.setSupplierImportFlag(getFlagDefalutValue(crmSupplierDTO==null?null:crmSupplierDTO.getSupplierImportFlag()));
                //
                bo.setHeadChainFlag(getFlagDefalutValue(crmSupplierDTO==null?null:crmSupplierDTO.getChainKaFlag()));
                //连锁是否ka
                bo.setChainKaFlagEnum(transAndKaFlag(bo.getChainKaFlag()));
                //基药商信息
                bo.setBaseSupplierInfoEnum(CrmBaseSupplierInfoEnum.getNameByCode(bo.getBaseSupplierInfo()));
                //商业销售类型
                bo.setSupplierSaleTypeEnum(CrmSupplierSaleTypeEnum.getNameByCode(bo.getSupplierSaleType()));
                //专利药协议类型
                bo.setPatentAgreementTypeEnum(CrmPatentAgreementTypeEnum.getNameByCode(bo.getPatentAgreementType()));
                //普药协议类型
                bo.setGeneralMedicineLevelEnum(CrmGeneralMedicineLevelEnum.getNameByCode(bo.getGeneralMedicineLevel()));
                //是否捆绑普药
                bo.setBindGeneralMedicineFlagEnum(transAndNoFlag(bo.getBindGeneralMedicineFlag()));
                bo.setContractFlagEnum(transAndNoFlag(bo.getContractFlag()));
                if(StringUtils.isNotBlank(bo.getParentSupplierCode())){
                    CrmEnterpriseNameDTO crmEnterpriseNameDTO = crmEnterpriseNameMap.get(bo.getParentSupplierCode());
                    if(Objects.nonNull(crmEnterpriseNameDTO)){
                        bo.setParentSupplierName(crmEnterpriseNameDTO.getName());
                    }
                }
                if(StringUtils.isNotBlank(bo.getFlowJobNumber())){
                    EsbEmployeeDTO esbEmployeeDTO = flowEsbEmployeeDTOSMap.get(bo.getFlowJobNumber());
                    if(Objects.nonNull(esbEmployeeDTO)){
                        bo.setFlowLiablePerson(esbEmployeeDTO.getEmpName());
                    }
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
                               mapResolveEmpToBoField(b,esbEmployeeDTO,bo);
                           });
                        }
                    }
                });
                //产品组处理数据 商务管理部普药产品组
                //枚举值处理
                Map<String, Object> dataPojo = BeanUtil.beanToMap(bo);
                data.add(dataPojo);
            });

            current = current + 1;
        }while (CollectionUtils.isNotEmpty(page.getRecords()));


        return setResultParam(result,data);
    }

    @Override
    public QueryCrmAgencyPageListRequest getParam(Map<String, Object> map) {
        QueryCrmAgencyPageListRequest request= PojoUtils.map(map, QueryCrmAgencyPageListRequest.class);
        String empId=map.getOrDefault("currentUserCode","").toString();
        SjmsUserDatascopeBO byEmpId = sjmsUserDatascopeApi.getByEmpId(empId);
        log.info("基础档案供应商导出获取权限:empId={},longs={}",empId,byEmpId);
        request.setSjmsUserDatascopeBO(getLongs(byEmpId));
        return request;
    }

    /**
     * 第一个产品组数据
     */
    private void resovleExportBOToAgAndAj(){

    }

    private String transAndNoFlag(Integer flag){
        if(flag==null ||flag==0)return null;
        else if (flag.intValue()==2) {
            return "否";
        } else if (flag.intValue()==1) {
            return "是";
        }
        return null;
    }
    private String transAndKaFlag(Integer flag){
        if(flag==null ||flag==0)return null;
        else if (flag.intValue()==2) {
            return "非KA";
        } else if (flag.intValue()==1) {
            return "KA";
        }
        return null;
    }
    private Integer getFlagDefalutValue(Integer intFlag){
        if(intFlag !=null && intFlag==0){
            return null;
        }
        return  intFlag;
    }
    public String getFormReationMapKey(CrmEnterpriseRelationPostDTO form){
        return StringUtils.isEmpty(form.getProductGroup())?"":form.getProductGroup() + toStringNpe(form.getCrmEnterpriseId());
    }

    public  void mapResolveEmpToBoField(AgencyDepartGrroupResolve agencyDepartGrroupResolve,Object sourceObject,Object tagetObject){
        try {
            Field sourceDeclaredField = EsbEmployeeDTO.class.getDeclaredField(agencyDepartGrroupResolve.getSourceFiled());
            sourceDeclaredField.setAccessible(true);
            Field targetDeclaredField = ExportCrmSupplierInfoBO.class.getDeclaredField(agencyDepartGrroupResolve.getTagetFiled());
            targetDeclaredField.setAccessible(true);
            Object sourceFiledValue = sourceDeclaredField.get(sourceObject);

            targetDeclaredField.set(tagetObject,sourceFiledValue);
        }catch (Exception e){
            log.error("导出属性转化失败:{}",e);
        }
    }

    public String toStringNpe(Long var){
        return ObjectUtil.isNull(var) ? "" : var.toString();
    }
    private QueryExportDataDTO setResultParam(QueryExportDataDTO result,List<Map<String, Object>> data){
        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("dtinfo");
        // 页签字段
        exportDataDTO.setTempleteParamList(FILED);
        // 页签数据
        exportDataDTO.setData(data);
        exportDataDTO.setFristRow(3);
        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        result.setUseExcelTemplete(true);
        result.setExcelTempletePath("templete/excel/crm_supplier_templete.xlsx");
        return result;
    }
    private  SjmsUserDatascopeBO getLongs(SjmsUserDatascopeBO byEmpId){
        if(OrgDatascopeEnum.NONE==OrgDatascopeEnum.getFromCode(byEmpId.getOrgDatascope())){
            return  null;
        }
        return  byEmpId;
    }
}
