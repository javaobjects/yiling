package com.yiling.dataflow.wash.handler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmHosDruRelActiveRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.entity.CrmHospitalDrugstoreRelationDO;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.dataflow.crm.service.CrmHospitalDrugstoreRelationService;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseCustomerMappingRequest;
import com.yiling.dataflow.utils.BackupUtil;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.flow.dto.FlowEnterpriseCustomerMappingDTO;
import com.yiling.dataflow.flow.entity.FlowEnterpriseCustomerMappingDO;
import com.yiling.dataflow.flow.service.FlowEnterpriseCustomerMappingService;
import com.yiling.dataflow.flowcollect.dto.FlowMonthSalesDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.entity.FlowFleeingGoodsDO;
import com.yiling.dataflow.flowcollect.entity.SaleAppealGoodsFormInfoDO;
import com.yiling.dataflow.flowcollect.service.FlowFleeingGoodsService;
import com.yiling.dataflow.flowcollect.service.FlowMonthSalesService;
import com.yiling.dataflow.flowcollect.service.SaleAppealGoodsFormInfoService;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.entity.FlowSaleDO;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.dataflow.utils.FlowDataIdUtils;
import com.yiling.dataflow.wash.entity.FlowMonthWashControlDO;
import com.yiling.dataflow.wash.entity.FlowMonthWashTaskDO;
import com.yiling.dataflow.wash.entity.FlowSaleWashDO;
import com.yiling.dataflow.wash.enums.CollectionMethodEnum;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.dataflow.wash.enums.FlowDataWashStatusEnum;
import com.yiling.dataflow.wash.enums.WashErrorEnum;
import com.yiling.dataflow.wash.service.FlowSaleWashService;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/3/4
 */
@Slf4j
@Service
public class FlowSaleWashHandler extends FlowWashHandler<FlowSaleWashDO> {

    @Autowired
    private FlowMonthSalesService flowMonthSalesService;

    @Autowired
    private FlowSaleService flowSaleService;

    @Autowired
    private FlowSaleWashService flowSaleWashService;

    @Autowired
    private FlowEnterpriseCustomerMappingService flowEnterpriseCustomerMappingService;

    @Autowired
    private FlowFleeingGoodsService flowFleeingGoodsService;

    @Autowired
    private SaleAppealGoodsFormInfoService saleAppealGoodsFormInfoService;

    @Autowired
    private CrmEnterpriseService crmEnterpriseService;

    @Autowired
    private CrmHospitalDrugstoreRelationService crmHospitalDrugstoreRelationService;

    @Override
    protected void trimHandle(FlowSaleWashDO entity) {
        if (StringUtils.isNotEmpty(entity.getEnterpriseName())) {
            entity.setEnterpriseName(entity.getEnterpriseName().trim());
        }
        if (StringUtils.isNotEmpty(entity.getGoodsName())) {
            entity.setGoodsName(entity.getGoodsName().trim());
        }
        if (StringUtils.isNotEmpty(entity.getSoSpecifications())) {
            entity.setSoSpecifications(entity.getSoSpecifications().trim());
        }

    }

    @Override
    protected boolean isNeedCheckFLowOut(FlowSaleWashDO flowSaleWashDO) {
        if (FlowClassifyEnum.NORMAL.getCode().equals(flowSaleWashDO.getFlowClassify())) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean isNeedGoodsMatch() {
        return true;
    }

    @Override
    protected boolean isNeedCustomMatch() {
        return true;
    }

    @Override
    protected boolean isNeedSupplierMatch() {
        return false;
    }

    @Override
    protected boolean isNeedDuplicateCheck(FlowSaleWashDO flowSaleWashDO) {
        if (flowSaleWashDO.getCreateType() == 1) {  // 自动生成的流向不做重复检查
            return false;
        }
        return true;
    }

    @Override
    protected boolean isNeedOrganizationCersIdMatch() {
        return true;
    }

    @Override
    public void setMappingStatus(FlowSaleWashDO flowSaleWashDO) {
        if (flowSaleWashDO.getCrmGoodsCode() != null && flowSaleWashDO.getCrmGoodsCode() > 0) {
            flowSaleWashDO.setGoodsMappingStatus(1);
        }
        if (flowSaleWashDO.getCrmOrganizationId() != null && flowSaleWashDO.getCrmOrganizationId() > 0)  {
            flowSaleWashDO.setOrganizationMappingStatus(1);
        }
    }

    @Override
    protected void updateFlowWashEntity(FlowSaleWashDO flowSaleWashDO) {
        flowSaleWashService.updateById(flowSaleWashDO);
    }


    @Override
    protected List<FlowSaleWashDO> extractFlowDataList(FlowMonthWashTaskDO flowMonthWashTask, FlowMonthWashControlDO flowMonthWashControl) {
        List<FlowSaleWashDO> list = new ArrayList<>();

        Integer collectionMethod = flowMonthWashTask.getCollectionMethod();
        if (CollectionMethodEnum.EXCEL.getCode().equals(collectionMethod) || CollectionMethodEnum.SYSTEM_IMPORT.getCode().equals(collectionMethod)) {
            //  excel导入方式
            if (FlowClassifyEnum.NORMAL.getCode().equals(flowMonthWashTask.getFlowClassify()) || FlowClassifyEnum.SUPPLY_TRANS_MONTH_FLOW.getCode().equals(flowMonthWashTask.getFlowClassify())) {
                // 正常
                List<FlowMonthSalesDTO> flowMonthSalesDTOList = findFlowMonthSalesByExcel(flowMonthWashTask.getId());
                for (FlowMonthSalesDTO flowMonthSalesDTO : flowMonthSalesDTOList) {
                    FlowSaleWashDO flowSaleWashDO = PojoUtils.map(flowMonthSalesDTO, FlowSaleWashDO.class);
                    resetFlowSaleWash(flowSaleWashDO, flowMonthWashTask);
                    list.add(flowSaleWashDO);
                }
            } else if (FlowClassifyEnum.SALE_APPEAL.getCode().equals(flowMonthWashTask.getFlowClassify())) {
                // 销量申诉
                List<SaleAppealGoodsFormInfoDO> saleAppealGoodsFormInfoDOList = findSaleAppealGoodsFormInfoList(flowMonthWashTask.getId());
                for (SaleAppealGoodsFormInfoDO saleAppealGoodsFormInfoDO : saleAppealGoodsFormInfoDOList) {
                    FlowSaleWashDO flowSaleWashDO = PojoUtils.map(saleAppealGoodsFormInfoDO, FlowSaleWashDO.class);
                    resetFlowSaleWash(flowSaleWashDO, flowMonthWashTask);
                    list.add(flowSaleWashDO);
                }

            } else if (FlowClassifyEnum.FLOW_CROSS.getCode().equals(flowMonthWashTask.getFlowClassify())) {
                // 窜货提报
                List<FlowFleeingGoodsDO> flowFleeingGoodsDOList = findFlowFleeingGoodsList(flowMonthWashTask.getId());
                for (FlowFleeingGoodsDO flowFleeingGoodsDO : flowFleeingGoodsDOList) {
                    FlowSaleWashDO flowSaleWashDO = PojoUtils.map(flowFleeingGoodsDO, FlowSaleWashDO.class);
                    resetFlowSaleWash(flowSaleWashDO, flowMonthWashTask);
                    list.add(flowSaleWashDO);
                }
            } else {
                throw new BusinessException(WashErrorEnum.FLOW_WASH_TASK_FLOW_CLASSIFY_EXCEPTION);
            }
        } else {
            //  获取区间日流向
            Date startTime = flowMonthWashControl.getDataStartTime();
            Date endTime = flowMonthWashControl.getDataEndTime();
            Long eid = flowMonthWashTask.getEid();
            List<FlowSaleDO> flowSaleDOList = findDayFlowSale(eid, startTime, endTime);

            for (FlowSaleDO flowSaleDO : flowSaleDOList) {
                FlowSaleWashDO flowSaleWashDO = PojoUtils.map(flowSaleDO, FlowSaleWashDO.class);
                resetFlowSaleWash(flowSaleWashDO, flowMonthWashTask);
                list.add(flowSaleWashDO);
            }
        }
        flowSaleWashService.batchInsert(list);
        return flowSaleWashService.getByFmwtId(flowMonthWashTask.getId());
    }




    @Override
    protected boolean customMatch(FlowSaleWashDO flowSaleWashDO, List<FlowSaleWashDO> flowDataWashEntityList) {
        boolean isCustomMatch = false;
        FlowEnterpriseCustomerMappingDTO flowEnterpriseCustomerMappingDTO = flowEnterpriseCustomerMappingService.findByCustomerNameAndCrmEnterpriseId(flowSaleWashDO.getEnterpriseName(), flowSaleWashDO.getCrmEnterpriseId());

        if (flowEnterpriseCustomerMappingDTO == null || flowEnterpriseCustomerMappingDTO.getCrmOrgId() == 0) {
            // 若匹配不到则先从行业库中匹配，若能完全匹配，则也算对照上
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseService.getCrmEnterpriseCodeByName(flowSaleWashDO.getEnterpriseName(), false);
            initOrUpdateFlowEnterpriseCustomerMappingDTO(flowSaleWashDO, crmEnterpriseDTO, flowEnterpriseCustomerMappingDTO);
            if (crmEnterpriseDTO != null) {
                flowSaleWashDO.setCrmOrganizationId(crmEnterpriseDTO.getId());
                flowSaleWashDO.setCrmOrganizationName(crmEnterpriseDTO.getName());
                // 有对照关系时，判断零售机构是否存在医院绑定关系
                List<FlowSaleWashDO> autoList = hospitalDrugstoreRelHandle(flowSaleWashDO);
                flowDataWashEntityList.addAll(autoList);
                return true;
            }
            return false;
        }

        if (flowEnterpriseCustomerMappingDTO.getCrmOrgId() > 0) {
            flowSaleWashDO.setCrmOrganizationId(flowEnterpriseCustomerMappingDTO.getCrmOrgId());
            flowSaleWashDO.setCrmOrganizationName(flowEnterpriseCustomerMappingDTO.getOrgName());
            // 有对照关系时，判断零售机构是否存在医院绑定关系
            List<FlowSaleWashDO> autoList = hospitalDrugstoreRelHandle(flowSaleWashDO);
            flowDataWashEntityList.addAll(autoList);
            isCustomMatch = true;
        }
        return isCustomMatch;
    }

    @Override
    protected List<FlowSaleWashDO> hospitalDrugstoreRelHandle(FlowSaleWashDO entity) {
        List<FlowSaleWashDO> list = new ArrayList<>();

        if (entity.getCreateType() == 1) {
            return list;   // 系统自动生成的直接跳过
        }
        //  院外药店关系处理 产品和客户必须对照上
        if (entity.getCrmOrganizationId() > 0 && entity.getCrmGoodsCode() > 0) {
            CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getSuffixByCrmEnterpriseId(entity.getCrmOrganizationId(), BackupUtil.generateTableSuffix(entity.getYear(), entity.getMonth()));
            if (crmEnterpriseDO == null || !CrmSupplyChainRoleEnum.PHARMACY.getCode().equals(crmEnterpriseDO.getSupplyChainRole())) {
                return list;
            }
            QueryCrmHosDruRelActiveRequest request = new QueryCrmHosDruRelActiveRequest();
            request.setDrugstoreOrgId(entity.getCrmOrganizationId());
            request.setCrmGoodsCode(entity.getCrmGoodsCode());
            CrmHospitalDrugstoreRelationDO crmHospitalDrugstoreRelationDO = crmHospitalDrugstoreRelationService.getActiveDataByDrugstoreIdAndGoodsCode(request);
            if (crmHospitalDrugstoreRelationDO == null) {
                return list;
            }

            // 系统自动生成一正一负的两条数据
            list.addAll(generateAutoList(entity, crmHospitalDrugstoreRelationDO));
            flowSaleWashService.batchInsert(list);
        }
        return list;
    }



    @Override
    protected void organizationCersIdMatch(FlowSaleWashDO flowSaleWashDO) {
        if (flowSaleWashDO.getCrmOrganizationId() == null || flowSaleWashDO.getCrmOrganizationId() == 0) {
            flowSaleWashDO.setOrganizationCersId(0L);
            flowSaleWashDO.setUnlockFlag(1);    // 非锁流向
            return;
        }

        // 匹配客户三者关系
        Long enterpriseCersId = crmEnterpriseRelationShipService.listSuffixByOrgIdAndGoodsCode(flowSaleWashDO.getCrmGoodsCode(), flowSaleWashDO.getCrmOrganizationId(), BackupUtil.generateTableSuffix(flowSaleWashDO.getYear(), flowSaleWashDO.getMonth()));
        flowSaleWashDO.setOrganizationCersId(enterpriseCersId);

        //  判断设置是否非锁流向
        flowSaleWashService.setFlowSaleWashOrgUnlockFlag(flowSaleWashDO, enterpriseCersId);
    }

    @Override
    protected String duplicateCheckKey(FlowSaleWashDO flowSaleWashDO) {
        Date soTime = flowSaleWashDO.getSoTime();
        String enterpriseName = flowSaleWashDO.getEnterpriseName();
        String goodsName = flowSaleWashDO.getGoodsName();
        String soSpecifications = flowSaleWashDO.getSoSpecifications();
        String soBatchNo = flowSaleWashDO.getSoBatchNo();
        BigDecimal soQuantity = flowSaleWashDO.getSoQuantity();
        return soTime + "," + enterpriseName + "," + goodsName + "," + soSpecifications + "," + soBatchNo + "," + soQuantity;
    }

    @Override
    protected void setConversionQuantity(FlowSaleWashDO flowSaleWashDO, Integer convertUnit, BigDecimal convertNumber) {
        try {
            // 设置换算数量
            BigDecimal conversionQuantity;
            if (convertNumber.compareTo(BigDecimal.ZERO) == 0) {
                conversionQuantity = flowSaleWashDO.getSoQuantity();
            } else {
                if (convertUnit == 1) {
                    conversionQuantity = flowSaleWashDO.getSoQuantity().multiply(convertNumber);
                } else {
                    conversionQuantity = flowSaleWashDO.getSoQuantity().divide(convertNumber, 6, RoundingMode.HALF_UP);
                }
            }
            flowSaleWashDO.setConversionQuantity(conversionQuantity);
        } catch (Exception e) {
            log.error("销售月流向明细清洗，id={}，换算数量计算失败", flowSaleWashDO.getId(), e);
        }

    }


    private void initOrUpdateFlowEnterpriseCustomerMappingDTO(FlowSaleWashDO flowSaleWashDO,  CrmEnterpriseDTO crmEnterpriseDTO, FlowEnterpriseCustomerMappingDTO flowEnterpriseCustomerMappingDTO) {
        SaveFlowEnterpriseCustomerMappingRequest request = new SaveFlowEnterpriseCustomerMappingRequest();
        if (flowEnterpriseCustomerMappingDTO != null) {     // 更新（只更新没有对照表没有对照的）
            request = PojoUtils.map(flowEnterpriseCustomerMappingDTO, SaveFlowEnterpriseCustomerMappingRequest.class);
        } else {    // 新增
            request.setFlowCustomerName(flowSaleWashDO.getEnterpriseName());
            request.setEnterpriseName(flowSaleWashDO.getName());    // 这里flowSaleWashDO的是客户名称，name是经销商名称
            request.setCrmEnterpriseId(flowSaleWashDO.getCrmEnterpriseId());
            request.setProvinceCode(flowSaleWashDO.getProvinceCode());
            request.setProvince(flowSaleWashDO.getProvinceName());
        }
        if (crmEnterpriseDTO != null) {
            request.setCrmOrgId(crmEnterpriseDTO.getId());
            request.setOrgName(crmEnterpriseDTO.getName());
            request.setOrgLicenseNumber(crmEnterpriseDTO.getLicenseNumber());
        }
        request.setLastUploadTime(new Date());

        flowEnterpriseCustomerMappingService.saveOrUpdate(request);
    }

    private List<FlowMonthSalesDTO> findFlowMonthSalesByExcel(Long fmwtId) {
        List<FlowMonthSalesDTO> list = new ArrayList<>();
        QueryFlowMonthPageRequest request = new QueryFlowMonthPageRequest();
        request.setTaskId(fmwtId);

        int current = 1;
        int size = 1000;
        while (true) {
            request.setCurrent(current);
            request.setSize(size);
            Page<FlowMonthSalesDTO> page = flowMonthSalesService.queryFlowMonthSalePage(request);
            if (page.getRecords() == null || page.getRecords().size() == 0) {
                break;
            }
            list.addAll(page.getRecords());
            if (page.getRecords().size() < size) {
                break;
            }
            current++;
        }
        return list;
    }

    private List<FlowFleeingGoodsDO> findFlowFleeingGoodsList(Long fmwtId) {
        List<FlowFleeingGoodsDO> list = new ArrayList<>();
        QueryFlowMonthPageRequest request = new QueryFlowMonthPageRequest();
        request.setTaskId(fmwtId);

        int current = 1;
        int size = 1000;
        while (true) {
            request.setCurrent(current);
            request.setSize(size);
            Page<FlowFleeingGoodsDO> page = flowFleeingGoodsService.pageList(request);
            if (page.getRecords() == null || page.getRecords().size() == 0) {
                break;
            }
            list.addAll(page.getRecords());
            if (page.getRecords().size() < size) {
                break;
            }
            current++;
        }
        return list;
    }

    private List<SaleAppealGoodsFormInfoDO> findSaleAppealGoodsFormInfoList(Long fmwtId) {
        List<SaleAppealGoodsFormInfoDO> list = new ArrayList<>();
        QueryFlowMonthPageRequest request = new QueryFlowMonthPageRequest();
        request.setTaskId(fmwtId);

        int current = 1;
        int size = 1000;
        while (true) {
            request.setCurrent(current);
            request.setSize(size);

            Page<SaleAppealGoodsFormInfoDO> page = saleAppealGoodsFormInfoService.pageList(request);
            if (page.getRecords() == null || page.getRecords().size() == 0) {
                break;
            }
            list.addAll(page.getRecords());
            if (page.getRecords().size() < size) {
                break;
            }
            current++;
        }
        return list;
    }

    private List<FlowSaleDO> findDayFlowSale(Long eid, Date startTime, Date endTime) {
        List<FlowSaleDO> list = new ArrayList<>();

        QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();
        request.setEid(eid);
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        int current = 1;
        int size = 1000;
        int pageCount = 0;

        Integer count = flowSaleService.getCountByEidAndSoTime(eid, startTime, endTime);
        if (count <= 0) {
            return list;
        }

        if (count % size == 0) {
            pageCount = count / size;
        } else {
            pageCount = count / size + 1;
        }

        while (current <= pageCount) {
            request.setCurrent(current);
            request.setSize(size);
            Page<FlowSaleDO> resultPage = flowSaleService.page(request);
            List<FlowSaleDO> flowList = resultPage.getRecords();
            list.addAll(flowList);
            current++;
        }
        return list;
    }



    public void resetFlowSaleWash(FlowSaleWashDO flowSaleWashDO, FlowMonthWashTaskDO flowMonthWashTask) {
        flowSaleWashDO.setId(null);
        flowSaleWashDO.setFlowKey(FlowDataIdUtils.nextId(flowMonthWashTask.getFlowClassify(), flowMonthWashTask.getFlowType()));
        flowSaleWashDO.setCrmEnterpriseId(flowMonthWashTask.getCrmEnterpriseId());
        flowSaleWashDO.setFmwtId(flowMonthWashTask.getId());
        flowSaleWashDO.setYear(flowMonthWashTask.getYear());
        flowSaleWashDO.setMonth(flowMonthWashTask.getMonth());
        flowSaleWashDO.setName(flowMonthWashTask.getName());
        flowSaleWashDO.setEid(flowMonthWashTask.getEid());

        flowSaleWashDO.setCrmGoodsCode(0L);
        flowSaleWashDO.setFlowClassify(flowMonthWashTask.getFlowClassify());
        flowSaleWashDO.setWashStatus(FlowDataWashStatusEnum.NOT_WASH.getCode());
        flowSaleWashDO.setCreateUser(0L);
        flowSaleWashDO.setUpdateUser(0L);
        flowSaleWashDO.setCreateTime(new Date());
        flowSaleWashDO.setUpdateTime(new Date());
        flowSaleWashDO.setRemark("");
    }

    private List<FlowSaleWashDO> generateAutoList(FlowSaleWashDO entity, CrmHospitalDrugstoreRelationDO crmHospitalDrugstoreRelationDO) {
        List<FlowSaleWashDO> list = new ArrayList<>();

        // 系统自动生成一正一负的两条数据
        FlowSaleWashDO flowSaleWashDO1 = new FlowSaleWashDO();
        PojoUtils.map(entity, flowSaleWashDO1);
        flowSaleWashDO1.setId(null);
        flowSaleWashDO1.setSoQuantity(entity.getSoQuantity().multiply(BigDecimal.valueOf(-1)));     // 替换为负数
        flowSaleWashDO1.setCreateType(1);   // 系统生成
        flowSaleWashDO1.setSourceId(entity.getId());
        flowSaleWashDO1.setFlowClassify(FlowClassifyEnum.HOSPITAL_DRUGSTORE.getCode());
        flowSaleWashDO1.setWashStatus(FlowDataWashStatusEnum.NOT_WASH.getCode());
        flowSaleWashDO1.setCreateUser(0L);
        flowSaleWashDO1.setUpdateUser(0L);
        flowSaleWashDO1.setCreateTime(new Date());
        flowSaleWashDO1.setUpdateTime(new Date());
        flowSaleWashDO1.setRemark("");
        list.add(flowSaleWashDO1);

        FlowSaleWashDO flowSaleWashDO2 = new FlowSaleWashDO();
        PojoUtils.map(entity, flowSaleWashDO2);
        flowSaleWashDO2.setId(null);
        //  替换为绑定医院的机构信息
        flowSaleWashDO2.setEnterpriseName(crmHospitalDrugstoreRelationDO.getHospitalOrgName());
        flowSaleWashDO2.setCrmOrganizationId(crmHospitalDrugstoreRelationDO.getHospitalOrgId());
        flowSaleWashDO2.setCrmOrganizationName(crmHospitalDrugstoreRelationDO.getHospitalOrgName());
        flowSaleWashDO2.setCreateType(1);   // 系统生成
        flowSaleWashDO2.setSourceId(entity.getId());
        flowSaleWashDO1.setFlowClassify(FlowClassifyEnum.HOSPITAL_DRUGSTORE.getCode());
        flowSaleWashDO2.setWashStatus(FlowDataWashStatusEnum.NOT_WASH.getCode());
        flowSaleWashDO2.setCreateUser(0L);
        flowSaleWashDO2.setUpdateUser(0L);
        flowSaleWashDO2.setCreateTime(new Date());
        flowSaleWashDO2.setUpdateTime(new Date());
        flowSaleWashDO2.setRemark("");
        list.add(flowSaleWashDO2);

        return list;
    }

}
