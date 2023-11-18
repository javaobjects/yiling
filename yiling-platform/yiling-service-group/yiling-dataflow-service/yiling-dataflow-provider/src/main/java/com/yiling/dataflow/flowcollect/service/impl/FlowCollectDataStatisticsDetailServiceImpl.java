package com.yiling.dataflow.flowcollect.service.impl;

import java.util.Date;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsDetailBO;
import com.yiling.dataflow.flowcollect.dao.FlowCollectDataStatisticsDetailMapper;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectDataStatisticsDetailRequest;
import com.yiling.dataflow.flowcollect.entity.FlowCollectDataStatisticsDetailDO;
import com.yiling.dataflow.flowcollect.service.FlowCollectDataStatisticsDetailService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 日流向数据统计明细表 服务实现类
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
@Service
public class FlowCollectDataStatisticsDetailServiceImpl extends BaseServiceImpl<FlowCollectDataStatisticsDetailMapper, FlowCollectDataStatisticsDetailDO> implements FlowCollectDataStatisticsDetailService {
    @Override
    public List<FlowDayHeartStatisticsDetailBO> listDetailsByFchsIds(List<Long> flowIds) {
        return this.baseMapper.listDetailsByFchsIds(flowIds);
    }

    @Override
    public boolean deleteDetailByFchIds(List<Long> fcdIds) {
        if (CollUtil.isEmpty(fcdIds)) {
            return false;
        }
        return this.baseMapper.deleteDetailByFchIds(fcdIds);
    }

    @Override
    public boolean saveBatchDetail(List<SaveFlowCollectDataStatisticsDetailRequest> requests) {
        List<FlowCollectDataStatisticsDetailDO> flowCollectHeartStatisticsDetailDOS = PojoUtils.map(requests, FlowCollectDataStatisticsDetailDO.class);
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
