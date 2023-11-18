package com.yiling.workflow.workflow.util;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2022/12/28
 */
@Slf4j
@Component
public class BpmnDeployUtil {

    @Value("${flow.deploy}")
    private Boolean deploy;

    @Autowired
    RepositoryService repositoryService;
    public  void deploy(){
        if(deploy){
            //String path = "processes/diagram.bpmn";
            // 创建部署构建器
            DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
            deploymentBuilder.name("SpringBootAutoDeployment") .enableDuplicateFiltering();
            //deploymentBuilder.addClasspathResource("processes/diagram.bpmn");
            // 添加资源
          /*  deploymentBuilder.addClasspathResource("processes/agencyAdd.bpmn");
            deploymentBuilder.addClasspathResource("processes/agencyExtendUpdate.bpmn");
            deploymentBuilder.addClasspathResource("processes/agencyLock.bpmn");
            deploymentBuilder.addClasspathResource("processes/agencyUnlock.bpmn");
            deploymentBuilder.addClasspathResource("processes/agencyUpdate.bpmn");
            deploymentBuilder.addClasspathResource("processes/relationShipChange.bpmn");*/
//            deploymentBuilder.addClasspathResource("processes/groupBuy.bpmn");
//            deploymentBuilder.addClasspathResource("processes/groupBuyCancle.bpmn");
            //deploymentBuilder.addClasspathResource("processes/displaceGoods.bpmn");
           // deploymentBuilder.addClasspathResource("processes/salesAppeal.bpmn");
            // deploymentBuilder.addClasspathResource("processes/groupBuyCost.bpmn");
            deploymentBuilder.addClasspathResource("processes/fixMonthFlow.bpmn");
            // 执行部署
            Deployment deploy = deploymentBuilder.deploy();
            log.info("deploy.getId() = " + deploy.getId());
            log.info("deploy.getName() = " + deploy.getName());
            log.info("deploy.getCategory() = " + deploy.getCategory());
        }
    }
}