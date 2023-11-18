package com.yiling.admin.system.system.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.admin.system.system.form.RemoveMenuForm;
import com.yiling.admin.system.system.form.SaveMenuForm;
import com.yiling.admin.system.system.vo.MenuAppVO;
import com.yiling.admin.system.system.vo.MenuListItemVO;
import com.yiling.admin.system.system.vo.SimpleMenuTreeVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.user.system.api.MenuApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.MenuDTO;
import com.yiling.user.system.dto.MenuListItemDTO;
import com.yiling.user.system.dto.request.RemoveMenuRequest;
import com.yiling.user.system.dto.request.SaveMenuRequest;
import com.yiling.user.system.enums.MenuTypeEnum;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 菜单模块 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/7/7
 */
@RestController
@RequestMapping("/menu")
@Api(tags = "菜单模块接口")
@Slf4j
public class MenuController extends BaseController {

    @DubboReference
    MenuApi menuApi;

    @ApiOperation(value = "获取顶部菜单列表接口")
    @GetMapping("/queryTopMenus")
    public Result<CollectionObject<MenuListItemVO>> queryTopMenus(@CurrentUser CurrentAdminInfo adminInfo){
        if (adminInfo == null) {
            return Result.success(new CollectionObject<>(ListUtil.empty()));
        }

        List<MenuDTO> userMenuList = menuApi.listMenusByUser(PermissionAppEnum.ADMIN, ListUtil.toList(MenuTypeEnum.CATALOGUE), 0L, adminInfo.getCurrentUserId());
        if (CollUtil.isEmpty(userMenuList)) {
            return Result.success(new CollectionObject<>(ListUtil.empty()));
        }

        userMenuList = userMenuList.stream().filter(e -> e.getParentId() == 0L).sorted(Comparator.comparing(MenuDTO::getSortNum)).collect(Collectors.toList());
        return Result.success(new CollectionObject<>(PojoUtils.map(userMenuList, MenuListItemVO.class)));
    }

    @ApiOperation(value = "获取左侧菜单列表接口")
    @GetMapping("/queryLeftMenus")
    public  Result<CollectionObject<SimpleMenuTreeVO>> queryLeftMenus(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam(required = true) @ApiParam(value = "上级菜单ID") Long parentId){
        if (adminInfo == null) {
            return Result.success(new CollectionObject<>(ListUtil.empty()));
        }

        List<MenuListItemDTO> menuListItemDTOS = menuApi.listMenuTreeByUser(PermissionAppEnum.ADMIN, MenuTypeEnum.all(), 0L, adminInfo.getCurrentUserId());
        if (CollUtil.isEmpty(menuListItemDTOS)){
            return Result.success(new CollectionObject<>(new ArrayList<>()));
        }

        List<MenuListItemDTO> userLeftMenuList;
        if (parentId != null && parentId != 0L) {
            userLeftMenuList = menuListItemDTOS.stream().filter(e -> e.getId().equals(parentId)).findFirst().orElse(new MenuListItemDTO()).getChildren();
        } else {
            userLeftMenuList = menuListItemDTOS.get(0).getChildren();
        }
        return Result.success(new CollectionObject<>(PojoUtils.map(userLeftMenuList, SimpleMenuTreeVO.class)));
    }

    @ApiOperation(value = "获取菜单所属的应用列表")
    @GetMapping("/listMenuApps")
    public Result<CollectionObject<MenuAppVO>> listMenuApps(@CurrentUser CurrentAdminInfo adminInfo){
        List<PermissionAppEnum> permissionAppEnums = CollUtil.newArrayList();
        permissionAppEnums.add(PermissionAppEnum.ADMIN);
        permissionAppEnums.add(PermissionAppEnum.MALL_ADMIN_DATA_CENTER);
        permissionAppEnums.add(PermissionAppEnum.MALL_ADMIN_POP);
        permissionAppEnums.add(PermissionAppEnum.MALL_ADMIN_B2B);
        permissionAppEnums.add(PermissionAppEnum.MALL_ADMIN_SALES_ASSISTANT);
        permissionAppEnums.add(PermissionAppEnum.MALL_ADMIN_HMC);
        permissionAppEnums.add(PermissionAppEnum.SJMS);

        List<MenuAppVO> menuAppVOS = CollUtil.newArrayList();
        permissionAppEnums.forEach(e -> {
            menuAppVOS.add(new MenuAppVO(e));
        });

        return Result.success(new CollectionObject<>(menuAppVOS));
    }

    @ApiOperation(value = "获取应用对应的菜单列表")
    @GetMapping("/queryMenusList")
    public Result<CollectionObject<MenuListItemVO>> queryMenusList(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("appId") @ApiParam("应用ID") Integer appId){
        List<MenuListItemDTO> menuListItemDTOList = menuApi.listMenuTreeByAppId(PermissionAppEnum.getByCode(appId), MenuTypeEnum.all(), EnableStatusEnum.ALL);
        return Result.success(new CollectionObject<>(PojoUtils.map(menuListItemDTOList, MenuListItemVO.class)));
    }

    @ApiOperation(value = "获取菜单目录树")
    @GetMapping("/queryCatalogTree")
    public Result<CollectionObject<SimpleMenuTreeVO>> queryCatalogTree(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("appId") @ApiParam("应用ID") Integer appId){
        List<MenuListItemDTO> menuListItemDTOList = menuApi.listMenuTreeByAppId(PermissionAppEnum.getByCode(appId), MenuTypeEnum.catalogueAndMenu(), EnableStatusEnum.ENABLED);
        return Result.success(new CollectionObject<>(PojoUtils.map(menuListItemDTOList, SimpleMenuTreeVO.class)));
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping("/create")
    @Log(title = "新增菜单", businessType = BusinessTypeEnum.INSERT)
    public Result<BoolObject> create(@CurrentUser CurrentAdminInfo adminInfo,@RequestBody @Valid SaveMenuForm menuForm){
        SaveMenuRequest request = PojoUtils.map(menuForm, SaveMenuRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        boolean result = menuApi.create(request);

        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "修改菜单")
    @PostMapping("/update")
    @Log(title = "修改菜单", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> update(@CurrentUser CurrentAdminInfo adminInfo,@RequestBody @Valid SaveMenuForm menuForm){
        SaveMenuRequest request = PojoUtils.map(menuForm, SaveMenuRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        boolean result = menuApi.update(request);

        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "删除菜单")
    @PostMapping("/batchDelete")
    @Log(title = "删除菜单", businessType = BusinessTypeEnum.DELETE)
    public Result<BoolObject> batchDelete(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody RemoveMenuForm menuForm){
        RemoveMenuRequest request = PojoUtils.map(menuForm, RemoveMenuRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        boolean result = menuApi.batchDelete(request);

        return Result.success(new BoolObject(result));
    }

}
