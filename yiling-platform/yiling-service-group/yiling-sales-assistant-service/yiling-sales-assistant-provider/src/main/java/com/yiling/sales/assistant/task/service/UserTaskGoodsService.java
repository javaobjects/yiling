package com.yiling.sales.assistant.task.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.task.dto.TaskTraceGoodsDTO;
import com.yiling.sales.assistant.task.dto.request.QueryTaskTraceGoodsRequest;
import com.yiling.sales.assistant.task.entity.UserTaskGoodsDO;

/**
 * <p>
 * 用户任务商品  服务类
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
public interface UserTaskGoodsService extends BaseService<UserTaskGoodsDO> {

    /**
     * 任务追踪-任务品完成进度查询
     * @param queryTaskTraceGoodsRequest
     * @return
     */
    List<TaskTraceGoodsDTO> listTaskTraceGoods(QueryTaskTraceGoodsRequest queryTaskTraceGoodsRequest);
}
