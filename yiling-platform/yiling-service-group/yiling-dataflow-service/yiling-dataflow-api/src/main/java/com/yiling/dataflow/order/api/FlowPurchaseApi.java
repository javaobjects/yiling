package com.yiling.dataflow.order.api;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.bo.FlowStatisticsBO;
import com.yiling.dataflow.order.dto.FlowPurchaseDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseDetailDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseGoodsDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseMonthDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowPurchaseByUnlockRequest;
import com.yiling.dataflow.order.dto.request.DeleteFlowPurchaseRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseByGoodsInSnRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseExistRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowStatisticesRequest;
import com.yiling.dataflow.order.dto.request.QueryPurchaseGoodsListRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowPurchaseRequest;

/**
 * erp采购流向接口
 *
 * @author: shuang.zhang
 * @date: 2022/2/14
 */
public interface FlowPurchaseApi {

    /**
     * 通过主键和eid获取采购单删除流向
     * @param request
     * @return
     */
    Integer deleteFlowPurchaseByPoIdAndEid(DeleteFlowPurchaseRequest request);

    /**
     * 修改流向数据标签
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
     * 通过主键和eid获取采购单详情
     * @param request
     * @return
     */
    List<FlowPurchaseDTO> getFlowPurchaseDTOByPoIdAndEid(QueryFlowPurchaseRequest request);

    /**
     * 修改流向
     * @param request
     * @return
     */
    Integer updateFlowPurchaseByPoIdAndEid(SaveOrUpdateFlowPurchaseRequest request);

    /**
     * 新增流向
     * @param request
     * @return
     */
    FlowPurchaseDTO insertFlowPurchase(SaveOrUpdateFlowPurchaseRequest request);

    /**
     * 查询列表分页
     * @param request
     * @return
     */
    Page<FlowPurchaseDTO> page(QueryFlowPurchaseListPageRequest request);

    /**
     *
     * @param request
     * @return
     */
    List<String> getByEidAndGoodsInSn(QueryFlowPurchaseByGoodsInSnRequest request);

    /**
     * 通过eid和po_time删除采购流向详情
     * @param request
     * @return
     */
    Integer deleteFlowPurchaseBydEidAndPoTime(DeleteFlowPurchaseByUnlockRequest request);

    /**
     * 修改流向
     * @param request
     * @return
     */
    Boolean updateFlowPurchaseById(SaveOrUpdateFlowPurchaseRequest request);

    Boolean updateFlowPurchaseByIds(List<SaveOrUpdateFlowPurchaseRequest> requestList);
    /**
     * 获取flow_purchase结果集中的采购商名称列表
     * @param channelId
     * @return
     */
    List<FlowPurchaseDTO> getFlowPurchaseEnterpriseList(Integer channelId);

    /**
     * 获取flow_purchase结果集中的供应商名称列表
     * @param channelId
     * @return
     */
    List<FlowPurchaseDTO> getFlowPurchaseSupplierList(Integer channelId);

    List<FlowPurchaseMonthDTO> getFlowPurchaseMonthList(QueryFlowPurchaseListRequest request);

    List<FlowPurchaseDetailDTO> getFlowPurchaseDetail(Long eid, Long supplierId, String time);


    List<FlowPurchaseGoodsDTO> getFlowPurchaseGoodsList(QueryPurchaseGoodsListRequest request);

    List<String> getPurchaseGoodsNameList();

    void syncFlowPurchaseSpec();

    void statisticsFlowPurchaseTotalQuantity();

    /**
     * 标记crm商品
     * @param eid
     */
    void updateFlowSaleCrmGoodsSign(List<Long> eid);

    /**
     * 根据企业id获取最新的采购时间
     *
     * @return
     */
    Date getMaxPoTimeByEid(Long eid);

    /**
     * 根据企业id、采购时间查询是否有流向数据
     *
     * @param request
     * @return true / false
     */
    boolean isHaveDataByEidAndPoTime(QueryFlowPurchaseExistRequest request);

    List<FlowStatisticsBO> getFlowPurchaseStatistics(QueryFlowStatisticesRequest request);

}
