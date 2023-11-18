package com.yiling.open.user.esb.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.util.ValidateUtils;
import com.yiling.open.user.esb.form.SyncEsbEmployeeForm;
import com.yiling.open.user.esb.form.SyncEsbJobForm;
import com.yiling.open.user.esb.form.SyncEsbOrganizationForm;
import com.yiling.open.user.esb.vo.EsbSyncResult;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.api.EsbJobApi;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.dto.request.SaveOrUpdateEsbEmployeeRequest;
import com.yiling.user.esb.dto.request.SaveOrUpdateEsbJobRequest;
import com.yiling.user.esb.dto.request.SaveOrUpdateEsbOrganizationRequest;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * ESB信息同步接口
 *
 * @author: xuan.zhou
 * @date: 2022/11/25
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/esb/data")
@Api(tags = "ESB数据接口")
public class EsbDataController extends BaseController {

    @Value("${esb.sync.enabled}")
    private Boolean esbSyncEnabled;

    @DubboReference
    EsbEmployeeApi employeeApi;
    @DubboReference
    EsbOrganizationApi esbOrganizationApi;
    @DubboReference
    EsbJobApi esbJobApi;

    @ApiOperation(value = "同步ESB人员信息")
    @PostMapping("/syncEmp")
    public EsbSyncResult syncEmp(@RequestBody SyncEsbEmployeeForm form) {
        if (!esbSyncEnabled) {
            return EsbSyncResult.fail("ESB数据同步接口已关闭");
        }

        String errorMessage = ValidateUtils.failFastValidate(form);
        if (StrUtil.isNotEmpty(errorMessage)) {
            return EsbSyncResult.fail(errorMessage);
        }

        SaveOrUpdateEsbEmployeeRequest request = PojoUtils.map(form, SaveOrUpdateEsbEmployeeRequest.class);
        try {
            employeeApi.saveOrUpdate(request);
        } catch (Exception e) {
            log.error("同步ESB人员信息出错:{}", e.getMessage(), e);
            return EsbSyncResult.fail("同步ESB人员信息出错");
        }

        return EsbSyncResult.success();
    }

    @ApiOperation(value = "同步ESB组织架构")
    @PostMapping("/syncOrg")
    public EsbSyncResult syncOrg(@RequestBody SyncEsbOrganizationForm form) {
        if (!esbSyncEnabled) {
            return EsbSyncResult.fail("ESB数据同步接口已关闭");
        }

        String errorMessage = ValidateUtils.failFastValidate(form);
        if (StrUtil.isNotEmpty(errorMessage)) {
            return EsbSyncResult.fail(errorMessage);
        }

        SaveOrUpdateEsbOrganizationRequest request = PojoUtils.map(form, SaveOrUpdateEsbOrganizationRequest.class);
        try {
            esbOrganizationApi.saveOrUpdate(request);
        } catch (Exception e) {
            log.error("同步ESB组织架构出错:{}", e.getMessage(), e);
            return EsbSyncResult.fail("同步ESB组织架构出错");
        }

        return EsbSyncResult.success();
    }

    @ApiOperation(value = "同步ESB岗位信息")
    @PostMapping("/syncJob")
    public EsbSyncResult syncJob(@RequestBody SyncEsbJobForm form) {
        if (!esbSyncEnabled) {
            return EsbSyncResult.fail("ESB数据同步接口已关闭");
        }

        String errorMessage = ValidateUtils.failFastValidate(form);
        if (StrUtil.isNotEmpty(errorMessage)) {
            return EsbSyncResult.fail(errorMessage);
        }

        SaveOrUpdateEsbJobRequest request = PojoUtils.map(form, SaveOrUpdateEsbJobRequest.class);
        try {
            esbJobApi.saveOrUpdate(request);
        } catch (Exception e) {
            log.error("同步ESB岗位信息出错:{}", e.getMessage(), e);
            return EsbSyncResult.fail("同步ESB岗位信息出错");
        }

        return EsbSyncResult.success();
    }
}
