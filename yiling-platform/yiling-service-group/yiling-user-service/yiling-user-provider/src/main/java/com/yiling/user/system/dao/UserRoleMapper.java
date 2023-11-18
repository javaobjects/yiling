package com.yiling.user.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.system.entity.UserRoleDO;

/**
 * <p>
 * 用户角色关联表 Dao 接口
 * </p>
 *
 * @author dexi.yao
 * @date 2021-05-28
 */
@Repository
public interface UserRoleMapper extends BaseMapper<UserRoleDO> {

    /**
     * 绑定用户角色
     *
     * @param list
     * @return
     */
    int bindUserRoles(@Param("list") List<UserRoleDO> list);

}
