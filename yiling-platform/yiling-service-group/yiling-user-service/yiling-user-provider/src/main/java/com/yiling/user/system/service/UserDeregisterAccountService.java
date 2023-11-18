package com.yiling.user.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.system.dto.UserDeregisterAccountDTO;
import com.yiling.user.system.dto.request.AddDeregisterAccountRequest;
import com.yiling.user.system.dto.request.QueryUserDeregisterPageListRequest;
import com.yiling.user.system.dto.request.UpdateUserDeregisterRequest;
import com.yiling.user.system.entity.UserDeregisterAccountDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 注销账号表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-06
 */
public interface UserDeregisterAccountService extends BaseService<UserDeregisterAccountDO> {

    /**
     * 申请注销账号
     *
     * @param request
     * @return
     */
    boolean applyDeregisterAccount(AddDeregisterAccountRequest request);

    /**
     * 根据用户ID查询注销账号记录
     *
     * @param userId
     * @return
     */
    UserDeregisterAccountDO getByUserId(Long userId);

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
     * 注销账号审核分页列表
     *
     * @param request
     * @return
     */
    Page<UserDeregisterAccountDTO> queryPageList(QueryUserDeregisterPageListRequest request);
}
