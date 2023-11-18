package com.yiling.user.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.system.bo.Admin;
import com.yiling.user.system.dto.request.QueryAdminPageListRequest;
import com.yiling.user.system.entity.AdminDO;

/**
 * <p>
 * 管理员信息 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-18
 */
@Repository
public interface AdminMapper extends BaseMapper<AdminDO> {

    Page<Admin> pageList(Page<Admin> page, @Param("request") QueryAdminPageListRequest request);

    Admin getByUsername(@Param("username") String username);

    Admin getByMobile(@Param("mobile") String mobile);

    List<Admin> getByName(@Param("name") String name, @Param("limit") Integer limit);
}
