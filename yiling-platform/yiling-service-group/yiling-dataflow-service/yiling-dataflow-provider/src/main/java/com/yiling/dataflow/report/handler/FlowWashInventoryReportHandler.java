package com.yiling.dataflow.report.handler;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.yiling.dataflow.agency.entity.CrmSupplierDO;
import com.yiling.dataflow.agency.service.CrmSupplierService;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationPinchRunnerDO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationPinchRunnerService;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupService;
import com.yiling.dataflow.crm.service.CrmGoodsInfoService;
import com.yiling.dataflow.report.dao.FlowWashInventoryReportMapper;
import com.yiling.dataflow.report.entity.FlowWashInventoryReportDO;
import com.yiling.dataflow.report.service.FlowWashInventoryReportService;
import com.yiling.dataflow.utils.BackupUtil;
import com.yiling.dataflow.wash.entity.FlowGoodsBatchDetailWashDO;
import com.yiling.dataflow.wash.enums.WashMappingStatusEnum;
import com.yiling.framework.common.util.Constants;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 流向清洗库存报表
 *
 * @author zhigang.guo
 * @date: 2023/3/3
 */
@Slf4j
@Component
public class FlowWashInventoryReportHandler {

    @Autowired
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;
    @Autowired
    private CrmSupplierService crmSupplierService;
    @Autowired
    private CrmGoodsInfoService crmGoodsInfoService;
    @Autowired
    private FlowWashInventoryReportService inventoryService;
    @Autowired
    private CrmGoodsGroupService crmGoodsGroupService;
    @Autowired
    private FlowWashInventoryReportMapper inventoryReportMapper;
    @Autowired
    private CrmEnterpriseRelationPinchRunnerService crmEnterpriseRelationPinchRunnerService;

    /**
     * 生成库存清洗任务报表
     *
     * @param saleReportDOList
     */
    public void generator(List<FlowGoodsBatchDetailWashDO> saleReportDOList, Integer year, Integer month) {

        if (CollectionUtil.isEmpty(saleReportDOList)) {

            return;
        }

        List<FlowWashInventoryReportDO> flowWashInventoryResults = this.initFlowWashSaleReportReports(saleReportDOList);
        // 1000 分批一次
        List<List<FlowWashInventoryReportDO>> subList = Lists.partition(flowWashInventoryResults, 1000);

        subList.forEach(flowWashInventoryReportDOS -> {

            long start1 = System.currentTimeMillis();
            // 设置商业零售信息
            this.setCrmSupplyInfo(flowWashInventoryReportDOS, year, month);
            // 设置商品信息
            this.setGoodsInfo(flowWashInventoryReportDOS, year, month);
            // 设置三者关系
            this.setEnterpriseRelationShip(flowWashInventoryReportDOS, year, month);
            // 设置三者关系数据
            this.setEnterpriseRelationPinchRunner(flowWashInventoryReportDOS, year, month);
            // 设置商品分组
            this.setGoodsProductGroup(flowWashInventoryReportDOS, year, month);
            // 先清除数据
            this.removeByWashIds(flowWashInventoryReportDOS.stream().map(t -> t.getFlowGoodsBatchWashId()).collect(Collectors.toList()));
            // 最后保存数据
            inventoryReportMapper.insertBatchSomeColumn(flowWashInventoryReportDOS);

            log.info("本次插入库存流向数据耗时:{}", (System.currentTimeMillis() - start1));
        });

    }

    /**
     * 根据flowSaleWashId 清除库存报表数据
     *
     * @param flowSaleWashIds
     */
    private void removeByWashIds(List<Long> flowSaleWashIds) {

        QueryWrapper<FlowWashInventoryReportDO> wrapper = new QueryWrapper<>();

        wrapper.lambda().in(FlowWashInventoryReportDO::getFlowGoodsBatchWashId, flowSaleWashIds);
        inventoryService.remove(wrapper);
    }

    /**
     * 设置基础资料信息
     *
     * @param flowSaleWashDOList
     * @return
     */
    private List<FlowWashInventoryReportDO> initFlowWashSaleReportReports(List<FlowGoodsBatchDetailWashDO> flowSaleWashDOList) {

        return flowSaleWashDOList.stream().map(t -> {
            String soMonth = DateUtil.format(DateUtil.parse(t.getYear() + "-" + t.getMonth(), "yyyy-MM"), "yyyy-MM");
            FlowWashInventoryReportDO reportDO = new FlowWashInventoryReportDO();
            reportDO.initModel();
            reportDO.setCrmId(Optional.ofNullable(t.getCrmEnterpriseId()).orElse(0l));
            reportDO.setFlowGoodsBatchWashId(Optional.ofNullable(t.getId()).orElse(0l));
            reportDO.setFlowKey(Optional.ofNullable(t.getFlowKey()).orElse(""));
            reportDO.setFmwtId(Optional.ofNullable(t.getFmwtId()).orElse(0l));
            reportDO.setSoMonth(Optional.ofNullable(soMonth).orElse(""));
            reportDO.setName(Optional.ofNullable(t.getName()).orElse(""));
            reportDO.setProvinceName(Optional.ofNullable(t.getProvinceName()).orElse(""));
            reportDO.setCityCode(Optional.ofNullable(t.getCityCode()).orElse(""));
            reportDO.setProvinceCode(Optional.ofNullable(t.getProvinceCode()).orElse(""));
            reportDO.setCityName(Optional.ofNullable(t.getCityName()).orElse(""));
            reportDO.setRegionCode(Optional.ofNullable(t.getRegionCode()).orElse(""));
            reportDO.setRegionName(Optional.ofNullable(t.getRegionName()).orElse(""));
            reportDO.setGoodsCode(Optional.ofNullable(t.getCrmGoodsCode()).orElse(0l));
            reportDO.setBatchNo(Optional.ofNullable(t.getGbBatchNo()).orElse(""));
            reportDO.setInventoryQuantity(Optional.ofNullable(t.getGbNumber()).orElse(BigDecimal.ZERO));
            reportDO.setEnterpriseCersId(Optional.ofNullable(t.getEnterpriseCersId()).orElse(0l));
            reportDO.setMappingStatus(WashMappingStatusEnum.BOTH_MISMATCH.getCode());
            if (t.getGoodsMappingStatus() == Constants.IS_YES) {
                reportDO.setMappingStatus(WashMappingStatusEnum.MATCH_SUCCESS.getCode());
            }

            return reportDO;

        }).collect(Collectors.toList());

    }

    /**
     * 设置企业三者关系数据
     *
     * @param reportDOList
     */
    private void setEnterpriseRelationShip(List<FlowWashInventoryReportDO> reportDOList, Integer year, Integer month) {
        List<Long> enterpriseCersIds = reportDOList.stream().filter(t -> t.getEnterpriseCersId() != 0).map(t -> t.getEnterpriseCersId()).distinct().collect(Collectors.toList());

        if (CollectionUtil.isEmpty(enterpriseCersIds)) {

            return;
        }

        List<CrmEnterpriseRelationShipDO> crmEnterpriseRelationShipDOS = crmEnterpriseRelationShipService.listSuffixByIdList(enterpriseCersIds, BackupUtil.generateTableSuffix(year, month));
        if (CollectionUtil.isEmpty(crmEnterpriseRelationShipDOS)) {
            return;
        }

        Map<Long, CrmEnterpriseRelationShipDO> crmEnterpriseMap = crmEnterpriseRelationShipDOS.stream().collect(Collectors.toMap(t -> t.getId(), Function.identity()));
        reportDOList.stream().filter(t -> t.getEnterpriseCersId() != 0).forEach(t -> {
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
            // 临时存储代跑三者关系数据
            t.setSubstituteRunning(Optional.ofNullable(crmEnterpriseRelationShipDO.getSubstituteRunning()).orElse(1));
        });

        if (log.isDebugEnabled()) {
            log.debug("设置企业三者关系数据:{}", reportDOList);
        }
    }


    /**
     * 设置三者关系代跑数据
     *
     * @param saleReportDOList
     * @param year
     * @param month
     */
    private void setEnterpriseRelationPinchRunner(List<FlowWashInventoryReportDO> saleReportDOList, Integer year, Integer month) {

        List<Long> organizationCersIdList = saleReportDOList.stream().filter(t -> t.getSubstituteRunning() == 2 && t.getEnterpriseCersId() != 0).map(t -> t.getEnterpriseCersId()).distinct().collect(Collectors.toList());

        if (CollectionUtil.isEmpty(organizationCersIdList)) {

            return;
        }
        // 备份表后缀
        String tableSuffix = BackupUtil.generateTableSuffix(year, month);
        List<CrmEnterpriseRelationPinchRunnerDO> crmEnterpriseRelationPinchRunnerList = crmEnterpriseRelationPinchRunnerService.getByCrmEnterpriseIdAndRelationShipIds(null, organizationCersIdList, tableSuffix);

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
        });

        if (log.isDebugEnabled()) {
            log.debug(" 设置三者关系代跑数据:{}", saleReportDOList);
        }
    }

    /**
     * 设置商品分组
     *
     * @param saleReportDOList
     * @param year
     * @param month
     */
    private void setGoodsProductGroup(List<FlowWashInventoryReportDO> saleReportDOList, Integer year, Integer month) {

        List<Long> productGroupIds = saleReportDOList.stream().filter(t -> t.getProductGroupId() != 0).map(t -> t.getProductGroupId()).distinct().collect(Collectors.toList());

        if (CollectionUtil.isEmpty(productGroupIds)) {

            return;
        }
        // 备份表后缀
        String tableSuffix = BackupUtil.generateTableSuffix(year, month);
        List<CrmGoodsGroupDTO> goodsGroupDOList = crmGoodsGroupService.findBakGroupByIds(productGroupIds, tableSuffix);

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
    private void setCrmSupplyInfo(List<FlowWashInventoryReportDO> reportDOList, Integer year, Integer month) {
        // 供应商信息
        List<Long> crmIdList = reportDOList.stream().map(t -> t.getCrmId()).filter(t -> t != 0).distinct().collect(Collectors.toList());


        if (CollectionUtil.isEmpty(crmIdList)) {

            return;
        }


        List<CrmSupplierDO> supplierList = crmSupplierService.listSuffixByCrmEnterpriseIdList(crmIdList, BackupUtil.generateTableSuffix(year, month));

        if (CollectionUtil.isEmpty(supplierList)) {

            return;
        }

        Map<Long, CrmSupplierDO> crmEnterpriseMap = supplierList.stream().collect(Collectors.toMap(t -> t.getCrmEnterpriseId(), Function.identity()));
        reportDOList.forEach(saleReportDO -> {
            CrmSupplierDO supplier = Optional.ofNullable(crmEnterpriseMap.get(saleReportDO.getCrmId())).orElse(new CrmSupplierDO());
            saleReportDO.setSupplierLevel(Optional.ofNullable(supplier.getSupplierLevel()).orElse(0));
            saleReportDO.setSupplierAttribute(Optional.ofNullable(supplier.getSupplierAttribute()).orElse(0));
            saleReportDO.setGeneralMedicineLevel(Optional.ofNullable(supplier.getGeneralMedicineLevel()).orElse(0));
            saleReportDO.setSupplierSaleType(Optional.ofNullable(supplier.getSupplierSaleType()).orElse(0));
            saleReportDO.setBaseSupplierInfo(Optional.ofNullable(supplier.getBaseSupplierInfo()).orElse(0));
            saleReportDO.setHeadChainFlag(Optional.ofNullable(supplier.getHeadChainFlag()).orElse(0));
            saleReportDO.setChainAttribute(Optional.ofNullable(supplier.getChainAttribute()).orElse(0));
            saleReportDO.setChainKaFlag(Optional.ofNullable(supplier.getChainKaFlag()).orElse(0));
            saleReportDO.setParentSupplierName(Optional.ofNullable(supplier.getParentSupplierName()).orElse(""));
            saleReportDO.setParentSupplierCode(Optional.ofNullable(supplier.getParentSupplierCode()).orElse(""));
            saleReportDO.setLockTime(Optional.ofNullable(supplier.getFirstLockTime()).orElse(DateUtil.parse("1970-01-01", "yyyy-MM-dd")));
            saleReportDO.setLastUnLockTime(Optional.ofNullable(supplier.getLastUnlockTime()).orElse(DateUtil.parse("1970-01-01", "yyyy-MM-dd")));

        });

        if (log.isDebugEnabled()) {
            log.debug("设置商业供应商信息:{}", reportDOList);
        }
    }


    /**
     * 设置商品信息
     *
     * @param reportDOList
     */
    private void setGoodsInfo(List<FlowWashInventoryReportDO> reportDOList, Integer year, Integer month) {
        List<FlowWashInventoryReportDO> selectGoodInfoList = reportDOList.stream().filter(t -> t.getGoodsCode() != 0l).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(selectGoodInfoList)) {

            return;
        }


        List<Long> goodCodeList = selectGoodInfoList.stream().map(t -> t.getGoodsCode()).distinct().collect(Collectors.toList());
        List<CrmGoodsInfoDTO> bakGoodsInfoList = crmGoodsInfoService.findBakByCodeList(goodCodeList, BackupUtil.generateTableSuffix(year, month));

        if (CollectionUtil.isEmpty(bakGoodsInfoList)) {

            return;
        }

        Map<Long, CrmGoodsInfoDTO> crmGoodsInfoDTOMap = bakGoodsInfoList.stream().collect(Collectors.toMap(CrmGoodsInfoDTO::getGoodsCode, Function.identity()));
        selectGoodInfoList.stream().filter(t -> t.getGoodsCode() != 0l).forEach(saleReportDO -> {
            CrmGoodsInfoDTO crmGoodsInfoDTO = crmGoodsInfoDTOMap.getOrDefault(saleReportDO.getGoodsCode(), new CrmGoodsInfoDTO().setSupplyPrice(BigDecimal.ZERO));
            saleReportDO.setGoodsName(Optional.ofNullable(crmGoodsInfoDTO.getGoodsName()).orElse(""));
            saleReportDO.setSpecifications(Optional.ofNullable(crmGoodsInfoDTO.getGoodsSpec()).orElse(""));
            // 设置商品金额
            saleReportDO.setTotalAmount(NumberUtil.round(NumberUtil.mul(saleReportDO.getInventoryQuantity(), crmGoodsInfoDTO.getSupplyPrice()), 2));
        });


        if (log.isDebugEnabled()) {

            log.debug("设置商品信息:{}", reportDOList);
        }
    }

}
