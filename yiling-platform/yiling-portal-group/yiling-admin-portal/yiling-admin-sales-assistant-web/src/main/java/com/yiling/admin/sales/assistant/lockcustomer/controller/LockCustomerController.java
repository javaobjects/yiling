package com.yiling.admin.sales.assistant.lockcustomer.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.sales.assistant.lockcustomer.form.AddLockCustomerForm;
import com.yiling.admin.sales.assistant.lockcustomer.form.QueryLockCustomerPageForm;
import com.yiling.admin.sales.assistant.lockcustomer.form.UpdateLockCustomerStatusForm;
import com.yiling.admin.sales.assistant.lockcustomer.vo.LockCustomerVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.lockcustomer.api.LockCustomerApi;
import com.yiling.user.lockcustomer.dto.LockCustomerDTO;
import com.yiling.user.lockcustomer.dto.request.AddLockCustomerRequest;
import com.yiling.user.lockcustomer.dto.request.QueryLockCustomerPageRequest;
import com.yiling.user.lockcustomer.dto.request.UpdateLockCustomerStatusRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 销售助手-锁定用户表 前端控制器
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-02
 */
@Slf4j
@RestController
@Api(tags = "用户锁定模块")
@RequestMapping("/lockCustomer")
public class LockCustomerController extends BaseController {

    @DubboReference
    LockCustomerApi lockCustomerApi;

    @ApiOperation(value = "锁定用户分页列表")
    @PostMapping("/queryListPage")
    public Result<Page<LockCustomerVO>> queryListPage(@CurrentUser CurrentAdminInfo adminInfo , @RequestBody @Valid QueryLockCustomerPageForm form) {
        QueryLockCustomerPageRequest request = PojoUtils.map(form, QueryLockCustomerPageRequest.class);
        Page<LockCustomerDTO> customerDTOPage = lockCustomerApi.queryListPage(request);
        return Result.success(PojoUtils.map(customerDTOPage, LockCustomerVO.class));
    }

    @ApiOperation(value = "添加锁定用户")
    @PostMapping("/addLockCustomer")
    @Log(title = "添加锁定用户", businessType = BusinessTypeEnum.INSERT)
    public Result<Void> addLockCustomer(@CurrentUser CurrentAdminInfo adminInfo , @RequestBody @Valid AddLockCustomerForm form) {
        AddLockCustomerRequest request = PojoUtils.map(form, AddLockCustomerRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        lockCustomerApi.addLockCustomer(request);
        return Result.success();
    }

    @ApiOperation(value = "更新锁定用户状态")
    @PostMapping("/updateStatus")
    @Log(title = "更新锁定用户状态", businessType = BusinessTypeEnum.UPDATE)
    public Result<Void> updateStatus(@CurrentUser CurrentAdminInfo adminInfo , @RequestBody @Valid UpdateLockCustomerStatusForm form) {
        UpdateLockCustomerStatusRequest request = PojoUtils.map(form, UpdateLockCustomerStatusRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        lockCustomerApi.updateStatus(request);
        return Result.success();
    }

}
