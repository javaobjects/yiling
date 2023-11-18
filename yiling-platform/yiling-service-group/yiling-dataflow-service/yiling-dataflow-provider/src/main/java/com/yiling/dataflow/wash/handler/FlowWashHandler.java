package com.yiling.dataflow.wash.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.crm.enums.CrmGoodsStatusEnum;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseGoodsMappingRequest;
import com.yiling.dataflow.utils.BackupUtil;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupRelationService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupService;
import com.yiling.dataflow.crm.service.CrmGoodsInfoService;
import com.yiling.dataflow.flow.dto.FlowEnterpriseGoodsMappingDTO;
import com.yiling.dataflow.flow.entity.FlowEnterpriseGoodsMappingDO;
import com.yiling.dataflow.flow.service.FlowEnterpriseGoodsMappingService;
import com.yiling.dataflow.wash.bo.BaseFlowDataWashDO;
import com.yiling.dataflow.wash.bo.FlowDataWashEntity;
import com.yiling.dataflow.wash.entity.FlowMonthWashControlDO;
import com.yiling.dataflow.wash.entity.FlowMonthWashTaskDO;
import com.yiling.dataflow.wash.enums.FlowDataWashStatusEnum;
import com.yiling.dataflow.wash.enums.FlowWashTaskStatusEnum;
import com.yiling.dataflow.wash.service.FlowMonthWashControlService;
import com.yiling.dataflow.wash.service.FlowMonthWashTaskService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/3/6
 */
@Slf4j
@Service
public abstract class FlowWashHandler<T extends BaseFlowDataWashDO> {

    @Autowired
    private CrmEnterpriseService crmEnterpriseService;

    @Autowired
    private FlowMonthWashTaskService flowMonthWashTaskService;

    @Autowired
    private FlowMonthWashControlService flowMonthWashControlService;

    @Autowired
    private FlowEnterpriseGoodsMappingService flowEnterpriseGoodsMappingService;

//    @Autowired
//    private CrmGoodsGroupRelationService crmGoodsGroupRelationService;
    @Autowired
    private CrmGoodsGroupService crmGoodsGroupService;

    @Autowired
    protected CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;

    @Autowired
    private CrmGoodsInfoService crmGoodsInfoService;

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;

    public void wash(Long fmwtId) {

        // 获取流向清洗任务
        FlowMonthWashTaskDO flowMonthWashTask = flowMonthWashTaskService.getById(fmwtId);
        if (flowMonthWashTask == null) {
            log.error("月流向清洗，flowMonthWashTaskId={}，不存在", fmwtId);
            return;
        }
        if (!FlowWashTaskStatusEnum.NOT_WASH.getCode().equals(flowMonthWashTask.getWashStatus())) {
            log.error("月流向清洗，flowMonthWashTaskId={}，任务状态不对", fmwtId);
            return;
        }

        // 获取清洗日程
        FlowMonthWashControlDO flowMonthWashControl = flowMonthWashControlService.getById(flowMonthWashTask.getFmwcId());
        if (flowMonthWashControl == null) {
            log.error("月流向清洗，flowMonthWashTaskId={}，flowMonthWashControl不存在", fmwtId);
            return;
        }

        CrmEnterpriseDO crmEnterpriseDO = null;
        try {
            crmEnterpriseDO = crmEnterpriseService
                    .getSuffixByCrmEnterpriseId(flowMonthWashTask.getCrmEnterpriseId(), BackupUtil.generateTableSuffix(flowMonthWashTask.getYear(), flowMonthWashTask.getMonth()));
        } catch (Exception e) {
            log.error("月流向清洗，flowMonthWashTaskId={}，crmEnterprise查询失败，year={}, month={}", fmwtId, flowMonthWashTask.getYear(), flowMonthWashTask.getMonth(), e);
            return;
        }

        if (crmEnterpriseDO == null) {
            log.error("月流向清洗，flowMonthWashTaskId={}，crmEnterprise备份数据不存在，year={}, month={}", fmwtId, flowMonthWashTask.getYear(), flowMonthWashTask.getMonth());
            return;
        }


        flowMonthWashTaskService.updateWashStatusById(fmwtId, FlowWashTaskStatusEnum.WASHING.getCode());

        //  获取该次任务数据
        List<T> flowDataWashEntityList;
        try {
            flowDataWashEntityList = extractFlowDataList(flowMonthWashTask, flowMonthWashControl);
        } catch (Exception e) {
            log.error("流向清洗，抽取流向数据失败，flowMonthWashTaskId={}", fmwtId, e);
            failFlowMonthWashTask(fmwtId);
            return;
        }

        // 记录  销售时间、原始客户名称、原始商品名称、原始商品规格、批号、数量 生成的md5值，用于判断疑似重复数据
        Set<String> md5KeySet = new HashSet<>();
        int successNumber = 0;  // 清洗完成数量
        int failNumber = 0;     // 清洗异常数量
        int unGoodsMappingCount = 0;    // 产品未对照数量
        int unCustomerMappingCount = 0;     // 客户未对照数量
        int unSupplierMappingCount = 0;     // 供应商未对照数量
        int flowOutCount = 0;   // 区间外数量
        int repeatCount = 0;    // 疑似重复数量

//        List<T> autoGenList = new ArrayList<>();
        for (int i = 0; i < flowDataWashEntityList.size(); i++) {
            T entity = flowDataWashEntityList.get(i);
//        }
//        for (T entity : flowDataWashEntityList) {
            // 将crmEnterprise部分信息传入
            trimHandle(entity);
            setCrmEnterpriseParam(entity, crmEnterpriseDO);
            try {
                entity.setWashStatus(FlowDataWashStatusEnum.NORMAL.getCode());
                // 1. 判断区间外数据
                if (isNeedCheckFLowOut(entity)) {
                    boolean isOut = flowOutCheck(flowMonthWashControl, entity);
                    if (isOut) {
                        entity.setWashStatus(FlowDataWashStatusEnum.DELETE.getCode());
                        flowOutCount++;
                        successNumber++;
                        updateFlowWashEntity(entity);
                        continue;
                    }
                }

                // 2. 产品对照
                if (isNeedGoodsMatch()) {
                    boolean isGoodsMatch = goodsMatch(entity);
                    if (!isGoodsMatch) {
                        unGoodsMappingCount++;
                    }
                }

                // 3. 客户对照
                if (isNeedCustomMatch()) {
                    boolean isCustomMatch = customMatch(entity, flowDataWashEntityList);
                    if (!isCustomMatch) {
                        unCustomerMappingCount++;
                    }
                }

                // 4. 供应商对照对照
                if (isNeedSupplierMatch()) {
                    boolean isSupplierMatch = supplierMatch(entity);
                    if (!isSupplierMatch) {
                        unSupplierMappingCount++;
                    }
                }

                // 5. 疑似重复数据检查
                if (isNeedDuplicateCheck(entity)) {
                    boolean isDuplicate = duplicateCheck(md5KeySet, entity);
                    if (isDuplicate) {
                        // 标记疑似重复
                        entity.setWashStatus(FlowDataWashStatusEnum.DUPLICATE.getCode());
                        repeatCount++;
                    }
                }

                //  6. 匹配三者关系
                try {
                    // 匹配经销商三者关系
                    Long enterpriseCersId = crmEnterpriseRelationShipService.listSuffixByOrgIdAndGoodsCode(entity.getCrmGoodsCode(), entity.getCrmEnterpriseId(), BackupUtil.generateTableSuffix(flowMonthWashTask.getYear(), flowMonthWashTask.getMonth()));
                    entity.setEnterpriseCersId(enterpriseCersId);

                    // 匹配客户三者关系
                    if (isNeedOrganizationCersIdMatch()) {
                        organizationCersIdMatch(entity);
                    }
                } catch (Exception e) {
                    log.error("月流向清洗，flowMonthWashTaskId={}，三者关系查询失败，year={}, month={}", fmwtId, flowMonthWashTask.getYear(), flowMonthWashTask.getMonth(), e);
                }

                setMappingStatus(entity);
                updateFlowWashEntity(entity);
                successNumber++;
            } catch (Exception e) {
                log.error("月流向数据清洗异常，id={}", entity.getId(), e);
                entity.setErrorFlag(1);
                entity.setErrorMsg(e.getMessage());
                failNumber++;
            }
        }
        int count = flowDataWashEntityList.size();
        // 清洗完成，更新数量和状态
        finishFlowMonthWashTask(fmwtId, count, unGoodsMappingCount, unCustomerMappingCount, unSupplierMappingCount, flowOutCount, repeatCount, successNumber, failNumber);

        //  清洗完成后通知
        sendMqFinishNotify(fmwtId);
    }

    protected abstract void trimHandle(T entity);


    /**
     * 是否需要进行月流向区间外检查
     * @return
     */
    protected abstract boolean isNeedCheckFLowOut(T entity);

    /**
     * 是否需要产品对照
     * @return
     */
    protected abstract boolean isNeedGoodsMatch();

    /**
     * 是否需要客户对照
     * @return
     */
    protected abstract boolean isNeedCustomMatch();

    /**
     * 是否需要供应商对照
     * @return
     */
    protected abstract boolean isNeedSupplierMatch();

    /**
     * 是否需要疑似重复检查
     * @return
     */
    protected boolean isNeedDuplicateCheck(T entity) {
        return true;
    }

    protected abstract boolean isNeedOrganizationCersIdMatch();

    /**
     * 设置清洗对照结果
     */
    public abstract void setMappingStatus(T entity);

    private void setCrmEnterpriseParam(T entity, CrmEnterpriseDO crmEnterpriseDO) {
        // 设置crmEnterprise参数
        entity.setProvinceCode(crmEnterpriseDO.getProvinceCode());
        entity.setProvinceName(crmEnterpriseDO.getProvinceName());
        entity.setCityCode(crmEnterpriseDO.getCityCode());
        entity.setCityName(crmEnterpriseDO.getCityName());
        entity.setRegionCode(crmEnterpriseDO.getRegionCode());
        entity.setRegionName(crmEnterpriseDO.getRegionName());
    }

    /**
     * 保存清洗数据
     * @param entity
     */
    protected abstract void updateFlowWashEntity(T entity);

    protected abstract List<T> extractFlowDataList(FlowMonthWashTaskDO flowMonthWashTask, FlowMonthWashControlDO flowMonthWashControl);

    protected boolean flowOutCheck(FlowMonthWashControlDO flowMonthWashControl, FlowDataWashEntity entity) {
        boolean isOut = false;
        Date time = entity.getTime();
        Date dataStartTime = flowMonthWashControl.getDataStartTime();
        Date dataEndTime = flowMonthWashControl.getDataEndTime();
        if (time.before(dataStartTime) || time.after(dataEndTime)) {
            isOut = true;
        }
        return isOut;
    }

    protected boolean goodsMatch(T entity) {
        boolean isGoodsMatch = false;
        FlowEnterpriseGoodsMappingDTO flowEnterpriseGoodsMappingDTO = flowEnterpriseGoodsMappingService.findByFlowGoodsNameAndFlowSpecificationAndCrmEnterpriseId(entity.getGoodsName(), entity.getSpecifications(), entity.getCrmEnterpriseId());
        if (flowEnterpriseGoodsMappingDTO == null || flowEnterpriseGoodsMappingDTO.getCrmGoodsCode() == 0) {
            //  自动行业库匹配
            CrmGoodsInfoDTO crmGoodsInfoDTO = crmGoodsInfoService.getByNameAndSpec(entity.getGoodsName(), entity.getSpecifications(), CrmGoodsStatusEnum.NORMAL.getCode());

            // 初始化或更新数据
            initOrUpdateFlowEnterpriseGoodsMapping(entity, crmGoodsInfoDTO, flowEnterpriseGoodsMappingDTO);
            setConversionQuantity(entity, 0, BigDecimal.ZERO);
            if (crmGoodsInfoDTO != null) {
                entity.setCrmGoodsCode(crmGoodsInfoDTO.getGoodsCode());
                entity.setCrmGoodsName(crmGoodsInfoDTO.getGoodsName());
                entity.setCrmGoodsSpecifications(crmGoodsInfoDTO.getGoodsSpec());
                return true;
            }
            return false;
        }

        if (flowEnterpriseGoodsMappingDTO.getCrmGoodsCode() > 0) {
            entity.setCrmGoodsCode(flowEnterpriseGoodsMappingDTO.getCrmGoodsCode());
            entity.setCrmGoodsName(flowEnterpriseGoodsMappingDTO.getGoodsName());
            entity.setCrmGoodsSpecifications(flowEnterpriseGoodsMappingDTO.getGoodsSpecification());
            setConversionQuantity(entity, flowEnterpriseGoodsMappingDTO.getConvertUnit(), flowEnterpriseGoodsMappingDTO.getConvertNumber());
            isGoodsMatch = true;
        }


        return isGoodsMatch;
    }

    private void initOrUpdateFlowEnterpriseGoodsMapping(T entity, CrmGoodsInfoDTO crmGoodsInfoDTO, FlowEnterpriseGoodsMappingDTO flowEnterpriseGoodsMappingDTO) {
        SaveFlowEnterpriseGoodsMappingRequest request = new SaveFlowEnterpriseGoodsMappingRequest();
        if (flowEnterpriseGoodsMappingDTO != null) {    // 更新
            request = PojoUtils.map(flowEnterpriseGoodsMappingDTO, SaveFlowEnterpriseGoodsMappingRequest.class);
        } else {    // 新增
            request.setFlowGoodsName(entity.getGoodsName());
            request.setFlowSpecification(entity.getSpecifications());
            request.setFlowGoodsInSn(entity.getGoodsInSn());
            request.setFlowManufacturer(entity.getManufacturer());
            request.setFlowUnit(entity.getUnit());
            request.setEnterpriseName(entity.getName());
            request.setCrmEnterpriseId(entity.getCrmEnterpriseId());
            request.setProvinceCode(entity.getProvinceCode());
            request.setProvince(entity.getProvinceName());
            request.setConvertUnit(1);
            request.setConvertNumber(new BigDecimal(1));
        }

        if (crmGoodsInfoDTO != null) {
            request.setCrmGoodsCode(crmGoodsInfoDTO.getGoodsCode());
            request.setGoodsName(crmGoodsInfoDTO.getGoodsName());
            request.setGoodsSpecification(crmGoodsInfoDTO.getGoodsSpec());
        }
        request.setLastUploadTime(new Date());
        flowEnterpriseGoodsMappingService.saveOrUpdate(request);
    }

    protected abstract void setConversionQuantity(T entity, Integer convertUnit, BigDecimal convertNumber);

    protected boolean customMatch(T entity, List<T> flowDataWashEntityList) {
        return false;
    }

    protected boolean supplierMatch(T entity) {
        return false;
    }


    protected List<T> hospitalDrugstoreRelHandle(T entity) {
        return null;
    }

    protected void organizationCersIdMatch(T entity) {
    }

    protected boolean duplicateCheck(Set<String> md5KeySet, T entity) {
        boolean isDuplicate = false;
        String key = duplicateCheckKey(entity);
        String md5Key = SecureUtil.md5(key);
        if (md5KeySet.contains(md5Key)) {
            isDuplicate = true;
        }
        md5KeySet.add(md5Key);
        return isDuplicate;
    }

    protected String duplicateCheckKey(T entity) {
        return "";
    }

    private void failFlowMonthWashTask(Long fmwtId) {
        FlowMonthWashTaskDO flowMonthWashTaskDO = new FlowMonthWashTaskDO();
        flowMonthWashTaskDO.setId(fmwtId);
        flowMonthWashTaskDO.setWashStatus(FlowWashTaskStatusEnum.FAIL.getCode());
        flowMonthWashTaskDO.setCompleteTime(new Date());
        flowMonthWashTaskDO.setUpdateTime(new Date());
        flowMonthWashTaskService.updateById(flowMonthWashTaskDO);
    }

    private void finishFlowMonthWashTask(Long fmwtId, int count, int unGoodsMappingCount, int unCustomerMappingCount, int unSupplierMappingCount, int flowOutCount, int repeatCount, int successNumber, int failNumber) {
        FlowMonthWashTaskDO flowMonthWashTaskDO = new FlowMonthWashTaskDO();
        flowMonthWashTaskDO.setId(fmwtId);
        flowMonthWashTaskDO.setCount(count);
        flowMonthWashTaskDO.setUnGoodsMappingCount(unGoodsMappingCount);
        flowMonthWashTaskDO.setUnCustomerMappingCount(unCustomerMappingCount);
        flowMonthWashTaskDO.setUnSupplierMappingCount(unSupplierMappingCount);
        flowMonthWashTaskDO.setFlowOutCount(flowOutCount);
        flowMonthWashTaskDO.setRepeatCount(repeatCount);
        flowMonthWashTaskDO.setSuccessNumber(successNumber);
        flowMonthWashTaskDO.setFailNumber(failNumber);
        flowMonthWashTaskDO.setWashStatus(FlowWashTaskStatusEnum.FINISH_WASH.getCode());
        flowMonthWashTaskDO.setCompleteTime(new Date());
        flowMonthWashTaskDO.setUpdateTime(new Date());
        flowMonthWashTaskService.updateById(flowMonthWashTaskDO);
    }

    private void sendMqFinishNotify(Long fmwtId) {
        SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_WASH_TASK_FINISH_NOTIFY, Constants.TAG_WASH_TASK_FINISH_NOTIFY, DateUtil.formatDate(new Date()), String.valueOf(fmwtId));
        if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
            log.error("月流向数据清洗完成，生成报表消息发送失败，taskId={}", fmwtId);
        }
    }
}
