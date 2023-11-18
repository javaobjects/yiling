package com.yiling.sjms.agency.controller;


import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyCountRequest;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.api.AgencyFormApi;
import com.yiling.sjms.agency.dto.AgencyFormDTO;
import com.yiling.sjms.agency.dto.request.QueryAgencyFormPageRequest;
import com.yiling.sjms.agency.dto.request.QueryFirstAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.RemoveAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.SubmitAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.UpdateAgencyFormArchiveRequest;
import com.yiling.sjms.agency.enums.AgencyFormChangeItemEnum;
import com.yiling.sjms.agency.form.CheckAgencyFormDataForm;
import com.yiling.sjms.agency.form.QueryAgencyFormPageForm;
import com.yiling.sjms.agency.form.SaveAgencyFormForm;
import com.yiling.sjms.agency.form.SubmitAgencyFormForm;
import com.yiling.sjms.agency.form.UpdateArchiveStatusForm;
import com.yiling.sjms.agency.vo.AgencyFormVO;
import com.yiling.sjms.agency.vo.CheckResultVO;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 机构新增修改表单 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-22
 */
@Api(tags = "机构新增修改表单")
@RestController
@RequestMapping("/agency/form")
public class AgencyFormController extends BaseController {

    @DubboReference
    AgencyFormApi agencyFormApi;

    @DubboReference
    LocationApi locationApi;

    @DubboReference
    CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    FormApi formApi;

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @ApiOperation("新增/编辑--- 机构新增表单")
    @PostMapping("/saveAddForm")
    public Result<Long> saveAddForm(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveAgencyFormForm form) {
        if (Objects.nonNull(form.getProvinceCode())) {
            String[] namesByCodes = locationApi.getNamesByCodes(form.getProvinceCode(), form.getCityCode(), form.getRegionCode());
            form.setProvinceName(namesByCodes[0]);
            form.setCityName(namesByCodes[1]);
            form.setRegionName(namesByCodes[2]);
        }
        if (Objects.nonNull(form.getFormId()) && 0 != form.getFormId()) {
            QueryAgencyFormPageRequest agencyFormPageRequest = new QueryAgencyFormPageRequest();
            agencyFormPageRequest.setFormId(form.getFormId());
            Page<AgencyFormDTO> dtoPage = agencyFormApi.pageList(agencyFormPageRequest);
            if (dtoPage.getRecords().size() > 1 || (dtoPage.getRecords().size() == 1 && (Objects.isNull(form.getId()) || 0 == form.getId()))) {
                AgencyFormDTO agencyFormDTO = dtoPage.getRecords().get(0);
                Integer supplyChainRole = agencyFormDTO.getSupplyChainRole();
                if (!supplyChainRole.equals(form.getSupplyChainRole())) {
                    return Result.failed("表单中机构的供应链角色必须一致！");
                }
            }
        }

        SaveAgencyFormRequest request = PojoUtils.map(form, SaveAgencyFormRequest.class);
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        request.setEmpName(esbEmployeeDTO.getEmpName());
        request.setFormTypeEnum(FormTypeEnum.ENTERPRISE_ADD);
        request.setOpUserId(userInfo.getCurrentUserId());
        Long formId = agencyFormApi.saveAgencyForm(request);
        return Result.success(formId);
    }

    @ApiOperation("新增/编辑--- 机构修改表单")
    @PostMapping("/saveEditForm")
    public Result<Long> saveEditForm(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveAgencyFormForm form) {
        if (Objects.isNull(form.getCrmEnterpriseId()) || 0 == form.getCrmEnterpriseId()) {
            return Result.failed("提交数据不完整");
        }
        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(form.getCrmEnterpriseId());
        List<Integer> changeItemList = form.getChangeItemList();
        if (CollUtil.isEmpty(changeItemList)) {
            return Result.failed("变更项目不能为空");
        }
        // 未修改时数据的一致性
        if (!changeItemList.contains(AgencyFormChangeItemEnum.SUPPLY_CHAIN_ROLE.getCode())) {
            form.setSupplyChainRole(crmEnterpriseDTO.getSupplyChainRole());
        }
        if (!changeItemList.contains(AgencyFormChangeItemEnum.NAME.getCode())) {
            form.setName(crmEnterpriseDTO.getName());
        }
        if (!changeItemList.contains(AgencyFormChangeItemEnum.LICENSE_NUMBER.getCode())) {
            form.setLicenseNumber(crmEnterpriseDTO.getLicenseNumber());
        }
        if (!changeItemList.contains(AgencyFormChangeItemEnum.PHONE.getCode())) {
            form.setPhone(crmEnterpriseDTO.getPhone());
        }
        if (!changeItemList.contains(AgencyFormChangeItemEnum.AREA.getCode())) {
            form.setProvinceCode(crmEnterpriseDTO.getProvinceCode());
            form.setCityCode(crmEnterpriseDTO.getCityCode());
            form.setRegionCode(crmEnterpriseDTO.getRegionCode());
        }
        if (Objects.nonNull(form.getProvinceCode())) {
            String[] namesByCodes = locationApi.getNamesByCodes(form.getProvinceCode(), form.getCityCode(), form.getRegionCode());
            form.setProvinceName(namesByCodes[0]);
            form.setCityName(namesByCodes[1]);
            form.setRegionName(namesByCodes[2]);
        }
        if (!changeItemList.contains(AgencyFormChangeItemEnum.ADDRESS.getCode())) {
            form.setAddress(crmEnterpriseDTO.getAddress());
        }

        if (Objects.nonNull(form.getFormId()) && 0 != form.getFormId()) {
            // 表单中机构的供应链角色必须一致
            QueryAgencyFormPageRequest agencyFormPageRequest = new QueryAgencyFormPageRequest();
            agencyFormPageRequest.setFormId(form.getFormId());
            Page<AgencyFormDTO> dtoPage = agencyFormApi.pageList(agencyFormPageRequest);
            if (dtoPage.getRecords().size() > 1 || (dtoPage.getRecords().size() == 1 && (Objects.isNull(form.getId()) || 0 == form.getId()))) {
                AgencyFormDTO agencyFormDTO = dtoPage.getRecords().get(0);
                Integer supplyChainRole = agencyFormDTO.getSupplyChainRole();
                if (!supplyChainRole.equals(form.getSupplyChainRole())) {
                    return Result.failed("表单中机构的供应链角色必须一致！");
                }
            }

            // 校验同一个机构不能在一个表单里面修改2次
            List<AgencyFormDTO> agencyFormDTOList = agencyFormApi.listByFormIdAndCrmEnterpriseId(form.getFormId(), form.getCrmEnterpriseId());
            if (CollUtil.isEmpty(agencyFormDTOList)) {
            } else if (agencyFormDTOList.size() == 1) {
                AgencyFormDTO agencyFormDTO = agencyFormDTOList.get(0);
                if (!form.getId().equals(agencyFormDTO.getId())) {
                    return Result.failed("表单中已经存在此机构的修改！");
                }
            } else {
                return Result.failed("表单中已经存在此机构的修改！");
            }
        }

        SaveAgencyFormRequest request = PojoUtils.map(form, SaveAgencyFormRequest.class);
        request.setBeforeName(crmEnterpriseDTO.getName());
        request.setBeforeLicenseNumber(crmEnterpriseDTO.getLicenseNumber());
        request.setChangeItem(form.getChangeItemList().toString());
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        request.setEmpName(esbEmployeeDTO.getEmpName());
        request.setFormTypeEnum(FormTypeEnum.ENTERPRISE_UPDATE);
        request.setOpUserId(userInfo.getCurrentUserId());
        Long formId = agencyFormApi.saveAgencyForm(request);
        return Result.success(formId);

    }

    @ApiOperation("分页查询")
    @PostMapping("/pageList")
    public Result<Page<AgencyFormVO>> pageList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryAgencyFormPageForm form) {
        QueryAgencyFormPageRequest request = PojoUtils.map(form, QueryAgencyFormPageRequest.class);
        if (Objects.isNull(request.getFormId()) || 0 == request.getFormId()) {
            FormTypeEnum formTypeEnum = FormTypeEnum.getByCode(form.getFormType());
            List<FormDTO> formDTOList = formApi.listUnsubmitFormsByUser(formTypeEnum, userInfo.getCurrentUserId());
            if (CollUtil.isNotEmpty(formDTOList)) {
                request.setFormId(formDTOList.get(0).getId());
            }
        }
        if (Objects.isNull(request.getFormId())) {
            return Result.success(form.getPage());
        }
        Page<AgencyFormDTO> dtoPage = agencyFormApi.pageList(request);
        Page<AgencyFormVO> voPage = PojoUtils.map(dtoPage, AgencyFormVO.class);
        for (AgencyFormVO agencyFormVO : voPage.getRecords()) {
            if (StringUtils.isNotBlank(agencyFormVO.getChangeItem())) {
                List<Integer> changeItemList = JSON.parseArray(agencyFormVO.getChangeItem(), Integer.class);
                agencyFormVO.setChangeItemList(changeItemList);
            }
            if (Objects.nonNull(agencyFormVO.getCrmEnterpriseId()) && 0 != agencyFormVO.getCrmEnterpriseId()) {
                agencyFormVO.setName(agencyFormVO.getBeforeName());
                agencyFormVO.setLicenseNumber(agencyFormVO.getBeforeLicenseNumber());
            }
        }
        return Result.success(voPage);
    }

    @ApiOperation("查看")
    @GetMapping("/query")
    public Result<AgencyFormVO> query(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        AgencyFormDTO agencyFormDTO = agencyFormApi.queryById(id);
        AgencyFormVO agencyFormVO = PojoUtils.map(agencyFormDTO, AgencyFormVO.class);
        if (null != agencyFormVO) {
            List<Integer> changeItemList = JSON.parseArray(agencyFormVO.getChangeItem(), Integer.class);
            agencyFormVO.setChangeItemList(changeItemList);
        }
        return Result.success(agencyFormVO);
    }

    @ApiOperation(("删除"))
    @GetMapping("/remove")
    public Result<Boolean> remove(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        RemoveAgencyFormRequest request = new RemoveAgencyFormRequest();
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setId(id);
        boolean isSuccess = agencyFormApi.removeById(request);
        if (isSuccess) {
            return Result.success(true);
        }
        return Result.failed("删除失败");
    }

    @ApiOperation("提交审核-新增机构表单")
    @PostMapping("/add/submit")
    public Result<Boolean> submitAdd(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SubmitAgencyFormForm form) {
        SubmitAgencyFormRequest request = PojoUtils.map(form, SubmitAgencyFormRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setFormTypeEnum(FormTypeEnum.ENTERPRISE_ADD);
        request.setEmpId(userInfo.getCurrentUserCode());
        agencyFormApi.submit(request);
        return Result.success(true);
    }

    @ApiOperation("提交审核-修改机构表单")
    @PostMapping("/update/submit")
    public Result<Boolean> submitUpdate(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SubmitAgencyFormForm form) {
        SubmitAgencyFormRequest request = PojoUtils.map(form, SubmitAgencyFormRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setFormTypeEnum(FormTypeEnum.ENTERPRISE_UPDATE);
        request.setEmpId(userInfo.getCurrentUserCode());
        agencyFormApi.submit(request);
        return Result.success(true);
    }

    @ApiOperation(value = "修改归档状态")
    @PostMapping(value = "/updateArchiveStatus")
    public Result<Boolean> updateArchiveStatus(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid UpdateArchiveStatusForm form) {
        UpdateAgencyFormArchiveRequest request = PojoUtils.map(form, UpdateAgencyFormArchiveRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        agencyFormApi.updateArchiveStatusById(request);
        return Result.success(true);
    }

    @ApiOperation(value = "校验机构数据信息")
    @PostMapping("/checkAgencyFormData")
    public Result<CheckResultVO> checkAgencyFormData(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody CheckAgencyFormDataForm form) {
        CheckResultVO checkResultVO = new CheckResultVO();
        checkResultVO.setIsSuccess(true);
        QueryCrmAgencyCountRequest request;
        QueryFirstAgencyFormRequest agencyFormRequest;
        if (Objects.nonNull(form.getSupplyChainRole()) && 0 != form.getSupplyChainRole() && Objects.nonNull(form.getFormId()) && 0 != form.getFormId()) {
            // 统一社会信用代码检查
            agencyFormRequest = new QueryFirstAgencyFormRequest();
            if (Objects.nonNull(form.getId())) {
                agencyFormRequest.setNotId(form.getId());
            }
            agencyFormRequest.setFormId(form.getFormId());
            AgencyFormDTO agencyFormDTO = agencyFormApi.getFirstInfo(agencyFormRequest);
            if (Objects.nonNull(agencyFormDTO)) {
                Integer supplyChainRole = agencyFormDTO.getSupplyChainRole();
                if (!supplyChainRole.equals(form.getSupplyChainRole())) {
                    checkResultVO.setIsSuccess(false);
                    checkResultVO.setErrorMessage("表单中机构的供应链角色必须一致");
                }
            }
        }
        if (StringUtils.isNotBlank(form.getLicenseNumber()) && !"0".equals(form.getLicenseNumber())) {
            // 统一社会信用代码检查
            request = new QueryCrmAgencyCountRequest();
            if (Objects.nonNull(form.getCrmEnterpriseId())) {
                request.setNotId(form.getCrmEnterpriseId());
            }
            request.setLicenseNumber(form.getLicenseNumber());
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getFirstCrmEnterpriseInfo(request);
            if (Objects.nonNull(crmEnterpriseDTO)) {
                checkResultVO.setIsSuccess(false);
                checkResultVO.setErrorMessage("您输入的统一社会信用代码" + getRoleString(crmEnterpriseDTO.getSupplyChainRole()) + "已存在");
                return Result.success(checkResultVO);
            }
            if (Objects.nonNull(form.getFormId()) && 0 != form.getFormId()) {
                agencyFormRequest = new QueryFirstAgencyFormRequest();
                if (Objects.nonNull(form.getId())) {
                    agencyFormRequest.setNotId(form.getId());
                }
                agencyFormRequest.setFormId(form.getFormId());
                agencyFormRequest.setLicenseNumber(form.getLicenseNumber());
                AgencyFormDTO agencyFormDTO = agencyFormApi.getFirstInfo(agencyFormRequest);
                if (Objects.nonNull(agencyFormDTO)) {
                    checkResultVO.setIsSuccess(false);
                    checkResultVO.setErrorMessage("列表中已存在当前机构");
                }
            }
        }
        if (StringUtils.isNotBlank(form.getName())) {
            // 机构名称重复检查
            request = new QueryCrmAgencyCountRequest();
            if (Objects.nonNull(form.getCrmEnterpriseId())) {
                request.setNotId(form.getCrmEnterpriseId());
            }
            request.setName(form.getName());
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getFirstCrmEnterpriseInfo(request);
            if (Objects.nonNull(crmEnterpriseDTO)) {
                checkResultVO.setIsSuccess(false);
                checkResultVO.setErrorMessage("您输入的机构名称" + getRoleString(crmEnterpriseDTO.getSupplyChainRole()) + "已存在");
                return Result.success(checkResultVO);
            }
            if (Objects.nonNull(form.getFormId()) && 0 != form.getFormId()) {
                agencyFormRequest = new QueryFirstAgencyFormRequest();
                if (Objects.nonNull(form.getId())) {
                    agencyFormRequest.setNotId(form.getId());
                }
                agencyFormRequest.setFormId(form.getFormId());
                agencyFormRequest.setName(form.getName());
                AgencyFormDTO agencyFormDTO = agencyFormApi.getFirstInfo(agencyFormRequest);
                if (Objects.nonNull(agencyFormDTO)) {
                    checkResultVO.setIsSuccess(false);
                    checkResultVO.setErrorMessage("列表中已存在当前机构");
                }
            }
        }
        return Result.success(checkResultVO);
    }

    private String getRoleString(Integer supplyChainRole) {
        // erp供应链角色：1-经销商 2-终端医院 3-终端药店。数据字典：erp_crm_supply_chain_role
        String supplyName = "";
        if (1 == supplyChainRole) {
            supplyName = "在商业公司";
        } else if (2 == supplyChainRole) {
            supplyName = "在医疗机构";
        } else if (3 == supplyChainRole) {
            supplyName = "在零售机构";
        }
        return supplyName;
    }
}
