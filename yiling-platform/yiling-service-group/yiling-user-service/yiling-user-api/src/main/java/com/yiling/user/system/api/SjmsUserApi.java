package com.yiling.user.system.api;

import java.util.List;

import com.yiling.user.system.bo.SjmsUser;
import com.yiling.user.system.dto.request.CreateSjmsUserRequest;

/**
 * 神机妙算用户 API
 *
 * @author: xuan.zhou
 * @date: 2022/11/24
 */
public interface SjmsUserApi {

    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID
     * @return
     */
    SjmsUser getById(Long id);

    /**
     * 根据批量用户ID获取用户信息
     *
     * @param ids 用户ID集合
     * @return
     */
    List<SjmsUser> listByIds(List<Long> ids);

    /**
     * 根据ESB人员工号获取用户信息
     *
     * @param empId ESB人员工号
     * @return
     */
    SjmsUser getByEmpId(String empId);

    /**
     * 创建用户
     *
     * @param request
     * @return boolean
     * @author xuan.zhou
     * @date 2022/9/27
     **/
    Long create(CreateSjmsUserRequest request);
}
