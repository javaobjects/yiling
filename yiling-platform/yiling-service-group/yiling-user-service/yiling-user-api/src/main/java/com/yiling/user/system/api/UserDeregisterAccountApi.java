package com.yiling.user.system.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.system.dto.UserDeregisterAccountDTO;
import com.yiling.user.system.dto.request.AddDeregisterAccountRequest;
import com.yiling.user.system.dto.request.QueryUserDeregisterPageListRequest;
import com.yiling.user.system.dto.request.UpdateUserDeregisterRequest;

/**
 * <p>
 * 用户注销账号 API
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-06
 */
public interface UserDeregisterAccountApi {

    /**
     * 申请注销账号
     *
     * @param request
     * @return
     */
    boolean applyDeregisterAccount(AddDeregisterAccountRequest request);

    /**
     * 注销账号定时任务
     *
     * @return
     */
    boolean deregisterAccountTask();

    /**
     * 审核注销账号
     *
     * @return
     */
    boolean deregisterAccountAuth(UpdateUserDeregisterRequest request);

    /**
     * 根据用户ID查询注销账号记录
     *
     * @param userId
     * @return
     */
    UserDeregisterAccountDTO getByUserId(Long userId);

    /**
     * 账号注销审核分页列表
     *
     * @param request
     * @return
     */
    Page<UserDeregisterAccountDTO> queryPageList(QueryUserDeregisterPageListRequest request);

    /**
     * 根据ID获取注销账号详情
     *
     * @param id
     * @return
     */
    UserDeregisterAccountDTO getById(Long id);
}
