package com.yiling.sales.assistant.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.task.dto.TaskTerminalDTO;
import com.yiling.sales.assistant.task.dto.request.QueryLockTerminalListRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryTaskTerminalPageRequest;
import com.yiling.sales.assistant.task.entity.UserSelectedTerminalDO;

/**
 * <p>
 * 销售用户已选终端  服务类
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
public interface UserSelectedTerminalService extends BaseService<UserSelectedTerminalDO> {

    /**
     * 查询锁定终端列表
     * @param request
     * @return
     */
    Page<UserSelectedTerminalDO> getLockTerminalList(QueryLockTerminalListRequest request);

    /**
     * 任务选择终端-我的终端（即我添加的客户并且审核通过的）
     * @param queryTaskTerminalPageRequest
     * @return
     */
    Page<TaskTerminalDTO>  listTaskTerminalPage(QueryTaskTerminalPageRequest queryTaskTerminalPageRequest);

    /**
     *
     * todo 废弃 任务选择终端-可选终端（任务区域销售范围和终端范围匹配的交集）
     * @param queryTaskTerminalPageRequest
     * @return
     */
    Page<TaskTerminalDTO>  listTaskAllTerminalPage(QueryTaskTerminalPageRequest queryTaskTerminalPageRequest);


}
