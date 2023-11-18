package com.yiling.workflow.workflow.mq.listener;

import java.util.Map;
import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.sjms.gb.api.GbWorkflowProcessorApi;
import com.yiling.user.esb.bo.SimpleEsbEmployeeInfoBO;
import com.yiling.user.esb.dto.request.QueryProvinceManagerRequest;
import com.yiling.workflow.workflow.api.TaskApi;
import com.yiling.workflow.workflow.constant.FlowConstant;
import com.yiling.workflow.workflow.dto.request.ResubmitGroupBuyRequest;

import cn.hutool.core.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 团购提报驳回后重新提交
 * @author: gxl
 * @date: 2022/12/8
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_GB_RESUBMIT_SEND_WORKFLOW, consumerGroup = Constants.TOPIC_GB_RESUBMIT_SEND_WORKFLOW,tag = Constants.TAG_GB_RESUBMIT_SEND_WORKFLOW)
public class ResubmitProcessListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @DubboReference
    private GbWorkflowProcessorApi gbWorkflowProcessorApi;


    @DubboReference
    private CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;
    @DubboReference
    TaskApi taskApi;

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), CharsetUtil.UTF_8);
        log.info("ResubmitProcessListener MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        ResubmitGroupBuyRequest resubmitGroupBuyRequest = JSON.parseObject(msg, ResubmitGroupBuyRequest.class);
        QueryProvinceManagerRequest queryProvinceManagerRequest = new QueryProvinceManagerRequest();
        queryProvinceManagerRequest.setOrgId(FlowConstant.MARKET_MANAGER_ORG_ID).setProvinceName(resubmitGroupBuyRequest.getProvinceName());
        SimpleEsbEmployeeInfoBO employeeInfoBO = gbWorkflowProcessorApi.getByProvinceName(queryProvinceManagerRequest);
        if(Objects.isNull(employeeInfoBO)){
            throw new BusinessException(ResultCode.FAILED,"获取市场运营部对应省区的省区经理信息失败");
        }
        //查商务部对应省区负责人
        QueryProvinceManagerRequest queryMarketRequest = new QueryProvinceManagerRequest();
        queryMarketRequest.setOrgId(FlowConstant.COMMERCE_ORG_ID).setProvinceName(resubmitGroupBuyRequest.getProvinceName());
        SimpleEsbEmployeeInfoBO marketEmployeeInfoBO = gbWorkflowProcessorApi.getByProvinceName(queryMarketRequest);
        if(Objects.isNull(marketEmployeeInfoBO)){
            throw new BusinessException(ResultCode.FAILED,"获取商务部对应省区的省区经理信息失败");
        }
        Map<String, Object> variables = Maps.newHashMap();
        //商务部省区经理变量
        variables.put(FlowConstant.COMMERCE_PROVINCE_MANAGER,marketEmployeeInfoBO.getEmpId());
        //查询上级部门
        SimpleEsbEmployeeInfoBO deptLeader = gbWorkflowProcessorApi.getByOrgId(resubmitGroupBuyRequest.getOrgId());
        if(Objects.isNull(deptLeader)){
            throw new BusinessException(ResultCode.FAILED,"获取发起人部门领导信息失败");
        }

        //指定市场运营部省区经理工号
        variables.put(FlowConstant.MARKET_PROVINCE_MANAGER,employeeInfoBO.getEmpId());
        //指定部门领导
        variables.put(FlowConstant.DEPT_LEADER,deptLeader.getEmpId());
        variables.put(FlowConstant.DEPT,resubmitGroupBuyRequest.getOrgId());
        // 出货终端是否是事业三部
        boolean isBreathDept = crmEnterpriseRelationShipApi.isBreathingDepartmentByNameCode(resubmitGroupBuyRequest.getTerminalCompanyCode());
        variables.put("outDept",isBreathDept);

        //是否呼吸事业部
        if(resubmitGroupBuyRequest.getOrgId().equals(FlowConstant.BREATH_DEPT)){
            variables.put("breathDept",true);
        }else{
            variables.put("breathDept",false);
        }

        taskApi.reSubmit(resubmitGroupBuyRequest.getGbNo(),variables);
        return   MqAction.CommitMessage;
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