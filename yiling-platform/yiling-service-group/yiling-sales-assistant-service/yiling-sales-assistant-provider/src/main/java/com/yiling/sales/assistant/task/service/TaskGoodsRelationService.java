package com.yiling.sales.assistant.task.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.task.dto.TaskSelectGoodsDTO;
import com.yiling.sales.assistant.task.dto.request.DeleteGoodsRequest;
import com.yiling.sales.assistant.task.dto.request.QueryGoodsPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskGoodsMatchRequest;
import com.yiling.sales.assistant.task.dto.request.TaskGoodsMatchListDTO;
import com.yiling.sales.assistant.task.entity.TaskGoodsRelationDO;

/**
 * <p>
 * 任务商品关联表  服务类
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
public interface TaskGoodsRelationService extends BaseService<TaskGoodsRelationDO> {

    /**
     * 移除任务药品
     * @param request
     * @return
     */
    Boolean deleteGoods(DeleteGoodsRequest request);

    /**
     * 创建任务-选择商品
     * @param queryGoodsPageRequest
     * @return
     */
    Page<TaskSelectGoodsDTO> queryGoodsForAdd(QueryGoodsPageRequest queryGoodsPageRequest);

    /**
     * 查询用户承接的进行中的交易量任务以岭品
     * @return
     */
    List<TaskGoodsMatchListDTO>  queryTaskGoodsList(List<QueryTaskGoodsMatchRequest> request);
}
