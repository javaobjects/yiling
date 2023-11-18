package com.yiling.dataflow.report.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.dataflow.agency.entity.CrmPharmacyDO;
import com.yiling.dataflow.agency.entity.CrmSupplierDO;
import com.yiling.dataflow.agency.service.CrmPharmacyService;
import com.yiling.dataflow.agency.service.CrmSupplierService;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationPinchRunnerDO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationPinchRunnerService;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupService;
import com.yiling.dataflow.crm.service.CrmGoodsInfoService;
import com.yiling.dataflow.report.dao.FlowWashPharmacyPurchaseReportMapper;
import com.yiling.dataflow.report.dto.FlowWashInventoryReportDTO;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.dto.request.CreateFlowWashReportRequest;
import com.yiling.dataflow.report.dto.request.FlowWashInventoryReportPageRequest;
import com.yiling.dataflow.report.dto.request.FlowWashSaleReportPageRequest;
import com.yiling.dataflow.report.dto.request.RemoveFlowWashStockReportRequest;
import com.yiling.dataflow.report.entity.FlowWashPharmacyPurchaseReportDO;
import com.yiling.dataflow.report.service.FlowWashInventoryReportService;
import com.yiling.dataflow.report.service.FlowWashPharmacyPurchaseReportService;
import com.yiling.dataflow.report.service.FlowWashSaleReportService;
import com.yiling.dataflow.utils.BackupUtil;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.dataflow.wash.enums.WashMappingStatusEnum;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 零售购进报表生成
 *
 * @author zhigang.guo
 * @date: 2023/3/7
 */
@Slf4j
@Component
public class FlowWashPharmacyPurchaseReportHandler {
    @Autowired
    private CrmEnterpriseService crmEnterpriseService;
    @Autowired
    private CrmGoodsInfoService crmGoodsInfoService;
    @Autowired
    private FlowWashSaleReportService washSaleReportService;
    @Autowired
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;
    @Autowired
    private FlowWashPharmacyPurchaseReportService reportService;
    @Autowired
    private FlowWashPharmacyPurchaseReportMapper pharmacyPurchaseReportMapper;
    @Autowired
    private CrmPharmacyService crmPharmacyService;
    @Autowired
    private CrmSupplierService crmSupplierService;
    @Autowired
    private FlowWashInventoryReportService inventoryReportService;
    @Autowired
    private CrmGoodsGroupService crmGoodsGroupService;
    @Autowired
    private RedisDistributedLock redisDistributedLock;
    @Autowired
    private CrmEnterpriseRelationPinchRunnerService crmEnterpriseRelationPinchRunnerService;


    /**
     * 设置零售购进报表数据
     *
     * @param request
     */
    public void generator(CreateFlowWashReportRequest request) {

        // 备份表前缀
        String tableSuffix = BackupUtil.generateTableSuffix(request.getYear(),request.getMonth());
        StopWatch stopWatch = new StopWatch();

        stopWatch.start("查询商品信息");
        List<CrmGoodsInfoDTO> crmGoodsInfoAll = crmGoodsInfoService.getBakCrmGoodsInfoAll(tableSuffix);
        stopWatch.stop();

        if (CollectionUtil.isEmpty(crmGoodsInfoAll)) {

            log.warn("未查询到备份的商品信息");
            return;
        }

        stopWatch.start("查询当前商业企业信息");
        CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getSuffixByCrmEnterpriseId(request.getCrmId(),tableSuffix);
        stopWatch.stop();

        if (crmEnterpriseDO == null) {

            throw new RuntimeException("企业档案信息不存在!");
        }

        // 当月数据
        String soMonth = DateUtil.format(DateUtil.parse(request.getYear() + "-" + request.getMonth(), "yyyy-MM"),"yyyy-MM");
        // 不是零售不统计
        if (CrmSupplyChainRoleEnum.PHARMACY != CrmSupplyChainRoleEnum.getFromCode(crmEnterpriseDO.getSupplyChainRole())) {

            return;
        }


        // 客户机构编码
        Long crmId = request.getCrmId();
        Map<Long, CrmGoodsInfoDTO> crmGoodsInfoDTOMap = crmGoodsInfoAll.stream().collect(Collectors.toMap(CrmGoodsInfoDTO::getGoodsCode, Function.identity()));
        List<FlowWashPharmacyPurchaseReportDO> pharmacyPurchaseReportDOList = Lists.newArrayList();
        // 800 分批一次
        List<List<Long>> subGoodCodeList = Lists.partition(crmGoodsInfoAll.stream().map(t -> t.getGoodsCode()).collect(Collectors.toList()), 800);

        int count = 0;

        for (List<Long> goodList : subGoodCodeList) {
            count++;

            //本月流向购进数据
            stopWatch.start("第:" + count + "批本月流向购进数据查询");
            Map<Long, List<FlowWashSaleReportDTO>> flowPurchaseWashQtyMap = this.selectWashSaleInfos(request.getYear(), request.getMonth(), null, crmId, goodList);
            stopWatch.stop();

            // 本月库存流向变化数据
            stopWatch.start("第:" + count + "批本月库存流向变化数据查询");
            Map<Long, List<FlowWashInventoryReportDTO>> selectWashInventoryInfoMap = this.selectWashInventoryInfos(soMonth, crmId, goodList);
            stopWatch.stop();

            stopWatch.start("第:" + count + "批构建数据");
            List<FlowWashPharmacyPurchaseReportDO> reportDOList = goodList.stream().map(t -> buildFlowWashHospitalStockReport(flowPurchaseWashQtyMap.get(t), selectWashInventoryInfoMap.get(t), crmGoodsInfoDTOMap.get(t), crmEnterpriseDO,soMonth,request)).filter(t -> ObjectUtil.isNotNull(t)).collect(Collectors.toList());
            stopWatch.stop();

            // 释放内存
            flowPurchaseWashQtyMap.clear();
            selectWashInventoryInfoMap.clear();


            if (CollectionUtils.isEmpty(reportDOList)) {

                continue;
            }

            // 设置窜货信息金额
            this.setFleeingGoodsInfo(reportDOList);
            // 设置三者关系数据
            stopWatch.start("第:" + count + "批设置三者关系");
            this.setEnterpriseRelationShip(reportDOList,tableSuffix);
            this.setEnterpriseRelationPinchRunner(reportDOList,tableSuffix);
            stopWatch.stop();

            // 设置商品分组
            stopWatch.start("第:" + count + "批设置商品分组");
            this.setGoodsProductGroup(reportDOList,tableSuffix);
            stopWatch.stop();

            // 设置零售商品信息
            stopWatch.start("第:" + count + "批设置零售商品信息");
            this.setCrmPharmacyInfo(reportDOList,tableSuffix);
            stopWatch.stop();

            pharmacyPurchaseReportDOList.addAll(reportDOList);

        }


        stopWatch.start("流向医疗数据插入");
        // 添加锁任务,防止数据插入重复
        createReport(request.getCrmId(),request.getYear(),request.getMonth(),soMonth,pharmacyPurchaseReportDOList);
        stopWatch.stop();

        log.info("零售机构Id:{},月份:{},生成零售购进报表数据,{}",request.getCrmId(),soMonth,stopWatch.prettyPrint());

    }

    /**
     * 生成报表
     * @param crmId 机构Id
     * @param year 年
     * @param month 月
     * @param soMonth 年月字符串
     * @param pharmacyPurchaseReportDOList 本次生成数据
     */
    private void createReport(Long crmId,Integer year,Integer month,String soMonth,List<FlowWashPharmacyPurchaseReportDO> pharmacyPurchaseReportDOList) {

        if (CollectionUtil.isEmpty(pharmacyPurchaseReportDOList)) {

            return;
        }

        // 添加锁任务,防止数据插入重复
        String lockId = "";
        String lockName = RedisKey.generate("flow","wash","pharmacyPurchaseReport",crmId + "",(year + "-" + month));
        try {
            lockId = redisDistributedLock.lock2(lockName, 60, 60*2, TimeUnit.SECONDS);
            // 清除本次报表数据
            removeStockReportRecord(crmId,soMonth);
            // 批量插入数据
            pharmacyPurchaseReportMapper.insertBatchSomeColumn(pharmacyPurchaseReportDOList);
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }


    /**
     * 构建报表终端数据
     *
     * @return
     */
    private FlowWashPharmacyPurchaseReportDO buildFlowWashHospitalStockReport(List<FlowWashSaleReportDTO> flowWashPurchaseQtyList, // 流向内购进
                                                                              List<FlowWashInventoryReportDTO> flowGoodsBatchDetailWashList, // 库存变化数据
                                                                              CrmGoodsInfoDTO crmGoodsInfoDTO, CrmEnterpriseDO crmEnterpriseDO,
                                                                              String soMonth,
                                                                              CreateFlowWashReportRequest request) {

        // 只要在本次清洗的月销售流向中，存在某个零售机构，就展示在列表中。  （需要判断月销售、月采购、月库存流向，无论买方卖方，只要存在当前零售机构）
        Boolean isProductData = false;
        // 流向内购进数量
        BigDecimal innerPurchaseQty = BigDecimal.ZERO;
        Long enterpriseId = 0l;
        if (CollectionUtils.isNotEmpty(flowGoodsBatchDetailWashList)) {
            isProductData = true;
            enterpriseId = flowGoodsBatchDetailWashList.stream().findFirst().get().getEnterpriseCersId();
        }
        if (CollectionUtils.isNotEmpty(flowWashPurchaseQtyList)) {
            isProductData = true;
            enterpriseId = flowWashPurchaseQtyList.stream().findFirst().get().getOrganizationCersId();
        }

        if (!isProductData) {
            return null;
        }

        // 窜货批复
        BigDecimal fleeingPurchaseQty = BigDecimal.ZERO;
        // 窜货扣减
        BigDecimal fleeingPurchaseReduceQty = BigDecimal.ZERO;
        // 销售申诉反流
        BigDecimal purchaseReverse = BigDecimal.ZERO;

        if (CollectionUtils.isNotEmpty(flowWashPurchaseQtyList)) {

            innerPurchaseQty = flowWashPurchaseQtyList.stream().filter(t -> FlowClassifyEnum.NORMAL == FlowClassifyEnum.getByCode(t.getFlowClassify())).map(t -> t.getFinalQuantity()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            fleeingPurchaseQty = flowWashPurchaseQtyList.stream().filter(t -> FlowClassifyEnum.FLOW_CROSS == FlowClassifyEnum.getByCode(t.getFlowClassify()) && CompareUtil.compare(t.getFinalQuantity(),BigDecimal.ZERO) > 0).map(t -> t.getFinalQuantity()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            fleeingPurchaseReduceQty  = flowWashPurchaseQtyList.stream().filter(t -> FlowClassifyEnum.FLOW_CROSS == FlowClassifyEnum.getByCode(t.getFlowClassify()) && CompareUtil.compare(t.getFinalQuantity(),BigDecimal.ZERO) < 0).map(t -> t.getFinalQuantity()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            purchaseReverse = flowWashPurchaseQtyList.stream().filter(t -> FlowClassifyEnum.SALE_APPEAL == FlowClassifyEnum.getByCode(t.getFlowClassify())).map(t -> t.getFinalQuantity()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        }

        FlowWashPharmacyPurchaseReportDO reportDO = new FlowWashPharmacyPurchaseReportDO();
        reportDO.initModel();

        if (CollectionUtils.isNotEmpty(flowGoodsBatchDetailWashList)) {
            FlowWashInventoryReportDTO reportDTO = flowGoodsBatchDetailWashList.stream().findFirst().get();

            // 设置三者关系
           /* reportDO.setProvincialArea(Optional.ofNullable(reportDTO.getProvincialArea()).orElse(""));
            reportDO.setBusinessProvince(Optional.ofNullable(reportDTO.getBusinessProvince()).orElse(""));
            reportDO.setDepartment(Optional.ofNullable(reportDTO.getDepartment()).orElse(""));
            reportDO.setBusinessDepartment(Optional.ofNullable(reportDTO.getBusinessDepartment()).orElse(""));
            reportDO.setDistrictCounty(Optional.ofNullable(reportDTO.getDistrictCounty()).orElse(""));
            reportDO.setSuperiorSupervisorCode(Optional.ofNullable(reportDTO.getSuperiorSupervisorCode()).orElse(""));
            reportDO.setSuperiorSupervisorName(Optional.ofNullable(reportDTO.getSuperiorSupervisorName()).orElse(""));
            reportDO.setRepresentativeCode(Optional.ofNullable(reportDTO.getRepresentativeCode()).orElse(""));
            reportDO.setRepresentativeName(Optional.ofNullable(reportDTO.getRepresentativeName()).orElse(""));
            reportDO.setPostCode(Optional.ofNullable(reportDTO.getPostCode()).orElse(0l));
            reportDO.setPostName(Optional.ofNullable(reportDTO.getPostName()).orElse(""));
            reportDO.setProductGroup(Optional.ofNullable(reportDTO.getProductGroup()).orElse(""));*/

        } else  if (CollectionUtil.isNotEmpty(flowWashPurchaseQtyList)) {

            FlowWashSaleReportDTO reportDTO = flowWashPurchaseQtyList.stream().findFirst().get();
            // 设置三者关系
           /* reportDO.setProvincialArea(Optional.ofNullable(reportDTO.getProvincialArea()).orElse(""));
            reportDO.setBusinessProvince(Optional.ofNullable(reportDTO.getBusinessProvince()).orElse(""));
            reportDO.setDepartment(Optional.ofNullable(reportDTO.getDepartment()).orElse(""));
            reportDO.setBusinessDepartment(Optional.ofNullable(reportDTO.getBusinessDepartment()).orElse(""));
            reportDO.setDistrictCounty(Optional.ofNullable(reportDTO.getDistrictCounty()).orElse(""));
            reportDO.setSuperiorSupervisorCode(Optional.ofNullable(reportDTO.getSuperiorSupervisorCode()).orElse(""));
            reportDO.setSuperiorSupervisorName(Optional.ofNullable(reportDTO.getSuperiorSupervisorName()).orElse(""));
            reportDO.setRepresentativeCode(Optional.ofNullable(reportDTO.getRepresentativeCode()).orElse(""));
            reportDO.setRepresentativeName(Optional.ofNullable(reportDTO.getRepresentativeName()).orElse(""));
            reportDO.setPostCode(Optional.ofNullable(reportDTO.getPostCode()).orElse(0l));
            reportDO.setPostName(Optional.ofNullable(reportDTO.getPostName()).orElse(""));
            reportDO.setProductGroup(Optional.ofNullable(reportDTO.getProductGroup()).orElse(""));*/
        }

        reportDO.setEnterpriseCersId(Optional.ofNullable(enterpriseId).orElse(0l));
        reportDO.setSoMonth(soMonth);
        reportDO.setCrmId(Optional.ofNullable(request.getCrmId()).orElse(0l));
        reportDO.setName(Optional.ofNullable(crmEnterpriseDO.getName()).orElse(""));
        reportDO.setGoodsCode(Optional.ofNullable(crmGoodsInfoDTO.getGoodsCode()).orElse(0l));
        reportDO.setGoodsName(Optional.ofNullable(crmGoodsInfoDTO.getGoodsName()).orElse(""));
        reportDO.setSpecifications(Optional.ofNullable(crmGoodsInfoDTO.getGoodsSpec()).orElse(""));
        reportDO.setPrice(Optional.ofNullable(crmGoodsInfoDTO.getSupplyPrice()).orElse(BigDecimal.ZERO));
        reportDO.setInnerPurchaseQty(innerPurchaseQty);

        // 初始化窜货以及销量申诉数量
        reportDO.setFleeingPurchaseQty(fleeingPurchaseQty);
        reportDO.setFleeingPurchaseReduceQty(fleeingPurchaseReduceQty);
        reportDO.setPurchaseReverse(purchaseReverse);

        reportDO.setProvinceName(Optional.ofNullable(crmEnterpriseDO.getProvinceName()).orElse(""));
        reportDO.setCityName(Optional.ofNullable(crmEnterpriseDO.getCityName()).orElse(""));
        reportDO.setRegionName(Optional.ofNullable(crmEnterpriseDO.getRegionName()).orElse(""));
        reportDO.setProvinceCode(Optional.ofNullable(crmEnterpriseDO.getProvinceCode()).orElse(""));
        reportDO.setCityCode(Optional.ofNullable(crmEnterpriseDO.getCityCode()).orElse(""));
        reportDO.setRegionCode(Optional.ofNullable(crmEnterpriseDO.getRegionCode()).orElse(""));

        return reportDO;
    }

    /**
     * 设置窜货信息
     *
     * @param reportDOList
     */
    private void setFleeingGoodsInfo(List<FlowWashPharmacyPurchaseReportDO> reportDOList) {

        reportDOList.forEach(reportDo -> {
            // 流向外购进合计  E=（B+C+D)
            reportDo.setOutterPurchaseSum(NumberUtil.add(reportDo.getFleeingPurchaseQty(), reportDo.getFleeingPurchaseReduceQty(), reportDo.getPurchaseReverse()));
            // 购进数量合计 F=（A+E）
            reportDo.setPurchaseSum(NumberUtil.add(reportDo.getOutterPurchaseSum(), reportDo.getInnerPurchaseQty()));
            // 购进总金额
            reportDo.setPurchaseSumMoney(NumberUtil.mul(reportDo.getPurchaseSum(), reportDo.getPrice()));
        });

    }


    /**
     * 清除本次报表数据
     *
     * @param crmId
     */
    private void removeStockReportRecord(Long crmId,String soMonth) {

        RemoveFlowWashStockReportRequest request = new RemoveFlowWashStockReportRequest();
        request.setSoMonth(soMonth);
        request.setCrmId(crmId);

        reportService.removeByCrmId(request);
    }


    /**
     * 销量清洗任务
     *
     * @param month
     * @param customerCrmId
     * @param goodCodeList
     * @return
     */
    private Map<Long, List<FlowWashSaleReportDTO>> selectWashSaleInfos(Integer year, Integer month, Long crmId, Long customerCrmId, List<Long> goodCodeList) {
        List<FlowWashSaleReportDTO> saleWashDOList = Lists.newArrayList();
        Page<FlowWashSaleReportDTO> page;

        int current = 1;
        int size = 1000;

        FlowWashSaleReportPageRequest request = new FlowWashSaleReportPageRequest();

        do {
            request.setCurrent(current);
            request.setSize(size);
            request.setYear(year.toString());
            request.setMonth(month.toString());
            request.setCrmId(crmId);
            request.setCustomerCrmId(customerCrmId);
            request.setGoodsCodeList(goodCodeList);
            request.setMappingStatus(WashMappingStatusEnum.MATCH_SUCCESS.getCode());
            request.setFlowClassifyList(ListUtil.toList(FlowClassifyEnum.NORMAL.getCode(),FlowClassifyEnum.SALE_APPEAL.getCode(),FlowClassifyEnum.FLOW_CROSS.getCode(),FlowClassifyEnum.HOSPITAL_DRUGSTORE.getCode()));

            page = washSaleReportService.pageList(request);

            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            current = current + 1;

            saleWashDOList.addAll(page.getRecords());
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));

        if (CollectionUtils.isEmpty(saleWashDOList)) {
            return MapUtil.empty();
        }

        if (crmId == null) {
            log.info("本次查询本月流向购进数据条数:{}",saleWashDOList.size());
        } else {
            log.info("本次查询流向本月卖出数据条数:{}",saleWashDOList.size());
        }

        return saleWashDOList.stream().collect(Collectors.groupingBy(FlowWashSaleReportDTO::getGoodsCode));
    }




    /**
     * 庫存清洗任务
     *
     * @param soMonth
     * @param crmId
     * @param goodCodeList
     * @return
     */
    private Map<Long,List<FlowWashInventoryReportDTO>> selectWashInventoryInfos(String soMonth, Long crmId, List<Long> goodCodeList) {

        List<FlowWashInventoryReportDTO> saleWashDOList = Lists.newArrayList();
        Page<FlowWashInventoryReportDTO> page;
        int current = 1;
        int size = 1000;
        FlowWashInventoryReportPageRequest request = new FlowWashInventoryReportPageRequest();

        do {
            request.setCurrent(current);
            request.setSize(size);
            request.setSoMonth(soMonth);
            request.setCrmId(crmId);
            request.setGoodsCodeList(goodCodeList);
            request.setMappingStatus(WashMappingStatusEnum.MATCH_SUCCESS.getCode());

            page = inventoryReportService.pageList(request);

            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            current = current + 1;

            saleWashDOList.addAll(page.getRecords());
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));

        if (CollectionUtils.isEmpty(saleWashDOList)) {

            return MapUtil.empty();
        }

        log.info("本次查询本月库存流向变化数据数据条数:{}",saleWashDOList.size());

        return  saleWashDOList.stream().collect(Collectors.groupingBy(FlowWashInventoryReportDTO::getGoodsCode));
    }

    /**
     * 设置企业三者关系数据
     *
     * @param reportDOList
     */
    private void setEnterpriseRelationShip(List<FlowWashPharmacyPurchaseReportDO> reportDOList,String tableSuffix) {

        List<Long> organizationCersIds = reportDOList.stream()
                .filter(t -> t.getEnterpriseCersId() != 0)
                // 三者关系数据还未填充的,重新查询
                .filter(t -> CompareUtil.compare(t.getPostCode(),0l) == 0)
                .map(t -> t.getEnterpriseCersId()).distinct().collect(Collectors.toList());

        if (CollectionUtil.isEmpty(organizationCersIds)) {

            return;
        }

        List<CrmEnterpriseRelationShipDO> crmEnterpriseRelationShipDOS = crmEnterpriseRelationShipService.listSuffixByIdList(organizationCersIds,tableSuffix);
        if (CollectionUtil.isEmpty(crmEnterpriseRelationShipDOS)) {
            return;
        }

        Map<Long, CrmEnterpriseRelationShipDO> crmEnterpriseMap = crmEnterpriseRelationShipDOS.stream().collect(Collectors.toMap(t -> t.getId(), Function.identity()));

        reportDOList.stream()
                .filter(t -> t.getEnterpriseCersId() != 0) // 三者关系部门数据还未填充的,重新查询
                .filter(t -> CompareUtil.compare(t.getPostCode(),0l) == 0).forEach(t -> {

            CrmEnterpriseRelationShipDO crmEnterpriseRelationShipDO = crmEnterpriseMap.getOrDefault(t.getEnterpriseCersId(), new CrmEnterpriseRelationShipDO());
            t.setProvincialArea(Optional.ofNullable(crmEnterpriseRelationShipDO.getProvincialArea()).orElse(""));
            t.setBusinessProvince(Optional.ofNullable(crmEnterpriseRelationShipDO.getBusinessProvince()).orElse(""));
            t.setDepartment(Optional.ofNullable(crmEnterpriseRelationShipDO.getDepartment()).orElse(""));
            t.setBusinessDepartment(Optional.ofNullable(crmEnterpriseRelationShipDO.getBusinessDepartment()).orElse(""));
            t.setDistrictCounty(Optional.ofNullable(crmEnterpriseRelationShipDO.getBusinessArea()).orElse(""));
            t.setSuperiorSupervisorCode(Optional.ofNullable(crmEnterpriseRelationShipDO.getSuperiorSupervisorCode()).orElse(""));
            t.setSuperiorSupervisorName(Optional.ofNullable(crmEnterpriseRelationShipDO.getSuperiorSupervisorName()).orElse(""));
            t.setRepresentativeCode(Optional.ofNullable(crmEnterpriseRelationShipDO.getRepresentativeCode()).orElse(""));
            t.setRepresentativeName(Optional.ofNullable(crmEnterpriseRelationShipDO.getRepresentativeName()).orElse(""));
            t.setPostCode(Optional.ofNullable(crmEnterpriseRelationShipDO.getPostCode()).orElse(0l));
            t.setPostName(Optional.ofNullable(crmEnterpriseRelationShipDO.getPostName()).orElse(""));
            // 临时存储商品分组Id
            t.setProductGroupId(Optional.ofNullable(crmEnterpriseRelationShipDO.getProductGroupId()).orElse(0l));
            t.setSubstituteRunning(Optional.ofNullable(crmEnterpriseRelationShipDO.getSubstituteRunning()).orElse(1));
            t.setProvinceManagerCode(Optional.ofNullable(crmEnterpriseRelationShipDO.getProvincialManagerCode()).orElse(""));
            t.setProvinceManagerName(Optional.ofNullable(crmEnterpriseRelationShipDO.getProvincialManagerName()).orElse(""));
        });

        if (log.isDebugEnabled()) {
            log.debug("设置企业三者关系数据:{}", reportDOList);
        }
    }


    /**
     * 设置三者关系代跑数据
     * @param reportDOList
     * @param tableSuffix
     */
    private void setEnterpriseRelationPinchRunner(List<FlowWashPharmacyPurchaseReportDO> reportDOList, String tableSuffix) {

        List<Long> organizationCersIdList = reportDOList.stream().filter(t -> t.getSubstituteRunning() == 2 && t.getEnterpriseCersId() != 0).map(t -> t.getEnterpriseCersId()).distinct().collect(Collectors.toList());

        if (CollectionUtil.isEmpty(organizationCersIdList)) {

            return;
        }

        List<CrmEnterpriseRelationPinchRunnerDO> crmEnterpriseRelationPinchRunnerList = crmEnterpriseRelationPinchRunnerService.getByCrmEnterpriseIdAndRelationShipIds(null,organizationCersIdList,tableSuffix);

        if (CollectionUtil.isEmpty(crmEnterpriseRelationPinchRunnerList)) {

            return;
        }

        Map<Long, CrmEnterpriseRelationPinchRunnerDO> crmEnterpriseRelationPinchRunnerMap = crmEnterpriseRelationPinchRunnerList.stream().collect(Collectors.toMap(CrmEnterpriseRelationPinchRunnerDO::getEnterpriseCersId, Function.identity()));
        reportDOList.stream().filter(t -> t.getSubstituteRunning() == 2 && t.getEnterpriseCersId() != 0).forEach(t -> {
            CrmEnterpriseRelationPinchRunnerDO crmEnterpriseRelationPinchRunnerDO = crmEnterpriseRelationPinchRunnerMap.get(t.getEnterpriseCersId());
            t.setSuperiorSupervisorCode(Optional.ofNullable(crmEnterpriseRelationPinchRunnerDO).map(z -> z.getBusinessSuperiorCode()).orElse(""));
            t.setSuperiorSupervisorName(Optional.ofNullable(crmEnterpriseRelationPinchRunnerDO).map(z -> z.getBusinessSuperiorName()).orElse(""));
            t.setBusinessDepartment(Optional.ofNullable(crmEnterpriseRelationPinchRunnerDO).map(z -> z.getBusinessSuperiorDepartment()).orElse(""));
            t.setBusinessProvince(Optional.ofNullable(crmEnterpriseRelationPinchRunnerDO).map(z -> z.getBusinessSuperiorProvince()).orElse(""));
            t.setProvincialArea(Optional.ofNullable(crmEnterpriseRelationPinchRunnerDO).map(z -> z.getSuperiorProvincial()).orElse(""));
            t.setDepartment(Optional.ofNullable(crmEnterpriseRelationPinchRunnerDO).map(z -> z.getSuperiorDepartment()).orElse(""));
            t.setProvinceManagerCode(Optional.ofNullable(crmEnterpriseRelationPinchRunnerDO).map(z -> z.getProvincialManagerCode()).orElse(""));
            t.setProvinceManagerName(Optional.ofNullable(crmEnterpriseRelationPinchRunnerDO).map(z -> z.getProvincialManagerName()).orElse(""));
        });

        if (log.isDebugEnabled()) {
            log.debug(" 设置三者关系代跑数据:{}", reportDOList);
        }
    }



    /**
     * 设置商品分组
     * @param saleReportDOList
     */
    private void setGoodsProductGroup(List<FlowWashPharmacyPurchaseReportDO> saleReportDOList, String tableSuffix) {

        List<Long> productGroupIds = saleReportDOList.stream().filter(t -> t.getProductGroupId() != 0).map(t -> t.getProductGroupId()).distinct().collect(Collectors.toList());

        if (CollectionUtil.isEmpty(productGroupIds)) {

            return;
        }

        List<CrmGoodsGroupDTO> goodsGroupDOList = crmGoodsGroupService.findBakGroupByIds(productGroupIds,tableSuffix);

        if (CollectionUtil.isEmpty(goodsGroupDOList)) {

            return;
        }

        Map<Long, CrmGoodsGroupDTO> crmGoodsGroupDOMap = goodsGroupDOList.stream().collect(Collectors.toMap(CrmGoodsGroupDTO::getId, Function.identity()));
        saleReportDOList.stream().filter(t -> t.getProductGroupId() != 0).forEach(t -> {
            CrmGoodsGroupDTO crmGoodsGroupDO = crmGoodsGroupDOMap.get(t.getProductGroupId());
            t.setProductGroup(Optional.ofNullable(crmGoodsGroupDO).map(z -> z.getName()).orElse(""));
        });

        if (log.isDebugEnabled()) {
            log.debug("设置商品组关系数据:{}", saleReportDOList);
        }
    }


    /**
     * 设置零售商品信息
     *
     * @param pharmacySaleReportDOList
     */
    private void setCrmPharmacyInfo(List<FlowWashPharmacyPurchaseReportDO> pharmacySaleReportDOList,String tableSuffix) {

        if (CollectionUtil.isEmpty(pharmacySaleReportDOList)) {

            return;
        }

        List<Long> pharmacyCrmIdList = pharmacySaleReportDOList.stream()
                .map(t -> t.getCrmId()).filter(t -> t != 0).distinct().collect(Collectors.toList());

        if (CollectionUtil.isEmpty(pharmacyCrmIdList)) {

            return;
        }

        List<CrmPharmacyDO> crmPharmacyDOList = crmPharmacyService.listSuffixByCrmEnterpriseIdList(pharmacyCrmIdList,tableSuffix);
        if (CollectionUtil.isEmpty(crmPharmacyDOList)) {

            return;
        }

        Map<Long, CrmPharmacyDO> crmEnterpriseMap = crmPharmacyDOList.stream().collect(Collectors.toMap(t -> t.getCrmEnterpriseId(), Function.identity()));
        pharmacySaleReportDOList.stream()
                // 药店属性未填充
                .filter(t -> t.getCrmId() != 0)
                .forEach(saleReportDO -> {
            CrmPharmacyDO crmPharmacyDO = crmEnterpriseMap.getOrDefault(saleReportDO.getCrmId(), new CrmPharmacyDO());
            saleReportDO.setPharmacyAttribute(Optional.ofNullable(crmPharmacyDO.getPharmacyAttribute()).orElse(0));
            saleReportDO.setPharmacyType(Optional.ofNullable(crmPharmacyDO.getPharmacyType()).orElse(0));
            saleReportDO.setPharmacyLevel(Optional.ofNullable(crmPharmacyDO.getPharmacyLevel()).orElse(0));
            saleReportDO.setParentCompanyCode(Optional.ofNullable(crmPharmacyDO.getParentCompanyCode()).orElse(""));
            saleReportDO.setLabelAttribute(Optional.ofNullable(crmPharmacyDO.getLabelAttribute()).orElse(0));
            saleReportDO.setTargetFlag(Optional.ofNullable(crmPharmacyDO.getTargetFlag()).orElse(0));
            saleReportDO.setParentSupplierName(Optional.ofNullable(crmPharmacyDO.getParentCompanyName()).orElse(""));
            saleReportDO.setParentSupplierCode(Optional.ofNullable(crmPharmacyDO.getParentCompanyCode()).orElse(""));
            saleReportDO.setLockTime(Optional.ofNullable(crmPharmacyDO.getFirstLockTime()).orElse(DateUtil.parse("1970-01-01", "yyyy-MM-dd")));
            saleReportDO.setLastUnLockTime(Optional.ofNullable(crmPharmacyDO.getLastUnlockTime()).orElse(DateUtil.parse("1970-01-01", "yyyy-MM-dd")));
        });


        // 设置上级公司连锁属性
        List<Long> parentCrmIds = pharmacySaleReportDOList.stream().filter(t -> StringUtils.isNotBlank(t.getParentCompanyCode())).map(t -> Long.valueOf(t.getParentCompanyCode())).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(parentCrmIds)) {

            return;
        }

        List<CrmSupplierDO> supplierList = crmSupplierService.listSuffixByCrmEnterpriseIdList(parentCrmIds,tableSuffix);

        if (CollectionUtil.isEmpty(supplierList)) {

            return;
        }

        Map<Long, CrmSupplierDO> crmSupplyEnterpriseMap = supplierList.stream().collect(Collectors.toMap(t -> t.getCrmEnterpriseId(), Function.identity()));
        pharmacySaleReportDOList.stream().filter((t -> StringUtils.isNotBlank(t.getParentCompanyCode()))).forEach(saleReportDO -> {
            CrmSupplierDO crmSupplierDO = crmSupplyEnterpriseMap.getOrDefault(Long.valueOf(saleReportDO.getParentCompanyCode()), new CrmSupplierDO());
            saleReportDO.setPharmacyChainAttribute(Optional.ofNullable(crmSupplierDO.getChainAttribute()).orElse(0));
        });

        if (log.isDebugEnabled()) {
            log.debug("设置零售商品信息:{}", pharmacySaleReportDOList);
        }
    }
}
