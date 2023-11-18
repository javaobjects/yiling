package com.yiling.job.executor.service.jobhandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.dataflow.backup.api.DataFlowBackupApi;
import com.yiling.dataflow.backup.dto.request.DataFlowBackupRequest;
import com.yiling.job.executor.log.JobLog;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpClientQueryRequest;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/7/12
 */
@Component
@Slf4j
public class FlowBackupJobHandler {

    @DubboReference(timeout = 10 * 1000)
    private ErpClientApi erpClientApi;
    @DubboReference(async = true)
    private DataFlowBackupApi dataFlowBackupApi;

    @JobLog
    @XxlJob("dataFlowBackupJobHandler")
    public ReturnT<String> dataFlowBackupJobHandler(String param) throws Exception {
        log.info("任务开始：线上流向备份清理任务执行，dataFlowBackupJobHandler");

        // 对接流向的企业列表
        List<Long> eidList = new ArrayList<>();
        ErpClientQueryRequest request = new ErpClientQueryRequest();

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

        // 开始备份线上流向
        DataFlowBackupRequest dataFlowBackupRequest = new DataFlowBackupRequest();
        dataFlowBackupRequest.setEidList(eidList);
        dataFlowBackupApi.dataFlowBackup(dataFlowBackupRequest);

        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("dataFlowBackupNewJobHandler")
    public ReturnT<String> dataFlowBackupNew(String param) throws Exception {
        log.info("任务开始：线上流向备份清理任务执行，dataFlowBackupNewJobHandler");

        // 对接流向的企业列表，仅查询数据初始化状态为已完成的
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

        // 开始备份线上流向
        DataFlowBackupRequest dataFlowBackupRequest = new DataFlowBackupRequest();
        dataFlowBackupRequest.setEidList(eidList);
        dataFlowBackupApi.dataFlowBackupNew(dataFlowBackupRequest);
        return ReturnT.SUCCESS;
    }
}
