package com.yiling.dataflow.flowcollect.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseByNamePageListRequest;
import com.yiling.dataflow.crm.bo.CrmEnterprisePartBO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsBO;
import com.yiling.dataflow.flowcollect.dao.FlowCollectDataStatisticsMapper;
import com.yiling.dataflow.flowcollect.dto.FlowCollectDataStatisticsDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryDayCollectStatisticsRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectDataStatisticsRequest;
import com.yiling.dataflow.flowcollect.entity.FlowCollectDataStatisticsDO;
import com.yiling.dataflow.flowcollect.service.FlowCollectDataStatisticsService;
import com.yiling.dataflow.order.bo.FlowStatisticsBO;
import com.yiling.dataflow.order.dto.request.QueryFlowStatisticesRequest;
import com.yiling.dataflow.order.service.FlowGoodsBatchDetailService;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 日流向数据统计表 服务实现类
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
@Service
public class FlowCollectDataStatisticsServiceImpl extends BaseServiceImpl<FlowCollectDataStatisticsMapper, FlowCollectDataStatisticsDO> implements FlowCollectDataStatisticsService {

    @Autowired
    private CrmEnterpriseService crmEnterpriseService;
    @Autowired
    private FlowSaleService flowSaleService;
    @Autowired
    private FlowPurchaseService flowPurchaseService;
    @Autowired
    private FlowGoodsBatchDetailService flowGoodsBatchDetailService;
    @Override
    public Page<FlowDayHeartStatisticsBO> pageList(QueryDayCollectStatisticsRequest request) {
        return this.baseMapper.pageList(request.getPage(), request);
    }

    @Override
    public List<FlowCollectDataStatisticsDO> findListByCrmEnterpriseIds(List<Long> ids) {
        if(CollUtil.isEmpty(ids)){
            return ListUtil.empty();
        }
        LambdaQueryWrapper<FlowCollectDataStatisticsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(FlowCollectDataStatisticsDO::getCrmEnterpriseId, ids);
        return this.list(wrapper);
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

    @Override
    public Long create(SaveFlowCollectDataStatisticsRequest request) {
        FlowCollectDataStatisticsDO flowCollectDataStatisticsDO = PojoUtils.map(request, FlowCollectDataStatisticsDO.class);
        this.saveOrUpdate(flowCollectDataStatisticsDO);
        return flowCollectDataStatisticsDO.getId();
    }

    @Override
    public boolean updateBatch(List<SaveFlowCollectDataStatisticsRequest> requestList) {
        List<FlowCollectDataStatisticsDO> flowCollectDataStatisticsDOList = PojoUtils.map(requestList, FlowCollectDataStatisticsDO.class);
        return this.updateBatchById(flowCollectDataStatisticsDOList);
    }

}
