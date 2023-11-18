package com.yiling.user.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.request.QueryStaffListRequest;
import com.yiling.user.system.dto.request.QueryStaffPageListRequest;
import com.yiling.user.system.entity.StaffDO;

/**
 * <p>
 * 员工信息 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-18
 */
@Repository
public interface StaffMapper extends BaseMapper<StaffDO> {

    /**
     * 员工账号分页列表
     *
     * @param page
     * @param request
     * @return
     */
    Page<Staff> pageList(Page page, @Param("request") QueryStaffPageListRequest request);

    /**
     * 查询员工账号列表
     *
     * @param request
     * @return
     */
    List<Staff> list(@Param("request") QueryStaffListRequest request);

    Staff getByUsername(@Param("username") String username);

    Staff getByMobile(@Param("mobile") String mobile);

}
