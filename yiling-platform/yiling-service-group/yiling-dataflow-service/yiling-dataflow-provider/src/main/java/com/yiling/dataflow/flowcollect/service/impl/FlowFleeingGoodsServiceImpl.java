package com.yiling.dataflow.flowcollect.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.dao.FlowFleeingGoodsMapper;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowFleeingGoodsRequest;
import com.yiling.dataflow.flowcollect.entity.FlowFleeingGoodsDO;
import com.yiling.dataflow.flowcollect.service.FlowFleeingGoodsService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 窜货申报上传数据表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2023-03-16
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FlowFleeingGoodsServiceImpl extends BaseServiceImpl<FlowFleeingGoodsMapper, FlowFleeingGoodsDO> implements FlowFleeingGoodsService {

    @Override
    public boolean saveFlowFleeingGoodsAndTask(List<SaveFlowFleeingGoodsRequest> requestList) {
        List<FlowFleeingGoodsDO> fleeingGoodsDOList = PojoUtils.map(requestList, FlowFleeingGoodsDO.class);
        return this.saveBatch(fleeingGoodsDOList);
    }

    @Override
    public boolean updateTaskIdByRecordId(Long recordId, Long taskId, Long opUserId) {
        QueryWrapper<FlowFleeingGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(FlowFleeingGoodsDO::getRecordId, recordId);

        FlowFleeingGoodsDO flowFleeingGoodsDO = new FlowFleeingGoodsDO();
        flowFleeingGoodsDO.setTaskId(taskId);
        flowFleeingGoodsDO.setOpUserId(opUserId);
        flowFleeingGoodsDO.setUpdateUser(opUserId);
        return this.update(flowFleeingGoodsDO, wrapper);
    }

    @Override
    public Page<FlowFleeingGoodsDO> pageList(QueryFlowMonthPageRequest request) {
        LambdaQueryWrapper<FlowFleeingGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowFleeingGoodsDO::getTaskId, request.getTaskId());
        return this.page(request.getPage(), wrapper);
    }
}
