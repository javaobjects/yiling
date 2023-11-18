package com.yiling.sjms.flee.controller;


import java.util.List;
import java.util.Objects;

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
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.flee.api.FleeingGoodsFormApi;
import com.yiling.sjms.flee.bo.FleeingFormBO;
import com.yiling.sjms.flee.dto.FleeingGoodsFormExtDTO;
import com.yiling.sjms.flee.dto.request.QueryFleeingFormPageRequest;
import com.yiling.sjms.flee.enums.FleeingImportFileTypeEnum;
import com.yiling.sjms.flee.form.QueryFleeingFormPageForm;
import com.yiling.sjms.flee.vo.AppendixDetailVO;
import com.yiling.sjms.flee.vo.FleeingFormDetailVO;
import com.yiling.sjms.flee.vo.FleeingFormVO;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.form.vo.FormVO;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 窜货申报表单 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2023-03-10
 */
@Api(tags = "窜货申报表单")
@RestController
@RequestMapping("/flee/form")
public class FleeingFormController extends BaseController {

    @DubboReference
    UserApi userApi;

    @DubboReference
    FormApi formApi;

    @DubboReference
    FleeingGoodsFormApi fleeingGoodsFormApi;

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    private CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;

    @DubboReference
    private BusinessDepartmentApi businessDepartmentApi;

    @ApiOperation(value = "流程列表")
    @PostMapping("/pageForm")
    public Result<Page<FleeingFormVO>> pageForm(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryFleeingFormPageForm form) {
        QueryFleeingFormPageRequest request = new QueryFleeingFormPageRequest();
        PojoUtils.map(form, request);
        request.setImportFileType(FleeingImportFileTypeEnum.FLEEING.getCode());
        request.setEmpId(userInfo.getCurrentUserCode());
        Page<FleeingFormBO> boPage = fleeingGoodsFormApi.pageForm(request);
        return Result.success(PojoUtils.map(boPage, FleeingFormVO.class));
    }

    @ApiOperation(value = "流程列表-窜货申报确认列表页面")
    @PostMapping("/pageAllForm")
    public Result<Page<FleeingFormVO>> pageAllForm(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryFleeingFormPageForm form) {
        QueryFleeingFormPageRequest request = new QueryFleeingFormPageRequest();
        PojoUtils.map(form, request);
        request.setImportFileType(FleeingImportFileTypeEnum.FLEEING_CONFIRM.getCode());
        Page<FleeingFormBO> boPage = fleeingGoodsFormApi.pageForm(request);
        Page<FleeingFormVO> voPage = PojoUtils.map(boPage, FleeingFormVO.class);
        for (FleeingFormVO item : voPage.getRecords()) {
            EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(item.getConfirmUserId());
            if (ObjectUtil.isNotNull(esbEmployeeDTO)) {
                item.setConfirmUserName(esbEmployeeDTO.getEmpName());
            }
        }
        return Result.success(voPage);
    }


    @ApiOperation("删除")
    @GetMapping("/remove")
    public Result<Boolean> remove(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        Boolean isSuccess = formApi.delete(id, userInfo.getCurrentUserId());
        if (isSuccess) {
            return Result.success(true);
        }
        return Result.failed("删除失败");
    }

    @ApiOperation("详情")
    @GetMapping("/query")
    public Result<FleeingFormDetailVO> query(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id, @RequestParam(value = "importFileType") Integer importFileType) {
        FleeingFormDetailVO fleeingFormDetailVO = new FleeingFormDetailVO();
        if (Objects.isNull(id) || 0L == id) {
            setEsbEmpProvince(fleeingFormDetailVO, userInfo);
            return Result.success(fleeingFormDetailVO);
        }

        FormDTO formDTO = formApi.getById(id);
        PojoUtils.map(formDTO, fleeingFormDetailVO);
        //省区为空的时候重新设置
        if (StringUtils.isEmpty(fleeingFormDetailVO.getProvince())) {
            setEsbEmpProvince(fleeingFormDetailVO, userInfo);
        }

        FleeingGoodsFormExtDTO fleeingGoodsFormExtDTO = fleeingGoodsFormApi.queryExtByFormId(id);
        if (Objects.nonNull(fleeingGoodsFormExtDTO)) {
            fleeingFormDetailVO.setFormId(fleeingGoodsFormExtDTO.getFormId());
            fleeingFormDetailVO.setFleeingDescribe(fleeingGoodsFormExtDTO.getFleeingDescribe());
            fleeingFormDetailVO.setAppendix(fleeingGoodsFormExtDTO.getAppendix());

            //            List<String> appendixList = JSON.parseArray(fleeingGoodsFormExtDTO.getAppendix(), String.class);
            //            fleeingFormDetailVO.setAppendixList(appendixList);

            if (StringUtils.isNotEmpty(fleeingGoodsFormExtDTO.getAppendix())) {
                List<AppendixDetailVO> appendixDetailList = JSON.parseArray(fleeingGoodsFormExtDTO.getAppendix(), AppendixDetailVO.class);
                fleeingFormDetailVO.setAppendixList(appendixDetailList);
            }

            fleeingFormDetailVO.setFleeingType(fleeingGoodsFormExtDTO.getReportType());
            fleeingFormDetailVO.setConfirmStatus(fleeingGoodsFormExtDTO.getConfirmStatus());
            fleeingFormDetailVO.setConfirmDescribe(fleeingGoodsFormExtDTO.getConfirmDescribe());
        }

        return Result.success(fleeingFormDetailVO);
    }

    @ApiOperation(value = "根据formId查看表头部分窜货申诉确认")
    @GetMapping("/querySubmitInfo")
    public Result<FleeingFormDetailVO> querySubmitInfo(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {

        FleeingFormDetailVO fleeingFormDetailVO = new FleeingFormDetailVO();
        if (Objects.nonNull(id) && 0L != id) {
            FleeingGoodsFormExtDTO fleeingGoodsFormExtDTO = fleeingGoodsFormApi.queryExtByFormId(id);
            if (Objects.nonNull(fleeingGoodsFormExtDTO)) {
                fleeingFormDetailVO.setConfirmDescribe(fleeingGoodsFormExtDTO.getConfirmDescribe());
                fleeingFormDetailVO.setConfirmStatus(fleeingGoodsFormExtDTO.getConfirmStatus());
                if (1 == fleeingGoodsFormExtDTO.getConfirmStatus()) {
                    EsbEmployeeDTO employee = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
                    fleeingFormDetailVO.setEmpName(employee.getEmpName());
                    fleeingFormDetailVO.setBizDept(employee.getYxDept());
                } else if (2 == fleeingGoodsFormExtDTO.getConfirmStatus()) {
                    EsbEmployeeDTO employee = esbEmployeeApi.getByEmpId(fleeingGoodsFormExtDTO.getConfirmUserId());
                    if (ObjectUtil.isNotEmpty(employee)) {
                        fleeingFormDetailVO.setEmpName(employee.getEmpName());
                        fleeingFormDetailVO.setBizDept(employee.getYxDept());
                    }
                }
            }
        }
        return Result.success(fleeingFormDetailVO);
    }

    private void setEsbEmpProvince(FormVO formVO, CurrentSjmsUserInfo userInfo) {
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
}
