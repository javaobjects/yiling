package com.yiling.dataflow.backup.api.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.yiling.dataflow.backup.api.AgencyBackupApi;
import com.yiling.dataflow.backup.dto.request.AgencyBackRequest;
import com.yiling.dataflow.backup.dto.request.CheckAgencyBackupRequest;
import com.yiling.dataflow.backup.service.AgencyBackupService;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.framework.common.log.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 */
@DubboService
@Slf4j
public class AgencyBackupApiImpl implements AgencyBackupApi {
    @Resource
    private AgencyBackupService agencyBackupService;
    @Resource
    private FlowMonthWashControlApi flowMonthWashControlApi;

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void agencyInfoBackup(AgencyBackRequest request) {
        //备份月计算
        DateTime lastMonth = DateUtil.offsetMonth(new Date(), request.getOffsetMonth());
        // yyyy-MM
        String month = DateUtil.format(lastMonth, "yyyy");
        String year = DateUtil.format(lastMonth, "MM");
        FlowMonthWashControlDTO byYearAndMonth = flowMonthWashControlApi.getByYearAndMonth(Integer.valueOf(year), Integer.valueOf(month));
        //流向清洗日程是否开始/**
        // * 日程状态：1未开始2已备份3已生成任务4结束
        // 已经结束的流向清洗日程已经结束的不能在重新开始清洗，不能在做备份
        //第一期上线暂时不启动此判断逻辑
//        if (Objects.nonNull(byYearAndMonth) && byYearAndMonth.getStatus().intValue() >= 2) {
//            log.info("备份的月份的流向清洗已经进行中或已经结束");
//        }

        boolean b = agencyBackupService.agencyEsbInfoBackUp(request);
    }
    @Override
    public void esbInfoBackup(AgencyBackRequest request){
        agencyBackupService.esbInfoBackup(request);
    }
    @Override
    public boolean checkAgencyBackupByYearMonth(CheckAgencyBackupRequest request) {
        return agencyBackupService.checkAgencyBackupByYearMonth(request);
    }

}
