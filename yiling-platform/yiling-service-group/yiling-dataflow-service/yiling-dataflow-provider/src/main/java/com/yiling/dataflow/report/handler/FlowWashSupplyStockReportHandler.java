package com.yiling.dataflow.report.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.dataflow.agency.entity.CrmSupplierDO;
import com.yiling.dataflow.agency.enums.CrmBusinessCodeEnum;
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
import com.yiling.dataflow.report.dto.FlowWashInventoryReportDTO;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.dto.FlowWashSupplyStockReportDTO;
import com.yiling.dataflow.report.dto.request.CreateFlowWashReportRequest;
import com.yiling.dataflow.report.dto.request.FlowWashInventoryReportPageRequest;
import com.yiling.dataflow.report.dto.request.FlowWashSaleReportPageRequest;
import com.yiling.dataflow.report.dto.request.FlowWashSupplyStockReportPageRequest;
import com.yiling.dataflow.report.dto.request.RemoveFlowWashStockReportRequest;
import com.yiling.dataflow.report.entity.FlowWashSupplyStockReportDO;
import com.yiling.dataflow.report.service.FlowWashInventoryReportService;
import com.yiling.dataflow.report.service.FlowWashSaleReportService;
import com.yiling.dataflow.report.service.FlowWashSupplyStockReportService;
import com.yiling.dataflow.utils.BackupUtil;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchTransitPageRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowPurchaseWashPageRequest;
import com.yiling.dataflow.wash.entity.FlowGoodsBatchTransitDO;
import com.yiling.dataflow.wash.entity.FlowPurchaseWashDO;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.dataflow.wash.enums.FlowDataWashStatusEnum;
import com.yiling.dataflow.wash.enums.FlowGoodsBatchTransitTypeEnum;
import com.yiling.dataflow.wash.enums.WashMappingStatusEnum;
import com.yiling.dataflow.wash.service.FlowGoodsBatchTransitService;
import com.yiling.dataflow.wash.service.FlowPurchaseWashService;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.util.Constants;

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
 * 流向商业进销存报表
 *
 * @author zhigang.guo
 * @date: 2023/3/6
 */
@Slf4j
@Component
public class FlowWashSupplyStockReportHandler {
    @Autowired
    private CrmEnterpriseService crmEnterpriseService;
    @Autowired
    private CrmGoodsInfoService crmGoodsInfoService;
    @Autowired
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;
    @Autowired
    private FlowWashSupplyStockReportService supplyStockReportService;
    @Autowired
    private FlowWashSaleReportService flowWashSaleReportService;
    @Autowired
    private FlowWashInventoryReportService inventoryReportService;
    @Autowired
    private FlowGoodsBatchTransitService transitService;
    @Autowired
    private RedisDistributedLock redisDistributedLock;
    @Autowired
    private FlowPurchaseWashService purchaseWashService;
    @Autowired
    private CrmSupplierService crmSupplierService;
    @Autowired
    private CrmGoodsGroupService crmGoodsGroupService;
    @Autowired
    private CrmEnterpriseRelationPinchRunnerService crmEnterpriseRelationPinchRunnerService;

    /**
     * 设置商业经销存库存报表数据
     *
     * @param request
     */
    public void generator(CreateFlowWashReportRequest request) {
        // 备份表前缀
        String tableSuffix = BackupUtil.generateTableSuffix(request.getYear(),request.getMonth());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("查询所有商品信息");
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

        // 上月数据
        String lastMonth = DateUtil.format(DateUtil.offsetMonth(DateUtil.parse(request.getYear() + "-" + request.getMonth(), "yyyy-MM"), -1),"yyyy-MM");
        // 当月数据
        String soMonth = DateUtil.format(DateUtil.parse(request.getYear() + "-" + request.getMonth(), "yyyy-MM"),"yyyy-MM");


        // 不是商业不统计
        if (CrmSupplyChainRoleEnum.DISTRIBUTOR != CrmSupplyChainRoleEnum.getFromCode(crmEnterpriseDO.getSupplyChainRole())) {

            return;
        }
        // 失效
        if (CrmBusinessCodeEnum.SIMPLE.getCode() == crmEnterpriseDO.getBusinessCode()) {

            return;
        }

        //经销商机构编码
        Long crmId = request.getCrmId();
        Map<Long, CrmGoodsInfoDTO> crmGoodsInfoDTOMap = crmGoodsInfoAll.stream().collect(Collectors.toMap(CrmGoodsInfoDTO::getGoodsCode, Function.identity()));
        // 本月库存
        stopWatch.start("本月库存查询");
        Map<Long, List<FlowWashInventoryReportDTO>> goodsBatchDetailWashMap = this.selectWashInventoryInfos(soMonth, crmId, null);
        stopWatch.stop();

        // 上个月库存
        stopWatch.start("上个月库存查询");
        Map<Long, List<FlowWashSupplyStockReportDTO>> lastGoodsBatchDetailWashMap = this.selectLastSupplyStockReportList(lastMonth, crmId, null);
        stopWatch.stop();

        // 本月销量
        stopWatch.start("本月销量查询");
        Map<Long, List<FlowWashSaleReportDTO>> flowSaleWashDOListMap = this.selectWashSaleInfos(request.getYear(), request.getMonth(), crmId, null, null);
        stopWatch.stop();

        // 本月购进
        stopWatch.start("本月购进查询");
        Map<Long, List<FlowPurchaseWashDO>> flowPurchaseWashDOListMap = this.selectWashPurchaseInfos(request.getYear(), request.getMonth(), crmId, null);
        stopWatch.stop();

        List<Long> goodList = crmGoodsInfoAll.stream().map(t -> t.getGoodsCode()).collect(Collectors.toList());

        // 生成报表数据
        stopWatch.start("报表数据生成");
        List<FlowWashSupplyStockReportDO> reportDOList = goodList.stream().map(t -> buildFlowWashSupplyStockReport(lastGoodsBatchDetailWashMap.get(t), goodsBatchDetailWashMap.get(t), flowSaleWashDOListMap.get(t), flowPurchaseWashDOListMap.get(t), crmGoodsInfoDTOMap.get(t), crmEnterpriseDO,soMonth,request)).filter(t -> ObjectUtil.isNotNull(t)).collect(Collectors.toList());
        stopWatch.stop();

        // 清空对象引用
        crmGoodsInfoAll.clear();
        goodsBatchDetailWashMap.clear();
        lastGoodsBatchDetailWashMap.clear();
        flowSaleWashDOListMap.clear();
        flowPurchaseWashDOListMap.clear();

        if (CollectionUtil.isEmpty(reportDOList)) {
            return;
        }

        // 三者关系
        stopWatch.start("设置三者关系");
        List<List<FlowWashSupplyStockReportDO>> subReportDOList = Lists.partition(reportDOList, 800);
        subReportDOList.forEach(t -> { setEnterpriseRelationShip(t,tableSuffix); setEnterpriseRelationPinchRunner(t,tableSuffix);setCrmSupplyInfo(t,tableSuffix);this.setGoodsProductGroup(t,tableSuffix);});
        stopWatch.stop();

        // 设置库存信息
        stopWatch.start("设置库存信息");
        setFlowWashInventoryInfo(soMonth, crmId, reportDOList);
        stopWatch.stop();


        // 添加锁任务,防止数据插入重复
        stopWatch.start("商业进销存数据插入");
        createReport(request.getCrmId(),request.getYear(),request.getMonth(),soMonth,reportDOList);
        stopWatch.stop();

        log.info("经销商Id:{},月份:{},生成商业进销存报表数据,{}",request.getCrmId(),soMonth,stopWatch.prettyPrint());
    }

    /**
     * 生成报表
     * @param crmId 机构Id
     * @param year 年
     * @param month 月
     * @param soMonth 年月字符串
     * @param supplyStockReportDOS 本次生成数据
     */
    private void createReport(Long crmId,Integer year,Integer month,String soMonth,List<FlowWashSupplyStockReportDO> supplyStockReportDOS) {

        if (CollectionUtil.isEmpty(supplyStockReportDOS)) {

            return;
        }

        // 添加锁任务,防止数据插入重复
        String lockId = "";
        String lockName = RedisKey.generate("flow","wash","supplyStockReport",crmId + "",(year + "-" + month));
        try {
            lockId = redisDistributedLock.lock2(lockName, 60, 60, TimeUnit.SECONDS);
            // 清除本次报表数据
            removeStockReportRecord(crmId,soMonth);
            // 数据插入
            supplyStockReportService.saveBatch(supplyStockReportDOS);
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }


    /**
     * 设置企业三者关系数据
     *
     * @param reportDOList
     */
    private void setEnterpriseRelationShip(List<FlowWashSupplyStockReportDO> reportDOList,String tableSuffix) {

        List<Long> organizationCersIds = reportDOList.stream().filter(t -> t.getEnterpriseCersId() != 0).filter(t -> CompareUtil.compare(t.getPostCode(),0l) == 0).map(t -> t.getEnterpriseCersId()).distinct().collect(Collectors.toList());

        if (CollectionUtil.isEmpty(organizationCersIds)) {

            return;
        }

        List<CrmEnterpriseRelationShipDO> crmEnterpriseRelationShipDOS = crmEnterpriseRelationShipService.listSuffixByIdList(organizationCersIds,tableSuffix);
        if (CollectionUtil.isEmpty(crmEnterpriseRelationShipDOS)) {
            return;
        }

        Map<Long, CrmEnterpriseRelationShipDO> crmEnterpriseMap = crmEnterpriseRelationShipDOS.stream().collect(Collectors.toMap(t -> t.getId(), Function.identity()));
        reportDOList.stream().filter(t -> t.getEnterpriseCersId() != 0).filter(t -> CompareUtil.compare(t.getPostCode(),0l) == 0).forEach(t -> {
            CrmEnterpriseRelationShipDO crmEnterpriseRelationShipDO = crmEnterpriseMap.getOrDefault(t.getEnterpriseCersId(), new CrmEnterpriseRelationShipDO());
            t.setProvincialArea(crmEnterpriseRelationShipDO.getProvincialArea());
            t.setBusinessProvince(crmEnterpriseRelationShipDO.getBusinessProvince());
            t.setDepartment(crmEnterpriseRelationShipDO.getDepartment());
            t.setBusinessDepartment(crmEnterpriseRelationShipDO.getBusinessDepartment());
            t.setDistrictCounty(crmEnterpriseRelationShipDO.getBusinessArea());
            t.setSuperiorSupervisorCode(crmEnterpriseRelationShipDO.getSuperiorSupervisorCode());
            t.setSuperiorSupervisorName(crmEnterpriseRelationShipDO.getSuperiorSupervisorName());
            t.setRepresentativeCode(crmEnterpriseRelationShipDO.getRepresentativeCode());
            t.setRepresentativeName(crmEnterpriseRelationShipDO.getRepresentativeName());
            t.setPostCode(crmEnterpriseRelationShipDO.getPostCode());
            t.setPostName(crmEnterpriseRelationShipDO.getPostName());
            // 临时存储商品分组Id
            t.setProductGroupId(Optional.ofNullable(crmEnterpriseRelationShipDO.getProductGroupId()).orElse(0l));
            // 是否代跑
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
     * @param saleReportDOList
     * @param tableSuffix
     */
    private void setEnterpriseRelationPinchRunner(List<FlowWashSupplyStockReportDO> saleReportDOList, String tableSuffix) {

        List<Long> organizationCersIdList = saleReportDOList.stream().filter(t -> t.getSubstituteRunning() == 2 && t.getEnterpriseCersId() != 0).map(t -> t.getEnterpriseCersId()).distinct().collect(Collectors.toList());

        if (CollectionUtil.isEmpty(organizationCersIdList)) {

            return;
        }

        List<CrmEnterpriseRelationPinchRunnerDO> crmEnterpriseRelationPinchRunnerList = crmEnterpriseRelationPinchRunnerService.getByCrmEnterpriseIdAndRelationShipIds(null,organizationCersIdList,tableSuffix);

        if (CollectionUtil.isEmpty(crmEnterpriseRelationPinchRunnerList)) {
            return;
        }

        Map<Long, CrmEnterpriseRelationPinchRunnerDO> crmEnterpriseRelationPinchRunnerMap = crmEnterpriseRelationPinchRunnerList.stream().collect(Collectors.toMap(CrmEnterpriseRelationPinchRunnerDO::getEnterpriseCersId, Function.identity()));
        saleReportDOList.stream().filter(t -> t.getSubstituteRunning() == 2 && t.getEnterpriseCersId() != 0).forEach(t -> {
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
            log.debug(" 设置三者关系代跑数据:{}", saleReportDOList);
        }
    }


    /**
     * 设置商品分组
     * @param saleReportDOList
     */
    private void setGoodsProductGroup(List<FlowWashSupplyStockReportDO> saleReportDOList,String tableSuffix) {

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
     * 设置商业供应商信息
     *
     * @param reportDOList
     */
    private void setCrmSupplyInfo(List<FlowWashSupplyStockReportDO> reportDOList, String tableSuffix) {
        // 供应商信息
        List<Long> crmIdList = reportDOList.stream().map(t -> t.getCrmId()).filter(t -> t != 0).distinct().collect(Collectors.toList());

        if (CollectionUtil.isEmpty(crmIdList)) {

            return;
        }

        List<CrmSupplierDO> supplierList = crmSupplierService.listSuffixByCrmEnterpriseIdList(crmIdList,tableSuffix);

        if (CollectionUtil.isEmpty(supplierList)) {

            return;
        }

        Map<Long, CrmSupplierDO> crmEnterpriseMap = supplierList.stream().collect(Collectors.toMap(t -> t.getCrmEnterpriseId(), Function.identity()));
        reportDOList.stream().forEach(saleReportDO -> {
            CrmSupplierDO supplier = Optional.ofNullable(crmEnterpriseMap.get(saleReportDO.getCrmId())).orElse(new CrmSupplierDO());
            saleReportDO.setSupplierLevel(Optional.ofNullable(supplier.getSupplierLevel()).orElse(0));
            saleReportDO.setSupplierAttribute(Optional.ofNullable(supplier.getSupplierAttribute()).orElse(0));
            saleReportDO.setGeneralMedicineLevel(Optional.ofNullable(supplier.getGeneralMedicineLevel()).orElse(0));
            saleReportDO.setSupplierSaleType(Optional.ofNullable(supplier.getSupplierSaleType()).orElse(0));
            saleReportDO.setBaseSupplierInfo(Optional.ofNullable(supplier.getBaseSupplierInfo()).orElse(0));
            saleReportDO.setHeadChainFlag(Optional.ofNullable(supplier.getHeadChainFlag()).orElse(0));
            saleReportDO.setChainAttribute(Optional.ofNullable(supplier.getChainAttribute()).orElse(0));
            saleReportDO.setChainKaFlag(Optional.ofNullable(supplier.getChainKaFlag()).orElse(0));
            saleReportDO.setLockTime(Optional.ofNullable(supplier.getFirstLockTime()).orElse(DateUtil.parse("1970-01-01", "yyyy-MM-dd")));
            saleReportDO.setLastUnLockTime(Optional.ofNullable(supplier.getLastUnlockTime()).orElse(DateUtil.parse("1970-01-01", "yyyy-MM-dd")));
        });


        if (log.isDebugEnabled()) {
            log.debug("设置商业供应商信息:{}", reportDOList);
        }
    }

    /**
     * 设置报表库存信息
     *
     * @param reportDOList
     */
    private void setFlowWashInventoryInfo(String soMonth, Long crmId, List<FlowWashSupplyStockReportDO> reportDOList) {

        // 查询在途库存信息
        Map<Long, List<FlowGoodsBatchTransitDO>> flowGoodsBatchTransitMap = this.selectFlowGoodsBatchTransits(soMonth, crmId, null);

        reportDOList.forEach(reportDO -> {
            // 在途库存数量
            BigDecimal onWayQty = BigDecimal.ZERO;
            List<FlowGoodsBatchTransitDO> goodsBatchTransitList = flowGoodsBatchTransitMap.get(reportDO.getGoodsCode());
            if (CollectionUtils.isNotEmpty(goodsBatchTransitList)) {
                onWayQty = goodsBatchTransitList.stream().map(t -> t.getGbNumber()).reduce(BigDecimal::add).get();
            }
            // 设置在途库存数量
            reportDO.setOnwayQty(onWayQty);

            // 本月计算库存数量
            // E=（A+B-C-D） 上月库存数量 + 本月购进数量 - 本月销售数量 - 本月在途数量
            BigDecimal sumQty = NumberUtil.add(reportDO.getLastMonthStockQty(), reportDO.getPurchaseQty());
            sumQty = NumberUtil.sub(NumberUtil.sub(sumQty, onWayQty), reportDO.getSaleQty());

            reportDO.setSumQty(sumQty);
            reportDO.setDiffQty(NumberUtil.sub(reportDO.getFinalQty(), sumQty));

            // 期末库存数量 S=（F+D） 本月实际库存数+ 本月在途数量
            reportDO.setEndQty(NumberUtil.add(reportDO.getFinalQty(), onWayQty));

            // 上月在途金额
            reportDO.setOnwayMoney(NumberUtil.mul(reportDO.getPrice(), reportDO.getOnwayQty()));
            reportDO.setOnwayMoney(NumberUtil.div(reportDO.getOnwayMoney(), 10000));

            // 本月计算库存金额（万元）L=（H+I-J+K）
            // 上月库存金额（万元） + 本月购进金额（万元） - 本月销售金额（万元) - 本月在途金额（万元）
            BigDecimal sumMoney = NumberUtil.sub(NumberUtil.add(reportDO.getLastMonthStockMoney(), reportDO.getPurchaseMoney()), reportDO.getOnwayMoney());
            sumMoney = NumberUtil.sub(sumMoney, reportDO.getSaleMoney());
            reportDO.setSumMoney(sumMoney);


            // 库存实际金额
            reportDO.setFinalMoney(NumberUtil.mul(reportDO.getFinalQty(), reportDO.getPrice()));
            reportDO.setFinalMoney(NumberUtil.div(reportDO.getFinalMoney(), 10000));
            // 库存差异金额
            reportDO.setDiffMoney(NumberUtil.sub(reportDO.getFinalMoney(), reportDO.getSumMoney()));

            // 期末库存金额（万元）（T=M+K） 本月实际库存金额（万元) + 本月在途金额（万元)
            reportDO.setEndMoney(NumberUtil.add(reportDO.getFinalMoney(), reportDO.getOnwayMoney()));
        });

    }


    /**
     * 构建商业库存报表数据
     *
     * @param flowGoodsBatchDetailWashList
     * @param lastFlowGoodsBatchDetailWashList
     * @param flowSaleWashDOList
     * @param flowPurchaseWashDOList
     * @param crmGoodsInfoDTO
     * @return
     */
    private FlowWashSupplyStockReportDO buildFlowWashSupplyStockReport(List<FlowWashSupplyStockReportDTO> lastFlowGoodsBatchDetailWashList, //上个月库存信息
                                                                       List<FlowWashInventoryReportDTO> flowGoodsBatchDetailWashList, // 本月库存信息
                                                                       List<FlowWashSaleReportDTO> flowSaleWashDOList, // 本月销售信息
                                                                       List<FlowPurchaseWashDO> flowPurchaseWashDOList, // 本月购进信息
                                                                       CrmGoodsInfoDTO crmGoodsInfoDTO, // 商品信息
                                                                       CrmEnterpriseDO crmEnterpriseDO, // 企业信息
                                                                       String soMonth, // 所属月份
                                                                       CreateFlowWashReportRequest request) {

        // 此列表中展示的商业公司需要同时满足2个条件：（1）商业公司档案中的状态为“有效”。（2）商业公司在月流向清洗中，有流向数据（销售/采购/库存）。
        if (CollectionUtil.isEmpty(flowGoodsBatchDetailWashList) && CollectionUtil.isEmpty(flowSaleWashDOList) && CollectionUtil.isEmpty(flowPurchaseWashDOList)) {

            return null;
        }

        FlowWashSupplyStockReportDO flowWashSupplyStockReportDO = new FlowWashSupplyStockReportDO();
        // 初始化
        flowWashSupplyStockReportDO.setSupplierLevel(0);
        flowWashSupplyStockReportDO.setPostCode(0l);
        flowWashSupplyStockReportDO.setProductGroupId(0l);
        flowWashSupplyStockReportDO.setSubstituteRunning(1);

        Long enterpriseId;
        if (CollectionUtil.isNotEmpty(flowSaleWashDOList)) {
            FlowWashSaleReportDTO reportDTO = flowSaleWashDOList.stream().findFirst().get();
            enterpriseId = reportDTO.getEnterpriseCersId();

            // 如果是销售流向的,商业扩展信息直接取商业流向报表
            flowWashSupplyStockReportDO.setSupplierLevel(reportDTO.getSupplierLevel());
            flowWashSupplyStockReportDO.setSupplierAttribute(reportDTO.getSupplierAttribute());
            flowWashSupplyStockReportDO.setGeneralMedicineLevel(reportDTO.getGeneralMedicineLevel());
            flowWashSupplyStockReportDO.setSupplierSaleType(reportDTO.getSupplierSaleType());
            flowWashSupplyStockReportDO.setBaseSupplierInfo(reportDTO.getBaseSupplierInfo());
            flowWashSupplyStockReportDO.setHeadChainFlag(reportDTO.getHeadChainFlag());
            flowWashSupplyStockReportDO.setChainAttribute(reportDTO.getChainAttribute());
            flowWashSupplyStockReportDO.setChainKaFlag(reportDTO.getChainKaFlag());

        } else if (CollectionUtil.isNotEmpty(flowPurchaseWashDOList)){
            FlowPurchaseWashDO reportDTO = flowPurchaseWashDOList.stream().findFirst().get();
            enterpriseId = reportDTO.getEnterpriseCersId();

        } else  {

            FlowWashInventoryReportDTO reportDTO = flowGoodsBatchDetailWashList.stream().findFirst().get();
            enterpriseId = reportDTO.getEnterpriseCersId();

            // 如果是销售流向的,商业扩展信息直接取商业流向报表
            flowWashSupplyStockReportDO.setSupplierLevel(reportDTO.getSupplierLevel());
            flowWashSupplyStockReportDO.setSupplierAttribute(reportDTO.getSupplierAttribute());
            flowWashSupplyStockReportDO.setGeneralMedicineLevel(reportDTO.getGeneralMedicineLevel());
            flowWashSupplyStockReportDO.setSupplierSaleType(reportDTO.getSupplierSaleType());
            flowWashSupplyStockReportDO.setBaseSupplierInfo(reportDTO.getBaseSupplierInfo());
            flowWashSupplyStockReportDO.setHeadChainFlag(reportDTO.getHeadChainFlag());
            flowWashSupplyStockReportDO.setChainAttribute(reportDTO.getChainAttribute());
            flowWashSupplyStockReportDO.setChainKaFlag(reportDTO.getChainKaFlag());

            // 设置三者关系数据
            /*flowWashSupplyStockReportDO.setProvincialArea(reportDTO.getProvincialArea());
            flowWashSupplyStockReportDO.setBusinessProvince(reportDTO.getBusinessProvince());
            flowWashSupplyStockReportDO.setDepartment(reportDTO.getDepartment());
            flowWashSupplyStockReportDO.setBusinessDepartment(reportDTO.getBusinessDepartment());
            flowWashSupplyStockReportDO.setDistrictCounty(reportDTO.getDistrictCounty());
            flowWashSupplyStockReportDO.setSuperiorSupervisorCode(reportDTO.getSuperiorSupervisorCode());
            flowWashSupplyStockReportDO.setSuperiorSupervisorName(reportDTO.getSuperiorSupervisorName());
            flowWashSupplyStockReportDO.setRepresentativeCode(reportDTO.getRepresentativeCode());
            flowWashSupplyStockReportDO.setRepresentativeName(reportDTO.getRepresentativeName());
            flowWashSupplyStockReportDO.setPostCode(reportDTO.getPostCode());
            flowWashSupplyStockReportDO.setPostName(reportDTO.getPostName());
            flowWashSupplyStockReportDO.setProductGroup(reportDTO.getProductGroup());*/
        }

        flowWashSupplyStockReportDO.setSoMonth(soMonth);
        flowWashSupplyStockReportDO.setCrmId(request.getCrmId());
        flowWashSupplyStockReportDO.setName(crmEnterpriseDO.getName());
        flowWashSupplyStockReportDO.setGoodsCode(crmGoodsInfoDTO.getGoodsCode());
        flowWashSupplyStockReportDO.setGoodsName(crmGoodsInfoDTO.getGoodsName());
        flowWashSupplyStockReportDO.setSpecifications(crmGoodsInfoDTO.getGoodsSpec());
        flowWashSupplyStockReportDO.setPrice(crmGoodsInfoDTO.getSupplyPrice());
        flowWashSupplyStockReportDO.setEnterpriseCersId(enterpriseId);
        flowWashSupplyStockReportDO.setProvinceName(crmEnterpriseDO.getProvinceName());
        flowWashSupplyStockReportDO.setCityName(crmEnterpriseDO.getCityName());
        flowWashSupplyStockReportDO.setRegionName(crmEnterpriseDO.getRegionName());
        flowWashSupplyStockReportDO.setProvinceCode(Optional.ofNullable(crmEnterpriseDO.getProvinceCode()).orElse(""));
        flowWashSupplyStockReportDO.setCityCode(Optional.ofNullable(crmEnterpriseDO.getCityCode()).orElse(""));
        flowWashSupplyStockReportDO.setRegionCode(Optional.ofNullable(crmEnterpriseDO.getRegionCode()).orElse(""));
        flowWashSupplyStockReportDO.setHideFlag(Constants.IS_NO);


        // 上个月库存数量
        BigDecimal lastMonthStockQty = BigDecimal.ZERO;
        // 本月购进数量
        BigDecimal purchaseQty = BigDecimal.ZERO;
        // 本月销售数量
        BigDecimal saleQty = BigDecimal.ZERO;
        // 本月实际库存数量
        BigDecimal finalQty = BigDecimal.ZERO;

        if (CollectionUtils.isNotEmpty(lastFlowGoodsBatchDetailWashList)) {
            lastMonthStockQty = lastFlowGoodsBatchDetailWashList.stream().findFirst().get().getEndQty();
        }
        if (CollectionUtils.isNotEmpty(flowPurchaseWashDOList)) {
            purchaseQty = flowPurchaseWashDOList.stream().map(t -> t.getPoQuantity()).reduce(BigDecimal::add).get();
        }
        if (CollectionUtils.isNotEmpty(flowSaleWashDOList)) {
            saleQty = flowSaleWashDOList.stream().map(t -> t.getFinalQuantity()).reduce(BigDecimal::add).get();
        }
        if (CollectionUtils.isNotEmpty(flowGoodsBatchDetailWashList)) {
            finalQty = flowGoodsBatchDetailWashList.stream().map(t -> t.getInventoryQuantity()).reduce(BigDecimal::add).get();
        }

        flowWashSupplyStockReportDO.setLastMonthStockQty(lastMonthStockQty);
        flowWashSupplyStockReportDO.setPurchaseQty(purchaseQty);
        flowWashSupplyStockReportDO.setSaleQty(saleQty);
        flowWashSupplyStockReportDO.setFinalQty(finalQty);

        // 上月库存金额
        flowWashSupplyStockReportDO.setLastMonthStockMoney(NumberUtil.mul(flowWashSupplyStockReportDO.getPrice(), flowWashSupplyStockReportDO.getLastMonthStockQty()));
        flowWashSupplyStockReportDO.setLastMonthStockMoney(NumberUtil.div(flowWashSupplyStockReportDO.getLastMonthStockMoney(), 10000));

        // 本月购进金额
        flowWashSupplyStockReportDO.setPurchaseMoney(NumberUtil.mul(flowWashSupplyStockReportDO.getPrice(), flowWashSupplyStockReportDO.getPurchaseQty()));
        flowWashSupplyStockReportDO.setPurchaseMoney(NumberUtil.div(flowWashSupplyStockReportDO.getPurchaseMoney(), 10000));

        // 本月销售金额
        flowWashSupplyStockReportDO.setSaleMoney(NumberUtil.mul(flowWashSupplyStockReportDO.getPrice(), flowWashSupplyStockReportDO.getSaleQty()));
        flowWashSupplyStockReportDO.setSaleMoney(NumberUtil.div(flowWashSupplyStockReportDO.getSaleMoney(), 10000));


        return flowWashSupplyStockReportDO;
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

        supplyStockReportService.removeByCrmId(request);
    }

    /**
     * 销量清洗任务
     *
     * @param month
     * @param crmId
     * @param customerCrmId
     * @param goodCodeList
     * @return
     */
    private Map<Long,List<FlowWashSaleReportDTO>> selectWashSaleInfos(Integer year, Integer month, Long crmId, Long customerCrmId, List<Long> goodCodeList) {
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

            page = flowWashSaleReportService.pageList(request);

            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            current = current + 1;

            saleWashDOList.addAll(page.getRecords());
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));


        if (CollectionUtils.isEmpty(saleWashDOList)) {

            return MapUtil.empty();
        }

        log.info("查询本月销量条数:{}",saleWashDOList.size());

        return saleWashDOList.stream().collect(Collectors.groupingBy(FlowWashSaleReportDTO::getGoodsCode));
    }


    /**
     * 查询销量采购任务
     *
     * @param month
     * @param customerCrmId
     * @param goodCodeList
     * @return
     */
    private Map<Long,List<FlowPurchaseWashDO>> selectWashPurchaseInfos(Integer year, Integer month,Long customerCrmId, List<Long> goodCodeList) {
        List<FlowPurchaseWashDO> saleWashDOList = Lists.newArrayList();
        Page<FlowPurchaseWashDO> page;

        int current = 1;
        int size = 1000;

        QueryFlowPurchaseWashPageRequest request = new QueryFlowPurchaseWashPageRequest();

        do {
            request.setCurrent(current);
            request.setSize(size);
            request.setYear(year);
            request.setMonth(month);
            request.setCrmEnterpriseId(customerCrmId);
            request.setCrmGoodsCodeList(goodCodeList);
            request.setMappingStatus(WashMappingStatusEnum.MATCH_SUCCESS.getCode());
            request.setWashStatusList(ListUtil.toList(FlowDataWashStatusEnum.NORMAL.getCode(),FlowDataWashStatusEnum.DUPLICATE.getCode()));

            page = purchaseWashService.listPage(request);

            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            current = current + 1;

            saleWashDOList.addAll(page.getRecords());
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));


        if (CollectionUtils.isEmpty(saleWashDOList)) {

            return MapUtil.empty();
        }

        log.info("查询本月购进条数:{}",saleWashDOList.size());

        return saleWashDOList.stream().collect(Collectors.groupingBy(FlowPurchaseWashDO::getCrmGoodsCode));
    }


    /**
     * 查询上个月的期末库存
     *
     * @param soMonth
     * @param crmId
     * @param goodCodeList
     * @return
     */
    private Map<Long,List<FlowWashSupplyStockReportDTO>> selectLastSupplyStockReportList(String soMonth, Long crmId, List<Long> goodCodeList) {
        List<FlowWashSupplyStockReportDTO> saleWashDOList = Lists.newArrayList();
        Page<FlowWashSupplyStockReportDTO> page;
        int current = 1;
        int size = 1000;
        FlowWashSupplyStockReportPageRequest request = new FlowWashSupplyStockReportPageRequest();

        do {
            request.setCurrent(current);
            request.setSize(size);
            request.setSoMonth(soMonth);
            request.setCrmId(crmId);
            request.setGoodsCodeList(goodCodeList);

            page = supplyStockReportService.pageList(request);

            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            current = current + 1;

            saleWashDOList.addAll(page.getRecords());
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));

        if (CollectionUtils.isEmpty(saleWashDOList)) {

            return MapUtil.empty();
        }

        log.info("查询上个月的期末库存条数:{}",saleWashDOList.size());

        return saleWashDOList.stream().collect(Collectors.groupingBy(FlowWashSupplyStockReportDTO::getGoodsCode));
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

        log.info("查询本月库存任务条数:{}",saleWashDOList.size());

        return  saleWashDOList.stream().collect(Collectors.groupingBy(FlowWashInventoryReportDTO::getGoodsCode));
    }

    /**
     * 查询在途订单信息
     *
     * @param soMonth
     * @param crmId
     * @param goodCodeList
     * @return
     */
    private Map<Long,List<FlowGoodsBatchTransitDO>> selectFlowGoodsBatchTransits(String  soMonth, Long crmId, List<Long> goodCodeList) {

        List<FlowGoodsBatchTransitDO> saleWashDOList = Lists.newArrayList();
        Page<FlowGoodsBatchTransitDO> page;

        int current = 1;
        int size = 1000;

        QueryFlowGoodsBatchTransitPageRequest request = new QueryFlowGoodsBatchTransitPageRequest();

        do {
            request.setCurrent(current);
            request.setSize(size);
            request.setGoodsBatchType(FlowGoodsBatchTransitTypeEnum.IN_TRANSIT.getCode());
            request.setGbDetailMonth(soMonth);
            request.setCrmEnterpriseId(crmId);
            request.setCrmGoodsCodeList(goodCodeList);

            page = transitService.listPage(request);

            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            current = current + 1;

            saleWashDOList.addAll(page.getRecords());
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));

        if (CollectionUtils.isEmpty(saleWashDOList)) {

            return MapUtil.empty();
        }

        return saleWashDOList.stream().collect(Collectors.groupingBy(FlowGoodsBatchTransitDO::getCrmGoodsCode));
    }
}