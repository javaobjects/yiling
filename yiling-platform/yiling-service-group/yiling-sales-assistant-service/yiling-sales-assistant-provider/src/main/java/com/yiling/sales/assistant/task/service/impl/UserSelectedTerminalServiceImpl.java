package com.yiling.sales.assistant.task.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.task.dao.UserSelectedTerminalMapper;
import com.yiling.sales.assistant.task.dto.TaskTerminalDTO;
import com.yiling.sales.assistant.task.dto.request.QueryLockTerminalListRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryTaskTerminalPageRequest;
import com.yiling.sales.assistant.task.entity.TaskAreaRelationDO;
import com.yiling.sales.assistant.task.entity.UserSelectedTerminalDO;
import com.yiling.sales.assistant.task.enums.TerminalStatusEnum;
import com.yiling.sales.assistant.task.enums.UserSelectedTerminalReleasedEnum;
import com.yiling.sales.assistant.task.service.TaskAreaRelationService;
import com.yiling.sales.assistant.task.service.UserSelectedTerminalService;
import com.yiling.user.common.enums.UserCustomerStatusEnum;
import com.yiling.user.usercustomer.dto.UserCustomerDTO;
import com.yiling.user.usercustomer.dto.request.QueryUserCustomerRequest;

/**
 * <p>
 * 销售用户已选终端  服务实现类
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
@Service
public class UserSelectedTerminalServiceImpl extends BaseServiceImpl<UserSelectedTerminalMapper, UserSelectedTerminalDO> implements UserSelectedTerminalService {

    /*@Autowired
    private UserCustomerService userCustomerService;*/

    @Autowired
    private TaskAreaRelationService taskAreaRelationService;

    @Override
    public Page<UserSelectedTerminalDO> getLockTerminalList(QueryLockTerminalListRequest request) {
        LambdaQueryWrapper<UserSelectedTerminalDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(UserSelectedTerminalDO::getUserTaskId,request.getUserTaskId())
                          .eq(UserSelectedTerminalDO::getReleased, UserSelectedTerminalReleasedEnum.LOCK.getCode()).orderByDesc(UserSelectedTerminalDO::getId);
        return this.page(request.getPage(),lambdaQueryWrapper);
    }

    @Override
    public Page<TaskTerminalDTO> listTaskTerminalPage(QueryTaskTerminalPageRequest queryTaskTerminalPageRequest) {
        QueryUserCustomerRequest queryUserCustomerRequest = new QueryUserCustomerRequest();
        queryUserCustomerRequest.setStatus(UserCustomerStatusEnum.PASS.getCode());
        PojoUtils.map(queryTaskTerminalPageRequest,queryUserCustomerRequest);
        //todo 废弃 查询用户销售区域
        //todo 废弃 根据任务区域匹配
        LambdaQueryWrapper<TaskAreaRelationDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TaskAreaRelationDO::getTaskId,queryTaskTerminalPageRequest.getTaskId());
        List<TaskAreaRelationDO> list = taskAreaRelationService.list(wrapper);
        Page<UserCustomerDTO> userCustomerDTOPage = null;//userCustomerService.pageList(queryUserCustomerRequest);
        Page<TaskTerminalDTO> taskTerminalDTOPage = PojoUtils.map(userCustomerDTOPage,TaskTerminalDTO.class);
        //查询当前用户已选的终端
        LambdaQueryWrapper<UserSelectedTerminalDO> terminalWrapper = Wrappers.lambdaQuery();
        terminalWrapper.eq(UserSelectedTerminalDO::getTaskId, queryTaskTerminalPageRequest.getTaskId());
        terminalWrapper.eq(UserSelectedTerminalDO::getReleased, false).eq(UserSelectedTerminalDO::getUserId,queryTaskTerminalPageRequest.getUserId());
        terminalWrapper.select(UserSelectedTerminalDO::getTerminalId);
        List<UserSelectedTerminalDO> selectedTerminals = this.list(terminalWrapper);
        List<Long> customerIds = selectedTerminals.stream().map(UserSelectedTerminalDO::getTerminalId).collect(Collectors.toList());

        //查询当前任务其他用户已占用终端
        LambdaQueryWrapper<UserSelectedTerminalDO> otherTerminalWrapper = Wrappers.lambdaQuery();
        otherTerminalWrapper.eq(UserSelectedTerminalDO::getTaskId, queryTaskTerminalPageRequest.getTaskId());
        otherTerminalWrapper.eq(UserSelectedTerminalDO::getReleased, false).ne(UserSelectedTerminalDO::getUserId,queryTaskTerminalPageRequest.getUserId());
        otherTerminalWrapper.select(UserSelectedTerminalDO::getTerminalId);
        List<UserSelectedTerminalDO> otherSelectedTerminals = this.list(terminalWrapper);
        List<Long> otherCustomerIds = otherSelectedTerminals.stream().map(UserSelectedTerminalDO::getTerminalId).collect(Collectors.toList());

        List<TaskTerminalDTO> taskTerminalDTOS = taskTerminalDTOPage.getRecords();
        taskTerminalDTOS.forEach(taskTerminalDTO -> {

            if(customerIds.contains(taskTerminalDTO.getCustomerEid())){
                taskTerminalDTO.setStatus(TerminalStatusEnum.SELECED.getStatus());
            }else if(otherCustomerIds.contains(taskTerminalDTO.getCustomerEid())){
                taskTerminalDTO.setStatus(TerminalStatusEnum.OTHER_SELECTED.getStatus());
            }else{
                taskTerminalDTO.setStatus(TerminalStatusEnum.SELECT.getStatus());
            }
        });
        //  todo 废弃 我选的任务
        return taskTerminalDTOPage;
    }

    @Override
    public Page<TaskTerminalDTO> listTaskAllTerminalPage(QueryTaskTerminalPageRequest queryTaskTerminalPageRequest) {
        //todo 废弃 选终端
        return null;
    }
}
