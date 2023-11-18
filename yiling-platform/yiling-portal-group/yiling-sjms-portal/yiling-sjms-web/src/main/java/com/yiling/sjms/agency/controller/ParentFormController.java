package com.yiling.sjms.agency.controller;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.alibaba.fastjson.JSON;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.sjms.crm.api.HospitalDrugstoreRelationFormApi;
import com.yiling.sjms.crm.dto.HospitalDrugstoreRelationExtFormDTO;
import com.yiling.sjms.flee.api.FleeingGoodsFormApi;
import com.yiling.sjms.flee.api.SaleaAppealFormApi;
import com.yiling.sjms.flee.dto.FleeingGoodsFormExtDTO;
import com.yiling.sjms.flee.dto.SalesAppealExtFormDTO;
import com.yiling.sjms.flee.vo.AppendixDetailVO;
import com.yiling.sjms.form.vo.FormVO;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.basic.no.api.NoApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.sjms.agency.form.ParentForm;
import com.yiling.sjms.flee.api.FleeingGoodsFormApi;
import com.yiling.sjms.flee.api.SaleaAppealFormApi;
import com.yiling.sjms.flee.dto.FleeingGoodsFormExtDTO;
import com.yiling.sjms.flee.dto.SalesAppealExtFormDTO;
import com.yiling.sjms.flee.vo.AppendixDetailVO;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.form.dto.request.CreateFormRequest;
import com.yiling.sjms.form.dto.request.QueryFormPageListRequest;
import com.yiling.sjms.form.dto.request.UpdateRemarkRequest;
import com.yiling.sjms.form.enums.FormNoEnum;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.form.vo.FormVO;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.sjms.util.OaTodoUtils;
import com.yiling.sjms.workflow.form.QueryFormPageForm;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 父表单（form表）
 * @author: gxl
 * @date: 2023/2/25
 */
@Slf4j
@RestController
@RequestMapping("/agency/parent")
@Api(tags = "父表单信息")
public class ParentFormController extends BaseController {
    @DubboReference
    private FormApi formApi;
    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    private NoApi noApi;
    @DubboReference
    private CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;
    @DubboReference
    private SaleaAppealFormApi saleaAppealFormApi;
    @DubboReference
    private FleeingGoodsFormApi fleeingGoodsFormApi;

    @DubboReference
    private BusinessDepartmentApi businessDepartmentApi;

    @DubboReference
    private HospitalDrugstoreRelationFormApi hospitalDrugstoreRelationFormApi;

    @Autowired
    FileService fileService;


    @ApiOperation(value = "新增或编辑")
    @PostMapping("saveParentForm")
    public Result<Long> saveParentForm(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid ParentForm parentForm){
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());

        CreateFormRequest request = new CreateFormRequest();
        String code = noApi.gen(FormNoEnum.getByCode(parentForm.getFormType()));
        String title = OaTodoUtils.genTitle(FormTypeEnum.getByCode(parentForm.getFormType()).getName(), esbEmployeeDTO.getEmpName(), code, new Date());
        request.setType(parentForm.getFormType()).setName(title).setCode(code);
        request.setRemark(parentForm.getAdjustReason()).setOpUserId(userInfo.getCurrentUserId());
        Long id = null;
        if(Objects.isNull(parentForm.getFormId())){
            id = formApi.create(request);
        }else{
            id = parentForm.getFormId();
            UpdateRemarkRequest remarkRequest = new UpdateRemarkRequest();
            remarkRequest.setId(parentForm.getFormId()).setRemark(parentForm.getAdjustReason()).setOpUserId(userInfo.getCurrentUserId());
            formApi.updateRemark(remarkRequest);
        }
        return Result.success(id);
    }

    @ApiOperation(value = "流程列表")
    @GetMapping("queryFormPage")
    public Result<Page<FormVO>> queryFormPage(@Valid QueryFormPageForm queryFormPageForm){
        QueryFormPageListRequest request = new QueryFormPageListRequest();
        PojoUtils.map(queryFormPageForm,request);
        List<Integer> excludeStatusList = Lists.newArrayList();
        excludeStatusList.add(FormStatusEnum.UNSUBMIT.getCode());
        request.setExcludeStatusList(excludeStatusList);
        Page<FormDTO> formDTOPage = formApi.pageList(request);
        Page<FormVO> formVOPage = PojoUtils.map(formDTOPage,FormVO.class);
        return Result.success(formVOPage);
    }

    @ApiOperation(value = "我的申请")
    @GetMapping("queryMyFormPage")
    public Result<Page<FormVO>> queryMyFormPage(@CurrentUser CurrentSjmsUserInfo userInfo,@Valid QueryFormPageForm queryFormPageForm){
        QueryFormPageListRequest request = new QueryFormPageListRequest();
        request.setEmpId(userInfo.getCurrentUserCode());
        List<Integer> excludeStatusList = Lists.newArrayList();
        excludeStatusList.add(FormStatusEnum.UNSUBMIT.getCode());
        request.setExcludeStatusList(excludeStatusList);
        PojoUtils.map(queryFormPageForm,request);
        Page<FormDTO> formDTOPage = formApi.pageList(request);
        Page<FormVO> formVOPage = PojoUtils.map(formDTOPage,FormVO.class);
        return Result.success(formVOPage);
    }
    @ApiOperation(value = "查询父表单信息或待提交状态")
    @GetMapping("query")
    public Result<FormVO> getParentFormInfoByIdOrDealWithSubmit(@CurrentUser CurrentSjmsUserInfo userInfo,@ApiParam(value = "表单类型",required = false) @RequestParam(value = "formType",required = false) Integer formType, @ApiParam(value = "主表单id") @RequestParam(required = false) Long formId){
       FormVO formVO=new FormVO();
        //根据ID查询
        if(Objects.nonNull(formId)){
            FormDTO formDTO=formApi.getById(formId);
            PojoUtils.map(formDTO,formVO);
            //省区为空的时候重新设置
            if(StringUtils.isEmpty(formVO.getProvince())){
                setEsbEmpProvince(formVO,userInfo);
            }
            return Result.success(formVO);
        }
        //获取当前人的待提交的数据
        List<FormDTO> formDTOS = formApi.listUnsubmitFormsByUser(FormTypeEnum.getByCode(formType), userInfo.getCurrentUserId());
        if(formDTOS !=null && formDTOS.size()>0){
            PojoUtils.map(formDTOS.get(0),formVO);
        }
        setEsbEmpProvince(formVO,userInfo);
        setApproveInfo(formType,formId,formVO);
        return Result.success(formVO);
    }
    private void setEsbEmpProvince(FormVO formVO,CurrentSjmsUserInfo userInfo){
        //如果没有待提交
        EsbEmployeeDTO employee = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        //PojoUtils.map(employee,formVO);
        // 员工信息
        formVO.setBizArea(employee.getYxArea());
        formVO.setBizProvince(employee.getYxProvince());
        formVO.setEmpName(employee.getEmpName());
        formVO.setEmpId(employee.getEmpId());
        formVO.setDeptId(employee.getDeptId());
        formVO.setDeptName(employee.getDeptName());
        formVO.setBizDept(employee.getYxDept());
        //获取申请人对应的事业部信息
        EsbOrganizationDTO esbOrganizationDTO = businessDepartmentApi.getByOrgId(employee.getDeptId());
        if (esbOrganizationDTO != null) {
            formVO.setBdDeptId(esbOrganizationDTO.getOrgId());
            formVO.setBdDeptName(esbOrganizationDTO.getOrgName());
        }
        // 获取省份信息
        String province = crmEnterpriseRelationShipApi.getProvinceAreaByThreeParms(employee.getYxDept(), employee.getYxProvince());
        formVO.setProvince(province);
    }

    private void setApproveInfo(Integer formType,Long formId,FormVO formVO){
        if (ObjectUtil.equal(formType,FormTypeEnum.GOODS_FLEEING.getCode())){
            FleeingGoodsFormExtDTO fleeingGoods = fleeingGoodsFormApi.queryExtByFormId(formId);
            if (ObjectUtil.isNotNull(fleeingGoods)){
                formVO.setAppealType(fleeingGoods.getReportType());
                formVO.setAppendix(fleeingGoods.getAppendix());
                formVO.setDescribe(fleeingGoods.getFleeingDescribe());
                formVO.setConfirmDescribe(fleeingGoods.getConfirmDescribe());
            }
        }
        if (ObjectUtil.equal(formType,FormTypeEnum.SALES_APPEAL.getCode())){
            SalesAppealExtFormDTO salesAppealExt = saleaAppealFormApi.queryAppendix(formId);
            if (ObjectUtil.isNotNull(salesAppealExt)){
                formVO.setAppealType(salesAppealExt.getAppealType());
                formVO.setAppealAmount(salesAppealExt.getAppealAmount());
                formVO.setAppendix(salesAppealExt.getAppendix());
                formVO.setDescribe(salesAppealExt.getAppealDescribe());
                formVO.setConfirmDescribe(salesAppealExt.getConfirmRemark());
                formVO.setMonthAppealType(salesAppealExt.getMonthAppealType());
            }
        }
        if (ObjectUtil.equal(formType,FormTypeEnum.HOSPITAL_PHARMACY_BIND.getCode())){
            HospitalDrugstoreRelationExtFormDTO hospitalDrugstoreRelationExt = hospitalDrugstoreRelationFormApi.queryAppendix(formId);
            if (ObjectUtil.isNotNull(hospitalDrugstoreRelationExt)){
                formVO.setAppendix(hospitalDrugstoreRelationExt.getAppendix());
            }
        }
        if (StrUtil.isNotBlank(formVO.getAppendix())){
            List<AppendixDetailVO> appendixList = JSON.parseArray(formVO.getAppendix(), AppendixDetailVO.class);

            appendixList.forEach(e->{

                FileTypeEnum fileTypeEnum=null;
                if (ObjectUtil.equal(FormTypeEnum.GOODS_FLEEING.getCode(),formType)){
                    fileTypeEnum=FileTypeEnum.FLEEING_GOODS_UPLOAD_FILE;
                }
                if (ObjectUtil.equal(FormTypeEnum.SALES_APPEAL.getCode(),formType)){
                    fileTypeEnum=FileTypeEnum.SALES_APPEAL_UPLOAD_FILE;
                }
                if (ObjectUtil.equal(FormTypeEnum.HOSPITAL_PHARMACY_BIND.getCode(),formType)){
                    fileTypeEnum=FileTypeEnum.HOSPITAL_DRUGSTORE_REL_APPENDIX_UPLOAD_FILE;
                }
                String url = fileService.getUrl(e.getKey(), fileTypeEnum);
                e.setUrl(url);
            });
            formVO.setAppendixList(appendixList);
        }
    }
}