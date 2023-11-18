package com.yiling.workflow.workflow.listener;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.enums.MqDelayLevel;
import com.yiling.sjms.constant.ApproveConstant;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.workflow.factory.FlowServiceFactory;
import com.yiling.workflow.workflow.enums.FlowCommentEnum;
import com.yiling.workflow.workflow.service.WfProcessService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 相同审批人自动审批监听
 * @author: gxl
 * @date: 2023/3/6
 */
@Slf4j
@Component(value = "autoApproveListener")
public class AutoApproveListener  extends FlowServiceFactory implements TaskListener {
    @Autowired
    private WfProcessService wfProcessService;

    @Autowired
    private RedisService redisService;

    @DubboReference
    private MqMessageSendApi mqMessageSendApi;

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    private FormApi formApi;

    @MdcLog
    @Override
    public void notify(DelegateTask delegateTask) {
        //上一个几点的审批人
        String previousAssignee = (String) delegateTask.getVariable("previousAssignee");
        String taskId = delegateTask.getId();
        Object autoFinishObj = delegateTask.getVariable(ApproveConstant.AUTO_FINISH);
        boolean autoFinish = false;
        if(Objects.nonNull(autoFinishObj)){
            autoFinish = (boolean)autoFinishObj;
        }
        //发起人的节点审批人是空的不执行下面的逻辑
        if((StrUtil.isNotEmpty(delegateTask.getAssignee()) &&StrUtil.isNotEmpty(previousAssignee)) || autoFinish){
            log.debug("自动审批上一个节点审批人previousAssignee={}taskid={}",previousAssignee,taskId);
            String currentAssignee = delegateTask.getAssignee();
            //相邻节点审批人一样自动审批
            if(currentAssignee.equals(previousAssignee) ||autoFinish){
                String redisKey = RedisKey.generate("workflow", taskId);
                redisService.set(redisKey,1,120);
                log.info("和前一个节点审批人相同自动审批taskId={}currentAssignee={}previousAssignee={}",taskId,currentAssignee,previousAssignee);
                MqMessageBO nextMqMessageBO = new MqMessageBO(Constants.TOPIC_AUTO_APPROVE, Constants.TOPIC_AUTO_APPROVE, taskId);
                nextMqMessageBO.setDelayLevel(MqDelayLevel.THIRTY_SECONDS);
                nextMqMessageBO = mqMessageSendApi.prepare(nextMqMessageBO);
                mqMessageSendApi.send(nextMqMessageBO);
            }else {
                //查询历史审批人
                List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).finished().orderByHistoricTaskInstanceEndTime().desc().list();
                if(CollUtil.isNotEmpty(historicTaskInstances)){
                    List<HistoricTaskInstance> collect = historicTaskInstances.stream().filter(historicTaskInstance -> StrUtil.isNotEmpty(historicTaskInstance.getAssignee())).collect(Collectors.toList());
                    if(CollUtil.isNotEmpty(collect)){
                        List<Comment> commentList = taskService.getProcessInstanceComments(delegateTask.getProcessInstanceId());
                        List<Comment> rejectCommentList = commentList.stream().filter(comment -> FlowCommentEnum.REBACK.getType().equals(comment.getType())).collect(Collectors.toList());
                        if(CollUtil.isNotEmpty(rejectCommentList)){
                            rejectCommentList.sort(Comparator.comparing(Comment::getTime).reversed());
                            log.debug("驳回任务节点rejectTaskInstance={}",JSON.toJSONString(rejectCommentList));
                            //过滤驳回任务之前已审批任务
                            HistoricTaskInstance taskInstance = historicTaskInstances.stream().filter(historicTaskInstance -> historicTaskInstance.getId().equals(rejectCommentList.get(0).getTaskId())).findFirst().orElse(null);
                            log.info("自动审批最后一个驳回的任务taskInstanceId={}",taskInstance.getId());
                            //HistoricTaskInstance rejectTask = collect.stream().filter(historicTaskInstance -> StrUtil.isNotEmpty(historicTaskInstance.getDeleteReason()) && rejectTaskInstance.getDeleteReason().equals(historicTaskInstance.getDeleteReason())).findFirst().orElse(null);
                                collect = collect.stream().filter(historicTaskInstance -> historicTaskInstance.getEndTime().compareTo(taskInstance.getEndTime())>0 && !historicTaskInstance.getId().equals(rejectCommentList.get(0).getTaskId())).collect(Collectors.toList());
                            log.debug("历史审批人collect={}",JSON.toJSONString(collect));
                        }

                        boolean present = collect.stream().filter(historicTaskInstance -> historicTaskInstance.getAssignee().equals(currentAssignee)).findAny().isPresent();

                        if(present){
                            log.info("自动审批taskId={}collect={}currentAssignee={}",taskId,JSON.toJSONString(collect),currentAssignee);
                            String redisKey = RedisKey.generate("workflow", taskId);
                            redisService.set(redisKey,1,120);
                            MqMessageBO nextMqMessageBO = new MqMessageBO(Constants.TOPIC_AUTO_APPROVE, Constants.TOPIC_AUTO_APPROVE, taskId);
                            nextMqMessageBO.setDelayLevel(MqDelayLevel.THIRTY_SECONDS);
                            nextMqMessageBO = mqMessageSendApi.prepare(nextMqMessageBO);
                            mqMessageSendApi.send(nextMqMessageBO);
                        }else{
                            log.debug("历史审批人中不包含当前审批人2");
                        }
                    }else{
                        log.debug("历史审批人中不包含当前审批人1");
                    }
                }else{
                    log.debug("不存在历史审批人");
                }

            }
            // 获取UserTask
            log.debug("自动审批监听delegateTask={}",JSON.toJSONString(delegateTask));


            }
    }

}
