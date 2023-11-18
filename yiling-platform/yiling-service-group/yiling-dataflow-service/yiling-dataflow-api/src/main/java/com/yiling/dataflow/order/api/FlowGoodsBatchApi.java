package com.yiling.dataflow.order.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dto.FlowGoodsBatchDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowGoodsBatchRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowGoodsBatchRequest;

/**
 * erp库存流向接口
 *
 * @author: houjie.sun
 * @date: 2022/2/14
 */
public interface FlowGoodsBatchApi {
    /**
     * 通过主键和eid获取采购单删除流向
     * @param request
     * @return
     */
    Integer deleteFlowGoodsBatchByGbIdNoAndEid(DeleteFlowGoodsBatchRequest request);

    /**
     * 通过主键id列表删除
     *
     * @param idList
     * @return
     */
    Integer deleteByIdList(List<Long> idList);

    /**
     * 通过主键和eid获取采购单详情
     * @param request
     * @return
     */
    List<FlowGoodsBatchDTO> getFlowGoodsBatchDTOByGbIdNoAndEid(QueryFlowGoodsBatchRequest request);

    /**
     * 通过内码和eid获取采购单详情
     * @param request
     * @return
     */
    List<FlowGoodsBatchDTO> getFlowGoodsBatchDTOByInSnAndEid(QueryFlowGoodsBatchRequest request);

    /**
     * 修改流向
     * @param request
     * @return
     */
    Integer updateFlowGoodsBatchByGbIdNoAndEid(SaveOrUpdateFlowGoodsBatchRequest request);

    /**
     * 修改流向
     * @param request
     * @return
     */
    Integer updateFlowGoodsBatchByInSnAndEid(SaveOrUpdateFlowGoodsBatchRequest request);

    /**
     * 新增流向
     * @param request
     * @return
     */
    Integer insertFlowGoodsBatch(SaveOrUpdateFlowGoodsBatchRequest request);

    /**
     * 查询列表分页
     * @param request
     * @return
     */
    Page<FlowGoodsBatchDTO> page(QueryFlowGoodsBatchListPageRequest request);

    /**
     * 硬删除库存流向
     * @param eids
     * @param createTime
     * @return
     */
    Integer deleteFlowGoodsBatchByEids(List<Long> eids, Date createTime);


    /**
     * 更新同一规格的商品库存总数 totalNumber
     *
     * @param idList
     * @param totalNumber
     * @return
     */
    Boolean updateFlowGoodsBatchTotalNumberByIdList(List<Long> idList, BigDecimal totalNumber);

    /**
     * 修改流向
     * @param request
     * @return
     */
    Boolean updateFlowGoodsBatchById(SaveOrUpdateFlowGoodsBatchRequest request);

    /**
     * 库存流向商品库存数量统计
     */
    void statisticsFlowGoodsBatchTotalNumber();

    void syncFlowGoodsBatchSpec();

    /**
     * 标记crm商品
     * @param eids
     */
    void updateFlowSaleCrmGoodsSign(List<Long> eids);

    /**
     * 根据企业id获取最新的销售时间
     *
     * @return
     */
    Date getMaxGbTimeByEid(Long eid);
}
