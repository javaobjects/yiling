package com.yiling.open.erp;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsTypeEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.BaseTest;
import com.yiling.open.config.ErpOpenConfig;
import com.yiling.open.erp.api.impl.ErpDataStatApiImpl;
import com.yiling.open.erp.bo.ErpInstallEmployeeInfoDetailBO;
import com.yiling.open.erp.bo.ErpMonitorCountInfoDetailBO;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.ErpGoodsBatchDTO;
import com.yiling.open.erp.entity.ErpDataStatDO;
import com.yiling.open.erp.entity.ErpSyncStatDO;
import com.yiling.open.erp.enums.ErpMonitorCountReminderTypeEnum;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpDataStatService;
import com.yiling.open.erp.util.ErpDataStatCacheUtils;
import com.yiling.open.erp.util.OpenStringUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2021/9/2
 */
@Slf4j
public class ErpDataStatServiceTest extends BaseTest {

    @Autowired
    private ErpDataStatService erpDataStatService;
    @Autowired
    private ErpClientService erpClientService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ErpOpenConfig erpOpenConfig;

    @Autowired
    private ErpDataStatApiImpl erpDataStatApiImpl;

    @DubboReference
    private DictApi dictApi;
    @DubboReference
    private SmsApi smsApi;

//    @Value("${common.open.monitorCount:'[]'}")
//    private String monitorCountInfo;
//
//    @Value("${common.open.installEmployeeInfo:'[]'}")
//    private String installEmployeeInfo;

    private static final String name = "erp_monitor_count";

    @Test
    public void Test1() {
        ErpGoodsBatchDTO erpGoodsBatchDTO=new ErpGoodsBatchDTO();
        erpGoodsBatchDTO.setSuId(1L);
        erpDataStatService.sendDataStat(erpGoodsBatchDTO);
    }

    @Test
    public void Test2() {
        ErpDataStatDO erpDataStat = new ErpDataStatDO();
        erpDataStat.setSuId(1L);
        erpDataStat.setTaskNo("40000007");
        erpDataStat.setCreateTime(new Date());
        erpDataStat.setOperType(1);

        Calendar calendar = Calendar.getInstance();
        erpDataStat.setStatDate(DateUtil.parse("2022-04-25","yyyy-MM-dd"));
        erpDataStat.setStatHour(calendar.get(Calendar.HOUR_OF_DAY));


        String key = ErpDataStatCacheUtils.getKey(erpDataStat);
        ErpSyncStatDO erpSyncStat = PojoUtils.map(erpDataStat, ErpSyncStatDO.class);
        erpSyncStat.setAddNum(10000);
        erpSyncStat.setUpdateNum(10000);
        erpSyncStat.setDeleteNum(10000);

        List<ErpClientDTO> erpClientDOList = erpClientService.selectBySuId(erpDataStat.getSuId());
        List<ErpClientDTO> erpClientDOS = erpClientDOList.stream().filter(e -> e.getMonitorStatus().equals(1)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(erpClientDOS)) {
//            String monitorCountInfo = "[{'taskNo':'10000001','monitorCount':1000},{'taskNo':'10000002','monitorCount':1000},{'taskNo':'10000003','monitorCount':5000},{'taskNo':'10000004','monitorCount':1000},{'taskNo':'10000007','monitorCount':5000},{'taskNo':'10000009','monitorCount':1000},{'taskNo':'10000010','monitorCount':1000},{'taskNo':'40000005','monitorCount':100},{'taskNo':'40000006','monitorCount':10},{'taskNo':'40000007','monitorCount':10}]";
//            if(StrUtil.isBlank(monitorCountInfo)){
//                return;
//            }
            List<ErpMonitorCountInfoDetailBO> monitorCountInfoList = erpOpenConfig.getErpMonitorCountInfoDetail();
            if(CollUtil.isNotEmpty(monitorCountInfoList)){
                boolean exceedCountFlag = false;
                for (ErpMonitorCountInfoDetailBO erpMonitorCountInfo : monitorCountInfoList) {
                    if (ObjectUtil.equal(erpMonitorCountInfo.getTaskNo(), erpDataStat.getTaskNo())) {
                        Integer count = erpSyncStat.getAddNum() + erpSyncStat.getUpdateNum() + erpSyncStat.getDeleteNum();
                        if (erpMonitorCountInfo.getMonitorCount() < count) {
                            exceedCountFlag = true;
                        }
                    }
                }
                // 统计时间的监控达到阈值，给实施负责人发送短信
                if(erpOpenConfig.getMonitorSmsFlag() && exceedCountFlag){
                    erpMonitorReminderSendSms(key, erpSyncStat, erpClientDOS);
                }
            }
        }


    }
    /**
     * ERP商业公司监控达到阈值发送提醒短信
     *
     * @param key 统计请求次数的key
     * @param erpSyncStat 统计信息
     * @param erpClientDOS erp对接信息
     */
    private void erpMonitorReminderSendSms(String key, ErpSyncStatDO erpSyncStat, List<ErpClientDTO> erpClientDOS) {
//        String installEmployeeInfo = "[{'name':'张三','mobileList':['17620358091','17620358092']},{'name':'李四','mobileList':['17620358092']}]";
//        if(StrUtil.isBlank(installEmployeeInfo)){
//            log.info("erpDataStatService，实施负责人手机号信息配置为空");
//            return;
//        }
        List<ErpInstallEmployeeInfoDetailBO> erpInstallEmployeeInfoList = erpOpenConfig.getErpInstallEmployeeInfoDetail();
        if(CollUtil.isEmpty(erpInstallEmployeeInfoList)){
            log.warn("erpDataStatService，实施负责人手机号信息配置为空");
            return;
        }
        // 实施负责人姓名对应的手机号
        Map<String, List<String>> installEmployeeMap = new HashMap<>();
        for (ErpInstallEmployeeInfoDetailBO erpInstallEmployeeInfoBO : erpInstallEmployeeInfoList) {
            List<String> mobileList = erpInstallEmployeeInfoBO.getMobileList();
            if(CollUtil.isEmpty(mobileList)){
                continue;
            }
            mobileList = mobileList.stream().distinct().collect(Collectors.toList());
            installEmployeeMap.put(OpenStringUtils.clearAllSpace(erpInstallEmployeeInfoBO.getName()), mobileList);
        }
        // 实施负责人姓名对应的商业公司
        Map<String,String> enterpriseNameMap = new HashMap<>();
        Map<String, List<ErpClientDTO>> map = erpClientDOS.stream().collect(Collectors.groupingBy(ErpClientDTO::getInstallEmployee));
        for (Map.Entry<String, List<ErpClientDTO>> entry : map.entrySet()) {
            String installEmployeeName = OpenStringUtils.clearAllSpace(entry.getKey());
            StringJoiner enterpriseNames = new StringJoiner("、");
            entry.getValue().forEach(e -> {
                enterpriseNames.add(e.getClientName());
            });
            enterpriseNameMap.put(installEmployeeName, enterpriseNames.toString());
        }
        // 监控统计时间
        String day = DateUtil.format(erpSyncStat.getStatDate(), "yyyy-MM-dd");
        String hour = erpSyncStat.getStatHour().toString();
        String reminderTime = day.concat("日").concat(hour).concat("时");
        // 【以岭药业】商业公司{}，在{}达到了阈值，请及时处理
        if(MapUtil.isEmpty(enterpriseNameMap)){
            return;
        }

        Set<String> mobileSet = new HashSet<>();
        try {
            for (Map.Entry<String, String> entry : enterpriseNameMap.entrySet()) {
                String installEmployeeName = entry.getKey();
                List<String> mobileList = installEmployeeMap.get(installEmployeeName);
                if(CollUtil.isEmpty(mobileList)){
                    log.info("erpDataStatService，实施负责人[{}]手机号为空", installEmployeeName);
                    continue;
                }
                for (String mobile : mobileList) {
                    mobileSet.add(mobile);
                }
            }

            if(CollUtil.isNotEmpty(mobileSet)){
                String content = StrFormatter.format(ErpMonitorCountReminderTypeEnum.MONITOR_REMINDER.getTemplateContent(), reminderTime, erpSyncStat.getTaskNo());
                for (String mobile : mobileSet) {
                    // 是否已发送短信
                    String smsKey = ErpDataStatCacheUtils.getSmsKey(erpSyncStat,mobile);
                    String sendMobile = (String)stringRedisTemplate.opsForValue().get(smsKey);
                    if (StrUtil.isNotBlank(sendMobile)) {
                        continue;
                    }
                    boolean result = smsApi.send(mobile, content, SmsTypeEnum.MONITOR_REMINDER);
                    log.info("erpDataStatService，ERP商业公司监控达到阈值提醒发送短信完成，短信内容：{}，发送结果：{}" , content , result);
                    if(result){
                        // 发送完成，加入缓存已发送，当前统计时间范围内每个手机号只发送一次
                        stringRedisTemplate.opsForValue().set(smsKey, content, 1, TimeUnit.HOURS);
                    }

                }
            }
        }catch (Exception e){
            log.error("erpDataStatService，ERP商业公司监控达到阈值提醒发送短信异常：{}" , e.getMessage());
        }
    }

    @Test
    public void erpSyncStatFlowTest() {
        erpDataStatApiImpl.erpSyncStatFlow();
    }

    @Test
    public void test10() {
        Set<String> keySet = null;

        try {
            // 读取key集合
            keySet = stringRedisTemplate.opsForSet().members(ErpDataStatCacheUtils.STAT_SET_KEY);
        } catch (Exception e) {
            //            log.error(e.getMessage(), e);
        }

        for (String key : keySet) {
            try {
                // 不符合长度的key清理，新的key增加了部门编码，旧key没有部门编码需清理
                String[] arr = ErpDataStatCacheUtils.subKey(key);
                if (arr.length < 5) {
                    stringRedisTemplate.opsForSet().remove(ErpDataStatCacheUtils.STAT_SET_KEY, key);
                    stringRedisTemplate.delete(key);
                    continue;
                }

                Map<Object, Object> statMap = stringRedisTemplate.opsForHash().entries(key);

                if (statMap != null && statMap.size() > 0) {
                    ErpSyncStatDO erpSyncStat = ErpDataStatCacheUtils.getErpSyncStat(key, statMap);
//                                        erpSyncStatMapper.save(erpSyncStat);
                }

                // 移除集合中已过期的key
                if (ErpDataStatCacheUtils.hasExpired(key)) {
                    stringRedisTemplate.opsForSet().remove(ErpDataStatCacheUtils.STAT_SET_KEY, key);
                    stringRedisTemplate.delete(key);
                }
            } catch (Exception e) {
                //                log.error("统计数据持久化失败", e);
            }
        }
    }

    @Test
    public void erpSyncLastestCollectAndFlowDateTest() {
        erpDataStatApiImpl.erpSyncLastestCollectDate();
    }

    @Test
    public void erpSyncLastestFlowDateTest() {
        erpDataStatApiImpl.erpSyncLastestFlowDate();
    }

    @Test
    public void flowDirectConnectMonitorTest() {
        erpDataStatApiImpl.flowDirectConnectMonitor();
    }

}
