package com.yiling.sjms.agency.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


import javax.validation.Valid;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.api.CrmGoodsGroupApi;
import com.yiling.dataflow.crm.api.CrmManorApi;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.dto.CrmManorDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorParamRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.sjms.agency.form.SaveCrmRelationShipForm;

import com.yiling.sjms.agency.vo.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.dataflow.agency.api.CrmHosptialApi;
import com.yiling.dataflow.agency.dto.CrmHospitalDTO;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyPageListRequest;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseByNamePageListRequest;
import com.yiling.dataflow.agency.dto.request.SaveCrmHosptialRequest;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.form.QueryCrmAgencyPageListForm;
import com.yiling.sjms.agency.form.QueryCrmEnterpriseInfoByNamePageListForm;
import com.yiling.sjms.agency.form.SaveCrmHosptialForm;

import com.yiling.sjms.gb.api.GbOrgMangerApi;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


/**
 * <p>
 * 医疗机构拓展表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-14
 */
@Slf4j
@RestController
@RequestMapping("/crm/agency/hospital")
@Api(tags = "医疗机构档案接口")
public class CrmHospitalController extends BaseController {

    @DubboReference
    CrmHosptialApi crmHosptialApi;

    @DubboReference(timeout = 10 * 1000)
    CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    LocationApi locationApi;

    @DubboReference
    EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    EsbOrganizationApi esbOrganizationApi;

    @DubboReference
    CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;

    @DubboReference
    GbOrgMangerApi gbOrgMangerApi;

    @DubboReference
    BusinessDepartmentApi businessDepartmentApi;

    @DubboReference
    CrmGoodsGroupApi crmGoodsGroupApi;

    @DubboReference
    SjmsUserDatascopeApi sjmsUserDatascopeApi;
    @DubboReference
    private CrmManorApi crmManorApi;
    @DubboReference
    private CrmGoodsCategoryApi crmGoodsCategoryApi;
    @ApiOperation(value = "列表查询")
    @PostMapping("/pageList")
    public Result<Page<CrmHosptialPageListVO>> pageList(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestBody QueryCrmAgencyPageListForm form) {
        SjmsUserDatascopeBO byEmpId = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        //代表没权限返回错误
        //代表没权限返回错误
        if(OrgDatascopeEnum.NONE==OrgDatascopeEnum.getFromCode(byEmpId.getOrgDatascope())){
            return Result.success(new Page<>());
        }
        QueryCrmAgencyPageListRequest request = PojoUtils.map(form, QueryCrmAgencyPageListRequest.class);
        request.setSupplyChainRole(2);
        request.setSjmsUserDatascopeBO(byEmpId);
        Page<CrmEnterpriseDTO> crmEnterpriseDTOS = crmEnterpriseApi.getCrmEnterpriseInfoPage(request);
        if (CollectionUtil.isEmpty(crmEnterpriseDTOS.getRecords())) {
            return Result.success(new Page<CrmHosptialPageListVO>());
        }
        Page<CrmHosptialPageListVO> crmHosptialPageListVOPage = PojoUtils.map(crmEnterpriseDTOS, CrmHosptialPageListVO.class);
        List<Long> crmEnterIds = crmEnterpriseDTOS.getRecords().stream().map(CrmEnterpriseDTO::getId).collect(Collectors.toList());
        List<CrmHospitalDTO> crmHospitalDTOS = crmHosptialApi.getHosptialInfoByCrmEnterId(crmEnterIds);
        if (CollectionUtil.isNotEmpty(crmHospitalDTOS)) {
            Map<Long, CrmHospitalDTO> hospitalDTOMap = crmHospitalDTOS.stream().collect(Collectors.toMap(CrmHospitalDTO::getCrmEnterpriseId, Function.identity()));
            crmHosptialPageListVOPage.getRecords().forEach(item -> {
                CrmHospitalDTO crmHospitalDTO = hospitalDTOMap.get(item.getId());
                if (ObjectUtil.isNotEmpty(crmHospitalDTO)) {
                    item.setMedicalNature(crmHospitalDTO.getMedicalNature());
                    item.setMedicalType(crmHospitalDTO.getMedicalType());
                    item.setYlLevel(crmHospitalDTO.getYlLevel());
                    item.setNationalGrade(crmHospitalDTO.getNationalGrade());
                    item.setBaseMedicineFlag(crmHospitalDTO.getBaseMedicineFlag());
                }
            });
        }
        /*crmHosptialPageListVOPage.getRecords().forEach(item -> {
            String[] namesByCodes = locationApi.getNamesByCodes(item.getProvinceCode(), item.getCityCode(), item.getRegionCode());
            item.setAddress(item.getAddress());
        });*/
        return Result.success(crmHosptialPageListVOPage);
    }


    @ApiOperation(value = "添加和修改医疗机构")
    @PostMapping("/saveOrUpdate")
    public Result<Boolean> addCustomer(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveCrmHosptialForm form){
      //  List<CrmEnterpriseRelationShipDTO> sourceRelationship=crmHosptialApi.getRelationByCrmEnterpriseId(form.getCrmEnterpriseId(),form.getCrmEnterpriseName());
        //boolean repeatFlag=isRepeatEnterpirseRelation(form.getCrmRelationShip(),sourceRelationship,form.getCrmEnterpriseName());
//        if(repeatFlag){
//            return Result.failed("三者关系有重复数据请检查");
//        }
        SaveCrmHosptialRequest saveCrmHosptialRequest = PojoUtils.map(form, SaveCrmHosptialRequest.class);
        saveCrmHosptialRequest.setOpUserId(userInfo.getCurrentUserId());
        saveCrmHosptialRequest.setName(form.getCrmEnterpriseName());
        saveCrmHosptialRequest.setCrmEnterpriseId(form.getCrmEnterpriseId());
        Boolean result = crmHosptialApi.saveEnterpriseInfoAndHosptialInfo(saveCrmHosptialRequest);
        return Result.success(result);
    }

    @ApiOperation(value = "医疗删除")
    @GetMapping("/remove")
    public Result<Page<CrmSupplierPageListVO>> removeSupplierById(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestParam(value = "id",required = true)String id) {
        crmHosptialApi.removeCrmSupplierById(id);
        return Result.success();
    }

    @ApiOperation(value = "医疗机构详情")
    @GetMapping("/detail")
    public Result<CrmHosptialQueryVO> detail(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "crmEnterId",required = true)Long crmEnterId){
        CrmEnterpriseDTO crmEnterpriseDTO=  crmEnterpriseApi.getCrmEnterpriseById(crmEnterId);
        if(ObjectUtil.isEmpty(crmEnterpriseDTO)){
            return Result.success(new CrmHosptialQueryVO());
        }

        CrmHospitalDTO crmHospitalDTO=crmHosptialApi.getCrmSupplierByCrmEnterId(crmEnterId.toString());
        CrmHosptialQueryVO detailsVO=new CrmHosptialQueryVO();
        if(ObjectUtil.isNotEmpty(crmHospitalDTO)){
            PojoUtils.map(crmHospitalDTO,detailsVO);
        }
        List<CrmEnterpriseRelationShipDTO>relationship=crmHosptialApi.getRelationByCrmEnterpriseId(crmEnterId,crmEnterpriseDTO.getName());
        buildEsbEmployInfo(relationship,false);
        relationship.forEach(item->{
            item.setSupplyChainRole(crmEnterpriseDTO.getSupplyChainRole()+"");
        });
        //辖区名称和品类名称处理
        buildManorAndCategoryName(relationship);
        detailsVO.setRelationshiplDetail(PojoUtils.map(relationship, CrmRelationshiplDetailVO.class));
        return Result.success(detailsVO);
    }



    @ApiOperation(value = "通过机构名称，获取机构详情")
    @PostMapping("/getCrmEnterpriseByName")
    public Result<Page<CrmHosptialDetailVO>> getCrmEnterpriseByName( @RequestBody QueryCrmEnterpriseInfoByNamePageListForm form){
        QueryCrmEnterpriseByNamePageListRequest request = PojoUtils.map(form, QueryCrmEnterpriseByNamePageListRequest.class);
        Page<CrmEnterpriseDTO> crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseByName(request);
        return Result.success(PojoUtils.map(crmEnterpriseDTO,CrmHosptialDetailVO.class));
    }

    /**
     * 是否重复三者关系数据
     * @param historyRelationship
     * @return
     */
    private boolean isRepeatEnterpirseRelation(List<SaveCrmRelationShipForm> formRelationShip, List<CrmEnterpriseRelationShipDTO> historyRelationship,String name){
        //过滤下页面传递过来旧的新增的
        List<SaveCrmRelationShipForm> saveCrmRelationshipRequestList = formRelationShip.stream().filter(item -> ObjectUtil.isNull(item.getId())).collect(Collectors.toList());
        List<CrmEnterpriseRelationShipDTO> formSaveRelationShip=PojoUtils.map(saveCrmRelationshipRequestList,CrmEnterpriseRelationShipDTO.class);
        formSaveRelationShip.stream().forEach(item->item.setCustomerName(name));
        //2个list合并form表单和数据库保存数据
        historyRelationship.addAll(formSaveRelationShip);
        //检查是否重复 唯一key 岗位编码+产品组+经销商为key
        List<String> formRelationShipKey=historyRelationship.stream().map(this::getFormReationMapKey).collect(Collectors.toList());
        long count = formRelationShipKey.stream().distinct().count();
        return  count<historyRelationship.size();
    }

    public String getFormReationMapKey(CrmEnterpriseRelationShipDTO form){
        return ObjectUtil.isNull(form.getProductGroupId())?"":form.getProductGroupId().toString();
    }

    public static void changeField(CrmEnterpriseRelationShipDTO item, EsbEmployeeDTO esbEmployeeDTO) {
        if(ObjectUtil.isNull(esbEmployeeDTO)){
            return;
        }
        item.setBusinessDepartment(esbEmployeeDTO.getYxDept());
        item.setBusinessProvince(esbEmployeeDTO.getYxProvince());
        item.setBusinessArea(esbEmployeeDTO.getYxArea());
        item.setSuperiorSupervisorCode(esbEmployeeDTO.getSuperior());
        item.setSuperiorSupervisorName(esbEmployeeDTO.getSuperiorName());
        item.setRepresentativeCode(esbEmployeeDTO.getEmpId());
        item.setRepresentativeName(esbEmployeeDTO.getEmpName());
        item.setCustomerCode(esbEmployeeDTO.getEmpId());
        item.setPostCode(esbEmployeeDTO.getJobId());
        item.setPostName(esbEmployeeDTO.getJobName());
    }

    public void buildEsbEmployInfo(List<CrmEnterpriseRelationShipDTO> crmEnterpriseDTOS, boolean needProvinceInfo) {
        // 用岗位id名字postcode找到人员信息，业务部门，省区，业务区域等
        List<Long> postCodes = crmEnterpriseDTOS.stream().filter(item->!(item.getPostCode().equals(0L))).map(CrmEnterpriseRelationShipDTO::getPostCode).collect(Collectors.toList());
        if(CollectionUtil.isEmpty(postCodes)){
            return;
        }
        List<EsbEmployeeDTO> esbEmployeeDTOS = esbEmployeeApi.listByJobIds(postCodes);
        Map<Long, EsbEmployeeDTO> esbEmployeeDTOMap=new HashMap<>();
        Map<Long, EsbOrganizationDTO> esbOrganizationDTOMap=new HashMap<>();
        List<Long> superJobId=new ArrayList<>();
        if(CollectionUtil.isNotEmpty(esbEmployeeDTOS)){
            esbEmployeeDTOMap = esbEmployeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getJobId,o -> o, (k1, k2) -> k1));
            List<Long> deptIds = esbEmployeeDTOS.stream().map(EsbEmployeeDTO::getDeptId).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(deptIds)){
                esbOrganizationDTOMap = businessDepartmentApi.listByOrgIds(deptIds);
            }
        }
        Map<Long, EsbEmployeeDTO> finalEsbEmployeeDTOMap = esbEmployeeDTOMap;
        Map<Long, EsbOrganizationDTO> finalEsbOrganizationDTOMap = esbOrganizationDTOMap;
        // 用岗位id获取esbEmploy信息
        crmEnterpriseDTOS.forEach(item -> {
            Long postCode = item.getPostCode();
            if (ObjectUtil.isNull(postCode) || postCode.equals(0L)) {
                return;
            }
            EsbEmployeeDTO esbEmployeeDTO = finalEsbEmployeeDTOMap.get(postCode);
            if (ObjectUtil.isNull(esbEmployeeDTO)) {
                return;
            }
            changeField(item, esbEmployeeDTO);
            // 获取上级主管岗位id和名称
            if(StringUtils.equals(esbEmployeeDTO.getDutyGredeId(),"1")){
                List<String> jobNamesList = ListUtil.toList("地区经理", "占位");
                List<EsbEmployeeDTO> provincialManagerList = esbEmployeeApi.getProvincialManagerByYxDeptAndYxProvinceAndJobNames(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince(), esbEmployeeDTO.getYxArea(), jobNamesList);
                if (CollUtil.isNotEmpty(provincialManagerList)) {
                    EsbEmployeeDTO esbEmployeeDTO1 = provincialManagerList.get(0);
                    item.setSuperiorJob(esbEmployeeDTO1.getJobId());
                    item.setSuperiorJobName(esbEmployeeDTO1.getJobName());
                }else{
                    item.setSuperiorJob(null);
                    item.setSuperiorJobName(null);
                    item.setSuperiorSupervisorName(null);
                    item.setSuperiorSupervisorCode(null);
                }
            }
            if(StringUtils.equals(esbEmployeeDTO.getDutyGredeId(),"2")){
                item.setSuperiorSupervisorCode(esbEmployeeDTO.getEmpId());
                item.setSuperiorSupervisorName(esbEmployeeDTO.getEmpName());
                item.setSuperiorJobName(esbEmployeeDTO.getJobName());
                item.setSuperiorJob(esbEmployeeDTO.getJobId());
                item.setRepresentativeCode(null);
                item.setRepresentativeName(null);
                item.setPostCode(null);
                item.setPostName(null);
            }
            //通过循环获取部门。
            EsbOrganizationDTO organizationDTO= finalEsbOrganizationDTOMap.get(esbEmployeeDTO.getDeptId());
            if (ObjectUtil.isNotEmpty(organizationDTO)) {
                item.setDepartment(organizationDTO.getOrgName());
            }
            //通过部门，业务部门，业务省区获取省区
            String provinceArea = crmEnterpriseRelationShipApi.getProvinceAreaByThreeParms(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince());
            item.setProvincialArea(provinceArea);
            // 省区经理，查不到则为空，查到多个则报错提示 666
            if(needProvinceInfo){
                List<String> jobNamesList1 = ListUtil.toList("省区经理", "省区主管");
                List<EsbEmployeeDTO> provincialManagerList1 = esbEmployeeApi.getProvincialManagerByYxDeptAndYxProvinceAndJobNames(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince(),null, jobNamesList1);
                if (CollUtil.isNotEmpty(provincialManagerList1)) {
                    EsbEmployeeDTO provincialManager = provincialManagerList1.get(0);
                    item.setProvincialManagerPostCode(provincialManager.getJobId());
                    item.setProvincialManagerPostName(provincialManager.getJobName());
                    item.setProvincialManagerCode(provincialManager.getEmpId());
                    item.setProvincialManagerName(provincialManager.getEmpName());
                }
            }
        });
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
}
