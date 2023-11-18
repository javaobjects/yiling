package com.yiling.user.web.user.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterprisePlatformDTO;
import com.yiling.user.system.api.MenuApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.MenuListItemDTO;
import com.yiling.user.system.enums.MenuTypeEnum;
import com.yiling.user.system.enums.PermissionAppEnum;
import com.yiling.user.web.user.vo.SimpleMenuTreeVO;
import com.yiling.user.web.user.vo.TopMenuVO;

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
    EnterpriseApi enterpriseApi;
    @DubboReference
    EmployeeApi   employeeApi;
    @DubboReference
    MenuApi       menuApi;

    @ApiOperation(value = "获取顶部菜单列表接口")
    @GetMapping("/queryTopMenus")
    public Result<CollectionObject<TopMenuVO>> queryTopMenus(@CurrentUser CurrentStaffInfo staffInfo){
        EnterprisePlatformDTO enterprisePlatformDTO = enterpriseApi.getEnterprisePlatform(staffInfo.getCurrentEid());
        if (enterprisePlatformDTO == null) {
            return Result.success(new CollectionObject<>(ListUtil.empty()));
        }

        List<MenuListItemDTO> menuListItemDTOS = CollUtil.newArrayList();
        boolean isAdmin = staffInfo.isAdminFlag();
        if (!isAdmin) {
            // 取出用户所有商城菜单列表
            menuListItemDTOS = menuApi.listMenuTreeByUser(PermissionAppEnum.MALL_ADMIN, ListUtil.toList(MenuTypeEnum.CATALOGUE), staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
        }

        List<TopMenuVO> topMenuVOList = CollUtil.newArrayList();
        if (enterprisePlatformDTO.getDataCenterFlag() == 1) {
            boolean hasMenu = menuListItemDTOS.stream().filter(e -> PermissionAppEnum.MALL_ADMIN_DATA_CENTER.getCode().equals(e.getAppId())).findFirst().isPresent();
            if (isAdmin || hasMenu) {
                topMenuVOList.add(new TopMenuVO(PermissionAppEnum.MALL_ADMIN_DATA_CENTER));
            }
        }

        if (enterprisePlatformDTO.getPopFlag() == 1) {
            boolean hasMenu = menuListItemDTOS.stream().filter(e -> PermissionAppEnum.MALL_ADMIN_POP.getCode().equals(e.getAppId())).findFirst().isPresent();
            if (isAdmin || hasMenu) {
                topMenuVOList.add(new TopMenuVO(PermissionAppEnum.MALL_ADMIN_POP));
            }
        }

        if (enterprisePlatformDTO.getMallFlag() == 1) {
            boolean hasMenu = menuListItemDTOS.stream().filter(e -> PermissionAppEnum.MALL_ADMIN_B2B.getCode().equals(e.getAppId())).findFirst().isPresent();
            if (isAdmin || hasMenu) {
                topMenuVOList.add(new TopMenuVO(PermissionAppEnum.MALL_ADMIN_B2B));
            }
        }

        if (enterprisePlatformDTO.getSalesAssistFlag() == 1) {
            boolean hasMenu = menuListItemDTOS.stream().filter(e -> PermissionAppEnum.MALL_ADMIN_SALES_ASSISTANT.getCode().equals(e.getAppId())).findFirst().isPresent();
            if (isAdmin || hasMenu) {
                topMenuVOList.add(new TopMenuVO(PermissionAppEnum.MALL_ADMIN_SALES_ASSISTANT));
            }
        }

        if (enterprisePlatformDTO.getHmcFlag() == 1) {
            boolean hasMenu = menuListItemDTOS.stream().filter(e -> PermissionAppEnum.MALL_ADMIN_HMC.getCode().equals(e.getAppId())).findFirst().isPresent();
            if (isAdmin || hasMenu) {
                topMenuVOList.add(new TopMenuVO(PermissionAppEnum.MALL_ADMIN_HMC));
            }
        }

        return Result.success(new CollectionObject<>(topMenuVOList));
    }

    @ApiOperation(value = "获取左侧菜单列表接口")
    @GetMapping("/queryLeftMenus")
    public  Result<CollectionObject<SimpleMenuTreeVO>> queryLeftMenus(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(required = false)
            @ApiParam(value = "应用ID") Integer appId){
        PermissionAppEnum permissionAppEnum = PermissionAppEnum.getByCode(appId);
        if (permissionAppEnum == null) {
            return Result.success(new CollectionObject<>(ListUtil.empty()));
        }

        List<MenuListItemDTO> menuListItemDTOS;
        if (staffInfo.isAdminFlag()) {
            menuListItemDTOS = menuApi.listMenuTreeByUser(permissionAppEnum, MenuTypeEnum.all(), staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
        } else {
            // 取出用户所有商城菜单列表
            menuListItemDTOS = menuApi.listMenuTreeByUser(PermissionAppEnum.MALL_ADMIN, MenuTypeEnum.all(), staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
            if (CollUtil.isNotEmpty(menuListItemDTOS)) {
                Map<Integer, List<MenuListItemDTO>> menuListItemDTOSByAppId = menuListItemDTOS.stream().collect(Collectors.groupingBy(MenuListItemDTO::getAppId));
                // 从中筛选中某个子系统的菜单列表
                menuListItemDTOS = menuListItemDTOSByAppId.get(appId);
            }
        }

        if (CollUtil.isEmpty(menuListItemDTOS)) {
            return Result.success(new CollectionObject<>(ListUtil.empty()));
        }

        return Result.success(new CollectionObject<>(PojoUtils.map(menuListItemDTOS, SimpleMenuTreeVO.class)));
    }

}
