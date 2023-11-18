package com.yiling.dataflow.wash;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.wash.api.FlowMonthWashTaskApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashTaskDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowMonthWashTaskPageRequest;
import com.yiling.dataflow.wash.entity.FlowMonthWashTaskDO;
import com.yiling.dataflow.wash.enums.CollectionMethodEnum;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.dataflow.wash.enums.FlowTypeEnum;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/3/9
 */
@Slf4j
public class FlowMonthWashTaskApiTest extends BaseTest {

    @Autowired
    private FlowMonthWashTaskApi flowMonthWashTaskApi;

    @Test
    public void listPageTest() {
        QueryFlowMonthWashTaskPageRequest request = new QueryFlowMonthWashTaskPageRequest();
        request.setFlowType(1);
        request.setCurrent(1);
        request.setSize(10);
        request.setCrmEids(Arrays.asList(12L, 13L));
        request.setProvinceCodes(Arrays.asList("140000", "500000"));
        Page<FlowMonthWashTaskDTO> page = flowMonthWashTaskApi.listPage(request);
        System.out.println(JSONUtil.toJsonStr(page.getRecords()));
    }

    @Test
    public void saveTest() {
        FlowMonthWashTaskDTO flowMonthWashTaskDTO = new FlowMonthWashTaskDTO();
        flowMonthWashTaskDTO.setFmwcId(1L);
        flowMonthWashTaskDTO.setYear(2023);
        flowMonthWashTaskDTO.setMonth(3);
        flowMonthWashTaskDTO.setCrmEnterpriseId(230349L);
        flowMonthWashTaskDTO.setName("河北康仁大药房连锁有限公司");
        flowMonthWashTaskDTO.setEid(64L);
        flowMonthWashTaskDTO.setCollectionMethod(CollectionMethodEnum.FTP.getCode());
        flowMonthWashTaskDTO.setFlowClassify(FlowClassifyEnum.NORMAL.getCode());
        flowMonthWashTaskDTO.setFlowType(FlowTypeEnum.PURCHASE.getCode());
        //flowMonthWashTaskApi.save(flowMonthWashTaskDTO);
    }

    @Test
    public void deleteTaskAndFlowDataByIdTest() {
        flowMonthWashTaskApi.deleteTaskAndFlowDataById(3L);
    }
}
