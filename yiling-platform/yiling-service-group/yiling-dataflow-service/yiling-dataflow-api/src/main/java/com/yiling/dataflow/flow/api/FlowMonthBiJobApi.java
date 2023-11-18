package com.yiling.dataflow.flow.api;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.flow.dto.FlowMonthBiJobDTO;

/**
 * @author fucheng.bai
 * @date 2022/8/5
 */
public interface FlowMonthBiJobApi {

    /**
     * 根据日期获取列表
     * @param date
     * @return
     */
    List<FlowMonthBiJobDTO> listByDate(Date date);

    Long save(FlowMonthBiJobDTO flowMonthBiJobDTO);
}
