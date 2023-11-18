package com.yiling.dataflow.flowcollect.service.impl;

import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsBO;
import com.yiling.dataflow.flowcollect.dao.FlowCollectHeartStatisticsMapper;
import com.yiling.dataflow.flowcollect.dto.request.QueryDayCollectStatisticsRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartStatisticsRequest;
import com.yiling.dataflow.flowcollect.entity.FlowCollectHeartStatisticsDO;
import com.yiling.dataflow.flowcollect.service.FlowCollectHeartStatisticsService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 日流向心跳统计表 服务实现类
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
@Service
public class FlowCollectHeartStatisticsServiceImpl extends BaseServiceImpl<FlowCollectHeartStatisticsMapper, FlowCollectHeartStatisticsDO> implements FlowCollectHeartStatisticsService {

    @Override
    public Page<FlowDayHeartStatisticsBO> pageList(QueryDayCollectStatisticsRequest request) {
        return this.baseMapper.pageList(request.getPage(), request);
    }

    @Override
    public List<FlowCollectHeartStatisticsDO> findListByCrmEnterpriseIds(List<Long> crmEnterpriseIds) {
        if(CollUtil.isEmpty(crmEnterpriseIds)){
            return ListUtil.empty();
        }
        LambdaQueryWrapper<FlowCollectHeartStatisticsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(FlowCollectHeartStatisticsDO::getCrmEnterpriseId, crmEnterpriseIds);
        return this.list(wrapper);
    }

    @Override
    public Long create(SaveFlowCollectHeartStatisticsRequest request) {
        FlowCollectHeartStatisticsDO flowCollectHeartStatisticsDO = PojoUtils.map(request, FlowCollectHeartStatisticsDO.class);
        this.saveOrUpdate(flowCollectHeartStatisticsDO);
        return flowCollectHeartStatisticsDO.getId();
    }

    @Override
    public boolean updateBatch(List<SaveFlowCollectHeartStatisticsRequest> requestList) {
        List<FlowCollectHeartStatisticsDO> flowCollectHeartStatisticsDOList = PojoUtils.map(requestList, FlowCollectHeartStatisticsDO.class);
        return this.updateBatchById(flowCollectHeartStatisticsDOList);
    }

    @Override
    public List<Long> findCrmList() {
        return this.baseMapper.findCrmList();
    }

    @Override
    public boolean deleteByIds(List<Long> ids) {
        if(CollUtil.isEmpty(ids)){
            return false;
        }
        return this.baseMapper.deleteByIds(ids);
    }
}
