package com.yiling.sjms.flow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.constant.ApproveConstant;
import com.yiling.sjms.flow.dao.MonthFlowFormMapper;
import com.yiling.sjms.flow.dto.MonthFlowFormDTO;
import com.yiling.sjms.flow.dto.request.DeleteFormRequest;
import com.yiling.sjms.flow.dto.request.FixMonthFlowImportMq;
import com.yiling.sjms.flow.dto.request.QueryMonthFlowFormRequest;
import com.yiling.sjms.flow.dto.request.SaveMonthFlowFormRequest;
import com.yiling.sjms.flow.dto.request.SaveSubFormRequest;
import com.yiling.sjms.flow.dto.request.SubmitMonthFlowFormRequest;
import com.yiling.sjms.flow.entity.MonthFlowExtFormDO;
import com.yiling.sjms.flow.entity.MonthFlowFormDO;
import com.yiling.sjms.flow.enums.MonthFlowErrorEnum;
import com.yiling.sjms.flow.service.MonthFlowExtFormService;
import com.yiling.sjms.flow.service.MonthFlowFormService;
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
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 补传月流向表单 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2023-06-25
 */
@RefreshScope
@Service
public class MonthFlowFormServiceImpl extends BaseServiceImpl<MonthFlowFormMapper, MonthFlowFormDO> implements MonthFlowFormService {
    @Autowired
    private NoService noService;

    @Autowired
    private FormService formService;
    @Autowired
    private MonthFlowExtFormService monthFlowExtFormService;
    @Autowired
    private WorkFlowService workFlowService;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @DubboReference
    FlowMonthWashControlApi flowMonthWashControlApi;

    @DubboReference
    EsbEmployeeApi esbEmployeeApi;
    @Value("#{${common.gb.process-parameter}}")
    Map<String, String> process;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(SaveMonthFlowFormRequest request) {
        if (CollectionUtil.isEmpty(request.getSubForms())) {
            return null;
        }
        boolean first = false;
        if (Objects.isNull(request.getFormId()) || 0L == request.getFormId()) {
            first = true;
            CreateFormRequest createFormRequest = new CreateFormRequest();
            createFormRequest.setOpUserId(request.getOpUserId());
            createFormRequest.setOpTime(request.getOpTime());
            createFormRequest.setCode(noService.genNo(FormNoEnum.FIX_MONTH_FLOW));
            createFormRequest.setType(FormTypeEnum.FIX_MONTH_FLOW.getCode());
            createFormRequest.setName(OaTodoUtils.genTitle(FormTypeEnum.FIX_MONTH_FLOW.getName(), request.getEmpName(), createFormRequest.getCode(), new Date()));
            Long formId = formService.create(createFormRequest);
            request.setFormId(formId);

            MonthFlowExtFormDO extFormDO = PojoUtils.map(request, MonthFlowExtFormDO.class);
            extFormDO.setFormId(formId);
            monthFlowExtFormService.save(extFormDO);
        }
        List<MonthFlowFormDO> objects = new ArrayList<>();
        Map<Date,Integer> monthFilter = Maps.newHashMap();
        request.getSubForms().forEach(item -> {
            MonthFlowFormDO monthFlowFormDO = PojoUtils.map(item, MonthFlowFormDO.class);

            monthFlowFormDO.setSourceUrl(item.getKey());
            monthFlowFormDO.setFormId(request.getFormId());
            monthFlowFormDO.setExcelName(item.getName());
            monthFlowFormDO.setUploader(request.getEmpId());
            monthFlowFormDO.setUploaderName(request.getEmpName());
            String[] fileNameArr = item.getName().split("_");
            DateTime month = DateUtil.parse(fileNameArr[3], "yyyyMMdd");
            monthFlowFormDO.setAppealMonth(month);
            monthFilter.put(month,1);
            if(monthFilter.size()>1){
                throw new BusinessException(MonthFlowErrorEnum.MONTH_NOT_EQUAL);
            }
            monthFlowFormDO.setUploadTime(request.getOpTime());
            if (StringUtils.isNotEmpty(item.getName()) && item.getName().toLowerCase().startsWith("sm")) {
                monthFlowFormDO.setDataType(2);
            }
            if (StringUtils.isNotEmpty(item.getName()) && item.getName().toLowerCase().startsWith("pm")) {
                monthFlowFormDO.setDataType(1);
            }
            if (StringUtils.isNotEmpty(item.getName()) && item.getName().toLowerCase().startsWith("im")) {
                monthFlowFormDO.setDataType(3);
            }
            objects.add(monthFlowFormDO);
        });
        if(!first){
            LambdaQueryWrapper<MonthFlowFormDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(MonthFlowFormDO::getFormId,request.getFormId()).last("limit 1");
            MonthFlowFormDO one = this.getOne(wrapper);
            if(Objects.nonNull(one)){
                List<MonthFlowFormDO> collect = objects.stream().filter(m -> one.getAppealMonth().compareTo(m.getAppealMonth()) != 0).collect(Collectors.toList());
                if(CollUtil.isNotEmpty(collect)){
                    throw new BusinessException(MonthFlowErrorEnum.MONTH_NOT_EQUAL);
                }
            }
        }
        this.saveBatch(objects);
        return request.getFormId();
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submit(SubmitMonthFlowFormRequest request) {
        MonthFlowExtFormDO monthFlowExtFormDO = PojoUtils.map(request, MonthFlowExtFormDO.class);
        if (CollectionUtil.isNotEmpty(request.getAppendixList())) {
            monthFlowExtFormDO.setAppendix(JSON.toJSONString(request.getAppendixList()));
        }
        MonthFlowExtFormDO extFormDO = monthFlowExtFormService.getByFormId(request.getFormId());
        // 保存或更新拓展表数据
        monthFlowExtFormDO.setId(extFormDO.getId());
        monthFlowExtFormService.updateById(monthFlowExtFormDO);
        FormDO formDO = formService.getById(request.getFormId());
        if (ObjectUtil.isNull(formDO)) {
            throw new BusinessException(FleeFormErrorCode.FORM_NOT_FIND);
        }
        LambdaQueryWrapper<MonthFlowFormDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MonthFlowFormDO::getFormId,request.getFormId());
        List<MonthFlowFormDO> monthFlowFormDOS = this.list(queryWrapper);
        boolean present = monthFlowFormDOS.stream().filter(m -> m.getCheckStatus() > 1 || m.getImportStatus()>1).findAny().isPresent();
        if(present){
            throw  new BusinessException(MonthFlowErrorEnum.DATA_CONTAIN_ILLEGAL);
        }
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(request.getEmpId());
        if (ObjectUtil.isNull(esbEmployeeDTO)) {
            throw new BusinessException(FleeFormErrorCode.START_USER_NOT_FIND);
        }
        //提交审批流程
        Map<String, Object> var = Maps.newHashMap();
        String procDefId = process.get("fixMonthFlow");
        String tag = Constants.TAG_FIX_MONTH_FLOW;

        var.put(ApproveConstant.START_USER_ID, esbEmployeeDTO.getEmpId());
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getWashStatus();
        if(Objects.isNull(flowMonthWashControlDTO)){
            throw new BusinessException(MonthFlowErrorEnum.WASH_CONTRO_CLOSED);
        }
        if (formDO.getStatus().equals(FormStatusEnum.UNSUBMIT.getCode())) {
            WorkFlowBaseRequest workFlowBaseRequest = new WorkFlowBaseRequest();
            workFlowBaseRequest.setBusinessKey(formDO.getCode());
            workFlowBaseRequest.setEmpName(esbEmployeeDTO.getEmpName());
            workFlowBaseRequest.setStartUserId(request.getEmpId());
            workFlowBaseRequest.setFormType(formDO.getType());
            workFlowBaseRequest.setTitle(formDO.getName());
            workFlowBaseRequest.setId(formDO.getId());
            // 当月流向自动审批

            if(Objects.nonNull(flowMonthWashControlDTO)){
                Integer month = flowMonthWashControlDTO.getMonth();
                Integer year = flowMonthWashControlDTO.getYear();
                LambdaQueryWrapper<MonthFlowFormDO> wrapper = Wrappers.lambdaQuery();
                wrapper.eq(MonthFlowFormDO::getCheckStatus,1);
                wrapper.eq(MonthFlowFormDO::getFormId,request.getFormId()).last("limit 1");
                MonthFlowFormDO one = this.getOne(wrapper);
                if((one.getAppealMonth().getMonth()+1)==month && year==(one.getAppealMonth().getYear()+1900)){
                   // 自动审批
                    var.put(ApproveConstant.AUTO_FINISH,true);
                }
            }
            workFlowBaseRequest.setVariables(var);
            workFlowBaseRequest.setProcDefId(procDefId);
            workFlowBaseRequest.setTag(tag);
            //首次提交
            workFlowService.sendSubmitMsg(workFlowBaseRequest);
        } else {
            WorkFlowBaseRequest updateRequest = new WorkFlowBaseRequest();
            updateRequest.setId(formDO.getId());
            updateRequest.setTag(tag);
            updateRequest.setFormType(formDO.getType());
            updateRequest.setVariables(var);
            //驳回重新提交 var变量变更
            workFlowService.resubmitMsg(updateRequest);
        }
    }

    @Override
    public List<MonthFlowFormDTO> list(QueryMonthFlowFormRequest request) {
        LambdaQueryWrapper<MonthFlowFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MonthFlowFormDO::getFormId,request.getFormId());
        wrapper.eq(Objects.nonNull(request.getCheckStatus()),MonthFlowFormDO::getCheckStatus,request.getCheckStatus());
        List<MonthFlowFormDO> monthFlowFormDOS = this.list(wrapper);
        return PojoUtils.map(monthFlowFormDOS,MonthFlowFormDTO.class);
    }

    @Override
    public void deleteById(DeleteFormRequest request) {
        // 流程发起后不允许删除
        MonthFlowFormDO changeFormDO = new MonthFlowFormDO();
        MonthFlowFormDO monthFlowFormDO = this.getById(request.getId());
        if(Objects.isNull(monthFlowFormDO)){
            return;
        }
        FormDO formDO = formService.getById(monthFlowFormDO.getFormId());
        if(formDO.getStatus().equals(FormStatusEnum.APPROVE.getCode()) || formDO.getStatus().equals(FormStatusEnum.AUDITING.getCode())){
            return;
        }
        PojoUtils.map(request,changeFormDO);
        this.deleteByIdWithFill(changeFormDO);
    }

    @Override
    public MonthFlowFormDTO getByRecordId(Long recordId) {
        LambdaQueryWrapper<MonthFlowFormDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MonthFlowFormDO::getTaskId, recordId);
        wrapper.last("limit 1");
        return PojoUtils.map(this.getOne(wrapper), MonthFlowFormDTO.class);
    }

    @Override
    public void updateFlowMonthRecord(SaveSubFormRequest request) {
        MonthFlowFormDO uploadRecordDO = PojoUtils.map(request, MonthFlowFormDO.class);
        this.updateById(uploadRecordDO);
    }

    @Override
    public void approveTo(Long formId) {
        LambdaQueryWrapper<MonthFlowFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MonthFlowFormDO::getFormId,formId).eq(MonthFlowFormDO::getCheckStatus, 1);
        List<MonthFlowFormDO> formDOS = this.list(wrapper);
        if(CollUtil.isEmpty(formDOS)){
            return ;
        }
        formDOS.forEach(f->{
            FixMonthFlowImportMq fixMonthFlowImportMq = new FixMonthFlowImportMq();
            fixMonthFlowImportMq.setFileKey(f.getSourceUrl()).setFileName(f.getExcelName()).setMonthFlowFormId(f.getId()).setOpUserId(f.getCreateUser());
            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_IMPORT_FIX_MONTH_FLOW,"",JSON.toJSONString(fixMonthFlowImportMq) );
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
            mqMessageSendApi.send(mqMessageBO);
        });
    }

    @Override
    public Boolean getByFileName(String fileName) {
        LambdaQueryWrapper<MonthFlowFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MonthFlowFormDO::getExcelName,fileName);
        List<MonthFlowFormDO> monthFlowFormDOS = this.list(wrapper);
        if(CollUtil.isEmpty(monthFlowFormDOS)){
            return false;
        }
        List<FormDO> formDOS = formService.listByIds(monthFlowFormDOS.stream().map(MonthFlowFormDO::getFormId).distinct().collect(Collectors.toList()));
        boolean present = formDOS.stream().filter(f -> !f.getStatus().equals(FormStatusEnum.REJECT.getCode())).findAny().isPresent();
        return present;
    }
}
