package com.yiling.sjms.wash.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.agency.enums.AgencySupplyChainRoleEnum;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseBackUpPageRequest;
import com.yiling.dataflow.gb.api.GbAppealFormApi;
import com.yiling.dataflow.gb.api.GbOrderApi;
import com.yiling.dataflow.gb.dto.GbAppealFormDTO;
import com.yiling.dataflow.gb.dto.GbOrderDTO;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormListPageRequest;
import com.yiling.dataflow.gb.dto.request.QueryGbOrderPageRequest;
import com.yiling.dataflow.report.api.FlowWashSaleReportApi;
import com.yiling.dataflow.report.dto.request.QueryFlowWashSaleReportRequest;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.api.UnlockFlowWashTaskApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.UnlockFlowWashTaskDTO;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockCustomerWashTaskRequest;
import com.yiling.dataflow.wash.dto.request.UpdateStageRequest;
import com.yiling.dataflow.wash.enums.WashErrorEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.sjms.wash.service.SjmsFlowMonthWashControlService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;

/**
 * @author: shuang.zhang
 * @date: 2023/5/5
 */
@Service
public class SjmsFlowMonthWashControlServiceImpl implements SjmsFlowMonthWashControlService {

    @DubboReference
    private FlowMonthWashControlApi flowMonthWashControlApi;
    @DubboReference(timeout = 1000*60)
    private CrmEnterpriseApi        crmEnterpriseApi;
    @DubboReference
    private UnlockFlowWashTaskApi   unlockFlowWashTaskApi;
    @DubboReference
    private GbOrderApi              gbOrderApi;
    @DubboReference
    private GbAppealFormApi         gbAppealFormApi;
    @DubboReference(timeout = 1000*60)
    private FlowWashSaleReportApi   flowWashSaleReportApi;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;
    @Autowired
    @Lazy
    SjmsFlowMonthWashControlServiceImpl _this;

    @Override
    public boolean gbLockStatus(UpdateStageRequest request) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getById(request.getId());
        List<UnlockFlowWashTaskDTO> unlockFlowWashTaskDTOList = unlockFlowWashTaskApi.getListByFmwcId(request.getId());
        QueryFlowWashSaleReportRequest reportRequest = new QueryFlowWashSaleReportRequest();
        reportRequest.setIsLockFlag(2);
        reportRequest.setYear(String.valueOf(flowMonthWashControlDTO.getYear()));
        reportRequest.setMonth(String.valueOf(flowMonthWashControlDTO.getMonth()));

        List<Long> crmList = flowWashSaleReportApi.listCrmIdByCondition(reportRequest);
        List<CrmEnterpriseDTO> crmEnterpriseDTOList = crmEnterpriseApi.getCrmEnterpriseListById(crmList);
        Map<Long, CrmEnterpriseDTO> crmEnterpriseDTOMap = crmEnterpriseDTOList.stream().collect(Collectors.toMap(CrmEnterpriseDTO::getId, Function.identity()));
        List<Long> hasCrmList = unlockFlowWashTaskDTOList.stream().map(e -> e.getCrmEnterpriseId()).collect(Collectors.toList());

        List<SaveOrUpdateUnlockCustomerWashTaskRequest> list = new ArrayList<>();
        for (Long crmId : crmList) {
            if (!hasCrmList.contains(crmId)) {
                SaveOrUpdateUnlockCustomerWashTaskRequest saveOrUpdateUnlockCustomerWashTaskRequest = new SaveOrUpdateUnlockCustomerWashTaskRequest();
                saveOrUpdateUnlockCustomerWashTaskRequest.setClassificationStatus(1);
                saveOrUpdateUnlockCustomerWashTaskRequest.setRuleStatus(1);
                saveOrUpdateUnlockCustomerWashTaskRequest.setFmwcId(request.getId());
                saveOrUpdateUnlockCustomerWashTaskRequest.setCrmEnterpriseId(crmId);
                saveOrUpdateUnlockCustomerWashTaskRequest.setYear(flowMonthWashControlDTO.getYear());
                saveOrUpdateUnlockCustomerWashTaskRequest.setMonth(flowMonthWashControlDTO.getMonth());
                saveOrUpdateUnlockCustomerWashTaskRequest.setName(crmEnterpriseDTOMap.get(crmId).getName());
                list.add(saveOrUpdateUnlockCustomerWashTaskRequest);
            }
        }
        if (CollUtil.isNotEmpty(list)) {
            unlockFlowWashTaskApi.saveBatch(list);
        }

        unlockFlowWashTaskDTOList = unlockFlowWashTaskApi.getListByFmwcId(request.getId());
        for (UnlockFlowWashTaskDTO unlockFlowWashTaskDTO : unlockFlowWashTaskDTOList) {
            this.sendMq(Constants.TOPIC_UNLOCK_CUSTOMER_CLASSIFICATION_TASK, Constants.TAG_UNLOCK_CUSTOMER_CLASSIFICATION_TASK, String.valueOf(unlockFlowWashTaskDTO.getId()));
        }
        unlockFlowWashTaskDTOList.clear();
        /**
         * 团购数据先匹配原流向
         */
        List<GbOrderDTO> gbOrderDTOList = pageGbOrder();
        for (GbOrderDTO gbOrderDTO : gbOrderDTOList) {
            this.sendMq(Constants.TOPIC_FLOW_SALE_GB_MATE_TASK, Constants.TAG_FLOW_SALE_GB_MATE_TASK, String.valueOf(gbOrderDTO.getId()));
        }
        return true;
    }

    @Override
    public boolean unlockStatus(UpdateStageRequest updateStageRequest) {
        List<UnlockFlowWashTaskDTO> unlockFlowWashTaskDTOList = unlockFlowWashTaskApi.getListByFmwcId(updateStageRequest.getId());
        for (UnlockFlowWashTaskDTO unlockFlowWashTaskDTO : unlockFlowWashTaskDTOList) {
            SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_UNLOCK_FLOW_SALE_MATCH_TASK, Constants.TAG_UNLOCK_FLOW_SALE_MATCH_TASK, DateUtil.formatDate(new Date()), String.valueOf(unlockFlowWashTaskDTO.getId()));
            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                throw new BusinessException(WashErrorEnum.FLOW_WASH_ERROR);
            }
        }
        return true;
    }

    @Override
    public boolean gbUnlockStatus(UpdateStageRequest updateStageRequest) {
        List<GbAppealFormDTO> gbAppealFormDTOS = pageGbAppealForm();
        for (GbAppealFormDTO gbAppealFormDTO : gbAppealFormDTOS) {
            SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_FLOW_SALE_GB_SUBSTRACT_TASK, Constants.TAG_FLOW_SALE_GB_SUBSTRACT_TASK, DateUtil.formatDate(new Date()), String.valueOf(gbAppealFormDTO.getId()));
            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                throw new BusinessException(WashErrorEnum.FLOW_WASH_ERROR);
            }
        }
        return true;
    }

    public List<GbOrderDTO> pageGbOrder() {
        List<GbOrderDTO> gbOrderDTOList = new ArrayList<>();
        QueryGbOrderPageRequest request = new QueryGbOrderPageRequest();
        request.setExecStatus(1);
        Page<GbOrderDTO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            page = gbOrderApi.getGbOrderPage(request);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            for (GbOrderDTO gbOrderDTO : page.getRecords()) {
                gbOrderDTOList.add(gbOrderDTO);
            }
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return gbOrderDTOList;
    }

    public List<GbAppealFormDTO> pageGbAppealForm() {
        List<GbAppealFormDTO> gbOrderDTOList = new ArrayList<>();
        QueryGbAppealFormListPageRequest request = new QueryGbAppealFormListPageRequest();
        request.setGbLockType(2);
        request.setDataExecStatus(1);
        Page<GbAppealFormDTO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            page = gbAppealFormApi.listPage(request);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            for (GbAppealFormDTO gbAppealFormDTO : page.getRecords()) {
                gbOrderDTOList.add(gbAppealFormDTO);
            }
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return gbOrderDTOList;
    }

    /**
     * 查询需要打取流向的商业公司
     *
     * @param tableSuffix
     * @return
     */
    public List<CrmEnterpriseDTO> pageCrmEnterprise(String tableSuffix) {
        //查询所有erp对接的企业信息
        List<CrmEnterpriseDTO> suIdList = new ArrayList<>();
        QueryCrmEnterpriseBackUpPageRequest request = new QueryCrmEnterpriseBackUpPageRequest();
        request.setSupplyChainRole(AgencySupplyChainRoleEnum.SUPPLIER.getCode());
        request.setSoMonth(tableSuffix);
        request.setBusinessCode(1);
        //需要循环调用
        Page<CrmEnterpriseDTO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            page = crmEnterpriseApi.pageListSuffixBackUpInfo(request);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            for (CrmEnterpriseDTO crmEnterpriseDTO : page.getRecords()) {
                suIdList.add(crmEnterpriseDTO);
            }
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return suIdList;
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public boolean sendMq(String topic,String topicTag,String msg) {
        MqMessageBO mqMessageBO = _this.sendPrepare(topic,topicTag,msg);
        mqMessageSendApi.send(mqMessageBO);
        return true;
    }

    /**
     * 消息持久化
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public MqMessageBO sendPrepare(String topic,String topicTag,String msg) {
        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        return mqMessageBO;
    }

}
