package com.yiling.sjms.crm.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.dataflow.crm.api.CrmHospitalDrugstoreRelationApi;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmHospitalDrugstoreRelRequest;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.dto.request.QueryHosDruRelPageListRequest;
import com.yiling.sjms.constant.ApproveConstant;
import com.yiling.sjms.crm.bo.HospitalDrugstoreRelAppendixBO;
import com.yiling.sjms.crm.dao.HospitalDrugstoreRelationFormMapper;
import com.yiling.sjms.crm.dto.request.DeleteHospitalDrugstoreRelFormRequest;
import com.yiling.sjms.crm.dto.request.SaveOrUpdateHosDruRelAppendixListRequest;
import com.yiling.sjms.crm.dto.request.SaveOrUpdateHospitalDrugstoreRelFormRequest;
import com.yiling.sjms.crm.dto.request.SubmitHosDruRelRequest;
import com.yiling.sjms.crm.entity.HospitalDrugstoreRelationExtFormDO;
import com.yiling.sjms.crm.entity.HospitalDrugstoreRelationFormDO;
import com.yiling.sjms.crm.service.HospitalDrugstoreRelationExtFormService;
import com.yiling.sjms.crm.service.HospitalDrugstoreRelationFormService;
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

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;

/**
 * <p>
 * 院外药店关系流程表单明细表 服务实现类
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-06-07
 */
@RefreshScope
@Service
public class HospitalDrugstoreRelationFormServiceImpl extends BaseServiceImpl<HospitalDrugstoreRelationFormMapper, HospitalDrugstoreRelationFormDO> implements HospitalDrugstoreRelationFormService {

    @Autowired
    private FormService formService;

    @Autowired
    private HospitalDrugstoreRelationExtFormService hospitalDrugstoreRelationExtFormService;

    @Autowired
    private WorkFlowService workFlowService;

    @Autowired
    private NoService noService;

    @DubboReference(timeout = 10 * 1000)
    private EsbEmployeeApi esbEmployeeApi;

    @DubboReference(timeout = 5 * 1000)
    private CrmHospitalDrugstoreRelationApi crmHospitalDrugstoreRelationApi;

    @Value("#{${common.gb.process-parameter}}")
    Map<String, String> process;

    @Override
    public Long saveHospitalDrugstoreRelationForm(SaveOrUpdateHospitalDrugstoreRelFormRequest request) {
        if (Objects.isNull(request.getFormId()) || 0L == request.getFormId()) {
            // 如果formId已经存在，则说明是第一个表单，需要新增流程信息
            Long formId = createForm(request.getFormTypeEnum(), request.getEmpName(), request.getOpUserId(), request.getOpTime());
            request.setFormId(formId);
        }

        HospitalDrugstoreRelationFormDO hospitalDrugstoreRelationFormDO = PojoUtils.map(request, HospitalDrugstoreRelationFormDO.class);
        this.saveOrUpdate(hospitalDrugstoreRelationFormDO);
        return hospitalDrugstoreRelationFormDO.getFormId();
    }

    @Override
    public void removeById(DeleteHospitalDrugstoreRelFormRequest request) {
        HospitalDrugstoreRelationFormDO hospitalDrugstoreRelationFormDO = PojoUtils.map(request, HospitalDrugstoreRelationFormDO.class);
        this.deleteByIdWithFill(hospitalDrugstoreRelationFormDO);
    }

    @Override
    public Page<HospitalDrugstoreRelationFormDO> pageList(QueryHosDruRelPageListRequest request) {
        Page<HospitalDrugstoreRelationFormDO> page = new Page<>();

        LambdaQueryWrapper<HospitalDrugstoreRelationFormDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HospitalDrugstoreRelationFormDO::getFormId, request.getFormId());
        if (request.getHospitalOrgId() != null) {
            wrapper.eq(HospitalDrugstoreRelationFormDO::getHospitalOrgId, request.getHospitalOrgId());
        }
        if (request.getDrugstoreOrgId() != null) {
            wrapper.eq(HospitalDrugstoreRelationFormDO::getDrugstoreOrgId, request.getDrugstoreOrgId());
        }
        if (request.getCrmGoodsCode() != null) {
            wrapper.eq(HospitalDrugstoreRelationFormDO::getCrmGoodsCode, request.getCrmGoodsCode());
        }
        wrapper.orderByDesc(HospitalDrugstoreRelationFormDO::getCreateTime);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean submit(SubmitHosDruRelRequest request) {
        FormDO formDO = formService.getById(request.getFormId());
        if (ObjectUtil.isNull(formDO)) {
            throw new BusinessException(FleeFormErrorCode.FORM_NOT_FIND);
        }
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(request.getEmpId());
        if (ObjectUtil.isNull(esbEmployeeDTO)) {
            throw new BusinessException(FleeFormErrorCode.START_USER_NOT_FIND);
        }
        Map<String, Object> var = Maps.newHashMap();
        String procDefId = process.get("hospitalPharmacyBind");
        String tag = Constants.TAG_HOSPITAL_DRUGSTORE_RELATION_ADD;

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
    public Long saveParentAppendixForm(SaveOrUpdateHosDruRelAppendixListRequest request) {
        if (Objects.isNull(request.getFormId()) || 0L == request.getFormId()) {
            // 创建表单
            Long formId = createForm(request.getFormTypeEnum(), request.getEmpName(), request.getOpUserId(), request.getOpTime());
            request.setFormId(formId);
        }

        List<HospitalDrugstoreRelAppendixBO> list = PojoUtils.map(request.getAppendixList(), HospitalDrugstoreRelAppendixBO.class);

        HospitalDrugstoreRelationExtFormDO hospitalDrugstoreRelationExtFormDO = new HospitalDrugstoreRelationExtFormDO();
        HospitalDrugstoreRelationExtFormDO entity = hospitalDrugstoreRelationExtFormService.detailByFormId(request.getFormId());
        if (entity != null) {
            hospitalDrugstoreRelationExtFormDO = PojoUtils.map(entity, HospitalDrugstoreRelationExtFormDO.class);
        }
        hospitalDrugstoreRelationExtFormDO.setFormId(request.getFormId());
        hospitalDrugstoreRelationExtFormDO.setAppendix(JSONUtil.toJsonStr(list));
        // 添加或更新附件
        hospitalDrugstoreRelationExtFormService.saveOrUpdate(hospitalDrugstoreRelationExtFormDO);
        return hospitalDrugstoreRelationExtFormDO.getFormId();
    }

    @Override
    public List<Long> selectDrugOrgIdFormIdByHosOrgId(Long formId, Long hospitalOrgId) {
        return baseMapper.selectDrugOrgIdFormIdByHosOrgId(formId, hospitalOrgId);
    }

    @Override
    public boolean approveToAdd(Long formId) {
        LambdaQueryWrapper<HospitalDrugstoreRelationFormDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HospitalDrugstoreRelationFormDO::getFormId, formId);
        List<HospitalDrugstoreRelationFormDO> hospitalDrugstoreRelationFormDOList = this.list(wrapper);

        for (HospitalDrugstoreRelationFormDO hospitalDrugstoreRelationFormDO : hospitalDrugstoreRelationFormDOList) {
            SaveOrUpdateCrmHospitalDrugstoreRelRequest saveRequest = new SaveOrUpdateCrmHospitalDrugstoreRelRequest();
            saveRequest.setDrugstoreOrgId(hospitalDrugstoreRelationFormDO.getDrugstoreOrgId());
            saveRequest.setDrugstoreOrgName(hospitalDrugstoreRelationFormDO.getDrugstoreOrgName());
            saveRequest.setHospitalOrgId(hospitalDrugstoreRelationFormDO.getHospitalOrgId());
            saveRequest.setHospitalOrgName(hospitalDrugstoreRelationFormDO.getHospitalOrgName());
            saveRequest.setCategoryId(hospitalDrugstoreRelationFormDO.getCategoryId());
            saveRequest.setCategoryName(hospitalDrugstoreRelationFormDO.getCategoryName());
            saveRequest.setCrmGoodsCode(hospitalDrugstoreRelationFormDO.getCrmGoodsCode());
            saveRequest.setCrmGoodsName(hospitalDrugstoreRelationFormDO.getCrmGoodsName());
            saveRequest.setCrmGoodsSpec(hospitalDrugstoreRelationFormDO.getCrmGoodsSpec());
            saveRequest.setEffectStartTime(hospitalDrugstoreRelationFormDO.getEffectStartTime());
            saveRequest.setEffectEndTime(hospitalDrugstoreRelationFormDO.getEffectEndTime());
            saveRequest.setDataSource(2);
            saveRequest.setLastOpTime(DateUtil.date());
            saveRequest.setLastOpUser(hospitalDrugstoreRelationFormDO.getCreateUser());
            try {
                crmHospitalDrugstoreRelationApi.saveOrUpdate(saveRequest);
                hospitalDrugstoreRelationFormDO.setSyncData(1);
                hospitalDrugstoreRelationFormDO.setUpdateTime(DateUtil.date());
            } catch (BusinessException e) {
                hospitalDrugstoreRelationFormDO.setSyncData(0);
                hospitalDrugstoreRelationFormDO.setUpdateTime(DateUtil.date());
                hospitalDrugstoreRelationFormDO.setSyncErrMsg(e.getMessage());
            } catch (Exception e) {
                hospitalDrugstoreRelationFormDO.setSyncData(0);
                hospitalDrugstoreRelationFormDO.setUpdateTime(DateUtil.date());
                hospitalDrugstoreRelationFormDO.setSyncErrMsg(ExceptionUtil.stacktraceToOneLineString(e, 1000));
            }
        }
        // 修改同步状态
        this.updateBatchById(hospitalDrugstoreRelationFormDOList);

        return true;
    }

    private Long createForm(FormTypeEnum formTypeEnum, String empName, Long opUserId, Date opTime) {
        // 如果formId已经存在，则说明是第一个表单，需要新增流程信息
        CreateFormRequest createFormRequest = new CreateFormRequest();
        createFormRequest.setOpUserId(opUserId);
        createFormRequest.setOpTime(opTime);

        createFormRequest.setCode(noService.genNo(FormNoEnum.HOSPITAL_DRUGSTORE_RELATION_ADD));
        createFormRequest.setType(formTypeEnum.getCode());
        createFormRequest.setName(OaTodoUtils.genTitle(formTypeEnum.getName(), empName, createFormRequest.getCode(), new Date()));
        Long formId = formService.create(createFormRequest);
        return formId;
    }
}
