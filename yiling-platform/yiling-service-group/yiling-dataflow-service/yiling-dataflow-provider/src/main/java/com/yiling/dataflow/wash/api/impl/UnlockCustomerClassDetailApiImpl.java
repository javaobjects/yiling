package com.yiling.dataflow.wash.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.UnlockCustomerClassDetailApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.UnlockCustomerClassDetailDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerClassDetailCountRequest;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerClassDetailPageRequest;
import com.yiling.dataflow.wash.dto.request.UpdateCustomerClassificationRequest;
import com.yiling.dataflow.wash.dto.request.UpdateUnlockFlowWashSaleRequest;
import com.yiling.dataflow.wash.entity.UnlockCustomerClassDetailDO;
import com.yiling.dataflow.wash.enums.WashErrorEnum;
import com.yiling.dataflow.wash.service.FlowMonthWashControlService;
import com.yiling.dataflow.wash.service.UnlockCustomerClassDetailService;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/5/5
 */
@Slf4j
@DubboService
public class UnlockCustomerClassDetailApiImpl implements UnlockCustomerClassDetailApi {

    @Autowired
    private UnlockCustomerClassDetailService unlockCustomerClassDetailService;

    @Autowired
    private FlowMonthWashControlService flowMonthWashControlService;

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;

    @Override
    public Page<UnlockCustomerClassDetailDTO> listPage(QueryUnlockCustomerClassDetailPageRequest request) {
        return PojoUtils.map(unlockCustomerClassDetailService.listPage(request), UnlockCustomerClassDetailDTO.class);
    }

    @Override
    public void resetCustomerClassification(UpdateCustomerClassificationRequest request) {
        List<Long> idList = request.getIdList();
        if (CollUtil.isEmpty(idList)) {
            throw new BusinessException(ResultCode.PARAM_MISS, "至少选择一条数据");
        }

        //  更新非锁销量分配结果，获取日程年月
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlService.getCurrentFlowMonthWashControl();
        if (flowMonthWashControlDTO == null) {
            throw new BusinessException(ResultCode.FAILED);
        }

        for (Long id : idList) {
            UnlockCustomerClassDetailDO unlockCustomerClassDetailDO = unlockCustomerClassDetailService.getById(id);
            unlockCustomerClassDetailDO.setId(id);
            unlockCustomerClassDetailDO.setClassFlag(1);
            unlockCustomerClassDetailDO.setCustomerClassification(request.getCustomerClassification());
            unlockCustomerClassDetailDO.setRemark(request.getRemark());
            unlockCustomerClassDetailDO.setClassGround(2);      // 人工
            unlockCustomerClassDetailDO.setLastOpTime(request.getOpTime());
            unlockCustomerClassDetailDO.setLastOpUser(request.getOpUserId());
            unlockCustomerClassDetailService.updateById(unlockCustomerClassDetailDO);

            UpdateUnlockFlowWashSaleRequest mqRequest = new UpdateUnlockFlowWashSaleRequest();
            mqRequest.setYear(flowMonthWashControlDTO.getYear());
            mqRequest.setMonth(flowMonthWashControlDTO.getMonth());
            mqRequest.setCrmId(unlockCustomerClassDetailDO.getCrmEnterpriseId());
            mqRequest.setCustomerName(unlockCustomerClassDetailDO.getCustomerName());
            mqRequest.setCustomerClassification(request.getCustomerClassification());

            SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_UNLOCK_FLOW_WASH_SALE_CLASSIFICATION, Constants.TAG_UNLOCK_FLOW_WASH_SALE_CLASSIFICATION, DateUtil.formatDate(new Date()), JSONUtil.toJsonStr(mqRequest));
            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.error("非锁客户明细手动设置分类id：" + id + "， 同步非锁销量失败！");
            }
        }

    }

    @Override
    public Integer count(QueryUnlockCustomerClassDetailCountRequest request) {
        return unlockCustomerClassDetailService.countByRequest(request);
    }
}
