package com.yiling.sales.assistant.task.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.task.dao.SaTaskRegisterUserMapper;
import com.yiling.sales.assistant.task.dto.TaskTraceRegisterUserDTO;
import com.yiling.sales.assistant.task.dto.request.QueryTaskRegisterUserRequest;
import com.yiling.sales.assistant.task.entity.SaTaskRegisterUserDO;
import com.yiling.sales.assistant.task.service.SaTaskRegisterUserService;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

/**
 * <p>
 * 任务和拉新人-关联表 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2021-09-18
 */
@Service
public class SaTaskRegisterUserServiceImpl extends BaseServiceImpl<SaTaskRegisterUserMapper, SaTaskRegisterUserDO> implements SaTaskRegisterUserService {
    @DubboReference
    private UserApi userApi;
    @Override
    public Page<TaskTraceRegisterUserDTO> listTaskRegisterUserPage(QueryTaskRegisterUserRequest queryTaskRegisterUserRequest) {
        LambdaQueryWrapper<SaTaskRegisterUserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SaTaskRegisterUserDO::getUserTaskId,queryTaskRegisterUserRequest.getUserTaskId()).orderByDesc(SaTaskRegisterUserDO::getId).orderByDesc(SaTaskRegisterUserDO::getRegisterTime);
        Page<SaTaskRegisterUserDO> page = this.page(queryTaskRegisterUserRequest.getPage(),wrapper);
        Page<TaskTraceRegisterUserDTO> result = PojoUtils.map(page,TaskTraceRegisterUserDTO.class);
        if(page.getTotal()>0){
            List<Long> opUserIds = result.getRecords().stream().map(TaskTraceRegisterUserDTO::getUserId).distinct().collect(Collectors.toList());

            List<UserDTO> userDTOList = userApi.listByIds(opUserIds);
            Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
            result.getRecords().forEach(registerUserDTO -> {
                registerUserDTO.setName(userDTOMap.getOrDefault(registerUserDTO.getUserId(), new UserDTO()).getName());
            });
        }
        return result;
    }
}
