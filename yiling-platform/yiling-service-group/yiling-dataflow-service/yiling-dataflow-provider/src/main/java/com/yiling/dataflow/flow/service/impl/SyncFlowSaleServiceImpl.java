package com.yiling.dataflow.flow.service.impl;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.flow.dao.SyncFlowSaleMapper;
import com.yiling.dataflow.flow.entity.SyncFlowSaleDO;
import com.yiling.dataflow.flow.service.FlowSaleSummaryService;
import com.yiling.dataflow.flow.service.SyncFlowSaleService;
import com.yiling.dataflow.order.enums.DataFlowErrorCode;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-12-21
 */
@Service
public class SyncFlowSaleServiceImpl extends BaseServiceImpl<SyncFlowSaleMapper, SyncFlowSaleDO> implements SyncFlowSaleService {

    @Autowired
    private FlowSaleSummaryService flowSaleSummaryService;

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;

    @Override
    public void insertList(List<Long> ids) {
        List<SyncFlowSaleDO> syncFlowSaleDOList = new ArrayList<>();
        for (Long id : ids) {
            SyncFlowSaleDO syncFlowSaleDO = new SyncFlowSaleDO();
            syncFlowSaleDO.setFlowSaleId(id);
            syncFlowSaleDOList.add(syncFlowSaleDO);
        }
        this.saveBatch(syncFlowSaleDOList);
    }

    @Override
    public void syncFlowSaleSummary() {
        QueryWrapper<SyncFlowSaleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().last("limit 1000");
        List<SyncFlowSaleDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<Long> ids = list.stream().map(e -> e.getFlowSaleId()).collect(Collectors.toList());
        SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_FLOW_SALE_SUMMARY_SYNC, Constants.TAG_FLOW_SALE_SUMMARY_SYNC, DateUtil.formatDate(new Date()), JSON.toJSONString(ids));
        if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
            throw new BusinessException(DataFlowErrorCode.SALE_FLOW_SYNC_ERROR);
        }
//        this.baseMapper.deleteSyncFlowSaleByIds(list.stream().map(e -> e.getId()).collect(Collectors.toList()));
    }
}
