package com.yiling.dataflow.gb.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.gb.dao.GbOrderMapper;
import com.yiling.dataflow.gb.dto.request.QueryGbOrderPageRequest;
import com.yiling.dataflow.gb.dto.request.SaveOrUpdateGbAppealFlowStatisticRequest;
import com.yiling.dataflow.gb.entity.GbAppealFlowRelatedDO;
import com.yiling.dataflow.gb.entity.GbAppealFormDO;
import com.yiling.dataflow.gb.entity.GbOrderDO;
import com.yiling.dataflow.gb.enums.GbDataExecStatusEnum;
import com.yiling.dataflow.gb.enums.GbExecTypeEnum;
import com.yiling.dataflow.gb.enums.GbOrderExecStatusEnum;
import com.yiling.dataflow.gb.service.GbAppealFlowRelatedService;
import com.yiling.dataflow.gb.service.GbAppealFlowStatisticService;
import com.yiling.dataflow.gb.service.GbAppealFormService;
import com.yiling.dataflow.gb.service.GbOrderService;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.dto.request.FlowWashSaleReportPageRequest;
import com.yiling.dataflow.report.service.FlowWashSaleReportService;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.dataflow.wash.service.FlowMonthWashControlService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 团购主表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-11
 */
@Slf4j
@Service
public class GbOrderServiceImpl extends BaseServiceImpl<GbOrderMapper, GbOrderDO> implements GbOrderService {

    @Autowired
    private GbOrderService               gbOrderService;
    @Lazy
    @Autowired
    private GbAppealFormService          gbAppealFormService;
    @Autowired
    private FlowWashSaleReportService    flowWashSaleReportService;
    @Autowired
    private FlowMonthWashControlService  flowMonthWashControlService;
    @Autowired
    private GbAppealFlowRelatedService   gbAppealFlowRelatedService;
    @Autowired
    private GbAppealFlowStatisticService gbAppealFlowStatisticService;
    @Autowired
    private RocketMqProducerService      rocketMqProducerService;
    @Autowired
    @Lazy
    GbOrderServiceImpl _this;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Override
    public Page<GbOrderDO> getGbOrderPage(QueryGbOrderPageRequest request) {
        QueryWrapper<GbOrderDO> queryWrapper = new QueryWrapper();
        // 是否仅查询主单信息
        Integer mainOrderFlag = request.getMainOrderFlag();
        if (ObjectUtil.isNotNull(mainOrderFlag) && mainOrderFlag.intValue() == 1) {
            queryWrapper.select(" DISTINCT `form_id`, `gb_no`, `gb_process`, `gb_month`, `flow_month`, `seller_emp_id`, `seller_emp_name`, `customer_id`, `customer_name`, `crm_id`, `ename`, `org_crm_id`, `enterprise_name`,  `gb_type`, `audit_status`, `check_status`, `gb_remark`, `create_name`, `create_time`, `exec_status` ");
        }

        // 数据处理:1-未处理；2-已处理
        Integer execStatus = request.getExecStatus();
        if (ObjectUtil.isNotNull(execStatus) && execStatus.intValue() != 0) {
            queryWrapper.lambda().eq(GbOrderDO::getExecStatus, execStatus);
        }
        // 团购表单ID
        Long formId = request.getFormId();
        if (ObjectUtil.isNotNull(formId) && formId.intValue() != 0) {
            queryWrapper.lambda().eq(GbOrderDO::getFormId, formId);
        }
        // 团购编号
        String gbNo = request.getGbNo();
        if (StrUtil.isNotBlank(gbNo)) {
            queryWrapper.lambda().eq(GbOrderDO::getGbNo, gbNo);
        }
        // 出库商业ID
        Long crmId = request.getCrmId();
        if (ObjectUtil.isNotNull(crmId) && crmId.intValue() != 0) {
            queryWrapper.lambda().eq(GbOrderDO::getCrmId, crmId);
        }
        // 出库终端名称
        String enterpriseName = request.getEnterpriseName();
        if (StrUtil.isNotBlank(enterpriseName)) {
            queryWrapper.lambda().eq(GbOrderDO::getEnterpriseName, enterpriseName);
        }
        // 复核状态
        Integer checkStatus = request.getCheckStatus();
        if (ObjectUtil.isNotNull(checkStatus) && checkStatus.intValue() != 0) {
            queryWrapper.lambda().eq(GbOrderDO::getCheckStatus, checkStatus);
        }
        // 提交时间
        Date createTimeStart = request.getCreateTimeStart();
        if (ObjectUtil.isNotNull(createTimeStart)) {
            queryWrapper.lambda().ge(GbOrderDO::getCreateTime, DateUtil.beginOfDay(createTimeStart));
        }
        Date createTimeEnd = request.getCreateTimeEnd();
        if (ObjectUtil.isNotNull(createTimeEnd)) {
            queryWrapper.lambda().le(GbOrderDO::getCreateTime, DateUtil.endOfDay(createTimeEnd));
        }
        // 排序字段
        String orderByDescField = request.getOrderByDescField();
        if (StrUtil.isNotBlank(orderByDescField)) {
            queryWrapper.lambda().last(" order by " + orderByDescField + " desc");
        }

        return this.page(new Page<>(request.getCurrent(), request.getSize()), queryWrapper);
    }

    @Override
    public boolean mateFlow(Long id) {
        GbOrderDO gbOrderDO = gbOrderService.getById(id);
        if (ObjectUtil.isNull(gbOrderDO)) {
            log.error("源流向自动匹配, 此团购数据不存在, gbOrderId:{}", id);
            return false;
        }
        if(GbOrderExecStatusEnum.FINISH.getCode().equals(gbOrderDO.getExecStatus())){
            log.warn("源流向自动匹配, 此团购数据状态是”已处理“, gbOrderId:{}", id);
            return false;
        }

        // 通过主订单获取明细团购处理
        List<GbAppealFormDO> gbAppealFormDOList = gbAppealFormService.getListByGbOrderId(id);
        if (CollUtil.isEmpty(gbAppealFormDOList)) {
            log.error("源流向自动匹配, 此团购处理不存在, gbOrderId:{}", id);
            return false;
        }
        // 取处理状态为“未开始”的
        gbAppealFormDOList = gbAppealFormDOList.stream().filter(o -> GbDataExecStatusEnum.UN_START.getCode().equals(o.getDataExecStatus())).collect(Collectors.toList());
        if (CollUtil.isEmpty(gbAppealFormDOList)) {
            log.warn("源流向自动匹配, 此团购处理状态不是“未开始”, gbOrderId:{}", id);
            return false;
        }
        // 取处理类型为“自动”的
        gbAppealFormDOList = gbAppealFormDOList.stream().filter(o -> GbExecTypeEnum.AUTO.getCode().equals(o.getExecType())).collect(Collectors.toList());
        if (CollUtil.isEmpty(gbAppealFormDOList)) {
            log.warn("源流向自动匹配, 此团购处理类型不是“自动”, gbOrderId:{}", id);
            return false;
        }

        // 校验流向日程锁定团购、非锁团购状态，阶段5进行中~阶段8进行中才能手动处理团购
        FlowMonthWashControlDTO flowMonthWashControlDTO = null;
        try {
            flowMonthWashControlDTO = gbAppealFormService.washControlGbStatusCheck(null);
            if (ObjectUtil.isNull(flowMonthWashControlDTO)) {
                log.warn("源流向自动匹配, 当前没有可以用的团购处理日程");
                return false;
            }
        } catch (Exception e) {
            log.warn("源流向自动匹配, 日常校验失败, exception:{}", e.getMessage());
            return false;
        }

        // 待更新团购处理
        List<GbAppealFormDO> updateGbAppealFormList = new ArrayList<>();
        GbAppealFormDO updateGbAppealForm;
        // 待保存团购处理与源流向关联
        List<GbAppealFlowRelatedDO> list = new ArrayList<>();
        Date date = new Date();

        for (GbAppealFormDO gbAppealFormDO : gbAppealFormDOList) {
            updateGbAppealForm = new GbAppealFormDO();
            updateGbAppealForm.setId(gbAppealFormDO.getId());
            List<FlowWashSaleReportDTO> flowWashSaleReportDTOList = selectFlowWashSaleReport(flowMonthWashControlDTO, gbOrderDO, gbAppealFormDO);
            if (CollUtil.isEmpty(flowWashSaleReportDTOList)) {
//                updateGbAppealForm.setGbLockType(2);
//                updateGbAppealFormList.add(updateGbAppealForm);
                log.warn("源流向自动匹配, 此团购处理未匹配到源流向, gbOrderId:{}, gbAppealFormId:{}", id, gbAppealFormDO.getId());
                continue;
            }
            FlowWashSaleReportDTO flowWashSaleReportDTO = flowWashSaleReportDTOList.get(0);
            if (flowWashSaleReportDTO.getIsLockFlag() == 1) {
                updateGbAppealForm.setGbLockType(1);
            } else {
                updateGbAppealForm.setGbLockType(2);
            }
            updateGbAppealForm.setFlowMatchNumber(Convert.toLong(flowWashSaleReportDTOList.size(), 0L));
            updateGbAppealForm.setMatchMonth(flowMonthWashControlDTO.getYear() + "-" + String.format("%02d", flowMonthWashControlDTO.getMonth()));
            updateGbAppealFormList.add(updateGbAppealForm);

            for (FlowWashSaleReportDTO flowWashSaleReport : flowWashSaleReportDTOList) {
                GbAppealFlowRelatedDO gbAppealFlowRelatedDO = new GbAppealFlowRelatedDO();
                gbAppealFlowRelatedDO.setAppealFormId(gbAppealFormDO.getId());
                gbAppealFlowRelatedDO.setFlowWashId(flowWashSaleReport.getFlowSaleWashId());
                gbAppealFlowRelatedDO.setCreateTime(date);
                gbAppealFlowRelatedDO.setUpdateTime(date);
                list.add(gbAppealFlowRelatedDO);

                SaveOrUpdateGbAppealFlowStatisticRequest request = new SaveOrUpdateGbAppealFlowStatisticRequest();
                request.setFlowWashId(flowWashSaleReport.getFlowSaleWashId());
                request.setSoQuantity(flowWashSaleReport.getFinalQuantity());
                request.setMatchQuantity(BigDecimal.ZERO);
                request.setUnMatchQuantity(flowWashSaleReport.getFinalQuantity());
                request.setOpUserId(0L);
                request.setOpTime(date);
                //流向团购统计表
                gbAppealFlowStatisticService.saveOrUpdateByFlowWashId(request);
            }
        }

        // 更艰辛团购处理
        if (CollUtil.isNotEmpty(updateGbAppealFormList)) {
            gbAppealFormService.updateBatchById(updateGbAppealFormList);
        }

        if (CollUtil.isNotEmpty(list)) {
            //流向团购申诉申请关联流向表
            gbAppealFlowRelatedService.saveBatch(list);

            // 更新团购数据处理状态 已处理
            GbOrderDO gbOrderUpdate = new GbOrderDO();
            gbOrderUpdate.setId(gbOrderDO.getId());
            gbOrderUpdate.setExecStatus(2);
            this.updateById(gbOrderUpdate);

            // 匹配完以后,锁定、非锁进行扣减和增加，发送mq
            if (CollUtil.isNotEmpty(updateGbAppealFormList)) {
                for (GbAppealFormDO gbAppealFormDO : updateGbAppealFormList) {
                    this.sendMq(Constants.TOPIC_FLOW_SALE_GB_SUBSTRACT_TASK, gbAppealFormDO.getGbLockType().toString(), String.valueOf(gbAppealFormDO.getId()));
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<GbOrderDO> getByIdList(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<GbOrderDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(GbOrderDO::getId, idList);
        List<GbOrderDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public List<GbOrderDO> listByFormId(Long formId) {
        Assert.notNull(formId, "参数 formId 不能为空");
        LambdaQueryWrapper<GbOrderDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(GbOrderDO::getFormId, formId);
        List<GbOrderDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public List<GbOrderDO> listByFormIdList(List<Long> formIds) {
        Assert.notEmpty(formIds, "参数 formIds 不能为空");
        LambdaQueryWrapper<GbOrderDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(GbOrderDO::getFormId, formIds);
        List<GbOrderDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    public List<FlowWashSaleReportDTO> selectFlowWashSaleReport(FlowMonthWashControlDTO flowMonthWashControlDTO, GbOrderDO gbOrderDO, GbAppealFormDO gbAppealFormDO) {
        List<FlowWashSaleReportDTO> flowWashSaleReportDTOList = pageFlowWashSaleReport(Convert.toStr(flowMonthWashControlDTO.getYear(), null), Convert.toStr(flowMonthWashControlDTO.getMonth(), null), gbOrderDO.getCrmId(), gbOrderDO.getOrgCrmId(), gbAppealFormDO.getGoodsCode());
        List<FlowWashSaleReportDTO> flowWashSaleReportDTOListByName = pageFlowWashSaleReport(Convert.toStr(flowMonthWashControlDTO.getYear(), null), Convert.toStr(flowMonthWashControlDTO.getMonth(), null), gbOrderDO.getCrmId(), gbOrderDO.getEnterpriseName(), gbAppealFormDO.getGoodsCode());
        Map<Long, FlowWashSaleReportDTO> flowWashSaleReportDTOMap = flowWashSaleReportDTOList.stream().collect(Collectors.toMap(FlowWashSaleReportDTO::getId, Function.identity()));
        for (FlowWashSaleReportDTO flowWashSaleReportDTO : flowWashSaleReportDTOListByName) {
            if (!flowWashSaleReportDTOMap.containsKey(flowWashSaleReportDTO.getId())) {
                flowWashSaleReportDTOMap.put(flowWashSaleReportDTO.getId(), flowWashSaleReportDTO);
            }
        }
        return new ArrayList<>(flowWashSaleReportDTOMap.values().stream().sorted(Comparator.comparing(FlowWashSaleReportDTO::getId)).collect(Collectors.toList()));
    }

    public List<FlowWashSaleReportDTO> pageFlowWashSaleReport(String year, String month, Long crmId, Long customerCrmId, Long goodsCode) {
        List<FlowWashSaleReportDTO> FlowWashSaleReportDTOList = new ArrayList<>();
        FlowWashSaleReportPageRequest reportPageRequest = new FlowWashSaleReportPageRequest();
        reportPageRequest.setYear(year);
        reportPageRequest.setMonth(month);
        reportPageRequest.setCrmId(crmId);
        reportPageRequest.setCustomerCrmId(customerCrmId);
        reportPageRequest.setGoodsCode(goodsCode);
        reportPageRequest.setFlowClassifyList(buildFlowClassifyList());
        Page<FlowWashSaleReportDTO> page = null;
        int current = 1;
        do {
            reportPageRequest.setCurrent(current);
            reportPageRequest.setSize(500);
            page = flowWashSaleReportService.pageList(reportPageRequest);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            FlowWashSaleReportDTOList.addAll(page.getRecords());
            current=current+1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return FlowWashSaleReportDTOList;
    }

    public List<FlowWashSaleReportDTO> pageFlowWashSaleReport(String year, String month, Long crmId, String customerName, Long goodsCode) {
        List<FlowWashSaleReportDTO> FlowWashSaleReportDTOList = new ArrayList<>();
        FlowWashSaleReportPageRequest reportPageRequest = new FlowWashSaleReportPageRequest();
        reportPageRequest.setYear(year);
        reportPageRequest.setMonth(month);
        reportPageRequest.setCrmId(crmId);
        reportPageRequest.setOriginalEnterpriseName(customerName);
        reportPageRequest.setGoodsCode(goodsCode);
        reportPageRequest.setFlowClassifyList(buildFlowClassifyList());
        Page<FlowWashSaleReportDTO> page = null;
        int current = 1;
        do {
            reportPageRequest.setCurrent(current);
            reportPageRequest.setSize(500);
            page = flowWashSaleReportService.pageList(reportPageRequest);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            FlowWashSaleReportDTOList.addAll(page.getRecords());
            current=current+1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return FlowWashSaleReportDTOList;
    }

    private List<Integer> buildFlowClassifyList() {
        List<Integer> list = new ArrayList<>();
        list.add(FlowClassifyEnum.NORMAL.getCode());
        list.add(FlowClassifyEnum.SUPPLY_TRANS_MONTH_FLOW.getCode());
        return list;
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public boolean sendMq(String topic,String topicTag,String msg) {
        MqMessageBO mqMessageBO = _this.sendPrepare(topic,topicTag,msg);
        mqMessageSendApi.send(mqMessageBO);
        return true;
    }

    /**
     * 消息持久化
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public MqMessageBO sendPrepare(String topic,String topicTag,String msg) {
        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        return mqMessageBO;
    }
}
