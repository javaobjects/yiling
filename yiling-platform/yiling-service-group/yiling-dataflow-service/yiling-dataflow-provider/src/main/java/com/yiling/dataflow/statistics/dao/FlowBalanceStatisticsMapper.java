package com.yiling.dataflow.statistics.dao;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.statistics.bo.EnterpriseMonthQuantityBO;
import com.yiling.dataflow.statistics.bo.FlowDailyStatisticsBO;
import com.yiling.dataflow.statistics.bo.GoodsSpecInfoBO;
import com.yiling.dataflow.statistics.bo.GoodsSpecNoMatchedBO;
import com.yiling.dataflow.statistics.bo.GoodsSpecStatisticsBO;
import com.yiling.dataflow.statistics.dto.request.GoodsSpecStatisticsRequest;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsMonthRequest;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsPageRequest;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsRequest;
import com.yiling.dataflow.statistics.entity.FlowBalanceStatisticsDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 商业公司每天平衡表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-07-11
 */
@Repository
public interface FlowBalanceStatisticsMapper extends BaseMapper<FlowBalanceStatisticsDO> {

    Integer deleteFlowBalanceStatistics(@Param("startTime") Date startTime,@Param("endTime") Date endTime,@Param("eidList") List<Long> eidList);

    List<FlowBalanceStatisticsDO> getEnterpriseList();


    Page<FlowBalanceStatisticsDO> getMonthPageList(Page<FlowBalanceStatisticsDO> page, @Param("request") QueryBalanceStatisticsMonthRequest request);

    List<EnterpriseMonthQuantityBO> getEnterpriseBeginMonthList(@Param("beginMonth") String beginMonth, @Param("eidList") List<Long> eidList);

    List<FlowBalanceStatisticsDO> getFlowBalanceStatisticsList(@Param("request") QueryBalanceStatisticsRequest request);

    Page<FlowDailyStatisticsBO> page(Page<FlowBalanceStatisticsDO> page, @Param("request") QueryBalanceStatisticsPageRequest request);

    Page<GoodsSpecStatisticsBO> getGoodsSpecStatisticsListPage(Page<GoodsSpecStatisticsBO> page, @Param("request") GoodsSpecStatisticsRequest request);

    List<GoodsSpecInfoBO> getGoodsSpecInfoList(@Param("eid") Long eid, @Param("monthTime") String monthTime);

    List<EnterpriseMonthQuantityBO> getEnterpriseCurrentMonthList(@Param("time") String time, @Param("eidList") List<Long> eidList);

    List<FlowBalanceStatisticsDO> getEnterpriseMonthFlowList(@Param("monthTime") String monthTime, @Param("eidList") List<Long> eidList);

    List<GoodsSpecNoMatchedBO> goodsSpecNoMatchedList(@Param("request") GoodsSpecStatisticsRequest request);

}
