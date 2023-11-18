package com.yiling.open.erp.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsTypeEnum;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseByNamePageListRequest;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.bo.CrmEnterprisePartBO;
import com.yiling.dataflow.flowcollect.api.FlowCollectDataStatisticsApi;
import com.yiling.dataflow.flowcollect.api.FlowCollectHeartStatisticsApi;
import com.yiling.dataflow.flowcollect.api.FlowCollectSummaryStatisticsApi;
import com.yiling.dataflow.flowcollect.dto.FlowCollectDataStatisticsDTO;
import com.yiling.dataflow.flowcollect.dto.FlowCollectHeartStatisticsDTO;
import com.yiling.dataflow.flowcollect.dto.FlowCollectHeartSummaryStatisticsDTO;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectDataStatisticsDetailRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectDataStatisticsRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartStatisticsDetailRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartStatisticsRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartSummaryStatisticsDetailRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartSummaryStatisticsRequest;
import com.yiling.dataflow.order.api.FlowGoodsBatchDetailApi;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.dataflow.order.bo.FlowStatisticsBO;
import com.yiling.dataflow.order.dto.request.QueryFlowStatisticesRequest;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.config.ErpOpenConfig;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.bo.ErpInstallEmployeeInfoDetailBO;
import com.yiling.open.erp.dao.ErpSyncStatMapper;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpSyncStatPageRequest;
import com.yiling.open.erp.entity.ErpClientDO;
import com.yiling.open.erp.entity.ErpDataStatDO;
import com.yiling.open.erp.entity.ErpSyncStatDO;
import com.yiling.open.erp.enums.ClientFlowModeEnum;
import com.yiling.open.erp.enums.ErpMonitorCountReminderTypeEnum;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpDataStatService;
import com.yiling.open.erp.service.LocalMessageQueueService;
import com.yiling.open.erp.util.ErpDataStatCacheUtils;
import com.yiling.open.erp.util.OpenStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: qi.xiong
 * @Date: 2018/10/9
 */
@Service("erpDataStatService")
@Slf4j
public class ErpDataStatServiceImpl implements ErpDataStatService {

    @Autowired
    private ErpSyncStatMapper                       erpSyncStatMapper;
    @Resource
    private StringRedisTemplate                     stringRedisTemplate;
    @Autowired
    private LocalMessageQueueService<ErpDataStatDO> localMessageQueueService;
    @Autowired
    private ErpClientService                        erpClientService;
    @Autowired
    private ErpOpenConfig                           erpOpenConfig;
    @DubboReference
    private DictApi                                 dictApi;
    @DubboReference
    private SmsApi                                  smsApi;
    @DubboReference(timeout = 1000 * 60)
    private FlowCollectHeartStatisticsApi           flowCollectHeartStatisticsApi;
    @DubboReference(timeout = 1000 * 60)
    private FlowCollectDataStatisticsApi            flowCollectDataStatisticsApi;
    @DubboReference(timeout = 1000 * 60)
    private FlowCollectSummaryStatisticsApi         flowCollectSummaryStatisticsApi;
    @DubboReference(timeout = 1000 * 60)
    private FlowSaleApi                             flowSaleApi;
    @DubboReference(timeout = 1000 * 60)
    private FlowPurchaseApi                         flowPurchaseApi;
    @DubboReference(timeout = 1000 * 60)
    private FlowGoodsBatchDetailApi                 flowGoodsBatchDetailApi;
    @DubboReference(timeout = 1000 * 60)
    private CrmEnterpriseApi                        crmEnterpriseApi;

    private ExecutorService executorService;

    private static final String name = "erp_monitor_count";

    public void dataStat(ErpDataStatDO erpDataStat) {
//        while (true) {
        try {
//                ErpDataStatDO erpDataStat = localMessageQueueService.take();

            // 数据统计表hash key, 每个key对应统计表中的一条记录
            String key = ErpDataStatCacheUtils.getKey(erpDataStat);

            // 缓存所有的hash key到set中，用于后续定时任务操作
            if (!stringRedisTemplate.opsForSet().isMember(ErpDataStatCacheUtils.STAT_SET_KEY, key)) {
                stringRedisTemplate.opsForSet().add(ErpDataStatCacheUtils.STAT_SET_KEY, key);
            }

            if (Objects.equals(erpDataStat.getOperType(), 1)) {
                stringRedisTemplate.opsForHash().increment(key, ErpDataStatCacheUtils.ADD_NUM_FIELD, 1);
            } else if (Objects.equals(erpDataStat.getOperType(), 2)) {
                stringRedisTemplate.opsForHash().increment(key, ErpDataStatCacheUtils.UPDATE_NUM_FIELD, 1);
            } else if (Objects.equals(erpDataStat.getOperType(), 3)) {
                stringRedisTemplate.opsForHash().increment(key, ErpDataStatCacheUtils.DELETE_NUM_FIELD, 1);
            }

            if (stringRedisTemplate.getExpire(key, TimeUnit.SECONDS) == -1L) {
                // 设置key的过期时间
                Date date = DateUtil.parse(DateUtil.format(new Date(), "yyyy-MM-dd HH"), "yyyy-MM-dd HH");
                stringRedisTemplate.expire(key, date.getTime() + (1000 * 60 * 90), TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            log.error("缓存数据统计失败", e);
        }
//        }
    }

    @Override
    public boolean sendDataStat(BaseErpEntity obj) {
        Objects.requireNonNull(obj);
        // 赋值suId, operType
        try {
            ErpDataStatDO erpDataStat = PojoUtils.map(obj, ErpDataStatDO.class);
            if (StringUtils.isEmpty(erpDataStat.getTaskNo())) {
                throw new IllegalArgumentException("taskNo为空");
            }
            Calendar calendar = Calendar.getInstance();
            erpDataStat.setStatDate(calendar.getTime());
            erpDataStat.setStatHour(calendar.get(Calendar.HOUR_OF_DAY));

            //获取redis里面统计的数量
//            String key = ErpDataStatCacheUtils.getKey(erpDataStat);
//            Map<Object, Object> statMap = stringRedisTemplate.opsForHash().entries(key);
//            ErpSyncStatDO erpSyncStat = ErpDataStatCacheUtils.getErpSyncStat(key, statMap);
//            List<ErpClientDTO> erpClientDOList = erpClientService.selectBySuId(erpDataStat.getSuId());
//            List<ErpClientDTO> erpClientDOS = erpClientDOList.stream().filter(e -> e.getClientStatus().equals(1) && e.getSyncStatus().equals(1) && e.getMonitorStatus().equals(1)).collect(Collectors.toList());
//            if (CollUtil.isNotEmpty(erpClientDOS)) {
//                List<ErpMonitorCountInfoDetailBO> monitorCountInfoList = erpOpenConfig.getErpMonitorCountInfoDetail();
//                if(CollUtil.isNotEmpty(monitorCountInfoList)){
//                    boolean exceedCountFlag = false;
//                    for (ErpMonitorCountInfoDetailBO erpMonitorCountInfo : monitorCountInfoList) {
//                        if (ObjectUtil.equal(erpMonitorCountInfo.getTaskNo(), erpDataStat.getTaskNo())) {
//                            Integer count = erpSyncStat.getAddNum() + erpSyncStat.getUpdateNum() + erpSyncStat.getDeleteNum();
//                            if (erpMonitorCountInfo.getMonitorCount() < count) {
//                                exceedCountFlag = true;
//                            }
//                        }
//                    }
//                    // 统计时间的监控达到阈值，给实施负责人发送短信
//                    if(erpOpenConfig.getMonitorSmsFlag() && exceedCountFlag){
//                        erpMonitorReminderSendSms(key, erpSyncStat, erpClientDOS);
//                    }
//                }
//            }
            this.dataStat(erpDataStat);
            return true;
        } catch (Exception e) {
            log.error("统计消息添加失败", e);
        }
        return false;
    }

    @Override
    public void saveDataStat() {
        log.info("读取缓存统计数据，刷新到数据库，执行开始...");
        Set<String> keySet = null;

        try {
            // 读取key集合
            keySet = stringRedisTemplate.opsForSet().members(ErpDataStatCacheUtils.STAT_SET_KEY);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        if (CollectionUtils.isEmpty(keySet)) {
            log.info("当前缓存统计数据为空，执行结束...");
            return;
        }

        for (String key : keySet) {
            try {
                // 不符合长度的key清理，新的key增加了部门编码，旧key没有部门编码需清理
                String[] arr = ErpDataStatCacheUtils.subKey(key);
                if (arr.length < 5) {
                    deleteByKey(key);
                    continue;
                }

                Map<Object, Object> statMap = stringRedisTemplate.opsForHash().entries(key);

                if (statMap != null && statMap.size() > 0) {
                    ErpSyncStatDO erpSyncStat = ErpDataStatCacheUtils.getErpSyncStat(key, statMap);
                    erpSyncStatMapper.save(erpSyncStat);
                }

                // 移除集合中已过期的key
                if (ErpDataStatCacheUtils.hasExpired(key)) {
                    deleteByKey(key);
                }
            } catch (Exception e) {
                log.error("统计数据持久化失败", e);
            }
        }
        log.info("读取缓存统计数据，刷新到数据库，执行结束...");
    }

    /**
     * 根据key删除缓存
     *
     * @param key
     */
    private void deleteByKey(String key) {
        stringRedisTemplate.opsForSet().remove(ErpDataStatCacheUtils.STAT_SET_KEY, key);
        stringRedisTemplate.delete(key);
    }

    @Override
    public Page<ErpSyncStatDO> page(ErpSyncStatPageRequest request) {
        Page<ErpSyncStatDO> page = new Page<>(request.getCurrent(), request.getSize());
        return erpSyncStatMapper.page(page, request);
    }

    @Override
    public List<ErpSyncStatDO> getOneBySuidAndSuDeptNoAndStatDate(Long suId, String suDeptNo, String statDate, List<String> taskNoList) {
        return this.erpSyncStatMapper.getOneBySuidAndSuDeptNoAndStatDate(suId, suDeptNo, statDate, taskNoList);
    }

    @Override
    public List<ErpSyncStatDO> listBySuidAndSuDeptNoAndStatDateAndTaskNoList(Long suId, String suDeptNo, String statDateStart, String statDateEnd, List<String> taskNoList) {
        return this.erpSyncStatMapper.listBySuidAndSuDeptNoAndStatDateAndTaskNoList(suId, suDeptNo, statDateStart, statDateEnd, taskNoList);
    }

    @Override
    public Date getMaxTaskTimeByEid(Long suId, String suDeptNo, List<String> taskNoList) {
        return erpSyncStatMapper.getMaxTaskTimeByEid(suId, suDeptNo, taskNoList);
    }

    @Override
    public void flowCollectHeartStatistics() {
        Date startDateTime = DateUtil.offsetDay(new Date(), -30);
        Date endDateTime = DateUtil.offsetDay(new Date(), -1);

        List<Long> hasCrmIds = new ArrayList<>();
        //请求所有的crmEnterpriseIds集合 （1）打单商业是指商业公司档案中状态为“有效”的机构。（2）此页面按心跳进行统计，即当天有任一条数据回传记为有心跳。
        QueryCrmEnterpriseByNamePageListRequest request = new QueryCrmEnterpriseByNamePageListRequest();
        request.setSupplyChainRole(1);
        request.setBusinessCode(1);
        Page<CrmEnterprisePartBO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            page = crmEnterpriseApi.getCrmEnterprisePartInfoByName(request);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            List<SaveFlowCollectHeartStatisticsDetailRequest> flowCollectHeartStatisticsDetailDTOList = new ArrayList<>();
            List<SaveFlowCollectHeartSummaryStatisticsDetailRequest> flowCollectHeartSummaryStatisticsDetailDTOList = new ArrayList<>();

            List<SaveFlowCollectHeartStatisticsRequest> updateFlowCollectHeartStatisticsRequestDTOList = new ArrayList<>();
            List<SaveFlowCollectHeartSummaryStatisticsRequest> updateFlowCollectHeartSummaryStatisticsRequestDTOList = new ArrayList<>();
            List<Long> fchsIds = new ArrayList<>();
            List<Long> fchssIds = new ArrayList<>();
            List<Long> crmIds = page.getRecords().stream().map(e -> e.getId()).collect(Collectors.toList());
            //crmEnterpriseIds->id
            List<FlowCollectHeartStatisticsDTO> flowCollectHeartStatisticsDTOS = flowCollectHeartStatisticsApi.findListByCrmEnterpriseIds(crmIds);
            Map<Long, List<FlowCollectHeartStatisticsDTO>> flowCollectHeartStatisticsDTOMap = flowCollectHeartStatisticsDTOS.stream().collect(Collectors.groupingBy(FlowCollectHeartStatisticsDTO::getCrmEnterpriseId));

            List<FlowCollectHeartSummaryStatisticsDTO> flowCollectHeartSummaryStatisticsDTOS = flowCollectSummaryStatisticsApi.findListByCrmEnterpriseIds(crmIds);
            Map<Long, List<FlowCollectHeartSummaryStatisticsDTO>> flowCollectHeartSummaryStatisticsDTOMap = flowCollectHeartSummaryStatisticsDTOS.stream().collect(Collectors.groupingBy(FlowCollectHeartSummaryStatisticsDTO::getCrmEnterpriseId));

            //查询erpclient表获取eid
            List<ErpClientDO> erpClientDOList = erpClientService.getByCrmEnterpriseIdList(crmIds);
            erpClientDOList = erpClientDOList.stream().filter(e -> e.getCrmEnterpriseId() > 0).collect(Collectors.toList());
            Map<Long, List<ErpClientDO>> erpClientDOMap = erpClientDOList.stream().collect(Collectors.groupingBy(ErpClientDO::getCrmEnterpriseId));
            for (CrmEnterprisePartBO crmEnterprisePartBO : page.getRecords()) {
                Map<String, Integer> timeFlowStatus = new HashMap<>();
                List<ErpSyncStatDO> erpSyncStatDOListP = ListUtil.empty();
                List<ErpSyncStatDO> erpSyncStatDOListS = ListUtil.empty();
                List<ErpSyncStatDO> erpSyncStatDOListGB = ListUtil.empty();
                List<ErpClientDO> erpClientDOS = erpClientDOMap.get(crmEnterprisePartBO.getId());
                ErpClientDO erpClientDO = CollUtil.isNotEmpty(erpClientDOS) ? erpClientDOS.get(0) : null;
                if (CollUtil.isNotEmpty(erpClientDOS)) {
                    //查询erp_sync_date里面数据
                    erpSyncStatDOListP = erpSyncStatMapper.listBySuidAndSuDeptNoAndStatDateAndTaskNoList(erpClientDO.getSuId(), erpClientDO.getSuDeptNo(), DateUtil.format(startDateTime, "yyyy-MM-dd"), DateUtil.format(endDateTime, "yyyy-MM-dd"), ListUtil.toList(ErpTopicName.ErpPurchaseFlow.getMethod()));
                    erpSyncStatDOListS = erpSyncStatMapper.listBySuidAndSuDeptNoAndStatDateAndTaskNoList(erpClientDO.getSuId(), erpClientDO.getSuDeptNo(), DateUtil.format(startDateTime, "yyyy-MM-dd"), DateUtil.format(endDateTime, "yyyy-MM-dd"), ListUtil.toList(ErpTopicName.ErpSaleFlow.getMethod()));
                    erpSyncStatDOListGB = erpSyncStatMapper.listBySuidAndSuDeptNoAndStatDateAndTaskNoList(erpClientDO.getSuId(), erpClientDO.getSuDeptNo(), DateUtil.format(startDateTime, "yyyy-MM-dd"), DateUtil.format(endDateTime, "yyyy-MM-dd"), ListUtil.toList(ErpTopicName.ErpGoodsBatchFlow.getMethod()));
                }
                //采购有一项未上传 1是 2否
                Integer nearThreeStatus = 2;
                Integer nearFiveStatus = 2;
                Integer nearThreeAllStatus = 1;
                Integer nearFiveAllStatus = 1;
                List<FlowCollectHeartStatisticsDTO> flowCollectHeartStatisticsList = flowCollectHeartStatisticsDTOMap.get(crmEnterprisePartBO.getId());
                if (CollUtil.isEmpty(flowCollectHeartStatisticsList)) {
                    SaveFlowCollectHeartStatisticsRequest flowCollectHeartStatisticsP = new SaveFlowCollectHeartStatisticsRequest();
                    if (erpClientDO != null) {
                        flowCollectHeartStatisticsP.setDepthTime(erpClientDO.getDepthTime());
                        flowCollectHeartStatisticsP.setFlowMode(erpClientDO.getFlowMode());
                        flowCollectHeartStatisticsP.setInstallEmployee(erpClientDO.getInstallEmployee());
                    } else {
                        flowCollectHeartStatisticsP.setFlowMode(ClientFlowModeEnum.MANUAL.getCode());
                    }
                    flowCollectHeartStatisticsP.setCrmEnterpriseId(crmEnterprisePartBO.getId());
                    flowCollectHeartStatisticsP.setEname(crmEnterprisePartBO.getName());
                    flowCollectHeartStatisticsP.setProvinceCode(crmEnterprisePartBO.getProvinceCode());
                    flowCollectHeartStatisticsP.setStatisticsTime(new Date());
                    flowCollectHeartStatisticsP.setFlowType(1);
                    flowCollectHeartStatisticsApi.create(flowCollectHeartStatisticsP);
                    flowCollectHeartStatisticsP.setFlowType(2);
                    flowCollectHeartStatisticsApi.create(flowCollectHeartStatisticsP);
                    flowCollectHeartStatisticsP.setFlowType(3);
                    flowCollectHeartStatisticsApi.create(flowCollectHeartStatisticsP);
                    flowCollectHeartStatisticsList = flowCollectHeartStatisticsApi.findListByCrmEnterpriseIds(Arrays.asList(crmEnterprisePartBO.getId()));
                }

                List<FlowCollectHeartSummaryStatisticsDTO> flowCollectHeartSummaryStatisticsList = flowCollectHeartSummaryStatisticsDTOMap.get(crmEnterprisePartBO.getId());
                if (CollUtil.isEmpty(flowCollectHeartSummaryStatisticsList)) {
                    SaveFlowCollectHeartSummaryStatisticsRequest flowCollectSummaryHeartStatisticsP = new SaveFlowCollectHeartSummaryStatisticsRequest();
                    if (erpClientDO != null) {
                        flowCollectSummaryHeartStatisticsP.setDepthTime(erpClientDO.getDepthTime());
                        flowCollectSummaryHeartStatisticsP.setFlowMode(erpClientDO.getFlowMode());
                        flowCollectSummaryHeartStatisticsP.setInstallEmployee(erpClientDO.getInstallEmployee());
                    } else {
                        flowCollectSummaryHeartStatisticsP.setFlowMode(ClientFlowModeEnum.MANUAL.getCode());
                    }
                    flowCollectSummaryHeartStatisticsP.setCrmEnterpriseId(crmEnterprisePartBO.getId());
                    flowCollectSummaryHeartStatisticsP.setEname(crmEnterprisePartBO.getName());
                    flowCollectSummaryHeartStatisticsP.setProvinceCode(crmEnterprisePartBO.getProvinceCode());
                    flowCollectSummaryHeartStatisticsP.setStatisticsTime(new Date());
                    flowCollectSummaryStatisticsApi.create(flowCollectSummaryHeartStatisticsP);

                    flowCollectHeartSummaryStatisticsList = flowCollectSummaryStatisticsApi.findListByCrmEnterpriseIds(Arrays.asList(crmEnterprisePartBO.getId()));
                }

                for (FlowCollectHeartStatisticsDTO flowCollectHeartStatisticsDTO : flowCollectHeartStatisticsList) {
                    if (flowCollectHeartStatisticsDTO.getFlowType() == 1) {
                        StringBuffer sign = getHeartStringBuffer(timeFlowStatus, flowCollectHeartStatisticsDetailDTOList, erpClientDO, erpSyncStatDOListP, flowCollectHeartStatisticsDTO);
                        String signStr3 = sign.toString().substring(0, 3);
                        //1未上传2已上传 日销售/日采购/日库存有一项未上传的
                        if (nearThreeStatus == 2 && signStr3.contains("1")) {
                            nearThreeStatus = 1;
                        }

                        String signStr5 = sign.toString().substring(0, 5);
                        if (nearFiveStatus == 2 && signStr5.contains("1")) {
                            nearFiveStatus = 1;
                        }

                        //近3天一次请求都没有
                        if (nearThreeAllStatus == 1 && !signStr3.equals("111")) {
                            nearThreeAllStatus = 2;
                        }
                        //近5天一次请求都没有
                        if (nearFiveAllStatus == 1 && !signStr5.equals("11111")) {
                            nearFiveAllStatus = 2;
                        }
                    }

                    if (flowCollectHeartStatisticsDTO.getFlowType() == 2) {
                        StringBuffer sign = getHeartStringBuffer(timeFlowStatus, flowCollectHeartStatisticsDetailDTOList, erpClientDO, erpSyncStatDOListS, flowCollectHeartStatisticsDTO);

                        String signStr3 = sign.toString().substring(0, 3);
                        //1未上传2已上传 日销售/日采购/日库存有一项未上传的
                        if (nearThreeStatus == 2 && signStr3.contains("1")) {
                            nearThreeStatus = 1;
                        }

                        String signStr5 = sign.toString().substring(0, 5);
                        if (nearFiveStatus == 2 && signStr5.contains("1")) {
                            nearFiveStatus = 1;
                        }

                        //近3天一次请求都没有
                        if (nearThreeAllStatus == 1 && !signStr3.equals("111")) {
                            nearThreeAllStatus = 2;
                        }
                        //近5天一次请求都没有
                        if (nearFiveAllStatus == 1 && !signStr5.equals("11111")) {
                            nearFiveAllStatus = 2;
                        }
                    }

                    if (flowCollectHeartStatisticsDTO.getFlowType() == 3) {
                        StringBuffer sign = getHeartStringBuffer(timeFlowStatus, flowCollectHeartStatisticsDetailDTOList, erpClientDO, erpSyncStatDOListGB, flowCollectHeartStatisticsDTO);

                        String signStr3 = sign.toString().substring(0, 3);
                        //1未上传2已上传 日销售/日采购/日库存有一项未上传的
                        if (nearThreeStatus == 2 && signStr3.contains("1")) {
                            nearThreeStatus = 1;
                        }

                        String signStr5 = sign.toString().substring(0, 5);
                        if (nearFiveStatus == 2 && signStr5.contains("1")) {
                            nearFiveStatus = 1;
                        }

                        //近3天一次请求都没有
                        if (nearThreeAllStatus == 1 && !signStr3.equals("111")) {
                            nearThreeAllStatus = 2;
                        }
                        //近5天一次请求都没有
                        if (nearFiveAllStatus == 1 && !signStr5.equals("11111")) {
                            nearFiveAllStatus = 2;
                        }
                    }
                    fchsIds.add(flowCollectHeartStatisticsDTO.getId());
                }

                for (FlowCollectHeartSummaryStatisticsDTO flowCollectHeartSummaryStatisticsDTO : flowCollectHeartSummaryStatisticsList) {
                    for (int i = 1; i <= 30; i++) {
                        String time = DateUtil.format(DateUtil.offsetDay(new Date(), -i), "yyyy-MM-dd");
                        Integer status = timeFlowStatus.get(time);
                        SaveFlowCollectHeartSummaryStatisticsDetailRequest saveFlowCollectHeartSummaryStatisticsDetailRequest = new SaveFlowCollectHeartSummaryStatisticsDetailRequest();
                        saveFlowCollectHeartSummaryStatisticsDetailRequest.setFchssId(flowCollectHeartSummaryStatisticsDTO.getId());
                        saveFlowCollectHeartSummaryStatisticsDetailRequest.setFlowStatus(status == null ? 1 : status);
                        saveFlowCollectHeartSummaryStatisticsDetailRequest.setDateTime(DateUtil.parseDate(time));
                        flowCollectHeartSummaryStatisticsDetailDTOList.add(saveFlowCollectHeartSummaryStatisticsDetailRequest);
                        fchssIds.add(flowCollectHeartSummaryStatisticsDTO.getId());
                    }
                }

                //修改标记
                Integer finalNearFiveAllStatus = nearFiveAllStatus;
                Integer finalNearFiveStatus = nearFiveStatus;
                Integer finalNearThreeAllStatus = nearThreeAllStatus;
                Integer finalNearThreeStatus = nearThreeStatus;

                flowCollectHeartStatisticsList.stream().forEach(e -> {
                    SaveFlowCollectHeartStatisticsRequest flowCollectHeartStatistics = new SaveFlowCollectHeartStatisticsRequest();
                    flowCollectHeartStatistics.setId(e.getId());
                    flowCollectHeartStatistics.setNearFiveAllStatus(finalNearFiveAllStatus);
                    flowCollectHeartStatistics.setNearFiveStatus(finalNearFiveStatus);
                    flowCollectHeartStatistics.setNearThreeAllStatus(finalNearThreeAllStatus);
                    flowCollectHeartStatistics.setNearThreeStatus(finalNearThreeStatus);
                    updateFlowCollectHeartStatisticsRequestDTOList.add(flowCollectHeartStatistics);
                });

                flowCollectHeartSummaryStatisticsList.stream().forEach(e -> {
                    SaveFlowCollectHeartSummaryStatisticsRequest flowCollectHeartSummaryStatistics = new SaveFlowCollectHeartSummaryStatisticsRequest();
                    flowCollectHeartSummaryStatistics.setId(e.getId());
                    flowCollectHeartSummaryStatistics.setNearFiveAllStatus(finalNearFiveAllStatus);
                    flowCollectHeartSummaryStatistics.setNearThreeAllStatus(finalNearThreeAllStatus);
                    updateFlowCollectHeartSummaryStatisticsRequestDTOList.add(flowCollectHeartSummaryStatistics);
                });
                hasCrmIds.add(crmEnterprisePartBO.getId());
            }
            flowCollectHeartStatisticsApi.updateBatch(updateFlowCollectHeartStatisticsRequestDTOList);
            flowCollectHeartStatisticsApi.deleteDetailByFchIds(fchsIds);
            flowCollectHeartStatisticsApi.saveBatchDetail(flowCollectHeartStatisticsDetailDTOList);

            flowCollectSummaryStatisticsApi.updateBatch(updateFlowCollectHeartSummaryStatisticsRequestDTOList);
            flowCollectSummaryStatisticsApi.deleteDetailByFchIds(fchssIds);
            flowCollectSummaryStatisticsApi.saveBatchDetail(flowCollectHeartSummaryStatisticsDetailDTOList);
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));

        List<Long> crmIds = flowCollectHeartStatisticsApi.findCrmList();
        crmIds.removeAll(hasCrmIds);
        {
            List<FlowCollectHeartStatisticsDTO> heartStatisticsDTOList = flowCollectHeartStatisticsApi.findListByCrmEnterpriseIds(crmIds);
            List<Long> ids = heartStatisticsDTOList.stream().map(e -> e.getId()).collect(Collectors.toList());
            flowCollectHeartStatisticsApi.deleteDetailByFchIds(ids);
            flowCollectHeartStatisticsApi.deleteByIds(ids);
        }

        {
            List<FlowCollectHeartSummaryStatisticsDTO> heartSummaryStatisticsDTOList = flowCollectSummaryStatisticsApi.findListByCrmEnterpriseIds(crmIds);
            List<Long> ids = heartSummaryStatisticsDTOList.stream().map(e -> e.getId()).collect(Collectors.toList());
            flowCollectSummaryStatisticsApi.deleteDetailByFchIds(ids);
            flowCollectSummaryStatisticsApi.deleteByIds(ids);
        }
    }

    @Override
    public void flowCollectDataStatistics() {
        List<Long> hasCrmIds = new ArrayList<>();
        //请求所有的crmEnterpriseIds集合 （1）打单商业是指商业公司档案中状态为“有效”的机构。（2）此页面按心跳进行统计，即当天有任一条数据回传记为有心跳。
        QueryCrmEnterpriseByNamePageListRequest request = new QueryCrmEnterpriseByNamePageListRequest();
        request.setSupplyChainRole(1);
        request.setBusinessCode(1);
        Page<CrmEnterprisePartBO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            page = crmEnterpriseApi.getCrmEnterprisePartInfoByName(request);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            List<SaveFlowCollectDataStatisticsDetailRequest> flowCollectDataStatisticsDetailDTOList = new ArrayList<>();
            List<SaveFlowCollectDataStatisticsRequest> updateFlowCollectDataStatisticsRequestDTOList = new ArrayList<>();
            List<Long> fchsIds = new ArrayList<>();
            List<Long> crmIds = page.getRecords().stream().map(e -> e.getId()).collect(Collectors.toList());
            //crmEnterpriseIds->id
            List<FlowCollectDataStatisticsDTO> flowCollectHeartStatisticsDTOS = flowCollectDataStatisticsApi.findListByCrmEnterpriseIds(crmIds);
            Map<Long, List<FlowCollectDataStatisticsDTO>> flowCollectDataStatisticsDTOMap = flowCollectHeartStatisticsDTOS.stream().collect(Collectors.groupingBy(FlowCollectDataStatisticsDTO::getCrmEnterpriseId));
            //查询erpclient表获取eid
            QueryFlowStatisticesRequest queryFlowStatisticesRequest = new QueryFlowStatisticesRequest();
            queryFlowStatisticesRequest.setCrmEnterpriseIds(crmIds);
            List<FlowStatisticsBO> erpSyncStatDOListS = flowSaleApi.getFlowSaleStatistics(queryFlowStatisticesRequest);
            List<FlowStatisticsBO> erpSyncStatDOListp = flowPurchaseApi.getFlowPurchaseStatistics(queryFlowStatisticesRequest);
            List<FlowStatisticsBO> erpSyncStatDOListGB = flowGoodsBatchDetailApi.getFlowGoodsBatchStatistics(queryFlowStatisticesRequest);
            List<ErpClientDO> erpClientDOList = erpClientService.getByCrmEnterpriseIdList(crmIds);
            erpClientDOList = erpClientDOList.stream().filter(e -> e.getCrmEnterpriseId() > 0).collect(Collectors.toList());
            Map<Long, List<ErpClientDO>> erpClientDOMap = erpClientDOList.stream().collect(Collectors.groupingBy(ErpClientDO::getCrmEnterpriseId));
            for (CrmEnterprisePartBO crmEnterprisePartBO : page.getRecords()) {
                List<ErpClientDO> erpClientDOS = erpClientDOMap.get(crmEnterprisePartBO.getId());
                ErpClientDO erpClientDO = CollUtil.isNotEmpty(erpClientDOS) ? erpClientDOS.get(0) : null;

                //采购有一项未上传
                Integer nearThreeStatus = 1;
                Integer nearThreeAllStatus = 1;
                Integer nearFiveStatus = 1;
                Integer nearFiveAllStatus = 1;
                List<FlowCollectDataStatisticsDTO> flowCollectDataStatisticsList = flowCollectDataStatisticsDTOMap.get(crmEnterprisePartBO.getId());
                if (CollUtil.isEmpty(flowCollectDataStatisticsList)) {
                    SaveFlowCollectDataStatisticsRequest flowCollectDataStatisticsP = new SaveFlowCollectDataStatisticsRequest();
                    if (erpClientDO != null) {
                        flowCollectDataStatisticsP.setDepthTime(erpClientDO.getDepthTime());
                        flowCollectDataStatisticsP.setFlowMode(erpClientDO.getFlowMode());
                        flowCollectDataStatisticsP.setInstallEmployee(erpClientDO.getInstallEmployee());
                    } else {
                        flowCollectDataStatisticsP.setFlowMode(ClientFlowModeEnum.MANUAL.getCode());
                    }

                    flowCollectDataStatisticsP.setCrmEnterpriseId(crmEnterprisePartBO.getId());
                    flowCollectDataStatisticsP.setEname(crmEnterprisePartBO.getName());
                    flowCollectDataStatisticsP.setProvinceCode(crmEnterprisePartBO.getProvinceCode());
                    flowCollectDataStatisticsP.setStatisticsTime(new Date());
                    flowCollectDataStatisticsP.setFlowType(1);
                    flowCollectDataStatisticsApi.create(flowCollectDataStatisticsP);
                    flowCollectDataStatisticsP.setFlowType(2);
                    flowCollectDataStatisticsApi.create(flowCollectDataStatisticsP);
                    flowCollectDataStatisticsP.setFlowType(3);
                    flowCollectDataStatisticsApi.create(flowCollectDataStatisticsP);
                    flowCollectDataStatisticsList = flowCollectDataStatisticsApi.findListByCrmEnterpriseIds(Arrays.asList(crmEnterprisePartBO.getId()));
                }

                for (FlowCollectDataStatisticsDTO flowCollectDataStatisticsDTO : flowCollectDataStatisticsList) {
                    if (flowCollectDataStatisticsDTO.getFlowType() == 1) {
                        StringBuffer sign = getDataStringBuffer(flowCollectDataStatisticsDetailDTOList, erpSyncStatDOListp, flowCollectDataStatisticsDTO);
                        String signStr3 = sign.toString().substring(0, 3);
                        //1未上传2已上传 日销售/日采购/日库存有一项未上传的
                        if (nearThreeStatus == 2 && signStr3.contains("1")) {
                            nearThreeStatus = 1;
                        }

                        String signStr5 = sign.toString().substring(0, 5);
                        if (nearFiveStatus == 2 && signStr5.contains("1")) {
                            nearFiveStatus = 1;
                        }

                        //近3天一次请求都没有
                        if (nearThreeAllStatus == 1 && !signStr3.equals("111")) {
                            nearThreeAllStatus = 2;
                        }
                        //近5天一次请求都没有
                        if (nearFiveAllStatus == 1 && !signStr5.equals("11111")) {
                            nearFiveAllStatus = 2;
                        }
                    }

                    if (flowCollectDataStatisticsDTO.getFlowType() == 2) {
                        StringBuffer sign = getDataStringBuffer(flowCollectDataStatisticsDetailDTOList, erpSyncStatDOListS, flowCollectDataStatisticsDTO);

                        String signStr3 = sign.toString().substring(0, 3);
                        //1未上传2已上传 日销售/日采购/日库存有一项未上传的
                        if (nearThreeStatus == 2 && signStr3.contains("1")) {
                            nearThreeStatus = 1;
                        }

                        String signStr5 = sign.toString().substring(0, 5);
                        if (nearFiveStatus == 2 && signStr5.contains("1")) {
                            nearFiveStatus = 1;
                        }

                        //近3天一次请求都没有
                        if (nearThreeAllStatus == 1 && !signStr3.equals("111")) {
                            nearThreeAllStatus = 2;
                        }
                        //近5天一次请求都没有
                        if (nearFiveAllStatus == 1 && !signStr5.equals("11111")) {
                            nearFiveAllStatus = 2;
                        }
                    }

                    if (flowCollectDataStatisticsDTO.getFlowType() == 3) {
                        StringBuffer sign = getDataStringBuffer(flowCollectDataStatisticsDetailDTOList, erpSyncStatDOListGB, flowCollectDataStatisticsDTO);

                        String signStr3 = sign.toString().substring(0, 3);
                        //1未上传2已上传 日销售/日采购/日库存有一项未上传的
                        if (nearThreeStatus == 2 && signStr3.contains("1")) {
                            nearThreeStatus = 1;
                        }

                        String signStr5 = sign.toString().substring(0, 5);
                        if (nearFiveStatus == 2 && signStr5.contains("1")) {
                            nearFiveStatus = 1;
                        }

                        //近3天一次请求都没有
                        if (nearThreeAllStatus == 1 && !signStr3.equals("111")) {
                            nearThreeAllStatus = 2;
                        }
                        //近5天一次请求都没有
                        if (nearFiveAllStatus == 1 && !signStr5.equals("11111")) {
                            nearFiveAllStatus = 2;
                        }
                    }
                    fchsIds.add(flowCollectDataStatisticsDTO.getId());
                }

                //修改标记
                Integer finalNearFiveAllStatus = nearFiveAllStatus;
                Integer finalNearFiveStatus = nearFiveStatus;
                Integer finalNearThreeAllStatus = nearThreeAllStatus;
                Integer finalNearThreeStatus = nearThreeStatus;

                flowCollectDataStatisticsList.stream().forEach(e -> {
                    SaveFlowCollectDataStatisticsRequest flowCollectDataStatistics = new SaveFlowCollectDataStatisticsRequest();
                    flowCollectDataStatistics.setId(e.getId());
                    flowCollectDataStatistics.setNearFiveAllStatus(finalNearFiveAllStatus);
                    flowCollectDataStatistics.setNearFiveStatus(finalNearFiveStatus);
                    flowCollectDataStatistics.setNearThreeAllStatus(finalNearThreeAllStatus);
                    flowCollectDataStatistics.setNearThreeStatus(finalNearThreeStatus);
                    updateFlowCollectDataStatisticsRequestDTOList.add(flowCollectDataStatistics);
                });
                hasCrmIds.add(crmEnterprisePartBO.getId());
            }
            flowCollectDataStatisticsApi.updateBatch(updateFlowCollectDataStatisticsRequestDTOList);
            flowCollectDataStatisticsApi.deleteDetailByFchIds(fchsIds);
            flowCollectDataStatisticsApi.saveBatchDetail(flowCollectDataStatisticsDetailDTOList);
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        List<Long> crmIds = flowCollectDataStatisticsApi.findCrmList();
        crmIds.removeAll(hasCrmIds);
        List<FlowCollectDataStatisticsDTO> heartStatisticsDTOList = flowCollectDataStatisticsApi.findListByCrmEnterpriseIds(crmIds);
        List<Long> ids = heartStatisticsDTOList.stream().map(e -> e.getId()).collect(Collectors.toList());
        flowCollectDataStatisticsApi.deleteDetailByFchIds(ids);
        flowCollectDataStatisticsApi.deleteByIds(ids);
    }


    private StringBuffer getDataStringBuffer(List<SaveFlowCollectDataStatisticsDetailRequest> flowCollectDataStatisticsDetailDTOList, List<FlowStatisticsBO> erpSyncStatDOList, FlowCollectDataStatisticsDTO flowCollectDataStatisticsDTO) {
        List<String> list = erpSyncStatDOList.stream().map(e -> e.getCrmEnterpriseId() + DateUtil.format(e.getDateTime(), "yyyy-MM-dd")).collect(Collectors.toList());
        StringBuffer sign = new StringBuffer();
        for (int i = 1; i <= 30; i++) {
            Integer flowStatus = 1;
            String time = DateUtil.format(DateUtil.offsetDay(new Date(), -i), "yyyy-MM-dd");
            StringBuffer sb = new StringBuffer();
            sb.append(flowCollectDataStatisticsDTO.getCrmEnterpriseId()).append(time);
            if (list.contains(sb.toString())) {
                flowStatus = 2;
            }
            sign.append(flowStatus);
            SaveFlowCollectDataStatisticsDetailRequest flowCollectDataStatisticsDetailDTO = new SaveFlowCollectDataStatisticsDetailRequest();
            flowCollectDataStatisticsDetailDTO.setFcdsId(flowCollectDataStatisticsDTO.getId());
            flowCollectDataStatisticsDetailDTO.setFlowStatus(flowStatus);
            flowCollectDataStatisticsDetailDTO.setDateTime(DateUtil.parseDate(time));
            flowCollectDataStatisticsDetailDTOList.add(flowCollectDataStatisticsDetailDTO);
        }
        return sign;
    }

    private StringBuffer getHeartStringBuffer(Map<String, Integer> timeFlowStatus, List<SaveFlowCollectHeartStatisticsDetailRequest> flowCollectHeartStatisticsDetailDTOList, ErpClientDO erpClientDO, List<ErpSyncStatDO> erpSyncStatDOList, FlowCollectHeartStatisticsDTO flowCollectHeartStatisticsDTO) {
        List<String> list = erpSyncStatDOList.stream().map(e -> e.getSuId() + e.getSuDeptNo() + DateUtil.format(e.getStatDate(), "yyyy-MM-dd")).collect(Collectors.toList());
        StringBuffer sign = new StringBuffer();
        for (int i = 1; i <= 30; i++) {
            Integer flowStatus = 1;
            String time = DateUtil.format(DateUtil.offsetDay(new Date(), -i), "yyyy-MM-dd");
            if (erpClientDO != null) {
                StringBuffer sb = new StringBuffer();
                sb.append(erpClientDO.getSuId()).append(erpClientDO.getSuDeptNo()).append(time);
                if (list.contains(sb.toString())) {
                    flowStatus = 2;
                }
            }
            sign.append(flowStatus);
            Integer status = timeFlowStatus.get(time);
            if (status == null || status == 1) {
                timeFlowStatus.put(time, flowStatus);
            }
            SaveFlowCollectHeartStatisticsDetailRequest flowCollectHeartStatisticsDetailDTO = new SaveFlowCollectHeartStatisticsDetailRequest();
            flowCollectHeartStatisticsDetailDTO.setFchsId(flowCollectHeartStatisticsDTO.getId());
            flowCollectHeartStatisticsDetailDTO.setFlowStatus(flowStatus);
            flowCollectHeartStatisticsDetailDTO.setDateTime(DateUtil.parseDate(time));
            flowCollectHeartStatisticsDetailDTOList.add(flowCollectHeartStatisticsDetailDTO);
        }
        return sign;
    }


    /**
     * ERP商业公司监控达到阈值发送提醒短信
     *
     * @param key          统计请求次数的key
     * @param erpSyncStat  统计信息
     * @param erpClientDOS erp对接信息
     */
    private void erpMonitorReminderSendSms(String key, ErpSyncStatDO erpSyncStat, List<ErpClientDTO> erpClientDOS) {
        List<ErpInstallEmployeeInfoDetailBO> erpInstallEmployeeInfoList = erpOpenConfig.getErpInstallEmployeeInfoDetail();
        if (CollUtil.isEmpty(erpInstallEmployeeInfoList)) {
            log.warn("erpDataStatService，实施负责人手机号信息配置为空");
            return;
        }
        // 实施负责人姓名对应的手机号
        Map<String, List<String>> installEmployeeMap = new HashMap<>();
        for (ErpInstallEmployeeInfoDetailBO erpInstallEmployeeInfoBO : erpInstallEmployeeInfoList) {
            List<String> mobileList = erpInstallEmployeeInfoBO.getMobileList();
            if (CollUtil.isEmpty(mobileList)) {
                continue;
            }
            mobileList = mobileList.stream().distinct().collect(Collectors.toList());
            installEmployeeMap.put(OpenStringUtils.clearAllSpace(erpInstallEmployeeInfoBO.getName()), mobileList);
        }
        // 实施负责人姓名对应的商业公司
        Map<String, String> enterpriseNameMap = new HashMap<>();
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
        if (MapUtil.isEmpty(enterpriseNameMap)) {
            return;
        }

        Set<String> mobileSet = new HashSet<>();
        try {
            for (Map.Entry<String, String> entry : enterpriseNameMap.entrySet()) {
                String installEmployeeName = entry.getKey();
                List<String> mobileList = installEmployeeMap.get(installEmployeeName);
                if (CollUtil.isEmpty(mobileList)) {
                    log.info("erpDataStatService，实施负责人[{}]手机号为空", installEmployeeName);
                    continue;
                }
                for (String mobile : mobileList) {
                    mobileSet.add(mobile);
                }
            }

            if (CollUtil.isNotEmpty(mobileSet)) {
                String content = StrFormatter.format(ErpMonitorCountReminderTypeEnum.MONITOR_REMINDER.getTemplateContent(), reminderTime, erpSyncStat.getTaskNo());
                for (String mobile : mobileSet) {
                    // 是否已发送短信
                    String smsKey = ErpDataStatCacheUtils.getSmsKey(erpSyncStat, mobile);
                    String sendMobile = (String) stringRedisTemplate.opsForValue().get(smsKey);
                    if (StrUtil.isNotBlank(sendMobile)) {
                        continue;
                    }
                    boolean result = smsApi.send(mobile, content, SmsTypeEnum.MONITOR_REMINDER);
                    log.info("erpDataStatService，ERP商业公司监控达到阈值提醒发送短信完成，短信内容：{}，发送结果：{}", content, result);
                    if (result) {
                        // 发送完成，加入缓存已发送，当前统计时间范围内每个手机号只发送一次
                        stringRedisTemplate.opsForValue().set(smsKey, content, 1, TimeUnit.HOURS);
                    }

                }
            }
        } catch (Exception e) {
            log.error("erpDataStatService，ERP商业公司监控达到阈值提醒发送短信异常：{}", e.getMessage());
        }
    }

}
