package com.yiling.dataflow.flowcollect.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsDetailBO;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartSummaryStatisticsDetailRequest;
import com.yiling.dataflow.flowcollect.entity.FlowCollectHeartSummaryStatisticsDetailDO;
import com.yiling.dataflow.flowcollect.dao.FlowCollectHeartSummaryStatisticsDetailMapper;
import com.yiling.dataflow.flowcollect.service.FlowCollectHeartSummaryStatisticsDetailService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 日流向心跳统计明细表 服务实现类
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
@Service
public class FlowCollectHeartSummaryStatisticsDetailServiceImpl extends BaseServiceImpl<FlowCollectHeartSummaryStatisticsDetailMapper, FlowCollectHeartSummaryStatisticsDetailDO> implements FlowCollectHeartSummaryStatisticsDetailService {
    @Override
    public List<FlowDayHeartStatisticsDetailBO> listDetailsByFchsIds(List<Long> flowIds) {
        return this.baseMapper.listDetailsByFchsIds(flowIds);
    }

    @Override
    public boolean deleteDetailByFchIds(List<Long> fchIds) {
        if(CollUtil.isEmpty(fchIds)){
            return false;
        }
        return this.baseMapper.deleteDetailByFchIds(fchIds);
    }

    @Override
    public boolean saveBatchDetail(List<SaveFlowCollectHeartSummaryStatisticsDetailRequest> requests) {
        List<FlowCollectHeartSummaryStatisticsDetailDO> flowCollectHeartSummaryStatisticsDetailDOS= PojoUtils.map(requests, FlowCollectHeartSummaryStatisticsDetailDO.class);
        flowCollectHeartSummaryStatisticsDetailDOS.forEach(e -> {
            e.setCreateTime(new Date());
            e.setCreateUser(0L);
            e.setUpdateTime(new Date());
            e.setUpdateUser(0L);
            e.setRemark("");
            e.setDelFlag(0);
        });
        this.baseMapper.insertBatchSomeColumn(flowCollectHeartSummaryStatisticsDetailDOS);
        return true;
    }
}
