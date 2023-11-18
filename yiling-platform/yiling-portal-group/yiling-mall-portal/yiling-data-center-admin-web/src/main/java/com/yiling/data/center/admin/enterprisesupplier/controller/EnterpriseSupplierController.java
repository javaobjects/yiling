package com.yiling.data.center.admin.enterprisesupplier.controller;


import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.data.center.admin.enterprisecustomer.vo.EnterpriseCustomerListItemVO;
import com.yiling.data.center.admin.enterprisesupplier.form.QueryEnterpriseListPageForm;
import com.yiling.data.center.admin.enterprisesupplier.form.QuerySupplierPageForm;
import com.yiling.data.center.admin.enterprisesupplier.form.UpdateSupplierForm;
import com.yiling.data.center.admin.enterprisesupplier.vo.EnterpriseSupplierVO;
import com.yiling.data.center.admin.enterprisesupplier.vo.EnterpriseVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseSupplierApi;
import com.yiling.user.enterprise.bo.EnterpriseSupplierBO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.dto.request.QuerySupplierPageRequest;
import com.yiling.user.enterprise.dto.request.UpdateSupplierRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 供应商管理表 前端控制器
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-15
 */
@Api(tags = "供应商管理接口")
@Slf4j
@RestController
@RequestMapping("/enterprise/supplier")
public class EnterpriseSupplierController extends BaseController {

    @DubboReference
    EnterpriseSupplierApi enterpriseSupplierApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    @ApiOperation(value = "供应商分页列表")
    @PostMapping("/pageList")
    public Result<Page<EnterpriseSupplierVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QuerySupplierPageForm form) {
        QuerySupplierPageRequest request = PojoUtils.map(form, QuerySupplierPageRequest.class);
        request.setCustomerEid(staffInfo.getCurrentEid());
        Page<EnterpriseSupplierBO> supplierBOPage = enterpriseSupplierApi.queryListPage(request);
        return Result.success(PojoUtils.map(supplierBOPage, EnterpriseSupplierVO.class));
    }

    @ApiOperation(value = "选择供应商下拉列表")
    @PostMapping("/chooseSupplierList")
    public Result<Page<EnterpriseVO>> chooseSupplierList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryEnterpriseListPageForm form) {
        QueryEnterprisePageListRequest request = PojoUtils.map(form, QueryEnterprisePageListRequest.class);
        Page<EnterpriseDTO> enterpriseDTOPage = enterpriseApi.pageList(request);
        return Result.success(PojoUtils.map(enterpriseDTOPage, EnterpriseVO.class));
    }

    @ApiOperation(value = "详情")
    @GetMapping("/get")
    public Result<EnterpriseSupplierVO> get(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam("ID") @RequestParam("id") Long id) {
        EnterpriseSupplierBO supplierBO = enterpriseSupplierApi.get(id);
        return Result.success(PojoUtils.map(supplierBO, EnterpriseSupplierVO.class));
    }

    @ApiOperation(value = "编辑")
    @PostMapping("/update")
    public Result<Boolean> update(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateSupplierForm form) {
        UpdateSupplierRequest request = PojoUtils.map(form, UpdateSupplierRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean result = enterpriseSupplierApi.updateSupplier(request);
        return Result.success(result);
    }


}
