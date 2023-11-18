package com.yiling.user.system.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.user.system.bo.MenuListItemBO;
import com.yiling.user.system.dto.request.RemoveMenuRequest;
import com.yiling.user.system.dto.request.SaveMenuRequest;
import com.yiling.user.system.entity.MenuDO;
import com.yiling.user.system.enums.MenuTypeEnum;
import com.yiling.user.system.enums.PermissionAppEnum;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-05-28
 */
public interface MenuService extends BaseService<MenuDO> {

    /**
     * 获取某个用户对应的菜单列表
     *
     * @param appEnum 系统类型枚举
     * @param typeEnums 菜单类型枚举
     * @param eid 企业ID
     * @param userId 用户ID
     * @return
     */
    List<MenuDO> listMenusByUser(PermissionAppEnum appEnum, List<MenuTypeEnum> typeEnums, Long eid, Long userId);

    /**
     * 获取某个系统对应的菜单列表
     *
     * @param appEnum 系统类型枚举
     * @param typeEnums 菜单类型枚举
     * @param statusEnum 菜单状态枚举
     * @return
     */
    List<MenuDO> listMenusByAppId(PermissionAppEnum appEnum, List<MenuTypeEnum> typeEnums, EnableStatusEnum statusEnum);

    /**
     * 获取用户菜单树
     *
     * @param appEnum 系统类型枚举
     * @param typeEnums 菜单类型枚举
     * @param eid 企业ID
     * @param userId 用户ID
     * @return
     */
	List<MenuListItemBO> listMenuTreeByUser(PermissionAppEnum appEnum, List<MenuTypeEnum> typeEnums, Long eid, Long userId);

    /**
     * 获取系统菜单树
     *
     * @param appEnum 系统类型枚举
     * @param typeEnums 菜单类型枚举
     * @param statusEnum 菜单状态枚举
     * @return
     */
    List<MenuListItemBO> listMenuTreeByAppId(PermissionAppEnum appEnum, List<MenuTypeEnum> typeEnums, EnableStatusEnum statusEnum);

    /**
     * 创建菜单
     * @param request
     * @return
     */
    boolean create(SaveMenuRequest request);

    /**
     * 更新菜单
     * @param request
     * @return
     */
    boolean update(SaveMenuRequest request);

    /**
     * 删除菜单
     * @param request
     * @return
     */
    boolean batchDelete(RemoveMenuRequest request);
}
