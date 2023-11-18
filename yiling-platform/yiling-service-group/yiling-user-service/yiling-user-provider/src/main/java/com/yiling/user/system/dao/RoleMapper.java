package com.yiling.user.system.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.system.bo.RoleListItemBO;
import com.yiling.user.system.dto.request.QueryRolePageListRequest;
import com.yiling.user.system.entity.RoleDO;

/**
 * <p>
 * 角色表 Dao 接口
 * </p>
 *
 * @author dexi.yao
 * @date 2021-05-28
 */
@Repository
public interface RoleMapper extends BaseMapper<RoleDO> {

	/**
	 * 根据相关参数查询角色列表
	 * @param page
	 * @param request
	 * @return
	 */
	Page<RoleListItemBO> queryRolePageList(Page<RoleListItemBO> page, @Param("request") QueryRolePageListRequest request);

	/**
	 * 查询营运后台角色列表
	 * 1、列运营后台的角色
	 * 2、列其他系统的，类型为“系统角色”的角色列表
	 * @param page
	 * @param request
	 * @return
	 */
	Page<RoleListItemBO> queryRoleManagePageList(Page<RoleListItemBO> page, @Param("request") QueryRolePageListRequest request);

}
