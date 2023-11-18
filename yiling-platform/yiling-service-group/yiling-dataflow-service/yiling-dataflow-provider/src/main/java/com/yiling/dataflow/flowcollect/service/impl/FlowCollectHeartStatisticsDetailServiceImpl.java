package com.yiling.dataflow.flowcollect.service.impl;

import java.util.Date;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsDetailBO;
import com.yiling.dataflow.flowcollect.dao.FlowCollectHeartStatisticsDetailMapper;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartStatisticsDetailRequest;
import com.yiling.dataflow.flowcollect.entity.FlowCollectHeartStatisticsDetailDO;
import com.yiling.dataflow.flowcollect.service.FlowCollectHeartStatisticsDetailService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 日流向心跳统计明细表 服务实现类
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
@Service
public class FlowCollectHeartStatisticsDetailServiceImpl extends BaseServiceImpl<FlowCollectHeartStatisticsDetailMapper, FlowCollectHeartStatisticsDetailDO> implements FlowCollectHeartStatisticsDetailService {


    @Override
    public List<FlowDayHeartStatisticsDetailBO> listDetailsByFchsIds(List<Long> flowIds) {
        return this.baseMapper.listDetailsByFchsIds(flowIds);
    }

    @Override
    public boolean deleteDetailByFchIds(List<Long> fchIds) {
        if (CollUtil.isEmpty(fchIds)) {
            return false;
        }
        return this.baseMapper.deleteDetailByFchIds(fchIds);
    }

    @Override
    public boolean saveBatchDetail(List<SaveFlowCollectHeartStatisticsDetailRequest> requests) {
        List<FlowCollectHeartStatisticsDetailDO> flowCollectHeartStatisticsDetailDOS = PojoUtils.map(requests, FlowCollectHeartStatisticsDetailDO.class);
        flowCollectHeartStatisticsDetailDOS.forEach(e -> {
            e.setCreateTime(new Date());
            e.setCreateUser(0L);
            e.setUpdateTime(new Date());
            e.setUpdateUser(0L);
            e.setRemark("");
            e.setDelFlag(0);
        });
        this.baseMapper.insertBatchSomeColumn(flowCollectHeartStatisticsDetailDOS);
        return true;
    }
}
