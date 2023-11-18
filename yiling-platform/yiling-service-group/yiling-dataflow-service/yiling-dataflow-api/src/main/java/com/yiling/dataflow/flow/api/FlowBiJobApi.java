package com.yiling.dataflow.flow.api;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.flow.dto.FlowBiJobDTO;

/**
 * @author: shuang.zhang
 * @date: 2022/4/7
 */
public interface FlowBiJobApi {

    /**
     * 执行流向导入到bi系统任务
     */
    void excelFlowBiTask();

    /**
     * 月流向导入到bi系统任务
     */
    void excelMonthFlowBiTask();

    /**
     * 根据日期获取列表
     * @param date
     * @return
     */
    List<FlowBiJobDTO> listByDate(Date date);

    Long save(FlowBiJobDTO flowBiJobDTO);
}
