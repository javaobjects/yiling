package com.yiling.open.erp.api.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.agency.api.CrmSupplierApi;
import com.yiling.dataflow.agency.dto.CrmSupplierDTO;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.dataflow.flow.api.FlowEnterpriseConnectMonitorApi;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseConnectMonitorRequest;
import com.yiling.dataflow.flow.enums.ConnectStatusEnum;
import com.yiling.dataflow.order.api.FlowGoodsBatchApi;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.dataflow.statistics.api.FlowErpSyncDateApi;
import com.yiling.dataflow.statistics.dto.request.SaveFlowErpSyncDateRequest;
import com.yiling.dataflow.wash.api.ErpClientWashPlanApi;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.api.FlowMonthWashTaskApi;
import com.yiling.dataflow.wash.bo.LastestFlowDateMqBO;
import com.yiling.dataflow.wash.dto.ErpClientWashPlanDTO;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateErpClientWashPlanRequest;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.enums.MqDelayLevel;
import com.yiling.open.erp.api.ErpDataStatApi;
import com.yiling.open.erp.bo.ErpErpDataStatCountBO;
import com.yiling.open.erp.dao.ErpSyncStatMapper;
import com.yiling.open.erp.dto.request.ErpClientQueryRequest;
import com.yiling.open.erp.dto.request.QueryErpDataStatCountRequest;
import com.yiling.open.erp.entity.ErpClientDO;
import com.yiling.open.erp.entity.ErpSyncStatDO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpDataStatService;
import com.yiling.open.erp.util.FlowUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@DubboService
@Slf4j
public class ErpDataStatApiImpl implements ErpDataStatApi {

    @Autowired
    private ErpDataStatService erpDataStatService;
    @Autowired
    ErpSyncStatMapper erpSyncStatMapper;
    @Autowired
    private ErpClientService erpClientService;

    @DubboReference
    private FlowErpSyncDateApi      flowErpSyncDateApi;
    @DubboReference
    private FlowPurchaseApi         flowPurchaseApi;
    @DubboReference
    private FlowSaleApi             flowSaleApi;
    @DubboReference
    private FlowGoodsBatchApi       flowGoodsBatchApi;
    @DubboReference
    private FlowMonthWashControlApi flowMonthWashControlApi;
    @DubboReference
    private FlowMonthWashTaskApi    flowMonthWashTaskApi;
    @DubboReference
    private ErpClientWashPlanApi    erpClientWashPlanApi;
    @Autowired
    @Lazy
    ErpDataStatApiImpl _this;
    @DubboReference
    MqMessageSendApi   mqMessageSendApi;

    @DubboReference
    private CrmEnterpriseApi                crmEnterpriseApi;
    @DubboReference
    private CrmSupplierApi                  crmSupplierApi;
    @DubboReference
    private FlowEnterpriseConnectMonitorApi flowEnterpriseConnectMonitorApi;

    private static final int RETURN_FLOW_DAY_COUNT_ALL       = 30;
    private static final int RETURN_FLOW_DAY_COUNT_EFFECTIVE = 19;

    @Override
    public void saveDataStat() {
        try {
            erpDataStatService.saveDataStat();
        } catch (Exception e) {
            log.error("[ErpDataStatApiImpl][saveDataStat] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public List<ErpErpDataStatCountBO> getErpDataStatCount(QueryErpDataStatCountRequest request) {
        try {
            return erpSyncStatMapper.getErpDataStatCount(request);
        } catch (Exception e) {
            log.error("[ErpDataStatApiImpl][getErpHeartCount] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public void erpSyncStatFlow() {
        log.info("昨天有无数据同步的企业信息统计, 任务开始");
        long start = System.currentTimeMillis();
        // 任务统计的日期为昨天
        DateTime yesterDay = DateUtil.yesterday();
        String taskTime = DateUtil.format(yesterDay, "yyyy-MM-dd");
        DateTime taskTimeDate = DateUtil.parse(taskTime, "yyyy-MM-dd");
        // 创建时间
        Date createTime = new Date();

        // 对接企业信息，分公司维度，key = suId + "_" + suDeptNo
        Map<String, ErpClientDO> erpClientMap = getErpClientMap();

        // 已统计的企业id
        Set<Long> eidSet = new HashSet<>();

        // 按照分公司进行统计
        List<Long> deleteEidList = new ArrayList<>();
        // 待保存同步统计
        List<SaveFlowErpSyncDateRequest> saveFlowErpSyncDateList = new ArrayList<>();
        for (String key : erpClientMap.keySet()) {
            ErpClientDO erpClientDO = erpClientMap.get(key);
            Long eid = erpClientDO.getRkSuId();
            // 是否已经统计过
            if (eidSet.contains(erpClientDO.getRkSuId())) {
                continue;
            }
            // 查询ERP同步数据统计表 erp_sync_stat
            Long suId = erpClientDO.getSuId();
            String suDeptNo = erpClientDO.getSuDeptNo();
            // 仅统计流向销售、采购、库存，任务编号：40000005, 40000006, 40000007, 40000008, 40000009, 40000010, 40000011
            List<String> taskNoList = ListUtil.toList(ErpTopicName.ErpFlowControl.getMethod(),
                    ErpTopicName.ErpPurchaseFlow.getMethod(), ErpTopicName.ErpSaleFlow.getMethod(),
                    ErpTopicName.ErpGoodsBatchFlow.getMethod(), ErpTopicName.ErpPurchaseFlowData.getMethod(),
                    ErpTopicName.ErpSaleFlowData.getMethod(), ErpTopicName.ErpGoodsBatchFlowData.getMethod());
            List<ErpSyncStatDO> erpSyncStatDOList = erpDataStatService.getOneBySuidAndSuDeptNoAndStatDate(suId, suDeptNo, taskTime, taskNoList);
            // 是否有同步
            List<ErpSyncStatDO> flowControlList = erpSyncStatDOList.stream().filter(e -> e.getTaskNo().equals(ErpTopicName.ErpFlowControl.getMethod())).collect(Collectors.toList());
            List<ErpSyncStatDO> saleList = erpSyncStatDOList.stream().filter(e -> Arrays.asList(ErpTopicName.ErpSaleFlow.getMethod(), ErpTopicName.ErpSaleFlowData.getMethod()).contains(e.getTaskNo())).collect(Collectors.toList());
            List<ErpSyncStatDO> purchaseList = erpSyncStatDOList.stream().filter(e -> Arrays.asList(ErpTopicName.ErpPurchaseFlow.getMethod(), ErpTopicName.ErpPurchaseFlowData.getMethod()).contains(e.getTaskNo())).collect(Collectors.toList());
            List<ErpSyncStatDO> goodsBatchList = erpSyncStatDOList.stream().filter(e -> Arrays.asList(ErpTopicName.ErpGoodsBatchFlow.getMethod(), ErpTopicName.ErpGoodsBatchFlowData.getMethod()).contains(e.getTaskNo())).collect(Collectors.toList());
            // 待保存统计结果
            SaveFlowErpSyncDateRequest saveFlowErpSyncDateRequest = new SaveFlowErpSyncDateRequest();
            saveFlowErpSyncDateRequest.setEid(eid);
            saveFlowErpSyncDateRequest.setEname(erpClientDO.getClientName());
            saveFlowErpSyncDateRequest.setTaskTime(taskTimeDate);
            saveFlowErpSyncDateRequest.setSyncFlag(CollUtil.isNotEmpty(erpSyncStatDOList) ? 1 : 0);
            saveFlowErpSyncDateRequest.setFlowControlFlag(CollUtil.isNotEmpty(flowControlList) ? 1 : 0);
            saveFlowErpSyncDateRequest.setSaleFlag(CollUtil.isNotEmpty(saleList) ? 1 : 0);
            saveFlowErpSyncDateRequest.setPurchaseFlag(CollUtil.isNotEmpty(purchaseList) ? 1 : 0);
            saveFlowErpSyncDateRequest.setGoodsBatchFlag(CollUtil.isNotEmpty(goodsBatchList) ? 1 : 0);
            saveFlowErpSyncDateRequest.setCreateTime(createTime);
            saveFlowErpSyncDateList.add(saveFlowErpSyncDateRequest);
            // 待删除已存在的统计结果
            deleteEidList.add(eid);
            // 缓存已统计的企业
            eidSet.add(eid);
        }

        // 任务统计的日期存在则先删除
        if (CollUtil.isNotEmpty(deleteEidList)) {
            List<List<Long>> deleteLists = FlowUtil.partitionList(deleteEidList, 200);
            for (List<Long> deleteList : deleteLists) {
                // 已存在的统计物理删除
                flowErpSyncDateApi.deleteByEidListAndTaskTime(deleteList, taskTime);
            }
        }

        // 保存新的统计结果
        if (CollUtil.isNotEmpty(saveFlowErpSyncDateList)) {
            List<List<SaveFlowErpSyncDateRequest>> saveLists = FlowUtil.partitionList(saveFlowErpSyncDateList, 200);
            for (List<SaveFlowErpSyncDateRequest> saveList : saveLists) {
                flowErpSyncDateApi.insertBatch(saveList);
            }
        }
        long end = System.currentTimeMillis();
        log.info("昨天有数据同步的企业信息统计, 任务结束, 耗时: {}", end - start);
    }

    @Override
    public void erpSyncLastestCollectDate() {
        log.info("最新采集日期统计, 任务开始");
        long start = System.currentTimeMillis();
        // 任务统计的日期为昨天
        DateTime yesterDay = DateUtil.yesterday();
        String taskTime = DateUtil.format(yesterDay, "yyyy-MM-dd");
        DateTime taskTimeDate = DateUtil.parse(taskTime, "yyyy-MM-dd");
        // 创建时间
        Date createTime = new Date();
        // 对接企业信息，分公司维度，key = suId + "_" + suDeptNo
        Map<String, ErpClientDO> erpClientMap = getErpClientMap();
        // 已统计的企业id
        Set<Long> eidSet = new HashSet<>();

        // 按照分公司进行统计
        // 待保存业务日期统计
        List<ErpClientDO> saveLastestCollectDateList = new ArrayList<>();
        for (String key : erpClientMap.keySet()) {
            ErpClientDO erpClientDO = erpClientMap.get(key);
            Long eid = erpClientDO.getRkSuId();
            // 是否已经统计过
            if (eidSet.contains(erpClientDO.getRkSuId())) {
                continue;
            }
            // 收集日期
            buildLastestCollectDate(saveLastestCollectDateList, createTime, taskTimeDate, erpClientDO);
            // 缓存已统计的企业
            eidSet.add(eid);
        }

        // 更新最新的收集时间
        if (CollUtil.isNotEmpty(saveLastestCollectDateList)) {
            erpClientService.updateCollectAndFlowDateBatch(saveLastestCollectDateList);
        }
        long end = System.currentTimeMillis();
        log.info("最新采集日期, 任务结束, 耗时: {}", end - start);
    }

    @Override
    public void erpSyncLastestFlowDate() {
        log.info("最新流向日期统计, 任务开始");
        long start = System.currentTimeMillis();
        // 任务统计的日期为昨天
        DateTime yesterDay = DateUtil.yesterday();
        String taskTime = DateUtil.format(yesterDay, "yyyy-MM-dd");
        DateTime taskTimeDate = DateUtil.parse(taskTime, "yyyy-MM-dd");
        // 创建时间
        Date createTime = new Date();
        // 对接企业信息，分公司维度，key = suId + "_" + suDeptNo
        Map<String, ErpClientDO> erpClientMap = getErpClientMap();

        // 按照分公司进行统计
        // 待保存业务日期统计
        List<ErpClientDO> saveLastestFlowDateList = new ArrayList<>();
        List<ErpClientDO> lastestFlowDateMqList = new ArrayList<>();
        for (String key : erpClientMap.keySet()) {
            ErpClientDO erpClientDO = erpClientMap.get(key);
            // 最新数据日期
            buildLastestFlowDate(saveLastestFlowDateList, lastestFlowDateMqList, createTime, taskTimeDate, erpClientDO);
        }

        // 更新最新的流向时间
        if (CollUtil.isNotEmpty(saveLastestFlowDateList)) {
            erpClientService.updateCollectAndFlowDateBatch(saveLastestFlowDateList);
        }
//            更新erp_client_wash_plan表的lastestFlowDate->plan_time
        // 查询是否存在月流向清洗日程、且业务日期等于数据范围的结束日期，符合则发送延时消息，延迟1小时
        FlowMonthWashControlDTO flowMonthWashControl = flowMonthWashControlApi.getWashStatus();
        if (ObjectUtil.isNull(flowMonthWashControl)) {
            return;
        }
//todo 临时处理
//        if (CollUtil.isNotEmpty(lastestFlowDateMqList)) {
//            List<SaveOrUpdateErpClientWashPlanRequest> erpClientWashPlanList = new ArrayList<>();
//            for (ErpClientDO erpClientDO : lastestFlowDateMqList) {
//                if (erpClientDO.getCrmEnterpriseId() != 0) {
//                    SaveOrUpdateErpClientWashPlanRequest saveOrUpdateErpClientWashPlanRequest = new SaveOrUpdateErpClientWashPlanRequest();
//                    saveOrUpdateErpClientWashPlanRequest.setFmwcId(flowMonthWashControl.getId());
//                    saveOrUpdateErpClientWashPlanRequest.setCrmEnterpriseId(erpClientDO.getCrmEnterpriseId());
//                    DateTime flowMonthWashEndDate = DateUtil.beginOfDay(flowMonthWashControl.getDataEndTime());
//                    if (erpClientDO.getLastestFlowDate().getTime() >= flowMonthWashEndDate.getTime()) {
//                        saveOrUpdateErpClientWashPlanRequest.setPlanTime(DateUtil.offsetHour(new Date(), 1));
//                    }
//                    erpClientWashPlanList.add(saveOrUpdateErpClientWashPlanRequest);
//                }
//            }
//            erpClientWashPlanApi.updateByFmwcId(erpClientWashPlanList);
//        }
//
//        sendWashControlMq(flowMonthWashControl);

        long end = System.currentTimeMillis();
        log.info("最新流向日期统计, 任务结束, 耗时: {}", end - start);
    }

    /**
     * 发送流向最新业务日期消息 到月流向清洗日程
     * 发送延时消息，延迟1小时
     *
     * @param flowMonthWashControl
     */
    private void sendWashControlMq(FlowMonthWashControlDTO flowMonthWashControl) {
        // 查询已经生成清洗数据的crm企业
        List<ErpClientWashPlanDTO> erpClientWashPlanDTOList = erpClientWashPlanApi.findListByFmwcId(flowMonthWashControl.getId());
        if (CollUtil.isEmpty(erpClientWashPlanDTOList)) {
            return;
        }
        for (ErpClientWashPlanDTO erpClientWashPlanDTO : erpClientWashPlanDTOList) {
            // 最新流向业务日期 大于等于 洗日程的数据结束日期、且不在已生成里面的，则发消息
            DateTime planTimeEndDate = DateUtil.beginOfDay(erpClientWashPlanDTO.getPlanTime());
            if (planTimeEndDate.getTime() < System.currentTimeMillis()&&planTimeEndDate.compareTo(DateUtil.parseDate("1970-01-01 00:00:00"))>0) {
                this.sendMq(Constants.TOPIC_LASTEST_FLOW_DATE_NOTIFY, Constants.TAG_LASTEST_FLOW_DATE_NOTIFY, String.valueOf(erpClientWashPlanDTO.getId()));
            }
        }
    }

    @Override
    public void flowDirectConnectMonitor() {
        log.info("流向直连接口监控信息统计任务开始");
        // flow_enterprise_connect_monitor
        long start = System.currentTimeMillis();

        DateTime yesterday = DateUtil.yesterday();
        // 向前推算30天
        DateTime endDate = DateUtil.endOfDay(yesterday);
        DateTime startDate = DateUtil.offsetDay(endDate, Math.negateExact(RETURN_FLOW_DAY_COUNT_ALL - 1));
        String endDateStr = DateUtil.format(endDate, "yyyy-MM-dd");
        String startDateStr = DateUtil.format(startDate, "yyyy-MM-dd");
        // 任务统计的流向上传日期为昨天
        String yesterDayStr = DateUtil.format(yesterday, "yyyy-MM-dd");
        String taskTime = DateUtil.format(yesterday, "yyyy-MM-dd");
        DateTime taskTimeDate = DateUtil.parse(taskTime, "yyyy-MM-dd");
        // 创建时间
        Date createTime = new Date();

        // 对接企业信息，分公司维度，key = suId + "_" + suDeptNo
        Map<String, ErpClientDO> erpClientMap = getErpClientMap();

        // crm企业信息, ，有效的、经销商，key-crmId, value-CrmEnterpriseDTO
        List<ErpClientDO> erpClientList = new ArrayList<>(erpClientMap.values());
        List<Long> crmEnterpriseIds = erpClientList.stream().filter(o -> ObjectUtil.isNotNull(o.getCrmEnterpriseId()) && o.getCrmEnterpriseId() > 0).map(ErpClientDO::getCrmEnterpriseId).distinct().collect(Collectors.toList());
        Map<Long, CrmEnterpriseDTO> crmEnterpriseMap = getCrmEnterpriseMap(crmEnterpriseIds);

        // crm企业级别信息 crm_supplier 表里面的级别，key-crmId, value-supplierLevel
        List<Long> crmIdEffectiveList = new ArrayList<>(crmEnterpriseMap.keySet());
        Map<Long, Integer> crmLevelMap = getCrmLevelMap(crmIdEffectiveList);

        // 待删除的直连接口监控：因erp对接企业无关联crm主键id，不需要进行直连接口统计了,根据erp_client主键进行删除
        Map<Long, String> deleteByFlowLevelMap = new HashMap<>();
        Map<Long, String> deleteByClientNoCrmMap = new HashMap<>();
        Map<Long, String> deleteByCrmNullMap = new HashMap<>();
        Map<Long, String> deleteByCrmBusinessCodeMap = new HashMap<>();
        Map<Long, String> deleteByCrmTypeMap = new HashMap<>();

        // 按照分公司进行统计
        for (String key : erpClientMap.keySet()) {
            ErpClientDO erpClientDO = erpClientMap.get(key);
            ArrayList<Integer> flowLevelList = ListUtil.toList(1, 2);
            // 流向级别是未对接   进行删除直连接口监控统计记录
            if (!flowLevelList.contains(erpClientDO.getFlowLevel())) {
                deleteByFlowLevelMap.put(erpClientDO.getId(), "erp_client企业信息流向级别是未对接");
                log.warn("流向直连接口监控信息统计, erp_client企业信息流向级别是未对接, rkSuId:{}, clientName:{}", erpClientDO.getRkSuId(), erpClientDO.getClientName());
                continue;
            }
            Long crmEnterpriseId = erpClientDO.getCrmEnterpriseId();
            // erp_client未关联crm
            if (ObjectUtil.isNull(crmEnterpriseId) || crmEnterpriseId.intValue() == 0) {
                deleteByClientNoCrmMap.put(erpClientDO.getId(), "erp_client企业信息未关联crm主键ID");
                log.warn("流向直连接口监控信息统计, erp_client企业信息未关联crm主键ID, rkSuId:{}, clientName:{}", erpClientDO.getRkSuId(), erpClientDO.getClientName());
                continue;
            }

            // crm企业信息
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseMap.get(crmEnterpriseId);
            // 未关联crm企业信息、或crm企业信息不存在  进行删除直连接口监控统计记录
            if (ObjectUtil.isNull(crmEnterpriseDTO)) {
                deleteByCrmNullMap.put(erpClientDO.getId(), "crm企业信息为空");
                log.warn("流向直连接口监控信息统计, crm企业信息为空, rkSuId:{}, clientName:{}", erpClientDO.getRkSuId(), erpClientDO.getClientName());
                continue;
            }
            // crm状态失效的 进行删除直连接口监控统计记录
            if (!ObjectUtil.equal(1, crmEnterpriseDTO.getBusinessCode())) {
                deleteByCrmBusinessCodeMap.put(erpClientDO.getId(), "crm企业信息已失效");
                log.warn("流向直连接口监控信息统计, crm企业信息已失效, rkSuId:{}, clientName:{}", erpClientDO.getRkSuId(), erpClientDO.getClientName());
                continue;
            }
            // crm不是经销商的  进行删除直连接口监控统计记录
            if (!ObjectUtil.equal(CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode(), crmEnterpriseDTO.getSupplyChainRole())) {
                deleteByCrmTypeMap.put(erpClientDO.getId(), "crm企业信息类型不是经销商");
                log.warn("流向直连接口监控信息统计, crm企业信息不是经销商, rkSuId:{}, clientName:{}", erpClientDO.getRkSuId(), erpClientDO.getClientName());
                continue;
            }

            // 查询ERP同步数据统计表 erp_sync_stat
            Long suId = erpClientDO.getSuId();
            String suDeptNo = erpClientDO.getSuDeptNo();
            List<ErpSyncStatDO> erpSyncStatList = getErpSyncStatListByDate(suId, suDeptNo, startDateStr, endDateStr);

            // 流向直连接口监控保存
            SaveFlowEnterpriseConnectMonitorRequest saveRequest = new SaveFlowEnterpriseConnectMonitorRequest();
            saveRequest.setCrmEnterpriseId(crmEnterpriseId);
            saveRequest.setCrmEnterpriseName(crmEnterpriseDTO.getName());
            saveRequest.setProvinceCode(crmEnterpriseDTO.getProvinceCode());
            saveRequest.setProvinceName(crmEnterpriseDTO.getProvinceName());
            // crm级别
            Integer supplierLevel = buildCrmLevel(crmLevelMap, crmEnterpriseId);
            saveRequest.setSupplierLevel(supplierLevel);
            // erp对接信息，根据erpClientId进行更新
            saveRequest.setErpClientId(erpClientDO.getId());
            saveRequest.setInstallEmployee(erpClientDO.getInstallEmployee());
            saveRequest.setFlowMode(erpClientDO.getFlowMode());
            saveRequest.setDockingTime(erpClientDO.getDepthTime());
            saveRequest.setFlowCollectionTime(erpClientDO.getLastestCollectDate());
            // 30天内回传流向天数、直连状态、状态说明
            buildDayCountAndConnectStatus(yesterDayStr, erpSyncStatList, saveRequest);
            // 保存或更新
            flowEnterpriseConnectMonitorApi.saveOrUpdate(saveRequest);
        }

        // 删除已统计的直连接口监控信息，并备注原因
        if (MapUtil.isNotEmpty(deleteByFlowLevelMap)) {
            flowEnterpriseConnectMonitorApi.deleteByClientId(ListUtil.toList(deleteByFlowLevelMap.keySet()), ListUtil.toList(deleteByFlowLevelMap.values()).get(0), 0L);
        }
        if (MapUtil.isNotEmpty(deleteByClientNoCrmMap)) {
            flowEnterpriseConnectMonitorApi.deleteByClientId(ListUtil.toList(deleteByClientNoCrmMap.keySet()), ListUtil.toList(deleteByClientNoCrmMap.values()).get(0), 0L);
        }
        if (MapUtil.isNotEmpty(deleteByCrmNullMap)) {
            flowEnterpriseConnectMonitorApi.deleteByClientId(ListUtil.toList(deleteByCrmNullMap.keySet()), ListUtil.toList(deleteByCrmNullMap.values()).get(0), 0L);
        }
        if (MapUtil.isNotEmpty(deleteByCrmBusinessCodeMap)) {
            flowEnterpriseConnectMonitorApi.deleteByClientId(ListUtil.toList(deleteByCrmBusinessCodeMap.keySet()), ListUtil.toList(deleteByCrmBusinessCodeMap.values()).get(0), 0L);
        }
        if (MapUtil.isNotEmpty(deleteByCrmTypeMap)) {
            flowEnterpriseConnectMonitorApi.deleteByClientId(ListUtil.toList(deleteByCrmTypeMap.keySet()), ListUtil.toList(deleteByCrmTypeMap.values()).get(0), 0L);
        }

        long end = System.currentTimeMillis();
        log.info("流向直连接口监控信息统计, 任务结束");
        log.info("流向直连接口监控信息统计, 耗时: {}", end - start);
    }

    @Override
    public void flowCollectHeartStatistics() {
        erpDataStatService.flowCollectHeartStatistics();
    }

    @Override
    public void flowCollectDataStatistics() {
        erpDataStatService.flowCollectDataStatistics();
    }

    private void buildDayCountAndConnectStatus(String yesterDayStr, List<ErpSyncStatDO> erpSyncStatList, SaveFlowEnterpriseConnectMonitorRequest saveRequest) {
        // 回传流向天数计数
        int returnFlowDayCount = 0;
        // 昨天是否上传数据
        boolean yesterSyncFlag = false;
        if (CollUtil.isNotEmpty(erpSyncStatList)) {
            returnFlowDayCount = erpSyncStatList.size();
            boolean yesterDayFlag = erpSyncStatList.stream().anyMatch(o -> ObjectUtil.equal(yesterDayStr, DateUtil.format(o.getStatDate(), "yyyy-MM-dd")));
            if (yesterDayFlag) {
                yesterSyncFlag = true;
            }
        }
        // 直连状态 0：无效 1：有效
        int connectStatus = ConnectStatusEnum.INVALID.getCode();
        // 直连状态说明
        String explanation = "";
        if (!yesterSyncFlag) {
            // 昨日未回传
            explanation = "昨日（".concat(yesterDayStr).concat("）未回传流向");
        } else {
            if (returnFlowDayCount < RETURN_FLOW_DAY_COUNT_EFFECTIVE) {
                int notReturnCount = RETURN_FLOW_DAY_COUNT_ALL - returnFlowDayCount;
                explanation = "过去30天，有".concat(notReturnCount + "").concat("天未回传流向");
            } else {
                connectStatus = ConnectStatusEnum.VALID.getCode();
            }
        }
        saveRequest.setReturnFlowDayCount(returnFlowDayCount);
        saveRequest.setConnectStatus(connectStatus);
        saveRequest.setExplanation(explanation);
    }

    private List<ErpSyncStatDO> getErpSyncStatListByDate(Long suId, String suDeptNo, String startDateStr, String endDateStr) {
        // 仅统计流向销售、采购、库存，任务编号：40000005, 40000006, 40000007, 40000008, 40000009, 40000010, 40000011
        List<String> taskNoList = ListUtil.toList(
                ErpTopicName.ErpPurchaseFlow.getMethod(), ErpTopicName.ErpSaleFlow.getMethod(),
                ErpTopicName.ErpGoodsBatchFlow.getMethod(), ErpTopicName.ErpPurchaseFlowData.getMethod(),
                ErpTopicName.ErpSaleFlowData.getMethod(), ErpTopicName.ErpGoodsBatchFlowData.getMethod());
        return erpDataStatService.listBySuidAndSuDeptNoAndStatDateAndTaskNoList(suId, suDeptNo, startDateStr, endDateStr, taskNoList);
    }

    private Integer buildCrmLevel(Map<Long, Integer> crmLevelMap, Long crmEnterpriseId) {
        Integer supplierLevel = 0;
        Integer crmLevel = crmLevelMap.get(crmEnterpriseId);
        if (ObjectUtil.isNotNull(crmLevel) && crmLevel.intValue() > 0) {
            supplierLevel = crmLevel;
        }
        return supplierLevel;
    }

    private Map<Long, Integer> getCrmLevelMap(List<Long> crmEnterpriseIds) {
        if (CollUtil.isEmpty(crmEnterpriseIds)) {
            return MapUtil.empty();
        }
        List<CrmSupplierDTO> crmSupplierList = crmSupplierApi.getSupplierInfoByCrmEnterId(crmEnterpriseIds);
        if (CollUtil.isEmpty(crmSupplierList)) {
            return MapUtil.empty();
        }
        return crmSupplierList.stream().filter(o -> ObjectUtil.isNotNull(o.getSupplierLevel()) && o.getSupplierLevel().intValue() > 0).collect(Collectors.toMap(o -> o.getCrmEnterpriseId(), o -> o.getSupplierLevel(), (k1, k2) -> k1));
    }

    private Map<Long, CrmEnterpriseDTO> getCrmEnterpriseMap(List<Long> crmEnterpriseIds) {
        if (CollUtil.isEmpty(crmEnterpriseIds)) {
            return MapUtil.empty();
        }
        List<CrmEnterpriseDTO> crmEnterpriseList = crmEnterpriseApi.getCrmEnterpriseListById(crmEnterpriseIds);
        if (CollUtil.isEmpty(crmEnterpriseList)) {
            return MapUtil.empty();
        }
        return crmEnterpriseList.stream().collect(Collectors.toMap(CrmEnterpriseDTO::getId, Function.identity()));
    }

    /**
     * 收集日期、最新数据日期
     *
     * @param saveLastestCollectDateList
     * @param createTime
     * @param taskTimeDate
     * @param erpClientDO
     */
    private void buildLastestCollectDate(List<ErpClientDO> saveLastestCollectDateList, Date createTime, DateTime taskTimeDate, ErpClientDO erpClientDO) {
        Long suId = erpClientDO.getSuId();
        String suDeptNo = erpClientDO.getSuDeptNo();
        ErpClientDO erpClientDOUpdate = new ErpClientDO();
        erpClientDOUpdate.setId(erpClientDO.getId());
//        erpClientDOUpdate.setLastestCollectDate(taskTimeDate);
        String defaultTime = "1970-01-01";
        // 最新收集日期
        // 仅统计流向销售、采购、库存，任务编号：40000005, 40000006, 40000007, 40000008, 40000009, 40000010, 40000011
        List<String> taskNoList = ListUtil.toList(
                ErpTopicName.ErpPurchaseFlow.getMethod(), ErpTopicName.ErpSaleFlow.getMethod(),
                ErpTopicName.ErpGoodsBatchFlow.getMethod(), ErpTopicName.ErpPurchaseFlowData.getMethod(),
                ErpTopicName.ErpSaleFlowData.getMethod(), ErpTopicName.ErpGoodsBatchFlowData.getMethod());
//        Date maxTaskTime = flowErpSyncDateApi.getMaxTaskTimeByEid(eid);
        Date maxTaskTime = erpDataStatService.getMaxTaskTimeByEid(suId, suDeptNo, taskNoList);
        String collectDate = DateUtil.format(maxTaskTime, "yyyy-MM-dd");
        if (ObjectUtil.isNull(maxTaskTime) || ObjectUtil.equal(collectDate, defaultTime)) {
            return;
        }
        erpClientDOUpdate.setLastestCollectDate(maxTaskTime);
        erpClientDOUpdate.setUpdateUser(0L);
        erpClientDOUpdate.setUpdateTime(createTime);
        saveLastestCollectDateList.add(erpClientDOUpdate);
    }

    /**
     * 收集日期、最新数据日期
     *
     * @param saveLastestFlowDateList
     * @param createTime
     * @param taskTimeDate
     * @param erpClientDO
     */
    private void buildLastestFlowDate(List<ErpClientDO> saveLastestFlowDateList, List<ErpClientDO> lastestFlowDateMqList,
                                      Date createTime, DateTime taskTimeDate, ErpClientDO erpClientDO) {
        Long eid = erpClientDO.getRkSuId();
        ErpClientDO erpClientDOUpdate = new ErpClientDO();
        erpClientDOUpdate.setId(erpClientDO.getId());
        erpClientDOUpdate.setLastestCollectDate(taskTimeDate);
        Date defaultTime = DateUtil.parse("1970-01-01", "yyyy-MM-dd");
        // 获取采购日期、销售日期、库存日期最新时间
        Date lastestFlowTime = defaultTime;
        Date maxPoTime = flowPurchaseApi.getMaxPoTimeByEid(eid);
        if (ObjectUtil.isNotNull(maxPoTime) && maxPoTime.getTime() > lastestFlowTime.getTime()) {
            lastestFlowTime = maxPoTime;
        }
        Date maxSoTime = flowSaleApi.getMaxSoTimeByEid(eid);
        if (ObjectUtil.isNotNull(maxSoTime) && maxSoTime.getTime() > lastestFlowTime.getTime()) {
            lastestFlowTime = maxSoTime;
        }
        Date maxGbTime = flowGoodsBatchApi.getMaxGbTimeByEid(eid);
        if (ObjectUtil.isNotNull(maxGbTime) && maxGbTime.getTime() > lastestFlowTime.getTime()) {
            lastestFlowTime = maxGbTime;
        }
        if (lastestFlowTime.getTime() == defaultTime.getTime()) {
            return;
        }
        //如果lastestFlowTime等于日程结束时间（年月日），延时1小时送mq消息生成清洗任务
        erpClientDOUpdate.setLastestFlowDate(lastestFlowTime);
        erpClientDOUpdate.setUpdateUser(0L);
        erpClientDOUpdate.setUpdateTime(createTime);
        saveLastestFlowDateList.add(erpClientDOUpdate);

        // 消息列表
        ErpClientDO erpClientMq = new ErpClientDO();
        erpClientMq.setCrmEnterpriseId(erpClientDO.getCrmEnterpriseId());
        erpClientMq.setLastestFlowDate(lastestFlowTime);
        erpClientMq.setRkSuId(erpClientDO.getRkSuId());
        erpClientMq.setFlowMode(erpClientDO.getFlowMode());
        lastestFlowDateMqList.add(erpClientMq);
    }


    private Map<String, ErpClientDO> getErpClientMap() {
        Map<String, ErpClientDO> erpClientMap = new HashMap<>();
        List<ErpClientDO> erpClientList = new ArrayList<>();
        int size = 500;
        int current = 1;
        Page<ErpClientDO> erpClientPage;
        ErpClientQueryRequest request = new ErpClientQueryRequest();
        request.setSize(size);
        do {
            request.setCurrent(current);
            erpClientPage = erpClientService.page(request);
            if (ObjectUtil.isNull(erpClientPage) || CollUtil.isEmpty(erpClientPage.getRecords())) {
                break;
            }
            erpClientList.addAll(erpClientPage.getRecords());
            if (erpClientPage.getRecords().size() < size) {
                break;
            }
            current++;
        } while (erpClientPage != null && CollUtil.isNotEmpty(erpClientPage.getRecords()));

        if (CollUtil.isNotEmpty(erpClientList)) {
            erpClientMap = erpClientList.stream().collect(Collectors.toMap(o -> o.getSuId() + "_" + o.getSuDeptNo(), o -> o, (k1, k2) -> k1));
        }
        return erpClientMap;
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public boolean sendMq(String topic, String topicTag, String msg) {
        // 延迟消息持久化
        MqMessageBO mqMessageBO = _this.sendPrepare(topic, topicTag, msg);
        mqMessageSendApi.send(mqMessageBO);
        return true;
    }

    /**
     * 延迟消息持久化
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public MqMessageBO sendPrepare(String topic, String topicTag, String msg) {
        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        return mqMessageBO;
    }
}
