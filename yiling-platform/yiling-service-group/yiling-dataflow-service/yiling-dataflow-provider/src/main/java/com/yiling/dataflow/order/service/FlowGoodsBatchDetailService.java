package com.yiling.dataflow.order.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.bo.FlowStatisticsBO;
import com.yiling.dataflow.order.dto.FlowGoodsBatchDetailDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowGoodsBatchDetailRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailBackupListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailByGbMonthPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailExistRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowStatisticesRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowGoodsBatchDetailRequest;
import com.yiling.dataflow.order.dto.request.UpdateCrmGoodsCodeRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowIndexRequest;
import com.yiling.dataflow.order.entity.FlowGoodsBatchDetailDO;
import com.yiling.dataflow.statistics.bo.GoodsSpecQuantityBO;
import com.yiling.dataflow.statistics.dto.request.FlushGoodsSpecIdRequest;
import com.yiling.framework.common.base.BaseService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * ERP库存汇总同步数据 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-06-14
 */
public interface FlowGoodsBatchDetailService extends BaseService<FlowGoodsBatchDetailDO> {
    /**
     * 查询列表分页
     * @param request
     * @return
     */
    Page<FlowGoodsBatchDetailDO> page(QueryFlowGoodsBatchDetailListPageRequest request);

    Boolean updateFlowGoodsBatchDetailByIds(List<SaveOrUpdateFlowGoodsBatchDetailRequest> requestList);

    Boolean updateFlowGoodsBatchDetailById(SaveOrUpdateFlowGoodsBatchDetailRequest request);

    Integer deleteFlowGoodsBatchDetailByEidAndDate(DeleteFlowGoodsBatchDetailRequest request);

    Integer addFlowGoodsBatchDetailList(List<SaveOrUpdateFlowGoodsBatchDetailRequest> requestList);

    void statisticsFlowGoodsBatch(List<Long> suIdList);

    Boolean executeGoodsBatchStatistics(Long eid, Date date);

    List<GoodsSpecQuantityBO> findGoodsDateQuantityByEidAndDateTime(Long eid, String dateTime, List<Long> specificationIdList);

    List<FlowGoodsBatchDetailDO> getEndMonthCantMatchGoodsQuantityList(List<Long> eidList, String dateTime);

    void flushGoodsSpecificationId(FlushGoodsSpecIdRequest request);

    boolean isUsedSpecificationId(Long specificationId);

    void updateFlowGoodsBatchDetailCrmGoodsSign(List<Long> crmIds);

    /**
     * 查询备份表列表分页
     * @param request
     * @return
     */
    Page<FlowGoodsBatchDetailDO> pageBackup(QueryFlowGoodsBatchDetailBackupListPageRequest request);

    /**
     * 批量刷新索引
     */
    int refreshFlowGoodsBatchDetail(UpdateFlowIndexRequest request);

    /**
     * 批量刷新索引
     */
    int refreshFlowGoodsBatchDetailBackup(UpdateFlowIndexRequest request);


    /**
     * 根据企业id、当前库存时间查询是否有流向数据
     *
     * @param request
     * @return true / false
     */
    boolean isHaveDataByEidAndGbDetailTime(QueryFlowGoodsBatchDetailExistRequest request);

    Page<FlowGoodsBatchDetailDTO> getPageByGbMonth(QueryFlowGoodsBatchDetailByGbMonthPageRequest request);

    void updateCrmGoodsCode(UpdateCrmGoodsCodeRequest request);

    List<FlowStatisticsBO> getFlowGoodsBatchStatistics(QueryFlowStatisticesRequest request);
}
