package com.yiling.sjms.wash;

import java.util.Arrays;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.gb.enums.GbLockTypeEnum;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleDepartmentRequest;
import com.yiling.dataflow.wash.dto.request.UpdateStageRequest;
import com.yiling.sjms.BaseTest;
import com.yiling.sjms.wash.dto.request.UpdateUnlockFlowWashSaleDistributionRequest;
import com.yiling.sjms.wash.handler.UnFlowWashHandler;
import com.yiling.sjms.wash.service.SjmsFlowMonthWashControlService;
import com.yiling.sjms.wash.service.SjmsUnlockSaleRuleService;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * DEMO TEST
 *
 * @author xuan.zhou
 * @date 2022/5/12
 */
@Slf4j
public class SjmsFlowMonWashControlTest extends BaseTest {

    @Autowired
    SjmsFlowMonthWashControlService sjmsFlowMonthWashControlService;

    @Autowired
    SjmsUnlockSaleRuleService sjmsUnlockSaleRuleService;

    @Autowired
    UnFlowWashHandler unFlowWashHandler;

    @DubboReference
    private FlowMonthWashControlApi flowMonthWashControlApi;

    @Test
    public void test() {
        UpdateStageRequest request=new UpdateStageRequest();
        request.setId(4L);
        sjmsFlowMonthWashControlService.gbLockStatus(request);
    }

    @Test
    public void test1() {
        UpdateUnlockFlowWashSaleDistributionRequest request=new UpdateUnlockFlowWashSaleDistributionRequest();
        request.setIds(Arrays.asList(19L));
        request.setJudgment(1);
        request.setNotes("张爽测试");
        request.setSaleRange(1);
        SaveUnlockSaleDepartmentRequest request1=new SaveUnlockSaleDepartmentRequest();
        request1.setType(1);
        request1.setBusinessDepartmentName("销售业务部门");
        request1.setDepartmentName("销售部门");
        request.setSaveUnlockSaleDepartmentRequest(request1);
        sjmsUnlockSaleRuleService.updateUnlockSaleRuleDistribution(request);
    }

    @Test
    public void test2() {
        unFlowWashHandler.wash(32732L);
    }

    @Test
    public void test3() {
        // type=1 锁定  type=2非锁定
        Integer type = Convert.toInt("2");
        String typeName = "";
        // 通过不同类型查询日程
        FlowMonthWashControlDTO flowMonthWashControlDTO;
        if (GbLockTypeEnum.LOCK.getCode().equals(type)) {
            typeName = "团购锁定";
            flowMonthWashControlDTO = flowMonthWashControlApi.getGbLockStatus();
        } else {
            typeName = "团购非锁定";
            flowMonthWashControlDTO = flowMonthWashControlApi.getGbUnlockStatus();
        }
        if (ObjectUtil.isNull(flowMonthWashControlDTO)) {
            String operType = "";
            GbLockTypeEnum fromCode = GbLockTypeEnum.getFromCode(type);
            if (ObjectUtil.isNotNull(fromCode)) {
                operType = fromCode.getMessage();
            }
            log.warn(typeName + "日程还没有开启, 不能自动扣减");
        }
    }

}
