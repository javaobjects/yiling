package com.yiling.dataflow.order.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dto.FlowShopSaleDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowShopSaleByUnlockRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowShopSaleListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowShopSaleRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowShopSaleRequest;

/**
 * @author: houjie.sun
 * @date: 2023/4/10
 */
public interface FlowShopSaleApi {

    /**
     * 查询列表分页
     *
     * @param request
     * @return
     */
    Page<FlowShopSaleDTO> page(QueryFlowShopSaleListPageRequest request);

    /**
     * 通过主键和eid获取连锁纯销详情
     *
     * @param request
     * @return
     */
    List<FlowShopSaleDTO> getFlowSaleDTOBySoIdAndEid(QueryFlowShopSaleRequest request);

    /**
     * 更新数据标签
     * @param idList
     * @param dataTag
     * @return
     */
    Integer updateDataTagByIdList(List<Long> idList, Integer dataTag);

    /**
     * 通过主键id列表删除
     *
     * @param idList
     * @return
     */
    Integer deleteByIdList(List<Long> idList);

    /**
     * 新增流向
     * @param request
     * @return
     */
    FlowShopSaleDTO insertFlowSale(SaveOrUpdateFlowShopSaleRequest request);

    /**
     * 修改流向
     * @param request
     * @return
     */
    Boolean updateFlowSaleById(SaveOrUpdateFlowShopSaleRequest request);

    /**
     * 通过eid和so_time删除连锁纯销流向详情
     * @param request
     * @return
     */
    Integer deleteFlowShopSaleBydEidAndSoTime(DeleteFlowShopSaleByUnlockRequest request);

}
