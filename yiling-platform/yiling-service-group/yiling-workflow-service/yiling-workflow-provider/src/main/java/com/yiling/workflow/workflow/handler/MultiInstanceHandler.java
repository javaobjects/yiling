package com.yiling.workflow.workflow.handler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import com.yiling.workflow.workflow.constant.FlowConstant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;

/**
 * 多实例指定审批人
 * @author: gxl
 * @date: 2023/3/2
 */
@AllArgsConstructor
@Component("multiInstanceHandler")
public class MultiInstanceHandler {
    public HashSet<String> getUserIds(DelegateExecution execution) {
        HashSet<String> candidateUserIds = new LinkedHashSet<>();
        FlowElement flowElement = execution.getCurrentFlowElement();
        if (ObjectUtil.isNotEmpty(flowElement) && flowElement instanceof UserTask) {
            String variable = execution.getVariable(FlowConstant.USERS, String.class);

            List<String> candidateUsers = ((UserTask) flowElement).getCandidateUsers();
            //动态的多个审批人
            if(StrUtil.isNotEmpty(variable) && CollUtil.isEmpty(candidateUsers)){
                String[] userArray = variable.split("_");
                candidateUserIds.addAll(Arrays.stream(userArray).collect(Collectors.toSet()));
                return candidateUserIds;
            }
            //已知的多个固定的审批人走此逻辑
            if(CollUtil.isNotEmpty(candidateUsers)){
                String var = execution.getVariable(FlowConstant.BUSINESS_DATA_SECTION, String.class);
                String[] userArray = var.split("_");
                candidateUserIds.addAll(Arrays.stream(userArray).collect(Collectors.toSet()));
            }
        }
        return candidateUserIds;
    }
}