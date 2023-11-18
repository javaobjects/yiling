package com.yiling.user.system.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.request.CreateStaffByUserIdRequest;
import com.yiling.user.system.dto.request.CreateStaffRequest;
import com.yiling.user.system.dto.request.QueryStaffListRequest;
import com.yiling.user.system.dto.request.QueryStaffPageListRequest;
import com.yiling.user.system.entity.StaffDO;

/**
 * <p>
 * 员工信息 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-18
 */
public interface StaffService extends BaseService<StaffDO> {

    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID
     * @return
     */
    Staff getById(Long id);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return
     */
    Staff getByUsername(String username);

    /**
     * 根据手机号获取用户信息
     *
     * @param mobile 手机号
     * @return
     */
    Staff getByMobile(String mobile);

    /**
     * 根据用户名获取用户信息
     *
     * @param userId 用户ID
     * @return
     */
    StaffDO getByUserId(Long userId);

    /**
     * 员工账号分页列表
     *
     * @param request
     * @return
     */
    Page<Staff> pageList(QueryStaffPageListRequest request);

    /**
     * 员工账号列表
     *
     * @param request
     * @return
     */
    List<Staff> list(QueryStaffListRequest request);

    /**
     * 创建用户
     *
     * @param request
     * @return boolean
     * @author xuan.zhou
     * @date 2022/9/27
     **/
    Long create(CreateStaffRequest request);

    /**
     * 创建员工账号
     *
     * @param request
     * @return
     */
    boolean create(CreateStaffByUserIdRequest request);

    /**
     * 修改密码
     *
     * @param id 用户ID
     * @param password 新密码
     * @param opUserId 操作人ID
     * @return
     */
    boolean updatePassword(Long id, String password, Long opUserId);

    /**
     * 重置密码
     *
     * @param id 用户ID
     * @param opUserId 操作人ID
     * @return
     */
    boolean resetPassword(Long id, Long opUserId);

    /**
     * 校验用户密码
     *
     * @param userId 用户ID
     * @param password 密码明文
     * @return boolean
     * @author xuan.zhou
     * @date 2022/9/27
     **/
    boolean checkPassword(Long userId, String password);

}
