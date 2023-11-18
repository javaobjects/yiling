package com.yiling.dataflow.flowcollect.service.impl;

import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsBO;
import com.yiling.dataflow.flowcollect.dto.request.QueryDayCollectStatisticsRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartSummaryStatisticsRequest;
import com.yiling.dataflow.flowcollect.entity.FlowCollectHeartStatisticsDO;
import com.yiling.dataflow.flowcollect.entity.FlowCollectHeartSummaryStatisticsDO;
import com.yiling.dataflow.flowcollect.dao.FlowCollectHeartSummaryStatisticsMapper;
import com.yiling.dataflow.flowcollect.service.FlowCollectHeartSummaryStatisticsService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 日流向心跳统计汇总表 服务实现类
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
@Service
public class FlowCollectHeartSummaryStatisticsServiceImpl extends BaseServiceImpl<FlowCollectHeartSummaryStatisticsMapper, FlowCollectHeartSummaryStatisticsDO> implements FlowCollectHeartSummaryStatisticsService {
    @Override
    public Page<FlowDayHeartStatisticsBO> pageList(QueryDayCollectStatisticsRequest request) {
        return this.baseMapper.pageList(request.getPage(),request);
    }

    @Override
    public List<FlowCollectHeartSummaryStatisticsDO> findListByCrmEnterpriseIds(List<Long> crmEnterpriseIds) {
        if(CollUtil.isEmpty(crmEnterpriseIds)){
            return ListUtil.empty();
        }
        LambdaQueryWrapper<FlowCollectHeartSummaryStatisticsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(FlowCollectHeartSummaryStatisticsDO::getCrmEnterpriseId, crmEnterpriseIds);
        return this.list(wrapper);
    }

    @Override
    public Long create(SaveFlowCollectHeartSummaryStatisticsRequest request) {
        FlowCollectHeartSummaryStatisticsDO flowCollectHeartSummaryStatisticsDO = PojoUtils.map(request, FlowCollectHeartSummaryStatisticsDO.class);
        this.saveOrUpdate(flowCollectHeartSummaryStatisticsDO);
        return flowCollectHeartSummaryStatisticsDO.getId();
    }

    @Override
    public boolean updateBatch(List<SaveFlowCollectHeartSummaryStatisticsRequest> requestList) {
        List<FlowCollectHeartSummaryStatisticsDO> flowCollectHeartSummaryStatisticsDOList = PojoUtils.map(requestList, FlowCollectHeartSummaryStatisticsDO.class);
        return this.updateBatchById(flowCollectHeartSummaryStatisticsDOList);
    }

    @Override
    public boolean deleteByIds(List<Long> ids) {
        if(CollUtil.isEmpty(ids)){
            return false;
        }
        return this.baseMapper.deleteByIds(ids);
    }

    @Override
    public List<Long> findCrmList() {
        return this.baseMapper.findCrmList();
    }
}
