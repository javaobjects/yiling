package com.yiling.sales.assistant.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.task.dto.TaskAccompanyingBillDTO;
import com.yiling.sales.assistant.task.dto.request.QueryTaskAccompanyBillPageRequest;
import com.yiling.sales.assistant.task.entity.TaskAccompanyingBillDO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gxl
 * @date 2023-01-11
 */
public interface TaskAccompanyingBillService extends BaseService<TaskAccompanyingBillDO> {

    /**
     * 分页查询
      * @param queryTaskAccompanyBillPageRequest
     * @return
     */
      Page<TaskAccompanyingBillDTO> queryPage(QueryTaskAccompanyBillPageRequest queryTaskAccompanyBillPageRequest);


    /**
     * 随货同行单任务计算进度和佣金
     * @param accompanyingBillId
     */
    void handleTaskAccompanyingBill(Long accompanyingBillId);

}
