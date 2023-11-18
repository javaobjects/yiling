package com.yiling.dataflow.wash.listener;

import cn.hutool.core.date.DateUtil;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailExistRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseExistRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSaleExistRequest;
import com.yiling.dataflow.order.service.FlowGoodsBatchDetailService;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.dataflow.utils.BackupUtil;
import com.yiling.dataflow.wash.dto.request.SaveFlowMonthWashTaskRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateErpClientWashPlanRequest;
import com.yiling.dataflow.wash.entity.ErpClientWashPlanDO;
import com.yiling.dataflow.wash.entity.FlowMonthWashControlDO;
import com.yiling.dataflow.wash.enums.CollectionMethodEnum;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.dataflow.wash.enums.FlowTypeEnum;
import com.yiling.dataflow.wash.service.ErpClientWashPlanService;
import com.yiling.dataflow.wash.service.FlowMonthWashControlService;
import com.yiling.dataflow.wash.service.FlowMonthWashTaskService;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 消息消费监听
 *
 * @author shuan
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_LASTEST_FLOW_DATE_NOTIFY, consumerGroup = Constants.TAG_LASTEST_FLOW_DATE_NOTIFY,maxThread = 3)
public class FlowMonthWashControlListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @Autowired(required = false)
    private RocketMqProducerService     rocketMqProducerService;
    @Autowired
    private FlowMonthWashTaskService    flowMonthWashTaskService;
    @Autowired
    private FlowMonthWashControlService flowMonthWashControlService;
    @Autowired
    private CrmEnterpriseService        crmEnterpriseService;
    @Autowired
    private FlowGoodsBatchDetailService flowGoodsBatchDetailService;
    @Autowired
    private FlowPurchaseService         flowPurchaseService;
    @Autowired
    private FlowSaleService             flowSaleService;
    @Autowired
    private ErpClientWashPlanService    erpClientWashPlanService;

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
            Long id = Long.parseLong(msg);
            if (id == null || id == 0) {
                return MqAction.CommitMessage;
            }

            ErpClientWashPlanDO erpClientWashPlanDO = erpClientWashPlanService.getById(id);
            if (erpClientWashPlanDO == null) {
                log.error("查询的消费信息对象为空");
                return MqAction.CommitMessage;
            }
            if (erpClientWashPlanDO.getStatus() != 1) {
                log.error("查询的消费信息状态不对");
                return MqAction.CommitMessage;
            }
            SaveOrUpdateErpClientWashPlanRequest request = new SaveOrUpdateErpClientWashPlanRequest();
            request.setId(id);
            request.setStatus(2);
            erpClientWashPlanService.updateById(request);

            flowGoodsBatchDetailService.executeGoodsBatchStatistics(erpClientWashPlanDO.getEid(), DateUtil.parseDate(DateUtil.today()));
            FlowMonthWashControlDO flowMonthWashControlDO = flowMonthWashControlService.getById(erpClientWashPlanDO.getFmwcId());
            CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getSuffixByCrmEnterpriseId(erpClientWashPlanDO.getCrmEnterpriseId(), BackupUtil.generateTableSuffix(flowMonthWashControlDO.getYear(), flowMonthWashControlDO.getMonth()));
            if (null == crmEnterpriseDO) {
                log.warn("crm企业未查到数据，crmEnterpriseId:{}", erpClientWashPlanDO.getCrmEnterpriseId());
                return MqAction.CommitMessage;
            }
            SaveFlowMonthWashTaskRequest saveFlowMonthWashTaskRequest = new SaveFlowMonthWashTaskRequest();
            saveFlowMonthWashTaskRequest.setFmwcId(erpClientWashPlanDO.getFmwcId());
            saveFlowMonthWashTaskRequest.setCrmEnterpriseId(crmEnterpriseDO.getId());
            saveFlowMonthWashTaskRequest.setEid(erpClientWashPlanDO.getEid());
            saveFlowMonthWashTaskRequest.setCollectionMethod(CollectionMethodEnum.getByCode(erpClientWashPlanDO.getFlowMode()).getCode());
            saveFlowMonthWashTaskRequest.setFlowClassify(FlowClassifyEnum.NORMAL.getCode());


            QueryFlowPurchaseExistRequest queryFlowPurchaseExistRequest = new QueryFlowPurchaseExistRequest();
            queryFlowPurchaseExistRequest.setPoTimeStart(flowMonthWashControlDO.getDataStartTime());
            queryFlowPurchaseExistRequest.setPoTimeEnd(flowMonthWashControlDO.getDataEndTime());
            queryFlowPurchaseExistRequest.setEid(erpClientWashPlanDO.getEid());
            boolean isHaveDataByEidAndPoTime = flowPurchaseService.isHaveDataByEidAndPoTime(queryFlowPurchaseExistRequest);
            if (isHaveDataByEidAndPoTime) {
                saveFlowMonthWashTaskRequest.setFlowType(FlowTypeEnum.PURCHASE.getCode());
                flowMonthWashTaskService.create(saveFlowMonthWashTaskRequest, true);
            }

            QueryFlowSaleExistRequest queryFlowSaleExistRequest = new QueryFlowSaleExistRequest();
            queryFlowSaleExistRequest.setSoTimeStart(flowMonthWashControlDO.getDataStartTime());
            queryFlowSaleExistRequest.setSoTimeEnd(flowMonthWashControlDO.getDataEndTime());
            queryFlowSaleExistRequest.setEid(erpClientWashPlanDO.getEid());
            boolean isHaveDataByEidAndSoTime = flowSaleService.isHaveDataByEidAndSoTime(queryFlowSaleExistRequest);
            if (isHaveDataByEidAndSoTime) {
                saveFlowMonthWashTaskRequest.setFlowType(FlowTypeEnum.SALE.getCode());
                flowMonthWashTaskService.create(saveFlowMonthWashTaskRequest, true);
            }

            QueryFlowGoodsBatchDetailExistRequest queryFlowGoodsBatchDetailExistRequest = new QueryFlowGoodsBatchDetailExistRequest();
            queryFlowGoodsBatchDetailExistRequest.setEid(erpClientWashPlanDO.getEid());
            queryFlowGoodsBatchDetailExistRequest.setGbDetailTime(DateUtil.parseDate(DateUtil.formatDate(flowMonthWashControlDO.getDataEndTime())));
            boolean isHaveDataByEidAndGbDetailTime = flowGoodsBatchDetailService.isHaveDataByEidAndGbDetailTime(queryFlowGoodsBatchDetailExistRequest);
            if (isHaveDataByEidAndGbDetailTime) {
                saveFlowMonthWashTaskRequest.setFlowType(FlowTypeEnum.GOODS_BATCH.getCode());
                flowMonthWashTaskService.create(saveFlowMonthWashTaskRequest, true);
            }
            return MqAction.CommitMessage;
        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected int getMaxReconsumeTimes() {
        return 3;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {
        return (body, message, context, e) -> {
            mqMessageConsumeFailureApi.handleConsumeFailure(body, message, e);
            DubboUtils.quickAsyncCall("mqMessageConsumeFailureApi", "handleConsumeFailure");
        };
    }
}
