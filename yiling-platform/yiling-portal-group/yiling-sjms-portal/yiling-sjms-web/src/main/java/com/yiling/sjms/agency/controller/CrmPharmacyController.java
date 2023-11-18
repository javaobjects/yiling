package com.yiling.sjms.agency.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.dataflow.crm.api.CrmGoodsGroupApi;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
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
import com.yiling.dataflow.agency.api.CrmPharmacyApi;
import com.yiling.dataflow.agency.api.CrmSupplierApi;
import com.yiling.dataflow.agency.dto.CrmPharmacyDTO;
import com.yiling.dataflow.agency.dto.CrmSupplierDTO;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyPageListRequest;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseByNamePageListRequest;
import com.yiling.dataflow.agency.dto.request.RemoveCrmPharmacyRequest;
import com.yiling.dataflow.agency.dto.request.SaveCrmPharmacyRequest;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.form.QueryCrmAgencyPageListForm;
import com.yiling.sjms.agency.form.QueryCrmEnterpriseInfoByNamePageListForm;
import com.yiling.sjms.agency.form.SaveCrmPharmacyForm;
import com.yiling.sjms.agency.form.SaveCrmRelationShipForm;
import com.yiling.sjms.gb.api.GbOrgMangerApi;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import static com.yiling.sjms.agency.controller.CrmHospitalController.changeField;

/**
 * <p>
 * 零售机构档案扩展表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-14
 */
@Slf4j
@Api(tags = "零售机构档案接口")
@RestController
@RequestMapping("/crm/agency/pharmacy")
public class CrmPharmacyController extends BaseController {

    @DubboReference(timeout = 10 * 1000)
    CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    CrmPharmacyApi crmPharmacyApi;

    @DubboReference
    LocationApi locationApi;

    @DubboReference
    CrmHosptialApi crmHosptialApi;

    @DubboReference
    CrmSupplierApi crmSupplierApi;

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
    @ApiOperation(value = "列表查询")
    @PostMapping("/pageList")
    public Result<Page<CrmPharmacyPageListVO>> pageList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryCrmAgencyPageListForm form) {
        // 获取权限
        SjmsUserDatascopeBO byEmpId = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        //代表没权限返回错误
        //代表没权限返回错误
        if(OrgDatascopeEnum.NONE==OrgDatascopeEnum.getFromCode(byEmpId.getOrgDatascope())){
            return Result.success(new Page<>());
        }
        QueryCrmAgencyPageListRequest request = PojoUtils.map(form, QueryCrmAgencyPageListRequest.class);
        request.setSupplyChainRole(3);
        request.setSjmsUserDatascopeBO(byEmpId);
        Page<CrmEnterpriseDTO> dtoPage = crmEnterpriseApi.getCrmEnterpriseInfoPage(request);
        Page<CrmPharmacyPageListVO> voPage = PojoUtils.map(dtoPage, CrmPharmacyPageListVO.class);
        if (CollUtil.isEmpty(voPage.getRecords())) {
            return Result.success(voPage);
        }

        List<Long> idList = voPage.getRecords().stream().map(CrmPharmacyPageListVO::getId).collect(Collectors.toList());
        List<CrmPharmacyDTO> pharmacyDTOList = crmPharmacyApi.listByCrmEnterpriseId(idList);
        Map<Long, CrmPharmacyDTO> pharmacyDTOMap = pharmacyDTOList.stream().collect(Collectors.toMap(CrmPharmacyDTO::getCrmEnterpriseId, e -> e, (k1, k2) -> k1));
        for (CrmPharmacyPageListVO record : voPage.getRecords()) {
            CrmPharmacyDTO crmPharmacyDTO = pharmacyDTOMap.get(record.getId());
            if (Objects.isNull(crmPharmacyDTO)) {
                continue;
            }
            record.setAgreement(crmPharmacyDTO.getAgreement());
            record.setPharmacyAttribute(crmPharmacyDTO.getPharmacyAttribute());
            record.setPharmacyType(crmPharmacyDTO.getPharmacyType());
            record.setPharmacyLevel(crmPharmacyDTO.getPharmacyLevel());
            record.setLabelAttribute(crmPharmacyDTO.getLabelAttribute());
        }
        return Result.success(voPage);
    }

    @ApiOperation("新增/编辑")
    @PostMapping("/save")
    public Result<Boolean> save(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveCrmPharmacyForm form) {
        SaveCrmPharmacyRequest request = PojoUtils.map(form, SaveCrmPharmacyRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setName(form.getCrmEnterpriseName());
        request.setCrmEnterpriseId(form.getCrmEnterpriseId());
        if (1 == request.getPharmacyAttribute() && Objects.isNull(request.getPharmacyType())) {
            Result.failed("连锁分店药店类型不能为空");
        }
        List<CrmEnterpriseRelationShipDTO> sourceRelationship = crmSupplierApi.getRelationByCrmEnterpriseId(form.getCrmEnterpriseId(), form.getCrmEnterpriseName());
        boolean repeatFlag = isRepeatEnterpirseRelation(form.getCrmRelationShip(), sourceRelationship, form.getCrmEnterpriseName());
        if (repeatFlag) {
            return Result.failed("三者关系有重复数据请检查");
        }
        if (StringUtils.isNotBlank(request.getParentCompanyCode())) {
            CrmEnterpriseDTO parentEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(Long.parseLong(request.getParentCompanyCode()));
            if (Objects.isNull(parentEnterpriseDTO)) {
                request.setParentCompanyCode("");
                request.setParentCompanyName("");
            } else {
                request.setParentCompanyName(parentEnterpriseDTO.getName());
            }
        }

        boolean isSuccess = crmPharmacyApi.saveCrmPharmacy(request);
        if (isSuccess) {
            return Result.success(true);
        }
        return Result.failed("保存失败");
    }

    @ApiOperation("详情查看")
    @GetMapping("/query")
    public Result<CrmPharmacyQueryVO> query(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "crmEnterId") Long crmEnterId) {
        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(crmEnterId);
        CrmPharmacyQueryVO crmPharmacyDetailVO = new CrmPharmacyQueryVO();
        if (Objects.isNull(crmEnterpriseDTO)) {
            return Result.success(crmPharmacyDetailVO);
        }

        CrmPharmacyDTO crmPharmacyDTO = crmPharmacyApi.queryByEnterpriseId(crmEnterId);
        if (Objects.nonNull(crmPharmacyDTO)) {
            PojoUtils.map(crmPharmacyDTO, crmPharmacyDetailVO);
            // DIH-零售机构档案，当企业所属上级公司的名称发生改变或上级公司档案被删除，对应的下级企业关联的上级公司信息应随之更新
            String parentCompanyCode = crmPharmacyDTO.getParentCompanyCode();
            if (StringUtils.isNotBlank(parentCompanyCode)) {
                CrmEnterpriseDTO enterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(Long.parseLong(parentCompanyCode));
                if (Objects.isNull(enterpriseDTO)) {
                    crmPharmacyDetailVO.setParentCompanyCode("");
                    crmPharmacyDetailVO.setParentCompanyName("");
                    crmPharmacyDetailVO.setChainAttribute(0);
                } else {
                    crmPharmacyDetailVO.setParentCompanyName(enterpriseDTO.getName());
                    CrmSupplierDTO crmSupplierDTO = crmSupplierApi.getCrmSupplierByCrmEnterId(enterpriseDTO.getId());
                    crmPharmacyDetailVO.setChainAttribute(0);
                    if (Objects.nonNull(crmSupplierDTO)) {
                        crmPharmacyDetailVO.setChainAttribute(crmSupplierDTO.getChainAttribute());
                    }
                }
            }
        }
        crmPharmacyDetailVO.setCrmEnterpriseId(crmEnterId);
        List<CrmEnterpriseRelationShipDTO> relationship = crmHosptialApi.getRelationByCrmEnterpriseId(crmEnterId, crmEnterpriseDTO.getName());
        buildEsbEmployInfo(relationship,false);
        relationship.forEach(item -> {
            item.setSupplyChainRole(crmEnterpriseDTO.getSupplyChainRole() + "");
        });
        crmPharmacyDetailVO.setRelationshipDetail(PojoUtils.map(relationship, CrmRelationshiplDetailVO.class));
        return Result.success(crmPharmacyDetailVO);
    }

    @ApiOperation("通过上级公司名称查询基础和商业公司扩展信息")
    @PostMapping("/getCrmSupplierByName")
    Result<Page<CrmSupplierDetailsVO>> getCrmSupplierByName(@RequestBody QueryCrmEnterpriseInfoByNamePageListForm form) {
        QueryCrmEnterpriseByNamePageListRequest request = PojoUtils.map(form, QueryCrmEnterpriseByNamePageListRequest.class);
        Page<CrmEnterpriseDTO> crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseByName(request);
        Page<CrmSupplierDetailsVO> voPage = PojoUtils.map(crmEnterpriseDTO, CrmSupplierDetailsVO.class);
        if (CollUtil.isEmpty(voPage.getRecords())) {
            return Result.success(form.getPage());
        }
        List<Long> idList = voPage.getRecords().stream().map(CrmSupplierDetailsVO::getId).collect(Collectors.toList());
        List<CrmSupplierDTO> supplierDTOList = crmSupplierApi.getSupplierInfoByCrmEnterId(idList);
        Map<Long, CrmSupplierDTO> supplierDTOMap = supplierDTOList.stream().collect(Collectors.toMap(CrmSupplierDTO::getCrmEnterpriseId, e -> e, (k1, k2) -> k1));
        for (CrmSupplierDetailsVO detailsVO : voPage.getRecords()) {
            CrmSupplierDTO crmSupplierDTO = supplierDTOMap.get(detailsVO.getId());
            if (null == crmSupplierDTO) {
                continue;
            }
            PojoUtils.map(crmSupplierDTO, detailsVO);
            detailsVO.setId(crmSupplierDTO.getCrmEnterpriseId());
        }
        return Result.success(voPage);
    }

    @ApiOperation(("删除"))
    @GetMapping("/remove")
    public Result<Boolean> remove(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        RemoveCrmPharmacyRequest request = new RemoveCrmPharmacyRequest();
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setCrmEnterpriseId(id);
        boolean isSuccess = crmPharmacyApi.removeByEnterpriseId(request);
        if (isSuccess) {
            return Result.success(true);
        }
        return Result.failed("删除失败");
    }

    /**
     * 是否重复三者关系数据
     *
     * @param historyRelationship
     * @return
     */
    private boolean isRepeatEnterpirseRelation(List<SaveCrmRelationShipForm> formRelationShip, List<CrmEnterpriseRelationShipDTO> historyRelationship, String name) {
        //过滤下页面传递过来旧的新增的
        List<SaveCrmRelationShipForm> saveCrmRelationshipRequestList = formRelationShip.stream().filter(item -> ObjectUtil.isNull(item.getId())).collect(Collectors.toList());
        List<CrmEnterpriseRelationShipDTO> formSaveRelationShip = PojoUtils.map(saveCrmRelationshipRequestList, CrmEnterpriseRelationShipDTO.class);
        formSaveRelationShip.stream().forEach(item -> item.setCustomerName(name));
        //2个list合并form表单和数据库保存数据
        historyRelationship.addAll(formSaveRelationShip);
        //检查是否重复 唯一key 岗位编码+产品组+经销商为key
        List<String> formRelationShipKey = historyRelationship.stream().map(this::getFormReationMapKey).collect(Collectors.toList());
        long count = formRelationShipKey.stream().distinct().count();
        return count < historyRelationship.size();
    }

    public String getFormReationMapKey(CrmEnterpriseRelationShipDTO form) {
        return toStringNpe(form.getProductGroupId());
    }

    public String toStringNpe(Long var){
        return ObjectUtil.isNull(var) ? "" : var.toString();
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
                List<String> jobNamesList = ListUtil.toList("省区经理", "省区主管");
                List<EsbEmployeeDTO> provincialManagerList = esbEmployeeApi.getProvincialManagerByYxDeptAndYxProvinceAndJobNames(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince(),null, jobNamesList);
                if (CollUtil.isNotEmpty(provincialManagerList)) {
                    EsbEmployeeDTO provincialManager = provincialManagerList.get(0);
                    item.setProvincialManagerPostCode(provincialManager.getJobId());
                    item.setProvincialManagerPostName(provincialManager.getJobName());
                    item.setProvincialManagerCode(provincialManager.getEmpId());
                    item.setProvincialManagerName(provincialManager.getEmpName());
                }
            }
        });
    }
}
