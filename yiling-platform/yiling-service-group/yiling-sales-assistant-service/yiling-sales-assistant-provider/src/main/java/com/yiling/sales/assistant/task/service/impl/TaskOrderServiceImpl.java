package com.yiling.sales.assistant.task.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.task.dao.TaskOrderMapper;
import com.yiling.sales.assistant.task.dto.TaskTraceOrderDTO;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderGoodsRequest;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskTraceOrderRequest;
import com.yiling.sales.assistant.task.entity.MarketTaskDO;
import com.yiling.sales.assistant.task.entity.TaskOrderDO;
import com.yiling.sales.assistant.task.entity.TaskOrderGoodsDO;
import com.yiling.sales.assistant.task.entity.UserTaskDO;
import com.yiling.sales.assistant.task.service.TaskOrderGoodsService;
import com.yiling.sales.assistant.task.service.TaskOrderService;
import com.yiling.sales.assistant.task.service.strategy.TaskStrategyContext;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 任务订单 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
@Slf4j
@Service
public class TaskOrderServiceImpl extends BaseServiceImpl<TaskOrderMapper, TaskOrderDO> implements TaskOrderService {

    @Autowired
    private TaskOrderGoodsService taskOrderGoodsService;


    @Autowired
    private TaskStrategyContext taskStrategyContext;

    @Override
    public Page<TaskTraceOrderDTO> listTaskTraceOrderPage(QueryTaskTraceOrderRequest queryTaskTraceOrderRequest) {
        LambdaQueryWrapper<TaskOrderDO> lambdaQueryWrapper = Wrappers.lambdaQuery();

        lambdaQueryWrapper.eq(TaskOrderDO::getUserTaskId,queryTaskTraceOrderRequest.getUserTaskId());
        lambdaQueryWrapper.select(TaskOrderDO::getTerminalName,TaskOrderDO::getOrderNo,TaskOrderDO::getOrderTime);
        // 完成的订单
        lambdaQueryWrapper.orderByDesc(TaskOrderDO::getUpdateTime);
        IPage<TaskOrderDO> iPage = this.page(queryTaskTraceOrderRequest.getPage(),lambdaQueryWrapper);
        Page<TaskTraceOrderDTO> retult = PojoUtils.map(iPage,TaskTraceOrderDTO.class);
        return retult;
    }

    @Override
    public void computeTaskOrder(List<Long> taskGoodsIds, AddTaskOrderRequest addTaskOrderRequest, MarketTaskDO taskDO, UserTaskDO userTaskDO) {
        Long taskId = taskDO.getId();
        TaskOrderDO taskOrder = new TaskOrderDO();
        PojoUtils.map(addTaskOrderRequest,taskOrder);
        taskOrder.setTaskId(taskId);
        taskOrder.setOrderId(addTaskOrderRequest.getOrderId());
        taskOrder.setAmount(0);
        taskOrder.setTotalMoney(BigDecimal.ZERO);
        List<AddTaskOrderGoodsRequest> taskOrderGoodsDOList = addTaskOrderRequest.getOrderGoodsAddDTOList();
        List<TaskOrderGoodsDO> goodsList = Lists.newArrayListWithExpectedSize(taskOrderGoodsDOList.size());

        taskOrderGoodsDOList.forEach(addTaskOrderGoodsRequest -> {
            if((CollUtil.isNotEmpty(taskGoodsIds)) && !taskGoodsIds.contains(addTaskOrderGoodsRequest.getGoodsId())){
                log.info("商品不在此任务中taskid={},商品id={}",taskId,addTaskOrderGoodsRequest.getGoodsId());
                return;
            }
            TaskOrderGoodsDO taskOrderGoods = new TaskOrderGoodsDO();
            PojoUtils.map(addTaskOrderGoodsRequest,taskOrderGoods);
            taskOrderGoods.setRealAmount(addTaskOrderGoodsRequest.getAmount()).setRealMoney(NumberUtil.mul(addTaskOrderGoodsRequest.getAmount(),addTaskOrderGoodsRequest.getPrice()).setScale(2,BigDecimal.ROUND_HALF_EVEN));
            goodsList.add(taskOrderGoods);
            BigDecimal totalMoney = NumberUtil.add(taskOrder.getTotalMoney(),NumberUtil.mul(addTaskOrderGoodsRequest.getPrice(),addTaskOrderGoodsRequest.getAmount()).setScale(2,BigDecimal.ROUND_HALF_EVEN));
            taskOrder.setTotalMoney(totalMoney).setRealMoney(totalMoney);
            taskOrder.setAmount(taskOrder.getAmount()+addTaskOrderGoodsRequest.getAmount()).setRealAmount(taskOrder.getAmount());
        });
        if(Objects.isNull(taskOrder.getRealAmount())){
           log.info("下单商品不在任务配置范围内orderNo={},userTaskDO={}",addTaskOrderRequest.getOrderNo(),userTaskDO.toString());
           return;
        }
        taskOrder.setUserTaskId(userTaskDO.getId());
       // this.save(taskOrder);
        goodsList.forEach(goods -> {
            goods.setTaskOrderId(taskOrder.getId());
        });
        //保存任务商品
        //taskOrderGoodsService.saveBatch(goodsList);
        //进度计算
        taskStrategyContext.taskProgressHandlerMap.get(userTaskDO.getFinishType()).taskHandle(taskDO,userTaskDO,taskOrder,goodsList,addTaskOrderRequest);

    }
}
