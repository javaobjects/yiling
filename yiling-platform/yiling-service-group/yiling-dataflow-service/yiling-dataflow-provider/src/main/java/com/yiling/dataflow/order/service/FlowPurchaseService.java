package com.yiling.dataflow.order.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yiling.dataflow.order.bo.FlowStatisticsBO;
import com.yiling.dataflow.order.dto.request.QueryFlowStatisticesRequest;
import org.apache.ibatis.annotations.Param;
import com.yiling.dataflow.order.dto.request.UpdateEnterpriseCrmCodeRequest;
import com.yiling.dataflow.order.dto.request.UpdateSupplierCrmCodeRequest;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dto.FlowPurchaseDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowPurchaseByUnlockRequest;
import com.yiling.dataflow.order.dto.request.DeleteFlowPurchaseRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseBackupListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseByGoodsInSnRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseByPoMonthPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseExistRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchasePageByEidAndGoodsInSnRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseRequest;
import com.yiling.dataflow.order.dto.request.QueryPurchaseGoodsListRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowPurchaseRequest;
import com.yiling.dataflow.order.dto.request.UpdateCrmGoodsCodeRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowIndexRequest;
import com.yiling.dataflow.order.entity.FlowPurchaseDO;
import com.yiling.dataflow.order.entity.FlowPurchaseDetailDO;
import com.yiling.dataflow.statistics.dto.request.FlushGoodsSpecIdRequest;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 采购流向表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-02-11
 */
@Service
public interface FlowPurchaseService extends BaseService<FlowPurchaseDO> {

    /**
     * 通过op库主键和商业公司查询流向是否存在
     * @param request
     * @return
     */
    Integer deleteFlowPurchaseByPoIdAndEid(DeleteFlowPurchaseRequest request);

    /**
     * 通过主键id列表删除
     *
     * @param idList
     * @return
     */
    Integer deleteByIdList(List<Long> idList);

    /**
     * 修改流向数据标签
     * @param idList
     * @param dataTag
     * @return
     */
    Integer updateDataTagByIdList(List<Long> idList, Integer dataTag);

    /**
     * 通过op库主键和商业公司查询流向列表数据
     * @param request
     * @return
     */
    List<FlowPurchaseDO> getFlowPurchaseDTOByPoIdAndEid(QueryFlowPurchaseRequest request);

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
    FlowPurchaseDO insertFlowPurchase(SaveOrUpdateFlowPurchaseRequest request);

    /**
     * 查询列表分页
     * @param request
     * @return
     */
    Page<FlowPurchaseDO> page(QueryFlowPurchaseListPageRequest request);

    /**
     *
     * @param request
     * @return
     */
    List<String> getByEidAndGoodsInSn(QueryFlowPurchaseByGoodsInSnRequest request);

    /**
     * 通过主键和eid获取采购单详情
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
     * 根据eid、goodsInSn查询分页
     *
     * @param request
     * @return
     */
    Page<FlowPurchaseDO> pageByEidAndGoodsInSn(QueryFlowPurchasePageByEidAndGoodsInSnRequest request);

    /**
     * 获取flow_purchase结果集中的采购商名称列表
     * @param channelId
     * @return
     */
    List<FlowPurchaseDO> getFlowPurchaseEnterpriseList(Integer channelId);

    /**
     * 获取flow_purchase结果集中的供应商名称列表
     * @param channelId
     * @return
     */
    List<FlowPurchaseDO> getFlowPurchaseSupplierList(Integer channelId);

    List<Map<String, Object>> getFlowPurchaseMonthList(QueryFlowPurchaseListRequest request);

    List<Map<String, Object>> getFlowPurchaseAllMonthList(QueryFlowPurchaseListRequest request);

    List<FlowPurchaseDetailDO> getFlowPurchaseDetail(Long eid, Long supplierId, String time);

    List<Map<String, Object>> getFlowPurchaseGoodsMonthList(QueryPurchaseGoodsListRequest request);

    List<String> getPurchaseGoodsNameList();

    void syncFlowPurchaseSpec();

    void updateFlowPurchaseCrmGoodsSign(List<Long> crmIds);

    List<FlowPurchaseDO> getCantMatchGoodsQuantityList(List<Long> eidList, String monthTime);

    void flushGoodsSpecificationId(FlushGoodsSpecIdRequest request);

    boolean isUsedSpecificationId(Long specificationId);

    /**
     * 查询备份表列表分页
     * @param request
     * @return
     */
    Page<FlowPurchaseDO> pageBackup(QueryFlowPurchaseBackupListPageRequest request);

    /**
     * 根据企业id获取最新的采购时间
     *
     * @return
     */
    Date getMaxPoTimeByEid(Long eid);

    /**
     * 批量刷新索引
     */
    int refreshFlowPurchase(UpdateFlowIndexRequest request);

    /**
     * 批量刷新索引
     */
    int refreshFlowPurchaseBackup(UpdateFlowIndexRequest request);

    Integer getCountByEidAndSoTime(Long eid, Date startTime, Date endTime);

    /**
     * 根据企业id、采购时间查询是否有流向数据
     *
     * @param request
     * @return true / false
     */
    boolean isHaveDataByEidAndPoTime(QueryFlowPurchaseExistRequest request);

    Page<FlowPurchaseDTO> getPageByPoMonth(QueryFlowPurchaseByPoMonthPageRequest request);

    void updateCrmGoodsCode(UpdateCrmGoodsCodeRequest request);

    List<FlowStatisticsBO> getFlowPurchaseStatistics(QueryFlowStatisticesRequest request);

    void updateSupplierCrmCodeCode(UpdateSupplierCrmCodeRequest request);
}
