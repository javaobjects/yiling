package com.yiling.sjms.gb;
import java.util.Date;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.BaseTest;

import com.yiling.sjms.gb.dto.StatisticDTO;
import com.yiling.sjms.gb.dto.request.GbFormStatisticPageRequest;
import com.yiling.sjms.gb.service.GbStatisticService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GbStatistic extends BaseTest {

    @Autowired
    GbStatisticService gbStatisticService;

    @Test
    public void gBStatisticProgram() {
        gbStatisticService.gBStatisticProgram();

    }
    @Test
    public void  getStatistic(){
        GbFormStatisticPageRequest request = new GbFormStatisticPageRequest();
        request.setStartTime(DateUtil.parse("2022-12-20"));
        request.setEndTime(DateUtil.parse("2023-12-20"));
        request.setStartMonthTime("2022-12-20");
        request.setEndMonthTime("2023-12-20");
        request.setProvinceName("甘青");
        request.setCode(15l);
        request.setType(4);
        Page<StatisticDTO> statistic = gbStatisticService.getStatistic(request);
        System.out.println(statistic);

    }
}
