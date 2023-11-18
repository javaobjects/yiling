package com.yiling.dataflow.flowcollect.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.entity.SaleAppealGoodsFormInfoDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 申诉确认数据单 服务类
 * </p>
 *
 * @author shixing.sun
 * @date 2023-03-14
 */
public interface SaleAppealGoodsFormInfoService extends BaseService<SaleAppealGoodsFormInfoDO> {

    /**
     * @param recordId
     * @param taskId
     * @param opUserId
     * @return
     */
    boolean updateTaskIdByRecordId(Long recordId, Long taskId, Long opUserId);

    /**
     * 窜货申报上传数据分页查询
     *
     * @param request 查询条件
     * @return 窜货申报上传数据
     */
    Page<SaleAppealGoodsFormInfoDO> pageList(QueryFlowMonthPageRequest request);
}
