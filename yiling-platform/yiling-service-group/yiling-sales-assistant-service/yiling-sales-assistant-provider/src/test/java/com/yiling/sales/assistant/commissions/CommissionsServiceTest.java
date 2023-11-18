package com.yiling.sales.assistant.commissions;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.sales.assistant.BaseTest;
import com.yiling.sales.assistant.commissions.dto.request.AddCommissionsToUserRequest;
import com.yiling.sales.assistant.commissions.enums.EffectStatusEnum;
import com.yiling.sales.assistant.commissions.service.CommissionsService;
import com.yiling.sales.assistant.task.enums.FinishTypeEnum;

import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;


 /**
 * @author dexi.yao
 * @date 2021-09-17
 */
@Slf4j
public class CommissionsServiceTest extends BaseTest {


    @Autowired
	CommissionsService commissionsService;


    @Test
    public void test(){
		AddCommissionsToUserRequest request=new AddCommissionsToUserRequest();
		request.setTaskId(25L);
		request.setUserTaskId(26L);
		request.setTaskName("测试并发24");
		request.setSources(1);
		request.setUserId(2L);
		request.setOpUserId(1L);
		request.setEffectStatus(EffectStatusEnum.VALID.getCode());
		request.setFinishType(FinishTypeEnum.MONEY.getCode());
		AddCommissionsToUserRequest.AddCommToUserDetailRequest detailRequest=new AddCommissionsToUserRequest.AddCommToUserDetailRequest();
		detailRequest.setOrderId(10101021L);
		detailRequest.setOrderCode("Q10101021");
		detailRequest.setSubAmount(new BigDecimal("30"));
//		AddCommissionsToUserRequest.AddCommToUserDetailRequest detailRequest=new AddCommissionsToUserRequest.AddCommToUserDetailRequest();
//		detailRequest.setOrderId(333L);
//		detailRequest.setOrderCode("Q3333333");
//		detailRequest.setSubAmount(new BigDecimal("20"));
//		AddCommissionsToUserRequest.AddCommToUserDetailRequest detailRequest2=new AddCommissionsToUserRequest.AddCommToUserDetailRequest();
//		detailRequest2.setOrderId(222L);
//		detailRequest2.setOrderCode("Q222222222");
//		detailRequest2.setSubAmount(new BigDecimal("10"));
		request.setDetailList(ListUtil.toList(detailRequest));

		Boolean aBoolean = commissionsService.addCommissionsToUser(request);
		System.err.println(aBoolean);
    }

    @Test
    public void test2(){
		AddCommissionsToUserRequest request=new AddCommissionsToUserRequest();
		request.setTaskId(3L);
		request.setUserTaskId(4L);
		request.setTaskName("测试拉新任务3");
		request.setUserId(1L);
		request.setOpUserId(1L);
		request.setEffectStatus(EffectStatusEnum.VALID.getCode());
		request.setFinishType(FinishTypeEnum.NEW_ENT.getCode());
		AddCommissionsToUserRequest.AddCommToUserDetailRequest detailRequest=new AddCommissionsToUserRequest.AddCommToUserDetailRequest();
		detailRequest.setNewEntId(777L);
		detailRequest.setNewEntName("测试拉新企业");
		detailRequest.setSubAmount(new BigDecimal("10.10"));
		detailRequest.setNewTime(new Date());
//		AddCommissionsToUserRequest.AddCommToUserDetailRequest detailRequest=new AddCommissionsToUserRequest.AddCommToUserDetailRequest();
//		detailRequest.setOrderId(333L);
//		detailRequest.setOrderCode("Q3333333");
//		detailRequest.setSubAmount(new BigDecimal("20"));
//		AddCommissionsToUserRequest.AddCommToUserDetailRequest detailRequest2=new AddCommissionsToUserRequest.AddCommToUserDetailRequest();
//		detailRequest2.setOrderId(222L);
//		detailRequest2.setOrderCode("Q222222222");
//		detailRequest2.setSubAmount(new BigDecimal("10"));
		request.setDetailList(ListUtil.toList(detailRequest));

		Boolean aBoolean = commissionsService.addCommissionsToUser(request);
		System.err.println(aBoolean);
    }

     @Test
     public void test3() throws InterruptedException {
         ExecutorService es = Executors.newFixedThreadPool(4);
         es.submit(new TestAddCommissionThread(commissionsService));
         es.submit(new TestAddCommissionThread(commissionsService));
         es.submit(new TestAddCommissionThread(commissionsService));

         Thread.sleep(100000000000000L);
     }

}
