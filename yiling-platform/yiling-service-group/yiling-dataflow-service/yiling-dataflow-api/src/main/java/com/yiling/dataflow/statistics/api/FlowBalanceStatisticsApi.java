package com.yiling.dataflow.statistics.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.report.dto.request.DeteleTimeRequest;
import com.yiling.dataflow.statistics.bo.FlowDailyStatisticsBO;
import com.yiling.dataflow.statistics.dto.DeteleFlowBalanceStatisticeRequest;
import com.yiling.dataflow.statistics.dto.FlowBalanceStatisticsDTO;
import com.yiling.dataflow.statistics.dto.FlowBalanceStatisticsMonthDTO;
import com.yiling.dataflow.statistics.dto.GoodsSpecInfoDTO;
import com.yiling.dataflow.statistics.dto.GoodsSpecNoMatchedDTO;
import com.yiling.dataflow.statistics.dto.GoodsSpecStatisticsDTO;
import com.yiling.dataflow.statistics.dto.request.FlushGoodsSpecIdRequest;
import com.yiling.dataflow.statistics.dto.request.GoodsSpecStatisticsRequest;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsMonthRequest;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsPageRequest;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsRequest;
import com.yiling.dataflow.statistics.dto.request.RecommendScoreRequest;
import com.yiling.dataflow.statistics.dto.request.SaveFlowBalanceStatisticsRequest;

/**
 * @author: shuang.zhang
 * @date: 2022/7/11
 */
public interface FlowBalanceStatisticsApi {
    /**
     * 删除统计数据
     * @return
     */
    Integer deleteFlowBalanceStatistics(DeteleFlowBalanceStatisticeRequest request);

    List<FlowBalanceStatisticsDTO> getFlowBalanceStatisticsEnterpriseList();

    Page<FlowBalanceStatisticsMonthDTO> getMonthPageList(QueryBalanceStatisticsMonthRequest request);

    List<FlowBalanceStatisticsDTO> getFlowBalanceStatisticsList(QueryBalanceStatisticsRequest request);

    Page<GoodsSpecStatisticsDTO> getGoodsSpecStatisticsListPage(GoodsSpecStatisticsRequest request);

    List<GoodsSpecInfoDTO> getGoodsSpecInfoList(Long eid, String monthTime);

    List<GoodsSpecNoMatchedDTO> goodsSpecNoMatchedList(GoodsSpecStatisticsRequest request);

    Integer getRecommendScore(RecommendScoreRequest request);

    void flushGoodsSpecificationId(FlushGoodsSpecIdRequest request);

    Boolean isUsedSpecificationId(Long specificationId);

    Page<FlowDailyStatisticsBO> page(QueryBalanceStatisticsPageRequest request);
}
