package com.yiling.admin.hmc.insurance.controller;


import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.hmc.insurance.form.InsuranceCompanyDeleteForm;
import com.yiling.admin.hmc.insurance.form.InsuranceCompanyPageForm;
import com.yiling.admin.hmc.insurance.form.InsuranceCompanySaveForm;
import com.yiling.admin.hmc.insurance.form.InsuranceCompanyStatusForm;
import com.yiling.admin.hmc.insurance.vo.InsuranceCompanyVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.insurance.api.InsuranceApi;
import com.yiling.hmc.insurance.api.InsuranceCompanyApi;
import com.yiling.hmc.insurance.dto.InsuranceCompanyDTO;
import com.yiling.hmc.insurance.dto.InsuranceDTO;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanyDeleteRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanyListRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanyPageRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanySaveRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanyStatusRequest;
import com.yiling.hmc.insurance.enums.InsuranceStatusEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 保险公司表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-23
 */
@Api(tags = "保险公司控制器")
@RestController
@RequestMapping("/insurance/company")
public class InsuranceCompanyController extends BaseController {

    @DubboReference
    InsuranceCompanyApi insuranceCompanyApi;

    @DubboReference
    InsuranceApi insuranceApi;

    @ApiOperation(value = "保险公司信息分页查询")
    @PostMapping("/pageList")
    public Result<Page<InsuranceCompanyVO>> pageList(@RequestBody InsuranceCompanyPageForm form) {
        InsuranceCompanyPageRequest request = PojoUtils.map(form, InsuranceCompanyPageRequest.class);
        Page<InsuranceCompanyDTO> dtoPage = insuranceCompanyApi.pageList(request);
        Page<InsuranceCompanyVO> voPage = PojoUtils.map(dtoPage, InsuranceCompanyVO.class);
        return Result.success(voPage);
    }

    @ApiOperation(value = "通过id查询保险公司信息")
    @GetMapping("/queryById")
    public Result<InsuranceCompanyVO> queryById(@RequestParam("id") @ApiParam("保险公司id") Long id) {
        InsuranceCompanyDTO dto = insuranceCompanyApi.queryById(id);
        InsuranceCompanyVO vo = PojoUtils.map(dto, InsuranceCompanyVO.class);
        return Result.success(vo);
    }

    @ApiOperation(value = "保险公司信息新增和修改")
    @PostMapping("/save")
    public Result saveInsuranceCompany(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid InsuranceCompanySaveForm form) {
        InsuranceCompanySaveRequest request = PojoUtils.map(form, InsuranceCompanySaveRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        InsuranceCompanyListRequest companyListRequest = new InsuranceCompanyListRequest().setCompanyName(request.getCompanyName());
        List<InsuranceCompanyDTO> dtoList = insuranceCompanyApi.listByCondition(companyListRequest);

        if (CollUtil.isNotEmpty(dtoList)) {
            // 新增
            if (null == request.getId()) {
                return Result.failed("服务商名称不允许重复");
            }
            // 编辑
            if (dtoList.size() > 1) {
                return Result.failed("编辑时服务商名称不允许重复");
            }
            InsuranceCompanyDTO insuranceCompanyDTO = dtoList.get(0);
            if (!insuranceCompanyDTO.getId().equals(request.getId())) {
                return Result.failed("编辑服务商名称不允许重复");
            }
        }

        boolean isSuccess = insuranceCompanyApi.saveInsuranceCompany(request);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.failed("保存出现问题");
        }
    }

    @ApiOperation(value = "保险公司信息启用和停用")
    @PostMapping("/modifyStatus")
    public Result modifyStatus(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid InsuranceCompanyStatusForm form) {
        InsuranceCompanyStatusRequest request = PojoUtils.map(form, InsuranceCompanyStatusRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        // 需要保险服务内容全部停用了才允许保险服务商停用
        if (2 == request.getStatus()) {
            List<InsuranceDTO> insuranceDTOList = insuranceApi.listByCompanyIdAndStatus(request.getId(), InsuranceStatusEnum.ENABLE);
            if (CollUtil.isNotEmpty(insuranceDTOList)) {
                return Result.failed("此保险服务商存在正在启用的服务内容，请停用后再停用保险服务商!");
            }
        }
        boolean isSuccess = insuranceCompanyApi.modifyStatus(request);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.failed("操作出现问题");
        }
    }

    @ApiOperation(value = "保险公司删除")
    @PostMapping("/delete")
    public Result deleteInsuranceCompany(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid InsuranceCompanyDeleteForm form) {
        InsuranceCompanyDeleteRequest request = PojoUtils.map(form, InsuranceCompanyDeleteRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        // 若此服务商下面有订单/服务项，不允许删除
        // 1. 此保险公司下面有没有服务内容(保险)才能删除
        List<InsuranceDTO> dtoList = insuranceApi.listByCompanyIdAndStatus(request.getId(), null);
        if (CollUtil.isNotEmpty(dtoList)) {
            return Result.failed("此保险公司下存在服务项，不可删除");
        }
        // 2. 删除保险公司
        boolean isSuccess = insuranceCompanyApi.deleteInsuranceCompany(request);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.failed("删除出现问题");
        }
    }
}
