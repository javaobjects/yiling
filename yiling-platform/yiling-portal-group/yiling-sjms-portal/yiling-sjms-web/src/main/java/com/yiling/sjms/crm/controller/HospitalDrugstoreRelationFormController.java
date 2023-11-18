package com.yiling.sjms.crm.controller;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
import com.yiling.basic.no.api.NoApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.api.CrmHospitalDrugstoreRelationApi;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.dto.CrmHospitalDrugstoreRelationDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmHosDruRelEffectiveListRequest;
import com.yiling.dataflow.crm.dto.request.QueryHospitalDrugstoreRelationPageRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.sjms.agency.dto.request.QueryHosDruRelPageListRequest;
import com.yiling.sjms.crm.api.HospitalDrugstoreRelationFormApi;
import com.yiling.sjms.crm.dto.HospitalDrugstoreRelationExtFormDTO;
import com.yiling.sjms.crm.dto.HospitalDrugstoreRelationFormDTO;
import com.yiling.sjms.crm.dto.request.DeleteHosDruRelFormAppendixRequest;
import com.yiling.sjms.crm.dto.request.DeleteHospitalDrugstoreRelFormRequest;
import com.yiling.sjms.crm.dto.request.SaveOrUpdateHosDruRelAppendixListRequest;
import com.yiling.sjms.crm.dto.request.SaveOrUpdateHospitalDrugstoreRelFormRequest;
import com.yiling.sjms.crm.dto.request.SubmitHosDruRelRequest;
import com.yiling.sjms.crm.form.QueryHosDruRelPageListForm;
import com.yiling.sjms.crm.form.SaveOrUpdateHosDruRelAppendixListForm;
import com.yiling.sjms.crm.form.SaveOrUpdateHospitalDrugstoreRelForm;
import com.yiling.sjms.crm.form.SaveOrUpdateHospitalDrugstoreRelParentForm;
import com.yiling.sjms.crm.form.SubmitHosDruRelForm;
import com.yiling.sjms.crm.vo.HospitalDrugstoreRelationFormVO;
import com.yiling.sjms.flee.dto.FleeingGoodsFormExtDTO;
import com.yiling.sjms.flee.dto.SalesAppealExtFormDTO;
import com.yiling.sjms.flee.vo.AppendixDetailVO;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.form.dto.request.CreateFormRequest;
import com.yiling.sjms.form.dto.request.UpdateRemarkRequest;
import com.yiling.sjms.form.enums.FormNoEnum;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.form.vo.FormVO;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.sjms.util.OaTodoUtils;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author fucheng.bai
 * @date 2023/6/7
 */
@Api(tags = "院外药店关系绑定表单")
@RestController
@RequestMapping("/hospitalDrugstoreRelation/form")
public class HospitalDrugstoreRelationFormController extends BaseController {

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    private NoApi noApi;

    @DubboReference
    private FormApi formApi;

    @DubboReference
    private CrmGoodsInfoApi crmGoodsInfoApi;

    @DubboReference
    private HospitalDrugstoreRelationFormApi hospitalDrugstoreRelationFormApi;

    @DubboReference
    private CrmHospitalDrugstoreRelationApi crmHospitalDrugstoreRelationApi;

    @DubboReference
    private BusinessDepartmentApi businessDepartmentApi;

    @DubboReference
    private CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;

    @DubboReference
    private CrmGoodsCategoryApi crmGoodsCategoryApi;

    @Autowired
    FileService fileService;


    @ApiOperation(value = "新增或编辑")
    @PostMapping("saveParentForm")
    public Result<Long> saveParentForm(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveOrUpdateHospitalDrugstoreRelParentForm form){
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());

        CreateFormRequest request = new CreateFormRequest();
        String code = noApi.gen(FormNoEnum.getByCode(form.getFormType()));
        String title = OaTodoUtils.genTitle(FormTypeEnum.getByCode(form.getFormType()).getName(), esbEmployeeDTO.getEmpName(), code, new Date());
        request.setType(form.getFormType()).setName(title).setCode(code);
        request.setRemark(form.getRemark()).setOpUserId(userInfo.getCurrentUserId());
        Long id = null;

        if(Objects.isNull(form.getFormId())){
            id = formApi.create(request);
        }else{
            id = form.getFormId();
            UpdateRemarkRequest remarkRequest = new UpdateRemarkRequest();
            remarkRequest.setId(form.getFormId()).setRemark(form.getRemark()).setOpUserId(userInfo.getCurrentUserId());
            formApi.updateRemark(remarkRequest);
        }
        return Result.success(id);
    }

    @ApiOperation(value = "查询父表单信息或待提交状态")
    @GetMapping("queryParentForm")
    public Result<FormVO> getParentForm(@CurrentUser CurrentSjmsUserInfo userInfo, @ApiParam(value = "表单类型") @RequestParam(value = "formType") Integer formType, @ApiParam(value = "主表单id") @RequestParam(required = false) Long formId){
        FormVO formVO = new FormVO();
        // 存在formId
        if (Objects.nonNull(formId)) {
            FormDTO formDTO = formApi.getById(formId);
            PojoUtils.map(formDTO, formVO);
        } else {
            //获取当前人的待提交的数据
            List<FormDTO> formList = formApi.listUnsubmitFormsByUser(FormTypeEnum.getByCode(formType), userInfo.getCurrentUserId());
            if (CollUtil.isNotEmpty(formList)) {
                PojoUtils.map(formList.get(0), formVO);
            }
        }
        //省区为空的时候重新设置
        if (StringUtils.isEmpty(formVO.getProvince())) {
            setEsbEmpProvince(formVO, userInfo);
        }

        if (formVO.getId() == null || formVO.getId() == 0) {
            return Result.success(formVO);
        }
        setAppendix(formType, formVO);
        return Result.success(formVO);
    }

    @ApiOperation(value = "新增或更新表单附件")
    @PostMapping("saveParentFormAppendix")
    public Result<Long> saveParentFormAppendix(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveOrUpdateHosDruRelAppendixListForm form) {

        SaveOrUpdateHosDruRelAppendixListRequest request = PojoUtils.map(form, SaveOrUpdateHosDruRelAppendixListRequest.class);

        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        request.setEmpName(esbEmployeeDTO.getEmpName());
        request.setFormTypeEnum(FormTypeEnum.HOSPITAL_PHARMACY_BIND);
        request.setOpUserId(userInfo.getCurrentUserId());
        Long formId = hospitalDrugstoreRelationFormApi.saveParentAppendixForm(request);
        return Result.success(formId);
    }



    @ApiOperation("新增/编辑--- 院外药店绑定表单")
    @PostMapping("/saveAddForm")
    public Result<Long> saveAddForm(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveOrUpdateHospitalDrugstoreRelForm form) {
        SaveOrUpdateHospitalDrugstoreRelFormRequest request = PojoUtils.map(form, SaveOrUpdateHospitalDrugstoreRelFormRequest.class);
        if (request.getCrmGoodsCode() != null) {
            CrmGoodsInfoDTO crmGoodsInfoDTO = crmGoodsInfoApi.findByCodeAndName(request.getCrmGoodsCode(), null);
            if (crmGoodsInfoDTO == null) {
                return Result.failed("选择的产品不存在或是无效产品！");
            }
            request.setCrmGoodsName(crmGoodsInfoDTO.getGoodsName());
            request.setCrmGoodsSpec(crmGoodsInfoDTO.getGoodsSpec());
        }

        if (form.getCategoryId() == null || form.getCategoryId() == 0) {
            CrmGoodsCategoryDTO crmGoodsCategoryDTO = crmGoodsCategoryApi.findByCodeOrName(null, form.getCategoryName());
            if (crmGoodsCategoryDTO == null) {
                return Result.failed("品种名称不存在！");
            }
            request.setCategoryId(crmGoodsCategoryDTO.getId());
        }

        // 验证生效结束时间不可小于当前时间
        Date now = DateUtil.beginOfDay(DateUtil.date());
        if (request.getEffectStartTime().after(request.getEffectEndTime())) {
            return Result.failed("生效开始时间不可大于生效结束时间！");
        }
        if (now.after(request.getEffectEndTime())) {
            return Result.failed("生效结束时间不可小于当前时间！");
        }

        /* 需要合并校验绑定
            1. 一个医院绑定三个药店
            2. 一个药店的一个品种只能绑定一家医院
            3. 一个药店的不同品种可以绑定不同医院
            4. 主库有效数据校验，只获取未生效和生效中，未停用的数据进行校验
        */

        // 合并验证院外药店绑定关系表+院外药店绑定表单表 有效数据中 医院绑定药店数量不可大于3个
        verifyDrugstoreCount(request);

        // 合并验证院外药店绑定关系表+院外药店绑定表单表 药店+品种+医院 有效数据中 唯一
        verifyUnique(request);

        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        request.setEmpName(esbEmployeeDTO.getEmpName());
        request.setFormTypeEnum(FormTypeEnum.HOSPITAL_PHARMACY_BIND);
        request.setOpUserId(userInfo.getCurrentUserId());
        Long formId = hospitalDrugstoreRelationFormApi.saveHospitalDrugstoreRelationForm(request);
        return Result.success(formId);
    }

    @ApiOperation(("删除院外药店绑定表单"))
    @GetMapping("/remove")
    public Result remove(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        DeleteHospitalDrugstoreRelFormRequest request = new DeleteHospitalDrugstoreRelFormRequest();
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setId(id);
        hospitalDrugstoreRelationFormApi.removeById(request);
        return Result.success();
    }

//    @ApiOperation(("删除院外药店绑定表单关联附件"))
//    @GetMapping("/removeParentFormAppendix")
//    public Result removeParentFormAppendix(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
//        DeleteHosDruRelFormAppendixRequest request = new DeleteHosDruRelFormAppendixRequest();
//        request.setOpUserId(userInfo.getCurrentUserId());
//        request.setId(id);
//        hospitalDrugstoreRelationFormApi.removeAppendixById(request);
//        return Result.success();
//    }

    @ApiOperation("分页查询")
    @PostMapping("/pageList")
    public Result<Page<HospitalDrugstoreRelationFormVO>> pageList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryHosDruRelPageListForm form) {
        QueryHosDruRelPageListRequest request = PojoUtils.map(form, QueryHosDruRelPageListRequest.class);
        if (Objects.isNull(request.getFormId()) || 0 == request.getFormId()) {
            List<FormDTO> formDTOList = formApi.listUnsubmitFormsByUser(FormTypeEnum.HOSPITAL_PHARMACY_BIND, userInfo.getCurrentUserId());
            if (CollUtil.isNotEmpty(formDTOList)) {
                request.setFormId(formDTOList.get(0).getId());
            }
        }
        if (Objects.isNull(request.getFormId())) {
            return Result.success(new Page<>(form.getCurrent(), form.getSize()));
        }

        Page<HospitalDrugstoreRelationFormVO> pageResult = PojoUtils.map(hospitalDrugstoreRelationFormApi.pageList(request), HospitalDrugstoreRelationFormVO.class);
        return Result.success(pageResult);
    }

    @ApiOperation("查看")
    @GetMapping("/query")
    public Result<HospitalDrugstoreRelationFormVO> query(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        return Result.success(PojoUtils.map(hospitalDrugstoreRelationFormApi.detail(id), HospitalDrugstoreRelationFormVO.class));
    }


    @ApiOperation("提交审核")
    @PostMapping("/submit")
    public Result submit(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SubmitHosDruRelForm form) {
        if (userInfo == null) {
            return Result.failed(ResultCode.FAILED);
        }

        SubmitHosDruRelRequest request = PojoUtils.map(form, SubmitHosDruRelRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setFormTypeEnum(FormTypeEnum.HOSPITAL_PHARMACY_BIND);
        request.setEmpId(userInfo.getCurrentUserCode());

        hospitalDrugstoreRelationFormApi.submit(request);
        return Result.success();
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

    private void setAppendix(Integer formType, FormVO formVO){
        if (ObjectUtil.equal(formType,FormTypeEnum.HOSPITAL_PHARMACY_BIND.getCode())){
            HospitalDrugstoreRelationExtFormDTO hospitalDrugstoreRelationExt = hospitalDrugstoreRelationFormApi.queryAppendix(formVO.getId());
            if (ObjectUtil.isNotNull(hospitalDrugstoreRelationExt)){
                formVO.setAppendix(hospitalDrugstoreRelationExt.getAppendix());
            }
        }
        if (StrUtil.isNotBlank(formVO.getAppendix())){
            List<AppendixDetailVO> appendixList = JSON.parseArray(formVO.getAppendix(), AppendixDetailVO.class);
            appendixList.forEach(a->{

                FileTypeEnum fileTypeEnum=null;
                if (ObjectUtil.equal(FormTypeEnum.HOSPITAL_PHARMACY_BIND.getCode(),formType)){
                    fileTypeEnum=FileTypeEnum.HOSPITAL_DRUGSTORE_REL_APPENDIX_UPLOAD_FILE;
                }
                String url = fileService.getUrl(a.getKey(), fileTypeEnum);
                a.setUrl(url);
            });
            formVO.setAppendixList(appendixList);
        }
    }

    private void verifyDrugstoreCount(SaveOrUpdateHospitalDrugstoreRelFormRequest request) {

        List<Long> drugOrgIdList = crmHospitalDrugstoreRelationApi.selectDrugOrgIdByHosOrgId(request.getHospitalOrgId());
        if (CollUtil.isNotEmpty(drugOrgIdList) && drugOrgIdList.size() >= 3) {
            if (!drugOrgIdList.contains(request.getDrugstoreOrgId())) {     // 是否为已绑定的药店，如果是已绑定过的，则可以继续绑定
                throw new BusinessException(ResultCode.FAILED, "该医院已超过绑定上限！");
            }
        }

        if (request.getFormId() != null && request.getFormId() != 0) {      // 若当前表单不为空，则需要主库数据和表单数据合并校验
            List<Long> formDrugOrgIdList = hospitalDrugstoreRelationFormApi.selectDrugOrgIdFormIdByHosOrgId(request.getFormId(), request.getHospitalOrgId());
            if (CollUtil.isNotEmpty(formDrugOrgIdList)) {
                drugOrgIdList.addAll(formDrugOrgIdList);
                List<Long> list = drugOrgIdList.stream().distinct().collect(Collectors.toList());   // 去重
                if (CollUtil.isNotEmpty(list) && list.size() >= 3) {
                    if (!list.contains(request.getDrugstoreOrgId())) {
                        throw new BusinessException(ResultCode.FAILED, "该医院已超过绑定上限！");
                    }
                }
            }
        }
    }

    private void verifyUnique(SaveOrUpdateHospitalDrugstoreRelFormRequest request) {
        // 主库校验
        QueryCrmHosDruRelEffectiveListRequest queryRequest = new QueryCrmHosDruRelEffectiveListRequest();
        queryRequest.setDrugstoreOrgId(request.getDrugstoreOrgId());
        queryRequest.setCrmGoodsCode(request.getCrmGoodsCode());
        List<CrmHospitalDrugstoreRelationDTO> list = crmHospitalDrugstoreRelationApi.getEffectiveList(queryRequest);
        if (request.getId() != null && request.getId() != 0) {
            // 若为更新，则去掉该条
            list = list.stream().filter(e -> !e.getId().equals(request.getId())).collect(Collectors.toList());
        }
        if (CollUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(chdr -> chdr.getHospitalOrgId().equals(request.getHospitalOrgId()))) {
                throw new BusinessException(ResultCode.FAILED, "有效数据内，医院已绑定该药店的药品，请勿重复绑定！");
            } else  {
                throw new BusinessException(ResultCode.FAILED, "一家药店的同一个药品，只能跟一个医院进行绑定!");
            }
        }

        // 表单校验
        QueryHosDruRelPageListRequest formRequest = new QueryHosDruRelPageListRequest();
        formRequest.setFormId(request.getFormId());
        formRequest.setDrugstoreOrgId(request.getDrugstoreOrgId());
        formRequest.setCrmGoodsCode(request.getCrmGoodsCode());
        Page<HospitalDrugstoreRelationFormDTO> pageResult = hospitalDrugstoreRelationFormApi.pageList(formRequest);
        List<HospitalDrugstoreRelationFormDTO> hdrList = pageResult.getRecords();
        if (request.getId() != null && request.getId() != 0) {      // 更新
            hdrList = pageResult.getRecords().stream().filter(hdr -> !hdr.getId().equals(request.getId())).collect(Collectors.toList());
        }
        if (CollUtil.isNotEmpty(hdrList)) {
            if (hdrList.stream().anyMatch(chdr -> chdr.getHospitalOrgId().equals(request.getHospitalOrgId()))) {
                throw new BusinessException(ResultCode.FAILED, "有效数据内，医院已绑定该药店的药品，请勿重复绑定！");
            } else  {
                throw new BusinessException(ResultCode.FAILED, "一家药店的同一个药品，只能跟一个医院进行绑定!");
            }
        }
    }

}
