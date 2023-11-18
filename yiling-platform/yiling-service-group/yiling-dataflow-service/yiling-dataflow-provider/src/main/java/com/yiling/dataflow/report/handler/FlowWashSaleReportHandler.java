package com.yiling.dataflow.report.handler;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.yiling.dataflow.agency.entity.CrmHospitalDO;
import com.yiling.dataflow.agency.entity.CrmPharmacyDO;
import com.yiling.dataflow.agency.entity.CrmSupplierDO;
import com.yiling.dataflow.agency.service.CrmHospitalService;
import com.yiling.dataflow.agency.service.CrmPharmacyService;
import com.yiling.dataflow.agency.service.CrmSupplierService;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationPinchRunnerDO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationPinchRunnerService;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.crm.service.CrmGoodsCategoryService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupService;
import com.yiling.dataflow.crm.service.CrmGoodsInfoService;
import com.yiling.dataflow.gb.entity.GbAppealAllocationDO;
import com.yiling.dataflow.report.dao.FlowWashSaleReportMapper;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.entity.FlowWashSaleReportDO;
import com.yiling.dataflow.report.service.FlowWashSaleReportService;
import com.yiling.dataflow.utils.BackupUtil;
import com.yiling.dataflow.wash.entity.FlowMonthWashTaskDO;
import com.yiling.dataflow.wash.entity.FlowSaleWashDO;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.dataflow.wash.enums.WashMappingStatusEnum;
import com.yiling.dataflow.wash.service.FlowMonthWashTaskService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 流向销售报表生成
 *
 * @author zhigang.guo
 * @date: 2023/3/3
 */
@Slf4j
@Component
public class FlowWashSaleReportHandler {
    @Autowired
    private CrmEnterpriseService crmEnterpriseService;
    @Autowired
    private CrmSupplierService crmSupplierService;
    @Autowired
    private CrmHospitalService crmHospitalService;
    @Autowired
    private CrmPharmacyService crmPharmacyService;
    @Autowired
    private CrmGoodsInfoService crmGoodsInfoService;
    @Autowired
    private CrmGoodsCategoryService crmGoodsCategoryService;
    @Autowired
    private CrmGoodsGroupService crmGoodsGroupService;
    @Autowired
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;
    @Autowired
    private FlowWashSaleReportService saleReportService;
    @Autowired
    private FlowMonthWashTaskService flowMonthWashTaskService;
    @Autowired
    private FlowWashSaleReportMapper saleReportMapper;
    @Autowired
    private CrmEnterpriseRelationPinchRunnerService crmEnterpriseRelationPinchRunnerService;

    /**
     * 生成销量清洗报表
     *
     * @param flowWashDOList
     */
    public void generator(List<FlowSaleWashDO> flowWashDOList, Integer year, Integer month) {

        if (CollectionUtil.isEmpty(flowWashDOList)) {

            return;
        }

        // 初始化基础资料信息
        List<FlowWashSaleReportDO> saleReportDOResultList = this.initFlowWashSaleReportReports(flowWashDOList);
        // 500 分批一次
        List<List<FlowWashSaleReportDO>> subList = Lists.partition(saleReportDOResultList, 500);

        subList.forEach(saleReportDOList -> {

            long start1 = System.currentTimeMillis();
            // 设置档案基础资料信息
            this.setFlowWashSaleSupplyEnterpriseInfo(saleReportDOList, year, month);
            // 设置商业零售信息
            this.setCrmSupplyInfo(saleReportDOList, year, month);
            // 设置医疗供应商信息
            this.setCrmHospitalInfo(saleReportDOList, year, month);
            // 设置零售供应商信息
            this.setCrmPharmacyInfo(saleReportDOList, year, month);
            // 设置商品信息
            this.setGoodsInfo(saleReportDOList, year, month);
            // 设置商品品类信息
            this.setGoodsVarietyType(saleReportDOList,year,month);
            // 设置三者关系
            this.setEnterpriseRelationShip(saleReportDOList, year, month);
            // 设置代跑三者关系数据
            this.setEnterpriseRelationPinchRunner(saleReportDOList, year, month);
            // 设置商品分组数据
            this.setGoodsProductGroup(saleReportDOList,year,month);
            // 设置任务信息
            this.setFlowWashSaleReportTaskInfo(saleReportDOList);
            // 先清除数据
            this.removeByWashIds(saleReportDOList.stream().map(t -> t.getFlowKey()).collect(Collectors.toList()));
            // 最后保存数据
            saleReportMapper.insertBatchSomeColumn(saleReportDOList);

            log.info("本次插入销售流向数据耗时:{}", (System.currentTimeMillis() - start1));
        });

    }


    /**
     * 批量保存团购流向数据
     * @param gbAppealAllocationDOS
     * @return
     */
    public boolean batchSaveOrUpdateGbFlowSale(List<GbAppealAllocationDO> gbAppealAllocationDOS) {

        if (CollectionUtil.isEmpty(gbAppealAllocationDOS)) {

            return true;
        }

        List<Long> flowSaleWashIds = gbAppealAllocationDOS.stream().map(t -> t.getFlowWashId()).collect(Collectors.toList());
        // 团购流向数据
        List<String> flowKeys = gbAppealAllocationDOS.stream().map(t -> t.getFlowKey()).collect(Collectors.toList());

        List<FlowWashSaleReportDTO> flowWashSaleReportDTOS = saleReportService.listByFlowSaleWashIds(flowSaleWashIds,ListUtil.toList(FlowClassifyEnum.NORMAL,FlowClassifyEnum.SUPPLY_TRANS_MONTH_FLOW));
        Assert.notEmpty(flowWashSaleReportDTOS,"未查询到源流向数据!");
        Map<Long, FlowWashSaleReportDTO> flowWashSaleReportMap = flowWashSaleReportDTOS.stream().collect(Collectors.toMap(FlowWashSaleReportDTO::getFlowSaleWashId, o -> o, (k1, k2) -> k1));
        List<FlowWashSaleReportDO> flowWashSaleReportDOList = gbAppealAllocationDOS.stream().map(t -> saveOrUpdateGbFlowSale(t,flowWashSaleReportMap.get(t.getFlowWashId()))).collect(Collectors.toList());

        // 先清除数据
        this.removeByWashIds(flowKeys);

        // 最后保存数据
        return  saleReportService.saveBatch(flowWashSaleReportDOList);
    }


    /**
     * 生成销售合并报表团购数据
     * @param t 团购处理结果
     * @return 是否保存成功
     */
    private FlowWashSaleReportDO saveOrUpdateGbFlowSale(GbAppealAllocationDO t,FlowWashSaleReportDTO reportDTO) {

        Assert.notBlank(t.getMatchMonth(),"所属月份不能为空!");
        Assert.notNull(reportDTO,"源流向不能为空!");

        FlowWashSaleReportDO reportDO = PojoUtils.map(reportDTO,FlowWashSaleReportDO.class);

        reportDO.setFlowSaleWashId(Optional.ofNullable(t.getId()).orElse(0l));
        reportDO.setFlowClassify(FlowClassifyEnum.FLOW_GB.getCode());
        reportDO.setSoTime(Optional.ofNullable(t.getSoTime()).orElse(new Date()));
        reportDO.setSoYear(Optional.ofNullable(t.getSoTime()).map(z -> StrUtil.toString(DateUtil.year(z))).orElse(""));
        reportDO.setSoMonth(Optional.ofNullable(t.getSoTime()).map(z -> StrUtil.toString(DateUtil.month(z) + 1)).orElse(""));
        reportDO.setEname(Optional.ofNullable(t.getEname()).orElse(""));
        reportDO.setCrmId(Optional.ofNullable(t.getCrmId()).orElse(0l));
        reportDO.setSoPrice(reportDTO.getSoPrice());
        reportDO.setSoQuantity(reportDTO.getSoQuantity());
        reportDO.setSalesPrice(Optional.ofNullable(t.getPrice()).orElse(BigDecimal.ZERO));
        reportDO.setSoTotalAmount(Optional.ofNullable(t.getTotalAmount()).orElse(BigDecimal.ZERO));
        reportDO.setOriginalEnterpriseName(Optional.ofNullable(t.getOriginalEnterpriseName()).orElse(""));
        reportDO.setFinalQuantity(Optional.ofNullable(t.getQuantity()).orElse(BigDecimal.ZERO));
        reportDO.setCustomerCrmId(Optional.ofNullable(t.getCustomerCrmId()).orElse(0l));
        reportDO.setEnterpriseName(Optional.ofNullable(t.getEnterpriseName()).orElse(""));
        reportDO.setSoGoodsName(Optional.ofNullable(t.getSoGoodsName()).orElse(""));
        reportDO.setFlowKey(Optional.ofNullable(t.getFlowKey()).orElse(""));

        // 设置团购清洗时间,申诉类型,文件名称
        reportDO.setWashTime(t.getCreateTime());
        reportDO.setComplainType(0);
        reportDO.setFileName("");


        // 三者关系数据
        reportDO.setDepartment(Optional.ofNullable(t.getDepartment()).orElse(""));
        reportDO.setBusinessDepartment(Optional.ofNullable(t.getBusinessDepartment()).orElse(""));
        reportDO.setProvincialArea(Optional.ofNullable(t.getProvincialArea()).orElse(""));
        reportDO.setBusinessProvince(Optional.ofNullable(t.getBusinessProvince()).orElse(""));
        reportDO.setDistrictCountyCode(Optional.ofNullable(t.getDistrictCountyCode()).orElse(""));
        reportDO.setDistrictCounty(Optional.ofNullable(t.getDistrictCounty()).orElse(""));
        reportDO.setSuperiorSupervisorCode(Optional.ofNullable(t.getSuperiorSupervisorCode()).orElse(""));
        reportDO.setSuperiorSupervisorName(Optional.ofNullable(t.getSuperiorSupervisorName()).orElse(""));
        reportDO.setRepresentativeCode(Optional.ofNullable(t.getRepresentativeCode()).orElse(""));
        reportDO.setRepresentativeName(Optional.ofNullable(t.getRepresentativeName()).orElse(""));
        reportDO.setPostCode(Optional.ofNullable(t.getPostCode()).orElse(0l));
        reportDO.setPostName(Optional.ofNullable(t.getPostName()).orElse(""));


        Date date = DateUtil.parse(t.getMatchMonth(), "yyyy-MM");

        Integer year = DateUtil.year(date);
        Integer month = DateUtil.month(date) + 1;

        reportDO.setYear(StrUtil.toString(year));
        reportDO.setMonth(StrUtil.toString(month));
        reportDO.setRecordMonth(t.getMatchMonth());

        return reportDO;
    }



    /**
     * 根据flowSaleWashId清除报表数据
     *
     * @param flowKeys
     */
    private void removeByWashIds(List<String> flowKeys) {

        QueryWrapper<FlowWashSaleReportDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(FlowWashSaleReportDO::getFlowKey, flowKeys);

        // 先清除数据
        saleReportService.remove(wrapper);
    }



    /**
     * 设置基础资料信息
     *
     * @param flowSaleWashDOList
     * @return
     */
    private List<FlowWashSaleReportDO> initFlowWashSaleReportReports(List<FlowSaleWashDO> flowSaleWashDOList) {

        return flowSaleWashDOList.stream().map(t -> {

            FlowWashSaleReportDO reportDO = new FlowWashSaleReportDO();
            // 初始报表,防止出现空值
            reportDO.initModel();
            reportDO.setFlowSaleWashId(Optional.ofNullable(t.getId()).orElse(0l));
            reportDO.setFlowKey(Optional.ofNullable(t.getFlowKey()).orElse(""));
            reportDO.setSoTime(Optional.ofNullable(t.getSoTime()).orElse(new Date()));
            reportDO.setSoYear(Optional.ofNullable(t.getSoTime()).map(z -> StrUtil.toString(DateUtil.year(z))).orElse(""));
            reportDO.setSoMonth(Optional.ofNullable(t.getSoTime()).map(z -> StrUtil.toString(DateUtil.month(z) + 1)).orElse(""));
            reportDO.setEname(Optional.ofNullable(t.getName()).orElse(""));
            reportDO.setCrmId(Optional.ofNullable(t.getCrmEnterpriseId()).orElse(0l));
            reportDO.setSoQuantity(Optional.ofNullable(t.getSoQuantity()).orElse(BigDecimal.ZERO));
            reportDO.setSoUnit(Optional.ofNullable(t.getSoUnit()).orElse(""));
            reportDO.setSoBatchNo(Optional.ofNullable(t.getSoBatchNo()).orElse(""));
            reportDO.setSoPrice(Optional.ofNullable(t.getSoPrice()).orElse(BigDecimal.ZERO));
            reportDO.setOriginalEnterpriseName(Optional.ofNullable(t.getEnterpriseName()).orElse(""));
            reportDO.setFinalQuantity(Optional.ofNullable(t.getConversionQuantity()).orElse(BigDecimal.ZERO));
            reportDO.setCustomerCrmId(Optional.ofNullable(t.getCrmOrganizationId()).orElse(0l));
            reportDO.setEnterpriseName(Optional.ofNullable(t.getCrmOrganizationName()).orElse(""));
            reportDO.setOrganizationCersId(Optional.ofNullable(t.getOrganizationCersId()).orElse(0l));
            reportDO.setEnterpriseCersId(Optional.ofNullable(t.getEnterpriseCersId()).orElse(0l));
            reportDO.setSoGoodsName(Optional.ofNullable(t.getGoodsName()).orElse(""));
            reportDO.setGoodsCode(Optional.ofNullable(t.getCrmGoodsCode()).orElse(0l));
            reportDO.setSoSpecifications(Optional.ofNullable(t.getSoSpecifications()).orElse(""));
            reportDO.setFmwtId(Optional.ofNullable(t.getFmwtId()).orElse(0l));
            reportDO.setFlowClassify(Optional.ofNullable(t.getFlowClassify()).orElse(0));
            reportDO.setRepresentativeCode(Optional.ofNullable(t.getRepresentativeCode()).orElse(""));
            // 机构是否锁定：（1）如果是商业公司，锁定类型为“打单”的，都算作锁定，字段值为“是”。（2）其他的机构，无论是零售/医疗/商业，有三者关系的，为“是”。  没有三者关系的，为“否”。
            Integer isChainFlag = reportDO.getOrganizationCersId() != 0 ? Constants.IS_YES : Constants.IS_NO;
            reportDO.setIsChainFlag(isChainFlag);

            reportDO.setMappingStatus(WashMappingStatusEnum.BOTH_MISMATCH.getCode());

            if (t.getGoodsMappingStatus() == Constants.IS_YES && t.getOrganizationMappingStatus() == Constants.IS_YES) {
                reportDO.setMappingStatus(WashMappingStatusEnum.MATCH_SUCCESS.getCode());
            } else if (t.getGoodsMappingStatus() == Constants.IS_YES) {
                reportDO.setMappingStatus(WashMappingStatusEnum.CUSTOM_MISMATCH.getCode());
            } else if (t.getOrganizationMappingStatus() == Constants.IS_YES) {
                reportDO.setMappingStatus(WashMappingStatusEnum.GOODS_MISMATCH.getCode());
            }

            // 流向是否锁定
            Integer isLockFlag = 0;

            if (ObjectUtil.isNull(t.getUnlockFlag())) {
                isLockFlag = 0;
            } else if (0 == t.getUnlockFlag()) {
                isLockFlag = Constants.IS_YES;
            } else if (1 == t.getUnlockFlag()) {
                isLockFlag = Constants.IS_NO;
            }

            reportDO.setIsLockFlag(isLockFlag);

            return reportDO;

        }).collect(Collectors.toList());

    }

    /**
     * 设置流向销售报表供应商信息
     *
     * @param saleReportDOs
     */
    private void setFlowWashSaleSupplyEnterpriseInfo(List<FlowWashSaleReportDO> saleReportDOs, Integer year, Integer month) {

        List<Long> supplyCrmIdList = saleReportDOs.stream().map(t -> t.getCrmId()).filter(t -> t != 0).distinct().collect(Collectors.toList());
        List<Long> customerCrmIdList = saleReportDOs.stream().map(t -> t.getCustomerCrmId()).filter(t -> t != 0).distinct().collect(Collectors.toList());
        List<Long> crmIdList = Stream.of(supplyCrmIdList, customerCrmIdList).flatMap(Collection::stream).distinct().collect(Collectors.toList());

        // 查询备份表数据
        List<CrmEnterpriseDO> crmEnterpriseDOList = crmEnterpriseService.listSuffixByCrmEnterpriseIdList(crmIdList, BackupUtil.generateTableSuffix(year, month));

        if (CollectionUtil.isEmpty(crmEnterpriseDOList)) {

            throw new RuntimeException("供应商信息未查询到!");
        }

        Map<Long, CrmEnterpriseDO> crmEnterpriseMap = crmEnterpriseDOList.stream().collect(Collectors.toMap(t -> t.getId(), Function.identity()));

        saleReportDOs.forEach(saleReportDO -> {
            CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseMap.get(saleReportDO.getCrmId());
            CrmEnterpriseDO customerCrmEnterpriseDO = crmEnterpriseMap.getOrDefault(saleReportDO.getCustomerCrmId(), new CrmEnterpriseDO());
            saleReportDO.setSupplierProvinceName(Optional.ofNullable(crmEnterpriseDO).map(t -> t.getProvinceName()).orElse(""));
            saleReportDO.setSupplierCityName(Optional.ofNullable(crmEnterpriseDO).map(t -> t.getCityName()).orElse(""));
            saleReportDO.setSupplierRegionName(Optional.ofNullable(crmEnterpriseDO).map(t -> t.getRegionName()).orElse(""));
            saleReportDO.setProvinceCode(Optional.ofNullable(customerCrmEnterpriseDO.getProvinceCode()).orElse(""));
            saleReportDO.setProvinceName(Optional.ofNullable(customerCrmEnterpriseDO.getProvinceName()).orElse(""));
            saleReportDO.setCityCode(Optional.ofNullable(customerCrmEnterpriseDO.getCityCode()).orElse(""));
            saleReportDO.setCityName(Optional.ofNullable(customerCrmEnterpriseDO.getCityName()).orElse(""));
            saleReportDO.setRegionCode(Optional.ofNullable(customerCrmEnterpriseDO.getRegionCode()).orElse(""));
            saleReportDO.setRegionName(Optional.ofNullable(customerCrmEnterpriseDO.getRegionName()).orElse(""));
            saleReportDO.setCustomerSupplyChainRole(Optional.ofNullable(customerCrmEnterpriseDO.getSupplyChainRole()).orElse(0));

        });

        if (log.isDebugEnabled()) {
            log.debug("设置流向销售报表供应商信息:{}", saleReportDOs);
        }
    }

    /**
     * 设置商业供应商信息
     *
     * @param saleReportDOList
     */
    private void setCrmSupplyInfo(List<FlowWashSaleReportDO> saleReportDOList, Integer year, Integer month) {
        // 供应商信息
        List<Long> supplyCrmIdList = saleReportDOList.stream().map(t -> t.getCrmId()).filter(t -> t != 0).distinct().collect(Collectors.toList());
        // 客户信息
        List<Long> customerSupplyCrmIdList = saleReportDOList.stream().filter(saleReportDO -> CrmSupplyChainRoleEnum.DISTRIBUTOR == CrmSupplyChainRoleEnum.getFromCode(saleReportDO.getCustomerSupplyChainRole())).map(t -> t.getCustomerCrmId()).filter(t -> t != 0).distinct().collect(Collectors.toList());

        List<Long> crmIdList = Stream.of(supplyCrmIdList, customerSupplyCrmIdList).flatMap(Collection::stream).distinct().collect(Collectors.toList());

        if (CollectionUtil.isEmpty(crmIdList)) {

            return;
        }

        List<CrmSupplierDO> supplierList = crmSupplierService.listSuffixByCrmEnterpriseIdList(crmIdList, BackupUtil.generateTableSuffix(year, month));

        if (CollectionUtil.isEmpty(supplierList)) {

            return;
        }

        Map<Long, CrmSupplierDO> crmEnterpriseMap = supplierList.stream().collect(Collectors.toMap(t -> t.getCrmEnterpriseId(), Function.identity()));
        saleReportDOList.forEach(saleReportDO -> {
            CrmSupplierDO supplier = Optional.ofNullable(crmEnterpriseMap.get(saleReportDO.getCrmId())).orElse(new CrmSupplierDO());
            saleReportDO.setSupplierLevel(Optional.ofNullable(supplier.getSupplierLevel()).orElse(0));
            saleReportDO.setSupplierAttribute(Optional.ofNullable(supplier.getSupplierAttribute()).orElse(0));
            saleReportDO.setGeneralMedicineLevel(Optional.ofNullable(supplier.getGeneralMedicineLevel()).orElse(0));
            saleReportDO.setSupplierSaleType(Optional.ofNullable(supplier.getSupplierSaleType()).orElse(0));
            saleReportDO.setBaseSupplierInfo(Optional.ofNullable(supplier.getBaseSupplierInfo()).orElse(0));
            saleReportDO.setHeadChainFlag(Optional.ofNullable(supplier.getHeadChainFlag()).orElse(0));
            saleReportDO.setChainAttribute(Optional.ofNullable(supplier.getChainAttribute()).orElse(0));
            saleReportDO.setChainKaFlag(Optional.ofNullable(supplier.getChainKaFlag()).orElse(0));
            saleReportDO.setFlowDepartment(Optional.ofNullable(supplier.getDepartment()).orElse(""));
            saleReportDO.setFlowJobNumber(Optional.ofNullable(supplier.getFlowJobNumber()).orElse(""));
            saleReportDO.setFlowLiablePerson(Optional.ofNullable(supplier.getFlowLiablePerson()).orElse(""));
            saleReportDO.setCommerceJobNumber(Optional.ofNullable(supplier.getCommerceJobNumber()).orElse(""));
            saleReportDO.setCommerceLiablePerson(Optional.ofNullable(supplier.getCommerceLiablePerson()).orElse(""));
            // 机构是否锁定：（1）如果是商业公司，锁定类型为“打单”的，都算作锁定，字段值为“是”。（2）其他的机构，无论是零售/医疗/商业，有三者关系的，为“是”。  没有三者关系的，为“否”。
            Integer isChainFlag = saleReportDO.getOrganizationCersId() != 0 ? Constants.IS_YES : Constants.IS_NO;
            saleReportDO.setIsChainFlag(isChainFlag);
        });

        // 处理客户信息
        saleReportDOList.stream().filter(saleReportDO -> CrmSupplyChainRoleEnum.DISTRIBUTOR == CrmSupplyChainRoleEnum.getFromCode(saleReportDO.getCustomerSupplyChainRole())).forEach(saleReportDO -> {
            CrmSupplierDO customerSupplier = Optional.ofNullable(crmEnterpriseMap.get(saleReportDO.getCustomerCrmId())).orElse(new CrmSupplierDO());
            saleReportDO.setCustomerSupplierLevel(Optional.ofNullable(customerSupplier.getSupplierLevel()).orElse(0));
            saleReportDO.setCustomerGeneralMedicineLevel(Optional.ofNullable(customerSupplier.getGeneralMedicineLevel()).orElse(0));
            saleReportDO.setCustomerSupplierAttribute(Optional.ofNullable(customerSupplier.getSupplierAttribute()).orElse(0));
            saleReportDO.setCustomerHeadChainFlag(Optional.ofNullable(customerSupplier.getHeadChainFlag()).orElse(0));
            saleReportDO.setCustomerChainAttribute(Optional.ofNullable(customerSupplier.getChainAttribute()).orElse(0));
            saleReportDO.setCustomerChainKaFlag(Optional.ofNullable(customerSupplier.getChainKaFlag()).orElse(0));
            saleReportDO.setCustomerParentSupplierName(Optional.ofNullable(customerSupplier.getParentSupplierName()).orElse(""));
            saleReportDO.setCustomerParentSupplierCode(Optional.ofNullable(customerSupplier.getParentSupplierCode()).orElse(""));
            saleReportDO.setCustomerSupplierSaleType(Optional.ofNullable(customerSupplier.getSupplierSaleType()).orElse(0));
            saleReportDO.setCustomerBaseSupplierInfo(Optional.ofNullable(customerSupplier.getBaseSupplierInfo()).orElse(0));
            saleReportDO.setLockTime(Optional.ofNullable(customerSupplier.getFirstLockTime()).orElse(DateUtil.parse("1970-01-01", "yyyy-MM-dd")));
            saleReportDO.setLastUnLockTime(Optional.ofNullable(customerSupplier.getLastUnlockTime()).orElse(DateUtil.parse("1970-01-01", "yyyy-MM-dd")));
            // 机构是否锁定：（1）如果是商业公司，锁定类型为“打单”的，都算作锁定，字段值为“是”。（2）其他的机构，无论是零售/医疗/商业，有三者关系的，为“是”。  没有三者关系的，为“否”。
            if (customerSupplier.getLockType() != null && 1 == customerSupplier.getLockType()) {
                saleReportDO.setIsChainFlag(Constants.IS_YES);
            }
        });


        if (log.isDebugEnabled()) {
            log.debug("设置商业供应商信息:{}", saleReportDOList);
        }
    }


    /**
     * 设置销售报表医疗相关信息
     *
     * @param saleReportDOList
     */
    private void setCrmHospitalInfo(List<FlowWashSaleReportDO> saleReportDOList, Integer year, Integer month) {

        List<FlowWashSaleReportDO> hospitalSaleReportDOList = saleReportDOList.stream().filter(saleReportDO -> CrmSupplyChainRoleEnum.HOSPITAL == CrmSupplyChainRoleEnum.getFromCode(saleReportDO.getCustomerSupplyChainRole())).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(hospitalSaleReportDOList)) {

            return;
        }

        List<Long> hospitalCrmIdList = hospitalSaleReportDOList.stream().map(t -> t.getCustomerCrmId()).filter(t -> t != 0).distinct().collect(Collectors.toList());
        if (CollectionUtil.isEmpty(hospitalCrmIdList)) {

            return;
        }

        List<CrmHospitalDO> crmEnterpriseList = crmHospitalService.listSuffixByCrmEnterpriseIdList(hospitalCrmIdList, BackupUtil.generateTableSuffix(year, month));

        if (CollectionUtil.isEmpty(crmEnterpriseList)) {
            return;
        }

        Map<Long, CrmHospitalDO> crmEnterpriseMap = crmEnterpriseList.stream().collect(Collectors.toMap(t -> t.getCrmEnterpriseId(), Function.identity()));

        hospitalSaleReportDOList.forEach(saleReportDO -> {
            CrmHospitalDO crmEnterprise = crmEnterpriseMap.getOrDefault(saleReportDO.getCustomerCrmId(), new CrmHospitalDO());
            saleReportDO.setCustomerMedicalNature(Optional.ofNullable(crmEnterprise.getMedicalNature()).orElse(0));
            saleReportDO.setCustomerMedicalType(Optional.ofNullable(crmEnterprise.getMedicalType()).orElse(0));
            saleReportDO.setCustomerYlLevel(Optional.ofNullable(crmEnterprise.getYlLevel()).orElse(0));
            saleReportDO.setCustomerNationalGrade(Optional.ofNullable(crmEnterprise.getNationalGrade()).orElse(0));
            saleReportDO.setBaseMedicineFlag(Optional.ofNullable(crmEnterprise.getBaseMedicineFlag()).orElse(0));
            saleReportDO.setLockTime(Optional.ofNullable(crmEnterprise.getFirstLockTime()).orElse(new Date()));
            saleReportDO.setLastUnLockTime(Optional.ofNullable(crmEnterprise.getLastUnlockTime()).orElse(new Date()));
        });

        if (log.isDebugEnabled()) {
            log.debug("设置商业医疗信息:{}", saleReportDOList);
        }
    }


    /**
     * 设置零售商品信息
     *
     * @param saleReportDOList
     */
    private void setCrmPharmacyInfo(List<FlowWashSaleReportDO> saleReportDOList, Integer year, Integer month) {

        List<FlowWashSaleReportDO> pharmacySaleReportDOList = saleReportDOList.stream().filter(saleReportDO -> CrmSupplyChainRoleEnum.PHARMACY == CrmSupplyChainRoleEnum.getFromCode(saleReportDO.getCustomerSupplyChainRole())).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(pharmacySaleReportDOList)) {

            return;
        }

        List<Long> pharmacyCrmIdList = pharmacySaleReportDOList.stream().map(t -> t.getCustomerCrmId()).filter(t -> t != 0).distinct().collect(Collectors.toList());

        if (CollectionUtil.isEmpty(pharmacyCrmIdList)) {

            return;
        }

        List<CrmPharmacyDO> crmPharmacyDOList = crmPharmacyService.listSuffixByCrmEnterpriseIdList(pharmacyCrmIdList, BackupUtil.generateTableSuffix(year, month));
        if (CollectionUtil.isEmpty(crmPharmacyDOList)) {

            return;
        }

        Map<Long, CrmPharmacyDO> crmEnterpriseMap = crmPharmacyDOList.stream().collect(Collectors.toMap(t -> t.getCrmEnterpriseId(), Function.identity()));


        pharmacySaleReportDOList.forEach(saleReportDO -> {
            CrmPharmacyDO crmPharmacyDO = crmEnterpriseMap.getOrDefault(saleReportDO.getCustomerCrmId(), new CrmPharmacyDO());

            saleReportDO.setPharmacyAttribute(Optional.ofNullable(crmPharmacyDO.getPharmacyAttribute()).orElse(0));
            saleReportDO.setPharmacyType(Optional.ofNullable(crmPharmacyDO.getPharmacyType()).orElse(0));
            saleReportDO.setPharmacyLevel(Optional.ofNullable(crmPharmacyDO.getPharmacyLevel()).orElse(0));
            // 临时存储上级公司编码
            saleReportDO.setParentCompanyCode(Optional.ofNullable(crmPharmacyDO.getParentCompanyCode()).orElse(""));
            saleReportDO.setLabelAttribute(Optional.ofNullable(crmPharmacyDO.getLabelAttribute()).orElse(0));
            saleReportDO.setTargetFlag(Optional.ofNullable(crmPharmacyDO.getTargetFlag()).orElse(0));
            saleReportDO.setLockTime(Optional.ofNullable(crmPharmacyDO.getFirstLockTime()).orElse(new Date()));
            saleReportDO.setLastUnLockTime(Optional.ofNullable(crmPharmacyDO.getLastUnlockTime()).orElse(new Date()));
        });


        // 设置上级公司连锁属性
        List<Long> parentCrmIds = pharmacySaleReportDOList.stream().filter(t -> StringUtils.isNotBlank(t.getParentCompanyCode())).map(t -> Long.valueOf(t.getParentCompanyCode())).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(parentCrmIds)) {

            return;
        }

        List<CrmSupplierDO> supplierList = crmSupplierService.listSuffixByCrmEnterpriseIdList(parentCrmIds, BackupUtil.generateTableSuffix(year, month));

        if (CollectionUtil.isEmpty(supplierList)) {

            return;
        }

        Map<Long, CrmSupplierDO> crmSupplyEnterpriseMap = supplierList.stream().collect(Collectors.toMap(t -> t.getCrmEnterpriseId(), Function.identity()));
        pharmacySaleReportDOList.stream().filter(t -> StringUtils.isNotBlank(t.getParentCompanyCode())).forEach(saleReportDO -> {
            CrmSupplierDO crmSupplierDO = crmSupplyEnterpriseMap.getOrDefault(Long.valueOf(saleReportDO.getParentCompanyCode()), new CrmSupplierDO());
            saleReportDO.setPharmacyChainAttribute(Optional.ofNullable(crmSupplierDO.getChainAttribute()).orElse(0));
        });

        if (log.isDebugEnabled()) {
            log.debug("设置零售商品信息:{}", saleReportDOList);
        }
    }

    /**
     * 设置商品信息
     *
     * @param saleReportDOList
     */
    private void setGoodsInfo(List<FlowWashSaleReportDO> saleReportDOList, Integer year, Integer month) {
        List<FlowWashSaleReportDO> selectGoodInfoList = saleReportDOList.stream().filter(t -> t.getGoodsCode() != 0).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(selectGoodInfoList)) {

            return;
        }

        // 备份表后缀
        String tableSuffix = BackupUtil.generateTableSuffix(year, month);

        List<Long> goodCodeList = selectGoodInfoList.stream().map(t -> t.getGoodsCode()).distinct().collect(Collectors.toList());
        List<CrmGoodsInfoDTO> bakGoodsInfoList = crmGoodsInfoService.findBakByCodeList(goodCodeList, tableSuffix);
        if (CollectionUtil.isEmpty(bakGoodsInfoList)) {

            return;
        }

        Map<Long, CrmGoodsInfoDTO> crmGoodsInfoDTOMap = bakGoodsInfoList.stream().collect(Collectors.toMap(CrmGoodsInfoDTO::getGoodsCode, Function.identity()));
        selectGoodInfoList.forEach(saleReportDO -> {
            CrmGoodsInfoDTO crmGoodsInfoDTO = crmGoodsInfoDTOMap.getOrDefault(saleReportDO.getGoodsCode(), new CrmGoodsInfoDTO());
            saleReportDO.setGoodsName(Optional.ofNullable(crmGoodsInfoDTO.getGoodsName()).orElse(""));
            // 临时存储商品分类
            saleReportDO.setCategoryId(Optional.ofNullable(crmGoodsInfoDTO.getCategoryId()).orElse(0l));
            saleReportDO.setSalesPrice(Optional.ofNullable(crmGoodsInfoDTO.getSupplyPrice()).orElse(BigDecimal.ZERO));
            saleReportDO.setSoTotalAmount(NumberUtil.mul(saleReportDO.getFinalQuantity(), saleReportDO.getSalesPrice()));
            saleReportDO.setGoodsSpec(Optional.ofNullable(crmGoodsInfoDTO.getGoodsSpec()).orElse(""));
        });

        if (log.isDebugEnabled()) {
            log.debug("设置商品信息:{}", saleReportDOList);
        }
    }

    /**
     * 设置商品品类信息
     * @param saleReportDOList
     * @param year
     * @param month
     */
    private void setGoodsVarietyType(List<FlowWashSaleReportDO> saleReportDOList, Integer year, Integer month){
        // 查询类目信息
        List<FlowWashSaleReportDO> selectVarietyTypeList = saleReportDOList.stream().filter(t -> t.getCategoryId() != 0l).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(selectVarietyTypeList)) {

            return;
        }

        List<Long> categoryIdList = selectVarietyTypeList.stream().map(t -> t.getCategoryId()).distinct().collect(Collectors.toList());

        // 备份表后缀
        String tableSuffix = BackupUtil.generateTableSuffix(year, month);

        List<CrmGoodsCategoryDTO> crmGoodsCategoryDOS = crmGoodsCategoryService.findBakByCategoryIds(categoryIdList,tableSuffix);

        if (CollectionUtil.isEmpty(crmGoodsCategoryDOS)) {

            return;
        }

        Map<Long, CrmGoodsCategoryDTO> crmGoodsCategoryDOMap = crmGoodsCategoryDOS.stream().collect(Collectors.toMap(CrmGoodsCategoryDTO::getId, Function.identity()));
        selectVarietyTypeList.forEach(t -> {
            CrmGoodsCategoryDTO crmGoodsCategoryDO = crmGoodsCategoryDOMap.get(t.getCategoryId());
            t.setVarietyType(Optional.ofNullable(crmGoodsCategoryDO).map(z -> z.getName()).orElse(""));
        });

    }


    /**
     * 设置企业三者关系数据
     *
     * @param saleReportDOList
     */
    private void setEnterpriseRelationShip(List<FlowWashSaleReportDO> saleReportDOList, Integer year, Integer month) {

        List<Long> organizationCersIds = saleReportDOList.stream().filter(t -> t.getOrganizationCersId() != 0).map(t -> t.getOrganizationCersId()).distinct().collect(Collectors.toList());
        if (CollectionUtil.isEmpty(organizationCersIds)) {

            return;
        }

        List<CrmEnterpriseRelationShipDO> crmEnterpriseRelationShipDOS = crmEnterpriseRelationShipService.listSuffixByIdList(organizationCersIds, BackupUtil.generateTableSuffix(year, month));
        if (CollectionUtil.isEmpty(crmEnterpriseRelationShipDOS)) {
            return;
        }

        Map<Long, CrmEnterpriseRelationShipDO> crmEnterpriseMap = crmEnterpriseRelationShipDOS.stream().collect(Collectors.toMap(t -> t.getId(), Function.identity()));
        saleReportDOList.stream().filter(t -> t.getOrganizationCersId() != 0).forEach(t -> {
            CrmEnterpriseRelationShipDO crmEnterpriseRelationShipDO = crmEnterpriseMap.getOrDefault(t.getOrganizationCersId(), new CrmEnterpriseRelationShipDO());
            t.setDepartment(Optional.ofNullable(crmEnterpriseRelationShipDO.getDepartment()).orElse(""));
            t.setBusinessDepartment(Optional.ofNullable(crmEnterpriseRelationShipDO.getBusinessDepartment()).orElse(""));
            t.setProvincialArea(Optional.ofNullable(crmEnterpriseRelationShipDO.getProvincialArea()).orElse(""));
            t.setBusinessProvince(Optional.ofNullable(crmEnterpriseRelationShipDO.getBusinessProvince()).orElse(""));
            t.setDistrictCountyCode(Optional.ofNullable(crmEnterpriseRelationShipDO.getBusinessAreaCode()).orElse(""));
            t.setDistrictCounty(Optional.ofNullable(crmEnterpriseRelationShipDO.getBusinessArea()).orElse(""));
            t.setSuperiorSupervisorCode(Optional.ofNullable(crmEnterpriseRelationShipDO.getSuperiorSupervisorCode()).orElse(""));
            t.setSuperiorSupervisorName(Optional.ofNullable(crmEnterpriseRelationShipDO.getSuperiorSupervisorName()).orElse(""));
            t.setRepresentativeCode(Optional.ofNullable(crmEnterpriseRelationShipDO.getRepresentativeCode()).orElse(""));
            t.setRepresentativeName(Optional.ofNullable(crmEnterpriseRelationShipDO.getRepresentativeName()).orElse(""));
            t.setPostCode(Optional.ofNullable(crmEnterpriseRelationShipDO.getPostCode()).orElse(0l));
            t.setPostName(Optional.ofNullable(crmEnterpriseRelationShipDO.getPostName()).orElse(""));
            t.setSubstituteRunning(Optional.ofNullable(crmEnterpriseRelationShipDO.getSubstituteRunning()).orElse(1));
            // 临时存储商品分组Id
            t.setProductGroupId(Optional.ofNullable(crmEnterpriseRelationShipDO.getProductGroupId()).orElse(0l));
            t.setProvinceManagerCode(Optional.ofNullable(crmEnterpriseRelationShipDO.getProvincialManagerCode()).orElse(""));
            t.setProvinceManagerName(Optional.ofNullable(crmEnterpriseRelationShipDO.getProvincialManagerName()).orElse(""));
        });

        if (log.isDebugEnabled()) {
            log.debug("设置企业三者关系数据:{}", saleReportDOList);
        }
    }


    /**
     * 设置三者关系代跑数据
     * @param saleReportDOList
     * @param year
     * @param month
     */
    private void setEnterpriseRelationPinchRunner(List<FlowWashSaleReportDO> saleReportDOList, Integer year, Integer month) {

        List<Long> organizationCersIdList = saleReportDOList.stream().filter(t -> t.getSubstituteRunning() == 2 && t.getOrganizationCersId() != 0).map(t -> t.getOrganizationCersId()).distinct().collect(Collectors.toList());

        if (CollectionUtil.isEmpty(organizationCersIdList)) {

            return;
        }
        // 备份表后缀
        String tableSuffix = BackupUtil.generateTableSuffix(year, month);
        List<CrmEnterpriseRelationPinchRunnerDO> crmEnterpriseRelationPinchRunnerList = crmEnterpriseRelationPinchRunnerService.getByCrmEnterpriseIdAndRelationShipIds(null,organizationCersIdList,tableSuffix);

        if (CollectionUtil.isEmpty(crmEnterpriseRelationPinchRunnerList)) {
            return;
        }

        Map<Long, CrmEnterpriseRelationPinchRunnerDO> crmEnterpriseRelationPinchRunnerMap = crmEnterpriseRelationPinchRunnerList.stream().collect(Collectors.toMap(CrmEnterpriseRelationPinchRunnerDO::getEnterpriseCersId, Function.identity()));
        saleReportDOList.stream().filter(t -> t.getSubstituteRunning() == 2 && t.getOrganizationCersId() != 0).forEach(t -> {
            CrmEnterpriseRelationPinchRunnerDO crmEnterpriseRelationPinchRunnerDO = crmEnterpriseRelationPinchRunnerMap.get(t.getOrganizationCersId());
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
     * @param year
     * @param month
     */
    private void setGoodsProductGroup(List<FlowWashSaleReportDO> saleReportDOList, Integer year, Integer month) {

        List<Long> productGroupIds = saleReportDOList.stream().filter(t -> t.getProductGroupId() != 0).map(t -> t.getProductGroupId()).distinct().collect(Collectors.toList());

        if (CollectionUtil.isEmpty(productGroupIds)) {

            return;
        }
        // 备份表后缀
        String tableSuffix = BackupUtil.generateTableSuffix(year, month);
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
     * 设置销售流向相关报表信息
     *
     * @param saleReportDOList
     * @return
     */
    private void setFlowWashSaleReportTaskInfo(List<FlowWashSaleReportDO> saleReportDOList) {

        List<Long> fmwtIdList = saleReportDOList.stream().map(t -> t.getFmwtId()).distinct().collect(Collectors.toList());

        if (CollectionUtil.isEmpty(fmwtIdList)) {

            return;
        }

        List<FlowMonthWashTaskDO> flowMonthWashTaskDOS = flowMonthWashTaskService.listByIds(fmwtIdList);

        if (CollectionUtil.isEmpty(flowMonthWashTaskDOS)) {

            return;
        }

        Map<Long, FlowMonthWashTaskDO> flowMonthWashTaskDOMap = flowMonthWashTaskDOS.stream().collect(Collectors.toMap(FlowMonthWashTaskDO::getId, Function.identity()));
        saleReportDOList.forEach(flowWashSaleReportDO -> {
            FlowMonthWashTaskDO washTaskDO = flowMonthWashTaskDOMap.getOrDefault(flowWashSaleReportDO.getFmwtId(), new FlowMonthWashTaskDO());
            String recordMonth = DateUtil.format(DateUtil.parse(washTaskDO.getYear() + "-" + washTaskDO.getMonth(), "yyyy-MM"), "yyyy-MM");
            flowWashSaleReportDO.setMonth(Optional.ofNullable(washTaskDO.getMonth()).map(t -> t.toString()).orElse(""));
            flowWashSaleReportDO.setYear(Optional.ofNullable(washTaskDO.getYear()).map(t -> t.toString()).orElse(""));
            flowWashSaleReportDO.setRecordMonth(recordMonth);
            flowWashSaleReportDO.setWashTime(Optional.ofNullable(washTaskDO.getCompleteTime()).map(t -> washTaskDO.getCompleteTime()).orElse(new Date()));
            // flowWashSaleReportDO.setFlowClassify(Optional.ofNullable(washTaskDO.getFlowClassify()).orElse(0));
            flowWashSaleReportDO.setCollectionMethod(Optional.ofNullable(washTaskDO.getCollectionMethod()).orElse(0));
            flowWashSaleReportDO.setFileName(Optional.ofNullable(washTaskDO.getFileName()).orElse(""));
            // 申诉类型
            flowWashSaleReportDO.setComplainType(Optional.ofNullable(washTaskDO.getAppealType()).orElse(0));
        });

        if (log.isDebugEnabled()) {
            log.debug("设置任务相关信息:{}", saleReportDOList);
        }
    }

}
