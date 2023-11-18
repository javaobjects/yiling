package com.yiling.sjms.flee.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.yiling.sjms.flee.dto.FleeingGoodsFormDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.flowcollect.api.FlowFleeingGoodsApi;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadCheckStatusEnum;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadImportStatusEnum;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.api.FlowMonthWashTaskApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.request.SaveFlowMonthWashTaskRequest;
import com.yiling.dataflow.wash.enums.CollectionMethodEnum;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.dataflow.wash.enums.FlowTypeEnum;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.constant.ApproveConstant;
import com.yiling.sjms.flee.bo.FleeingFormBO;
import com.yiling.sjms.flee.dao.FleeingGoodsFormMapper;
import com.yiling.sjms.flee.dto.request.ApproveFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.CreateFleeFlowRequest;
import com.yiling.sjms.flee.dto.request.QueryFleeingFormPageRequest;
import com.yiling.sjms.flee.dto.request.QueryFleeingGoodsFormListRequest;
import com.yiling.sjms.flee.dto.request.RemoveFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.SaveBatchFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.SaveFleeingFormDraftRequest;
import com.yiling.sjms.flee.dto.request.SaveFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.SaveFleeingGoodsRequest;
import com.yiling.sjms.flee.dto.request.SubmitFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.UpdateFleeingGoodsFormRequest;
import com.yiling.sjms.flee.entity.FleeingGoodsFormDO;
import com.yiling.sjms.flee.entity.FleeingGoodsFormExtDO;
import com.yiling.sjms.flee.enums.FleeingConfirmStatusEnum;
import com.yiling.sjms.flee.enums.FleeingImportFileTypeEnum;
import com.yiling.sjms.flee.service.FleeingGoodsFormExtService;
import com.yiling.sjms.flee.service.FleeingGoodsFormService;
import com.yiling.sjms.form.FleeFormErrorCode;
import com.yiling.sjms.form.dto.request.CreateFormRequest;
import com.yiling.sjms.form.entity.FormDO;
import com.yiling.sjms.form.enums.FormNoEnum;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.form.service.FormService;
import com.yiling.sjms.gb.service.NoService;
import com.yiling.sjms.util.OaTodoUtils;
import com.yiling.sjms.workflow.WorkFlowService;
import com.yiling.sjms.workflow.dto.request.WorkFlowBaseRequest;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 窜货申报表单 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2023-03-10
 */
@Slf4j
@Service
@RefreshScope
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FleeingGoodsFormServiceImpl extends BaseServiceImpl<FleeingGoodsFormMapper, FleeingGoodsFormDO> implements FleeingGoodsFormService {

    @DubboReference
    EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    FlowMonthWashControlApi flowMonthWashControlApi;

    @DubboReference
    FlowMonthWashTaskApi flowMonthWashTaskApi;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @DubboReference
    FlowFleeingGoodsApi flowFleeingGoodsApi;

    private final FormService formService;

    private final NoService noService;

    private final FleeingGoodsFormExtService fleeingGoodsFormExtService;

    private final WorkFlowService workFlowService;

    @Value("#{${common.gb.process-parameter}}")
    Map<String, String> process;

    @Override
    public Page<FleeingFormBO> pageForm(QueryFleeingFormPageRequest request) {
        return this.getBaseMapper().pageForm(new Page<>(request.getCurrent(), request.getSize()), request);
    }

    @Override
    public List<FleeingGoodsFormDO> pageList(QueryFleeingGoodsFormListRequest request) {
        LambdaQueryWrapper<FleeingGoodsFormDO> wrapper = Wrappers.lambdaQuery();
        if (Objects.nonNull(request.getFormId()) && 0L != request.getFormId()) {
            wrapper.eq(FleeingGoodsFormDO::getFormId, request.getFormId());
        }
        if (Objects.nonNull(request.getImportFileType()) && 0 != request.getImportFileType()) {
            wrapper.eq(FleeingGoodsFormDO::getImportFileType, request.getImportFileType());
        }
        if (Objects.nonNull(request.getCheckStatus()) && 0 != request.getCheckStatus()) {
            wrapper.eq(FleeingGoodsFormDO::getCheckStatus, request.getCheckStatus());
        }
        if (Objects.nonNull(request.getImportStatus()) && 0 != request.getImportStatus()) {
            wrapper.eq(FleeingGoodsFormDO::getImportStatus, request.getImportStatus());
        }
        wrapper.orderByDesc(FleeingGoodsFormDO::getId);
        return this.list(wrapper);
    }

    @Override
    public FleeingGoodsFormDO getFirst(QueryFleeingGoodsFormListRequest request) {
        LambdaQueryWrapper<FleeingGoodsFormDO> wrapper = Wrappers.lambdaQuery();
        if (Objects.nonNull(request.getFormId()) && 0L != request.getFormId()) {
            wrapper.eq(FleeingGoodsFormDO::getFormId, request.getFormId());
        }
        wrapper.last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public FleeingGoodsFormDO getByFileName(String fileName) {
        LambdaQueryWrapper<FleeingGoodsFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(FleeingGoodsFormDO::getFileName, fileName);
        wrapper.eq(FleeingGoodsFormDO::getImportFileType, 2);
        wrapper.eq(FleeingGoodsFormDO::getImportStatus, 1);
        wrapper.last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public FleeingGoodsFormDO getByTaskId(Long taskId) {
        LambdaQueryWrapper<FleeingGoodsFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(FleeingGoodsFormDO::getTaskId, taskId);
        wrapper.last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public FleeingGoodsFormDO queryByFileName(String fileName) {
        LambdaQueryWrapper<FleeingGoodsFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(FleeingGoodsFormDO::getFileName, fileName);
        wrapper.eq(FleeingGoodsFormDO::getImportStatus, 1);
        wrapper.eq(FleeingGoodsFormDO::getImportFileType, FleeingImportFileTypeEnum.FLEEING_CONFIRM.getCode());
        wrapper.last("limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public Long saveBatchUpload(SaveBatchFleeingGoodsFormRequest request) {
        Long formId = request.getFormId();
        Integer importFileType = request.getImportFileType();
        if (1 == importFileType && (Objects.isNull(formId) || 0L == formId)) {
            CreateFormRequest createFormRequest = new CreateFormRequest();
            createFormRequest.setOpUserId(request.getOpUserId());
            createFormRequest.setOpTime(request.getOpTime());
            createFormRequest.setCode(noService.genNo(FormNoEnum.GOODS_FLEEING));
            createFormRequest.setType(FormTypeEnum.GOODS_FLEEING.getCode());
            createFormRequest.setName(OaTodoUtils.genTitle(FormTypeEnum.GOODS_FLEEING.getName(), request.getEmpName(), createFormRequest.getCode(), new Date()));
            formId = formService.create(createFormRequest);

            FleeingGoodsFormExtDO goodsFormExtDO = PojoUtils.map(request, FleeingGoodsFormExtDO.class);
            goodsFormExtDO.setFormId(formId);
            goodsFormExtDO.setConfirmStatus(FleeingConfirmStatusEnum.TO_BE_SUBMIT.getCode());
            fleeingGoodsFormExtService.save(goodsFormExtDO);
        }

        List<FleeingGoodsFormDO> addDOList = new ArrayList<>();
        List<SaveFleeingGoodsRequest> saveFleeingGoodsList = request.getSaveFleeingGoodsList();
        for (SaveFleeingGoodsRequest saveFleeingGoodsRequest : saveFleeingGoodsList) {
            FleeingGoodsFormDO fleeingGoodsFormDO = PojoUtils.map(saveFleeingGoodsRequest, FleeingGoodsFormDO.class);
            fleeingGoodsFormDO.setOpUserId(request.getOpUserId());
            fleeingGoodsFormDO.setImportUser(request.getEmpId());
            fleeingGoodsFormDO.setImportTime(request.getOpTime());
            fleeingGoodsFormDO.setFormId(formId);
            fleeingGoodsFormDO.setCheckStatus(1);
            fleeingGoodsFormDO.setImportStatus(1);
            fleeingGoodsFormDO.setImportFileType(importFileType);
            fleeingGoodsFormDO.setFileName(saveFleeingGoodsRequest.getName());
            fleeingGoodsFormDO.setFileUrl(saveFleeingGoodsRequest.getKey());
            addDOList.add(fleeingGoodsFormDO);
        }
        if (CollUtil.isNotEmpty(addDOList)) {
            this.saveBatch(addDOList);
        }
        return formId;
    }

    @Override
    public Long saveUpload(SaveFleeingGoodsFormRequest request) {
        FleeingGoodsFormDO fleeingGoodsFormDO = PojoUtils.map(request, FleeingGoodsFormDO.class);
        if (Objects.isNull(fleeingGoodsFormDO)) {
            throw new BusinessException(FleeFormErrorCode.REQUEST_NOT_NULL);
        }

        if (1 == request.getImportFileType() && (Objects.isNull(request.getFormId()) || 0L == request.getFormId())) {
            CreateFormRequest createFormRequest = new CreateFormRequest();
            createFormRequest.setOpUserId(request.getOpUserId());
            createFormRequest.setOpTime(request.getOpTime());
            createFormRequest.setCode(noService.genNo(FormNoEnum.GOODS_FLEEING));
            createFormRequest.setType(FormTypeEnum.GOODS_FLEEING.getCode());
            createFormRequest.setName(OaTodoUtils.genTitle(FormTypeEnum.GOODS_FLEEING.getName(), request.getEmpName(), createFormRequest.getCode(), new Date()));
            Long formId = formService.create(createFormRequest);
            fleeingGoodsFormDO.setFormId(formId);

            FleeingGoodsFormExtDO goodsFormExtDO = PojoUtils.map(request, FleeingGoodsFormExtDO.class);
            goodsFormExtDO.setFormId(formId);
            goodsFormExtDO.setConfirmStatus(FleeingConfirmStatusEnum.TO_BE_SUBMIT.getCode());
            fleeingGoodsFormExtService.save(goodsFormExtDO);
        }

        this.saveOrUpdate(fleeingGoodsFormDO);

        return fleeingGoodsFormDO.getFormId();
    }

    @Override
    public Long saveUploadRecord(SaveFleeingGoodsFormRequest request) {
        FleeingGoodsFormDO fleeingGoodsFormDO = PojoUtils.map(request, FleeingGoodsFormDO.class);
        this.saveOrUpdate(fleeingGoodsFormDO);
        return fleeingGoodsFormDO.getId();
    }

    @Override
    public boolean updateUploadRecord(UpdateFleeingGoodsFormRequest request) {
        FleeingGoodsFormDO fleeingGoodsFormDO = PojoUtils.map(request, FleeingGoodsFormDO.class);
        return this.updateById(fleeingGoodsFormDO);
    }

    @Override
    public Long saveDraft(SaveFleeingFormDraftRequest request) {
        if (1 == request.getImportFileType() && (Objects.isNull(request.getFormId()) || 0L == request.getFormId())) {
            CreateFormRequest createFormRequest = new CreateFormRequest();
            createFormRequest.setOpUserId(request.getOpUserId());
            createFormRequest.setOpTime(request.getOpTime());
            createFormRequest.setCode(noService.genNo(FormNoEnum.GOODS_FLEEING));
            createFormRequest.setType(FormTypeEnum.GOODS_FLEEING.getCode());
            createFormRequest.setName(OaTodoUtils.genTitle(FormTypeEnum.GOODS_FLEEING.getName(), request.getEmpName(), createFormRequest.getCode(), new Date()));
            Long formId = formService.create(createFormRequest);
            request.setFormId(formId);
        }

        FleeingGoodsFormExtDO goodsFormExtDO = PojoUtils.map(request, FleeingGoodsFormExtDO.class);
        goodsFormExtDO.setFleeingDescribe(request.getDescribe());
        FleeingGoodsFormExtDO fleeingGoodsFormExtDO = fleeingGoodsFormExtService.queryExtByFormId(request.getFormId());
        if (Objects.isNull(fleeingGoodsFormExtDO)) {
            goodsFormExtDO.setConfirmStatus(FleeingConfirmStatusEnum.TO_BE_SUBMIT.getCode());
        } else {
            goodsFormExtDO.setId(fleeingGoodsFormExtDO.getId());
        }
        fleeingGoodsFormExtService.saveOrUpdate(goodsFormExtDO);
        return goodsFormExtDO.getFormId();
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
        String procDefId = process.get("goodsFleeing");
        String tag = Constants.TAG_DISPLACE_GOODS_CHANGE;

        var.put(ApproveConstant.START_USER_ID, esbEmployeeDTO.getEmpId());

        if (formDO.getStatus().equals(FormStatusEnum.UNSUBMIT.getCode())) {
            WorkFlowBaseRequest workFlowBaseRequest = new WorkFlowBaseRequest();
            // 从配置文件获取ProcDefId
            workFlowBaseRequest.setBusinessKey(formDO.getCode());
            workFlowBaseRequest.setEmpName(esbEmployeeDTO.getEmpName());
            workFlowBaseRequest.setStartUserId(request.getEmpId());

            workFlowBaseRequest.setFormType(request.getFormTypeEnum().getCode());
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
    public boolean removeById(RemoveFleeingGoodsFormRequest request) {
        if (Objects.isNull(request.getId())) {
            return false;
        }
        FleeingGoodsFormDO fleeingGoodsFormDO = PojoUtils.map(request, FleeingGoodsFormDO.class);
        return this.deleteByIdWithFill(fleeingGoodsFormDO) > 0;
    }

    @Override
    public boolean approve(ApproveFleeingGoodsFormRequest request) {
        FleeingGoodsFormExtDO fleeingGoodsFormExtDO = fleeingGoodsFormExtService.queryExtByFormId(request.getId());

        FleeingGoodsFormExtDO updateDO = new FleeingGoodsFormExtDO();
        updateDO.setId(fleeingGoodsFormExtDO.getId());
        updateDO.setConfirmStatus(1);
        fleeingGoodsFormExtService.updateById(updateDO);

        return true;
    }

    @Override
    public String checkUploadFileName(String fileName) {
        // IM_HHN099_濮阳九州通医药有限公司_20221231.csv
        // IM_200099_濮阳九州通医药有限公司_20221231_窜货申报确认.xlsx
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
        Long crmEnterpriseId = null;
        try {
            crmEnterpriseId = Long.parseLong(fileNameArr[1]);
        } catch (Exception e) {
            return "文件名称中的经销商编码不存在";
        }

        if (0L == crmEnterpriseId) {
            return "文件名称中的经销商编码不存在";
        }

        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(crmEnterpriseId);
        if (Objects.isNull(crmEnterpriseDTO)) {
            return "文件名称中的经销商编码不存在";
        }
        //        if (!crmEnterpriseDTO.getName().equalsIgnoreCase(fileNameArr[2])) {
        //            return "未按照模板文件名称进行命名";
        //        }
        String[] dateArr = fileNameArr[3].split("\\.");
        try {
            if (dateArr[0].length() > 8) {
                return "未按照模板文件名称进行命名";
            }
            DateTime date = DateUtil.parse(dateArr[0], "yyyyMMdd");
            //                    FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getFlowCrossStatus();
            //                    if (Objects.nonNull(flowMonthWashControlDTO) && (flowMonthWashControlDTO.getYear() != date.year() || flowMonthWashControlDTO.getMonth() != (date.month() + 1))) {
            //                        return "文件名称年月与当前流向收集年月不匹配";
            //                    }
        } catch (Exception e) {
            return "未按照模板文件名称进行命名";
        }

        if (!fileNameArr[4].startsWith("窜货申报确认.")) {
            return "未按照模板文件名称进行命名";
        }

        // 重复校验
        FleeingGoodsFormDO fleeingGoodsFormDO = this.queryByFileName(fileName);
        if (Objects.nonNull(fleeingGoodsFormDO)) {
            return "文件名称与当前流向清洗队列中的文件名称重复";
        }
        return null;
    }

    @Override
    @GlobalTransactional
    public boolean createFleeFlowForm(CreateFleeFlowRequest request) {
        log.info("createFleeFlowForm start request:[{}]", request);
        Long formId = request.getFormId();
        FormDO formDO = formService.getById(formId);
        if (Objects.isNull(formDO)) {
            throw new BusinessException(FleeFormErrorCode.FORM_NOT_FIND);
        }
        FleeingGoodsFormExtDO fleeingGoodsFormExtDO = fleeingGoodsFormExtService.queryExtByFormId(formId);
        if (FleeingConfirmStatusEnum.SUBMITTED.getCode().equals(fleeingGoodsFormExtDO.getConfirmStatus())) {
            throw new BusinessException(FleeFormErrorCode.FORM_DETAIL_NOT_RESUBMIT);
        }
        // 查询出申诉确认的文件信息，分别处理
        QueryFleeingGoodsFormListRequest formListRequest = new QueryFleeingGoodsFormListRequest();
        formListRequest.setFormId(request.getFormId());
        formListRequest.setImportFileType(FleeingImportFileTypeEnum.FLEEING_CONFIRM.getCode());
        List<FleeingGoodsFormDO> fleeingGoodsFormDOList = this.pageList(formListRequest);
        if (CollUtil.isEmpty(fleeingGoodsFormDOList)) {
            throw new BusinessException(FleeFormErrorCode.FORM_DETAIL_ALL_RIGHT);
        }
        List<FleeingGoodsFormDO> failFleeingGoodsFormDOList = fleeingGoodsFormDOList.stream().filter(e -> !e.getCheckStatus().equals(FlowMonthUploadCheckStatusEnum.PASS.getCode()) || !e.getImportStatus().equals(FlowMonthUploadImportStatusEnum.SUCCESS.getCode())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(failFleeingGoodsFormDOList)) {
            throw new BusinessException(FleeFormErrorCode.FORM_DETAIL_ALL_RIGHT);
        }

        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getWashStatus();
        if (Objects.isNull(flowMonthWashControlDTO)) {
            throw new BusinessException(FleeFormErrorCode.FORM_DETAIL_IS_CLOSE);
        }

        for (FleeingGoodsFormDO fleeingGoodsFormDO : fleeingGoodsFormDOList) {
            String crmEnterpriseIdStr = fleeingGoodsFormDO.getFileName().split("_")[1];
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(Long.parseLong(crmEnterpriseIdStr));

            SaveFlowMonthWashTaskRequest taskRequest = new SaveFlowMonthWashTaskRequest();
            taskRequest.setCrmEnterpriseId(crmEnterpriseDTO.getId());
            taskRequest.setFmwcId(flowMonthWashControlDTO.getId());
            taskRequest.setCollectionMethod(CollectionMethodEnum.EXCEL.getCode());
            taskRequest.setFlowClassify(FlowClassifyEnum.FLOW_CROSS.getCode());
            taskRequest.setFlowType(FlowTypeEnum.SALE.getCode());
            taskRequest.setFileName(fleeingGoodsFormDO.getFileName());
            taskRequest.setOpUserId(request.getOpUserId());

            long taskId = flowMonthWashTaskApi.create(taskRequest, false);

            // 新增任务id
            FleeingGoodsFormDO updateFleeingGoods = new FleeingGoodsFormDO();
            updateFleeingGoods.setId(fleeingGoodsFormDO.getId());
            updateFleeingGoods.setTaskId(taskId);
            updateFleeingGoods.setOpUserId(request.getOpUserId());
            this.updateById(updateFleeingGoods);

            // 新增任务id
            flowFleeingGoodsApi.updateTaskIdByRecordId(fleeingGoodsFormDO.getId(), taskId, request.getOpUserId());

            // 发送MQ通知
            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_FLOW_WASH_TASK, Constants.TAG_FLOW_WASH_TASK, Long.toString(taskId));
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
            if (mqMessageBO != null) {
                mqMessageSendApi.send(mqMessageBO);
            }
        }

        FleeingGoodsFormExtDO updateExtDO = new FleeingGoodsFormExtDO();
        updateExtDO.setId(fleeingGoodsFormExtDO.getId());
        updateExtDO.setConfirmStatus(FleeingConfirmStatusEnum.SUBMITTED.getCode());
        updateExtDO.setConfirmDescribe(request.getConfirmDescribe());
        updateExtDO.setConfirmUserId(request.getEmpId());
        updateExtDO.setSubmitWashTime(request.getOpTime());
        updateExtDO.setOpUserId(request.getOpUserId());
        updateExtDO.setOpTime(request.getOpTime());
        fleeingGoodsFormExtService.updateById(updateExtDO);
        log.info("createFleeFlowForm end");
        return true;
    }

    private Integer getDataType(String fileName) {
        String[] fileNameArr = fileName.split("_");
        String code = fileNameArr[0].toUpperCase();
        int dataType = 0;
        // 月销售（SM开头）   月采购（PM开头）   月库存（IM开头）
        switch (code) {
            case "SM":
                dataType = 1;
                break;
            case "IM":
                dataType = 2;
                break;
            case "PM":
                dataType = 3;
                break;
        }
        return dataType;
    }
}
