package com.yiling.sales.assistant.commissions;

import java.math.BigDecimal;

import com.yiling.sales.assistant.commissions.dto.request.AddCommissionsToUserRequest;
import com.yiling.sales.assistant.commissions.enums.EffectStatusEnum;
import com.yiling.sales.assistant.commissions.service.CommissionsService;
import com.yiling.sales.assistant.task.enums.FinishTypeEnum;

import cn.hutool.core.collection.ListUtil;

/**
 * @author: dexi.yao
 * @date: 2022-09-02
 */
public class TestAddCommissionThread implements Runnable{

    CommissionsService commissionsService;

    public TestAddCommissionThread(CommissionsService commissionsService) {
        this.commissionsService = commissionsService;
    }

    @Override
    public void run() {
        Long startTime=1662109579000L;
        while (Boolean.TRUE){
            if (System.currentTimeMillis()>=startTime){
                System.err.println(Thread.currentThread().getId()+"线程开始，时间："+System.currentTimeMillis());
                AddCommissionsToUserRequest request=new AddCommissionsToUserRequest();
                request.setTaskId(1L);
                request.setUserTaskId(1L);
                request.setTaskName(String.valueOf(Thread.currentThread().getId()));
                request.setSources(1);
                request.setUserId(2L);
                request.setOpUserId(1L);
                request.setEffectStatus(EffectStatusEnum.INVALID.getCode());
                request.setFinishType(FinishTypeEnum.MONEY.getCode());
                AddCommissionsToUserRequest.AddCommToUserDetailRequest detailRequest=new AddCommissionsToUserRequest.AddCommToUserDetailRequest();
                detailRequest.setOrderId(10101021L);
                detailRequest.setOrderCode(String.valueOf(Thread.currentThread().getId()));
                detailRequest.setSubAmount(new BigDecimal("30"));
                request.setDetailList(ListUtil.toList(detailRequest));

                commissionsService.addCommissionsToUser(request);

                System.err.println(Thread.currentThread().getName()+"线程结束，时间："+System.currentTimeMillis());
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
