package com.yiling.sales.assistant.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.task.dto.AccompanyingBillMatchDTO;
import com.yiling.sales.assistant.task.dto.request.QueryMatchBillPageRequest;
import com.yiling.sales.assistant.task.entity.AccompanyingBillMatchDO;

/**
 * <p>
 * 随货同行单匹配流向 服务类
 * </p>
 *
 * @author gxl
 * @date 2023-01-09
 */
public interface AccompanyingBillMatchService extends BaseService<AccompanyingBillMatchDO> {

    /**
     * 分页列表
     * @param request
     * @return
     */
    Page<AccompanyingBillMatchDTO> queryPage(QueryMatchBillPageRequest request);

    /**
     * 详情
     * @param id
     * @return
     */
    AccompanyingBillMatchDTO getDetail(Long id);

    /**
     * 匹配流向定时任务
     */
    void billFlowMatchTimer();
}
