package com.yiling.sjms.system.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
import com.yiling.sjms.system.vo.MenuListItemVO;
import com.yiling.user.system.api.MenuApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.dto.MenuDTO;
import com.yiling.user.system.dto.MenuListItemDTO;
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
 * @date: 2022/11/24
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
    public Result<CollectionObject<MenuListItemVO>> queryTopMenus(@CurrentUser CurrentSjmsUserInfo userInfo){
        if (userInfo == null) {
            return Result.success(new CollectionObject<>(ListUtil.empty()));
        }

        List<MenuDTO> userMenuList = menuApi.listMenusByUser(PermissionAppEnum.SJMS, ListUtil.toList(MenuTypeEnum.CATALOGUE), 0L, userInfo.getCurrentUserId());
        if (CollUtil.isEmpty(userMenuList)) {
            return Result.success(new CollectionObject<>(ListUtil.empty()));
        }

        List<MenuDTO> userTopMenuList = userMenuList.stream().filter(e -> e.getParentId() == 0L).sorted(Comparator.comparing(MenuDTO::getSortNum)).collect(Collectors.toList());
        return Result.success(new CollectionObject<>(PojoUtils.map(userTopMenuList, MenuListItemVO.class)));
    }

    @ApiOperation(value = "获取左侧菜单列表接口")
    @GetMapping("/queryLeftMenus")
    public  Result<CollectionObject<MenuListItemVO>> queryLeftMenus(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(required = false) @ApiParam(value = "上级菜单ID") Long parentId){
        if (userInfo == null) {
            return Result.success(new CollectionObject<>(ListUtil.empty()));
        }

        List<MenuListItemDTO> menuListItemDTOS = menuApi.listMenuTreeByUser(PermissionAppEnum.SJMS, MenuTypeEnum.all(), 0L, userInfo.getCurrentUserId());
        if (CollUtil.isEmpty(menuListItemDTOS)){
            return Result.success(new CollectionObject<>(new ArrayList<>()));
        }

        List<MenuListItemDTO> userLeftMenuList;
        if (parentId != null && parentId != 0L) {
            userLeftMenuList = menuListItemDTOS.stream().filter(e -> e.getId().equals(parentId)).findFirst().orElse(new MenuListItemDTO()).getChildren();
        } else {
            userLeftMenuList = menuListItemDTOS.get(0).getChildren();
        }
        return Result.success(new CollectionObject<>(PojoUtils.map(userLeftMenuList, MenuListItemVO.class)));
    }

}
