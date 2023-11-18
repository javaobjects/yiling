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
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.flow.dto.FlowEnterpriseSupplierMappingDTO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseCustomerMappingRequest;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseSupplierMappingRequest;
import com.yiling.dataflow.flow.entity.FlowEnterpriseCustomerMappingDO;
import com.yiling.dataflow.flow.entity.FlowEnterpriseSupplierMappingDO;
import com.yiling.dataflow.flow.service.FlowEnterpriseSupplierMappingService;
import com.yiling.dataflow.flowcollect.dto.FlowMonthPurchaseDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.service.FlowMonthPurchaseService;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.entity.FlowPurchaseDO;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.utils.FlowDataIdUtils;
import com.yiling.dataflow.wash.entity.FlowGoodsBatchDetailWashDO;
import com.yiling.dataflow.wash.entity.FlowMonthWashControlDO;
import com.yiling.dataflow.wash.entity.FlowMonthWashTaskDO;
import com.yiling.dataflow.wash.entity.FlowPurchaseWashDO;
import com.yiling.dataflow.wash.enums.CollectionMethodEnum;
import com.yiling.dataflow.wash.enums.FlowDataWashStatusEnum;
import com.yiling.dataflow.wash.service.FlowPurchaseWashService;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/3/7
 */
@Slf4j
@Service
public class FlowPurchaseWashHandler extends FlowWashHandler<FlowPurchaseWashDO> {

    @Autowired
    private FlowMonthPurchaseService flowMonthPurchaseService;

    @Autowired
    private FlowPurchaseService flowPurchaseService;

    @Autowired
    private FlowPurchaseWashService flowPurchaseWashService;

    @Autowired
    private FlowEnterpriseSupplierMappingService flowEnterpriseSupplierMappingService;

    @Autowired
    private CrmEnterpriseService crmEnterpriseService;

    @Override
    protected void trimHandle(FlowPurchaseWashDO entity) {
        if (StringUtils.isNotEmpty(entity.getEnterpriseName())) {
            entity.setEnterpriseName(entity.getEnterpriseName().trim());
        }
        if (StringUtils.isNotEmpty(entity.getGoodsName())) {
            entity.setGoodsName(entity.getGoodsName().trim());
        }
        if (StringUtils.isNotEmpty(entity.getPoSpecifications())) {
            entity.setPoSpecifications(entity.getPoSpecifications().trim());
        }
    }

    @Override
    protected boolean isNeedCheckFLowOut(FlowPurchaseWashDO entity) {
        return true;
    }

    @Override
    protected boolean isNeedGoodsMatch() {
        return true;
    }

    @Override
    protected boolean isNeedCustomMatch() {
        return false;
    }

    @Override
    protected boolean isNeedSupplierMatch() {
        return true;
    }

    @Override
    protected boolean isNeedOrganizationCersIdMatch() {
        return false;
    }

    @Override
    public void setMappingStatus(FlowPurchaseWashDO flowPurchaseWashDO) {
        if (flowPurchaseWashDO.getCrmGoodsCode() != null && flowPurchaseWashDO.getCrmGoodsCode() > 0) {
            flowPurchaseWashDO.setGoodsMappingStatus(1);
        }
        if (flowPurchaseWashDO.getCrmOrganizationId() != null && flowPurchaseWashDO.getCrmOrganizationId() > 0)  {
            flowPurchaseWashDO.setOrganizationMappingStatus(1);
        }
    }



    @Override
    protected List<FlowPurchaseWashDO> extractFlowDataList(FlowMonthWashTaskDO flowMonthWashTask, FlowMonthWashControlDO flowMonthWashControl) {
        List<FlowPurchaseWashDO> list = new ArrayList<>();

        Integer collectionMethod = flowMonthWashTask.getCollectionMethod();
        if (CollectionMethodEnum.EXCEL.getCode().equals(collectionMethod)) {
            //  excel导入方式
            List<FlowMonthPurchaseDTO> flowMonthPurchaseDTOList = findFlowMonthPurchaseByExcel(flowMonthWashTask.getId());
            for (FlowMonthPurchaseDTO flowMonthPurchaseDTO : flowMonthPurchaseDTOList) {
                FlowPurchaseWashDO flowPurchaseWashDO = PojoUtils.map(flowMonthPurchaseDTO, FlowPurchaseWashDO.class);
                resetFlowPurchaseWash(flowPurchaseWashDO, flowMonthWashTask);
                list.add(flowPurchaseWashDO);
            }

        } else {
            //  获取区间日流向
            Date startTime = flowMonthWashControl.getDataStartTime();
            Date endTime = flowMonthWashControl.getDataEndTime();
            Long eid = flowMonthWashTask.getEid();
            List<FlowPurchaseDO> flowPurchaseDOList = findDayFlowPurchase(eid, startTime, endTime);

            for (FlowPurchaseDO flowPurchaseDO : flowPurchaseDOList) {
                FlowPurchaseWashDO flowPurchaseWashDO = PojoUtils.map(flowPurchaseDO, FlowPurchaseWashDO.class);
                resetFlowPurchaseWash(flowPurchaseWashDO, flowMonthWashTask);
                list.add(flowPurchaseWashDO);
            }
        }
        flowPurchaseWashService.batchInsert(list);
        return flowPurchaseWashService.getByFmwtId(flowMonthWashTask.getId());
    }

    @Override
    public boolean supplierMatch(FlowPurchaseWashDO flowPurchaseWashDO) {
        boolean isSupplierMatch = false;
        //  供应商对照清洗逻辑
        FlowEnterpriseSupplierMappingDTO flowEnterpriseSupplierMappingDTO = flowEnterpriseSupplierMappingService.findBySupplierNameAndCrmEnterpriseId(flowPurchaseWashDO.getEnterpriseName(), flowPurchaseWashDO.getCrmEnterpriseId());
        if (flowEnterpriseSupplierMappingDTO == null || flowEnterpriseSupplierMappingDTO.getCrmOrgId() == 0) {
            // 若匹配不到则先从行业库中匹配，若能完全匹配，则也算对照上
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseService.getCrmEnterpriseCodeByName(flowPurchaseWashDO.getEnterpriseName(), false);
            initOrUpdateFlowEnterpriseSupplierMappingDTO(flowPurchaseWashDO, crmEnterpriseDTO, flowEnterpriseSupplierMappingDTO);
            if (crmEnterpriseDTO != null) {
                flowPurchaseWashDO.setCrmOrganizationId(crmEnterpriseDTO.getId());
                flowPurchaseWashDO.setCrmOrganizationName(crmEnterpriseDTO.getName());
                return true;
            }
            return false;
        }

        if (flowEnterpriseSupplierMappingDTO.getCrmOrgId() > 0) {
            flowPurchaseWashDO.setCrmOrganizationId(flowEnterpriseSupplierMappingDTO.getCrmOrgId());
            flowPurchaseWashDO.setCrmOrganizationName(flowEnterpriseSupplierMappingDTO.getOrgName());
            isSupplierMatch = true;
        }
        return isSupplierMatch;
    }

    private void initOrUpdateFlowEnterpriseSupplierMappingDTO(FlowPurchaseWashDO flowPurchaseWashDO, CrmEnterpriseDTO crmEnterpriseDTO, FlowEnterpriseSupplierMappingDTO flowEnterpriseSupplierMappingDTO) {
        SaveFlowEnterpriseSupplierMappingRequest request = new SaveFlowEnterpriseSupplierMappingRequest();
        if (flowEnterpriseSupplierMappingDTO != null) {     // 更新（只更新没有对照表没有对照的）
            request = PojoUtils.map(flowEnterpriseSupplierMappingDTO, SaveFlowEnterpriseSupplierMappingRequest.class);
        } else {    // 新增
            request.setFlowSupplierName(flowPurchaseWashDO.getEnterpriseName());
            request.setEnterpriseName(flowPurchaseWashDO.getName());    // 这里flowPurchaseWashDO的是供应商名称，name是经销商名称
            request.setCrmEnterpriseId(flowPurchaseWashDO.getCrmEnterpriseId());
            request.setProvinceCode(flowPurchaseWashDO.getProvinceCode());
            request.setProvince(flowPurchaseWashDO.getProvinceName());
        }
        if (crmEnterpriseDTO != null) {
            request.setCrmOrgId(crmEnterpriseDTO.getId());
            request.setOrgName(crmEnterpriseDTO.getName());
            request.setOrgLicenseNumber(crmEnterpriseDTO.getLicenseNumber());
        }
        request.setLastUploadTime(new Date());

        flowEnterpriseSupplierMappingService.saveOrUpdate(request);
    }


    @Override
    protected String duplicateCheckKey(FlowPurchaseWashDO flowPurchaseWashDO) {
        Date poTime = flowPurchaseWashDO.getPoTime();
        String enterpriseName = flowPurchaseWashDO.getEnterpriseName();
        String goodsName = flowPurchaseWashDO.getGoodsName();
        String poSpecifications = flowPurchaseWashDO.getPoSpecifications();
        String poBatchNo = flowPurchaseWashDO.getPoBatchNo();
        BigDecimal poQuantity = flowPurchaseWashDO.getPoQuantity();
        return poTime + "," + enterpriseName + "," + goodsName + "," + poSpecifications + "," + poBatchNo + "," + poQuantity;
    }

    @Override
    protected void updateFlowWashEntity(FlowPurchaseWashDO flowPurchaseWashDO) {
        flowPurchaseWashService.updateById(flowPurchaseWashDO);
    }


    @Override
    protected void setConversionQuantity(FlowPurchaseWashDO flowPurchaseWashDO, Integer convertUnit, BigDecimal convertNumber) {
        try {
            // 设置换算数量
            BigDecimal conversionQuantity;
            if (convertNumber.compareTo(BigDecimal.ZERO) == 0) {
                conversionQuantity = flowPurchaseWashDO.getPoQuantity();
            } else {
                if (convertUnit == 1) {
                    conversionQuantity = flowPurchaseWashDO.getPoQuantity().multiply(convertNumber);
                } else {
                    conversionQuantity = flowPurchaseWashDO.getPoQuantity().divide(convertNumber, 6, RoundingMode.HALF_UP);
                }
            }
            flowPurchaseWashDO.setConversionQuantity(conversionQuantity);
        } catch (Exception e) {
            log.error("采购月流向明细清洗，id={}，换算数量计算失败", flowPurchaseWashDO.getId(), e);
        }

    }

    private List<FlowMonthPurchaseDTO> findFlowMonthPurchaseByExcel(Long fmwtId) {
        List<FlowMonthPurchaseDTO> list = new ArrayList<>();
        QueryFlowMonthPageRequest request = new QueryFlowMonthPageRequest();
        request.setTaskId(fmwtId);

        int current = 1;
        int size = 1000;
        while (true) {
            request.setCurrent(current);
            request.setSize(size);
            Page<FlowMonthPurchaseDTO> page = flowMonthPurchaseService.queryFlowMonthPurchasePage(request);
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

    private List<FlowPurchaseDO> findDayFlowPurchase(Long eid, Date startTime, Date endTime) {
        List<FlowPurchaseDO> list = new ArrayList<>();

        QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();
        request.setEid(eid);
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        int current = 1;
        int size = 1000;
        int pageCount = 0;

        Integer count = flowPurchaseService.getCountByEidAndSoTime(eid, startTime, endTime);
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
            Page<FlowPurchaseDO> resultPage = flowPurchaseService.page(request);
            List<FlowPurchaseDO> flowList = resultPage.getRecords();
            list.addAll(flowList);
            current++;
        }
        return list;
    }

    private void resetFlowPurchaseWash(FlowPurchaseWashDO flowPurchaseWashDO, FlowMonthWashTaskDO flowMonthWashTask) {
        flowPurchaseWashDO.setId(null);
        flowPurchaseWashDO.setFlowKey(FlowDataIdUtils.nextId(flowMonthWashTask.getFlowClassify(), flowMonthWashTask.getFlowType()));
        flowPurchaseWashDO.setCrmEnterpriseId(flowMonthWashTask.getCrmEnterpriseId());
        flowPurchaseWashDO.setFmwtId(flowMonthWashTask.getId());
        flowPurchaseWashDO.setYear(flowMonthWashTask.getYear());
        flowPurchaseWashDO.setMonth(flowMonthWashTask.getMonth());
        flowPurchaseWashDO.setEid(flowMonthWashTask.getEid());
        flowPurchaseWashDO.setName(flowMonthWashTask.getName());

        flowPurchaseWashDO.setCrmGoodsCode(0L);
        flowPurchaseWashDO.setWashStatus(FlowDataWashStatusEnum.NOT_WASH.getCode());
        flowPurchaseWashDO.setCreateUser(0L);
        flowPurchaseWashDO.setUpdateUser(0L);
        flowPurchaseWashDO.setCreateTime(new Date());
        flowPurchaseWashDO.setUpdateTime(new Date());
        flowPurchaseWashDO.setRemark("");
    }
}
