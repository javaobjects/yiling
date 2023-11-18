package com.yiling.admin.hmc.insurance.controller;


import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.hmc.insurance.form.InsurancePageForm;
import com.yiling.admin.hmc.insurance.form.InsuranceSaveForm;
import com.yiling.admin.hmc.insurance.form.InsuranceStatusForm;
import com.yiling.admin.hmc.insurance.vo.InsuranceDetailVO;
import com.yiling.admin.hmc.insurance.vo.InsurancePageVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.insurance.api.InsuranceApi;
import com.yiling.hmc.insurance.api.InsuranceCompanyApi;
import com.yiling.hmc.insurance.api.InsuranceDetailApi;
import com.yiling.hmc.insurance.dto.InsuranceCompanyDTO;
import com.yiling.hmc.insurance.dto.InsuranceDetailDTO;
import com.yiling.hmc.insurance.dto.InsurancePageDTO;
import com.yiling.hmc.insurance.dto.request.InsurancePageRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceSaveRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceStatusRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * C端保险保险表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-23
 */
@Api(tags = "保险控制器")
@RestController
@RequestMapping("/insurance/insurance")
public class InsuranceController extends BaseController {

    @DubboReference
    InsuranceApi insuranceApi;

    @DubboReference
    InsuranceDetailApi insuranceDetailApi;

    @DubboReference
    InsuranceCompanyApi insuranceCompanyApi;

    @ApiOperation(value = "保险公司信息分页查询")
    @PostMapping("/pageList")
    public Result<Page<InsurancePageVO>> pageList(@RequestBody InsurancePageForm form) {
        InsurancePageRequest request = PojoUtils.map(form, InsurancePageRequest.class);
        Page<InsurancePageDTO> dtoPage = insuranceApi.pageList(request);
        Page<InsurancePageVO> voPage = PojoUtils.map(dtoPage, InsurancePageVO.class);
        if (CollUtil.isEmpty(dtoPage.getRecords())) {
            return Result.success(voPage);
        }
        voPage.getRecords().forEach(item -> {
            List<InsuranceDetailDTO> insuranceDetailDTOList = insuranceDetailApi.listByInsuranceId(item.getId());
            List<InsuranceDetailVO> insuranceDetailVOList = PojoUtils.map(insuranceDetailDTOList, InsuranceDetailVO.class);
            item.setInsuranceDetailList(insuranceDetailVOList);
            InsuranceCompanyDTO insuranceCompanyDTO = insuranceCompanyApi.queryById(item.getInsuranceCompanyId());
            item.setCompanyName(insuranceCompanyDTO.getCompanyName());
        });
        return Result.success(voPage);
    }

    @ApiOperation(value = "保险新增和修改")
    @PostMapping("/save")
    public Result saveInsurance(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid InsuranceSaveForm form) {
        InsuranceSaveRequest request = PojoUtils.map(form, InsuranceSaveRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        boolean isSuccess = insuranceApi.saveInsurance(request);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.failed("保存出现问题");
        }
    }

    @ApiOperation(value = "保险启用和停用")
    @PostMapping("/modifyStatus")
    public Result modifyStatus(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid InsuranceStatusForm form) {
        InsuranceStatusRequest request = PojoUtils.map(form, InsuranceStatusRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        boolean isSuccess = insuranceApi.modifyStatus(request);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.failed("操作出现问题");
        }
    }
}
