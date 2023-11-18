package com.yiling.sales.assistant.task.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.Constants;
import com.yiling.sales.assistant.task.dao.TaskDeptRelationMapper;
import com.yiling.sales.assistant.task.entity.MarketTaskDO;
import com.yiling.sales.assistant.task.entity.TaskDeptRelationDO;
import com.yiling.sales.assistant.task.enums.FinishTypeEnum;
import com.yiling.sales.assistant.task.service.MarketTaskService;
import com.yiling.sales.assistant.task.service.TaskDeptRelationService;
import com.yiling.user.enterprise.api.CustomerContactApi;
import com.yiling.user.enterprise.api.DepartmentApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerContactDTO;
import com.yiling.user.enterprise.dto.EnterpriseDepartmentDTO;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gxl
 * @date 2023-01-09
 */
@Slf4j
@Service
public class TaskDeptRelationServiceImpl extends BaseServiceImpl<TaskDeptRelationMapper, TaskDeptRelationDO> implements TaskDeptRelationService {

    @DubboReference
    CustomerContactApi customerContactApi;

    @DubboReference
    DepartmentApi departmentApi;

    @Autowired
    private MarketTaskService marketTaskService;

    @Override
    public List<Long> filterTaskByDept(List<Long> taskIds,Long eid) {
        log.debug("匹配上传资料任务taskIds={}",taskIds);
        //过滤上传资料的任务
        LambdaQueryWrapper<MarketTaskDO> lambdaTask = new QueryWrapper<MarketTaskDO>().lambda();
        lambdaTask.in(MarketTaskDO::getId, taskIds).eq(MarketTaskDO::getFinishType, FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode());
        List<MarketTaskDO> marketTaskDOList = marketTaskService.list(lambdaTask);
        //没有上传资料的任务不执行部门判断的逻辑
        if(CollUtil.isEmpty(marketTaskDOList)){
            return taskIds;
        }
        //匹配小三元所属商业公司商务联系人
        List<EnterpriseCustomerContactDTO> enterpriseCustomerContactDTOS = customerContactApi.listByEidAndCustomerEid(Constants.YILING_EID, eid);
        //没有商务联系人 看不到上传资料的任务
        List<Long> uploadTaskIds = marketTaskDOList.stream().map(MarketTaskDO::getId).collect(Collectors.toList());
        if(CollUtil.isEmpty(enterpriseCustomerContactDTOS)){
           log.debug("没有商务联系人eid={}",eid);
           //取任务id差集
            List<Long> subtractToList = CollUtil.subtractToList(taskIds,uploadTaskIds);
            return subtractToList;
        }
        List<Long> contactUserIds = enterpriseCustomerContactDTOS.stream().map(EnterpriseCustomerContactDTO::getContactUserId).collect(Collectors.toList());
        Map<Long, List<EnterpriseDepartmentDTO>> departmentByEidUser = departmentApi.getDepartmentByEidUser(Constants.YILING_EID, contactUserIds);
        if(CollUtil.isEmpty(departmentByEidUser)){
            log.info("未查到商务联系人部门信息contactUserIds={} ",contactUserIds);
            List<Long> subtractToList = CollUtil.subtractToList(taskIds,uploadTaskIds);
            return subtractToList;
        }
        log.debug("部门信息departmentByEidUser={}", JSON.toJSONString(departmentByEidUser));
        Set<Long> set = new HashSet();
        departmentByEidUser.forEach((key,value)->{
            List<Long> deptIds = value.stream().map(EnterpriseDepartmentDTO::getId).collect(Collectors.toList());
            set.addAll(deptIds);
        });
        //商务联系人部门ids
        ArrayList<Long> contactDeptIds = CollUtil.newArrayList(set);
        //上传资料任务配置部门id
        LambdaQueryWrapper<TaskDeptRelationDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(TaskDeptRelationDO::getTaskId,uploadTaskIds).in(TaskDeptRelationDO::getDeptId,contactDeptIds);
        //匹配部门成功的上传资料任务
        List<TaskDeptRelationDO> taskDeptRelationDOS = this.list(wrapper);
        List<Long> matchTaskIds = taskDeptRelationDOS.stream().map(TaskDeptRelationDO::getTaskId).distinct().collect(Collectors.toList());
        if(CollUtil.isEmpty(matchTaskIds)){
            //没有匹配的就全部移除
            List<Long> subtractToList = CollUtil.subtractToList(taskIds,uploadTaskIds );
            return subtractToList;
        }
        //移除部门不匹配的部分上传资料的任务
        List<Long> unmatchList = CollUtil.subtractToList(uploadTaskIds, matchTaskIds);
        return CollUtil.subtractToList(taskIds, unmatchList);
    }
}
