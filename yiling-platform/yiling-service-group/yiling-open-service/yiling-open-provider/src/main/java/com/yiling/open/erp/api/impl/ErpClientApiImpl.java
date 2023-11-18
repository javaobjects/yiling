package com.yiling.open.erp.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.dataflow.check.api.FlowPurchaseCheckApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.flow.api.FlowEnterpriseConnectMonitorApi;
import com.yiling.dataflow.flow.enums.ConnectStatusEnum;
import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.dataflow.order.bo.FlowSaleCurrentMonthCountBO;
import com.yiling.dataflow.order.dto.request.FlowSaleMonthCountRequest;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.config.ErpOpenConfig;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.bo.ErpMonitorCountInfoDetailBO;
import com.yiling.open.erp.bo.ErpMonitorCountStatisticsBO;
import com.yiling.open.erp.bo.SjmsFlowCollectStatisticsCountBO;
import com.yiling.open.erp.bo.SjmsFlowMonitorNoDataSyncEnterpriseBO;
import com.yiling.open.erp.bo.SjmsFlowMonitorStatisticsCountBO;
import com.yiling.open.erp.dao.ErpClientMapper;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpClientParentQueryRequest;
import com.yiling.open.erp.dto.request.ErpClientQueryRequest;
import com.yiling.open.erp.dto.request.ErpClientQuerySjmsRequest;
import com.yiling.open.erp.dto.request.ErpClientSaveRequest;
import com.yiling.open.erp.dto.request.ErpMonitorQueryRequest;
import com.yiling.open.erp.dto.request.QueryClientFlowEnterpriseRequest;
import com.yiling.open.erp.dto.request.UpdateHeartBeatTimeRequest;
import com.yiling.open.erp.dto.request.UpdateMonitorStatusRequest;
import com.yiling.open.erp.entity.ErpClientDO;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.util.ErpConstants;
import com.yiling.open.heart.dto.request.SaveErpClientNoHeartRequest;
import com.yiling.open.heart.service.ErpClientNoHeartService;
import com.yiling.open.monitor.service.MonitorAbnormalDataService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Slf4j
@DubboService
public class ErpClientApiImpl implements ErpClientApi {

    @Autowired
    private ErpClientService erpClientService;
    @Autowired
    private ErpClientMapper  erpClientMapper;
    @Autowired
    private MonitorAbnormalDataService monitorAbnormalDataService;
    @Autowired
    private ErpOpenConfig erpOpenConfig;
    @Autowired
    private ErpClientNoHeartService erpClientNoHeartService;
    @DubboReference(timeout = 10 * 1000)
    private FlowSaleApi flowSaleApi;
    @DubboReference
    private FlowPurchaseCheckApi flowPurchaseCheckApi;
    @DubboReference
    private CrmEnterpriseApi crmEnterpriseApi;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @DubboReference
    private FlowEnterpriseConnectMonitorApi flowEnterpriseConnectMonitorApi;


    @Override
    public ErpClientDTO findErpClientByKey(String key) {
        try {
            return  erpClientService.selectByClientKey(key);
        } catch (Exception e) {
            log.error("[ErpClientApiImpl][findErpClientByKey] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public List<ErpClientDTO> selectBySuId(Long suId) {
        try {
            return erpClientService.selectBySuId(suId);
        } catch (Exception e) {
            log.error("[ErpClientApiImpl][selectBySuId] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public ErpClientDTO selectByRkSuId(Long rkSuId) {
        try {
            return erpClientService.selectByRkSuId(rkSuId);
        } catch (Exception e) {
            log.error("[ErpClientApiImpl][selectByRkSuId] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public Boolean updateCommandBySuId(ErpClientSaveRequest erpClientSaveRequest) {
        return erpClientService.updateCommandBySuId(erpClientSaveRequest);
    }

//    @Override
//    public DtoResponse<List<Integer>> findErpClientByDepthTimeNotNull() {
//        try {
//            return new DtoResponse<>(erpClientService.findErpClientByDepthTimeNotNull());
//        } catch (Exception e) {
//            log.error("[ErpClientApiImpl][findErpClientByDepthTimeNotNull] 异常！" + e.getMessage(), e);
//            throw new ServiceException(ResultCode.FAILURE);
//        }
//    }
//
//    @Override
//    public DtoResponse<Boolean> saveOrUpdateErpClient(ErpClientSaveRequest erpClientSaveRequest) {
//        try {
//            erpClientService.saveOrUpdateErpClient(erpClientSaveRequest);
//            return new DtoResponse<>(true);
//        } catch (Exception e) {
//            log.error("[ErpClientApiImpl][saveOrUpdateErpClient] 异常！" + e.getMessage(), e);
//            throw new ServiceException(ResultCode.FAILURE);
//        }
//    }
//
    @Override
    public Page<ErpClientDTO> page(ErpClientQueryRequest request) {
        try {
            Page<ErpClientDTO> page = PojoUtils.map(erpClientService.page(request), ErpClientDTO.class);
            if(ObjectUtil.isNotNull(page) && CollUtil.isNotEmpty(page.getRecords())){
                page.getRecords().forEach(r -> {
                    if(checkDefaultTime(r.getDepthTime())){
                        r.setDepthTime(null);
                    }
                });
            }
            return page;
        } catch (Exception e) {
            log.error("[ErpClientApiImpl][page] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public List<ErpClientDTO> selectByRkSuIdList(List<Long> rkSuIdList) {
        if(CollUtil.isEmpty(rkSuIdList)){
            return ListUtil.empty();
        }
        try {
            List<ErpClientDO> erpClientList = erpClientService.selectByRkSuIdList(rkSuIdList);
            return PojoUtils.map(erpClientList,ErpClientDTO.class);
        } catch (Exception e) {
            log.error("[ErpClientApiImpl][selectByRkSuIdList] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public Long saveOrUpdateErpClient(ErpClientSaveRequest erpClientSaveRequest) {
        if(ObjectUtil.isNull(erpClientSaveRequest)){
            return null;
        }
        try {
            return erpClientService.saveOrUpdateErpClient(erpClientSaveRequest);
        } catch (Exception e) {
            log.error("[ErpClientApiImpl][saveOrUpdateErpClient] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public Page<ErpClientDTO> parentPage(ErpClientParentQueryRequest request) {
        try {
            return PojoUtils.map(erpClientService.parentPage(request), ErpClientDTO.class);
        } catch (Exception e) {
            log.error("[ErpClientApiImpl][parentPage] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public Boolean updateMonitorStatus(UpdateMonitorStatusRequest request) {
        return erpClientService.updateMonitorStatus(request);
    }

    @Override
    public Boolean updateHeartBeatTimeBySuid(UpdateHeartBeatTimeRequest request) {
        return erpClientService.updateHeartBeatTimeBySuid(request);
    }

    @Override
    public Page<ErpClientDTO> getErpMonitorListPage(ErpMonitorQueryRequest request) {
        try {
            Page<ErpClientDO> page = erpClientService.getErpMonitorListPage(request);
            if (ObjectUtil.isNotNull(page) && CollUtil.isNotEmpty(page.getRecords())) {
                page.getRecords().forEach(r -> {
                    if (checkDefaultTime(r.getDepthTime())) {
                        r.setDepthTime(null);
                    }
                    if (checkDefaultTime(r.getHeartBeatTime())) {
                        r.setHeartBeatTime(null);
                    }
                });
            }
            return PojoUtils.map(page, ErpClientDTO.class);
        } catch (Exception e) {
            log.error("[ErpClientApiImpl][getErpMonitorListPage] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public ErpMonitorCountStatisticsBO getErpMonitorCountStatistics() {
        ErpMonitorCountStatisticsBO countStatisticsBO = new ErpMonitorCountStatisticsBO();
        try {
            // 超过请求次数关闭对接数量
            ErpMonitorCountStatisticsBO clientStatusClosedCount = erpClientMapper.getErpMonitorClientStatusCount();
            if(ObjectUtil.isNotNull(clientStatusClosedCount)){
                countStatisticsBO.setClosedCount(clientStatusClosedCount.getClosedCount());
            }
            // 24小时内无心跳对接数量
            ErpMonitorCountStatisticsBO noHartBeatCount = erpClientMapper.getErpMonitorNoHartBeatCount();
            if(ObjectUtil.isNotNull(noHartBeatCount)){
                countStatisticsBO.setNoHeartCount(noHartBeatCount.getNoHeartCount());
            }
            // 当月未上传销售企业数量
            long noFlowSaleCount = statisticsEnterpriseNoFlowSaleCurrentMonth();
            countStatisticsBO.setNoFlowSaleCount(noFlowSaleCount);

            Date date = new Date();
//            DateTime startTime = DateUtil.beginOfMonth(date);
//            DateTime endTime = DateUtil.endOfMonth(date);
            // 销售异常数据数量
            Long saleExceptionCount = monitorAbnormalDataService.getSaleExceptionCount(null, null);
            countStatisticsBO.setSaleExceptionCount(saleExceptionCount);
            // 采购异常数据数量
            Long purchaseExceptionCount = flowPurchaseCheckApi.getPurchaseExceptionCount(null, null);
            countStatisticsBO.setPurchaseExceptionCount(purchaseExceptionCount);

            return countStatisticsBO;
        } catch (Exception e) {
            log.error("[ErpClientApiImpl][getErpMonitorCountStatistics] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    /**
     * 统计当月无销售的企业
     *
     * @return
     */
    private long statisticsEnterpriseNoFlowSaleCurrentMonth(){
        // 当月无销售的企业
        return stringRedisTemplate.opsForSet().size(ErpConstants.erp_flow_sale_Statistics);
    }

    @Override
    public Page<ErpClientDTO> flowEnterprisePage(QueryClientFlowEnterpriseRequest request) {
        try {
            return PojoUtils.map(erpClientService.flowEnterprisePage(request), ErpClientDTO.class);
        } catch (Exception e) {
            log.error("[ErpClientApiImpl][flowEnterprisePage] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public List<ErpClientDTO> getFlowEnterpriseListByName(String name) {
        try {
            return PojoUtils.map(erpClientService.getFlowEnterpriseListByName(name), ErpClientDTO.class);
        } catch (Exception e) {
            log.error("[ErpClientApiImpl][getFlowEnterpriseListByName] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public Page<ErpClientDTO> getNoSaleListPage(ErpClientQueryRequest request) {
        Page<ErpClientDTO> page = new Page<>();
        // 查询erp对接企业列表
        List<ErpClientDTO> erpClientList = PojoUtils.map(erpClientService.getAllFlowEnterpriseList(request), ErpClientDTO.class);
        if(CollUtil.isEmpty(erpClientList)){
            return page;
        }


        return page;
    }

    /**
     * 当月无销售的企业id列表
     */
    @Override
    public void statisticsNoFlowSaleEidList() {
        log.info("当月无销售的企业统计任务执行");
        long start = System.currentTimeMillis();
        List<ErpClientDO> allFlowEnterpriseList = erpClientService.getAllFlowEnterpriseList(new ErpClientQueryRequest());
        if (CollUtil.isEmpty(allFlowEnterpriseList)) {
            return;
        }
        // 当月未上传销售企业数量
        List<Long> eidList = allFlowEnterpriseList.stream().map(ErpClientDO::getRkSuId).distinct().collect(Collectors.toList());
        Date date = new Date();
        DateTime startTime = DateUtil.beginOfMonth(date);
        DateTime endTime = DateUtil.endOfMonth(date);

        List<List<Long>> eidSubList = new ArrayList<>();
        if (eidList.size() > 10) {
            eidSubList = Lists.partition(eidList, 10);
        } else {
            eidSubList.add(eidList);
        }
        List<FlowSaleCurrentMonthCountBO> flowSaleCountList = new ArrayList<>();
        for (List<Long> eids : eidSubList) {
            FlowSaleMonthCountRequest flowSaleRequest = new FlowSaleMonthCountRequest();
            flowSaleRequest.setEidList(eids);
            flowSaleRequest.setStartTime(startTime);
            flowSaleRequest.setEndTime(endTime);
            List<FlowSaleCurrentMonthCountBO> flowSaleCurrentMonthCountList = flowSaleApi.getMonthCount(flowSaleRequest);
            if(CollUtil.isNotEmpty(flowSaleCurrentMonthCountList)){
                flowSaleCountList.addAll(flowSaleCurrentMonthCountList);
            }
        }

        if(CollUtil.isEmpty(flowSaleCountList)){
            List<String> eids = Convert.toList(String.class, eidList);
            String[] noFlowSaleEidArray = ArrayUtil.toArray(eids, String.class);
            stringRedisTemplate.opsForSet().add(ErpConstants.erp_flow_sale_Statistics, noFlowSaleEidArray);
            return;
        }

        Map<Long, FlowSaleCurrentMonthCountBO> flowSaleEidMap = flowSaleCountList.stream().collect(Collectors.toMap(FlowSaleCurrentMonthCountBO::getEid, Function.identity()));
        List<String> noFlowSaleEidList = new ArrayList<>();
        for (Long eid : eidList) {
            FlowSaleCurrentMonthCountBO flowSaleCurrentMonthCountBO = flowSaleEidMap.get(eid);
            if(ObjectUtil.isNull(flowSaleCurrentMonthCountBO) || 0 == flowSaleCurrentMonthCountBO.getCurrentMonthCount().intValue()){
                noFlowSaleEidList.add(eid.toString());
            }
        }
        if(CollUtil.isNotEmpty(noFlowSaleEidList)){
            String[] noFlowSaleEidArray = ArrayUtil.toArray(noFlowSaleEidList, String.class);
            stringRedisTemplate.opsForSet().add(ErpConstants.erp_flow_sale_Statistics, noFlowSaleEidArray);
        }
        log.info("月无销售的企业统计任务执行结束, 耗时：" + (System.currentTimeMillis() - start));
    }

    @Override
    public List<ErpMonitorCountInfoDetailBO> getErpMonitorCountInfoDetail() {
        List<ErpMonitorCountInfoDetailBO> monitorCountInfoList = erpOpenConfig.getErpMonitorCountInfoDetail();
        if(CollUtil.isEmpty(monitorCountInfoList)){
            return ListUtil.empty();
        }
        return monitorCountInfoList;
    }

    @Override
    public void handleErpClientsNoHeartBetween24h() {
        log.info("任务开始: 昨天无心跳的企业信息统计开始");
        long start = System.currentTimeMillis();
        Date opTime = new Date();
        // 最后心跳时间，昨天0点之前
        DateTime heartBeatTimEnd = DateUtil.beginOfDay(DateUtil.offsetDay(opTime, -1));
        // 任务统计的日期
        DateTime taskTime = heartBeatTimEnd;
        // 先删除再新增
        erpClientNoHeartService.deleteByTaskTime(taskTime);

        try {
            int size = 200;
            int current = 1;
            Page<ErpClientDO> page;
            ErpClientQueryRequest request = new ErpClientQueryRequest();
//            request.setSyncStatus(1);
            request.setClientStatus(1);
//            request.setFlowStatus(1);
            request.setHeartBeatTimEnd(heartBeatTimEnd);
            request.setSize(size);
            do {
                request.setCurrent(current);
                page = erpClientService.page(request);
                if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                    break;
                }
                saveErpClientNoHeartList(page.getRecords(), taskTime, opTime);
                current++;
            } while (ObjectUtil.isNotNull(page) && CollUtil.isNotEmpty(page.getRecords()));
        } catch (Exception e) {
            log.error("昨日无心跳的企业信息统计异常，e:{}", e.getMessage());
            e.printStackTrace();
        }
        log.info("任务结束: erp昨天无心跳企业信息统计结束。耗时：" + (System.currentTimeMillis() - start));
    }

    @Override
    public void erpClientDataInitStatusUpdate() {
        log.info("任务开始: 数据初始化状态更新开始");
        long start = System.currentTimeMillis();
        List<Long> idList = new ArrayList<>();
        // 对接企业信息
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
            List<Long> ids = erpClientPage.getRecords().stream()
                    .filter(o -> !ObjectUtil.equal(1, o.getDataInitStatus()) && ObjectUtil.isNotNull(o.getSyncStatusTime()) && !ObjectUtil.equal("1970-01-01 00:00:00", DateUtil.format(o.getSyncStatusTime(), "yyyy-MM-dd HH:mm:ss")) && DateUtil.between(o.getSyncStatusTime(), new Date(), DateUnit.HOUR) >= 24)
                    .map(ErpClientDO::getId)
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(ids)) {
                idList.addAll(ids);
            }

            if (erpClientPage.getRecords().size() < size) {
                break;
            }
            current++;
        } while (erpClientPage != null && CollUtil.isNotEmpty(erpClientPage.getRecords()));

        if (CollUtil.isNotEmpty(idList)) {
            erpClientService.updateDdataInitStatusByIdList(idList);
        }
        log.info("任务结束: 数据初始化状态更新结束。耗时：" + (System.currentTimeMillis() - start));
    }

    @Override
    public SjmsFlowMonitorStatisticsCountBO getSjmsFlowMonitorStatisticsCount(List<String> licenseNumberList) {
        SjmsFlowMonitorStatisticsCountBO sjmsFlowMonitorStatisticsCountBO = new SjmsFlowMonitorStatisticsCountBO();
        if (null == licenseNumberList) {
            return sjmsFlowMonitorStatisticsCountBO;
        }
        // 已部署接口数量，包含基础对接、流向对接
        //        Integer deployInterfaceCount = erpClientService.getDeployInterfaceCountByRkSuIdList(licenseNumberList);
        //        sjmsFlowMonitorStatisticsCountBO.setDeployInterfaceCount(ObjectUtil.isNull(deployInterfaceCount) ? 0 : deployInterfaceCount);

        // 未开启同步数量，包含基础对接、流向对接
        //        Integer syncStatusOffCount = erpClientService.getSyncStatusOffCountByRkSuIdList(eidList);
        //        sjmsFlowMonitorStatisticsCountBO.setSyncStatusOffCount(ObjectUtil.isNull(syncStatusOffCount) ? 0 : syncStatusOffCount);
        // 终端未激活数量，包含基础对接、流向对接
        //        Integer clientStatusOffCount = erpClientService.getClientStatusOffCountByRkSuIdList(eidList);
        //        sjmsFlowMonitorStatisticsCountBO.setClientStatusOffCount(ObjectUtil.isNull(clientStatusOffCount) ? 0 : clientStatusOffCount);

        // 运行中接口数量，流向对接、已激活、已开启同步的
        Integer runningCount = erpClientService.getRunningCountByRkSuIdList(licenseNumberList);
        sjmsFlowMonitorStatisticsCountBO.setRunningCount(ObjectUtil.isNull(runningCount) ? 0 : runningCount);

        // 未上传昨日流向数量，即超1天未上传流向数量，从昨天开始（包含昨天）、时间向前推1天、取最新流向日期大于1天之前的，仅包含流向对接
        Date yesterday = DateUtil.yesterday();
        Date noDataDay1 = DateUtil.offsetDay(yesterday, Math.negateExact(1));
        //        DateTime noDataDay1Begin = DateUtil.beginOfDay(noDataDay1);
        DateTime noDataDay1End = DateUtil.endOfDay(noDataDay1);
        Integer noDataYesterdayCount = erpClientService.getNoDatCountByRkSuIdListAndFlowDate(licenseNumberList, noDataDay1End);
        sjmsFlowMonitorStatisticsCountBO.setNoDataYesterdayCount(ObjectUtil.isNull(noDataYesterdayCount) ? 0 : noDataYesterdayCount);

        // 超3天未上传流向数量，从昨天开始（包含昨天）、时间向前推3天、取最新流向日期大于4天之前的，仅包含流向对接
        Date noDataDayEnd3 = DateUtil.offsetDay(DateUtil.endOfDay(yesterday), Math.negateExact(4));
        Integer noDataMoreThan3DaysCount = erpClientService.getNoDatCountByRkSuIdListAndFlowDate(licenseNumberList, noDataDayEnd3);
        sjmsFlowMonitorStatisticsCountBO.setNoDataMoreThan3DaysCount(ObjectUtil.isNull(noDataMoreThan3DaysCount) ? 0 : noDataMoreThan3DaysCount);

        // 超7天未上传流向数量，从昨天开始（包含昨天）、时间向前推7天、取最新流向日期大于8天之前的，仅包含流向对接
        Date noDataDayEnd7 = DateUtil.offsetDay(DateUtil.endOfDay(yesterday), Math.negateExact(8));
        Integer noDataMoreThan7DaysCount = erpClientService.getNoDatCountByRkSuIdListAndFlowDate(licenseNumberList, noDataDayEnd7);
        sjmsFlowMonitorStatisticsCountBO.setNoDataMoreThan7DaysCount(ObjectUtil.isNull(noDataMoreThan7DaysCount) ? 0 : noDataMoreThan7DaysCount);
        return sjmsFlowMonitorStatisticsCountBO;
    }

    @Override
    public SjmsFlowMonitorStatisticsCountBO getSjmsFlowMonitorStatisticsCountByCrmIds(SjmsUserDatascopeBO datascopeBO) {
        SjmsFlowMonitorStatisticsCountBO sjmsFlowMonitorStatisticsCountBO = new SjmsFlowMonitorStatisticsCountBO();
        if (ObjectUtil.isNull(datascopeBO) || ObjectUtil.equal(OrgDatascopeEnum.NONE.getCode(), datascopeBO.getOrgDatascope())) {
            return sjmsFlowMonitorStatisticsCountBO;
        }

        // 有效直连接口数量
        Integer runningCount = flowEnterpriseConnectMonitorApi.countMonitorEnterprise(datascopeBO, ConnectStatusEnum.VALID);
        sjmsFlowMonitorStatisticsCountBO.setRunningCount(ObjectUtil.isNull(runningCount) ? 0 : runningCount);

        // 未上传昨日流向数量，即超1天未上传流向数量，从昨天开始（包含昨天）、时间向前推1天、取最新流向日期大于1天之前的，仅包含流向对接
        Date yesterday = DateUtil.yesterday();
        Date noDataDay1 = DateUtil.offsetDay(yesterday, Math.negateExact(1));
        DateTime noDataDay1End = DateUtil.endOfDay(noDataDay1);
        Integer noDataYesterdayCount = erpClientService.getNoDatCountByRkSuIdListAndFlowDateByCrmIds(datascopeBO, noDataDay1End);
        sjmsFlowMonitorStatisticsCountBO.setNoDataYesterdayCount(ObjectUtil.isNull(noDataYesterdayCount) ? 0 : noDataYesterdayCount);

        // 超3天未上传流向数量，从昨天开始（包含昨天）、时间向前推3天、取最新流向日期大于4天之前的，仅包含流向对接
        Date noDataDayEnd3 = DateUtil.offsetDay(DateUtil.endOfDay(yesterday), Math.negateExact(4));
        Integer noDataMoreThan3DaysCount = erpClientService.getNoDatCountByRkSuIdListAndFlowDateByCrmIds(datascopeBO, noDataDayEnd3);
        sjmsFlowMonitorStatisticsCountBO.setNoDataMoreThan3DaysCount(ObjectUtil.isNull(noDataMoreThan3DaysCount) ? 0 : noDataMoreThan3DaysCount);

        // 超7天未上传流向数量，从昨天开始（包含昨天）、时间向前推7天、取最新流向日期大于8天之前的，仅包含流向对接
        Date noDataDayEnd7 = DateUtil.offsetDay(DateUtil.endOfDay(yesterday), Math.negateExact(8));
        Integer noDataMoreThan7DaysCount = erpClientService.getNoDatCountByRkSuIdListAndFlowDateByCrmIds(datascopeBO, noDataDayEnd7);
        sjmsFlowMonitorStatisticsCountBO.setNoDataMoreThan7DaysCount(ObjectUtil.isNull(noDataMoreThan7DaysCount) ? 0 : noDataMoreThan7DaysCount);
        return sjmsFlowMonitorStatisticsCountBO;
    }

    @Override
    public SjmsFlowCollectStatisticsCountBO getSjmsFlowCollectStatisticsCount(ErpClientQuerySjmsRequest request) {
        SjmsFlowCollectStatisticsCountBO flowCollectStatisticsCountBO = new SjmsFlowCollectStatisticsCountBO();
        if (ObjectUtil.isNull(request) || CollUtil.isEmpty(request.getLicenseNumberList())) {
            return flowCollectStatisticsCountBO;
        }
        Integer runningStatus = request.getRunningStatus();
        if (ObjectUtil.isNotNull(runningStatus) && 1 == runningStatus.intValue()) {
            // 状态：1-未运行
            return flowCollectStatisticsCountBO;
        }
        Integer flowLevel = request.getFlowLevel();
        if (ObjectUtil.isNotNull(flowLevel) && 0 == flowLevel.intValue()) {
            // 流向级别：0-未对接
            return flowCollectStatisticsCountBO;
        }

        // 运行中接口数量，包含基础对接、流向对接、已激活、已开启同步的
        ErpClientQuerySjmsRequest runningRequest = buildBaseErpClientQuerySjmsRequest(request);
        Integer runningCount = erpClientService.getRunningCountByRkSuIdListForPage(runningRequest);
        flowCollectStatisticsCountBO.setRunningCount(ObjectUtil.isNull(runningCount) ? 0 : runningCount);

        // 未上传昨日流向数量，从昨天开始（包含昨天）、时间向前推3天、取最新流向日期大于4天之前的，仅包含流向对接
        Date yesterday = DateUtil.yesterday();
        Date noDataDay1 = DateUtil.offsetDay(yesterday, Math.negateExact(1));
//        DateTime noDataDay1Begin = DateUtil.beginOfDay(noDataDay1);
        DateTime noDataDay1End = DateUtil.endOfDay(noDataDay1);
        ErpClientQuerySjmsRequest noDataDay1Request = buildBaseErpClientQuerySjmsRequest(request);
        noDataDay1Request.setLastestFlowDateEnd(noDataDay1End);
        Integer noDataYesterdayCount = erpClientService.getNoDatCountByRkSuIdListAndFlowDateForPage(noDataDay1Request);
        flowCollectStatisticsCountBO.setNoDataYesterdayCount(ObjectUtil.isNull(noDataYesterdayCount) ? 0 : noDataYesterdayCount);

        // 超3天未上传流向数量，从昨天开始（包含昨天）、时间向前推3天、取最新流向日期大于4天之前的，仅包含流向对接
        Date noDataDayEnd3 = DateUtil.offsetDay(DateUtil.endOfDay(yesterday), Math.negateExact(4));
        ErpClientQuerySjmsRequest noDataDay3Request = buildBaseErpClientQuerySjmsRequest(request);
        noDataDay3Request.setLastestFlowDateEnd(noDataDayEnd3);
        Integer noDataMoreThan3DaysCount = erpClientService.getNoDatCountByRkSuIdListAndFlowDateForPage(noDataDay3Request);
        flowCollectStatisticsCountBO.setNoDataMoreThan3DaysCount(ObjectUtil.isNull(noDataMoreThan3DaysCount) ? 0 : noDataMoreThan3DaysCount);

        // 超7天未上传流向数量，从昨天开始（包含昨天）、时间向前推7天、取最新流向日期大于8天之前的，仅包含流向对接
        Date noDataDayEnd7 = DateUtil.offsetDay(DateUtil.endOfDay(yesterday), Math.negateExact(8));
        ErpClientQuerySjmsRequest noDataDay7Request = buildBaseErpClientQuerySjmsRequest(request);
        noDataDay7Request.setLastestFlowDateEnd(noDataDayEnd7);
        Integer noDataMoreThan7DaysCount = erpClientService.getNoDatCountByRkSuIdListAndFlowDateForPage(noDataDay7Request);
        flowCollectStatisticsCountBO.setNoDataMoreThan7DaysCount(ObjectUtil.isNull(noDataMoreThan7DaysCount) ? 0 : noDataMoreThan7DaysCount);
        return flowCollectStatisticsCountBO;
    }

    private ErpClientQuerySjmsRequest buildBaseErpClientQuerySjmsRequest(ErpClientQuerySjmsRequest request) {
        ErpClientQuerySjmsRequest runningRequest = new ErpClientQuerySjmsRequest();
        runningRequest.setLicenseNumberList(request.getLicenseNumberList());
        runningRequest.setFlowMode(request.getFlowMode());
        runningRequest.setDepthTimeStart(request.getDepthTimeStart());
        runningRequest.setDepthTimeEnd(request.getDepthTimeEnd());
        runningRequest.setLastestCollectDateStart(request.getLastestCollectDateStart());
        runningRequest.setLastestCollectDateEnd(request.getLastestCollectDateEnd());
        runningRequest.setRunningStatus(request.getRunningStatus());
        runningRequest.setFlowLevel(request.getFlowLevel());
        return runningRequest;
    }

    @Override
    public List<SjmsFlowMonitorNoDataSyncEnterpriseBO> getSjmsNoDataSyncEnterpriseList(List<String> licenseNumberList) {
        if (null == licenseNumberList) {
            return ListUtil.empty();
        }
        // 从昨天开始，统计未上传流向天数，已激活、已开启同步、流向已对接
        Date yesterday = DateUtil.beginOfDay(DateUtil.yesterday());
        return erpClientService.getNoDataSyncEnterpriseListByRkSuIdListAndEndDate(licenseNumberList, yesterday);
    }

    @Override
    public Page<ErpClientDTO> sjmsPage(ErpClientQuerySjmsRequest request) {
        try {
            return PojoUtils.map(erpClientService.sjmsPage(request), ErpClientDTO.class);
        } catch (Exception e) {
            log.error("[ErpClientApiImpl][sjmsPage] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public Map<Long, String> getInstallEmployeeByEidList(List<Long> eidList) {
        if (CollUtil.isEmpty(eidList)) {
            return MapUtil.empty();
        }
        return erpClientService.getInstallEmployeeByEidList(eidList);
    }

    @Override
    public Map<Long, String> getInstallEmployeeByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList) {
        if (CollUtil.isEmpty(crmEnterpriseIdList)) {
            return MapUtil.empty();
        }
        return erpClientService.getInstallEmployeeByCrmEnterpriseIdList(crmEnterpriseIdList);
    }

    @Override
    public List<ErpClientDTO> getByLicenseNumberList(List<String> licenseNumberList) {
        return PojoUtils.map(erpClientService.getByLicenseNumberList(licenseNumberList), ErpClientDTO.class);
    }

    @Override
    public List<ErpClientDTO> getByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList) {
        return PojoUtils.map(erpClientService.getByCrmEnterpriseIdList(crmEnterpriseIdList), ErpClientDTO.class);
    }

    @Override
    public List<ErpClientDTO> getWithDatascopeByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList) {
        return PojoUtils.map(erpClientService.getWithDatascopeByCrmEnterpriseIdList(crmEnterpriseIdList), ErpClientDTO.class);
    }

    @Override
    public ErpClientDTO getByCrmEnterpriseId(Long crmEnterpriseId) {
        Assert.notNull(crmEnterpriseId, "参数 crmEnterpriseId 不能为空");
        return PojoUtils.map(erpClientService.getByCrmEnterpriseId(crmEnterpriseId), ErpClientDTO.class);
    }


    private boolean saveErpClientNoHeartList(List<ErpClientDO> erpClientList, Date taskTime, Date opTime) {
        if (CollUtil.isEmpty(erpClientList)) {
            return true;
        }
        // 根据公司名称匹配crm企业表
        List<String> enameList = erpClientList.stream().filter(o -> StrUtil.isNotBlank(o.getClientName())).map(ErpClientDO::getClientName).distinct().collect(Collectors.toList());
        List<CrmEnterpriseDTO> crmEnterpriseList = crmEnterpriseApi.getCrmEnterpriseCodeByNameList(enameList);
        if(CollUtil.isEmpty(crmEnterpriseList)){
            return true;
        }
        Map<String, CrmEnterpriseDTO> crmEnterpriseMap = crmEnterpriseList.stream().collect(Collectors.toMap(o -> convertBrackets(o.getName()), o -> o, (v1,v2) -> v2));

        List<SaveErpClientNoHeartRequest> requestList = new ArrayList<>();
        for (ErpClientDO erpClientDO : erpClientList) {
            // 企业名称中英文括号导致匹配不上，转成英文进行匹配
            String clientName = convertBrackets(erpClientDO.getClientName());
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseMap.get(clientName);
            if(ObjectUtil.isNull(crmEnterpriseDTO)){
                continue;
            }
            SaveErpClientNoHeartRequest saveErpClientNoHeartRequest = new SaveErpClientNoHeartRequest();
            saveErpClientNoHeartRequest.setTaskTime(taskTime);
            saveErpClientNoHeartRequest.setOpTime(opTime);
            saveErpClientNoHeartRequest.setOpUserId(0L);
            saveErpClientNoHeartRequest.setSuId(erpClientDO.getSuId());
            saveErpClientNoHeartRequest.setSuDeptNo(erpClientDO.getSuDeptNo());
            saveErpClientNoHeartRequest.setRkSuId(erpClientDO.getRkSuId());
            saveErpClientNoHeartRequest.setClientName(erpClientDO.getClientName());
            saveErpClientNoHeartRequest.setFlowMode(convertFlowMode(erpClientDO.getFlowMode()));
            saveErpClientNoHeartRequest.setInstallEmployee(erpClientDO.getInstallEmployee());
            //todo crm信息，部门、省区、商务负责人工号、商务负责人 需要到supper表里面获取
//            saveErpClientNoHeartRequest.setCrmDepartment(crmEnterpriseDTO.getDepartment());
//            saveErpClientNoHeartRequest.setCrmProvince(crmEnterpriseDTO.getProvince());
//            saveErpClientNoHeartRequest.setCrmCommerceJobNumber(crmEnterpriseDTO.getCommerceJobNumber());
//            saveErpClientNoHeartRequest.setCrmCommerceLiablePerson(crmEnterpriseDTO.getCommerceLiablePerson());
            requestList.add(saveErpClientNoHeartRequest);
        }
        return erpClientNoHeartService.saveBatchErpClientNoHeart(requestList, (DateTime) taskTime);
    }

    private String convertBrackets(String str){
        if(StrUtil.isBlank(str)){
            return str;
        }
        if(str.contains("（")){
            str = str.replaceAll("（", "(");
        }
        if(str.contains("）")){
            str = str.replaceAll("）", ")");
        }
        return str;
    }

    /**
     * 对接方式类型转换：1-工具、2-ftp、3-第三方接口、4-以岭平台接口
     *
     * @param type
     * @return
     */
    private String convertFlowMode(Integer type) {
        if (ObjectUtil.isNull(type) || 0 == type.intValue()){
            return "";
        }
        switch (type){
            case 1:
                return "工具";
            case 2:
                return "ftp";
            case 3:
                return "第三方接口";
            case 4:
                return "以岭平台接口";
            default:
                return "";
        }
    }


    /**
     * 检查默认时间
     * @param time
     * @return
     */
    public boolean checkDefaultTime(Date time){
        if (ObjectUtil.isNotNull(time)
                && ObjectUtil.equal("1970-01-01 00:00:00", DateUtil.format(time, "yyyy-MM-dd HH:mm:ss"))) {
            return true;
        }
        return false;
    }

//
//    @Override
//    public DtoResponse<ErpClientDto> get(Long id) {
//        try {
//            return new DtoResponse<>(erpClientService.get(id));
//        } catch (Exception e) {
//            log.error("[ErpClientApiImpl][get] 异常！" + e.getMessage(), e);
//            throw new ServiceException(ResultCode.FAILURE);
//        }
//    }
//
//    @Override
//    public DtoResponse<Boolean> saveOrUpdate(ErpClientSaveRequest request) {
//        try {
//            Long id = erpClientService.saveOrUpdateErpClient(request);
//            request.setId(id);
//            return new DtoResponse<>(erpClientService.saveOrUpdate(request));
//        } catch (Exception e) {
//            log.error("[ErpClientApiImpl][saveOrUpdate] 异常！" + e.getMessage(), e);
//            throw new ServiceException(ResultCode.FAILURE);
//        }
//    }

}
