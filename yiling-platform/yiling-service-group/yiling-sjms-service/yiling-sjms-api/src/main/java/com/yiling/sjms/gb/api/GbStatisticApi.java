package com.yiling.sjms.gb.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.gb.dto.StatisticDTO;
import com.yiling.sjms.gb.dto.request.GbFormStatisticPageRequest;

/**
 * 团购统计
 *
 * @author: wei.wang
 * @date: 2023/02/14
 */
public interface GbStatisticApi {

    /**
     * 获取团购统计信息
     * @param request
     * @return
     */
    Page<StatisticDTO> getStatistic(GbFormStatisticPageRequest request);

    /**
     * 统计日表单数据
     */
    void gBStatisticProgram();
}
