package com.yiling.user.system.service.impl;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.service.EnterpriseEmployeeService;
import com.yiling.user.system.bo.Admin;
import com.yiling.user.system.bo.MenuListItemBO;
import com.yiling.user.system.bo.SjmsUser;
import com.yiling.user.system.dao.MenuMapper;
import com.yiling.user.system.dto.request.RemoveMenuRequest;
import com.yiling.user.system.dto.request.SaveMenuRequest;
import com.yiling.user.system.entity.MenuDO;
import com.yiling.user.system.entity.RoleDO;
import com.yiling.user.system.entity.RoleMenuDO;
import com.yiling.user.system.enums.MenuTypeEnum;
import com.yiling.user.system.enums.PermissionAppEnum;
import com.yiling.user.system.service.AdminService;
import com.yiling.user.system.service.MenuService;
import com.yiling.user.system.service.RoleMenuService;
import com.yiling.user.system.service.RoleService;
import com.yiling.user.system.service.SjmsUserService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-05-28
 */
@Service
@Slf4j
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, MenuDO> implements MenuService {

	@Autowired
    private RoleMenuService           roleMenuService;
	@Autowired
    private RoleService               roleService;
	@Autowired
    private AdminService              adminService;
    @Autowired
    private SjmsUserService sjmsUserService;
    @Autowired
    private EnterpriseEmployeeService enterpriseEmployeeService;

    @Override
    public List<MenuDO> listMenusByUser(PermissionAppEnum appEnum, List<MenuTypeEnum> typeEnums, Long eid, Long userId) {
        List<PermissionAppEnum> appEnums = ListUtil.toList(appEnum);

        if (appEnum == PermissionAppEnum.ADMIN) {
            Admin admin = adminService.getById(userId);
            if (admin.isAdmin()) {
                List<MenuDO> menuDOList = this.listMenusByAppId(PermissionAppEnum.ADMIN, typeEnums, EnableStatusEnum.ENABLED);
                return menuDOList;
            }
        }

        if (appEnum == PermissionAppEnum.SJMS) {
            SjmsUser sjmsUser = sjmsUserService.getById(userId);
            if (sjmsUser.isAdmin()) {
                List<MenuDO> menuDOList = this.listMenusByAppId(PermissionAppEnum.SJMS, typeEnums, EnableStatusEnum.ENABLED);
                return menuDOList;
            }
        }

        if (appEnum == PermissionAppEnum.MALL_ADMIN) {
            boolean isAdmin = enterpriseEmployeeService.isAdmin(eid, userId);
            if (isAdmin) {
                appEnums = ListUtil.toList(PermissionAppEnum.getMallAdminAll());
            } else {
                appEnums = ListUtil.toList(appEnum);
            }
        }

        List<RoleDO> roleDOList = roleService.listByEidUserId(appEnums, eid, userId);
        if (CollUtil.isEmpty(roleDOList)){
            return ListUtil.empty();
        }

        List<Long> roleIds = roleDOList.stream().map(RoleDO::getId).distinct().collect(Collectors.toList());
        List<Long> menuIds = roleMenuService.listMenuIdsByRoleIds(roleIds);
        if (CollUtil.isEmpty(menuIds)) {
            return ListUtil.empty();
        }

        List<MenuDO> menuDOList = this.listByIds(menuIds);
        if (CollUtil.isEmpty(menuDOList)) {
            return ListUtil.empty();
        }

        if (CollUtil.isNotEmpty(typeEnums)) {
            List<Integer> typeCodes = typeEnums.stream().map(MenuTypeEnum::getCode).collect(Collectors.toList());
            menuDOList = menuDOList.stream().filter(e -> typeCodes.contains(e.getMenuType())).collect(Collectors.toList());
        }

        menuDOList = menuDOList.stream().filter(e -> e.getMenuStatus().equals(EnableStatusEnum.ENABLED.getCode())).collect(Collectors.toList());
        if (CollUtil.isEmpty(menuDOList)) {
            return ListUtil.empty();
        }

        return menuDOList;
    }

    @Override
    public List<MenuDO> listMenusByAppId(PermissionAppEnum appEnum, List<MenuTypeEnum> typeEnums, EnableStatusEnum statusEnum) {
        QueryWrapper<MenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MenuDO::getAppId, appEnum.getCode());
        if (CollUtil.isNotEmpty(typeEnums)) {
            queryWrapper.lambda().in(MenuDO::getMenuType, typeEnums.stream().map(MenuTypeEnum::getCode).collect(Collectors.toList()));
        }
        if (statusEnum != EnableStatusEnum.ALL) {
            queryWrapper.lambda().eq(MenuDO::getMenuStatus, statusEnum.getCode());
        }

        List<MenuDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

	@Override
	public List<MenuListItemBO> listMenuTreeByUser(PermissionAppEnum appEnum, List<MenuTypeEnum> typeEnums, Long eid, Long userId) {
		List<MenuDO> menuDOList = this.listMenusByUser(appEnum, typeEnums, eid, userId);
		return this.buildTree(menuDOList);
	}

    @Override
    public List<MenuListItemBO> listMenuTreeByAppId(PermissionAppEnum appEnum, List<MenuTypeEnum> typeEnums, EnableStatusEnum statusEnum) {
        List<MenuDO> menuDOList = this.listMenusByAppId(appEnum, typeEnums, statusEnum);
        return this.buildTree(menuDOList);
    }

    /**
     * 根据菜单标识获取菜单信息
     *
     * @param appId 应用ID
     * @param menuIdentification 菜单标识
     * @return com.yiling.user.system.entity.MenuDO
     * @author xuan.zhou
     * @date 2022/12/12
     **/
    private MenuDO getByMenuIdentification(Integer appId, String menuIdentification) {
        QueryWrapper<MenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(MenuDO::getAppId, appId)
                .eq(MenuDO::getMenuIdentification, menuIdentification)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    private void validate(SaveMenuRequest request) {
        boolean updateFlag = request.getId() != null && request.getId() != 0L;

        PermissionAppEnum permissionAppEnum = PermissionAppEnum.getByCode(request.getAppId());
        if (permissionAppEnum == null) {
            throw new BusinessException(ResultCode.FAILED, "无效的应用ID");
        }

        Long parentId = request.getParentId();
        MenuDO parentMenuDO = null;
        if (parentId != null && parentId != 0L) {
            parentMenuDO = this.getById(parentId);
            if (parentMenuDO == null) {
                throw new BusinessException(ResultCode.FAILED, "无效的父级菜单");
            }

            if (updateFlag) {
                Set<Long> parentIds = CollUtil.newHashSet();
                // 获取父菜单的所有上级菜单ID列表
                this.getParent(parentId, parentIds);
                if (parentIds.contains(request.getId())) {
                    throw new BusinessException(ResultCode.FAILED, "父级菜单不能为当前菜单的子节点");
                }
            }
        }

        MenuTypeEnum menuTypeEnum = MenuTypeEnum.getByCode(request.getMenuType());
        if (menuTypeEnum == MenuTypeEnum.CATALOGUE) {
            if (!(parentMenuDO == null || MenuTypeEnum.getByCode(parentMenuDO.getMenuType()) == MenuTypeEnum.CATALOGUE)) {
                throw new BusinessException(ResultCode.FAILED, "父级菜单必须选择为空或者目录类型");
            }
        } else if(menuTypeEnum == MenuTypeEnum.MENU) {
            if (!(parentMenuDO != null && MenuTypeEnum.getByCode(parentMenuDO.getMenuType()) == MenuTypeEnum.CATALOGUE)) {
                throw new BusinessException(ResultCode.FAILED, "父级菜单必须选择目录类型");
            }
        } else if(menuTypeEnum == MenuTypeEnum.BUTTON) {
            if (!(parentMenuDO != null && MenuTypeEnum.getByCode(parentMenuDO.getMenuType()) == MenuTypeEnum.MENU)) {
                throw new BusinessException(ResultCode.FAILED, "父级菜单必须选择菜单类型");
            }
        } else {
            throw new BusinessException(ResultCode.FAILED, "无效的菜单类型");
        }

        String menuIdentification = request.getMenuIdentification();
        if (StrUtil.isNotEmpty(menuIdentification)) {
            MenuDO entity = this.getByMenuIdentification(request.getAppId(), menuIdentification);
            if (entity != null && !entity.getId().equals(request.getId())) {
                throw new BusinessException(ResultCode.FAILED, "菜单标识重复");
            }
        }
    }

	@Transactional(rollbackFor = Exception.class)
    @Override
    public boolean create(SaveMenuRequest request) {
        // 验证
        this.validate(request);

        MenuDO menuDO = PojoUtils.map(request, MenuDO.class);
        return this.save(menuDO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(SaveMenuRequest request) {
        MenuDO originMenuDO = this.getById(request.getId());
        if (originMenuDO == null) {
            throw new BusinessException(ResultCode.FAILED, "未找到对应的菜单信息");
        }

        // 验证
        this.validate(request);
        // 验证：包含下级节点的菜单不允许修改类型
        if (!originMenuDO.getMenuType().equals(request.getMenuType())) {
            boolean hasChildren = this.hasChildren(request.getId());
            if (hasChildren) {
                throw new BusinessException(ResultCode.FAILED, "包含下级节点的菜单不允许修改类型");
            }
        }

        MenuDO menuDO = PojoUtils.map(request, MenuDO.class);
        menuDO.setOpUserId(request.getOpUserId());
        this.updateById(menuDO);

        // 菜单结构如果发生变化，则需要清理掉这些菜单绑定的角色关系，否则会有问题
        Set<Long> childrenMenuIdList = new HashSet<>();
        if (!originMenuDO.getParentId().equals(request.getParentId())) {
            // 获取所有下级菜单ID集合
            this.getChildren(request.getId(), childrenMenuIdList);

            if (CollUtil.isNotEmpty(childrenMenuIdList)) {
                // 删除这些菜单绑定的角色关系
                QueryWrapper<RoleMenuDO> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().in(RoleMenuDO::getMenuId, childrenMenuIdList);

                RoleMenuDO roleMenuDO = new RoleMenuDO();
                roleMenuDO.setOpUserId(request.getOpUserId());

                roleMenuService.batchDeleteWithFill(roleMenuDO, queryWrapper);
            }
        }

        // 如果菜单状态发生变化
        if (!originMenuDO.getMenuStatus().equals(menuDO.getMenuStatus())) {
            Set<Long> menuIdList = new HashSet<>();
            EnableStatusEnum statusEnum = EnableStatusEnum.getByCode(request.getMenuStatus());
            if (statusEnum == EnableStatusEnum.ENABLED) {
                // 如果变成启用状态，则同时启用所有上级节点
                this.getParent(request.getParentId(), menuIdList);
                // 如果当前类型是菜单，则同时启用下级按钮
                if (MenuTypeEnum.getByCode(request.getMenuType()) == MenuTypeEnum.MENU) {
                    this.getChildren(request.getId(), menuIdList);
                }

                this.updateStatus(menuIdList, EnableStatusEnum.ENABLED, request.getOpUserId());
            } else {
                // 如果变成停用状态，则同时停用所有下级节点
                this.getChildren(request.getId(), menuIdList);
                this.updateStatus(menuIdList, EnableStatusEnum.DISABLED, request.getOpUserId());
            }
        }

        return true;
    }

    private boolean hasChildren(Long id) {
        LambdaQueryWrapper<MenuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuDO::getParentId, id).last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    private void updateStatus(Set<Long> ids, EnableStatusEnum statusEnum, Long opUserId) {
        if (CollUtil.isNotEmpty(ids)) {
            List<MenuDO> menuDOList = CollUtil.newArrayList();
            ids.forEach(e -> {
                MenuDO entity = new MenuDO();
                entity.setId(e);
                entity.setMenuStatus(statusEnum.getCode());
                entity.setOpUserId(opUserId);
                menuDOList.add(entity);
            });
            this.updateBatchById(menuDOList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(RemoveMenuRequest request) {
        if(CollUtil.isEmpty(request.getMenuIdList())){
            return true;
        }

        Set<Long> menuSet = new HashSet<>();
        request.getMenuIdList().forEach(menuId-> this.getChildren(menuId,menuSet));

        MenuDO menuDO = new MenuDO();
        menuDO.setOpUserId(request.getOpUserId());
        LambdaQueryWrapper<MenuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(MenuDO::getId,menuSet);

        boolean result = this.batchDeleteWithFill(menuDO, queryWrapper) > 0;
        if(result){
            //同时删除该菜单被绑定了的角色
            LambdaQueryWrapper<RoleMenuDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(RoleMenuDO::getMenuId, menuSet);
            RoleMenuDO roleMenuDo = new RoleMenuDO();
            roleMenuDo.setOpUserId(request.getOpUserId());
            roleMenuService.batchDeleteWithFill(roleMenuDo,lambdaQueryWrapper);
        }

        return true;
    }

    private void getChildren(Long menuId, Set<Long> menuIdList){
        menuIdList.add(menuId);

        LambdaQueryWrapper<MenuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuDO::getParentId, menuId);
        List<MenuDO> menuDOList = this.list(queryWrapper);
        if (CollUtil.isNotEmpty(menuDOList)) {
            menuDOList.forEach(menuDO -> this.getChildren(menuDO.getId(), menuIdList));
        }
    }

    private void getParent(Long parentId, Set<Long> menuIdList){
        // 上级菜单
        MenuDO menuDO = this.getById(parentId);
        if(menuDO != null){
            menuIdList.add(menuDO.getId());
            this.getParent(menuDO.getParentId(), menuIdList);
        }
    }

    /**
     * 构建菜单树
     *
     * @param menuDOList
     * @return
     */
	private List<MenuListItemBO> buildTree(List<MenuDO> menuDOList) {
	    if (CollUtil.isEmpty(menuDOList)) {
	        return ListUtil.empty();
        }

        // 筛选出顶层菜单
        List<MenuDO> topMenuList = this.getTopMenuList(menuDOList);

        List<MenuListItemBO> list = PojoUtils.map(topMenuList, MenuListItemBO.class);
        List<MenuListItemBO> menuListItemBOList = PojoUtils.map(menuDOList, MenuListItemBO.class);

        // 从顶层菜单迭代往下设置子菜单集合
	    list.forEach(e -> this.setChildren(e, menuListItemBOList));

	    return list;
    }

    /**
     * 从菜单列表中筛选出顶层菜单。<br/>
     * 因为这里的顶层菜单可能parentId为0，也可能不是，所以采用此方法筛选出来。
     *
     * @param menuDOList 菜单列表
     * @return java.util.List<com.yiling.user.system.entity.MenuDO>
     * @author xuan.zhou
     * @date 2022/12/9
     **/
    private List<MenuDO> getTopMenuList(List<MenuDO> menuDOList) {
        List<Long> menuIds = menuDOList.stream().map(MenuDO::getId).distinct().collect(Collectors.toList());
        return menuDOList.stream().filter(e -> !menuIds.contains(e.getParentId())).sorted(Comparator.comparing(MenuDO::getSortNum)).collect(Collectors.toList());
    }

    /**
     * 给每个菜单设置下级菜单
     *
     * @param menuListItemBO
     * @param menuDOList
     */
    private void setChildren(MenuListItemBO menuListItemBO, List<MenuListItemBO> menuDOList) {
        List<MenuListItemBO> children = menuDOList.stream().filter(e -> e.getParentId().equals(menuListItemBO.getId())).sorted(Comparator.comparing(MenuListItemBO::getSortNum)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(children)) {
            children.forEach(c -> this.setChildren(c, menuDOList));
            menuListItemBO.setChildren(children);
        } else {
            menuListItemBO.setChildren(ListUtil.empty());
        }
    }

}
