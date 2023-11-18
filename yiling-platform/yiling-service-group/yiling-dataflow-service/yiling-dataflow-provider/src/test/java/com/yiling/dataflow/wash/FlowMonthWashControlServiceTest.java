package com.yiling.dataflow.wash;

import java.util.Date;

import cn.hutool.core.date.DateUtil;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateFlowMonthWashControlRequest;
import com.yiling.dataflow.wash.service.FlowMonthWashControlService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2023/3/8
 */
@Slf4j
public class FlowMonthWashControlServiceTest extends BaseTest {

    @Autowired
    private FlowMonthWashControlService flowMonthWashControlService;

    public static void main(String[] args) {
        int month = DateUtil.year(new Date());

        System.out.println(month + "==============");
    }

    @Test
    public void test(){
//        SaveOrUpdateFlowMonthWashControlRequest request=new SaveOrUpdateFlowMonthWashControlRequest();
//        request.setId(2L);
//        request.setMonth(2);
//        request.setYear(2023);
//        request.setDataStartTime(DateUtil.parse("2023-02-01 00:00:00.000"));
//        request.setDataEndTime(DateUtil.parse("2023-02-28 23:59:59.000"));
//        request.setGoodsMappingStartTime(DateUtil.parse("2023-03-01 00:00:00.000"));
//        request.setGoodsMappingEndTime(DateUtil.parse("2023-03-08 23:59:59.999"));
//        request.setGoodsMappingStatus(3);
//        request.setCustomerMappingStartTime(DateUtil.parse("2023-03-01 00:00:00.000"));
//        request.setCustomerMappingEndTime(DateUtil.parse("2023-03-08 23:59:59.999"));
//        request.setCustomerMappingStatus(3);
//        request.setGoodsBatchStartTime(DateUtil.parse("2023-03-01 00:00:00.000"));
//        request.setGoodsBatchEndTime(DateUtil.parse("2023-03-08 23:59:59.999"));
//        request.setGoodsBatchStatus(3);
//        request.setFlowCrossStartTime(DateUtil.parse("2023-03-01 00:00:00.000"));
//        request.setFlowCrossEndTime(DateUtil.parse("2023-03-08 23:59:59.999"));
//        request.setFlowCrossStatus(3);
//        flowMonthWashControlService.saveOrUpdate(request);
    }
}
