package com.yiling.open.erp.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.yiling.open.erp.entity.ErpDataStatDO;
import com.yiling.open.erp.entity.ErpSyncStatDO;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @Description:
 * @Author: shuang.zhang
 * @Date: 2018/10/10
 */
public class ErpDataStatCacheUtils {

    public static final String STAT_SET_KEY = "erpsyncstat_set";
    public static final String ADD_NUM_FIELD = "add_num";
    public static final String UPDATE_NUM_FIELD = "update_num";
    public static final String DELETE_NUM_FIELD = "delete_num";

    private ErpDataStatCacheUtils() {
    }

    /**
     * key = erpsyncstat_taskNo:suId:statDate:statHour
     * @param erpDataStat
     * @return
     */
    public static String getKey(ErpDataStatDO erpDataStat) {
        Objects.requireNonNull(erpDataStat);

        if (StrUtil.isBlank(erpDataStat.getSuDeptNo())) {
            erpDataStat.setSuDeptNo("");
        }

        // key = erpsyncstat_taskNo:suId:statDate:statHour
        StringBuilder sb = new StringBuilder();
        sb.append("erpsyncstat_").append(erpDataStat.getTaskNo())
                .append(":").append(erpDataStat.getSuId())
                .append(":").append(erpDataStat.getSuDeptNo())
                .append(":").append(DateUtil.formatDate(erpDataStat.getStatDate()))
                .append(":").append(erpDataStat.getStatHour());

        return sb.toString();
    }

    public static ErpSyncStatDO getErpSyncStat(String key, Map<Object, Object> statMap) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(statMap);

        String[] arr = subKey(key);
        if (arr.length < 5) {
            throw new IllegalArgumentException("key不合法，key=" + key);
        }

        ErpSyncStatDO erpSyncStat = new ErpSyncStatDO();
        erpSyncStat.setTaskNo(arr[0]);
        erpSyncStat.setSuId(Long.valueOf(arr[1]));
        erpSyncStat.setSuDeptNo(arr[2]);
        erpSyncStat.setStatDate(DateUtil.parse(arr[3],"yyyy-MM-dd"));
        erpSyncStat.setStatHour(Integer.valueOf(arr[4]));
        erpSyncStat.setAddNum(Integer.valueOf(statMap.get(ADD_NUM_FIELD) == null ? "0" : String.valueOf(statMap.get(ADD_NUM_FIELD))));
        erpSyncStat.setUpdateNum(Integer.valueOf(statMap.get(UPDATE_NUM_FIELD) == null ? "0" : String.valueOf(statMap.get(UPDATE_NUM_FIELD))));
        erpSyncStat.setDeleteNum(Integer.valueOf(statMap.get(DELETE_NUM_FIELD) == null ? "0" : String.valueOf(statMap.get(DELETE_NUM_FIELD))));

        return erpSyncStat;
    }

    public static boolean hasExpired(String key) {
        Objects.requireNonNull(key);

        String[] arr = subKey(key);
        if (arr.length < 5) {
            throw new IllegalArgumentException("key不合法，key=" + key);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.parse(arr[3],"yyyy-MM-dd"));
        calendar.add(Calendar.HOUR_OF_DAY, Integer.valueOf(arr[4]) + 1);

        Date now = new Date();

        return now.after(calendar.getTime());
    }

    /**
     * key = sms:erpMonitorCount:taskNo:suId_statDate_statHour:mobile
     *
     * @param erpDataStat
     * @return
     */
    public static String getSmsKey(ErpSyncStatDO erpDataStat, String mobile) {
        Objects.requireNonNull(erpDataStat);

        StringBuilder sb = new StringBuilder();
        sb.append("sms")
            .append(":").append("expiration_reminder")
            .append(":").append("erpMonitorCount")
            .append(":").append(erpDataStat.getTaskNo())
            .append("_").append(DateUtil.formatDate(erpDataStat.getStatDate()))
            .append("_").append(erpDataStat.getStatHour())
            .append(":").append(mobile);
        return sb.toString();
    }

    public static String[] subKey(String key) {
        String[] arr = key.substring(key.indexOf("_") + 1).split(":");
        return arr;
    }
}
