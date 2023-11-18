package com.yiling.job.statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.job.BaseTest;
import com.yiling.job.executor.service.jobhandler.ErpSyncHandler;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpClientQueryRequest;

import cn.hutool.core.collection.CollUtil;

/**
 * @author: houjie.sun
 * @date: 2022/10/28
 */
public class ErpSyncHandlerTest extends BaseTest {

    @Autowired
    private ErpSyncHandler erpSyncHandler;
    @DubboReference(timeout = 10 * 1000)
    private ErpClientApi erpClientApi;


    @Test
    public void erpClientHeartSyncBiTest() {
        try {
            erpSyncHandler.erpClientNoHeart(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void erpClientTest() {
        List<Long> eidList = new ArrayList<>();
        ErpClientQueryRequest request = new ErpClientQueryRequest();
        request.setDataInitStatus(1);

        Page<ErpClientDTO> page;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            page = erpClientApi.page(request);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            List<Long> eids = page.getRecords().stream().map(ErpClientDTO::getRkSuId).distinct().collect(Collectors.toList());
            eidList.addAll(eids);

            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
    }

}
