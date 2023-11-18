package com.yiling.sjms.flee.service.impl;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.flowcollect.api.SalesAppealConfirmApi;
import com.yiling.dataflow.flowcollect.dto.request.SaveSaleAppealFlowRelateRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveSalesAppealDateRequest;
import com.yiling.dataflow.flowcollect.enums.TransferTypeEnum;
import com.yiling.dataflow.report.api.FlowWashSaleReportApi;
import com.yiling.dataflow.report.dto.request.CreateFlowWashSaleReportRequest;
import com.yiling.dataflow.utils.FlowDataIdUtils;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.api.FlowMonthWashTaskApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.request.SaveFlowMonthWashTaskRequest;
import com.yiling.dataflow.wash.enums.CollectionMethodEnum;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.dataflow.wash.enums.FlowTypeEnum;
import com.yiling.dataflow.wash.enums.SaleAppealChangeTypeEnum;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.constant.ApproveConstant;
import com.yiling.sjms.flee.bo.SalesAppealFormBO;
import com.yiling.sjms.flee.dao.SalesAppealFormMapper;
import com.yiling.sjms.flee.dto.SalesAppealExtFormDTO;
import com.yiling.sjms.flee.dto.request.CreateSalesAppealFlowRequest;
import com.yiling.sjms.flee.dto.request.QuerySalesAppealPageRequest;
import com.yiling.sjms.flee.dto.request.RemoveFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.RemoveSelectAppealFlowFormRequest;
import com.yiling.sjms.flee.dto.request.SaveSalesAppealFormDetailRequest;
import com.yiling.sjms.flee.dto.request.SaveSalesAppealFormRequest;
import com.yiling.sjms.flee.dto.request.SaveSalesAppealSubmitFormRequest;
import com.yiling.sjms.flee.dto.request.SelectAppealFlowFormRequest;
import com.yiling.sjms.flee.dto.request.SubmitFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.UpdateSalesAppealFormRequest;
import com.yiling.sjms.flee.dto.request.UpdateSalesAppealRequest;
import com.yiling.sjms.flee.entity.SaleAppealSelectFlowFormDO;
import com.yiling.sjms.flee.entity.SalesAppealExtFormDO;
import com.yiling.sjms.flee.entity.SalesAppealFormDO;
import com.yiling.sjms.flee.enums.FleeingConfirmStatusEnum;
import com.yiling.sjms.flee.service.SaleAppealSelectFlowFormService;
import com.yiling.sjms.flee.service.SalesAppealExtFormService;
import com.yiling.sjms.flee.service.SalesAppealFormService;
import com.yiling.sjms.form.FleeFormErrorCode;
import com.yiling.sjms.form.dto.request.CreateFormRequest;
import com.yiling.sjms.form.entity.FormDO;
import com.yiling.sjms.form.enums.FormNoEnum;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.form.service.FormService;
import com.yiling.sjms.gb.service.NoService;
import com.yiling.sjms.util.DateUtilsCalculation;
import com.yiling.sjms.util.OaTodoUtils;
import com.yiling.sjms.workflow.WorkFlowService;
import com.yiling.sjms.workflow.dto.request.WorkFlowBaseRequest;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 销量申诉表单 服务实现类
 * </p>
 *
 * @author shixing.sun
 * @date 2023-03-14
 */
@Service
@RefreshScope
@Slf4j
public class SalesAppealFormServiceImpl extends BaseServiceImpl<SalesAppealFormMapper, SalesAppealFormDO> implements SalesAppealFormService {
    @Autowired
    private FormService               formService;
    @Autowired
    private SalesAppealExtFormService salesAppealExtFormService;
    @Autowired
    private NoService                 noService;
    @DubboReference
    private FlowMonthWashControlApi   flowMonthWashControlApi;
    @DubboReference
    private CrmEnterpriseApi          crmEnterpriseApi;
    @DubboReference
    FlowMonthWashTaskApi  flowMonthWashTaskApi;
    @DubboReference
    MqMessageSendApi      mqMessageSendApi;
    @DubboReference
    SalesAppealConfirmApi salesAppealConfirmApi;
    @DubboReference(timeout = 10 * 1000)
    EsbEmployeeApi        esbEmployeeApi;
    @Autowired
    private WorkFlowService workFlowService;



    @Value("#{${common.gb.process-parameter}}")
    Map<String, String> process;
    @Autowired
    SaleAppealSelectFlowFormService saleAppealSelectFlowFormService;

    @DubboReference
    FlowWashSaleReportApi flowWashSaleReportApi;


    @Override
    public Page<SalesAppealFormBO> pageForm(QuerySalesAppealPageRequest request) {
        return this.getBaseMapper().pageForm(new Page<>(request.getCurrent(), request.getSize()), request);
    }

    @Override
    public Long saveUpload(SaveSalesAppealFormRequest request) {
        if (CollectionUtil.isEmpty(request.getSaveSalesAppealDetailForms())) {
            return null;
        }
        if (Objects.isNull(request.getFormId()) || 0L == request.getFormId()) {
            CreateFormRequest createFormRequest = new CreateFormRequest();
            createFormRequest.setOpUserId(request.getOpUserId());
            createFormRequest.setOpTime(request.getOpTime());
            createFormRequest.setCode(noService.genNo(FormNoEnum.SALES_APPEAL));
            createFormRequest.setType(FormTypeEnum.SALES_APPEAL.getCode());
            createFormRequest.setName(OaTodoUtils.genTitle(FormTypeEnum.SALES_APPEAL.getName(), request.getEmpName(), createFormRequest.getCode(), new Date()));
            Long formId = formService.create(createFormRequest);
            request.setFormId(formId);

            SalesAppealExtFormDO goodsFormExtDO = PojoUtils.map(request, SalesAppealExtFormDO.class);
            goodsFormExtDO.setFormId(formId);
            goodsFormExtDO.setConfirmStatus(FleeingConfirmStatusEnum.TO_BE_SUBMIT.getCode());
            salesAppealExtFormService.save(goodsFormExtDO);
        }
        List<SalesAppealFormDO> objects = new ArrayList<>();
        request.getSaveSalesAppealDetailForms().forEach(item -> {
            SalesAppealFormDO salesAppealFormDO = PojoUtils.map(item, SalesAppealFormDO.class);
            salesAppealFormDO.setSourceUrl(item.getKey());
            salesAppealFormDO.setFormId(request.getFormId());
            salesAppealFormDO.setExcelName(item.getName());
            salesAppealFormDO.setUploader(request.getEmpId());
            salesAppealFormDO.setUploaderName(request.getEmpName());
            salesAppealFormDO.setCheckStatus(1);
            salesAppealFormDO.setImportStatus(1);
            salesAppealFormDO.setType(1);
            salesAppealFormDO.setUploadTime(request.getOpTime());

            if (StringUtils.isNotEmpty(item.getName()) && item.getName().toLowerCase().startsWith("sm")) {
                salesAppealFormDO.setDataType(2);
            }
            if (StringUtils.isNotEmpty(item.getName()) && item.getName().toLowerCase().startsWith("pm")) {
                salesAppealFormDO.setDataType(1);
            }
            if (StringUtils.isNotEmpty(item.getName()) && item.getName().toLowerCase().startsWith("im")) {
                salesAppealFormDO.setDataType(3);
            }
            objects.add(salesAppealFormDO);
            // 生成待提交电子流
        });
        this.saveBatch(objects);
        return request.getFormId();
    }

    @Override
    public boolean removeById(RemoveFleeingGoodsFormRequest request) {
        if (Objects.isNull(request.getId())) {
            return false;
        }
        SalesAppealFormDO fleeingGoodsFormDO = PojoUtils.map(request, SalesAppealFormDO.class);
        return this.deleteByIdWithFill(fleeingGoodsFormDO) > 0;
    }

    @Override
    public List<SalesAppealFormDO> ListByFormId(Long formId, Integer type) {
        LambdaQueryWrapper<SalesAppealFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SalesAppealFormDO::getType, type);
        wrapper.eq(SalesAppealFormDO::getFormId, formId).orderByDesc(SalesAppealFormDO::getId);
        return this.list(wrapper);
    }

    @Override
    public Long saveDraft(SaveSalesAppealSubmitFormRequest request) {
        if (Objects.isNull(request.getFormId()) || 0L == request.getFormId()) {
            CreateFormRequest createFormRequest = new CreateFormRequest();
            createFormRequest.setOpUserId(request.getOpUserId());
            createFormRequest.setOpTime(request.getOpTime());
            createFormRequest.setCode(noService.genNo(FormNoEnum.SALES_APPEAL));
            createFormRequest.setType(FormTypeEnum.SALES_APPEAL.getCode());
            createFormRequest.setName(OaTodoUtils.genTitle(FormTypeEnum.SALES_APPEAL.getName(), request.getEmpName(), createFormRequest.getCode(), new Date()));
            Long formId = formService.create(createFormRequest);
            request.setFormId(formId);
            log.info("formId is" + formId);
        }

        SalesAppealExtFormDO salesAppealExtFormDO = PojoUtils.map(request, SalesAppealExtFormDO.class);
        if (CollectionUtil.isNotEmpty(request.getAppendixList())) {
            salesAppealExtFormDO.setAppendix(JSON.toJSONString(request.getAppendixList()));
        }
        SalesAppealExtFormDTO appealExtFormDTO = salesAppealExtFormService.getByFormId(request.getFormId());
        // 保存或更新拓展表数据
        if (ObjectUtil.isNull(appealExtFormDTO)) {
            salesAppealExtFormDO.setConfirmStatus(FleeingConfirmStatusEnum.TO_BE_SUBMIT.getCode());
            salesAppealExtFormService.save(salesAppealExtFormDO);
        } else {
            salesAppealExtFormDO.setId(appealExtFormDTO.getId());
            salesAppealExtFormService.updateById(salesAppealExtFormDO);
        }
        List<UpdateSalesAppealRequest> updateSalesAppealForms = request.getUpdateSalesAppealForms();
        if (CollectionUtil.isNotEmpty(updateSalesAppealForms)) {
            List<SalesAppealFormDO> salesAppealFormDOS = new ArrayList<>();
            updateSalesAppealForms.forEach(item -> {
                SalesAppealFormDO salesAppealFormDO = BeanUtil.copyProperties(item, SalesAppealFormDO.class, "appealMonth");
                String appealMonthStr = item.getAppealMonth();
                Date startDate = DateUtil.parse(appealMonthStr, "yyyy-MM");
                salesAppealFormDO.setAppealMonth(startDate);
                salesAppealFormDOS.add(salesAppealFormDO);
            });

            this.updateBatchById(salesAppealFormDOS);
        }
        return request.getFormId();
    }

    @Override
    public boolean submit(SubmitFleeingGoodsFormRequest request) {

        FormDO formDO = formService.getById(request.getFormId());
        if (ObjectUtil.isNull(formDO)) {
            throw new BusinessException(FleeFormErrorCode.FORM_NOT_FIND);
        }
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(request.getEmpId());
        if (ObjectUtil.isNull(esbEmployeeDTO)) {
            throw new BusinessException(FleeFormErrorCode.START_USER_NOT_FIND);
        }
        Map<String, Object> var = Maps.newHashMap();
        String procDefId = process.get("salesAppeal");
        String tag = Constants.TAG_SALES_GOODS_CHANGE;

        var.put(ApproveConstant.START_USER_ID, esbEmployeeDTO.getEmpId());

        if (formDO.getStatus().equals(FormStatusEnum.UNSUBMIT.getCode())) {
            WorkFlowBaseRequest workFlowBaseRequest = new WorkFlowBaseRequest();
            // 从配置文件获取ProcDefId
            workFlowBaseRequest.setBusinessKey(formDO.getCode());
            workFlowBaseRequest.setEmpName(esbEmployeeDTO.getEmpName());
            workFlowBaseRequest.setStartUserId(request.getEmpId());

            workFlowBaseRequest.setFormType(formDO.getType());
            workFlowBaseRequest.setTitle(formDO.getName());
            workFlowBaseRequest.setId(formDO.getId());
            workFlowBaseRequest.setVariables(var);
            workFlowBaseRequest.setProcDefId(procDefId);
            workFlowBaseRequest.setTag(tag);
            //首次提交
            workFlowService.sendSubmitMsg(workFlowBaseRequest);
        } else {
            WorkFlowBaseRequest updateRequest = new WorkFlowBaseRequest();
            updateRequest.setId(formDO.getId());
            updateRequest.setTag(tag);
            updateRequest.setFormType(request.getFormTypeEnum().getCode());
            updateRequest.setVariables(var);
            //驳回重新提交 var变量变更
            workFlowService.resubmitMsg(updateRequest);
        }
        return Boolean.TRUE;

    }

    @Override
    public String checkFlowFileName(String fileName) {

        // PM_HHN099_濮阳九州通医药有限公司_20221231.csv    月销售_经销商编码_经销商名称_日期.格式
        if (!fileName.toLowerCase().endsWith(".xlsx") && !fileName.toLowerCase().endsWith(".xls")) {
            return "未按照模板文件名称进行命名";
        }
        String[] fileNameArr = fileName.split("_");
        if (fileNameArr.length != 5) {
            return "未按照模板文件名称进行命名";
        }

        Integer dataType = this.getDataType(fileName);
        if (dataType == 0) {
            return "未按照模板文件名称进行命名";
        }
        Long crmenterpriseId;
        try {
            crmenterpriseId = Long.parseLong(fileNameArr[1]);
        } catch (Exception e) {
            return "文件名称中的经销商编码不存在";
        }

        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(crmenterpriseId);
        if (Objects.isNull(crmEnterpriseDTO)) {
            return "文件名称中的经销商编码不存在";
        }
        String[] dateArr = fileNameArr[3].split("\\.");
        if (dateArr[0].contains("-") || dateArr[0].contains("_")) {
            return "未按照模板文件名称进行命名";
        }
        try {
            DateTime date = DateUtil.parse(dateArr[0], "yyyyMMdd");
            /*FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getCustomerMappingStatus();
            if (Objects.nonNull(flowMonthWashControlDTO) && (flowMonthWashControlDTO.getYear() != date.year() || flowMonthWashControlDTO.getMonth() != (date.month() + 1))) {
                return "文件名称年月与当前流向收集年月不匹配";
            }*/
        } catch (Exception e) {
            return "未按照模板文件名称进行命名";
        }
        if (!fileNameArr[4].startsWith("销量申诉确认.")) {
            return "未按照模板文件名称进行命名";
        }
        // 重复校验
        String rightFileName = fileNameArr[1] + "_" + fileNameArr[2] + "_" + fileNameArr[3] + "_" + fileNameArr[4];
        List<SalesAppealFormDO> salesAppealFormDO = this.getByLikeFileName(rightFileName);
        if (CollectionUtil.isNotEmpty(salesAppealFormDO)) {
            Optional<SalesAppealFormDO> salesAppealFormDO1 = salesAppealFormDO.stream().filter(item -> StringUtils.equals(item.getExcelName().toLowerCase(), fileName.toLowerCase())).findAny();
            if (salesAppealFormDO1.isPresent()) {
                return "文件名称与当前流向清洗队列中的文件名称重复";
            }
        }
        return null;

    }

    @Override
    public Long saveSalesConfirm(SaveSalesAppealFormDetailRequest request) {
        SalesAppealFormDO salesAppealFormDO = PojoUtils.map(request, SalesAppealFormDO.class);
        save(salesAppealFormDO);
        return salesAppealFormDO.getId();
    }

    @Override
    public String createFleeFlowForm(CreateSalesAppealFlowRequest request) {
        Long formId = request.getFormId();
        FlowMonthWashControlDTO flowMonthWashControl = flowMonthWashControlApi.getWashStatus();
        if (ObjectUtil.isNull(flowMonthWashControl)) {
            // 针对清洗日程未开启时，此时销量申诉表单的状态为：已提交/未清洗状态, 待清洗日程开启，重新进行清洗任务，确认状态变为：'已提交/未清洗' 状态
            updateSalesAppealConfirmStatus(request);
            return "流向收集工作未开始/已结束";
        }
        SalesAppealExtFormDTO salesAppealExtFormDTO = salesAppealExtFormService.getByFormId(request.getFormId());
        if (FleeingConfirmStatusEnum.SUBMITTED.getCode().equals(salesAppealExtFormDTO.getConfirmStatus())) {
            throw new BusinessException(FleeFormErrorCode.FORM_DETAIL_NOT_RESUBMIT);
        }
        LambdaQueryWrapper<SalesAppealFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SalesAppealFormDO::getType, 2);
        wrapper.eq(SalesAppealFormDO::getFormId, formId).orderByDesc(SalesAppealFormDO::getCreateTime);
        List<SalesAppealFormDO> salesAppealFormDOS = this.list(wrapper);

        if (CollectionUtil.isEmpty(salesAppealFormDOS)) {
            return "请确保数据检查全部“通过”，导入状态全部“导入成功”后再生成流向表单";
        }
        List<SalesAppealFormDO> allRightData = salesAppealFormDOS.stream().filter(item -> item.getCheckStatus() == 1 && item.getImportStatus() == 1).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(allRightData)) {
            return "请确保数据检查全部“通过”，导入状态全部“导入成功”后再生成流向表单";
        }
        if (salesAppealFormDOS.size() != allRightData.size()) {
            return "请确保数据检查全部“通过”，导入状态全部“导入成功”后再生成流向表单";
        }
        for (SalesAppealFormDO createFleeGoodsFlowRequest : salesAppealFormDOS) {
            SalesAppealFormDO fleeingGoodsFormDO = this.getById(createFleeGoodsFlowRequest.getId());
            String crmEnterpriseId = fleeingGoodsFormDO.getExcelName().split("_")[1];
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(Long.parseLong(crmEnterpriseId));

            FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getWashStatus();

            SaveFlowMonthWashTaskRequest taskRequest = new SaveFlowMonthWashTaskRequest();
            taskRequest.setCrmEnterpriseId(crmEnterpriseDTO.getId());
            taskRequest.setFmwcId(flowMonthWashControlDTO.getId());
            taskRequest.setCollectionMethod(CollectionMethodEnum.EXCEL.getCode());
            taskRequest.setFlowClassify(FlowClassifyEnum.SALE_APPEAL.getCode());
            taskRequest.setAppealType(salesAppealExtFormDTO.getAppealType());
            taskRequest.setFlowType(fleeingGoodsFormDO.getDataType());
            taskRequest.setFileName(fleeingGoodsFormDO.getExcelName());
            taskRequest.setOpUserId(request.getOpUserId());
            log.info("创建清洗任务参数" + JSONUtil.toJsonStr(taskRequest));
            long taskId = flowMonthWashTaskApi.create(taskRequest, false);
            // 新增任务id
            fleeingGoodsFormDO.setTaskId(taskId);
            fleeingGoodsFormDO.setOpUserId(request.getOpUserId());
            updateById(fleeingGoodsFormDO);
            // 新增任务id
            salesAppealConfirmApi.updateTaskIdByRecordId(fleeingGoodsFormDO.getId(), taskId, request.getOpUserId());
            // 发送MQ通知
            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_FLOW_WASH_TASK, Constants.TAG_FLOW_WASH_TASK, Long.toString(taskId));
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
            if (mqMessageBO != null) {
                mqMessageSendApi.send(mqMessageBO);
            }
        }

        SalesAppealExtFormDTO fleeingGoodsFormExtDO = salesAppealExtFormService.getByFormId(formId);
        SalesAppealExtFormDO updateExtDO = new SalesAppealExtFormDO();
        updateExtDO.setId(fleeingGoodsFormExtDO.getId());
        updateExtDO.setSubmitWashTime(new Date());
        updateExtDO.setConfirmStatus(2);
        updateExtDO.setConfirmRemark(request.getConfirmDescribe());
        updateExtDO.setConfirmUser(request.getEmpId());
        updateExtDO.setOpUserId(request.getOpUserId());
        updateExtDO.setOpTime(request.getOpTime());
        salesAppealExtFormService.updateById(updateExtDO);
        return "";
    }

    @Override
    public SalesAppealFormDO getByFileName(String fileName) {
        LambdaQueryWrapper<SalesAppealFormDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesAppealFormDO::getType, 2);
        queryWrapper.eq(SalesAppealFormDO::getExcelName, fileName);
        queryWrapper.eq(SalesAppealFormDO::getImportStatus, 1);
        return this.getOne(queryWrapper);
    }

    @Override
    public SalesAppealFormDO getByTaskId(Long taskId) {
        LambdaQueryWrapper<SalesAppealFormDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesAppealFormDO::getTaskId, taskId);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<SalesAppealFormDO> getByLikeFileName(String fileName) {
        LambdaQueryWrapper<SalesAppealFormDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesAppealFormDO::getType, 2);
        queryWrapper.likeLeft(SalesAppealFormDO::getExcelName, fileName);
        queryWrapper.eq(SalesAppealFormDO::getImportStatus, 1);
        return this.list(queryWrapper);
    }

    @Override
    public Boolean updateSalesConfirm(UpdateSalesAppealFormRequest appealFormRequest) {
        SalesAppealFormDO appealFormDO = PojoUtils.map(appealFormRequest, SalesAppealFormDO.class);
        appealFormDO.setOpTime(new Date());
        return updateById(appealFormDO);
    }

    @Override
    public List<SalesAppealFormBO> queryToDoList(QuerySalesAppealPageRequest request) {
        LambdaQueryWrapper<SalesAppealFormDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesAppealFormDO::getType, 1);
        queryWrapper.eq(SalesAppealFormDO::getFormId, request.getFormId());
        queryWrapper.eq(SalesAppealFormDO::getImportStatus, 1);
        return PojoUtils.map(this.list(queryWrapper), SalesAppealFormBO.class);
    }

    @Override
    public SalesAppealExtFormDTO queryAppendix(Long formId) {
        LambdaQueryWrapper<SalesAppealExtFormDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesAppealExtFormDO::getFormId, formId);
        SalesAppealExtFormDO salesAppealFormDO = salesAppealExtFormService.getOne(queryWrapper);
        return PojoUtils.map(salesAppealFormDO, SalesAppealExtFormDTO.class);
    }

    @Override
    public Long newSaveDraft(SaveSalesAppealSubmitFormRequest request) {
        if (Objects.isNull(request.getFormId()) || 0L == request.getFormId()) {
            CreateFormRequest createFormRequest = new CreateFormRequest();
            createFormRequest.setOpUserId(request.getOpUserId());
            createFormRequest.setOpTime(request.getOpTime());
            createFormRequest.setCode(noService.genNo(FormNoEnum.SALES_APPEAL));
            createFormRequest.setType(FormTypeEnum.SALES_APPEAL.getCode());
            createFormRequest.setName(OaTodoUtils.genTitle(FormTypeEnum.SALES_APPEAL.getName(), request.getEmpName(), createFormRequest.getCode(), new Date()));
            createFormRequest.setTransferType(request.getTransferType());
            Long formId = formService.create(createFormRequest);
            request.setFormId(formId);
            log.info("formId is" + formId);
        }
        formService.updateTransferType(request.getFormId(), request.getTransferType());


        SalesAppealExtFormDO salesAppealExtFormDO = PojoUtils.map(request, SalesAppealExtFormDO.class);
        if (CollectionUtil.isNotEmpty(request.getAppendixList())) {
            salesAppealExtFormDO.setAppendix(JSON.toJSONString(request.getAppendixList()));
        }
        SalesAppealExtFormDTO appealExtFormDTO = salesAppealExtFormService.getByFormId(request.getFormId());
        // 保存或更新拓展表数据
        if (ObjectUtil.isNull(appealExtFormDTO)) {
            // 拓展表添加表单id
            salesAppealExtFormDO.setFormId(request.getFormId());
            salesAppealExtFormDO.setConfirmStatus(FleeingConfirmStatusEnum.TO_BE_SUBMIT.getCode());
            salesAppealExtFormService.save(salesAppealExtFormDO);
        } else {
            salesAppealExtFormDO.setId(appealExtFormDTO.getId());
            salesAppealExtFormService.updateById(salesAppealExtFormDO);
        }
        // 根据不同的传输类型选择不同的存取表
        if (Objects.nonNull(request.getTransferType())) {
            if (request.getTransferType() == 1) { // 传输类型为1 上传excel
                List<UpdateSalesAppealRequest> updateSalesAppealForms = request.getUpdateSalesAppealForms();
                if (CollectionUtil.isNotEmpty(updateSalesAppealForms)) {
                    List<SalesAppealFormDO> salesAppealFormDOS = new ArrayList<>();
                    updateSalesAppealForms.forEach(item -> {
                        SalesAppealFormDO salesAppealFormDO = BeanUtil.copyProperties(item, SalesAppealFormDO.class, "appealMonth");
                        String appealMonthStr = item.getAppealMonth();
                        Date startDate = DateUtil.parse(appealMonthStr, "yyyy-MM");
                        salesAppealFormDO.setAppealMonth(startDate);
                        salesAppealFormDOS.add(salesAppealFormDO);
                    });

                    this.saveOrUpdateBatch(salesAppealFormDOS);
                }
                // 清除当前formId相关的所有选择流向的数据
                this.removeAllSelectFlowByFormId(request.getFormId());
            } else if(request.getTransferType() == 2){ // 传输类型为2 选择流向
                // 查询当前提交审核相关formId的所有选择流向数据
                LambdaQueryWrapper<SaleAppealSelectFlowFormDO> queryWrapper = Wrappers.lambdaQuery();
                queryWrapper.eq(SaleAppealSelectFlowFormDO::getFormId, request.getFormId());
                queryWrapper.eq(SaleAppealSelectFlowFormDO::getDelFlag, 0);
                List<SaleAppealSelectFlowFormDO> saleAppealSelectFlowFormDOS = saleAppealSelectFlowFormService.list(queryWrapper);
                if(CollectionUtil.isEmpty(saleAppealSelectFlowFormDOS)){
                    throw new BusinessException(FleeFormErrorCode.Error_19013);
                }
                // 根据销售日期进行分组
                Map<Date, List<SaleAppealSelectFlowFormDO>> saleTimeMap = saleAppealSelectFlowFormDOS.stream()
                        .collect(Collectors.groupingBy(SaleAppealSelectFlowFormDO::getSaleTime));
                // 对提交审核中流向列表数据销售日期进行当前时间往前推六个月的校验（不包含系统当前月份），销售日期超出范围给予提示
                for (Map.Entry<Date, List<SaleAppealSelectFlowFormDO>> dateListEntry : saleTimeMap.entrySet()) {
                    if(!DateUtilsCalculation.isRange(dateListEntry.getKey())){
                        throw new BusinessException(FleeFormErrorCode.ERROR_19018);
                    }
                }
//                List<SelectAppealFlowFormRequest> appealFlowDataDetailFormList = request.getAppealFlowDataDetailFormList();
//                List<SaleAppealSelectFlowFormDO> saleAppealSelectFlowFormDOList = appealFlowDataDetailFormList.stream().map(selectAppealFlowFormRequest -> {
//                    LambdaQueryWrapper<SaleAppealSelectFlowFormDO> wrapper = Wrappers.lambdaQuery();
//                    wrapper.eq(SaleAppealSelectFlowFormDO::getId, selectAppealFlowFormRequest.getId());
//                    SaleAppealSelectFlowFormDO saleAppealSelectFlowFormDO = saleAppealSelectFlowFormService.getOne(wrapper);
//                    saleAppealSelectFlowFormDO.setAppealFinalQuantity(selectAppealFlowFormRequest.getAppealFinalQuantity());
//                    saleAppealSelectFlowFormDO.setChangeCode(selectAppealFlowFormRequest.getChangeCode());
//                    saleAppealSelectFlowFormDO.setChangeName(selectAppealFlowFormRequest.getChangeName());
//                    saleAppealSelectFlowFormDO.setChangeType(selectAppealFlowFormRequest.getChangeType());
//                    saleAppealSelectFlowFormDO.setAppealGoodsSpec(selectAppealFlowFormRequest.getAppealGoodsSpec());
//                    // 保存状态：0-选择确认  1-待提交 3、提交审核
//                    saleAppealSelectFlowFormDO.setSaveStatus(2);
//                    return saleAppealSelectFlowFormDO;
//                }).collect(Collectors.toList());
                List<SaleAppealSelectFlowFormDO> saleAppealSelectFlowFormDOList = saleAppealSelectFlowFormDOS.stream().map(saleAppealSelectFlowFormDO -> {
                    // 保存状态：0-选择确认  1-待提交 3、提交审核
                    saleAppealSelectFlowFormDO.setSaveStatus(2);
                    return saleAppealSelectFlowFormDO;
                }).collect(Collectors.toList());
                saleAppealSelectFlowFormService.updateBatchById(saleAppealSelectFlowFormDOList);
                // 通过formId删除当前formId相关的所有上传excel的数据
                this.removeAllSelectByFormId(request.getFormId());
            }
        }
        return request.getFormId();
    }

    @Override
    public boolean deleteByIds(RemoveSelectAppealFlowFormRequest request) {
        SalesAppealFormDO salesAppealFormDO = new SalesAppealFormDO();
        try {
            QueryWrapper<SalesAppealFormDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().in(SalesAppealFormDO::getId, request.getIds());
            this.batchDeleteWithFill(salesAppealFormDO, wrapper);
        } catch (Exception e) {
            log.error("批量删除销售申诉表单数据错误", e);
            return false;
        }
        return true;
    }

    @Override
    public String compatibleCreateFleeFlowForm(CreateSalesAppealFlowRequest request) {
        // 传输类型：1、上传excel; 2、选择流向
        if(request.getTransferType() == 1){ // 上传excel
            return createFleeFlowForm(request);
        } else if (request.getTransferType() == 2) { //选择流向方式
            FlowMonthWashControlDTO flowMonthWashControl = flowMonthWashControlApi.getWashStatus();
            if (ObjectUtil.isNull(flowMonthWashControl)) {
                // 针对清洗日程未开启时，此时销量申诉表单的状态为：已提交/未清洗状态, 待清洗日程开启，重新进行清洗任务，确认状态修改为：'已提交/未清洗' 状态
                updateSalesAppealConfirmStatus(request);
                return "流向收集工作未开始/已结束";
            }
            // 推送销量申诉清洗数据到月清洗消息队列，开启清洗任务
            startSalesAppealFlowWashTask(request, flowMonthWashControl);
        }
        return "";
    }

    /**
     * 针对清洗日程未开启时，此时销量申诉表单的状态为：已提交/未清洗状态, 待清洗日程开启，重新进行清洗任务，确认状态修改为：'已提交/未清洗' 状态
     * @param request 生成流向表单请求类
     */
    private void updateSalesAppealConfirmStatus(CreateSalesAppealFlowRequest request) {
        // 针对清洗日程未开启时，此时销量申诉表单的状态为：已提交/未清洗状态, 待清洗日程开启，重新进行清洗任务，确认状态修改为：'已提交/未清洗' 状态
        QueryWrapper<SalesAppealExtFormDO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.lambda().eq(SalesAppealExtFormDO::getFormId, request.getFormId());
        SalesAppealExtFormDO updateExtDO = salesAppealExtFormService.getOne(objectQueryWrapper);
        // 确认状态：1-待确认 2-确认提交 3-已提交/未清洗
        updateExtDO.setConfirmStatus(FleeingConfirmStatusEnum.SUBMITTED_UNCLEANED.getCode());
        updateExtDO.setConfirmRemark(request.getConfirmDescribe());
        updateExtDO.setConfirmUser(request.getEmpId());
        updateExtDO.setOpUserId(request.getOpUserId());
        updateExtDO.setOpTime(request.getOpTime());
        salesAppealExtFormService.updateById(updateExtDO);
    }

    /**
     * 推送销量申诉清洗数据到月清洗消息队列，开启清洗任务
     * @param request
     * @param flowMonthWashControl
     */
    private void startSalesAppealFlowWashTask(CreateSalesAppealFlowRequest request, FlowMonthWashControlDTO flowMonthWashControl) {
        SalesAppealExtFormDTO salesAppealExtFormDTO = salesAppealExtFormService.getByFormId(request.getFormId());
        Long formId = request.getFormId();
        LambdaQueryWrapper<SaleAppealSelectFlowFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SaleAppealSelectFlowFormDO::getFormId, formId).orderByDesc(SaleAppealSelectFlowFormDO::getCreateTime);
        List<SaleAppealSelectFlowFormDO> saleAppealSelectFlowFormDOList = saleAppealSelectFlowFormService.list(wrapper);
        if (FleeingConfirmStatusEnum.SUBMITTED.getCode().equals(salesAppealExtFormDTO.getConfirmStatus())) {
            throw new BusinessException(FleeFormErrorCode.FORM_DETAIL_NOT_RESUBMIT);
        }
        Optional<Long> first = saleAppealSelectFlowFormDOList.stream().map(x -> x.getCrmId()).findFirst();
        Long taskId = null;
        if(first.isPresent()){
            FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getWashStatus();
            SaveFlowMonthWashTaskRequest taskRequest = new SaveFlowMonthWashTaskRequest();
            // 经销商编码
            taskRequest.setCrmEnterpriseId(first.get());
            taskRequest.setFmwcId(flowMonthWashControlDTO.getId());
            taskRequest.setCollectionMethod(CollectionMethodEnum.SYSTEM_IMPORT.getCode());
            taskRequest.setFlowClassify(FlowClassifyEnum.SALE_APPEAL.getCode());
            taskRequest.setAppealType(salesAppealExtFormDTO.getAppealType());
            taskRequest.setFlowType(2);
            taskRequest.setFileName("- -");
            taskRequest.setOpUserId(request.getOpUserId());
            taskRequest.setCount(saleAppealSelectFlowFormDOList.size());
            log.info("创建清洗任务参数" + JSONUtil.toJsonStr(taskRequest));
            taskId = flowMonthWashTaskApi.create(taskRequest, false);
        }
        // 新增传输方式为选择流向的销量申诉确认表单正流向数据
        List<SaveSalesAppealDateRequest> saveSalesAppealDateRequestList = new ArrayList<>();
        // 新增传输方式为选择流向的销量申诉确认表单负流向数据
        List<CreateFlowWashSaleReportRequest> createFlowWashSaleReportRequests = new ArrayList<>();

        for (SaleAppealSelectFlowFormDO saleAppealSelectFlowFormDO : saleAppealSelectFlowFormDOList) {
            // 新增任务id
            saleAppealSelectFlowFormDO.setTaskId(taskId);
            saleAppealSelectFlowFormDO.setOpUserId(request.getOpUserId());
            saleAppealSelectFlowFormService.updateById(saleAppealSelectFlowFormDO);

            // 二期销量申诉针对选择流向生成正负流向数据，逻辑如下：
            // 1、根据提交的申诉数据将申诉的列表明细，直接生成流向表单数据，进入月销售清洗。
            // 2、根据申诉列表数据生成正负流向数据，申诉数量作为扣减原始商业生成负流向，同时为机构名称（申诉后）生成正流向；

            //销售申述传输方式为选择流向的数据，生成正流向数据进入销量申诉确认表单待清洗任务开始
            generateObverseFlowData(request, saveSalesAppealDateRequestList, saleAppealSelectFlowFormDO);
            // 新增申诉确认选择流向数据进库
            salesAppealConfirmApi.saveSalesAppealConfirmDate(saveSalesAppealDateRequestList);
            // 销售申述传输方式为选择流向的数据，生成负流向进入销售合并报表
            generateNegateFlow(request, flowMonthWashControl, formId, createFlowWashSaleReportRequests, saleAppealSelectFlowFormDO);

        }
        // 发送MQ通知
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_FLOW_WASH_TASK, Constants.TAG_FLOW_WASH_TASK, Long.toString(taskId));
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        if (mqMessageBO != null) {
            mqMessageSendApi.send(mqMessageBO);
        }

        SalesAppealExtFormDTO fleeingGoodsFormExtDO = salesAppealExtFormService.getByFormId(formId);
        SalesAppealExtFormDO updateExtDO = new SalesAppealExtFormDO();
        updateExtDO.setId(fleeingGoodsFormExtDO.getId());
        updateExtDO.setSubmitWashTime(new Date());
        updateExtDO.setConfirmStatus(2);
        updateExtDO.setConfirmRemark(request.getConfirmDescribe());
        updateExtDO.setConfirmUser(request.getEmpId());
        updateExtDO.setOpUserId(request.getOpUserId());
        updateExtDO.setOpTime(request.getOpTime());
        salesAppealExtFormService.updateById(updateExtDO);
    }

    /**
     * 销售申述传输方式为选择流向的数据，生成正流向数据进入销量申诉确认表单待清洗任务开始
     * @param request
     * @param saveSalesAppealDateRequestList
     * @param saleAppealSelectFlowFormDO
     */
    private static void generateObverseFlowData(CreateSalesAppealFlowRequest request, List<SaveSalesAppealDateRequest> saveSalesAppealDateRequestList, SaleAppealSelectFlowFormDO saleAppealSelectFlowFormDO) {
        // 针对机构名称变化的
        if(saleAppealSelectFlowFormDO.getChangeType().equals(SaleAppealChangeTypeEnum.INSTITUTION_NAME.getCode())){
            // 针对申诉后机构名称生成正流向
            SaveSalesAppealDateRequest targetPositiveSaveSalesAppealDateRequest = new SaveSalesAppealDateRequest();
            // 申诉后经销商名称
            targetPositiveSaveSalesAppealDateRequest.setName(saleAppealSelectFlowFormDO.getChangeName());
            // 申诉后客户名称
            targetPositiveSaveSalesAppealDateRequest.setEnterpriseName(saleAppealSelectFlowFormDO.getChangeName());
            // 原始产品名称
            targetPositiveSaveSalesAppealDateRequest.setGoodsName(saleAppealSelectFlowFormDO.getGoodsName());
            // 原始产品规格
            targetPositiveSaveSalesAppealDateRequest.setSoSpecifications(saleAppealSelectFlowFormDO.getGoodsSpec());
            // 数量（盒）
            targetPositiveSaveSalesAppealDateRequest.setSoQuantity(String.valueOf(saleAppealSelectFlowFormDO.getAppealFinalQuantity()));
            // 终端类型
            targetPositiveSaveSalesAppealDateRequest.setCustomerSupplyChainRole(saleAppealSelectFlowFormDO.getCustomerSupplyChainRole());
            // 不需要随不同申诉类型改变的字段赋值
            fixFieldSet(request, saleAppealSelectFlowFormDO, targetPositiveSaveSalesAppealDateRequest);
            saveSalesAppealDateRequestList.add(targetPositiveSaveSalesAppealDateRequest);
        }
        // 3、如果申诉的类型为产品品名、终端类型，正、负流向则为同一商业；
        // 针对产品名称变化的
        if(saleAppealSelectFlowFormDO.getChangeType().equals(SaleAppealChangeTypeEnum.PRODUCT_NAME.getCode())){

            // 针对申诉后机构名称生成正流向
            SaveSalesAppealDateRequest targetPositiveSaveSalesAppealDateRequest = new SaveSalesAppealDateRequest();
            // 经销商名称
            targetPositiveSaveSalesAppealDateRequest.setName(saleAppealSelectFlowFormDO.getEname());
            // 客户名称
            targetPositiveSaveSalesAppealDateRequest.setEnterpriseName(saleAppealSelectFlowFormDO.getOrgName());
            // 申诉后产品名称
            targetPositiveSaveSalesAppealDateRequest.setGoodsName(saleAppealSelectFlowFormDO.getChangeName());
            // 申诉后的产品规格
            targetPositiveSaveSalesAppealDateRequest.setSoSpecifications(saleAppealSelectFlowFormDO.getAppealGoodsSpec());
            // 数量（盒）
            targetPositiveSaveSalesAppealDateRequest.setSoQuantity(String.valueOf(saleAppealSelectFlowFormDO.getAppealFinalQuantity()));
            // 终端类型
            targetPositiveSaveSalesAppealDateRequest.setCustomerSupplyChainRole(saleAppealSelectFlowFormDO.getCustomerSupplyChainRole());
            // 不需要随不同申诉类型改变的字段赋值
            fixFieldSet(request, saleAppealSelectFlowFormDO, targetPositiveSaveSalesAppealDateRequest);
            saveSalesAppealDateRequestList.add(targetPositiveSaveSalesAppealDateRequest);
        }
        // 针对终端类型
        if(saleAppealSelectFlowFormDO.getChangeType().equals(SaleAppealChangeTypeEnum.INSTITUTIONAL_ATTRIBUTES.getCode())){
            // 针对申诉后终端类型生成正流向
            SaveSalesAppealDateRequest targetPositiveSaveSalesAppealDateRequest = new SaveSalesAppealDateRequest();
            // 经销商名称
            targetPositiveSaveSalesAppealDateRequest.setName(saleAppealSelectFlowFormDO.getEname());
            // 客户名称
            targetPositiveSaveSalesAppealDateRequest.setEnterpriseName(saleAppealSelectFlowFormDO.getOrgName());
            // 标准产品名称
            targetPositiveSaveSalesAppealDateRequest.setGoodsName(saleAppealSelectFlowFormDO.getGoodsName());
            // 申诉后终端类型
            targetPositiveSaveSalesAppealDateRequest.setCustomerSupplyChainRole(saleAppealSelectFlowFormDO.getChangeCode().intValue());
            // 原始产品规格
            targetPositiveSaveSalesAppealDateRequest.setSoSpecifications(saleAppealSelectFlowFormDO.getGoodsSpec());
            // 数量（盒）
            targetPositiveSaveSalesAppealDateRequest.setSoQuantity(String.valueOf(saleAppealSelectFlowFormDO.getAppealFinalQuantity()));
            // 不需要随不同申诉类型改变的字段赋值
            fixFieldSet(request, saleAppealSelectFlowFormDO, targetPositiveSaveSalesAppealDateRequest);
            saveSalesAppealDateRequestList.add(targetPositiveSaveSalesAppealDateRequest);
        }
    }

    /**
     * 销售申述传输方式为选择流向的数据，生成负流向进入销售合并报表
     * @param request
     * @param flowMonthWashControl
     * @param formId
     * @param createFlowWashSaleReportRequests
     * @param saleAppealSelectFlowFormDO
     */
    private void generateNegateFlow(CreateSalesAppealFlowRequest request, FlowMonthWashControlDTO flowMonthWashControl, Long formId, List<CreateFlowWashSaleReportRequest> createFlowWashSaleReportRequests, SaleAppealSelectFlowFormDO saleAppealSelectFlowFormDO) {
        // 组装传输方式为选择流向的销量申述源流向与负流向关联数据
        SaveSaleAppealFlowRelateRequest saveSaleAppealFlowRelateRequest = new SaveSaleAppealFlowRelateRequest();
        // 新流向key 参数枚举 FlowClassifyEnum FlowTypeEnum
        // 调取白富城FlowDataIdUtils.nextId(Integer flowClassify, Integer flowType) 生成新流向key
        String newFlowKey = FlowDataIdUtils.nextId(FlowClassifyEnum.SALE_APPEAL.getCode(), FlowTypeEnum.SALE.getCode());
        // 申述数量
        BigDecimal negateQty = saleAppealSelectFlowFormDO.getAppealFinalQuantity().negate();
        // 清洗时间
        Date washTime = new Date();
        // 计入年份指的是月流向清洗日程中的所属年月
        // 计入年份
        Integer includedYear = flowMonthWashControl.getYear();
        // 计入月份
        Integer includedMonth = flowMonthWashControl.getMonth();


        // 主流程表单id
        saveSaleAppealFlowRelateRequest.setFormId(formId);
        // 源流向id
        saveSaleAppealFlowRelateRequest.setOriginFlowId(saleAppealSelectFlowFormDO.getSelectFlowId());
        // 源流向key
        saveSaleAppealFlowRelateRequest.setOldFlowKey(saleAppealSelectFlowFormDO.getFlowKey());
        // 新流向key
        saveSaleAppealFlowRelateRequest.setFlowKey(newFlowKey);
        // 销售时间
        saveSaleAppealFlowRelateRequest.setSaleTime(saleAppealSelectFlowFormDO.getSaleTime());
        // 流向类型：1、正常；2、销量申诉；3、窜货申报；4、团购处理；5、补传月流向；6、院外药店
        saveSaleAppealFlowRelateRequest.setFlowClassify(FlowClassifyEnum.SALE_APPEAL.getCode());
        // 申述类型
        saveSaleAppealFlowRelateRequest.setComplainType(saleAppealSelectFlowFormDO.getAppealType());
        // 计入年份指的是月流向清洗日程中的所属年月
        // 计入年份 流向审核通过的年份
        saveSaleAppealFlowRelateRequest.setIncludedYear(includedYear);
        // 计入年份 流向审核通过的月份
        saveSaleAppealFlowRelateRequest.setIncludedMonth(includedMonth);
        // 清洗时间
        saveSaleAppealFlowRelateRequest.setWashTime(washTime);
        // 申诉数量
        saveSaleAppealFlowRelateRequest.setQty(negateQty);
        // 新流向id
        long saleAppealFlowRelateId = salesAppealConfirmApi.saveSaveSaleAppealFlowRelate(saveSaleAppealFlowRelateRequest);


        // 根于源流向创建负流向进入流向合并报表
        CreateFlowWashSaleReportRequest createFlowWashSaleReportRequest = new CreateFlowWashSaleReportRequest();
        // 源流向key
        createFlowWashSaleReportRequest.setOldFlowKey(saleAppealSelectFlowFormDO.getFlowKey());
        // 新流向key
        createFlowWashSaleReportRequest.setFlowKey(newFlowKey);
        // 申诉数量
        createFlowWashSaleReportRequest.setQty(negateQty);
        // 清洗时间
        createFlowWashSaleReportRequest.setWashTime(washTime);
        // 流向类型：1、正常；2、销量申诉；3、窜货申报；4、团购处理；5、补传月流向；6、院外药店
        createFlowWashSaleReportRequest.setFlowClassify(FlowClassifyEnum.SALE_APPEAL.getCode());
        // 新生成的流向Id 即为流向关系表中的主键id
        createFlowWashSaleReportRequest.setFlowWashId(saleAppealFlowRelateId);
        // 申诉类型
        createFlowWashSaleReportRequest.setComplainType(saleAppealSelectFlowFormDO.getAppealType());
        // 计入年份
        createFlowWashSaleReportRequest.setYear(includedYear);
        // 计入月份
        createFlowWashSaleReportRequest.setMonth(includedMonth);
        // 操作人
        createFlowWashSaleReportRequest.setOpUserId(request.getOpUserId());
        // 操作时间
        createFlowWashSaleReportRequest.setOpTime(new Date());
        createFlowWashSaleReportRequests.add(createFlowWashSaleReportRequest);
        // 根据源流向创建新的流向
        flowWashSaleReportApi.createByFlowWashRecord(createFlowWashSaleReportRequests);
    }

    @Override
    public boolean valid(Long formId) {
        // 查询库里选择流向的数据
        LambdaQueryWrapper<SaleAppealSelectFlowFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SaleAppealSelectFlowFormDO::getDelFlag, 0);
        wrapper.notIn(SaleAppealSelectFlowFormDO::getFormId, formId);
        wrapper.eq(SaleAppealSelectFlowFormDO::getSaveStatus, 2);
        List<SaleAppealSelectFlowFormDO> saleAppealSelectFlowFormDOList = saleAppealSelectFlowFormService.list(wrapper);
        List<String> flowIdList= saleAppealSelectFlowFormDOList.stream().map(SaleAppealSelectFlowFormDO::getFlowKey).collect(Collectors.toList());
        // 查询当前提交审核相关formId的所有选择流向数据
        LambdaQueryWrapper<SaleAppealSelectFlowFormDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SaleAppealSelectFlowFormDO::getFormId, formId);
        List<SaleAppealSelectFlowFormDO> saleAppealSelectFlowFormDOS = saleAppealSelectFlowFormService.list(queryWrapper);
        List<String> flowIdListRequest = saleAppealSelectFlowFormDOS.stream().map(SaleAppealSelectFlowFormDO::getFlowKey).collect(Collectors.toList());

        if(CollectionUtil.isNotEmpty(saleAppealSelectFlowFormDOList)){
            // 取出两个集合的交集
            List<String> intersectionFlowIdList = flowIdList.stream().filter(flowIdListRequest::contains).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(intersectionFlowIdList)){
                throw new BusinessException(FleeFormErrorCode.FLOW_IS_LOCK);
            }
        }
        return true;
    }

    @Override
    public boolean removeAllSelectByFormId(Long formId) {
        try {
            // 查询当前formId相关的所有上传excel的数据
            SalesAppealFormDO salesAppealFormDO = new SalesAppealFormDO();
            LambdaQueryWrapper<SalesAppealFormDO> salesAppealFormDOLambdaQueryWrapper = Wrappers.lambdaQuery();
            salesAppealFormDOLambdaQueryWrapper.eq(SalesAppealFormDO::getFormId, formId);
            this.batchDeleteWithFill(salesAppealFormDO, salesAppealFormDOLambdaQueryWrapper);
        } catch (Exception e) {
            log.error("清除当前formId相关的上传excel数据失败", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean removeAllSelectFlowByFormId(Long formId) {
        try {
            // 查询当前formId相关的所有选择流向的数据
            SaleAppealSelectFlowFormDO saleAppealSelectFlowFormDO = new SaleAppealSelectFlowFormDO();
            LambdaQueryWrapper<SaleAppealSelectFlowFormDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(SaleAppealSelectFlowFormDO::getFormId, formId);
            saleAppealSelectFlowFormService.batchDeleteWithFill(saleAppealSelectFlowFormDO, wrapper);
        } catch (Exception e) {
            log.error("清除当前formId相关的所有选择流向的数据失败", e);
            return false;
        }
        return false;
    }

    @Override
    public boolean washSaleAppealFlowData() {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getWashStatus();
        if (ObjectUtil.isNull(flowMonthWashControlDTO)) {
            // 针对清洗日程未开启时，此时销量申诉表单的状态为：已提交/未清洗状态, 待清洗日程开启，重新进行清洗任务，确认状态变为：'已提交/未清洗' 状态
            log.info("流向收集工作未开始/已结束, 日程参数flowMonthWashControlDTO为：{}", JSON.toJSONString(flowMonthWashControlDTO));
            return false;
        }
        // 查询出所有日程未开启，已提交销量申诉确认生成流向表单申请，未清洗的表单id
        List<Long> submitUnCleanedFormIdList = salesAppealExtFormService.getSubmitUnCleaned();
        log.info("查询出所有日程未开启，已提交销量申诉确认生成流向表单申请，未清洗的表单formId为：{}", JSON.toJSONString(submitUnCleanedFormIdList));
        // 查询出传输类型为上传excel方式的所有表单id集合
        List<Long> excelFormIds = formService.formIdByTransferType(TransferTypeEnum.EXCEL_UPLOAD.getCode(), submitUnCleanedFormIdList);
        log.info("查询出未清洗的, 传输类型为上传excel方式的所有表单formId集合为：{}", JSON.toJSONString(excelFormIds));
        if (CollUtil.isNotEmpty(excelFormIds)) {
            for (Long excelFormId : excelFormIds) {
                LambdaQueryWrapper<SalesAppealFormDO> wrapper = Wrappers.lambdaQuery();
                wrapper.eq(SalesAppealFormDO::getType, 2);
                wrapper.eq(SalesAppealFormDO::getFormId, excelFormId).orderByDesc(SalesAppealFormDO::getCreateTime);
                List<SalesAppealFormDO> salesAppealFormDOS = this.list(wrapper);
                SalesAppealExtFormDTO salesAppealExtFormDTO = salesAppealExtFormService.getByFormId(excelFormId);
                // 查询数据检查全部“通过”，导入状态全部“导入成功”的数据
                List<SalesAppealFormDO> allRightData = salesAppealFormDOS.stream().filter(item -> item.getCheckStatus() == 1 && item.getImportStatus() == 1).collect(Collectors.toList());
                // 已提交/未清洗的数据，数据检查全部“通过”，导入状态全部“导入成功”的数据，才允许生成月流向数据
                if (FleeingConfirmStatusEnum.SUBMITTED_UNCLEANED.getCode().equals(salesAppealExtFormDTO.getConfirmStatus())
                        && CollectionUtil.isNotEmpty(salesAppealFormDOS)
                        && CollectionUtil.isNotEmpty(allRightData)
                        && salesAppealFormDOS.size() == allRightData.size()) {
                    // 将检测与当前formId相关的销量申诉确认表单，提交清洗时间赋值为推送清洗队列的时间
                    UpdateWrapper<SalesAppealExtFormDO> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("form_id", excelFormId);
                    updateWrapper.set("submit_wash_time", new Date());
                    salesAppealExtFormService.update(updateWrapper);
                    // 检测与当前formId相关的数据检查通过和导入状态成功的的excel销量申诉数据，开启清洗任务
                    restartCleanExcelSalesAppealData(salesAppealExtFormDTO, salesAppealFormDOS);
                }
            }
        }
        // 查询出传输类型为选择流向方式的所有表单id集合
        List<Long> selectAppealDataFormIds = formService.formIdByTransferType(TransferTypeEnum.SELECT_APPEAL.getCode(), submitUnCleanedFormIdList);
        if (CollUtil.isNotEmpty(selectAppealDataFormIds)) {
            for (Long selectAppealDataFormId : selectAppealDataFormIds) {
                SalesAppealExtFormDTO salesAppealExtFormDTO = salesAppealExtFormService.getByFormId(selectAppealDataFormId);
                // 查询formId相关的选择流向数据
                LambdaQueryWrapper<SaleAppealSelectFlowFormDO> wrapper = Wrappers.lambdaQuery();
                wrapper.eq(SaleAppealSelectFlowFormDO::getFormId, selectAppealDataFormId).orderByDesc(SaleAppealSelectFlowFormDO::getCreateTime);
                List<SaleAppealSelectFlowFormDO> saleAppealSelectFlowFormDOList = saleAppealSelectFlowFormService.list(wrapper);
                // 将检测与当前formId相关的销量申诉确认表单，提交清洗时间赋值为推送清洗队列的时间
                UpdateWrapper<SalesAppealExtFormDO> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("form_id", selectAppealDataFormId);
                updateWrapper.set("submit_wash_time", new Date());
                salesAppealExtFormService.update(updateWrapper);
                // 日程开启，创建月清洗任务，生成taskId
                Long taskId = createMonthFlowWashTask(flowMonthWashControlDTO, salesAppealExtFormDTO, saleAppealSelectFlowFormDOList);
                // 新增传输方式为选择流向的销量申诉确认表单正流向数据
                List<SaveSalesAppealDateRequest> saveSalesAppealDateRequestList = new ArrayList<>();
                // 新增传输方式为选择流向的销量申诉确认表单负流向数据
                List<CreateFlowWashSaleReportRequest> createFlowWashSaleReportRequests = new ArrayList<>();

                for (SaleAppealSelectFlowFormDO saleAppealSelectFlowFormDO : saleAppealSelectFlowFormDOList) {
                    // 新增任务id
                    saleAppealSelectFlowFormDO.setTaskId(taskId);
                    saleAppealSelectFlowFormDO.setUpdateUser(0L);
                    saleAppealSelectFlowFormDO.setUpdateTime(new Date());
                    saleAppealSelectFlowFormService.updateById(saleAppealSelectFlowFormDO);
                    // 清洗日程重新开启，将传输方式为选择流向 ”已提交/未清洗“ 的数据重新清洗生成正流向数据
                    restartGenerateObverseCleanData(saveSalesAppealDateRequestList, saleAppealSelectFlowFormDO);
                    // 新增申诉确认选择流向数据进库
                    salesAppealConfirmApi.saveSalesAppealConfirmDate(saveSalesAppealDateRequestList);
                    // 销售申述传输方式为选择流向的数据，生成负流向进入销售合并报表
                    // 日程开启，对传输方式为选择流向 “已提交/未清洗” 的数据重新清洗生成负流向数据推送到 销量申诉源流向与负流向关联表 和 销售合并报表
                    restartGenerateNegateCleanFlowData(flowMonthWashControlDTO, selectAppealDataFormId, createFlowWashSaleReportRequests, saleAppealSelectFlowFormDO);

                }
                // 发送MQ通知
                MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_FLOW_WASH_TASK, Constants.TAG_FLOW_WASH_TASK, Long.toString(taskId));
                mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
                if (mqMessageBO != null) {
                    mqMessageSendApi.send(mqMessageBO);
                }

                SalesAppealExtFormDO updateExtDO = new SalesAppealExtFormDO();
                updateExtDO.setId(salesAppealExtFormDTO.getId());
                updateExtDO.setSubmitWashTime(new Date());
                updateExtDO.setConfirmStatus(2);
                updateExtDO.setRemark("日程开启系统重新开启清洗任务");
                updateExtDO.setUpdateTime(new Date());
                salesAppealExtFormService.updateById(updateExtDO);
            }
        } 
        return true;
    }

    /**
     * 日程开启，创建月清洗任务
     * @param flowMonthWashControlDTO 日程
     * @param salesAppealExtFormDTO 销量申诉拓展表单
     * @param saleAppealSelectFlowFormDOList 选择流向数据列表
     * @return
     */
    private Long createMonthFlowWashTask(FlowMonthWashControlDTO flowMonthWashControlDTO, SalesAppealExtFormDTO salesAppealExtFormDTO, List<SaleAppealSelectFlowFormDO> saleAppealSelectFlowFormDOList) {
        Optional<Long> first = saleAppealSelectFlowFormDOList.stream().map(SaleAppealSelectFlowFormDO::getCrmId).findFirst();
        Long taskId = null;
        if(first.isPresent()){
            SaveFlowMonthWashTaskRequest taskRequest = new SaveFlowMonthWashTaskRequest();
            // 经销商编码
            taskRequest.setCrmEnterpriseId(first.get());
            taskRequest.setFmwcId(flowMonthWashControlDTO.getId());
            taskRequest.setCollectionMethod(CollectionMethodEnum.SYSTEM_IMPORT.getCode());
            taskRequest.setFlowClassify(FlowClassifyEnum.SALE_APPEAL.getCode());
            taskRequest.setAppealType(salesAppealExtFormDTO.getAppealType());
            taskRequest.setFlowType(2);
            taskRequest.setFileName("- -");
            taskRequest.setCount(saleAppealSelectFlowFormDOList.size());
            log.info("创建清洗任务参数" + JSONUtil.toJsonStr(taskRequest));
            taskId = flowMonthWashTaskApi.create(taskRequest, false);
        }
        return taskId;
    }

    /**
     * 日程开启，对传输方式为选择流向 “已提交/未清洗” 的数据重新清洗生成负流向数据推送到 销量申诉源流向与负流向关联表 和 销售合并报表
     * @param flowMonthWashControlDTO 日程 为 后期计入年、 计入月份准备
     * @param selectAppealDataFormId 选择流向的表单id
     * @param createFlowWashSaleReportRequests 组装推送销售合并报表的请求类
     * @param saleAppealSelectFlowFormDO 选择流向表单
     */
    private void restartGenerateNegateCleanFlowData(FlowMonthWashControlDTO flowMonthWashControlDTO, Long selectAppealDataFormId, List<CreateFlowWashSaleReportRequest> createFlowWashSaleReportRequests, SaleAppealSelectFlowFormDO saleAppealSelectFlowFormDO) {
        SaveSaleAppealFlowRelateRequest saveSaleAppealFlowRelateRequest = new SaveSaleAppealFlowRelateRequest();
        // 新流向key 参数枚举 FlowClassifyEnum FlowTypeEnum
        // 调取白富城FlowDataIdUtils.nextId(Integer flowClassify, Integer flowType) 生成新流向key
        String newFlowKey = FlowDataIdUtils.nextId(FlowClassifyEnum.SALE_APPEAL.getCode(), FlowTypeEnum.SALE.getCode());
        // 申述数量
        BigDecimal negateQty = saleAppealSelectFlowFormDO.getAppealFinalQuantity().negate();
        // 清洗时间
        Date washTime = new Date();
        // 计入年份指的是月流向清洗日程中的所属年月
        // 计入年份
        Integer includedYear = flowMonthWashControlDTO.getYear();
        // 计入月份
        Integer includedMonth = flowMonthWashControlDTO.getMonth();


        // 主流程表单id
        saveSaleAppealFlowRelateRequest.setFormId(selectAppealDataFormId);
        // 源流向id
        saveSaleAppealFlowRelateRequest.setOriginFlowId(saleAppealSelectFlowFormDO.getSelectFlowId());
        // 源流向key
        saveSaleAppealFlowRelateRequest.setOldFlowKey(saleAppealSelectFlowFormDO.getFlowKey());
        // 新流向key
        saveSaleAppealFlowRelateRequest.setFlowKey(newFlowKey);
        // 销售时间
        saveSaleAppealFlowRelateRequest.setSaleTime(saleAppealSelectFlowFormDO.getSaleTime());
        // 流向类型：1、正常；2、销量申诉；3、窜货申报；4、团购处理；5、补传月流向；6、院外药店
        saveSaleAppealFlowRelateRequest.setFlowClassify(FlowClassifyEnum.SALE_APPEAL.getCode());
        // 申述类型
        saveSaleAppealFlowRelateRequest.setComplainType(saleAppealSelectFlowFormDO.getAppealType());
        // 计入年份指的是月流向清洗日程中的所属年月
        // 计入年份 流向审核通过的年份
        saveSaleAppealFlowRelateRequest.setIncludedYear(includedYear);
        // 计入年份 流向审核通过的月份
        saveSaleAppealFlowRelateRequest.setIncludedMonth(includedMonth);
        // 清洗时间
        saveSaleAppealFlowRelateRequest.setWashTime(washTime);
        // 申诉数量
        saveSaleAppealFlowRelateRequest.setQty(negateQty);
        // 新流向id
        long saleAppealFlowRelateId = salesAppealConfirmApi.saveSaveSaleAppealFlowRelate(saveSaleAppealFlowRelateRequest);


        // 根于源流向创建负流向进入流向合并报表
        CreateFlowWashSaleReportRequest createFlowWashSaleReportRequest = new CreateFlowWashSaleReportRequest();
        // 源流向key
        createFlowWashSaleReportRequest.setOldFlowKey(saleAppealSelectFlowFormDO.getFlowKey());
        // 新流向key
        createFlowWashSaleReportRequest.setFlowKey(newFlowKey);
        // 申诉数量
        createFlowWashSaleReportRequest.setQty(negateQty);
        // 清洗时间
        createFlowWashSaleReportRequest.setWashTime(washTime);
        // 流向类型：1、正常；2、销量申诉；3、窜货申报；4、团购处理；5、补传月流向；6、院外药店
        createFlowWashSaleReportRequest.setFlowClassify(FlowClassifyEnum.SALE_APPEAL.getCode());
        // 新生成的流向Id 即为流向关系表中的主键id
        createFlowWashSaleReportRequest.setFlowWashId(saleAppealFlowRelateId);
        // 申诉类型
        createFlowWashSaleReportRequest.setComplainType(saleAppealSelectFlowFormDO.getAppealType());
        // 计入年份
        createFlowWashSaleReportRequest.setYear(includedYear);
        // 计入月份
        createFlowWashSaleReportRequest.setMonth(includedMonth);
        // 操作人
        createFlowWashSaleReportRequest.setOpUserId(0L);
        // 操作时间
        createFlowWashSaleReportRequest.setOpTime(new Date());
        createFlowWashSaleReportRequests.add(createFlowWashSaleReportRequest);
        // 根据源流向创建新的流向
        flowWashSaleReportApi.createByFlowWashRecord(createFlowWashSaleReportRequests);
    }

    /**
     * 清洗日程重新开启，将传输方式为选择流向 ”已提交/未清洗“ 的数据重新清洗生成正流向数据
     * @param saveSalesAppealDateRequestList 新增传输方式为选择流向的销量申诉确认表单正流向数据
     * @param saleAppealSelectFlowFormDO 选择流向数据
     */
    private static void restartGenerateObverseCleanData(List<SaveSalesAppealDateRequest> saveSalesAppealDateRequestList, SaleAppealSelectFlowFormDO saleAppealSelectFlowFormDO) {
        // 针对机构名称变化的
        if(saleAppealSelectFlowFormDO.getChangeType().equals(SaleAppealChangeTypeEnum.INSTITUTION_NAME.getCode())){
            // 针对申诉后机构名称生成正流向
            SaveSalesAppealDateRequest targetPositiveSaveSalesAppealDateRequest = new SaveSalesAppealDateRequest();
            // 申诉后经销商名称
            targetPositiveSaveSalesAppealDateRequest.setName(saleAppealSelectFlowFormDO.getChangeName());
            // 申诉后客户名称
            targetPositiveSaveSalesAppealDateRequest.setEnterpriseName(saleAppealSelectFlowFormDO.getChangeName());
            // 原始产品名称
            targetPositiveSaveSalesAppealDateRequest.setGoodsName(saleAppealSelectFlowFormDO.getGoodsName());
            // 原始产品规格
            targetPositiveSaveSalesAppealDateRequest.setSoSpecifications(saleAppealSelectFlowFormDO.getGoodsSpec());
            // 数量（盒）
            targetPositiveSaveSalesAppealDateRequest.setSoQuantity(String.valueOf(saleAppealSelectFlowFormDO.getAppealFinalQuantity()));
            // 终端类型
            targetPositiveSaveSalesAppealDateRequest.setCustomerSupplyChainRole(saleAppealSelectFlowFormDO.getCustomerSupplyChainRole());
            // 不需要随不同申诉类型改变的字段赋值
            systemCleanFixFieldSet(saveSalesAppealDateRequestList, saleAppealSelectFlowFormDO, targetPositiveSaveSalesAppealDateRequest);
            saveSalesAppealDateRequestList.add(targetPositiveSaveSalesAppealDateRequest);

        }
        // 3、如果申诉的类型为产品品名、终端类型，正、负流向则为同一商业；
        // 针对产品名称变化的
        if(saleAppealSelectFlowFormDO.getChangeType().equals(SaleAppealChangeTypeEnum.PRODUCT_NAME.getCode())){

            // 针对申诉后机构名称生成正流向
            SaveSalesAppealDateRequest targetPositiveSaveSalesAppealDateRequest = new SaveSalesAppealDateRequest();
            // 经销商名称
            targetPositiveSaveSalesAppealDateRequest.setName(saleAppealSelectFlowFormDO.getEname());
            // 客户名称
            targetPositiveSaveSalesAppealDateRequest.setEnterpriseName(saleAppealSelectFlowFormDO.getOrgName());
            // 申诉后产品名称
            targetPositiveSaveSalesAppealDateRequest.setGoodsName(saleAppealSelectFlowFormDO.getChangeName());
            // 申诉产品规格
            targetPositiveSaveSalesAppealDateRequest.setSoSpecifications(saleAppealSelectFlowFormDO.getAppealGoodsSpec());
            // 数量（盒）
            targetPositiveSaveSalesAppealDateRequest.setSoQuantity(String.valueOf(saleAppealSelectFlowFormDO.getAppealFinalQuantity()));
            // 终端类型
            targetPositiveSaveSalesAppealDateRequest.setCustomerSupplyChainRole(saleAppealSelectFlowFormDO.getCustomerSupplyChainRole());
            // 不需要随不同申诉类型改变的字段赋值
            // 不需要随不同申诉类型改变的字段赋值
            systemCleanFixFieldSet(saveSalesAppealDateRequestList, saleAppealSelectFlowFormDO, targetPositiveSaveSalesAppealDateRequest);
            saveSalesAppealDateRequestList.add(targetPositiveSaveSalesAppealDateRequest);
        }
        // 针对终端类型
        if(saleAppealSelectFlowFormDO.getChangeType().equals(SaleAppealChangeTypeEnum.INSTITUTIONAL_ATTRIBUTES.getCode())){
            // 针对申诉后终端类型生成正流向
            SaveSalesAppealDateRequest targetPositiveSaveSalesAppealDateRequest = new SaveSalesAppealDateRequest();
            // 经销商名称
            targetPositiveSaveSalesAppealDateRequest.setName(saleAppealSelectFlowFormDO.getEname());
            // 客户名称
            targetPositiveSaveSalesAppealDateRequest.setEnterpriseName(saleAppealSelectFlowFormDO.getOrgName());
            // 标准产品名称
            targetPositiveSaveSalesAppealDateRequest.setGoodsName(saleAppealSelectFlowFormDO.getGoodsName());
            // 申诉后终端类型
            targetPositiveSaveSalesAppealDateRequest.setCustomerSupplyChainRole(saleAppealSelectFlowFormDO.getChangeCode().intValue());
            // 原始产品规格
            targetPositiveSaveSalesAppealDateRequest.setSoSpecifications(saleAppealSelectFlowFormDO.getGoodsSpec());
            // 数量（盒）
            targetPositiveSaveSalesAppealDateRequest.setSoQuantity(String.valueOf(saleAppealSelectFlowFormDO.getAppealFinalQuantity()));
            // 不需要随不同申诉类型改变的字段赋值
            systemCleanFixFieldSet(saveSalesAppealDateRequestList, saleAppealSelectFlowFormDO, targetPositiveSaveSalesAppealDateRequest);
            saveSalesAppealDateRequestList.add(targetPositiveSaveSalesAppealDateRequest);
        }
    }

    /**
     * 检测与当前formId相关的数据检查通过和导入状态成功的的excel销量申诉数据，重新开启清洗任务
     * @param salesAppealExtFormDTO 销量申诉拓展表数据
     * @param salesAppealFormDOS 销量申诉表单列表数据
     */
    private void restartCleanExcelSalesAppealData(SalesAppealExtFormDTO salesAppealExtFormDTO, List<SalesAppealFormDO> salesAppealFormDOS) {
        for (SalesAppealFormDO createFleeGoodsFlowRequest : salesAppealFormDOS) {
            SalesAppealFormDO fleeingGoodsFormDO = this.getById(createFleeGoodsFlowRequest.getId());
            String crmEnterpriseId = fleeingGoodsFormDO.getExcelName().split("_")[1];
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(Long.parseLong(crmEnterpriseId));

            FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getWashStatus();

            SaveFlowMonthWashTaskRequest taskRequest = new SaveFlowMonthWashTaskRequest();
            taskRequest.setCrmEnterpriseId(crmEnterpriseDTO.getId());
            taskRequest.setFmwcId(flowMonthWashControlDTO.getId());
            taskRequest.setCollectionMethod(CollectionMethodEnum.EXCEL.getCode());
            taskRequest.setFlowClassify(FlowClassifyEnum.SALE_APPEAL.getCode());
            taskRequest.setAppealType(salesAppealExtFormDTO.getAppealType());
            taskRequest.setFlowType(fleeingGoodsFormDO.getDataType());
            taskRequest.setFileName(fleeingGoodsFormDO.getExcelName());
            log.info("创建清洗任务参数" + JSONUtil.toJsonStr(taskRequest));
            long taskId = flowMonthWashTaskApi.create(taskRequest, false);
            // 新增任务id
            fleeingGoodsFormDO.setTaskId(taskId);
            fleeingGoodsFormDO.setUpdateTime(new Date());
            updateById(fleeingGoodsFormDO);
            // 新增任务id
            salesAppealConfirmApi.updateTaskIdByRecordId(fleeingGoodsFormDO.getId(), taskId, null);
            // 发送MQ通知
            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_FLOW_WASH_TASK, Constants.TAG_FLOW_WASH_TASK, Long.toString(taskId));
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
            if (mqMessageBO != null) {
                mqMessageSendApi.send(mqMessageBO);
            }
        }
        SalesAppealExtFormDO updateExtDO = new SalesAppealExtFormDO();
        updateExtDO.setId(salesAppealExtFormDTO.getId());
        updateExtDO.setSubmitWashTime(new Date());
        updateExtDO.setConfirmStatus(2);
        updateExtDO.setRemark("日程开启系统重新开启清洗任务");
        updateExtDO.setUpdateTime(new Date());
        salesAppealExtFormService.updateById(updateExtDO);
    }

    /**
     * 日程开启，系统清洗销量任务重新开启
     * @param saveSalesAppealDateRequestList
     * @param saleAppealSelectFlowFormDO
     * @param targetPositiveSaveSalesAppealDateRequest
     */
    private static void systemCleanFixFieldSet(List<SaveSalesAppealDateRequest> saveSalesAppealDateRequestList, SaleAppealSelectFlowFormDO saleAppealSelectFlowFormDO, SaveSalesAppealDateRequest targetPositiveSaveSalesAppealDateRequest) {
        // 任务id
        targetPositiveSaveSalesAppealDateRequest.setTaskId(saleAppealSelectFlowFormDO.getTaskId());
        // 主流程表单
        targetPositiveSaveSalesAppealDateRequest.setFormId(saleAppealSelectFlowFormDO.getFormId());
        // 销量申诉选择流向表记录id
        targetPositiveSaveSalesAppealDateRequest.setRecordId(saleAppealSelectFlowFormDO.getId());
        // 销售日期
        targetPositiveSaveSalesAppealDateRequest.setSoTime(saleAppealSelectFlowFormDO.getSaleTime());
        // 单位
        targetPositiveSaveSalesAppealDateRequest.setSoUnit(saleAppealSelectFlowFormDO.getSoUnit());
        // 单价
        targetPositiveSaveSalesAppealDateRequest.setSoPrice(String.valueOf(saleAppealSelectFlowFormDO.getSalesPrice()));
        // 金额
        targetPositiveSaveSalesAppealDateRequest.setSoTotalAmount(String.valueOf(saleAppealSelectFlowFormDO.getSoTotalAmount()));
        // 备注
        targetPositiveSaveSalesAppealDateRequest.setBusinessRemark("销量申诉选择流向");
        // 创建人
        targetPositiveSaveSalesAppealDateRequest.setCreateUser(0L);
        // 创建人
        targetPositiveSaveSalesAppealDateRequest.setCreateTime(new Date());
        // 修改人
        targetPositiveSaveSalesAppealDateRequest.setUpdateUser(0L);
        // 修改时间
        targetPositiveSaveSalesAppealDateRequest.setUpdateTime(new Date());
        // 备注
        targetPositiveSaveSalesAppealDateRequest.setRemark("清洗日程开启，重新清洗销量申诉选择流向");
        // 操作人
        targetPositiveSaveSalesAppealDateRequest.setOpUserId(0L);
        // 操作时间
        targetPositiveSaveSalesAppealDateRequest.setOpTime(new Date());
        // 源流向key
        targetPositiveSaveSalesAppealDateRequest.setOriginFlowKey(saleAppealSelectFlowFormDO.getFlowKey());
        // 流向id
        targetPositiveSaveSalesAppealDateRequest.setSelectFlowId(saleAppealSelectFlowFormDO.getSelectFlowId());
        // 传输类型：1、上传excel; 2、选择流向
        targetPositiveSaveSalesAppealDateRequest.setTransferType(TransferTypeEnum.SELECT_APPEAL.getCode());
    }

    /**
     * 保持月流向不变的字段赋值
     * @param request
     * @param saleAppealSelectFlowFormDO
     * @param saveSalesAppealDateRequest
     */
    private static void fixFieldSet(CreateSalesAppealFlowRequest request, SaleAppealSelectFlowFormDO saleAppealSelectFlowFormDO, SaveSalesAppealDateRequest saveSalesAppealDateRequest) {
        // 任务id
        saveSalesAppealDateRequest.setTaskId(saleAppealSelectFlowFormDO.getTaskId());
        // 主流程表单
        saveSalesAppealDateRequest.setFormId(saleAppealSelectFlowFormDO.getFormId());
        // 销量申诉选择流向表记录id
        saveSalesAppealDateRequest.setRecordId(saleAppealSelectFlowFormDO.getId());
        // 销售日期
        saveSalesAppealDateRequest.setSoTime(saleAppealSelectFlowFormDO.getSaleTime());
        // 单位
        saveSalesAppealDateRequest.setSoUnit(saleAppealSelectFlowFormDO.getSoUnit());
        // 单价
        saveSalesAppealDateRequest.setSoPrice(String.valueOf(saleAppealSelectFlowFormDO.getSalesPrice()));
        // 金额
        saveSalesAppealDateRequest.setSoTotalAmount(String.valueOf(saleAppealSelectFlowFormDO.getSoTotalAmount()));
        // 备注
        saveSalesAppealDateRequest.setBusinessRemark("销量申诉选择流向");
        // 创建人
        saveSalesAppealDateRequest.setCreateUser(request.getOpUserId());
        // 创建人
        saveSalesAppealDateRequest.setCreateTime(new Date());
        // 修改人
        saveSalesAppealDateRequest.setUpdateUser(request.getOpUserId());
        // 修改时间
        saveSalesAppealDateRequest.setUpdateTime(new Date());
        // 备注
        saveSalesAppealDateRequest.setRemark("销量申诉选择流向");
        // 操作人
        saveSalesAppealDateRequest.setOpUserId(request.getOpUserId());
        // 操作时间
        saveSalesAppealDateRequest.setOpTime(new Date());
        // 源流向key
        saveSalesAppealDateRequest.setOriginFlowKey(saleAppealSelectFlowFormDO.getFlowKey());
        // 流向id
        saveSalesAppealDateRequest.setSelectFlowId(saleAppealSelectFlowFormDO.getSelectFlowId());
        // 传输类型：1、上传excel; 2、选择流向
        saveSalesAppealDateRequest.setTransferType(2);
    }

    @Override
    public Integer getDataType(String fileName) {
        String[] fileNameArr = fileName.split("_");
        String code = fileNameArr[0].toLowerCase();
        //1-采购 2-销售 3-库存
        int dataType = 0;
        // 月销售（SM开头）   月采购（PM开头）   月库存（IM开头）
        switch (code) {
            case "sm":
                dataType = 2;
                break;
            case "pm":
                dataType = 1;
                break;
            case "im":
                dataType = 3;
                break;
        }
        return dataType;
    }
}
