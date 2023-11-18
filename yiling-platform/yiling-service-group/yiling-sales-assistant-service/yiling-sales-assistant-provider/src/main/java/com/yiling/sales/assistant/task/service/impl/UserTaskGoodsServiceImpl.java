package com.yiling.sales.assistant.task.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.task.dao.UserTaskGoodsMapper;
import com.yiling.sales.assistant.task.dto.TaskTraceGoodsDTO;
import com.yiling.sales.assistant.task.dto.request.QueryTaskTraceGoodsRequest;
import com.yiling.sales.assistant.task.entity.UserTaskGoodsDO;
import com.yiling.sales.assistant.task.enums.FinishTypeEnum;
import com.yiling.sales.assistant.task.service.UserTaskGoodsService;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 用户任务商品  服务实现类
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
@Service
public class UserTaskGoodsServiceImpl extends BaseServiceImpl<UserTaskGoodsMapper, UserTaskGoodsDO> implements UserTaskGoodsService {

    @Override
    public List<TaskTraceGoodsDTO> listTaskTraceGoods(QueryTaskTraceGoodsRequest queryTaskTraceGoodsRequest) {
        LambdaQueryWrapper<UserTaskGoodsDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(UserTaskGoodsDO::getUserTaskId,queryTaskTraceGoodsRequest.getUserTaskId());
        List<UserTaskGoodsDO> list = this.list(lambdaQueryWrapper);
        List<TaskTraceGoodsDTO> records = Lists.newArrayListWithExpectedSize(list.size());
        if(CollUtil.isEmpty(list)){
            return records;
        }
        list.forEach(UserTaskGoodsDO -> {
            TaskTraceGoodsDTO taskTraceGoodsDTO = new TaskTraceGoodsDTO();
            PojoUtils.map(UserTaskGoodsDO,taskTraceGoodsDTO);
            if(UserTaskGoodsDO.getValueType().equals(FinishTypeEnum.MONEY.getCode())){
                String  goal = new BigDecimal(UserTaskGoodsDO.getGoalValue()).divide(BigDecimal.valueOf(100)).toString();
                taskTraceGoodsDTO.setGoalValue(goal);
                String  finish = new BigDecimal(UserTaskGoodsDO.getFinishValue()).divide(BigDecimal.valueOf(100)).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
                taskTraceGoodsDTO.setFinishValue(finish);
            }else{
                taskTraceGoodsDTO.setGoalValue(UserTaskGoodsDO.getGoalValue().toString());
                taskTraceGoodsDTO.setFinishValue(UserTaskGoodsDO.getFinishValue().toString());
            }
            records.add(taskTraceGoodsDTO);
        });
        return records;
    }
}
