package com.yiling.bi.order.api.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import com.yiling.bi.order.api.BiOrderBackupJobApi;
import com.yiling.bi.order.entity.BiOrderBackupDO;
import com.yiling.bi.order.entity.BiOrderBackupTaskDO;
import com.yiling.bi.order.entity.OdsOrderDetailDO;
import com.yiling.bi.order.enums.BiTaskEnums;
import com.yiling.bi.order.service.BiOrderBackupService;
import com.yiling.bi.order.service.BiOrderBackupTaskService;
import com.yiling.bi.order.service.OdsOrderDetailService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2022/9/16
 */
@DubboService
@Slf4j
public class BiOrderBackupJobApiImpl implements BiOrderBackupJobApi {

    @Autowired
    private BiOrderBackupService biOrderBackupService;


    @Override
    @Async("asyncExecutor")
    public void backupBiOrderData() {
        biOrderBackupService.backupBiOrderData();
    }

}
