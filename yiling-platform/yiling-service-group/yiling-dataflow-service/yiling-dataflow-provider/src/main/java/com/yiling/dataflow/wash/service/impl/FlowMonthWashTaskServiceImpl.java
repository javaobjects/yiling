package com.yiling.dataflow.wash.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.dataflow.flow.dto.FlowEnterpriseSupplierMappingDTO;
import com.yiling.dataflow.flow.entity.FlowEnterpriseSupplierMappingDO;
import com.yiling.dataflow.flow.service.FlowEnterpriseSupplierMappingService;
import com.yiling.dataflow.order.dto.request.UpdateSupplierCrmCodeRequest;
import com.yiling.dataflow.utils.BackupUtil;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.crm.service.CrmGoodsInfoService;
import com.yiling.dataflow.flow.dto.FlowEnterpriseCustomerMappingDTO;
import com.yiling.dataflow.flow.dto.FlowEnterpriseGoodsMappingDTO;
import com.yiling.dataflow.flow.entity.FlowEnterpriseCustomerMappingDO;
import com.yiling.dataflow.flow.entity.FlowEnterpriseGoodsMappingDO;
import com.yiling.dataflow.flow.service.FlowEnterpriseCustomerMappingService;
import com.yiling.dataflow.flow.service.FlowEnterpriseGoodsMappingService;
import com.yiling.dataflow.order.dto.FlowGoodsBatchDetailDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseDTO;
import com.yiling.dataflow.order.dto.FlowSaleDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailByGbMonthPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseByPoMonthPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSaleBySoMonthPageRequest;
import com.yiling.dataflow.order.dto.request.UpdateCrmGoodsCodeRequest;
import com.yiling.dataflow.order.dto.request.UpdateEnterpriseCrmCodeRequest;
import com.yiling.dataflow.order.service.FlowGoodsBatchDetailService;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.dataflow.report.dto.request.FlowWashChangeRequest;
import com.yiling.dataflow.report.service.FlowWashInventoryReportService;
import com.yiling.dataflow.report.service.FlowWashSaleReportService;
import com.yiling.dataflow.wash.dao.FlowMonthWashTaskMapper;
import com.yiling.dataflow.wash.dto.FlowGoodsBatchDetailWashDTO;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.FlowPurchaseWashDTO;
import com.yiling.dataflow.wash.dto.FlowSaleWashDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchDetailWashPageRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowMonthWashTaskPageRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowPurchaseWashPageRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowSaleWashPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveFlowMonthWashTaskRequest;
import com.yiling.dataflow.wash.dto.request.UpdateCrmGoodsInfoRequest;
import com.yiling.dataflow.wash.dto.request.UpdateCrmOrganizationInfoRequest;
import com.yiling.dataflow.wash.entity.FlowGoodsBatchDetailWashDO;
import com.yiling.dataflow.wash.entity.FlowMonthWashControlDO;
import com.yiling.dataflow.wash.entity.FlowMonthWashTaskDO;
import com.yiling.dataflow.wash.entity.FlowPurchaseWashDO;
import com.yiling.dataflow.wash.entity.FlowSaleWashDO;
import com.yiling.dataflow.wash.enums.CollectionMethodEnum;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.dataflow.wash.enums.FlowMonthWashControlStatusEnum;
import com.yiling.dataflow.wash.enums.FlowTypeEnum;
import com.yiling.dataflow.wash.enums.FlowWashTaskStatusEnum;
import com.yiling.dataflow.wash.enums.WashErrorEnum;
import com.yiling.dataflow.wash.service.FlowGoodsBatchDetailWashService;
import com.yiling.dataflow.wash.service.FlowMonthWashControlService;
import com.yiling.dataflow.wash.service.FlowMonthWashTaskService;
import com.yiling.dataflow.wash.service.FlowPurchaseWashService;
import com.yiling.dataflow.wash.service.FlowSaleWashService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fucheng.bai
 * @date 2023/3/1
 */
@Slf4j
@Service
public class FlowMonthWashTaskServiceImpl extends BaseServiceImpl<FlowMonthWashTaskMapper, FlowMonthWashTaskDO> implements FlowMonthWashTaskService {

    @Autowired
    private FlowSaleWashService                  flowSaleWashService;
    @Autowired
    private FlowSaleService                      flowSaleService;
    @Autowired
    private FlowPurchaseService                  flowPurchaseService;
    @Autowired
    private FlowPurchaseWashService              flowPurchaseWashService;
    @Autowired
    private FlowGoodsBatchDetailWashService      flowGoodsBatchDetailWashService;
    @Autowired
    private FlowGoodsBatchDetailService          flowGoodsBatchDetailService;
    @Autowired
    private CrmEnterpriseService                 crmEnterpriseService;
    @Autowired
    private FlowMonthWashControlService          flowMonthWashControlService;
    @Autowired
    private FlowWashSaleReportService            FlowWashSaleReportService;
    @Autowired
    private FlowWashInventoryReportService       flowWashInventoryReportService;
    @Autowired(required = false)
    private RocketMqProducerService              rocketMqProducerService;
    @Autowired
    private FlowEnterpriseGoodsMappingService    flowEnterpriseGoodsMappingService;
    @Autowired
    private CrmGoodsInfoService                  crmGoodsInfoService;
    @Autowired
    private FlowMonthWashTaskService             flowMonthWashTaskService;
    @Autowired
    private FlowEnterpriseCustomerMappingService flowEnterpriseCustomerMappingService;
    @Autowired
    private FlowEnterpriseSupplierMappingService flowEnterpriseSupplierMappingService;

    @Override
    public Page<FlowMonthWashTaskDO> listPage(QueryFlowMonthWashTaskPageRequest request) {
        Page<FlowMonthWashTaskDO> page = new Page<>(request.getCurrent(), request.getSize());
        return baseMapper.listPage(request, page);
    }

    @Override
    public int updateWashStatusById(Long id, Integer washStatus) {
        FlowMonthWashTaskDO flowMonthWashTaskDO = new FlowMonthWashTaskDO();
        flowMonthWashTaskDO.setId(id);
        flowMonthWashTaskDO.setWashStatus(washStatus);
        flowMonthWashTaskDO.setUpdateTime(new Date());
        return baseMapper.updateById(flowMonthWashTaskDO);
    }

    @Override
    public int updateReportConsumeStatusById(Long id, Integer reportConsumeStatus) {
        FlowMonthWashTaskDO flowMonthWashTaskDO = new FlowMonthWashTaskDO();
        flowMonthWashTaskDO.setId(id);
        flowMonthWashTaskDO.setReportConsumeStatus(reportConsumeStatus);
        flowMonthWashTaskDO.setUpdateTime(new Date());
        return baseMapper.updateById(flowMonthWashTaskDO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteTaskAndFlowDataById(Long id) {
        FlowMonthWashTaskDO flowMonthWashTaskDO = baseMapper.selectById(id);
        if (flowMonthWashTaskDO == null) {
            // 已结束不可删除
            throw new BusinessException(ResultCode.FAILED, "该流向清洗任务不存在或已删除");
        }

        Integer flowType = flowMonthWashTaskDO.getFlowType();

        // 查看日程状态
        FlowMonthWashControlDO flowMonthWashControlDO = flowMonthWashControlService.getById(flowMonthWashTaskDO.getFmwcId());
        if (flowMonthWashControlDO.getWashStatus()!=2) {
            // 已结束不可删除
            throw new BusinessException(WashErrorEnum.FLOW_WASH_CONTROL_STATUS_FINISH);
        }

        // 删除任务
        flowMonthWashTaskDO.setOpTime(new Date());
        this.deleteByIdWithFill(flowMonthWashTaskDO);

        // 删除月流向数据
        if (FlowTypeEnum.PURCHASE.getCode().equals(flowType)) {
            LambdaQueryWrapper<FlowPurchaseWashDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FlowPurchaseWashDO::getFmwtId, id);

            FlowPurchaseWashDO flowPurchaseWashDO = new FlowPurchaseWashDO();
            flowPurchaseWashDO.setOpTime(new Date());
            flowPurchaseWashService.batchDeleteWithFill(flowPurchaseWashDO, wrapper);

        } else if (FlowTypeEnum.SALE.getCode().equals(flowType)) {
            LambdaQueryWrapper<FlowSaleWashDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FlowSaleWashDO::getFmwtId, id);

            FlowSaleWashDO flowSaleWashDO = new FlowSaleWashDO();
            flowSaleWashDO.setOpTime(new Date());
            flowSaleWashService.batchDeleteWithFill(flowSaleWashDO, wrapper);
            // 删除报表数据
            FlowWashSaleReportService.removeByFmwtId(id);

        } else if (FlowTypeEnum.GOODS_BATCH.getCode().equals(flowType)) {
            LambdaQueryWrapper<FlowGoodsBatchDetailWashDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FlowGoodsBatchDetailWashDO::getFmwtId, id);

            FlowGoodsBatchDetailWashDO flowGoodsBatchDetailWashDO = new FlowGoodsBatchDetailWashDO();
            flowGoodsBatchDetailWashDO.setOpTime(new Date());
            flowGoodsBatchDetailWashService.batchDeleteWithFill(flowGoodsBatchDetailWashDO, wrapper);
            // 删除报表数据
            flowWashInventoryReportService.removeByFmwtId(id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long create(SaveFlowMonthWashTaskRequest request, boolean isSendMq) {
        FlowMonthWashControlDO flowMonthWashControlDO = flowMonthWashControlService.getById(request.getFmwcId());
        CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getSuffixByCrmEnterpriseId(request.getCrmEnterpriseId(), BackupUtil.generateTableSuffix(flowMonthWashControlDO.getYear(), flowMonthWashControlDO.getMonth()));
        if (crmEnterpriseDO == null) {
            throw new BusinessException(WashErrorEnum.FLOW_WASH_TASK_NOT_ENTERPRISE_EXCEPTION);
        }
        FlowMonthWashTaskDO flowMonthWashTaskDO = new FlowMonthWashTaskDO();
        flowMonthWashTaskDO.setFmwcId(request.getFmwcId());
        flowMonthWashTaskDO.setYear(flowMonthWashControlDO.getYear());
        flowMonthWashTaskDO.setMonth(flowMonthWashControlDO.getMonth());
        flowMonthWashTaskDO.setCrmEnterpriseId(request.getCrmEnterpriseId());
        flowMonthWashTaskDO.setProvinceName(crmEnterpriseDO.getProvinceName());
        flowMonthWashTaskDO.setProvinceCode(crmEnterpriseDO.getProvinceCode());
        flowMonthWashTaskDO.setRegionCode(crmEnterpriseDO.getRegionCode());
        flowMonthWashTaskDO.setRegionName(crmEnterpriseDO.getRegionName());
        flowMonthWashTaskDO.setCityName(crmEnterpriseDO.getCityName());
        flowMonthWashTaskDO.setCityCode(crmEnterpriseDO.getCityCode());
        flowMonthWashTaskDO.setName(crmEnterpriseDO.getName());
        flowMonthWashTaskDO.setEid(request.getEid());
        flowMonthWashTaskDO.setCollectionMethod(request.getCollectionMethod());
        flowMonthWashTaskDO.setFlowClassify(request.getFlowClassify());
        flowMonthWashTaskDO.setCollectTime(new Date());
        flowMonthWashTaskDO.setFlowType(request.getFlowType());
        flowMonthWashTaskDO.setAppealType(request.getAppealType());
        flowMonthWashTaskDO.setFileName(request.getFileName());
        flowMonthWashTaskDO.setCount(request.getCount());
        if (request.getFlowClassify().equals(FlowClassifyEnum.NORMAL.getCode()) && Arrays.asList(CollectionMethodEnum.TOOL.getCode(), CollectionMethodEnum.FTP.getCode(), CollectionMethodEnum.THIRD_INTERFACE.getCode(), CollectionMethodEnum.YL_INTERFACE.getCode()).contains(request.getCollectionMethod())) {
            LambdaQueryWrapper<FlowMonthWashTaskDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FlowMonthWashTaskDO::getCrmEnterpriseId, request.getCrmEnterpriseId());
            wrapper.eq(FlowMonthWashTaskDO::getFmwcId, request.getFmwcId());
            wrapper.eq(FlowMonthWashTaskDO::getFlowType, request.getFlowType());
            wrapper.eq(FlowMonthWashTaskDO::getFlowClassify, FlowClassifyEnum.NORMAL.getCode());
            if (this.count(wrapper) > 0) {
                log.error("工具类对接的企业只能生成一个任务{}", request.getCrmEnterpriseId());
                return 0;
            }
        }
        this.save(flowMonthWashTaskDO);
        if (isSendMq) {
            SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_FLOW_WASH_TASK, Constants.TAG_FLOW_WASH_TASK, DateUtil.formatDate(new Date()), String.valueOf(flowMonthWashTaskDO.getId()));
            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                throw new BusinessException(WashErrorEnum.FLOW_WASH_ERROR);
            }
        }
        return flowMonthWashTaskDO.getId();
    }

    @Override
    public List<Long> getCrmEnterIdsByFmwcIdAndClassify(long fmwcId, int flowClassify) {
        return this.baseMapper.getCrmEnterIdsByFmwcIdAndClassify(fmwcId, flowClassify);
    }

    @Override
    public void confirm(Long id) {
        FlowMonthWashTaskDO flowMonthWashTaskDO = baseMapper.selectById(id);
        if (!FlowWashTaskStatusEnum.FINISH_WASH.getCode().equals(flowMonthWashTaskDO.getWashStatus())) {
            throw new BusinessException(WashErrorEnum.FLOW_WASH_CONFIRM_ERROR);
        }
        flowMonthWashTaskDO.setConfirmStatus(1);
        flowMonthWashTaskDO.setUpdateTime(new Date());
        baseMapper.updateById(flowMonthWashTaskDO);
    }

    @Override
    public List<FlowMonthWashTaskDO> getByControlId(Long fmwcId) {
        LambdaQueryWrapper<FlowMonthWashTaskDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowMonthWashTaskDO::getFmwcId, fmwcId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public void reUnGoodsMappingCount(Long id) {
        FlowMonthWashTaskDO flowMonthWashTaskDO = baseMapper.selectById(id);
        if (!FlowWashTaskStatusEnum.FINISH_WASH.getCode().equals(flowMonthWashTaskDO.getWashStatus())) {
            throw new BusinessException(WashErrorEnum.FLOW_WASH_TASK_STATUS_EXCEPTION);
        }

        Integer count = 0;
        if (FlowTypeEnum.PURCHASE.getCode().equals(flowMonthWashTaskDO.getFlowType())) {
            count = flowPurchaseWashService.goodsUnMappingStatusCount(id);
        } else if (FlowTypeEnum.SALE.getCode().equals(flowMonthWashTaskDO.getFlowType())) {
            count = flowSaleWashService.goodsUnMappingStatusCount(id);
        } else if (FlowTypeEnum.GOODS_BATCH.getCode().equals(flowMonthWashTaskDO.getFlowType())) {
            count = flowGoodsBatchDetailWashService.goodsUnMappingStatusCount(id);
        }

        flowMonthWashTaskDO.setUnGoodsMappingCount(count);
        baseMapper.updateById(flowMonthWashTaskDO);
    }

    @Override
    public void reUnCustomerMappingCount(Long id) {
        FlowMonthWashTaskDO flowMonthWashTaskDO = baseMapper.selectById(id);
        if (!FlowWashTaskStatusEnum.FINISH_WASH.getCode().equals(flowMonthWashTaskDO.getWashStatus())) {
            throw new BusinessException(WashErrorEnum.FLOW_WASH_TASK_STATUS_EXCEPTION);
        }

        if (!FlowTypeEnum.SALE.getCode().equals(flowMonthWashTaskDO.getFlowType())) {
            return;
        }
        Integer count = flowSaleWashService.organizationUnMappingStatusCount(id);
        flowMonthWashTaskDO.setUnCustomerMappingCount(count);
        baseMapper.updateById(flowMonthWashTaskDO);
    }

    @Override
    public void reUnSupplierMappingCount(Long id) {
        FlowMonthWashTaskDO flowMonthWashTaskDO = baseMapper.selectById(id);
        if (!FlowWashTaskStatusEnum.FINISH_WASH.getCode().equals(flowMonthWashTaskDO.getWashStatus())) {
            throw new BusinessException(WashErrorEnum.FLOW_WASH_TASK_STATUS_EXCEPTION);
        }

        if (!FlowTypeEnum.PURCHASE.getCode().equals(flowMonthWashTaskDO.getFlowType())) {
            return;
        }
        Integer count = flowPurchaseWashService.organizationUnMappingStatusCount(id);
        flowMonthWashTaskDO.setUnSupplierMappingCount(count);
        baseMapper.updateById(flowMonthWashTaskDO);
    }

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public void updateMonthFlowByGoodsMapping(List<FlowEnterpriseGoodsMappingDTO> flowEnterpriseGoodsMappingDTOList) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlService.getWashStatus();
        //开启状态
        if (flowMonthWashControlDTO != null) {
            List<Long> ids = new ArrayList<>();
            for (FlowEnterpriseGoodsMappingDTO flowEnterpriseGoodsMappingDTO : flowEnterpriseGoodsMappingDTOList) {
                //年月商品名称商品规格 查询接口
                FlowEnterpriseGoodsMappingDO flowEnterpriseGoodsMappingDO = flowEnterpriseGoodsMappingService.getById(flowEnterpriseGoodsMappingDTO.getId());
                Long crmGoodsCode = 0L;
                String goodsName = "";
                String goodsSpecification = "";
                if (flowEnterpriseGoodsMappingDO == null) {
                    continue;
                }

                crmGoodsCode = flowEnterpriseGoodsMappingDO.getCrmGoodsCode();
                if (crmGoodsCode != 0L) {
                    List<CrmGoodsInfoDTO> crmGoodsInfoDTOList = crmGoodsInfoService.findBakByCodeList(Arrays.asList(crmGoodsCode), BackupUtil.generateTableSuffix(flowMonthWashControlDTO.getYear(), flowMonthWashControlDTO.getMonth()));
                    if (CollUtil.isNotEmpty(crmGoodsInfoDTOList)) {
                        CrmGoodsInfoDTO crmGoodsInfoDTO = crmGoodsInfoDTOList.get(0);
                        goodsName = crmGoodsInfoDTO.getGoodsName();
                        goodsSpecification = crmGoodsInfoDTO.getGoodsSpec();
                    }
                }

                QueryFlowSaleWashPageRequest queryFlowSaleWashPageRequest = new QueryFlowSaleWashPageRequest();
                queryFlowSaleWashPageRequest.setYear(flowMonthWashControlDTO.getYear());
                queryFlowSaleWashPageRequest.setMonth(flowMonthWashControlDTO.getMonth());
                queryFlowSaleWashPageRequest.setCrmGoodsCode(flowEnterpriseGoodsMappingDTO.getCrmGoodsCode());
                queryFlowSaleWashPageRequest.setGoodsName(flowEnterpriseGoodsMappingDO.getFlowGoodsName());
                queryFlowSaleWashPageRequest.setGoodsSpec(flowEnterpriseGoodsMappingDO.getFlowSpecification());
                List<FlowSaleWashDTO> flowSaleWashDTOList = pageFlowSaleWash(queryFlowSaleWashPageRequest);
                if (CollUtil.isNotEmpty(flowSaleWashDTOList)) {
                    for (FlowSaleWashDTO flowSaleWashDTO : flowSaleWashDTOList) {
                        flowSaleWashDTO.setCrmGoodsCode(crmGoodsCode);
                        flowSaleWashDTO.setCrmGoodsName(goodsName);
                        flowSaleWashDTO.setCrmGoodsSpecifications(goodsSpecification);
                        UpdateCrmGoodsInfoRequest updateCrmGoodsInfoRequest = PojoUtils.map(flowSaleWashDTO, UpdateCrmGoodsInfoRequest.class);
                        updateCrmGoodsInfoRequest.setConversionQuantity(this.getConversionQuantity(flowSaleWashDTO.getSoQuantity(), flowEnterpriseGoodsMappingDO.getConvertUnit(), flowEnterpriseGoodsMappingDO.getConvertNumber()));
                        flowSaleWashService.updateCrmGoodsInfo(updateCrmGoodsInfoRequest);
                    }
                    ids.addAll(flowSaleWashDTOList.stream().map(e -> e.getFmwtId()).distinct().collect(Collectors.toList()));
                    List<Long> listId = flowSaleWashDTOList.stream().map(e -> e.getId()).collect(Collectors.toList());
                    List<List<Long>> partition = Lists.partition(listId, 200);
                    for (List<Long> listIds : partition) {
                        FlowWashChangeRequest flowWashChangeRequest = new FlowWashChangeRequest();
                        flowWashChangeRequest.setFlowWashIds(listIds);
                        flowWashChangeRequest.setChangeTypeEnum(FlowWashChangeRequest.ChangeTypeEnum.SALE);
                        flowWashChangeRequest.setYear(flowMonthWashControlDTO.getYear());
                        flowWashChangeRequest.setMonth(flowMonthWashControlDTO.getMonth());
                        SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_WASH_TASK_CHANGE_NOTIFY, Constants.TAG_WASH_TASK_CHANGE_NOTIFY, DateUtil.formatDate(new Date()), JSON.toJSONString(flowWashChangeRequest));
                        if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                            throw new BusinessException(WashErrorEnum.FLOW_WASH_ERROR);
                        }
                    }
                }
                QueryFlowPurchaseWashPageRequest queryFlowPurchaseWashPageRequest = new QueryFlowPurchaseWashPageRequest();
                queryFlowPurchaseWashPageRequest.setCrmGoodsCode(flowEnterpriseGoodsMappingDTO.getCrmGoodsCode());
                queryFlowPurchaseWashPageRequest.setYear(flowMonthWashControlDTO.getYear());
                queryFlowPurchaseWashPageRequest.setMonth(flowMonthWashControlDTO.getMonth());
                queryFlowPurchaseWashPageRequest.setGoodsName(flowEnterpriseGoodsMappingDO.getFlowGoodsName());
                queryFlowPurchaseWashPageRequest.setGoodsSpec(flowEnterpriseGoodsMappingDO.getFlowSpecification());
                List<FlowPurchaseWashDTO> flowPurchaseWashDTOList = pageFlowPurchaseWash(queryFlowPurchaseWashPageRequest);
                //todo 占时不发没有采购报表
                if (CollUtil.isNotEmpty(flowPurchaseWashDTOList)) {
                    for (FlowPurchaseWashDTO flowPurchaseWashDTO : flowPurchaseWashDTOList) {
                        flowPurchaseWashDTO.setCrmGoodsCode(crmGoodsCode);
                        flowPurchaseWashDTO.setCrmGoodsName(goodsName);
                        flowPurchaseWashDTO.setCrmGoodsSpecifications(goodsSpecification);
                        UpdateCrmGoodsInfoRequest updateCrmGoodsInfoRequest = PojoUtils.map(flowPurchaseWashDTO, UpdateCrmGoodsInfoRequest.class);
                        updateCrmGoodsInfoRequest.setConversionQuantity(this.getConversionQuantity(flowPurchaseWashDTO.getPoQuantity(), flowEnterpriseGoodsMappingDO.getConvertUnit(), flowEnterpriseGoodsMappingDO.getConvertNumber()));
                        flowPurchaseWashService.updateCrmGoodsInfo(updateCrmGoodsInfoRequest);
                    }
                    ids.addAll(flowPurchaseWashDTOList.stream().map(e -> e.getFmwtId()).distinct().collect(Collectors.toList()));
                }
                QueryFlowGoodsBatchDetailWashPageRequest queryFlowGoodsBatchDetailWashPageRequest = new QueryFlowGoodsBatchDetailWashPageRequest();
                queryFlowGoodsBatchDetailWashPageRequest.setCrmGoodsCode(flowEnterpriseGoodsMappingDTO.getCrmGoodsCode());
                queryFlowGoodsBatchDetailWashPageRequest.setYear(flowMonthWashControlDTO.getYear());
                queryFlowGoodsBatchDetailWashPageRequest.setMonth(flowMonthWashControlDTO.getMonth());
                queryFlowGoodsBatchDetailWashPageRequest.setGoodsName(flowEnterpriseGoodsMappingDO.getFlowGoodsName());
                queryFlowGoodsBatchDetailWashPageRequest.setGoodsSpec(flowEnterpriseGoodsMappingDO.getFlowSpecification());
                List<FlowGoodsBatchDetailWashDTO> flowGoodsBatchDetailWashDTOList = pageFlowGoodsBatchDetailWash(queryFlowGoodsBatchDetailWashPageRequest);
                if (CollUtil.isNotEmpty(flowGoodsBatchDetailWashDTOList)) {
                    for (FlowGoodsBatchDetailWashDTO flowGoodsBatchDetailWashDTO : flowGoodsBatchDetailWashDTOList) {
                        flowGoodsBatchDetailWashDTO.setCrmGoodsCode(crmGoodsCode);
                        flowGoodsBatchDetailWashDTO.setCrmGoodsName(goodsName);
                        flowGoodsBatchDetailWashDTO.setCrmGoodsSpecifications(goodsSpecification);
                        UpdateCrmGoodsInfoRequest updateCrmGoodsInfoRequest = PojoUtils.map(flowGoodsBatchDetailWashDTO, UpdateCrmGoodsInfoRequest.class);
                        updateCrmGoodsInfoRequest.setConversionQuantity(this.getConversionQuantity(flowGoodsBatchDetailWashDTO.getGbNumber(), flowEnterpriseGoodsMappingDO.getConvertUnit(), flowEnterpriseGoodsMappingDO.getConvertNumber()));
                        flowGoodsBatchDetailWashService.updateCrmGoodsInfo(updateCrmGoodsInfoRequest);
                    }
                    ids.addAll(flowGoodsBatchDetailWashDTOList.stream().map(e -> e.getFmwtId()).distinct().collect(Collectors.toList()));
                    List<Long> listId = flowGoodsBatchDetailWashDTOList.stream().map(e -> e.getId()).collect(Collectors.toList());
                    List<List<Long>> partition = Lists.partition(listId, 200);
                    for (List<Long> listIds : partition) {
                        FlowWashChangeRequest flowWashChangeRequest = new FlowWashChangeRequest();
                        flowWashChangeRequest.setFlowWashIds(listIds);
                        flowWashChangeRequest.setChangeTypeEnum(FlowWashChangeRequest.ChangeTypeEnum.INVENTORY);
                        flowWashChangeRequest.setYear(flowMonthWashControlDTO.getYear());
                        flowWashChangeRequest.setMonth(flowMonthWashControlDTO.getMonth());
                        SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_WASH_TASK_CHANGE_NOTIFY, Constants.TAG_WASH_TASK_CHANGE_NOTIFY, DateUtil.formatDate(new Date()), JSON.toJSONString(flowWashChangeRequest));
                        if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                            throw new BusinessException(WashErrorEnum.FLOW_WASH_ERROR);
                        }
                    }
                }
                ids = ids.stream().distinct().collect(Collectors.toList());
                for (Long id : ids) {
                    flowMonthWashTaskService.reUnGoodsMappingCount(id);
                }
            }
        }
    }

    @Override
    public void updateDayFlowByGoodsMapping(List<FlowEnterpriseGoodsMappingDTO> flowEnterpriseGoodsMappingDTOList) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        String soMonth = formatter.format(new Date());
        for (FlowEnterpriseGoodsMappingDTO flowEnterpriseGoodsMappingDTO : flowEnterpriseGoodsMappingDTOList) {
            //年月商品名称商品规格 查询接口
            FlowEnterpriseGoodsMappingDO flowEnterpriseGoodsMappingDO = flowEnterpriseGoodsMappingService.getById(flowEnterpriseGoodsMappingDTO.getId());
            Long crmGoodsCode = 0L;
            if (flowEnterpriseGoodsMappingDO == null) {
                continue;
            }
            crmGoodsCode = flowEnterpriseGoodsMappingDO.getCrmGoodsCode();

            QueryFlowSaleBySoMonthPageRequest queryFlowSaleBySoMonthPageRequest = new QueryFlowSaleBySoMonthPageRequest();
            queryFlowSaleBySoMonthPageRequest.setSoMonth(soMonth);
            queryFlowSaleBySoMonthPageRequest.setCrmGoodsCode(flowEnterpriseGoodsMappingDTO.getCrmGoodsCode());
            queryFlowSaleBySoMonthPageRequest.setGoodsName(flowEnterpriseGoodsMappingDO.getFlowGoodsName());
            queryFlowSaleBySoMonthPageRequest.setSoSpecifications(flowEnterpriseGoodsMappingDO.getFlowSpecification());
            List<FlowSaleDTO> flowSaleDTOList = pageFlowSale(queryFlowSaleBySoMonthPageRequest);
            if (CollUtil.isNotEmpty(flowSaleDTOList)) {
                for (FlowSaleDTO flowSaleDTO : flowSaleDTOList) {
                    flowSaleDTO.setCrmGoodsCode(crmGoodsCode);
                    UpdateCrmGoodsCodeRequest updateCrmGoodsInfoRequest = PojoUtils.map(flowSaleDTO, UpdateCrmGoodsCodeRequest.class);
                    flowSaleService.updateCrmGoodsCode(updateCrmGoodsInfoRequest);
                }
            }

            QueryFlowPurchaseByPoMonthPageRequest queryFlowPurchaseByPoMonthPageRequest = new QueryFlowPurchaseByPoMonthPageRequest();
            queryFlowPurchaseByPoMonthPageRequest.setCrmGoodsCode(flowEnterpriseGoodsMappingDTO.getCrmGoodsCode());
            queryFlowPurchaseByPoMonthPageRequest.setPoMonth(soMonth);
            queryFlowPurchaseByPoMonthPageRequest.setGoodsName(flowEnterpriseGoodsMappingDO.getFlowGoodsName());
            queryFlowPurchaseByPoMonthPageRequest.setPoSpecifications(flowEnterpriseGoodsMappingDO.getFlowSpecification());
            List<FlowPurchaseDTO> flowPurchaseDTOList = pageFlowPurchase(queryFlowPurchaseByPoMonthPageRequest);
            if (CollUtil.isNotEmpty(flowPurchaseDTOList)) {
                for (FlowPurchaseDTO flowPurchaseDTO : flowPurchaseDTOList) {
                    flowPurchaseDTO.setCrmGoodsCode(crmGoodsCode);
                    UpdateCrmGoodsCodeRequest updateCrmGoodsCodeRequest = PojoUtils.map(flowPurchaseDTO, UpdateCrmGoodsCodeRequest.class);
                    flowPurchaseService.updateCrmGoodsCode(updateCrmGoodsCodeRequest);
                }
            }

            QueryFlowGoodsBatchDetailByGbMonthPageRequest queryFlowGoodsBatchDetailByGbMonthPageRequest = new QueryFlowGoodsBatchDetailByGbMonthPageRequest();
            queryFlowGoodsBatchDetailByGbMonthPageRequest.setCrmGoodsCode(flowEnterpriseGoodsMappingDTO.getCrmGoodsCode());
            queryFlowGoodsBatchDetailByGbMonthPageRequest.setGbName(flowEnterpriseGoodsMappingDO.getFlowGoodsName());
            queryFlowGoodsBatchDetailByGbMonthPageRequest.setGbSpecifications(flowEnterpriseGoodsMappingDO.getFlowSpecification());
            List<FlowGoodsBatchDetailDTO> flowGoodsBatchDetailDTOList = pageFlowGoodsBatchDetail(queryFlowGoodsBatchDetailByGbMonthPageRequest);
            if (CollUtil.isNotEmpty(flowGoodsBatchDetailDTOList)) {
                for (FlowGoodsBatchDetailDTO flowGoodsBatchDetailDTO : flowGoodsBatchDetailDTOList) {
                    flowGoodsBatchDetailDTO.setCrmGoodsCode(crmGoodsCode);
                    UpdateCrmGoodsCodeRequest updateCrmGoodsCodeRequest = PojoUtils.map(flowGoodsBatchDetailDTO, UpdateCrmGoodsCodeRequest.class);
                    flowGoodsBatchDetailService.updateCrmGoodsCode(updateCrmGoodsCodeRequest);
                }
            }
        }
    }

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public void updateMonthFlowByCustomerMapping(List<FlowEnterpriseCustomerMappingDTO> flowEnterpriseCustomerMappingDTOList) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlService.getWashStatus();
        //开启状态
        if (flowMonthWashControlDTO != null) {
            List<Long> ids = new ArrayList<>();
            for (FlowEnterpriseCustomerMappingDTO flowEnterpriseCustomerMappingDTO : flowEnterpriseCustomerMappingDTOList) {
                //年月商品名称商品规格 查询接口
                FlowEnterpriseCustomerMappingDO flowEnterpriseCustomerMappingDO = flowEnterpriseCustomerMappingService.getById(flowEnterpriseCustomerMappingDTO.getId());
                Long crmOrgId = 0L;
                String name = "";
                if (flowEnterpriseCustomerMappingDO != null) {
                    crmOrgId = flowEnterpriseCustomerMappingDO.getCrmOrgId();
                    if (crmOrgId != 0L) {
                        CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getSuffixByCrmEnterpriseId(crmOrgId, BackupUtil.generateTableSuffix(flowMonthWashControlDTO.getYear(), flowMonthWashControlDTO.getMonth()));
                        if (crmEnterpriseDO != null) {
                            name = crmEnterpriseDO.getName();
                        }
                    }
                }
                QueryFlowSaleWashPageRequest queryFlowSaleWashPageRequest = new QueryFlowSaleWashPageRequest();
                queryFlowSaleWashPageRequest.setYear(flowMonthWashControlDTO.getYear());
                queryFlowSaleWashPageRequest.setMonth(flowMonthWashControlDTO.getMonth());
                queryFlowSaleWashPageRequest.setCrmOrganizationId(flowEnterpriseCustomerMappingDTO.getCrmOrgId());
                queryFlowSaleWashPageRequest.setEnterpriseName(flowEnterpriseCustomerMappingDO.getFlowCustomerName());
                List<FlowSaleWashDTO> flowSaleWashDTOList = pageFlowSaleWash(queryFlowSaleWashPageRequest);
                if (CollUtil.isNotEmpty(flowSaleWashDTOList)) {
                    for (FlowSaleWashDTO flowSaleWashDTO : flowSaleWashDTOList) {
                        flowSaleWashDTO.setCrmOrganizationId(crmOrgId);
                        flowSaleWashDTO.setCrmOrganizationName(name);
                        flowSaleWashService.updateCrmOrganizationInfo(PojoUtils.map(flowSaleWashDTO, UpdateCrmOrganizationInfoRequest.class));
                    }
                    ids.addAll(flowSaleWashDTOList.stream().map(e -> e.getFmwtId()).distinct().collect(Collectors.toList()));
                    List<Long> listId = flowSaleWashDTOList.stream().map(e -> e.getId()).collect(Collectors.toList());
                    List<List<Long>> partition = Lists.partition(listId, 200);
                    for (List<Long> washIds : partition) {
                        FlowWashChangeRequest flowWashChangeRequest = new FlowWashChangeRequest();
                        flowWashChangeRequest.setFlowWashIds(washIds);
                        flowWashChangeRequest.setChangeTypeEnum(FlowWashChangeRequest.ChangeTypeEnum.SALE);
                        flowWashChangeRequest.setYear(flowMonthWashControlDTO.getYear());
                        flowWashChangeRequest.setMonth(flowMonthWashControlDTO.getMonth());
                        SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_WASH_TASK_CHANGE_NOTIFY, Constants.TAG_WASH_TASK_CHANGE_NOTIFY, DateUtil.formatDate(new Date()), JSON.toJSONString(flowWashChangeRequest));
                        if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                            throw new BusinessException(WashErrorEnum.FLOW_WASH_ERROR);
                        }
                    }
                }
                for (Long id : ids) {
                    flowMonthWashTaskService.reUnCustomerMappingCount(id);
                }
            }
        }
    }

    @Override
    public void updateDayFlowByCustomerMapping(List<FlowEnterpriseCustomerMappingDTO> flowEnterpriseCustomerMappingDTOList) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        String soMonth = formatter.format(new Date());
        for (FlowEnterpriseCustomerMappingDTO flowEnterpriseCustomerMappingDTO : flowEnterpriseCustomerMappingDTOList) {
            //年月商品名称商品规格 查询接口
            FlowEnterpriseCustomerMappingDO flowEnterpriseCustomerMappingDO = flowEnterpriseCustomerMappingService.getById(flowEnterpriseCustomerMappingDTO.getId());
            if (flowEnterpriseCustomerMappingDO == null) {
                continue;
            }
            Long crmOrgId = flowEnterpriseCustomerMappingDO.getCrmOrgId();
            String crmOrgStr = "";
            if (crmOrgId != 0) {
                crmOrgStr = String.valueOf(crmOrgId);
            }
            QueryFlowSaleBySoMonthPageRequest queryFlowSaleBySoMonthPageRequest = new QueryFlowSaleBySoMonthPageRequest();
            queryFlowSaleBySoMonthPageRequest.setSoMonth(soMonth);
            queryFlowSaleBySoMonthPageRequest.setEnterpriseCrmCode(flowEnterpriseCustomerMappingDTO.getCrmOrgId()==0?"":String.valueOf(flowEnterpriseCustomerMappingDTO.getCrmOrgId()));
            queryFlowSaleBySoMonthPageRequest.setEnterpriseName(flowEnterpriseCustomerMappingDO.getFlowCustomerName());
            List<FlowSaleDTO> flowSaleDTOList = pageFlowSale(queryFlowSaleBySoMonthPageRequest);
            if (CollUtil.isNotEmpty(flowSaleDTOList)) {
                for (FlowSaleDTO flowSaleDTO : flowSaleDTOList) {
                    flowSaleDTO.setEnterpriseCrmCode(crmOrgStr);
                    flowSaleService.updateEnterpriseCrmCodeCode(PojoUtils.map(flowSaleDTO, UpdateEnterpriseCrmCodeRequest.class));
                }
            }
        }
    }

    @Override
    public void updateMonthFlowBySupplierMapping(List<FlowEnterpriseSupplierMappingDTO> flowEnterpriseSupplierMappingDTOList) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlService.getWashStatus();
        //开启状态
        if (flowMonthWashControlDTO != null) {
            List<Long> ids = new ArrayList<>();
            for (FlowEnterpriseSupplierMappingDTO flowEnterpriseSupplierMappingDTO : flowEnterpriseSupplierMappingDTOList) {
                //年月商品名称商品规格 查询接口
                FlowEnterpriseSupplierMappingDO flowEnterpriseSupplierMappingDO = flowEnterpriseSupplierMappingService.getById(flowEnterpriseSupplierMappingDTO.getId());
                Long crmOrgId = 0L;
                String name = "";
                if (flowEnterpriseSupplierMappingDO != null) {
                    crmOrgId = flowEnterpriseSupplierMappingDO.getCrmOrgId();
                    if (crmOrgId != 0L) {
                        CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getSuffixByCrmEnterpriseId(crmOrgId, BackupUtil.generateTableSuffix(flowMonthWashControlDTO.getYear(), flowMonthWashControlDTO.getMonth()));
                        if (crmEnterpriseDO != null) {
                            name = crmEnterpriseDO.getName();
                        }
                    }
                }
                QueryFlowPurchaseWashPageRequest queryFlowPurchaseWashPageRequest = new QueryFlowPurchaseWashPageRequest();
                queryFlowPurchaseWashPageRequest.setYear(flowMonthWashControlDTO.getYear());
                queryFlowPurchaseWashPageRequest.setMonth(flowMonthWashControlDTO.getMonth());
                queryFlowPurchaseWashPageRequest.setCrmOrganizationId(flowEnterpriseSupplierMappingDTO.getCrmOrgId());
                queryFlowPurchaseWashPageRequest.setEnterpriseName(flowEnterpriseSupplierMappingDTO.getFlowSupplierName());
                List<FlowPurchaseWashDTO> flowSaleWashDTOList = pageFlowPurchaseWash(queryFlowPurchaseWashPageRequest);
                if (CollUtil.isNotEmpty(flowSaleWashDTOList)) {
                    for (FlowPurchaseWashDTO flowPurchaseWashDTO : flowSaleWashDTOList) {
                        flowPurchaseWashDTO.setCrmOrganizationId(crmOrgId);
                        flowPurchaseWashDTO.setCrmOrganizationName(name);
                        flowPurchaseWashService.updateCrmOrganizationInfo(PojoUtils.map(flowPurchaseWashDTO, UpdateCrmOrganizationInfoRequest.class));
                    }
                    ids.addAll(flowSaleWashDTOList.stream().map(e -> e.getFmwtId()).distinct().collect(Collectors.toList()));
                }
                for (Long id : ids) {
                    flowMonthWashTaskService.reUnSupplierMappingCount(id);
                }
            }
        }
    }

    @Override
    public void updateDayFlowBySupplierMapping(List<FlowEnterpriseSupplierMappingDTO> flowEnterpriseSupplierMappingDTOList) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        String soMonth = formatter.format(new Date());
        for (FlowEnterpriseSupplierMappingDTO flowEnterpriseSupplierMappingDTO : flowEnterpriseSupplierMappingDTOList) {
            //年月商品名称商品规格 查询接口
            FlowEnterpriseSupplierMappingDO flowEnterpriseSupplierMappingDO = flowEnterpriseSupplierMappingService.getById(flowEnterpriseSupplierMappingDTO.getId());
            if (flowEnterpriseSupplierMappingDO == null) {
                continue;
            }
            Long crmOrgId = flowEnterpriseSupplierMappingDO.getCrmOrgId();
            QueryFlowPurchaseByPoMonthPageRequest queryFlowPurchaseByPoMonthPageRequest = new QueryFlowPurchaseByPoMonthPageRequest();
            queryFlowPurchaseByPoMonthPageRequest.setPoMonth(soMonth);
            queryFlowPurchaseByPoMonthPageRequest.setSupplierId(flowEnterpriseSupplierMappingDTO.getCrmOrgId());
            queryFlowPurchaseByPoMonthPageRequest.setEnterpriseName(flowEnterpriseSupplierMappingDTO.getFlowSupplierName());
            List<FlowPurchaseDTO> flowPurchaseList = pageFlowPurchase(queryFlowPurchaseByPoMonthPageRequest);
            if (CollUtil.isNotEmpty(flowPurchaseList)) {
                for (FlowPurchaseDTO flowPurchaseDTO : flowPurchaseList) {
                    flowPurchaseDTO.setSupplierId(crmOrgId);
                    flowPurchaseService.updateSupplierCrmCodeCode(PojoUtils.map(flowPurchaseDTO, UpdateSupplierCrmCodeRequest.class));
                }
            }
        }
    }

    public BigDecimal getConversionQuantity(BigDecimal quantity, Integer convertUnit, BigDecimal convertNumber) {
        // 设置换算数量
        BigDecimal conversionQuantity;
        if (convertUnit == 1) {
            conversionQuantity = quantity.multiply(convertNumber);
        } else {
            conversionQuantity = quantity.divide(convertNumber, 6, RoundingMode.HALF_UP);
        }
        return conversionQuantity;
    }

    /**
     * 分页获取销售月流向
     *
     * @param queryFlowSaleWashPageRequest
     * @return
     */
    public List<FlowSaleWashDTO> pageFlowSaleWash(QueryFlowSaleWashPageRequest queryFlowSaleWashPageRequest) {
        //查询所有erp对接的企业信息
        List<FlowSaleWashDTO> list = new ArrayList<>();
        //需要循环调用
        Page<FlowSaleWashDTO> page = null;
        int current = 1;
        do {
            queryFlowSaleWashPageRequest.setCurrent(current);
            queryFlowSaleWashPageRequest.setSize(2000);
            page = flowSaleWashService.getPageByYearMonth(queryFlowSaleWashPageRequest);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            list.addAll(page.getRecords());
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return list;
    }

    /**
     * 分页获取销售日流向
     *
     * @param queryFlowSaleBySoMonthPageRequest
     * @return
     */
    public List<FlowSaleDTO> pageFlowSale(QueryFlowSaleBySoMonthPageRequest queryFlowSaleBySoMonthPageRequest) {
        //查询所有erp对接的企业信息
        List<FlowSaleDTO> list = new ArrayList<>();
        //需要循环调用
        Page<FlowSaleDTO> page = null;
        int current = 1;
        do {
            queryFlowSaleBySoMonthPageRequest.setCurrent(current);
            queryFlowSaleBySoMonthPageRequest.setSize(2000);
            page = flowSaleService.getPageBySoMonth(queryFlowSaleBySoMonthPageRequest);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            list.addAll(page.getRecords());
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return list;
    }

    /**
     * 分页获取采购信息
     *
     * @param queryFlowPurchaseWashPageRequest
     * @return
     */
    public List<FlowPurchaseWashDTO> pageFlowPurchaseWash(QueryFlowPurchaseWashPageRequest queryFlowPurchaseWashPageRequest) {
        //查询所有erp对接的企业信息
        List<FlowPurchaseWashDTO> list = new ArrayList<>();
        //需要循环调用
        Page<FlowPurchaseWashDTO> page = null;
        int current = 1;
        do {
            queryFlowPurchaseWashPageRequest.setCurrent(current);
            queryFlowPurchaseWashPageRequest.setSize(2000);
            page = flowPurchaseWashService.getPageByYearMonth(queryFlowPurchaseWashPageRequest);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            list.addAll(page.getRecords());
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return list;
    }

    /**
     * 分页获取采购信息
     *
     * @param queryFlowPurchaseByPoMonthPageRequest
     * @return
     */
    public List<FlowPurchaseDTO> pageFlowPurchase(QueryFlowPurchaseByPoMonthPageRequest queryFlowPurchaseByPoMonthPageRequest) {
        //查询所有erp对接的企业信息
        List<FlowPurchaseDTO> list = new ArrayList<>();
        //需要循环调用
        Page<FlowPurchaseDTO> page = null;
        int current = 1;
        do {
            queryFlowPurchaseByPoMonthPageRequest.setCurrent(current);
            queryFlowPurchaseByPoMonthPageRequest.setSize(2000);
            page = flowPurchaseService.getPageByPoMonth(queryFlowPurchaseByPoMonthPageRequest);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            list.addAll(page.getRecords());
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return list;
    }


    /**
     * 分页获取采购信息
     *
     * @param queryFlowGoodsBatchDetailWashPageRequest
     * @return
     */
    public List<FlowGoodsBatchDetailWashDTO> pageFlowGoodsBatchDetailWash(QueryFlowGoodsBatchDetailWashPageRequest queryFlowGoodsBatchDetailWashPageRequest) {
        //查询所有erp对接的企业信息
        List<FlowGoodsBatchDetailWashDTO> list = new ArrayList<>();
        //需要循环调用
        Page<FlowGoodsBatchDetailWashDTO> page = null;
        int current = 1;
        do {
            queryFlowGoodsBatchDetailWashPageRequest.setCurrent(current);
            queryFlowGoodsBatchDetailWashPageRequest.setSize(2000);
            page = flowGoodsBatchDetailWashService.getPageByYearMonth(queryFlowGoodsBatchDetailWashPageRequest);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            list.addAll(page.getRecords());
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return list;
    }

    /**
     * 分页获取采购信息
     *
     * @param queryFlowGoodsBatchDetailByGbMonthPageRequest
     * @return
     */
    public List<FlowGoodsBatchDetailDTO> pageFlowGoodsBatchDetail(QueryFlowGoodsBatchDetailByGbMonthPageRequest queryFlowGoodsBatchDetailByGbMonthPageRequest) {
        //查询所有erp对接的企业信息
        List<FlowGoodsBatchDetailDTO> list = new ArrayList<>();
        //需要循环调用
        Page<FlowGoodsBatchDetailDTO> page = null;
        int current = 1;
        do {
            queryFlowGoodsBatchDetailByGbMonthPageRequest.setCurrent(current);
            queryFlowGoodsBatchDetailByGbMonthPageRequest.setSize(2000);
            page = flowGoodsBatchDetailService.getPageByGbMonth(queryFlowGoodsBatchDetailByGbMonthPageRequest);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            list.addAll(page.getRecords());
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return list;
    }
}
