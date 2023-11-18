package com.yiling.job.executor.service.jobhandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.dataflow.flow.api.FlowBiJobApi;
import com.yiling.dataflow.flow.api.FlowBiTaskApi;
import com.yiling.dataflow.flow.api.FlowMonthBiJobApi;
import com.yiling.dataflow.flow.api.FlowMonthBiTaskApi;
import com.yiling.dataflow.flow.dto.FlowBiJobDTO;
import com.yiling.dataflow.flow.dto.FlowBiTaskDTO;
import com.yiling.dataflow.flow.dto.FlowMonthBiJobDTO;
import com.yiling.dataflow.flow.dto.FlowMonthBiTaskDTO;
import com.yiling.dataflow.flow.enums.ErpTopicName;
import com.yiling.job.executor.log.JobLog;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpClientQueryRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2022/8/5
 */
@Component
@Slf4j
public class FlowBiJobHandler {

    @DubboReference(timeout = 5 * 1000)
    private ErpClientApi erpClientApi;

    @DubboReference(timeout = 5 * 1000)
    private FlowBiJobApi flowBiJobApi;

    @DubboReference(timeout = 5 * 1000)
    private FlowMonthBiJobApi flowMonthBiJobApi;

    @DubboReference(timeout = 5 * 1000)
    private FlowBiTaskApi flowBiTaskApi;

    @DubboReference(timeout = 5 * 1000)
    private FlowMonthBiTaskApi flowMonthBiTaskApi;

    @JobLog
    @XxlJob("flowBiJobHandler")
    public ReturnT<String> flowBiJobControl(String param) throws Exception {
        log.info("任务开始：日流向对接BI生成任务");
        long start = System.currentTimeMillis();

        //判断任务是否已经生成
        Date date=DateUtil.offsetDay(DateUtil.parseDate(DateUtil.today()),-1);
        List<FlowBiJobDTO> flowBiJobDTOList = flowBiJobApi.listByDate(date);
        if (CollUtil.isEmpty(flowBiJobDTOList)) {
            FlowBiJobDTO flowBiJobDTO = new FlowBiJobDTO();
            flowBiJobDTO.setTaskTime(date);
            Long flowBiJobId = flowBiJobApi.save(flowBiJobDTO);
            //添加erpClient流向任务
            ErpClientQueryRequest request = new ErpClientQueryRequest();
            request.setClientStatus(1);
            request.setBiStatus(1);
            request.setFlowStatus(1);

            //需要循环调用
            Page<ErpClientDTO> page = null;
            int current = 1;
            do {
                request.setCurrent(current);
                request.setSize(500);
                page = erpClientApi.page(request);
                if (page == null || CollUtil.isEmpty(page.getRecords())) {
                    break;
                }
                List<FlowBiTaskDTO> flowBiTaskDTOList=new ArrayList<>();

                for (ErpClientDTO e : page.getRecords()) {
                        FlowBiTaskDTO flowBiTaskDTO = new FlowBiTaskDTO();
                        flowBiTaskDTO.setEid(e.getRkSuId());
                        flowBiTaskDTO.setCrmEnterpriseId(e.getCrmEnterpriseId());
                        flowBiTaskDTO.setJobId(flowBiJobId);
                        flowBiTaskDTO.setTaskCode(ErpTopicName.ErpPurchaseFlow.getMethod());
                        flowBiTaskDTO.setSyncStatus(0);
                        flowBiTaskDTO.setTaskTime(date);
                        flowBiTaskDTO.setId(0L);
                        flowBiTaskDTOList.add(flowBiTaskDTO);
                }

                for (ErpClientDTO e : page.getRecords()) {
                        FlowBiTaskDTO flowBiTaskDTO = new FlowBiTaskDTO();
                        flowBiTaskDTO.setEid(e.getRkSuId());
                        flowBiTaskDTO.setCrmEnterpriseId(e.getCrmEnterpriseId());
                        flowBiTaskDTO.setJobId(flowBiJobId);
                        flowBiTaskDTO.setTaskCode(ErpTopicName.ErpSaleFlow.getMethod());
                        flowBiTaskDTO.setSyncStatus(0);
                        flowBiTaskDTO.setTaskTime(date);
                        flowBiTaskDTO.setId(0L);
                        flowBiTaskDTOList.add(flowBiTaskDTO);
                }

                for (ErpClientDTO e : page.getRecords()) {
                        FlowBiTaskDTO flowBiTaskDTO = new FlowBiTaskDTO();
                        flowBiTaskDTO.setEid(e.getRkSuId());
                        flowBiTaskDTO.setCrmEnterpriseId(e.getCrmEnterpriseId());
                        flowBiTaskDTO.setJobId(flowBiJobId);
                        flowBiTaskDTO.setTaskCode(ErpTopicName.ErpGoodsBatchFlow.getMethod());
                        flowBiTaskDTO.setSyncStatus(0);
                        flowBiTaskDTO.setTaskTime(date);
                        flowBiTaskDTO.setId(0L);
                        flowBiTaskDTOList.add(flowBiTaskDTO);
                }
                //插入任务
                flowBiTaskApi.saveBatch(flowBiTaskDTOList);

                current = current + 1;
            } while (page != null && CollUtil.isNotEmpty(page.getRecords()));

        }

        log.info("任务结束：日流向对接BI生成任务。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("flowMonthBiJobHandler")
    public ReturnT<String> flowMonthBiJobControl(String param) throws Exception {
        log.info("任务开始：月流向对接BI生成任务");
        long start = System.currentTimeMillis();
        param= XxlJobHelper.getJobParam();
        String time = DateUtil.today();
        if (!StringUtils.isEmpty(param)) {
            time = param;
        }
        //判断任务是否已经生成
        Date date=DateUtil.parseDate(time);
        List<FlowMonthBiJobDTO> flowMonthBiJobDTOList = flowMonthBiJobApi.listByDate(date);
        if (CollUtil.isEmpty(flowMonthBiJobDTOList)) {
            // 月流向导出开始时间
            DateTime flowMonthStartTime = DateUtil.beginOfMonth(DateUtil.offsetMonth(date, -1));

            FlowMonthBiJobDTO flowMonthBiJobDTO = new FlowMonthBiJobDTO();
            flowMonthBiJobDTO.setTaskTime(date);
            Long flowMonthBiJobId = flowMonthBiJobApi.save(flowMonthBiJobDTO);

            Date now = new Date();
            //添加erpClient流向任务
            ErpClientQueryRequest request = new ErpClientQueryRequest();
            request.setClientStatus(1);
            request.setBiStatus(1);
            request.setFlowStatus(1);
            //需要循环调用
            Page<ErpClientDTO> page = null;
            int current = 1;
            do {
                request.setCurrent(current);
                request.setSize(500);
                page = erpClientApi.page(request);
                if (page == null || CollUtil.isEmpty(page.getRecords())) {
                    break;
                }
                List<FlowMonthBiTaskDTO> flowMonthBiTaskDTOList=new ArrayList<>();
                for (ErpClientDTO e : page.getRecords()) {
                        // 校验导出月份的心跳时间
                        String errorMsg = checkErpClientHeartTimeMonth(e.getHeartBeatTime(), flowMonthStartTime);
                        // 保存任务
                        FlowMonthBiTaskDTO flowMonthBiTaskDTO = new FlowMonthBiTaskDTO();
                        flowMonthBiTaskDTO.setEid(e.getRkSuId());
                        flowMonthBiTaskDTO.setCrmEnterpriseId(e.getCrmEnterpriseId());
                        flowMonthBiTaskDTO.setMonthJobId(flowMonthBiJobId);
                        flowMonthBiTaskDTO.setTaskCode(ErpTopicName.ErpPurchaseFlow.getMethod());
                        if (StrUtil.isBlank(errorMsg)) {
                            flowMonthBiTaskDTO.setSyncStatus(0);
                        } else {
                            // 导出月份没有心跳，直接给失败状态、失败信息
                            flowMonthBiTaskDTO.setSyncStatus(4);
                            flowMonthBiTaskDTO.setSyncMsg(errorMsg);
                            flowMonthBiTaskDTO.setSyncTime(now);
                        }
                        flowMonthBiTaskDTO.setTaskTime(date);
                        flowMonthBiTaskDTO.setId(0L);
                        flowMonthBiTaskDTO.setOpTime(new Date());
                        flowMonthBiTaskDTOList.add(flowMonthBiTaskDTO);
                }

                for (ErpClientDTO e : page.getRecords()) {
                        // 校验导出月份的心跳时间
                        String errorMsg = checkErpClientHeartTimeMonth(e.getHeartBeatTime(), flowMonthStartTime);
                        // 保存任务
                        FlowMonthBiTaskDTO flowMonthBiTaskDTO = new FlowMonthBiTaskDTO();
                        flowMonthBiTaskDTO.setEid(e.getRkSuId());
                        flowMonthBiTaskDTO.setCrmEnterpriseId(e.getCrmEnterpriseId());
                        flowMonthBiTaskDTO.setMonthJobId(flowMonthBiJobId);
                        flowMonthBiTaskDTO.setTaskCode(ErpTopicName.ErpSaleFlow.getMethod());
                        if (StrUtil.isBlank(errorMsg)) {
                            flowMonthBiTaskDTO.setSyncStatus(0);
                        } else {
                            // 导出月份没有心跳，直接给失败状态、失败信息
                            flowMonthBiTaskDTO.setSyncStatus(4);
                            flowMonthBiTaskDTO.setSyncMsg(errorMsg);
                            flowMonthBiTaskDTO.setSyncTime(now);
                        }
                        flowMonthBiTaskDTO.setTaskTime(date);
                        flowMonthBiTaskDTO.setId(0L);
                        flowMonthBiTaskDTO.setOpTime(new Date());
                        flowMonthBiTaskDTOList.add(flowMonthBiTaskDTO);
                }

                for (ErpClientDTO e : page.getRecords()) {
                        // 校验导出月份的心跳时间
                        String errorMsg = checkErpClientHeartTimeMonth(e.getHeartBeatTime(), flowMonthStartTime);
                        // 保存任务
                        FlowMonthBiTaskDTO flowMonthBiTaskDTO = new FlowMonthBiTaskDTO();
                        flowMonthBiTaskDTO.setEid(e.getRkSuId());
                        flowMonthBiTaskDTO.setCrmEnterpriseId(e.getCrmEnterpriseId());
                        flowMonthBiTaskDTO.setMonthJobId(flowMonthBiJobId);
                        flowMonthBiTaskDTO.setTaskCode(ErpTopicName.ErpGoodsBatchFlow.getMethod());
                        if (StrUtil.isBlank(errorMsg)) {
                            flowMonthBiTaskDTO.setSyncStatus(0);
                        } else {
                            // 导出月份没有心跳，直接给失败状态、失败信息
                            flowMonthBiTaskDTO.setSyncStatus(4);
                            flowMonthBiTaskDTO.setSyncMsg(errorMsg);
                            flowMonthBiTaskDTO.setSyncTime(now);
                        }
                        flowMonthBiTaskDTO.setTaskTime(date);
                        flowMonthBiTaskDTO.setId(0L);
                        flowMonthBiTaskDTO.setOpTime(new Date());
                        flowMonthBiTaskDTOList.add(flowMonthBiTaskDTO);
                }
                //插入任务
                flowMonthBiTaskApi.saveBatch(flowMonthBiTaskDTOList);
                current = current + 1;
            } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        }

        log.info("任务结束：月流向对接BI生成任务。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    /**
     * 校验导出月份是否有心跳
     *
     * @param heartBeatTime 心跳时间
     * @param flowMonthStartTime 导出月份开始时间
     * @return
     */
    private String checkErpClientHeartTimeMonth(Date heartBeatTime, Date flowMonthStartTime) {
        String month = DateUtil.format(flowMonthStartTime, "yyyy-MM");
        if (ObjectUtil.isNull(heartBeatTime)) {
            return "该企业的心跳时间为空";
        }
        if (heartBeatTime.getTime() < flowMonthStartTime.getTime()) {
            return "该企业的心跳时间小于导出月份" + month;
        }
        return null;
    }

}
