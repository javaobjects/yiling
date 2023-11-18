package com.yiling.admin.system.system.controller;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.system.system.form.QueryAdminPageListForm;
import com.yiling.admin.system.system.form.SaveAdminForm;
import com.yiling.admin.system.system.form.UpdateAdminStatusForm;
import com.yiling.admin.system.system.vo.AdminVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.system.api.AdminApi;
import com.yiling.user.system.api.RoleApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.Admin;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.RoleDTO;
import com.yiling.user.system.dto.request.QueryAdminPageListRequest;
import com.yiling.user.system.dto.request.SaveAdminRequest;
import com.yiling.user.system.dto.request.UpdateAdminRequest;
import com.yiling.user.system.dto.request.UpdateUserStatusRequest;
import com.yiling.user.system.enums.PermissionAppEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 管理员 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/5/12
 */
@RestController
@RequestMapping("/system/admin")
@Api(tags = "管理员接口")
@Slf4j
public class AdminController extends BaseController {

    @DubboReference
    UserApi userApi;
    @DubboReference
    AdminApi adminApi;
    @DubboReference
    RoleApi roleApi;

    @ApiOperation(value = "用户分页列表")
    @PostMapping("/pageList")
    public Result<Page<AdminVO>> pageList(@RequestBody QueryAdminPageListForm form) {
        QueryAdminPageListRequest request = PojoUtils.map(form, QueryAdminPageListRequest.class);
        Page<Admin> page = adminApi.pageList(request);

        List<AdminVO> adminVoList = page.getRecords().stream().map(admin -> {
            List<Long> roleList = roleApi.listByEidUserId(PermissionAppEnum.ADMIN, 0L, admin.getId()).stream().map(RoleDTO::getId).collect(Collectors.toList());
            AdminVO adminVO = PojoUtils.map(admin, AdminVO.class);
            adminVO.setRoleIdList(roleList);
            return adminVO;
        }).collect(Collectors.toList());

        Page<AdminVO> voPage = PojoUtils.map(page, AdminVO.class);
        voPage.setRecords(adminVoList);

        return Result.success(voPage);
    }

    @ApiOperation(value = "保存用户信息")
    @PostMapping("/save")
    @Log(title = "保存用户信息", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> save(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveAdminForm form) {
        SaveAdminRequest request = PojoUtils.map(form, SaveAdminRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());

        boolean result = adminApi.save(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping("/get")
    public Result<AdminVO> get(@RequestParam("id") Long id) {
        Admin admin = adminApi.getById(id);
        if (admin == null) {
            return Result.failed("用户信息不存在");
        }

        List<Long> roleList = roleApi.listByEidUserId(PermissionAppEnum.ADMIN, 0L, id).stream().map(RoleDTO::getId).collect(Collectors.toList());
        AdminVO adminVO = PojoUtils.map(admin, AdminVO.class);
        adminVO.setRoleIdList(roleList);

        return Result.success(adminVO);
    }

    @ApiOperation(value = "用户启用/停用")
    @PostMapping("/updateStatus")
    @Log(title = "用户启用/停用", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> updateStatus(@CurrentUser CurrentAdminInfo adminInfo,@RequestBody @Valid UpdateAdminStatusForm form) {
        Admin admin = adminApi.getById(form.getUserId());
        if(Objects.nonNull(admin) && admin.isAdmin()){
            if(form.getUserId().compareTo(adminInfo.getCurrentUserId()) != 0){
                throw new BusinessException(UserErrorCode.AUTH_ILLEGAL);
            }
        }

        UpdateUserStatusRequest request = new UpdateUserStatusRequest();
        request.setId(form.getUserId());
        request.setStatus(form.getStatus());
        request.setOpUserId(adminInfo.getCurrentUserId());

        boolean result = userApi.updateStatus(request);
        return Result.success(new BoolObject(result));
    }
}
