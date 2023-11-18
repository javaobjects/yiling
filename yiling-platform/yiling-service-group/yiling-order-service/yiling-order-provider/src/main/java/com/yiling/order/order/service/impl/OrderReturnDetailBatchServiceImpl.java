package com.yiling.order.order.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.order.order.dao.OrderReturnDetailBatchMapper;
import com.yiling.order.order.dto.ReturnDetailBathDTO;
import com.yiling.order.order.dto.ReturnOrderExportDTO;
import com.yiling.order.order.dto.request.QueryOrderReturnInfoRequest;
import com.yiling.order.order.entity.OrderReturnDO;
import com.yiling.order.order.entity.OrderReturnDetailBatchDO;
import com.yiling.order.order.service.OrderReturnDetailBatchService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 退货单明细批次信息 服务实现类
 * </p>
 *
 * @author zhangy
 * @date 2021-08-06
 */
@Slf4j
@Service
public class OrderReturnDetailBatchServiceImpl extends BaseServiceImpl<OrderReturnDetailBatchMapper, OrderReturnDetailBatchDO> implements OrderReturnDetailBatchService {

    @Override
    public Page<ReturnOrderExportDTO> queryExportByCondition(QueryOrderReturnInfoRequest request) {
        log.info("导出退货单，请求数据为:[{}]", request);
        Page<OrderReturnDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        return this.getBaseMapper().queryExportByCondition(objectPage, request);
    }

    @Override
    public List<OrderReturnDetailBatchDO> queryByDetailIdAndBatchNoAndType(Long detailId, String batchNo, Integer returnType) {
        return this.getBaseMapper().queryByDetailIdAndBatchNoAndType(detailId, batchNo, returnType);
    }

    @Override
    public List<OrderReturnDetailBatchDO> getByDetailIdAndBatchNo(Long detailId, String batchNo) {
        QueryWrapper<OrderReturnDetailBatchDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderReturnDetailBatchDO::getDetailId, detailId);
        if (StringUtils.isNotEmpty(batchNo)) {
            wrapper.lambda().eq(OrderReturnDetailBatchDO::getBatchNo, batchNo);
        }
        return this.list(wrapper);
    }

    @Override
    public List<OrderReturnDetailBatchDO> getOrderReturnDetailBatch(Long returnId, Long detailId, String batchNo) {
        QueryWrapper<OrderReturnDetailBatchDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderReturnDetailBatchDO::getReturnId, returnId);
        if (null != detailId) {
            wrapper.lambda().eq(OrderReturnDetailBatchDO::getDetailId, detailId);
        }
        if (StringUtils.isNotEmpty(batchNo)) {
            wrapper.lambda().eq(OrderReturnDetailBatchDO::getBatchNo, batchNo);
        }
        return this.list(wrapper);
    }

    @Override
    public List<OrderReturnDetailBatchDO> getOrderReturnDetailBatchByReturnIds(List<Long> returnIds) {
        QueryWrapper<OrderReturnDetailBatchDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderReturnDetailBatchDO::getReturnId, returnIds);
        wrapper.lambda().orderByDesc(OrderReturnDetailBatchDO::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public OrderReturnDetailBatchDO queryByReturnIdAndDetailIdAndBathNo(Long returnId, Long detailId, String batchNo) {
        QueryWrapper<OrderReturnDetailBatchDO> orderReturnDetailBatchDOQueryWrapper = new QueryWrapper<>();
        orderReturnDetailBatchDOQueryWrapper.lambda().eq(OrderReturnDetailBatchDO::getReturnId, returnId);
        orderReturnDetailBatchDOQueryWrapper.lambda().eq(OrderReturnDetailBatchDO::getDetailId, detailId);
        orderReturnDetailBatchDOQueryWrapper.lambda().eq(OrderReturnDetailBatchDO::getBatchNo, batchNo);
        orderReturnDetailBatchDOQueryWrapper.lambda().eq(OrderReturnDetailBatchDO::getDelFlag, 0);
        orderReturnDetailBatchDOQueryWrapper.last("LIMIT 1");
        return this.getOne(orderReturnDetailBatchDOQueryWrapper);
    }

    @Override
    public List<ReturnDetailBathDTO> queryByDetailId(List<Long> detailIdList) {
        if (CollectionUtil.isEmpty(detailIdList)) {
            return ListUtil.empty();
        }
        return this.getBaseMapper().queryByDetailId(detailIdList);
    }
}
