package com.yiling.dataflow.statistics.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.order.entity.FlowGoodsBatchDetailDO;
import com.yiling.dataflow.order.entity.FlowPurchaseDO;
import com.yiling.dataflow.order.entity.FlowSaleDO;
import com.yiling.dataflow.order.service.FlowGoodsBatchDetailService;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.dataflow.statistics.dto.DeteleFlowBalanceStatisticeRequest;
import com.yiling.dataflow.statistics.dto.ErpClientDataDTO;
import com.yiling.dataflow.statistics.dto.request.StatisticsFlowBalanceRequest;
import com.yiling.dataflow.statistics.entity.FlowBalanceStatisticsDO;
import com.yiling.dataflow.statistics.service.FlowBalanceStatisticsJobService;
import com.yiling.dataflow.statistics.service.FlowBalanceStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: shuang.zhang
 * @date: 2022/7/11
 */
@Slf4j
@Service
public class FlowBalanceStatisticsJobServiceImpl implements FlowBalanceStatisticsJobService {

    private static Integer day = -30;

    @Autowired
    private FlowBalanceStatisticsService flowBalanceStatisticsService;


    @Override
    public void statisticsFlowBalanceJob(StatisticsFlowBalanceRequest request, List<ErpClientDataDTO> erpClientDataDTOList) {
        //添加erpClient流向任务
        try {
            Date statisticsDate = new Date();
            Date startTime = DateUtil.beginOfDay(DateUtil.offsetDay(statisticsDate, day));
            Date endTime = DateUtil.endOfDay(statisticsDate);
            if (request != null && StrUtil.isNotEmpty(request.getMonthTime())) {
                startTime = DateUtil.parseDate(request.getMonthTime() + "-01 00:00:00");
                endTime = DateUtil.endOfMonth(startTime);
            }

            for (ErpClientDataDTO erpClientDO : erpClientDataDTOList) {
                log.info("商业公司eid={},统计日期startDate={},endDate={}", erpClientDO.getRkSuId(), startTime, endTime);
                flowBalanceStatisticsService.statisticsFlowBalance(startTime, endTime, erpClientDO);
            }
            log.info("流向平衡数据统计完成");
        } catch (Exception e) {
            log.error("流向平衡数据统计异常", e);
        }
    }

}
