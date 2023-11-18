package com.yiling.sjms.flee.service.impl;

import java.util.Date;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.sjms.BaseTest;
import com.yiling.sjms.flee.dto.request.CreateSalesAppealFlowRequest;
import com.yiling.sjms.flee.entity.SalesAppealExtFormDO;
import com.yiling.sjms.flee.service.SalesAppealExtFormService;
import com.yiling.sjms.flee.service.SalesAppealFormService;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.form.service.FormService;
import com.yiling.sjms.gb.dto.request.UpdateGBFormInfoRequest;
import com.yiling.sjms.gb.service.GbFormService;

import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: shixing.sun
 * @date: 2023/3/20
 */
@Slf4j
public class SalesAppealFormServiceImplTest extends BaseTest {

    @Autowired
    SalesAppealFormService salesAppealFormService;

    @Autowired
    private SalesAppealExtFormService salesAppealExtFormService;

    @DubboReference
    private FlowMonthWashControlApi flowMonthWashControlApi;

    @Test
    public void  checkFlowFileName(){
        salesAppealFormService.checkFlowFileName("sM_992_洛阳市伊川县鸣皋镇卫生院_20230320_销量申诉确认.xlsx");
    }
    @Test
    public void  updateGBF1ormById(){
        CreateSalesAppealFlowRequest createSalesAppealFlowRequest = new CreateSalesAppealFlowRequest();
        createSalesAppealFlowRequest.setFormId(359L);
        salesAppealFormService.createFleeFlowForm(createSalesAppealFlowRequest);
    }

    @Test
    public void updateWrapper() {
        UpdateWrapper<SalesAppealExtFormDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("form_id", "336");
        updateWrapper.set("submit_wash_time", new Date());
        boolean update = salesAppealExtFormService.update(updateWrapper);
        System.out.println(update);
    }

    /**
     * 日程开启，将 “已提交/未清洗” 的销量申诉数据重新生成清洗任务，推送到清洗队列
     */
    @Test
    public void washSaleAppealFlowData() {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getWashStatus();
        boolean isSuccess = salesAppealFormService.washSaleAppealFlowData();
        System.out.println(isSuccess);
    }

}