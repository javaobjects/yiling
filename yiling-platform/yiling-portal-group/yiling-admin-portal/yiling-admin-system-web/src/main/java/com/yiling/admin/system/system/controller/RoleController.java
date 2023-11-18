package com.yiling.admin.system.system.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.system.system.form.CreateRoleForm;
import com.yiling.admin.system.system.form.QueryRolePageForm;
import com.yiling.admin.system.system.form.RemoveRoleForm;
import com.yiling.admin.system.system.form.RoleMenuForm;
import com.yiling.admin.system.system.form.UpdateRoleForm;
import com.yiling.admin.system.system.vo.RoleMenuVO;
import com.yiling.admin.system.system.vo.RoleVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.system.api.AdminApi;
import com.yiling.user.system.api.MenuApi;
import com.yiling.user.system.api.RoleApi;
import com.yiling.user.system.bo.Admin;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.MenuListItemDTO;
import com.yiling.user.system.dto.RoleDTO;
import com.yiling.user.system.dto.RoleListItemDTO;
import com.yiling.user.system.dto.request.CreateRoleRequest;
import com.yiling.user.system.dto.request.QueryRolePageListRequest;
import com.yiling.user.system.dto.request.RemoveRoleRequest;
import com.yiling.user.system.dto.request.RoleMenuRequest;
import com.yiling.user.system.dto.request.UpdateRoleInfoRequest;
import com.yiling.user.system.enums.MenuTypeEnum;
import com.yiling.user.system.enums.PermissionAppEnum;
import com.yiling.user.system.enums.RoleTypeEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 角色管理 Controller
 *
 * @author: lun.yu
 * @date: 2021/7/23
 */
@Slf4j
@RestController
@RequestMapping("/role")
@Api(tags = "角色管理接口")
public class RoleController extends BaseController {

    @DubboReference
    RoleApi roleApi;
    @DubboReference
    AdminApi adminApi;
    @DubboReference
    MenuApi menuApi;

    @ApiOperation(value = "角色分页列表")
    @PostMapping("/pageList")
    public Result<Page<RoleVO>> pageList(@RequestBody @Valid QueryRolePageForm form){
        QueryRolePageListRequest request = PojoUtils.map(form, QueryRolePageListRequest.class);
        request.setAppEnum(PermissionAppEnum.getByCode(form.getAppId()));

        Page<RoleListItemDTO> pageList = roleApi.queryRoleManagePageList(request);
        return Result.success(PojoUtils.map(pageList,RoleVO.class));
    }

    @ApiOperation(value = "运营后台所有角色列表")
    @GetMapping("/list")
    public Result<List<RoleVO>> list(){
        List<RoleDTO> roleVOList = roleApi.allRole();
        return Result.success(PojoUtils.map(roleVOList,RoleVO.class));
    }

    @ApiOperation(value = "创建角色")
    @PostMapping ("/create")
    @Log(title = "创建角色", businessType = BusinessTypeEnum.INSERT)
    public  Result<BoolObject> create(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid CreateRoleForm form){
        CreateRoleRequest request = PojoUtils.map(form, CreateRoleRequest.class);
        request.setAppId(PermissionAppEnum.ADMIN.getCode());
        request.setOpUserId(adminInfo.getCurrentUserId());

        boolean result = roleApi.create(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "创建角色关联菜单")
    @PostMapping ("/createRoleMenu")
    @Log(title = "创建角色关联菜单", businessType = BusinessTypeEnum.INSERT)
    public  Result<BoolObject> createRoleMenu(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid RoleMenuForm form){
        RoleMenuRequest request = PojoUtils.map(form, RoleMenuRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());

        boolean result = roleApi.createRoleMenu(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "修改角色")
    @PostMapping ("/update")
    @Log(title = "修改角色", businessType = BusinessTypeEnum.UPDATE)
    public  Result<BoolObject> update(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateRoleForm form){
        UpdateRoleInfoRequest request = PojoUtils.map(form, UpdateRoleInfoRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());

        boolean result = roleApi.updateRole(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "修改角色关联菜单")
    @PostMapping ("/updateRoleMenu")
    @Log(title = "修改角色关联菜单", businessType = BusinessTypeEnum.UPDATE)
    public  Result<BoolObject> updateRoleMenu(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid RoleMenuForm form){
        RoleMenuRequest request = PojoUtils.map(form, RoleMenuRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());

        //系统角色只有admin超级管理员账号可以操作
        RoleDTO roleDTO = Optional.ofNullable(roleApi.getById(form.getRoleId())).orElseThrow(() -> new BusinessException(UserErrorCode.ROLE_NOT_FOUND));
        if(RoleTypeEnum.SYSTEM.getCode().equals(roleDTO.getType())){
            Admin admin = adminApi.getById(request.getOpUserId());
            if(Objects.nonNull(admin) && !admin.isAdmin()){
                throw new BusinessException(UserErrorCode.AUTH_ILLEGAL);
            }
        }

        boolean result = roleApi.updateRoleMenu(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "删除角色")
    @PostMapping ("/batchDelete")
    @Log(title = "删除角色", businessType = BusinessTypeEnum.DELETE)
    public  Result<BoolObject> batchDelete(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid RemoveRoleForm form){
        RemoveRoleRequest request = PojoUtils.map(form, RemoveRoleRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());

        boolean result = roleApi.batchDelete(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "获取角色菜单详情")
    @GetMapping("/getRoleMenu")
    public Result<List<RoleMenuVO>> getRoleMenu(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam @ApiParam(value = "角色ID", required = true) Long id) {
        RoleDTO roleDTO = roleApi.getById(id);
        PermissionAppEnum appEnum = PermissionAppEnum.getByCode(roleDTO.getAppId());

        Admin admin = adminApi.getById(adminInfo.getCurrentUserId());
        List<MenuListItemDTO> menuListItemDTOList;
        if (admin.isAdmin() || appEnum != PermissionAppEnum.ADMIN) {
            menuListItemDTOList = menuApi.listMenuTreeByAppId(appEnum, MenuTypeEnum.all(), EnableStatusEnum.ENABLED);
        } else {
            menuListItemDTOList = menuApi.listMenuTreeByUser(appEnum, MenuTypeEnum.all(), 0L, adminInfo.getCurrentUserId());
        }

        List<RoleMenuVO> roleMenuVOList = PojoUtils.map(menuListItemDTOList, RoleMenuVO.class);

        // 获取角色绑定的菜单ID列表
        List<Long> roleMenuIds = menuApi.listMenuIdsByRoleIds(ListUtil.toList(id));
        // 设置树节点选中
        this.setSelected(roleMenuVOList, roleMenuIds);

        return Result.success(roleMenuVOList);
    }

    private void setSelected(List<RoleMenuVO> list, List<Long> roleMenuIds) {
        if (CollUtil.isEmpty(roleMenuIds)) {
            return;
        }

        list.forEach(e -> {
            e.setSelected(roleMenuIds.contains(e.getId()));

            this.setSelected(e.getChildren(), roleMenuIds);
        });
    }
}
