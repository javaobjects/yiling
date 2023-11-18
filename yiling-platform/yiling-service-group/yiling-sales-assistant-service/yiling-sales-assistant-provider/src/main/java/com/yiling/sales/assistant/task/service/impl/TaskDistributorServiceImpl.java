package com.yiling.sales.assistant.task.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.util.SpringUtils;
import com.yiling.sales.assistant.task.dao.TaskDistributorMapper;
import com.yiling.sales.assistant.task.dto.DistributorDTO;
import com.yiling.sales.assistant.task.dto.app.TaskDistributorDTO;
import com.yiling.sales.assistant.task.dto.request.DeleteTaskDistributorRequest;
import com.yiling.sales.assistant.task.dto.request.QueryDistributorPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskDistributorPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskDistributorRequest;
import com.yiling.sales.assistant.task.entity.MarketTaskDO;
import com.yiling.sales.assistant.task.entity.TaskDistributorDO;
import com.yiling.sales.assistant.task.entity.UserTaskDO;
import com.yiling.sales.assistant.task.enums.FinishTypeEnum;
import com.yiling.sales.assistant.task.enums.TaskStatusEnum;
import com.yiling.sales.assistant.task.enums.UserTaskStatusEnum;
import com.yiling.sales.assistant.task.service.MarketTaskService;
import com.yiling.sales.assistant.task.service.TaskDistributorService;
import com.yiling.sales.assistant.task.service.UserTaskService;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gxl
 * @date 2021-10-11
 */
@Service
public class TaskDistributorServiceImpl extends BaseServiceImpl<TaskDistributorMapper, TaskDistributorDO> implements TaskDistributorService {
    @DubboReference
    private EnterpriseApi enterpriseApi;

/*    @Autowired
    private UserTaskService userTaskService;

    @Autowired
    private MarketTaskService marketTaskService;*/

    @Override
    public Page<DistributorDTO> listDistributorPage(QueryDistributorPageRequest queryDistributorPageRequest) {
        QueryEnterprisePageListRequest request= PojoUtils.map(queryDistributorPageRequest, QueryEnterprisePageListRequest.class);
        request.setMallFlag(1);
        request.setInTypeList(Arrays.asList(EnterpriseTypeEnum.BUSINESS.getCode()));
        Page<EnterpriseDTO> page = enterpriseApi.pageList(request);
        List<EnterpriseDTO> enterpriseDTOList = page.getRecords();
        if(CollUtil.isEmpty(enterpriseDTOList)){
            return queryDistributorPageRequest.getPage();
        }
        List<DistributorDTO> distributorDTOS = Lists.newArrayList();
        enterpriseDTOList.forEach(enterpriseDTO -> {
            DistributorDTO distributorDTO = new DistributorDTO();
            distributorDTO.setDistributorEid(enterpriseDTO.getId()).setName(enterpriseDTO.getName());
            distributorDTOS.add(distributorDTO);
        });
        Page<DistributorDTO> result = queryDistributorPageRequest.getPage();
        result.setRecords(distributorDTOS);
        result.setTotal(page.getTotal());
        return result;
    }

    @Override
    public Page<DistributorDTO> listTaskDistributorPage(QueryTaskDistributorPageRequest queryTaskDistributorPageRequest) {
        LambdaQueryWrapper<TaskDistributorDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TaskDistributorDO::getTaskId,queryTaskDistributorPageRequest.getTaskId());
        wrapper.eq(0!=queryTaskDistributorPageRequest.getType(),TaskDistributorDO::getType,queryTaskDistributorPageRequest.getType());
        Page<TaskDistributorDO> taskDistributorDOPage = this.page(queryTaskDistributorPageRequest.getPage() ,wrapper);
        Page<DistributorDTO> distributorDTOPage = PojoUtils.map(taskDistributorDOPage,DistributorDTO.class);
        return distributorDTOPage;
    }

    @Override
    public Page<TaskDistributorDTO> listAppTaskDistributorPage(com.yiling.sales.assistant.task.dto.request.app.QueryTaskDistributorPageRequest queryTaskDistributorPageRequest) {
        LambdaQueryWrapper<TaskDistributorDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TaskDistributorDO::getTaskId,queryTaskDistributorPageRequest.getTaskId());
        wrapper.like(StringUtils.isNotEmpty(queryTaskDistributorPageRequest.getName()),TaskDistributorDO::getName,queryTaskDistributorPageRequest.getName());
        Page<TaskDistributorDO> taskDistributorDOList = this.page(queryTaskDistributorPageRequest.getPage() ,wrapper);
        Page<TaskDistributorDTO> taskDistributorDTOPage = PojoUtils.map(taskDistributorDOList,TaskDistributorDTO.class);
        return taskDistributorDTOPage;
    }

    @Override
    public void deleteById(DeleteTaskDistributorRequest deleteTaskDistributorRequest) {
        TaskDistributorDO taskDistributorDO = new TaskDistributorDO();
        taskDistributorDO.setOpTime(new Date());
        taskDistributorDO.setId(deleteTaskDistributorRequest.getTaskDistributorId());
        taskDistributorDO.setOpUserId(deleteTaskDistributorRequest.getOpUserId());
        this.deleteByIdWithFill(taskDistributorDO);
    }

    @Override
    public List<Long> queryDistributorByEidList(QueryTaskDistributorRequest request) {
        //查询进行中的交易量任务
        LambdaQueryWrapper<MarketTaskDO> taskWrapper = Wrappers.lambdaQuery();
        taskWrapper.eq(MarketTaskDO::getFinishType,FinishTypeEnum.AMOUNT.getCode()).eq(MarketTaskDO::getTaskStatus, TaskStatusEnum.IN_PROGRESS.getStatus());
        List<MarketTaskDO> marketTaskDOS = SpringUtils.getBean(MarketTaskService.class).list(taskWrapper);
        if(CollUtil.isEmpty(marketTaskDOS)){
            return Lists.newArrayList();
        }
        List<Long> taskIds = marketTaskDOS.stream().map(MarketTaskDO::getId).collect(Collectors.toList());
        List<Long> eidList = request.getEidList();
        Long userId = request.getUserId();
        LambdaQueryWrapper<UserTaskDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserTaskDO::getUserId,userId).eq(UserTaskDO::getFinishType, FinishTypeEnum.AMOUNT.getCode()).in(UserTaskDO::getTaskStatus, UserTaskStatusEnum.IN_PROGRESS.getStatus(),UserTaskStatusEnum.FINISHED.getStatus()).select(UserTaskDO::getTaskId);
        wrapper.in(UserTaskDO::getTaskId,taskIds);
        List<Object> objects = SpringUtils.getBean(UserTaskService.class).listObjs(wrapper);
        if(CollUtil.isEmpty(objects)){
            return Lists.newArrayList();
        }
        List<Long> inTaskIds = PojoUtils.map(objects,Long.class).stream().distinct().collect(Collectors.toList());
        LambdaQueryWrapper<TaskDistributorDO> dwrapper = Wrappers.lambdaQuery();
        dwrapper.in(TaskDistributorDO::getTaskId,inTaskIds).in(TaskDistributorDO::getDistributorEid,eidList);
        List<TaskDistributorDO> list = this.list(dwrapper);
        if(CollUtil.isEmpty(list)){
            return eidList;
        }
        Map<Long, List<TaskDistributorDO>> collect = list.stream().collect(Collectors.groupingBy(TaskDistributorDO::getTaskId));
        for (int i = 0; i < inTaskIds.size(); i++) {
            if(null == collect.get(inTaskIds.get(i))){
                return eidList;
            }
        }
        return list.stream().map(TaskDistributorDO::getDistributorEid).collect(Collectors.toList());
    }
}
