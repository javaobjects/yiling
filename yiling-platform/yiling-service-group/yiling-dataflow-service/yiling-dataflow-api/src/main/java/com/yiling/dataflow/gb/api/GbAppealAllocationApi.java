package com.yiling.dataflow.gb.api;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.gb.dto.GbAppealAllocationDTO;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormAllocationPageRequest;

/**
 * 流向团购申诉分配结果表 API
 *
 * @author: houjie.sun
 * @date: 2023/5/15
 */
public interface GbAppealAllocationApi {

    /**
     * 根据团购申诉申请ID，查询团购数据处理扣减/增加 列表分页
     *
     * @param request
     * @return
     */
    Page<GbAppealAllocationDTO> flowStatisticListPage(QueryGbAppealFormAllocationPageRequest request);

    /**
     * 根据团购申诉申请ID、分配类型，查询团购处理结果列表
     *
     * @param appealFormId
     * @param allocationType
     * @return
     */
    List<GbAppealAllocationDTO> listByAppealFormIdAndAllocationType(Long appealFormId, Integer allocationType);

    GbAppealAllocationDTO getById(Long id);

}
