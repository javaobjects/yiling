package com.yiling.dataflow.order.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dto.request.DeleteFlowShopSaleByUnlockRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowShopSaleListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowShopSaleRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowShopSaleRequest;
import com.yiling.dataflow.order.entity.FlowShopSaleDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 流向销售明细信息表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2023-04-06
 */
public interface FlowShopSaleService extends BaseService<FlowShopSaleDO> {

    /**
     * 查询列表分页
     * @param request
     * @return
     */
    Page<FlowShopSaleDO> page(QueryFlowShopSaleListPageRequest request);

    /**
     * 通过主键和eid获取连锁纯销详情
     *
     * @param request
     * @return
     */
    List<FlowShopSaleDO> getFlowSaleDTOBySoIdAndEid(QueryFlowShopSaleRequest request);

    /**
     * 通过主键修改数据标记
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
    FlowShopSaleDO insertFlowSale(SaveOrUpdateFlowShopSaleRequest request);

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
