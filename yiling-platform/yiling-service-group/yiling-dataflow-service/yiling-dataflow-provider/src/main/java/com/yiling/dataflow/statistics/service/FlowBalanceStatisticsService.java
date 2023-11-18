package com.yiling.dataflow.statistics.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.statistics.bo.EnterpriseMonthQuantityBO;
import com.yiling.dataflow.statistics.bo.FlowDailyStatisticsBO;
import com.yiling.dataflow.statistics.bo.GoodsSpecInfoBO;
import com.yiling.dataflow.statistics.bo.GoodsSpecNoMatchedBO;
import com.yiling.dataflow.statistics.bo.GoodsSpecStatisticsBO;
import com.yiling.dataflow.statistics.dto.DeteleFlowBalanceStatisticeRequest;
import com.yiling.dataflow.statistics.dto.ErpClientDataDTO;
import com.yiling.dataflow.statistics.dto.request.FlushGoodsSpecIdRequest;
import com.yiling.dataflow.statistics.dto.request.GoodsSpecStatisticsRequest;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsMonthRequest;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsPageRequest;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsRequest;
import com.yiling.dataflow.statistics.dto.request.SaveFlowBalanceStatisticsRequest;
import com.yiling.dataflow.statistics.entity.FlowBalanceStatisticsDO;
import com.yiling.framework.common.base.BaseService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商业公司每天平衡表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-07-11
 */
public interface FlowBalanceStatisticsService extends BaseService<FlowBalanceStatisticsDO> {
    Integer deleteFlowBalanceStatistics(DeteleFlowBalanceStatisticeRequest request);
    Integer saveFlowBalanceStatisticsList(List<FlowBalanceStatisticsDO> request);

    List<FlowBalanceStatisticsDO> getFlowBalanceStatisticsEnterpriseList();

    Page<FlowBalanceStatisticsDO> getMonthPageList(QueryBalanceStatisticsMonthRequest request);

    List<EnterpriseMonthQuantityBO> getEnterpriseBeginMonthList(String beginMonth, List<Long> eidList);

    List<FlowBalanceStatisticsDO> getFlowBalanceStatisticsList(QueryBalanceStatisticsRequest request);

    Page<GoodsSpecStatisticsBO> getGoodsSpecStatisticsListPage(GoodsSpecStatisticsRequest request);

    List<GoodsSpecInfoBO> getGoodsSpecInfoList(Long eid, String monthTime);

    List<EnterpriseMonthQuantityBO> getEnterpriseCurrentMonthList(String time, List<Long> eidList);

    List<FlowBalanceStatisticsDO> getEnterpriseMonthFlowList(String monthTime, List<Long> eidList);

    List<GoodsSpecNoMatchedBO> goodsSpecNoMatchedList(GoodsSpecStatisticsRequest request);

    void flushGoodsSpecificationId(FlushGoodsSpecIdRequest request);

    Page<FlowDailyStatisticsBO> page(QueryBalanceStatisticsPageRequest request);

    List<FlowBalanceStatisticsDO> getGbQuantityByEidAndDateTime(Long eid, Date startDateTime,Date endDateTime);

    void statisticsFlowBalance(Date startTime, Date endTime, ErpClientDataDTO erpClientDO);

}
