package com.yiling.dataflow.wash.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.FlowMonthWashTaskDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowMonthWashTaskPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveFlowMonthWashTaskRequest;
import com.yiling.dataflow.wash.dto.request.UpdateFlowMonthWashTaskStatusRequest;
import com.yiling.dataflow.wash.dto.request.UpdateReportConsumeStatusRequest;

/**
 * @author fucheng.bai
 * @date 2023/3/1
 */
public interface FlowMonthWashTaskApi {

    /**
     * 查询流向清洗列表
     * @param request
     * @return
     */
    Page<FlowMonthWashTaskDTO> listPage(QueryFlowMonthWashTaskPageRequest request);

    /**
     * 创建清洗任务
     * @param request
     * @param isSendMq true=发送mq false=不发送mq自己单独发送
     * @return
     */
    long create(SaveFlowMonthWashTaskRequest request, boolean isSendMq);

    /**
     * 通过日程ID查询已经生成的机构id
     * @param fmwcId 日程ID
     * @param flowClassify 分类ID
     * @return
     */
    List<Long> getCrmEnterIdsByFmwcIdAndClassify(long fmwcId, int flowClassify);

    void confirm(Long id);

    void deleteTaskAndFlowDataById(Long id);

    FlowMonthWashTaskDTO getById(Long id);

    int updateReportConsumeStatusById(UpdateReportConsumeStatusRequest request);

    void reWash(Long fmwcId);

    int updateWashStatusById(Long id, Integer washStatus);

    List<FlowMonthWashTaskDTO> getByControlId(Long fmwcId);
}
