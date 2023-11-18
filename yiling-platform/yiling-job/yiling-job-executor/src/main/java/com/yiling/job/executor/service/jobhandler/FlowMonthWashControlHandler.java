package com.yiling.job.executor.service.jobhandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.dataflow.agency.enums.AgencySupplyChainRoleEnum;
import com.yiling.dataflow.backup.api.AgencyBackupApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseBackUpPageRequest;
import com.yiling.dataflow.order.api.FlowGoodsBatchDetailApi;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.dataflow.report.api.FlowWashReportApi;
import com.yiling.dataflow.report.dto.FlowWashHospitalStockReportDTO;
import com.yiling.dataflow.report.dto.FlowWashInventoryReportDTO;
import com.yiling.dataflow.report.dto.request.CreateFlowWashReportRequest;
import com.yiling.dataflow.report.dto.request.FlowWashHospitalStockReportPageRequest;
import com.yiling.dataflow.report.dto.request.FlowWashInventoryReportPageRequest;
import com.yiling.dataflow.report.dto.request.QueryFlowWashSaleReportRequest;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.api.FlowMonthWashTaskApi;
import com.yiling.dataflow.wash.api.FlowTerminalOrderApi;
import com.yiling.dataflow.wash.dto.FlowGoodsBatchTransitDTO;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchTransitPageRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowMonthWashControlPageRequest;
import com.yiling.dataflow.wash.enums.FlowGoodsBatchTransitTypeEnum;
import com.yiling.dataflow.wash.enums.FlowMonthWashControlMappingStatusEnum;
import com.yiling.dataflow.wash.enums.FlowMonthWashControlStatusEnum;
import com.yiling.dataflow.wash.enums.WashErrorEnum;
import com.yiling.dataflow.wash.enums.WashMappingStatusEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.job.executor.log.JobLog;
import com.yiling.open.erp.api.ErpClientApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: shuang.zhang
 * @date: 2023/3/3
 */
@Slf4j
@Component
public class FlowMonthWashControlHandler {

    @DubboReference
    private FlowMonthWashTaskApi    flowMonthWashTaskApi;
    @DubboReference(timeout = 1000 * 60 * 10)
    private CrmEnterpriseApi        crmEnterpriseApi;
    @DubboReference(timeout = 1000 * 60)
    private FlowMonthWashControlApi flowMonthWashControlApi;
    @DubboReference
    private FlowPurchaseApi         flowPurchaseApi;
    @DubboReference
    private FlowSaleApi             flowSaleApi;
    @DubboReference
    private FlowGoodsBatchDetailApi flowGoodsBatchDetailApi;
    @DubboReference
    private ErpClientApi            erpClientApi;
    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;
    @DubboReference
    private AgencyBackupApi         agencyBackupApi;
    @DubboReference
    private FlowTerminalOrderApi    flowTerminalOrderApi;
    @DubboReference(timeout = 1000 * 60 * 10)
    private FlowWashReportApi       flowWashReportApi;
    @DubboReference
    private CrmGoodsInfoApi         crmGoodsInfoApi;

    /**
     * 每小时更新日程里面的状态信息
     *
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("flowMonthWashControlJobHandler")
    public ReturnT<String> flowMonthWashControlJobHandler(String param) throws Exception {
        log.info("任务开始：月流向清洗日程调度开始");
//        QueryFlowMonthWashControlPageRequest request = new QueryFlowMonthWashControlPageRequest();
//        List<FlowMonthWashControlDTO> flowMonthWashControlDTOList = new ArrayList<>();
//        Page<FlowMonthWashControlDTO> page = null;
//        int current = 1;
//        do {
//            request.setCurrent(current);
//            request.setSize(2000);
//            page = flowMonthWashControlApi.listPage(request);
//            if (page == null || CollUtil.isEmpty(page.getRecords())) {
//                break;
//            }
//            flowMonthWashControlDTOList.addAll(page.getRecords());
//            current = current + 1;
//        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
//
//        for (FlowMonthWashControlDTO flowMonthWashControlDTO : flowMonthWashControlDTOList) {
//            if (flowMonthWashControlDTO.getStatus().equals(FlowMonthWashControlStatusEnum.FINISH.getCode())) {
//                continue;
//            }
//            try {
//                String tableSuffix = flowMonthWashControlDTO.getYear() + "" + String.format("%02d", flowMonthWashControlDTO.getMonth());
//                List<ErpClientSimpleBO> erpClientSimpleBOList = new ArrayList<>();
//                //商品映射结束方法
//                if (flowMonthWashControlDTO.getGoodsMappingEndTime().getTime() < System.currentTimeMillis()) {
//                    if (FlowMonthWashControlStatusEnum.NOT_OPEN.getCode().equals(flowMonthWashControlDTO.getStatus())) {
//                        CheckAgencyBackupRequest checkAgencyBackupRequest = new CheckAgencyBackupRequest();
//                        checkAgencyBackupRequest.setYearMonth(Long.parseLong(tableSuffix));
//                        boolean check = agencyBackupApi.checkAgencyBackupByYearMonth(checkAgencyBackupRequest);
//                        if (check) {
//                            flowMonthWashControlDTO.setBackupStatus(1);
//                        }
//                    }
//                    if (flowMonthWashControlDTO.getBackupStatus()==1) {
//                        flowMonthWashControlDTO.setGoodsMappingStatus(FlowMonthWashControlMappingStatusEnum.CLOSE.getCode());
//                        flowMonthWashControlDTO.setTaskStatus(1);
//                        List<Long> crmEnterpriseDTOList = pageCrmEnterprise(AgencySupplyChainRoleEnum.SUPPLIER.getCode(), 1, tableSuffix);
//                        List<Long> crmEnterpriseIds = flowMonthWashTaskApi.getCrmEnterIdsByFmwcIdAndClassify(flowMonthWashControlDTO.getId(), FlowClassifyEnum.NORMAL.getCode());
//                        crmEnterpriseDTOList = crmEnterpriseDTOList.stream().filter(e -> !crmEnterpriseIds.contains(e)).collect(Collectors.toList());
//                        List<ErpClientDTO> erpClientDTOList = erpClientApi.getByCrmEnterpriseIdList(crmEnterpriseDTOList);
//                        for (ErpClientDTO erpClientDTO : erpClientDTOList) {
//                            if (crmEnterpriseDTOList.contains(erpClientDTO.getCrmEnterpriseId())) {
//                                continue;
//                            }
//                            flowGoodsBatchDetailApi.statisticsFlowGoodsBatch(Arrays.asList(erpClientDTO.getRkSuId()));
//                            QueryFlowPurchaseExistRequest queryFlowPurchaseExistRequest = new QueryFlowPurchaseExistRequest();
//                            queryFlowPurchaseExistRequest.setPoTimeStart(flowMonthWashControlDTO.getDataStartTime());
//                            queryFlowPurchaseExistRequest.setPoTimeEnd(flowMonthWashControlDTO.getDataEndTime());
//                            queryFlowPurchaseExistRequest.setEid(erpClientDTO.getRkSuId());
//                            boolean isHaveDataByEidAndPoTime = flowPurchaseApi.isHaveDataByEidAndPoTime(queryFlowPurchaseExistRequest);
//                            boolean isHaveDataByEidAndSoTime = false;
//                            boolean isHaveDataByEidAndGbDetailTime = false;
//                            if (!isHaveDataByEidAndPoTime) {
//                                QueryFlowSaleExistRequest queryFlowSaleExistRequest = new QueryFlowSaleExistRequest();
//                                queryFlowSaleExistRequest.setSoTimeStart(flowMonthWashControlDTO.getDataStartTime());
//                                queryFlowSaleExistRequest.setSoTimeEnd(flowMonthWashControlDTO.getDataEndTime());
//                                queryFlowSaleExistRequest.setEid(erpClientDTO.getRkSuId());
//                                isHaveDataByEidAndSoTime = flowSaleApi.isHaveDataByEidAndSoTime(queryFlowSaleExistRequest);
//                            }
//                            if (!isHaveDataByEidAndPoTime && !isHaveDataByEidAndSoTime) {
//                                QueryFlowGoodsBatchDetailExistRequest queryFlowGoodsBatchDetailExistRequest = new QueryFlowGoodsBatchDetailExistRequest();
//                                queryFlowGoodsBatchDetailExistRequest.setEid(erpClientDTO.getRkSuId());
//                                queryFlowGoodsBatchDetailExistRequest.setGbDetailTime(DateUtil.parseDate(DateUtil.formatDate(flowMonthWashControlDTO.getDataEndTime())));
//                                isHaveDataByEidAndGbDetailTime = flowGoodsBatchDetailApi.isHaveDataByEidAndGbDetailTime(queryFlowGoodsBatchDetailExistRequest);
//                            }
//                            if (isHaveDataByEidAndPoTime || isHaveDataByEidAndSoTime || isHaveDataByEidAndGbDetailTime) {
//                                ErpClientSimpleBO erpClientSimpleBO = new ErpClientSimpleBO();
//                                erpClientSimpleBO.setCrmEnterpriseId(erpClientDTO.getCrmEnterpriseId());
//                                erpClientSimpleBO.setEid(erpClientDTO.getRkSuId());
//                                erpClientSimpleBO.setFlowMode(erpClientDTO.getFlowMode());
//                                erpClientSimpleBO.setLicenseNumber(erpClientDTO.getLicenseNumber());
//                                erpClientSimpleBOList.add(erpClientSimpleBO);
//                            }
//                        }
//                    }
//                }
//                SaveOrUpdateFlowMonthWashControlRequest saveOrUpdateFlowMonthWashControlRequest = PojoUtils.map(flowMonthWashControlDTO, SaveOrUpdateFlowMonthWashControlRequest.class);
//                saveOrUpdateFlowMonthWashControlRequest.setErpClientSimpleBOList(erpClientSimpleBOList);
//                flowMonthWashControlApi.saveOrUpdate(saveOrUpdateFlowMonthWashControlRequest);
//            } catch (Exception e) {
//                log.error("任务调度异常", e);
//            }
//        }
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("washSumNotifyJobHandler")
    public ReturnT<String> washSumNotifyJobHandler(String param) throws Exception {
        log.info("任务开始：流向清洗完成汇总报表调度开始");
        QueryFlowMonthWashControlPageRequest request = new QueryFlowMonthWashControlPageRequest();
        List<FlowMonthWashControlDTO> flowMonthWashControlDTOList = new ArrayList<>();
        Page<FlowMonthWashControlDTO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(2000);
            page = flowMonthWashControlApi.listPage(request);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            flowMonthWashControlDTOList.addAll(page.getRecords());
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));

        for (FlowMonthWashControlDTO flowMonthWashControlDTO : flowMonthWashControlDTOList) {
            if (flowMonthWashControlDTO.getTaskStatus()==2) {
                continue;
            }
            String tableSuffix = flowMonthWashControlDTO.getYear() + "" + String.format("%02d", flowMonthWashControlDTO.getMonth());
            // 上月数据
            String lastMonth = DateUtil.format(DateUtil.offsetMonth(DateUtil.parse(flowMonthWashControlDTO.getYear() + "-" + flowMonthWashControlDTO.getMonth(), "yyyy-MM"), -1), "yyyy-MM");
            // 当月数据
            String soMonth = DateUtil.format(DateUtil.parse(flowMonthWashControlDTO.getYear() + "-" + flowMonthWashControlDTO.getMonth(), "yyyy-MM"), "yyyy-MM");

            List<CrmGoodsInfoDTO> crmGoodsInfoAll = crmGoodsInfoApi.getBakCrmGoodsInfoAll("wash_" + tableSuffix);
            if (CollectionUtil.isEmpty(crmGoodsInfoAll)) {
                log.warn("未查询到备份的商品信息");
                return ReturnT.SUCCESS;
            }

            if (flowMonthWashControlDTO.getWashStatus()>1) {
                List<Long> crmEnterpriseSupplierDTOList = pageCrmEnterprise(AgencySupplyChainRoleEnum.SUPPLIER.getCode(), 1, tableSuffix);
                flowWashReportApi.removeSupplyStockReportBySoMonth(soMonth);
                if (CollUtil.isNotEmpty(crmEnterpriseSupplierDTOList)) {
                    List<Long> crmEnterpriseIds = flowMonthWashTaskApi.getCrmEnterIdsByFmwcIdAndClassify(flowMonthWashControlDTO.getId(), 0);
                    for (Long crmEnterpriseId : crmEnterpriseSupplierDTOList) {
                        if (crmEnterpriseIds.contains(crmEnterpriseId)) {
                            CreateFlowWashReportRequest reportRequest = new CreateFlowWashReportRequest();
                            reportRequest.setYear(flowMonthWashControlDTO.getYear());
                            reportRequest.setMonth(flowMonthWashControlDTO.getMonth());
                            reportRequest.setReportType(CreateFlowWashReportRequest.ReportTypeEnum.SUPPLY);
                            reportRequest.setCrmId(crmEnterpriseId);
                            SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_WASH_TASK_FINISH_SUM_NOTIFY, Constants.TAG_WASH_TASK_FINISH_SUM_NOTIFY, DateUtil.formatDate(new Date()), JSON.toJSONString(reportRequest));
                            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                                throw new BusinessException(WashErrorEnum.FLOW_WASH_ERROR);
                            }
                        }
                    }
                }

                //本月流向数据
                List<Long> goodList = crmGoodsInfoAll.stream().map(e -> e.getGoodsCode()).collect(Collectors.toList());
                List<Long> crmIdList = this.selectWashSaleCrmIds(flowMonthWashControlDTO.getYear(), flowMonthWashControlDTO.getMonth());
                crmIdList = crmIdList.stream().filter(e -> e > 0).collect(Collectors.toList());
                List<Long> customerCrmIds = this.selectWashSaleCustomerCrmIds(flowMonthWashControlDTO.getYear(), flowMonthWashControlDTO.getMonth());
                customerCrmIds = customerCrmIds.stream().filter(e -> e > 0).collect(Collectors.toList());
                List<Long> crmEnterpriseHospitalDTOList = pageCrmEnterprise(AgencySupplyChainRoleEnum.HOSPITAL.getCode(), 0, tableSuffix);
                flowWashReportApi.removeHospitalStockReportBySoMonth(soMonth);
                if (CollUtil.isNotEmpty(crmEnterpriseHospitalDTOList)) {
                    Set<Long> ids = new HashSet<>();
                    // 本月终端订单信息
                    List<FlowGoodsBatchTransitDTO> flowGoodsBatchTransitQtyList = this.selectFlowGoodsBatchTransits(soMonth, goodList);
                    ids.addAll(flowGoodsBatchTransitQtyList.stream().map(e -> e.getCrmEnterpriseId()).collect(Collectors.toList()));
                    //本月流向数据
                    ids.addAll(customerCrmIds);
                    // 上个月报表数据
                    List<FlowWashHospitalStockReportDTO> flowWashHospitalStockReportDTOS = this.selectLastHospitalStockReportRecords(lastMonth, goodList);
                    ids.addAll(flowWashHospitalStockReportDTOS.stream().filter(e -> CompareUtil.compare(e.getEndQty(), BigDecimal.ZERO) != 0).map(e -> e.getCrmId()).collect(Collectors.toList()));

                    for (Long crmEnterpriseId : crmEnterpriseHospitalDTOList) {
                        if (ids.contains(crmEnterpriseId)) {
                            CreateFlowWashReportRequest reportRequest = new CreateFlowWashReportRequest();
                            reportRequest.setYear(flowMonthWashControlDTO.getYear());
                            reportRequest.setMonth(flowMonthWashControlDTO.getMonth());
                            reportRequest.setReportType(CreateFlowWashReportRequest.ReportTypeEnum.HOSPITAL);
                            reportRequest.setCrmId(crmEnterpriseId);
                            SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_WASH_TASK_FINISH_SUM_NOTIFY, Constants.TAG_WASH_TASK_FINISH_SUM_NOTIFY, DateUtil.formatDate(new Date()), JSON.toJSONString(reportRequest));
                            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                                throw new BusinessException(WashErrorEnum.FLOW_WASH_ERROR);
                            }
                        }
                    }
                }

                List<Long> crmEnterprisePharmacyDTOList = pageCrmEnterprise(AgencySupplyChainRoleEnum.PHARMACY.getCode(), 0, tableSuffix);
                flowWashReportApi.removePharmacyPurchaseReportBySoMonth(soMonth);
                if (CollUtil.isNotEmpty(crmEnterprisePharmacyDTOList)) {

                    Set<Long> ids = new HashSet<>();
                    //本月购进数据
                    ids.addAll(customerCrmIds);
                    // 本月卖出
                    ids.addAll(crmIdList);
                    // 本月库存流向变化数据
                    List<FlowWashInventoryReportDTO> selectWashInventoryList = this.selectWashInventoryInfos(soMonth, goodList);
                    ids.addAll(selectWashInventoryList.stream().filter(e -> e.getCrmId() != 0).map(e -> e.getCrmId()).collect(Collectors.toList()));

                    for (Long crmEnterpriseId : crmEnterprisePharmacyDTOList) {
                        if (ids.contains(crmEnterpriseId)) {
                            CreateFlowWashReportRequest reportRequest = new CreateFlowWashReportRequest();
                            reportRequest.setYear(flowMonthWashControlDTO.getYear());
                            reportRequest.setMonth(flowMonthWashControlDTO.getMonth());
                            reportRequest.setReportType(CreateFlowWashReportRequest.ReportTypeEnum.PHARMACY);
                            reportRequest.setCrmId(crmEnterpriseId);
                            SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_WASH_TASK_FINISH_SUM_NOTIFY, Constants.TAG_WASH_TASK_FINISH_SUM_NOTIFY, DateUtil.formatDate(new Date()), JSON.toJSONString(reportRequest));
                            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                                throw new BusinessException(WashErrorEnum.FLOW_WASH_ERROR);
                            }
                        }
                    }
                }
            }
        }
        log.info("任务结束：流向清洗完成汇总报表调度结束");
        return ReturnT.SUCCESS;
    }

    /**
     * 查询终端订单信息
     *
     * @param soMonth
     * @param goodCodeList
     * @return
     */
    private List<FlowGoodsBatchTransitDTO> selectFlowGoodsBatchTransits(String soMonth, List<Long> goodCodeList) {

        List<FlowGoodsBatchTransitDTO> saleWashDOList = Lists.newArrayList();
        Page<FlowGoodsBatchTransitDTO> page;

        int current = 1;
        int size = 1000;

        QueryFlowGoodsBatchTransitPageRequest request = new QueryFlowGoodsBatchTransitPageRequest();

        do {
            request.setCurrent(current);
            request.setSize(size);
            request.setGoodsBatchType(FlowGoodsBatchTransitTypeEnum.TERMINAL.getCode());
            request.setCrmGoodsCodeList(goodCodeList);
            request.setGbDetailMonth(soMonth);

            page = flowTerminalOrderApi.listPage(request);

            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            current = current + 1;

            saleWashDOList.addAll(page.getRecords());
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));
        return saleWashDOList;
    }

    /**
     * 销量清洗任务
     *
     * @param month
     * @return
     */
    private List<Long> selectWashSaleCrmIds(Integer year, Integer month) {
        QueryFlowWashSaleReportRequest request = new QueryFlowWashSaleReportRequest();
        request.setYear(year.toString());
        request.setMonth(month.toString());
        request.setMappingStatus(WashMappingStatusEnum.MATCH_SUCCESS.getCode());
        return flowWashReportApi.listCrmIdBySaleReportCondition(request);
    }

    /**
     * 销量清洗任务
     *
     * @param month
     * @return
     */
    private List<Long> selectWashSaleCustomerCrmIds(Integer year, Integer month) {
        QueryFlowWashSaleReportRequest request = new QueryFlowWashSaleReportRequest();
        request.setYear(year.toString());
        request.setMonth(month.toString());
        request.setMappingStatus(WashMappingStatusEnum.MATCH_SUCCESS.getCode());
        return flowWashReportApi.listCustomerCrmIdBySaleReportCondition(request);
    }


    /**
     * 查询上个月的医疗进销存数据
     *
     * @param lastMonth
     * @param goodCodeList
     * @return
     */
    private List<FlowWashHospitalStockReportDTO> selectLastHospitalStockReportRecords(String lastMonth, List<Long> goodCodeList) {
        List<FlowWashHospitalStockReportDTO> saleWashDOList = Lists.newArrayList();
        Page<FlowWashHospitalStockReportDTO> page;

        int current = 1;
        int size = 1000;

        FlowWashHospitalStockReportPageRequest request = new FlowWashHospitalStockReportPageRequest();

        do {
            request.setCurrent(current);
            request.setSize(size);
            request.setSoMonth(lastMonth);
            request.setGoodsCodeList(goodCodeList);

            page = flowWashReportApi.hospitalStockReportPageList(request);

            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            current = current + 1;
            saleWashDOList.addAll(page.getRecords());
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));

        return saleWashDOList;
    }

    /**
     * 庫存清洗任务
     *
     * @param soMonth
     * @param goodCodeList
     * @return
     */
    private List<FlowWashInventoryReportDTO> selectWashInventoryInfos(String soMonth, List<Long> goodCodeList) {

        List<FlowWashInventoryReportDTO> saleWashDOList = Lists.newArrayList();
        Page<FlowWashInventoryReportDTO> page;
        int current = 1;
        int size = 1000;
        FlowWashInventoryReportPageRequest request = new FlowWashInventoryReportPageRequest();

        do {
            request.setCurrent(current);
            request.setSize(size);
            request.setSoMonth(soMonth);
            request.setGoodsCodeList(goodCodeList);
            request.setMappingStatus(WashMappingStatusEnum.MATCH_SUCCESS.getCode());

            page = flowWashReportApi.inventoryReportPageList(request);

            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            current = current + 1;

            saleWashDOList.addAll(page.getRecords());
        } while (page != null && com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(page.getRecords()));

        return saleWashDOList;
    }

    public List<Long> pageCrmEnterprise(Integer roleId, Integer businessCode, String tableSuffix) {
        //查询所有erp对接的企业信息
        List<Long> suIdList = new ArrayList<>();
        QueryCrmEnterpriseBackUpPageRequest request = new QueryCrmEnterpriseBackUpPageRequest();
        request.setSupplyChainRole(roleId);
        if (businessCode != null && businessCode != 0) {
            request.setBusinessCode(businessCode);
        }
        request.setSoMonth(tableSuffix);
        //需要循环调用
        Page<CrmEnterpriseDTO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(2000);
            page = crmEnterpriseApi.pageListSuffixBackUpInfo(request);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            for (CrmEnterpriseDTO crmEnterpriseDO : page.getRecords()) {
                suIdList.add(crmEnterpriseDO.getId());
            }
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return suIdList;
    }

    /**
     * 重新清洗任务调度
     *
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("reWashJobHandler")
    public ReturnT<String> reWashJobHandler(String param) throws Exception {
        String command = XxlJobHelper.getJobParam();
        flowMonthWashTaskApi.reWash(Long.parseLong(command));
        return ReturnT.SUCCESS;
    }
}
