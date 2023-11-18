package com.yiling.sales.assistant.task.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.task.constant.TaskConstant;
import com.yiling.sales.assistant.task.entity.MarketTaskDO;
import com.yiling.sales.assistant.task.entity.UserTaskDO;
import com.yiling.sales.assistant.task.service.TaskModifyNotifyService;
import com.yiling.sales.assistant.task.service.UserTaskService;

/**
 * @author: gxl
 * @date: 2022/10/18
 */
@Service
public class TaskModifyNotifyServiceImpl implements TaskModifyNotifyService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserTaskService userTaskService;

    @Override
    @Async("asyncExecutor")
    public void addGoodsOrDistributorNotify(MarketTaskDO marketTaskDO) {
        LambdaQueryWrapper<UserTaskDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserTaskDO::getTaskId, marketTaskDO.getId()).select(UserTaskDO::getId);
        List<Object> objects = userTaskService.listObjs(wrapper);
        if (Objects.isNull(objects)) {
            return;
        }
        List<Long> userTaskIds = PojoUtils.map(objects, Long.class);
        userTaskIds.forEach(userTaskId -> {
            String key = String.format(TaskConstant.ADD_GOODS_OR_DISTRIBUTOR_NOTIFY, userTaskId);
            Boolean absent = redisTemplate.opsForValue().setIfAbsent(key, 1);
            if (absent) {
                redisTemplate.expireAt(key, marketTaskDO.getEndTime());
            }
        });
    }
}