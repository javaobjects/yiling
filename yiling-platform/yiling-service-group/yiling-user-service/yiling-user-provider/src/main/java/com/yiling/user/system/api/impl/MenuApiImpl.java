package com.yiling.user.system.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.api.MenuApi;
import com.yiling.user.system.bo.MenuListItemBO;
import com.yiling.user.system.dto.MenuDTO;
import com.yiling.user.system.dto.MenuListItemDTO;
import com.yiling.user.system.dto.request.RemoveMenuRequest;
import com.yiling.user.system.dto.request.SaveMenuRequest;
import com.yiling.user.system.enums.MenuTypeEnum;
import com.yiling.user.system.enums.PermissionAppEnum;
import com.yiling.user.system.service.MenuService;
import com.yiling.user.system.service.RoleMenuService;

/**
 * @author dexi.yao
 * @date 2021-06-03
 */
@DubboService
public class MenuApiImpl implements MenuApi {

	@Autowired
	MenuService menuService;
    @Autowired
    RoleMenuService roleMenuService;

    @Override
    public List<MenuDTO> listMenusByUser(PermissionAppEnum appEnum, List<MenuTypeEnum> typeEnums, Long eid, Long userId) {
        return PojoUtils.map(menuService.listMenusByUser(appEnum, typeEnums, eid, userId), MenuDTO.class);
    }

	@Override
	public List<MenuListItemDTO> listMenuTreeByUser(PermissionAppEnum appEnum, List<MenuTypeEnum> typeEnums, Long eid, Long userId) {
		List<MenuListItemBO> menuListItemBOS = menuService.listMenuTreeByUser(appEnum, typeEnums, eid, userId);
		return PojoUtils.map(menuListItemBOS,MenuListItemDTO.class);
	}

    @Override
    public List<Long> listMenuIdsByRoleIds(List<Long> roleIds) {
        return roleMenuService.listMenuIdsByRoleIds(roleIds);
    }

	@Override
	public List<MenuListItemDTO> listMenuTreeByAppId(PermissionAppEnum appEnum, List<MenuTypeEnum> typeEnums, EnableStatusEnum statusEnum) {
        return PojoUtils.map(menuService.listMenuTreeByAppId(appEnum, typeEnums, statusEnum), MenuListItemDTO.class);
	}

	@Override
	public boolean create(SaveMenuRequest request) {
		return menuService.create(request);
	}

	@Override
	public boolean update(SaveMenuRequest request) {
		return menuService.update(request);
	}

	@Override
	public boolean batchDelete(RemoveMenuRequest request) {
    	return menuService.batchDelete(request);
	}
}
