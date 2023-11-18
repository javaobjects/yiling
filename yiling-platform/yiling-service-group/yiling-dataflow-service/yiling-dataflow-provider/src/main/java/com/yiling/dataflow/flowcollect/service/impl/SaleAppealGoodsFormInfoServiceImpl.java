package com.yiling.dataflow.flowcollect.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.api.SalesAppealConfirmApi;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.entity.FlowFleeingGoodsDO;
import com.yiling.dataflow.flowcollect.entity.SaleAppealGoodsFormInfoDO;
import com.yiling.dataflow.flowcollect.dao.SaleAppealGoodsFormInfoMapper;
import com.yiling.dataflow.flowcollect.service.SaleAppealGoodsFormInfoService;
import com.yiling.framework.common.base.BaseServiceImpl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 申诉确认数据单 服务实现类
 * </p>
 *
 * @author shixing.sun
 * @date 2023-03-14
 */
@Service
public class SaleAppealGoodsFormInfoServiceImpl extends BaseServiceImpl<SaleAppealGoodsFormInfoMapper, SaleAppealGoodsFormInfoDO> implements SaleAppealGoodsFormInfoService {


    @Override
    public boolean updateTaskIdByRecordId(Long recordId, Long taskId, Long opUserId) {
        QueryWrapper<SaleAppealGoodsFormInfoDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SaleAppealGoodsFormInfoDO::getRecordId, recordId);

        SaleAppealGoodsFormInfoDO flowFleeingGoodsDO = new SaleAppealGoodsFormInfoDO();
        flowFleeingGoodsDO.setTaskId(taskId);
        // 传输类型：1、上传excel; 2、选择流向
        flowFleeingGoodsDO.setTransferType(1);
       /* flowFleeingGoodsDO.setOpUserId(opUserId);
        flowFleeingGoodsDO.setUpdateUser(opUserId);*/
        return this.update(flowFleeingGoodsDO, wrapper);
    }

    @Override
    public Page<SaleAppealGoodsFormInfoDO> pageList(QueryFlowMonthPageRequest request) {
        LambdaQueryWrapper<SaleAppealGoodsFormInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SaleAppealGoodsFormInfoDO::getTaskId, request.getTaskId());
        return this.page(request.getPage(), wrapper);
    }

}
