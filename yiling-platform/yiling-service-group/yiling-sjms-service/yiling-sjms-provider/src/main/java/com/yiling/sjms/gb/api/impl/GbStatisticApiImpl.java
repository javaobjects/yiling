package com.yiling.sjms.gb.api.impl;


import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.gb.api.GbStatisticApi;
import com.yiling.sjms.gb.dto.StatisticDTO;
import com.yiling.sjms.gb.dto.request.GbFormStatisticPageRequest;
import com.yiling.sjms.gb.service.GbStatisticService;

/**
 * 团购统计信息
 *
 * @author: wei.wang
 * @date: 2023/02/14
 */
@DubboService
public class GbStatisticApiImpl implements GbStatisticApi {

    @Autowired
    private GbStatisticService gbStatisticService;

    @Override
    public Page<StatisticDTO> getStatistic(GbFormStatisticPageRequest request) {
        return gbStatisticService.getStatistic(request);
    }

    @Override
    public void gBStatisticProgram() {
        gbStatisticService.gBStatisticProgram();
    }
}
