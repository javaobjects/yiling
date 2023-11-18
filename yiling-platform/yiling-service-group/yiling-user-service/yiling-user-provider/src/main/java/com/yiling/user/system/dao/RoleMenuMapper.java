package com.yiling.user.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.system.entity.RoleMenuDO;

/**
 * <p>
 * 角色菜单关联表 Dao 接口
 * </p>
 *
 * @author dexi.yao
 * @date 2021-05-28
 */
@Repository
public interface RoleMenuMapper extends BaseMapper<RoleMenuDO> {

    /**
     * 绑定菜单角色
     *
     * @param list
     * @return
     */
    int bindRoleMenus(@Param("list") List<RoleMenuDO> list);
}
