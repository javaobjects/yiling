package com.yiling.bi.order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.bi.BaseTest;
import com.yiling.bi.order.entity.BiOrderBackupDO;
import com.yiling.bi.order.service.BiOrderBackupService;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2022/9/26
 */
@Slf4j
public class BiOrderBackupTest extends BaseTest {

    @Autowired
    public BiOrderBackupService biOrderBackupService;

    @Test
    public void backupBiOrderDataTest() {
        biOrderBackupService.backupBiOrderData();
    }

    @Test
    public void getNoMatchedBiOrderBackupTest() {
        Date endDate = null;
        try {
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2022-08-01");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        List<Long> ids = Arrays.asList(98L,29L,32L, 19L, 120L, 28L, 48L);

        List<BiOrderBackupDO> biOrderBackupDOList = biOrderBackupService.getNoMatchedBiOrderBackup(endDate, ids);
        Map<String, List<BiOrderBackupDO>> biOrderBackupMap = biOrderBackupDOList.stream().collect(Collectors.groupingBy(b -> b.getSellerEname().trim() + "_" + b.getBuyerEname().trim()));

    }

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date monthDate = null;
        try {
            monthDate = sdf.parse("2022-07");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Date date = DateUtil.offsetMonth(monthDate, 1);
        System.out.println(date);
    }
}
