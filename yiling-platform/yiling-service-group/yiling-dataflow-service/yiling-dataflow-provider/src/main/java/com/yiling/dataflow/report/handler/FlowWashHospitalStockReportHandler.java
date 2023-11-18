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
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.dataflow.agency.entity.CrmHospitalDO;
import com.yiling.dataflow.agency.service.CrmHospitalService;
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
import com.yiling.dataflow.report.dao.FlowWashHospitalStockReportMapper;
import com.yiling.dataflow.report.dto.FlowWashHospitalStockReportDTO;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.dto.request.CreateFlowWashReportRequest;
import com.yiling.dataflow.report.dto.request.FlowWashHospitalStockReportPageRequest;
import com.yiling.dataflow.report.dto.request.FlowWashSaleReportPageRequest;
import com.yiling.dataflow.report.dto.request.RemoveFlowWashStockReportRequest;
import com.yiling.dataflow.report.entity.FlowWashHospitalStockReportDO;
import com.yiling.dataflow.report.service.FlowWashHospitalStockReportService;
import com.yiling.dataflow.report.service.FlowWashSaleReportService;
import com.yiling.dataflow.utils.BackupUtil;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchSafePageRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchTransitPageRequest;
import com.yiling.dataflow.wash.entity.FlowGoodsBatchSafeDO;
import com.yiling.dataflow.wash.entity.FlowGoodsBatchTransitDO;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.dataflow.wash.enums.FlowGoodsBatchTransitTypeEnum;
import com.yiling.dataflow.wash.enums.WashMappingStatusEnum;
import com.yiling.dataflow.wash.service.FlowGoodsBatchSafeService;
import com.yiling.dataflow.wash.service.FlowGoodsBatchTransitService;
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
 * 流向医疗进销存库存报表生成
 *
 * @author zhigang.guo
 * @date: 2023/3/7
 */
@Slf4j
@Component
public class FlowWashHospitalStockReportHandler {
    @Autowired
    private CrmEnterpriseService crmEnterpriseService;
    @Autowired
    private CrmGoodsInfoService crmGoodsInfoService;
    @Autowired
    private FlowGoodsBatchTransitService transitService;
    @Autowired
    private FlowGoodsBatchSafeService flowGoodsBatchSafeService;
    @Autowired
    private FlowWashHospitalStockReportService hospitalStockReportService;
    @Autowired
    private FlowWashHospitalStockReportMapper hospitalStockReportMapper;
    @Autowired
    private FlowWashSaleReportService washSaleReportService;
    @Autowired
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;
    @Autowired
    private CrmHospitalService crmHospitalService;
    @Autowired
    private CrmGoodsGroupService crmGoodsGroupService;
    @Autowired
    private RedisDistributedLock redisDistributedLock;
    @Autowired
    private CrmEnterpriseRelationPinchRunnerService crmEnterpriseRelationPinchRunnerService;

    /**
     * 设置医疗经销存库存报表数据
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

        stopWatch.start("查询企业信息");
        CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getSuffixByCrmEnterpriseId(request.getCrmId(),tableSuffix);
        stopWatch.stop();

        if (crmEnterpriseDO == null) {

            throw new RuntimeException("企业档案信息不存在!");
        }

        // 上月数据
        String lastMonth = DateUtil.format(DateUtil.offsetMonth(DateUtil.parse(request.getYear() + "-" + request.getMonth(), "yyyy-MM"), -1),"yyyy-MM");
        // 当月数据
        String soMonth = DateUtil.format(DateUtil.parse(request.getYear() + "-" + request.getMonth(), "yyyy-MM"),"yyyy-MM");
        // 不是医疗不统计
        if (CrmSupplyChainRoleEnum.HOSPITAL != CrmSupplyChainRoleEnum.getFromCode(crmEnterpriseDO.getSupplyChainRole())) {

            return;
        }


        // 客户机构编码
        Long crmId = request.getCrmId();
        Map<Long, CrmGoodsInfoDTO> crmGoodsInfoDTOMap = crmGoodsInfoAll.stream().collect(Collectors.toMap(CrmGoodsInfoDTO::getGoodsCode, Function.identity()));
        List<FlowWashHospitalStockReportDO> hospitalStockReportDOS = Lists.newArrayList();

        stopWatch.start("本月终端订单信息查询");
        // 本月终端订单信息
        Map<Long, List<FlowGoodsBatchTransitDO>> flowGoodsBatchTransitQtyMap = this.selectFlowGoodsBatchTransits(soMonth, crmId, null);
        stopWatch.stop();

        // 上个月报表数据
        stopWatch.start("上个月报表数据查询");
        List<FlowWashHospitalStockReportDTO> flowWashHospitalStockReportDTOS = this.selectLastHospitalStockReportRecords(lastMonth, crmId, null);
        stopWatch.stop();

        //800 分批一次
        List<List<Long>> subGoodCodeList = Lists.partition(crmGoodsInfoAll.stream().map(t -> t.getGoodsCode()).collect(Collectors.toList()), 800);

        int count = 0;
        for (List<Long> goodList : subGoodCodeList) {

            count++;

            //本月流向数据
            stopWatch.start("第:" + count + "批本月流向数据数据查询");
            Map<Long, List<FlowWashSaleReportDTO>> flowSaleWashQtyMap = this.selectWashSaleInfos(request.getYear(), request.getMonth(), crmId, goodList);
            stopWatch.stop();

            // 组装报表
            stopWatch.start("第:" + count + "批医疗流向数据组装");
            Map<Long, List<FlowWashHospitalStockReportDTO>> flowWashHospitalStockReportMap = flowWashHospitalStockReportDTOS.stream().collect(Collectors.groupingBy(FlowWashHospitalStockReportDTO::getGoodsCode));
            List<FlowWashHospitalStockReportDO> reportDOList = goodList.stream().map(t -> buildFlowWashHospitalStockReport(flowWashHospitalStockReportMap.get(t), flowGoodsBatchTransitQtyMap.get(t), flowSaleWashQtyMap.get(t), crmGoodsInfoDTOMap.get(t), crmEnterpriseDO,soMonth,request)).filter(t -> ObjectUtil.isNotNull(t)).collect(Collectors.toList());
            stopWatch.stop();

            flowSaleWashQtyMap.clear();

            if (CollectionUtils.isEmpty(reportDOList)) {

                continue;
            }

            // 设置上报数量，期末数量，以及金额
            List<Long> finalGoodCodeList = reportDOList.stream().map(t -> t.getGoodsCode()).collect(Collectors.toList());
            stopWatch.start("第:" + count + "批医疗流向数据设置金额");
            setEndMoney(reportDOList, flowGoodsBatchTransitQtyMap, selectFlowGoodsBatchSafe(crmId, finalGoodCodeList));
            stopWatch.stop();

            // 设置三者关系
            stopWatch.start("第:" + count + "批医疗流向数据设置三者关系");
            setEnterpriseRelationShip(reportDOList,tableSuffix);
            setEnterpriseRelationPinchRunner(reportDOList,tableSuffix);
            stopWatch.stop();

            // 设置商品产品组
            stopWatch.start("第:" + count + "批医疗流向数据设置商品组");
            setGoodsProductGroup(reportDOList,tableSuffix);
            stopWatch.stop();

            // 设置医疗信息
            stopWatch.start("第:" + count + "批医疗流向数据医疗扩展信息");
            setCrmHospitalInfo(reportDOList,tableSuffix);
            stopWatch.stop();

            hospitalStockReportDOS.addAll(reportDOList);
        }

        // 释放内存
        flowGoodsBatchTransitQtyMap.clear();
        flowWashHospitalStockReportDTOS.clear();

        stopWatch.start("流向医疗数据插入");
        // 添加锁任务,防止数据插入重复
        createReport(request.getCrmId(),request.getYear(),request.getMonth(),soMonth,hospitalStockReportDOS);
        stopWatch.stop();

        log.info("医疗机构Id:{},月份:{},生成医疗经销存库存报表数据,{}",request.getCrmId(),soMonth,stopWatch.prettyPrint());
    }



    /**
     * 生成报表
     * @param crmId 机构Id
     * @param year 年
     * @param month 月
     * @param soMonth 年月字符串
     * @param hospitalStockReportDOS 本次生成数据
     */
    private void createReport(Long crmId,Integer year,Integer month,String soMonth,List<FlowWashHospitalStockReportDO> hospitalStockReportDOS) {

        if (CollectionUtil.isEmpty(hospitalStockReportDOS)) {

            return;
        }

        String lockId = "";
        String lockName = RedisKey.generate("flow","wash","hospitalStockReport",crmId + "",(year + "-" + month));
        try {
            lockId = redisDistributedLock.lock2(lockName, 60, 60, TimeUnit.SECONDS);
            // 清除本次报表数据
            removeStockReportRecord(crmId,soMonth);
            // 保存数据
            hospitalStockReportMapper.insertBatchSomeColumn(hospitalStockReportDOS);
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }

    }


    /**
     * 构建报表终端数据
     * @param flowWashHospitalStockReportDTOS
     * @param flowGoodsBatchTransitList
     * @param flowSaleWashDOList
     * @param crmGoodsInfoDTO
     * @param crmEnterpriseDO
     * @param request
     * @return
     */
    private FlowWashHospitalStockReportDO buildFlowWashHospitalStockReport(List<FlowWashHospitalStockReportDTO> flowWashHospitalStockReportDTOS, // 上个月报表数据
                                                                           List<FlowGoodsBatchTransitDO> flowGoodsBatchTransitList, // 终端维护数据
                                                                           List<FlowWashSaleReportDTO> flowSaleWashDOList, // 流向内购进
                                                                           CrmGoodsInfoDTO crmGoodsInfoDTO, // 商品信息
                                                                           CrmEnterpriseDO crmEnterpriseDO, // 企业信息
                                                                           String soMonth, // 所属月份
                                                                           CreateFlowWashReportRequest request) {

        // 上个月期末库存
        BigDecimal sumEndQty = BigDecimal.ZERO;

        if (CollectionUtils.isNotEmpty(flowWashHospitalStockReportDTOS)) {
            sumEndQty = flowWashHospitalStockReportDTOS.stream().map(t -> t.getEndQty()).reduce(BigDecimal::add).get();
        }
        // 此列表中展示的医疗机构需要满足任一条件：（1）上个月的期末库存不为0 。 （2）本月的流向中，有至少一条数据（只需要判断销售流向中，别的机构销售给当前医疗机构的。 因为医疗机构不可能传采购和库存流向）。 （3）终端库存功能中，本月维护了医疗机构的库存。
        if (CompareUtil.compare(BigDecimal.ZERO, sumEndQty) == 0 && CollectionUtils.isEmpty(flowGoodsBatchTransitList) && CollectionUtils.isEmpty(flowSaleWashDOList)) {

            return null;
        }

        FlowWashHospitalStockReportDO reportDO = new FlowWashHospitalStockReportDO();
        reportDO.initModel();

        Long enterpriseId;

        if (CollectionUtils.isNotEmpty(flowSaleWashDOList)) {
            FlowWashSaleReportDTO reportDTO = flowSaleWashDOList.stream().findFirst().get();
            enterpriseId = reportDTO.getOrganizationCersId();
            // 设置三者关系
         /*   reportDO.setProvincialArea(Optional.ofNullable(reportDTO.getProvincialArea()).orElse(""));
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

            // 设置医疗属性
           /* reportDO.setMedicalNature(Optional.ofNullable(reportDTO.getCustomerMedicalNature()).orElse(0));
            reportDO.setMedicalType(Optional.ofNullable(reportDTO.getCustomerMedicalType()).orElse(0));
            reportDO.setYlLevel(Optional.ofNullable(reportDTO.getCustomerYlLevel()).orElse(0));
            reportDO.setNationalGrade(Optional.ofNullable(reportDTO.getCustomerNationalGrade()).orElse(0));
            reportDO.setBaseMedicineFlag(Optional.ofNullable(reportDTO.getBaseMedicineFlag()).orElse(0));*/

        } else if (CollectionUtils.isNotEmpty(flowWashHospitalStockReportDTOS))  {
            FlowWashHospitalStockReportDTO reportDTO = flowWashHospitalStockReportDTOS.stream().findFirst().get();
            enterpriseId = reportDTO.getEnterpriseCersId();
            // 设置三者关系
         /*   reportDO.setProvincialArea(Optional.ofNullable(reportDTO.getProvincialArea()).orElse(""));
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

            // 设置医疗属性
            /*reportDO.setMedicalNature(Optional.ofNullable(reportDTO.getMedicalNature()).orElse(0));
            reportDO.setMedicalType(Optional.ofNullable(reportDTO.getMedicalType()).orElse(0));
            reportDO.setYlLevel(Optional.ofNullable(reportDTO.getYlLevel()).orElse(0));
            reportDO.setNationalGrade(Optional.ofNullable(reportDTO.getNationalGrade()).orElse(0));
            reportDO.setBaseMedicineFlag(Optional.ofNullable(reportDTO.getBaseMedicineFlag()).orElse(0));*/
        } else {
            enterpriseId = flowGoodsBatchTransitList.stream().findFirst().get().getEnterpriseCersId();
        }
        // 流向内购进数量
        BigDecimal innerPurchaseQty = BigDecimal.ZERO;
        // 窜货批复
        BigDecimal fleeingPurchaseQty = BigDecimal.ZERO;
        // 窜货扣减
        BigDecimal fleeingPurchaseReduceQty = BigDecimal.ZERO;
        // 销售申诉反流
        BigDecimal purchaseReverse = BigDecimal.ZERO;

        if (CollectionUtils.isNotEmpty(flowSaleWashDOList)) {
            innerPurchaseQty = flowSaleWashDOList.stream().filter(t -> FlowClassifyEnum.NORMAL == FlowClassifyEnum.getByCode(t.getFlowClassify())).map(t -> t.getFinalQuantity()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            fleeingPurchaseQty = flowSaleWashDOList.stream().filter(t -> FlowClassifyEnum.FLOW_CROSS == FlowClassifyEnum.getByCode(t.getFlowClassify()) && CompareUtil.compare(t.getFinalQuantity(),BigDecimal.ZERO) > 0).map(t -> t.getFinalQuantity()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            fleeingPurchaseReduceQty  = flowSaleWashDOList.stream().filter(t -> FlowClassifyEnum.FLOW_CROSS == FlowClassifyEnum.getByCode(t.getFlowClassify()) && CompareUtil.compare(t.getFinalQuantity(),BigDecimal.ZERO) < 0).map(t -> t.getFinalQuantity()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            purchaseReverse = flowSaleWashDOList.stream().filter(t -> FlowClassifyEnum.SALE_APPEAL == FlowClassifyEnum.getByCode(t.getFlowClassify())).map(t -> t.getFinalQuantity()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        }

        reportDO.setEnterpriseCersId(enterpriseId);
        reportDO.setSoMonth(soMonth);
        reportDO.setCrmId(request.getCrmId());
        reportDO.setName(Optional.ofNullable(crmEnterpriseDO.getName()).orElse(""));
        reportDO.setGoodsCode(Optional.ofNullable(crmGoodsInfoDTO.getGoodsCode()).orElse(0l));
        reportDO.setGoodsName(Optional.ofNullable(crmGoodsInfoDTO.getGoodsName()).orElse(""));
        reportDO.setSpecifications(Optional.ofNullable(crmGoodsInfoDTO.getGoodsSpec()).orElse(""));
        reportDO.setPrice(Optional.ofNullable(crmGoodsInfoDTO.getSupplyPrice()).orElse(BigDecimal.ZERO));
        reportDO.setLastMonthStockQty(sumEndQty);
        // 上月库存金额
        reportDO.setLastMonthStockMoney(NumberUtil.mul(reportDO.getPrice(), reportDO.getLastMonthStockQty()));
        reportDO.setLastMonthStockMoney(NumberUtil.div(reportDO.getLastMonthStockMoney(), 10000));

        reportDO.setInnerPurchaseQty(innerPurchaseQty);

        // 初始化
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
     * 设置上报数量，期末数量，以及金额
     *
     * @param stockReportDOS
     * @param flowGoodsBatchTransitQtyMap
     * @param flowGoodsBatchSafeMap
     */
    private void setEndMoney(List<FlowWashHospitalStockReportDO> stockReportDOS, Map<Long, List<FlowGoodsBatchTransitDO>> flowGoodsBatchTransitQtyMap, Map<Long, BigDecimal> flowGoodsBatchSafeMap) {
        stockReportDOS.stream().forEach(report -> {
            BigDecimal safeQty = flowGoodsBatchSafeMap.getOrDefault(report.getGoodsCode(), BigDecimal.ZERO);
            // 终端上报信息
            List<FlowGoodsBatchTransitDO> flowGoodsBatchTransitDOS = flowGoodsBatchTransitQtyMap.get(report.getGoodsCode());
            // 上报终端库存数量
            BigDecimal batchTransitQty;
            if (CollectionUtils.isNotEmpty(flowGoodsBatchTransitDOS)) {
                batchTransitQty = flowGoodsBatchTransitDOS.stream().map(t -> t.getGbNumber()).reduce(BigDecimal::add).get();
            } else  {
                batchTransitQty = null;
            }
            BigDecimal monthStockQty;
            if (batchTransitQty == null) {
                monthStockQty = safeQty;
            } else {
                monthStockQty = batchTransitQty;
            }
            //  本月上报库存数量：终端库存功能中，当前机构、当前所属年月、当前商品的上报库存数量。（如果未上报，则使用安全库存数量作为本月库存数量。）
            report.setMonthStockQty(monthStockQty);
            // 本月上报库存金额：本月上报库存数量 * 单价
            report.setMonthStockMoney(NumberUtil.mul(report.getPrice(), report.getMonthStockQty()));
            report.setMonthStockMoney(NumberUtil.div(report.getMonthStockMoney(), 10000));


            // 流向外购进合计  E=（B+C+D)
            report.setOutterPurchaseSum(NumberUtil.add(report.getFleeingPurchaseQty(), report.getFleeingPurchaseReduceQty(), report.getPurchaseReverse()));
            // 商销量（购进）数 G=（B+F）
            report.setSaleQty(NumberUtil.add(report.getOutterPurchaseSum(), report.getInnerPurchaseQty()));
            // 购进总金额
            report.setSaleMoney(NumberUtil.mul(report.getSaleQty(), report.getPrice()));
            // 转换为万元
            report.setSaleMoney(NumberUtil.div(report.getSaleMoney(), 10000));
            // 纯销量（销售）数量  H=（A+G-I）
            BigDecimal pureSaleQty = NumberUtil.add(report.getLastMonthStockQty(), report.getSaleQty());
            pureSaleQty = NumberUtil.sub(pureSaleQty, report.getMonthStockQty());

            report.setPureSaleQty(pureSaleQty);
            // 纯销量（销售）金额（万元）R=（X*H）
            report.setPureSaleMoney(NumberUtil.mul(report.getPureSaleQty(), report.getPrice()));
            report.setPureSaleMoney(NumberUtil.div(report.getPureSaleMoney(), 10000));

            // 期末库存计算
            /*
            （1）如果当前机构、当前商品在“终端库存”功能中上报了本月的终端库存数量，则比较 “终端库存数量” 与 安全库存功能中对应机构对应商品的“安全库存数量”。 两个数值取较大值，作为期末库存数量。   如果安全库存数量不存在，则在此处按0计算。
             （2）如果当前机构、当前商品在“终端库存”功能中未上报本月的终端库存数量（注意：上报的终端库存数量为0也算是上报了）， 则期末库存数量 等于 上月库存数量 加 商销量（购进）数量，即列表中的A+G
              */
            BigDecimal endQty;
            if (batchTransitQty != null && flowGoodsBatchSafeMap.get(report.getGoodsCode()) != null) {
                endQty = NumberUtil.max(batchTransitQty, safeQty);
            } else if (batchTransitQty != null && flowGoodsBatchSafeMap.get(report.getGoodsCode()) == null) {
                endQty = BigDecimal.ZERO;
            } else {
                endQty = NumberUtil.add(report.getLastMonthStockQty(), report.getSaleQty());
            }
            report.setEndQty(endQty);
            // 设置期末库存金额
            report.setEndMoney(NumberUtil.mul(report.getPrice(), report.getEndQty()));
            report.setEndMoney(NumberUtil.div(report.getEndMoney(), 10000));

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

        hospitalStockReportService.removeByCrmId(request);
    }


    /**
     * 查询终端订单信息
     *
     * @param soMonth
     * @param crmId
     * @param goodCodeList
     * @return
     */
    private Map<Long, List<FlowGoodsBatchTransitDO>> selectFlowGoodsBatchTransits(String soMonth, Long crmId, List<Long> goodCodeList) {

        List<FlowGoodsBatchTransitDO> saleWashDOList = Lists.newArrayList();
        Page<FlowGoodsBatchTransitDO> page;

        int current = 1;
        int size = 1000;

        QueryFlowGoodsBatchTransitPageRequest request = new QueryFlowGoodsBatchTransitPageRequest();

        do {
            request.setCurrent(current);
            request.setSize(size);
            request.setGoodsBatchType(FlowGoodsBatchTransitTypeEnum.TERMINAL.getCode());
            request.setCrmEnterpriseId(crmId);
            request.setCrmGoodsCodeList(goodCodeList);
            request.setGbDetailMonth(soMonth);

            page = transitService.listPage(request);

            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            current = current + 1;

            saleWashDOList.addAll(page.getRecords());
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));

        log.info("本次查询查询终端订单信息条数:{}",saleWashDOList.size());

        if (CollectionUtils.isEmpty(saleWashDOList)) {
            return MapUtil.empty();
        }

        return saleWashDOList.stream().collect(Collectors.groupingBy(FlowGoodsBatchTransitDO::getCrmGoodsCode));
    }

    /**
     * 查询安全库存信息
     *
     * @param crmId
     * @param goodCodeList
     * @return
     */
    private Map<Long, BigDecimal> selectFlowGoodsBatchSafe(Long crmId, List<Long> goodCodeList) {

        if (CollectionUtils.isEmpty(goodCodeList)) {

            return MapUtil.empty();
        }

        List<FlowGoodsBatchSafeDO> saleWashDOList = Lists.newArrayList();
        Page<FlowGoodsBatchSafeDO> page;

        int current = 1;
        int size = 1000;

        QueryFlowGoodsBatchSafePageRequest request = new QueryFlowGoodsBatchSafePageRequest();

        do {
            request.setCurrent(current);
            request.setSize(size);
            request.setCrmEnterpriseId(crmId);
            request.setCrmGoodsCodeList(goodCodeList);

            page = flowGoodsBatchSafeService.listPage(request);

            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            current = current + 1;

            saleWashDOList.addAll(page.getRecords());
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));

        if (CollectionUtils.isEmpty(saleWashDOList)) {
            return MapUtil.empty();
        }

        return saleWashDOList.stream().collect(Collectors.groupingBy(FlowGoodsBatchSafeDO::getCrmGoodsCode,Collectors.mapping(FlowGoodsBatchSafeDO::getGbNumber,Collectors.reducing(BigDecimal.ZERO,BigDecimal::add))));
    }


    /**
     * 查询上个月的医疗进销存数据
     *
     * @param lastMonth
     * @param crmId
     * @param goodCodeList
     * @return
     */
    private List<FlowWashHospitalStockReportDTO> selectLastHospitalStockReportRecords(String lastMonth, Long crmId, List<Long> goodCodeList) {
        List<FlowWashHospitalStockReportDTO> saleWashDOList = Lists.newArrayList();
        Page<FlowWashHospitalStockReportDTO> page;

        int current = 1;
        int size = 1000;

        FlowWashHospitalStockReportPageRequest request = new FlowWashHospitalStockReportPageRequest();

        do {
            request.setCurrent(current);
            request.setSize(size);
            request.setSoMonth(lastMonth);
            request.setCrmId(crmId);
            request.setGoodsCodeList(goodCodeList);

            page = hospitalStockReportService.pageList(request);

            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            current = current + 1;
            saleWashDOList.addAll(page.getRecords());
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));

        log.info("本次查询上个月的医疗进销存数据条数:{}",saleWashDOList.size());

        return saleWashDOList;
    }


    /**
     * 销量清洗任务
     *
     * @param month
     * @param customerCrmId
     * @param goodCodeList
     * @return
     */
    private Map<Long, List<FlowWashSaleReportDTO>> selectWashSaleInfos(Integer year, Integer month, Long customerCrmId, List<Long> goodCodeList) {
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


        log.info("本次查询本月流向数据条数:{}",saleWashDOList.size());

        if (CollectionUtils.isEmpty(saleWashDOList)) {
            return MapUtil.empty();
        }

        return saleWashDOList.stream().collect(Collectors.groupingBy(FlowWashSaleReportDTO::getGoodsCode));
    }


    /**
     * 设置企业三者关系数据
     *
     * @param reportDOList
     */
    private void setEnterpriseRelationShip(List<FlowWashHospitalStockReportDO> reportDOList,String tableSuffix) {

        List<Long> organizationIds = reportDOList.stream()
                .filter(t -> t.getEnterpriseCersId() != 0)
                // 三者关系数据还未填充的,重新查询
                .filter(t -> CompareUtil.compare(t.getPostCode(),0l) == 0)
                .map(t -> t.getEnterpriseCersId()).distinct().collect(Collectors.toList());

        if (CollectionUtil.isEmpty(organizationIds)) {

            return;
        }

        List<CrmEnterpriseRelationShipDO> crmEnterpriseRelationShipDOS = crmEnterpriseRelationShipService.listSuffixByIdList(organizationIds,tableSuffix);
        if (CollectionUtil.isEmpty(crmEnterpriseRelationShipDOS)) {
            return;
        }

        Map<Long, CrmEnterpriseRelationShipDO> crmEnterpriseMap = crmEnterpriseRelationShipDOS.stream().collect(Collectors.toMap(t -> t.getId(), Function.identity()));
        // 有机构编码,并且三者关系部门数据未填充的
        reportDOList.stream().filter(t -> t.getEnterpriseCersId() != 0).filter(t -> CompareUtil.compare(t.getPostCode(),0l) == 0).filter(t -> StringUtils.isBlank(t.getDepartment())).forEach(t -> {
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
            // 设置代跑关系
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
    private void setEnterpriseRelationPinchRunner(List<FlowWashHospitalStockReportDO> saleReportDOList, String tableSuffix) {

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
    private void setGoodsProductGroup(List<FlowWashHospitalStockReportDO> saleReportDOList,String tableSuffix) {

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
     * 设置销售报表医疗相关信息
     *
     * @param hospitalSaleReportDOList
     */
    private void setCrmHospitalInfo(List<FlowWashHospitalStockReportDO> hospitalSaleReportDOList,String tableSuffix) {
        if (CollectionUtil.isEmpty(hospitalSaleReportDOList)) {

            return;
        }

        List<Long> hospitalCrmIdList = hospitalSaleReportDOList.stream()
                .map(t -> t.getCrmId()).filter(t -> t != 0).distinct().collect(Collectors.toList());
        if (CollectionUtil.isEmpty(hospitalCrmIdList)) {
            return;
        }

        List<CrmHospitalDO> crmEnterpriseList = crmHospitalService.listSuffixByCrmEnterpriseIdList(hospitalCrmIdList,tableSuffix);

        if (CollectionUtil.isEmpty(crmEnterpriseList)) {
            return;
        }

        Map<Long, CrmHospitalDO> crmEnterpriseMap = crmEnterpriseList.stream().collect(Collectors.toMap(t -> t.getCrmEnterpriseId(), Function.identity()));
        hospitalSaleReportDOList.stream().filter(t -> t.getCrmId() != 0).forEach(saleReportDO -> {
            CrmHospitalDO crmEnterprise = crmEnterpriseMap.getOrDefault(saleReportDO.getCrmId(), new CrmHospitalDO());
            saleReportDO.setMedicalNature(Optional.ofNullable(crmEnterprise.getMedicalNature()).orElse(0));
            saleReportDO.setMedicalType(Optional.ofNullable(crmEnterprise.getMedicalType()).orElse(0));
            saleReportDO.setYlLevel(Optional.ofNullable(crmEnterprise.getYlLevel()).orElse(0));
            saleReportDO.setNationalGrade(Optional.ofNullable(crmEnterprise.getNationalGrade()).orElse(0));
            saleReportDO.setBaseMedicineFlag(Optional.ofNullable(crmEnterprise.getBaseMedicineFlag()).orElse(0));
            saleReportDO.setLockTime(Optional.ofNullable(crmEnterprise.getFirstLockTime()).orElse(DateUtil.parse("1970-01-01", "yyyy-MM-dd")));
            saleReportDO.setLastUnLockTime(Optional.ofNullable(crmEnterprise.getLastUnlockTime()).orElse(DateUtil.parse("1970-01-01", "yyyy-MM-dd")));
        });

        if (log.isDebugEnabled()) {
            log.debug("设置商业医疗信息:{}", hospitalSaleReportDOList);
        }
    }
}
