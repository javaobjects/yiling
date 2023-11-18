package com.yiling.sjms.agency.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.dataflow.agency.api.CrmHosptialApi;
import com.yiling.dataflow.agency.api.CrmPharmacyApi;
import com.yiling.dataflow.agency.api.CrmSupplierApi;
import com.yiling.dataflow.agency.dto.request.UpdateCrmHospitalRequest;
import com.yiling.dataflow.agency.dto.request.UpdateCrmPharmacyRequest;
import com.yiling.dataflow.agency.dto.request.UpdateCrmSupplierRequest;
import com.yiling.dataflow.agency.enums.CrmPharmacyAttributeEnum;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.dto.request.PermitAgencyLockApplyRequest;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.dao.AgencyLockFormMapper;
import com.yiling.sjms.agency.dto.AgencyLockFormDTO;
import com.yiling.sjms.agency.dto.AgencyLockRelationShipDTO;
import com.yiling.sjms.agency.dto.request.ApproveAgencyLockFormRequest;
import com.yiling.sjms.agency.dto.request.ApproveToAgLockRequest;
import com.yiling.sjms.agency.dto.request.DeleteAgencyLockFormRequest;
import com.yiling.sjms.agency.dto.request.QueryAgencyLockDetailPageRequest;
import com.yiling.sjms.agency.dto.request.QueryAgencyLockFormPageRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyLockFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyLockRequest;
import com.yiling.sjms.agency.dto.request.UpdateAgencyLockArchiveRequest;
import com.yiling.sjms.agency.entity.AgencyLockFormDO;
import com.yiling.sjms.agency.entity.AgencyLockRelationShipDO;
import com.yiling.sjms.agency.enums.AgencyLockBusinessTypeEnum;
import com.yiling.sjms.agency.enums.AgencyLockErrorCode;
import com.yiling.sjms.agency.enums.ArchiveStatusEnum;
import com.yiling.sjms.agency.service.AgencyLockFormService;
import com.yiling.sjms.agency.service.AgencyLockRelationShipService;
import com.yiling.sjms.constant.ApproveConstant;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.form.dto.request.CreateFormRequest;
import com.yiling.sjms.form.entity.FormDO;
import com.yiling.sjms.form.enums.FormNoEnum;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.form.service.FormService;
import com.yiling.sjms.gb.api.GbWorkflowProcessorApi;
import com.yiling.sjms.gb.service.BusinessDepartmentService;
import com.yiling.sjms.gb.service.NoService;
import com.yiling.sjms.sjsp.api.DataApproveDeptApi;
import com.yiling.sjms.sjsp.dto.DataApproveDeptDTO;
import com.yiling.sjms.sjsp.enums.DataApprovePlateEnum;
import com.yiling.sjms.sjsp.enums.SjshErrorCode;
import com.yiling.sjms.util.OaTodoUtils;
import com.yiling.sjms.workflow.WorkFlowService;
import com.yiling.sjms.workflow.dto.request.SubmitFormBaseRequest;
import com.yiling.sjms.workflow.dto.request.WorkFlowBaseRequest;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.bo.SimpleEsbEmployeeInfoBO;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.esb.dto.request.QueryProvinceManagerRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 机构锁定信息表 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2023-02-22
 */
@RefreshScope
@Slf4j
@Service
public class AgencyLockFormServiceImpl extends BaseServiceImpl<AgencyLockFormMapper, AgencyLockFormDO> implements AgencyLockFormService {

    @Autowired
    AgencyLockRelationShipService agencyLockRelationShipService;

    @Autowired
    private FormService formService;

    @Autowired
    private NoService noService;

    @Autowired
    private WorkFlowService workFlowService;

    @Autowired
    private BusinessDepartmentService businessDepartmentService;

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;
    @DubboReference
    private CrmSupplierApi crmSupplierApi;
    @DubboReference
    private CrmPharmacyApi crmPharmacyApi;
    @DubboReference
    private CrmHosptialApi crmHosptialApi;
    @DubboReference
    private CrmEnterpriseApi crmEnterpriseApi;
    @DubboReference
    private CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;
    @DubboReference
    private GbWorkflowProcessorApi gbWorkflowProcessorApi;
    @DubboReference
    DataApproveDeptApi dataApproveDeptApi;
    @DubboReference
    private FormApi formApi;

    @Value("#{${common.gb.process-parameter}}")
    Map<String, String> process ;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(SaveAgencyLockFormRequest request) {
        AgencyLockFormDO agencyLockFormDO = new AgencyLockFormDO();
        PojoUtils.map(request,agencyLockFormDO);
        //判断机构是否重复
        if (ObjectUtil.isNotNull(request.getFormId())&&ObjectUtil.notEqual(request.getFormId(), 0L)) {
            List<AgencyLockFormDO> oldAgencyLockForm;
            LambdaQueryWrapper<AgencyLockFormDO> queryLockFormWrapper = Wrappers.lambdaQuery();
            queryLockFormWrapper.eq(AgencyLockFormDO::getFormId, request.getFormId());
            if (ObjectUtil.isNotNull(request.getId()) && ObjectUtil.notEqual(request.getId(), 0L)){
                queryLockFormWrapper.ne(AgencyLockFormDO::getId, request.getId());
            }
            oldAgencyLockForm = list(queryLockFormWrapper);
            if (ObjectUtil.isNotEmpty(oldAgencyLockForm)){
                Map<Long, AgencyLockFormDO> var = oldAgencyLockForm.stream().collect(Collectors.toMap(AgencyLockFormDO::getCrmEnterpriseId, e -> e));
                if (ObjectUtil.isNotNull(var.get(request.getCrmEnterpriseId()))) {
                    log.warn("当前机构列表中存在重复的机构,fomId={}", request.getFormId());
                    throw new BusinessException(AgencyLockErrorCode.SAVE_FORM_FAIL);
                }
                if (ObjectUtil.notEqual(oldAgencyLockForm.stream().findAny().get().getSupplyChainRole(),request.getSupplyChainRole())) {
                    log.warn("表单中机构的供应链角色必须一致,fomId={},request.chainRole={}", request.getFormId(),request.getSupplyChainRole());
                    throw new BusinessException(AgencyLockErrorCode.ROLE_CHAIN_NEED_UNIQUE);
                }
            }
        }
        if(Objects.isNull(request.getFormId()) || request.getFormId()==0){
            //获取当前人的待提交的数据
            List<FormDTO> formDTOS = formApi.listUnsubmitFormsByUser(FormTypeEnum.EXTEND_INFO_CHANGE, request.getOpUserId());
            if(CollUtil.isEmpty(formDTOS)){
                CreateFormRequest createFormRequest = new CreateFormRequest();
                createFormRequest.setCode(noService.genNo(FormNoEnum.EXTEND_INFO_CHANGE)).setType(FormTypeEnum.EXTEND_INFO_CHANGE.getCode()).setRemark(request.getAdjustReason())
                        .setName(OaTodoUtils.genTitle(FormTypeEnum.EXTEND_INFO_CHANGE.getName(), request.getEmpName(), createFormRequest.getCode(), new Date())).setOpUserId(request.getOpUserId());
                Long formId = formService.create(createFormRequest);
                agencyLockFormDO.setFormId(formId);
            }else{
                agencyLockFormDO.setFormId(formDTOS.get(0).getId());
            }

        }
        this.saveOrUpdate(agencyLockFormDO);
        return agencyLockFormDO.getFormId();
    }

    @Override
    public Page<AgencyLockFormDTO> queryPage(QueryAgencyLockFormPageRequest request) {
        LambdaQueryWrapper<AgencyLockFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AgencyLockFormDO::getFormId,request.getFormId());
        //wrapper.eq(AgencyLockFormDO::getCreateUser,request.getOpUserId()).orderByDesc(AgencyLockFormDO::getId);
        Page<AgencyLockFormDO> page = this.page(request.getPage(), wrapper);
        return PojoUtils.map(page,AgencyLockFormDTO.class);
    }

    @Override
    public AgencyLockFormDTO getAgencyLockForm(Long id) {
        AgencyLockFormDO agencyLockFormDO = this.getById(id);
        return PojoUtils.map(agencyLockFormDO,AgencyLockFormDTO.class);
    }

    @Override
    public void deleteById(DeleteAgencyLockFormRequest request) {
        // 流程发起后不允许删除
        AgencyLockFormDO agencyLockFormDO = new AgencyLockFormDO();
        AgencyLockFormDO lockFormDO = this.getById(request.getId());
        if(Objects.isNull(lockFormDO)){
            return;
        }
        FormDO formDO = formService.getById(lockFormDO.getFormId());
        if(formDO.getStatus().equals(FormStatusEnum.APPROVE.getCode()) || formDO.getStatus().equals(FormStatusEnum.AUDITING.getCode())){
            return;
        }
        PojoUtils.map(request,agencyLockFormDO);
        this.deleteByIdWithFill(agencyLockFormDO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long saveAgencyLock(SaveAgencyLockRequest request) {
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(request.getEmpNo());
        //存form
        Long formId=request.getFormId();
        if (ObjectUtil.isNull(request.getId())||ObjectUtil.equal(request.getId(),0L)){
            if(Objects.isNull(request.getFormId()) || ObjectUtil.equal(request.getFormId(),0L)){
                List<FormDO> list = formService.listUnsubmitFormsByUser(FormTypeEnum.ENTERPRISE_LOCK, request.getUserId());
                if (CollUtil.isNotEmpty(list)){
                    log.warn("当前人存在草稿不能新建,fomId={}", list.stream().findAny().get().getId());
                    throw new BusinessException(AgencyLockErrorCode.ROUGH_DRAFT_HAS_EXIST);
                }
                CreateFormRequest createFormRequest = new CreateFormRequest();
                createFormRequest.setCode(noService.genNo(FormNoEnum.ENTERPRISE_LOCK));
                createFormRequest.setType(FormTypeEnum.ENTERPRISE_LOCK.getCode());
                createFormRequest.setRemark(request.getRemark());
                createFormRequest.setName(OaTodoUtils.genTitle(FormTypeEnum.ENTERPRISE_LOCK.getName(), esbEmployeeDTO.getEmpName(), createFormRequest.getCode(), new Date()));
                createFormRequest.setOpUserId(request.getOpUserId());
                formId = formService.create(createFormRequest);
            }
        }

        //存机构锁定form
        //判断机构是否重复
        if (ObjectUtil.isNotNull(request.getFormId())&&ObjectUtil.notEqual(request.getFormId(), 0L)) {
            List<AgencyLockFormDO> oldAgencyLockForm;
            LambdaQueryWrapper<AgencyLockFormDO> queryLockFormWrapper = Wrappers.lambdaQuery();
            queryLockFormWrapper.eq(AgencyLockFormDO::getFormId, request.getFormId());
            if (ObjectUtil.isNotNull(request.getId()) && ObjectUtil.notEqual(request.getId(), 0L)){
                queryLockFormWrapper.ne(AgencyLockFormDO::getId, request.getId());
            }
            oldAgencyLockForm = list(queryLockFormWrapper);
            if (ObjectUtil.isNotEmpty(oldAgencyLockForm)){
                Map<Long, AgencyLockFormDO> var = oldAgencyLockForm.stream().collect(Collectors.toMap(AgencyLockFormDO::getCrmEnterpriseId, e -> e));
                if (ObjectUtil.isNotNull(var.get(request.getCrmEnterpriseId()))) {
                    log.warn("当前机构列表中存在重复的机构,fomId={}", request.getFormId());
                    throw new BusinessException(AgencyLockErrorCode.SAVE_FORM_FAIL);
                }
                if (ObjectUtil.notEqual(oldAgencyLockForm.stream().findAny().get().getSupplyChainRole(),request.getSupplyChainRole())) {
                    log.warn("表单中机构的供应链角色必须一致,fomId={},request.chainRole={}", request.getFormId(),request.getSupplyChainRole());
                    throw new BusinessException(AgencyLockErrorCode.ROLE_CHAIN_NEED_UNIQUE);
                }
            }
        }

        //表单基本信息存盘
        AgencyLockFormDO agencyLockFormDO = PojoUtils.map(request, AgencyLockFormDO.class);
        agencyLockFormDO.setBusinessType(AgencyLockBusinessTypeEnum.LOCK.getCode());

        //如果是新增
        if (ObjectUtil.isNull(request.getFormId()) || ObjectUtil.equal(request.getFormId(), 0L)) {
            agencyLockFormDO.setFormId(formId);
        }
        boolean isSuccess = saveOrUpdate(agencyLockFormDO);
        if (!isSuccess) {
            log.error("锁定机构时保存机构锁定信息失败，参数={}", agencyLockFormDO);
            throw new ServiceException(ResultCode.FAILED);
        }
        //存三者关系
        List<AgencyLockRelationShipDO> reList = ListUtil.toList();

        //如果是修改操作则删除原来的三者关系，重新插入最新的
        if (ObjectUtil.isNotNull(request.getId()) && ObjectUtil.notEqual(request.getId(), 0L)) {
            LambdaQueryWrapper<AgencyLockRelationShipDO> queryWrapper = Wrappers.lambdaQuery();
            //机构锁定信息idList
            queryWrapper.eq(AgencyLockRelationShipDO::getAgencyFormId, request.getId());
            //当前db存在的机构三者关系
            List<AgencyLockRelationShipDO> var3 = agencyLockRelationShipService.list(queryWrapper);
            if (CollUtil.isNotEmpty(var3)) {
                LambdaQueryWrapper<AgencyLockRelationShipDO> delWrapper = Wrappers.lambdaQuery();
                delWrapper.in(AgencyLockRelationShipDO::getId, var3.stream().map(AgencyLockRelationShipDO::getId).collect(Collectors.toList()));
                AgencyLockRelationShipDO delData = new AgencyLockRelationShipDO();
                delData.setOpUserId(request.getOpUserId());
                int row = agencyLockRelationShipService.batchDeleteWithFill(delData, delWrapper);
                if (row == 0) {
                    log.error("锁定机构时删除老的三者关系信息失败，参数={}", var3);
                    throw new ServiceException(ResultCode.FAILED);
                }
            }
        }
        //为三者关系设置机构锁定信息的formId
        //如果不存在三者关系则到此结束
        if (CollUtil.isEmpty(request.getRelationList())){
            return formId;
        }

        List<AgencyLockRelationShipDO> var1 = PojoUtils.map(request.getRelationList(), AgencyLockRelationShipDO.class);
        var1.stream().forEach(e -> {
            e.setAgencyFormId(agencyLockFormDO.getId());
            e.setOptType(1);
            e.setCrmEnterpriseId(request.getCrmEnterpriseId());
        });
        reList.addAll(var1);
        //新的三者关系存盘
        isSuccess = agencyLockRelationShipService.saveBatch(reList);
        if (!isSuccess) {
            log.error("锁定机构时保存三者信息失败，参数={}", reList);
            throw new ServiceException(ResultCode.FAILED);
        }

        return formId;
    }

    @Override
    public Page<AgencyLockFormDTO> queryAgencyLockDetailPage(QueryAgencyLockDetailPageRequest request) {
        if (ObjectUtil.isNull(request.getFormId())){
            return request.getPage();
        }
        LambdaQueryWrapper<AgencyLockFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AgencyLockFormDO::getFormId,request.getFormId());
        Page<AgencyLockFormDO> page = this.page(request.getPage(), wrapper);
        return PojoUtils.map(page,AgencyLockFormDTO.class);
    }

    @Override
    public Boolean submit(SubmitFormBaseRequest request) {
        FormDO formDO = formService.getById(request.getId());
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(request.getEmpId());
        // 获取省份信息
        String province = crmEnterpriseRelationShipApi.getProvinceAreaByThreeParms(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince());
        if(Objects.isNull(province)){
            log.error("获取省区信息失败esbEmployeeDTO={}",esbEmployeeDTO.toString());
            throw new BusinessException(ResultCode.FAILED,"获取省区信息失败");
        }
        WorkFlowBaseRequest workFlowBaseRequest = new WorkFlowBaseRequest();
        // 从配置文件获取ProcDefId
        workFlowBaseRequest.setBusinessKey(formDO.getCode()).setEmpName(esbEmployeeDTO.getEmpName()).setStartUserId(request.getEmpId()).setProcDefId(process.get("agencyExtendUpdate")).setTag(Constants.TAG_EXTEND_CHANGE);
        workFlowBaseRequest.setTitle(formDO.getName()).setId(request.getId()).setFormType(FormTypeEnum.EXTEND_INFO_CHANGE.getCode());
        HashMap<String, Object> var = Maps.newHashMap();
        //查商务部对应省区负责人
        QueryProvinceManagerRequest queryMarketRequest = new QueryProvinceManagerRequest();
        queryMarketRequest.setOrgId(ApproveConstant.COMMERCE_ORG_ID).setProvinceName(province);
        SimpleEsbEmployeeInfoBO marketEmployeeInfoBO = gbWorkflowProcessorApi.getByProvinceName(queryMarketRequest);
        if(Objects.isNull(marketEmployeeInfoBO)){
            log.error("获取商务部对应省区的省区经理信息失败queryMarketRequest={}",queryMarketRequest.toString());
            throw new BusinessException(ResultCode.FAILED,"获取商务部对应省区的省区经理信息失败");
        }
        var.put(ApproveConstant.COMMERCE_PROVINCE_MANAGER,marketEmployeeInfoBO.getEmpId());
        var.put(ApproveConstant.SUPERIOR,esbEmployeeDTO.getSuperior());
        // 获取申请人对应的事业部信息
        EsbOrganizationDTO esbOrganizationDTO = businessDepartmentService.getByOrgId(esbEmployeeDTO.getDeptId());
        //事业部总监查询
        DataApproveDeptDTO dataApproveDeptByDeptId = dataApproveDeptApi.getDataApproveDeptByDeptId(esbOrganizationDTO.getOrgId().toString());
        if(Objects.isNull(dataApproveDeptByDeptId)){
            log.error("事业部总监信息失败esbEmployeeDTO.getDeptId()={}",esbEmployeeDTO.getDeptId());
            throw new BusinessException(ResultCode.FAILED,"事业部总监信息失败");
        }
        var.put(ApproveConstant.BU_DIRECTOR,dataApproveDeptByDeptId.getDepDirId());
        var.put(ApproveConstant.DIGITAL_GENERAL_MANAGER,process.get(ApproveConstant.DIGITAL_GENERAL_MANAGER));
        //数据节点根据板块指定审批人
        //查询板块信息
        DataApproveDeptDTO plate = dataApproveDeptApi.getDataApproveDeptByDeptId(String.valueOf(esbOrganizationDTO.getOrgId()));
        if (ObjectUtil.isNull(plate)) {
            log.error("板块信息不存在，esbOrganizationDTO.getOrgId(){}", esbOrganizationDTO.getOrgId());
            throw new BusinessException(SjshErrorCode.EMP_PLATE_NOT_FIND);
        }
        var.put(ApproveConstant.USER_PLATE,plate.getPlate());
        //查询对应审批人
        String approveUser = "";
        //商业
        if (ObjectUtil.equal(DataApprovePlateEnum.SUPPLIER.getCode(),plate.getPlate())){
            approveUser=process.get("dataApproveDistributorUser");
        }
        //医疗
        if (ObjectUtil.equal(DataApprovePlateEnum.HOSPITAL.getCode(),plate.getPlate())){
            approveUser=process.get("dataApproveHospitalUser");
        }
        //零售
        if (ObjectUtil.equal(DataApprovePlateEnum.PHARMACY.getCode(),plate.getPlate())){
            approveUser=process.get("dataApprovePharmacyUser");
        }
        var.put(ApproveConstant.USERS,approveUser);
        //var.put(ApproveConstant.HOM_DEPUTY_DIRECTOR,process.get(ApproveConstant.HOM_DEPUTY_DIRECTOR));
        var.put(ApproveConstant.COMMERCE_DEPT_DIRECTOR,process.get(ApproveConstant.COMMERCE_DEPT_DIRECTOR));
        var.put(ApproveConstant.BUSINESS_DATA_SECTION,process.get(ApproveConstant.BUSINESS_DATA_SECTION));
        var.put(ApproveConstant.MC_FINANCE_DIRECTOR,process.get(ApproveConstant.MC_FINANCE_DIRECTOR));
        var.put(ApproveConstant.MARKETING_COMPANY_GENERAL_MANAGER,process.get(ApproveConstant.MARKETING_COMPANY_GENERAL_MANAGER));
        var.put(ApproveConstant.CMD_GENERAL_MANAGER,process.get(ApproveConstant.CMD_GENERAL_MANAGER));
        //var.put(ApproveConstant.EXECUTIVE_DEPUTY_GENERAL_MANAGER,process.get(ApproveConstant.EXECUTIVE_DEPUTY_GENERAL_MANAGER));
        var.put(ApproveConstant.CHIEF_ADMINISTRATIVE_OPERATING_OFFICER,process.get(ApproveConstant.CHIEF_ADMINISTRATIVE_OPERATING_OFFICER));
        workFlowBaseRequest.setVariables(var);
        if(formDO.getStatus().equals(FormStatusEnum.UNSUBMIT.getCode())){
            //首次提交
            workFlowService.sendSubmitMsg(workFlowBaseRequest);
        }else if(formDO.getStatus().equals(FormStatusEnum.REJECT.getCode())){
            workFlowService.resubmitMsg(workFlowBaseRequest);
        }
        return true;
    }

    @GlobalTransactional
    @Override
    public Boolean approveToChange(ApproveAgencyLockFormRequest request) {
        LambdaQueryWrapper<AgencyLockFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AgencyLockFormDO::getFormId,request.getId()).eq(AgencyLockFormDO::getArchiveStatus, ArchiveStatusEnum.OPEN.getCode()).eq(AgencyLockFormDO::getBusinessType,2);
        List<AgencyLockFormDO> agencyLockFormDOS = this.list(wrapper);
        if(CollUtil.isEmpty(agencyLockFormDOS)){
            return false;
        }
        List<UpdateCrmSupplierRequest> supplierRequests = Lists.newArrayList();
        List<UpdateCrmPharmacyRequest> pharmacyRequests = Lists.newArrayList();
        List<UpdateCrmHospitalRequest> hospitalRequests = Lists.newArrayList();
        agencyLockFormDOS.forEach(agencyLockFormDO -> {
            //商业
            if(agencyLockFormDO.getSupplyChainRole().equals(CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode())){
                UpdateCrmSupplierRequest saveCrmSupplierRequest = new UpdateCrmSupplierRequest();
                List<String> split= Arrays.asList(agencyLockFormDO.getChangeItem().split(","));
                PojoUtils.mapByFields(agencyLockFormDO,saveCrmSupplierRequest,split);
                saveCrmSupplierRequest.setCrmEnterpriseId(agencyLockFormDO.getCrmEnterpriseId());
                if(agencyLockFormDO.getHeadChainFlag()==2){
                    saveCrmSupplierRequest.setChainAttribute(0).setChainKaFlag(0);
                }
                if(agencyLockFormDO.getHeadChainFlag()==1){
                    saveCrmSupplierRequest.setChainAttribute(agencyLockFormDO.getChainAttribute()).setChainKaFlag(agencyLockFormDO.getChainKaFlag());
                }
                supplierRequests.add(saveCrmSupplierRequest);
            }
            //零售
            if(agencyLockFormDO.getSupplyChainRole().equals(CrmSupplyChainRoleEnum.PHARMACY.getCode())){
                UpdateCrmPharmacyRequest var = new UpdateCrmPharmacyRequest();
                List<String> split= Arrays.asList(agencyLockFormDO.getChangeItem().split(","));
                PojoUtils.mapByFields(agencyLockFormDO,var,split);
                var.setCrmEnterpriseId(agencyLockFormDO.getCrmEnterpriseId());
                if(CrmPharmacyAttributeEnum.SIMPLE.getCode().equals(agencyLockFormDO.getPharmacyAttribute())){
                    var.setParentCompanyCode("").setParentCompanyName("").setChainAttribute("");
                }else{
                    var.setParentCompanyCode(agencyLockFormDO.getParentSupplierCode()).setParentCompanyName(agencyLockFormDO.getParentSupplierName()).setChainAttribute(String.valueOf(agencyLockFormDO.getChainAttribute()));
                }
                pharmacyRequests.add(var);
            }
            //医疗
            if(agencyLockFormDO.getSupplyChainRole().equals(CrmSupplyChainRoleEnum.HOSPITAL.getCode())){
                UpdateCrmHospitalRequest var = new UpdateCrmHospitalRequest();
                List<String> split= Arrays.asList(agencyLockFormDO.getChangeItem().split(","));
                PojoUtils.mapByFields(agencyLockFormDO,var,split);
                var.setCrmEnterpriseId(agencyLockFormDO.getCrmEnterpriseId());
                hospitalRequests.add(var);
            }
        });
        if(CollUtil.isNotEmpty(supplierRequests)){
            boolean isSuccess = crmSupplierApi.updateCrmSupplierBatch(supplierRequests);
            if (!isSuccess){
                log.error("机构修改审批成功后更新商业拓展信息失败，参数={}",supplierRequests);
                throw new ServiceException(ResultCode.FAILED);
            }
        }
        if(CollUtil.isNotEmpty(pharmacyRequests)){
            boolean isSuccess = crmPharmacyApi.updateCrmPharmacyBatch(pharmacyRequests);
            if (!isSuccess){
                log.error("机构修改审批成功后更新零售拓展信息失败，参数={}",pharmacyRequests);
                throw new ServiceException(ResultCode.FAILED);
            }
        }
        if(CollUtil.isNotEmpty(hospitalRequests)){
            boolean isSuccess = crmHosptialApi.updateCrmHospitalBatch(hospitalRequests);
            if (!isSuccess){
                log.error("机构修改审批成功后更新医疗拓展信息失败，参数={}",hospitalRequests);
                throw new ServiceException(ResultCode.FAILED);
            }
        }
        return true;
    }

    @Override
    public void updateArchiveStatusById(UpdateAgencyLockArchiveRequest request) {
        //判断form状态
        AgencyLockFormDO lockFormDO = getById(request.getId());
        if (ObjectUtil.isNull(lockFormDO)){
            log.warn("机构信息锁定表数据不存在，id={}",request.getId());
            throw  new BusinessException(AgencyLockErrorCode.DATA_NOT_FIND);
        }
        if (ObjectUtil.equal(lockFormDO.getArchiveStatus(),request.getArchiveStatus())){
            log.warn("当前状态与修改状态一致，无需修改，id={}",request.getId());
            throw  new ServiceException(ResultCode.FAILED);
        }
        FormDO formDO = formService.getById(lockFormDO.getFormId());

        if (ObjectUtil.isNull(formDO)){
            log.warn("更新数据归档状态时表单信息不存在，formId={}",lockFormDO.getFormId());
            throw new BusinessException(AgencyLockErrorCode.FORM_NOT_FIND);
        }
        Integer formStatus = formDO.getStatus();
        if (ObjectUtil.equal(FormStatusEnum.APPROVE.getCode(), formStatus)) {
            log.warn("当前流程状态不允许修改操作，formId={},状态={}",lockFormDO.getFormId(),FormStatusEnum.getByCode(formDO.getStatus()).getName());
            throw  new BusinessException(AgencyLockErrorCode.PROHIBIT_UPDATE);
        }
        AgencyLockFormDO aDo = PojoUtils.map(request, AgencyLockFormDO.class);
        aDo.setOpTime(new Date());
        boolean isSuccess = updateById(aDo);
        if (!isSuccess){
            log.error("更新归档状态失败，id={}",request.getId());
            throw  new ServiceException(ResultCode.FAILED);
        }
    }

    @Override
    public Boolean approveToAgLock(ApproveToAgLockRequest request) {
        LambdaQueryWrapper<AgencyLockFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AgencyLockFormDO::getFormId,request.getId());
        wrapper.eq(AgencyLockFormDO::getArchiveStatus, ArchiveStatusEnum.OPEN.getCode());
        wrapper.eq(AgencyLockFormDO::getBusinessType, AgencyLockBusinessTypeEnum.LOCK.getCode());
        List<AgencyLockFormDO> agencyLockFormList = this.list(wrapper);
        if(CollUtil.isEmpty(agencyLockFormList)){
            return true;
        }
        //查询三者关系
        List<Long> lockFormIdList = agencyLockFormList.stream().map(AgencyLockFormDO::getId).collect(Collectors.toList());
        Map<Long, List<AgencyLockRelationShipDTO>> relationMap = agencyLockRelationShipService.queryRelationListByAgencyFormId(lockFormIdList);

        List<PermitAgencyLockApplyRequest> sendPar = PojoUtils.map(agencyLockFormList, PermitAgencyLockApplyRequest.class);
        sendPar.stream().forEach(e->{
            e.setOpUserId(1L);
            e.setRelationList(PojoUtils.map(relationMap.get(e.getId()), PermitAgencyLockApplyRequest.AgencyLockRelationShipRequest.class));
            e.getRelationList().stream().forEach(s->{
                s.setOpUserId(1L);
                s.setCustomerName(e.getName());
                s.setSupplyChainRoleType(e.getSupplyChainRole());
                s.setSupplyChainRole(CrmSupplyChainRoleEnum.getFromCode(e.getSupplyChainRole()).getName());
            });
        });
        return crmEnterpriseApi.permitAgencyLockApply(sendPar);
    }

    @Override
    public Boolean submitAgencyLockForm(SubmitFormBaseRequest request) {
        FormDO formDO = formService.getById(request.getId());
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(request.getEmpId());
        // 获取省份信息
        String province = crmEnterpriseRelationShipApi.getProvinceAreaByThreeParms(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince());
        if(Objects.isNull(province)){
            log.error("获取省区信息失败esbEmployeeDTO={}",esbEmployeeDTO.toString());
            throw new BusinessException(ResultCode.FAILED,"获取省区信息失败");
        }
        WorkFlowBaseRequest workFlowBaseRequest = new WorkFlowBaseRequest();
        // 从配置文件获取ProcDefId
        workFlowBaseRequest.setBusinessKey(formDO.getCode());
        workFlowBaseRequest.setEmpName(esbEmployeeDTO.getEmpName());
        workFlowBaseRequest.setStartUserId(request.getEmpId());
        workFlowBaseRequest.setProcDefId(process.get("agencyLock"));
        workFlowBaseRequest.setTag(Constants.TAG_LOCK_AGENCY);
        workFlowBaseRequest.setTitle(formDO.getName());
        workFlowBaseRequest.setId(request.getId());
        workFlowBaseRequest.setFormType(FormTypeEnum.ENTERPRISE_LOCK.getCode());
        HashMap<String, Object> var = Maps.newHashMap();
        //查商务部对应省区负责人
        QueryProvinceManagerRequest queryMarketRequest = new QueryProvinceManagerRequest();
        queryMarketRequest.setOrgId(ApproveConstant.COMMERCE_ORG_ID).setProvinceName(province);
        SimpleEsbEmployeeInfoBO marketEmployeeInfoBO = gbWorkflowProcessorApi.getByProvinceName(queryMarketRequest);
        if(Objects.isNull(marketEmployeeInfoBO)){
            log.error("获取商务部对应省区的省区经理信息失败queryMarketRequest={}",queryMarketRequest.toString());
            throw new BusinessException(ResultCode.FAILED,"获取商务部对应省区的省区经理信息失败");
        }
        var.put(ApproveConstant.COMMERCE_PROVINCE_MANAGER,marketEmployeeInfoBO.getEmpId());
        var.put(ApproveConstant.SUPERIOR,esbEmployeeDTO.getSuperior());
        // 获取申请人对应的事业部信息
        EsbOrganizationDTO esbOrganizationDTO = businessDepartmentService.getByOrgId(esbEmployeeDTO.getDeptId());
        //事业部总监查询
        DataApproveDeptDTO dataApproveDeptByDeptId = dataApproveDeptApi.getDataApproveDeptByDeptId(esbOrganizationDTO.getOrgId().toString());
        if(Objects.isNull(dataApproveDeptByDeptId)){
            log.error("事业部总监信息失败esbEmployeeDTO.getDeptId()={}",esbEmployeeDTO.getDeptId());
            throw new BusinessException(ResultCode.FAILED,"事业部总监信息失败");
        }
        var.put(ApproveConstant.BU_DIRECTOR,dataApproveDeptByDeptId.getDepDirId());
        var.put(ApproveConstant.DIGITAL_GENERAL_MANAGER,process.get(ApproveConstant.DIGITAL_GENERAL_MANAGER));
        //数据节点根据板块指定审批人
        //查询板块信息
        DataApproveDeptDTO plate = dataApproveDeptApi.getDataApproveDeptByDeptId(String.valueOf(esbOrganizationDTO.getOrgId()));
        if (ObjectUtil.isNull(plate)) {
            log.error("板块信息不存在，esbOrganizationDTO.getOrgId(){}", esbOrganizationDTO.getOrgId());
            throw new BusinessException(SjshErrorCode.EMP_PLATE_NOT_FIND);
        }
        var.put(ApproveConstant.USER_PLATE,plate.getPlate());
        //查询对应审批人
        String approveUser = "";
        //商业
        if (ObjectUtil.equal(DataApprovePlateEnum.SUPPLIER.getCode(),plate.getPlate())){
            approveUser=process.get("dataApproveDistributorUser");
        }
        //医疗
        if (ObjectUtil.equal(DataApprovePlateEnum.HOSPITAL.getCode(),plate.getPlate())){
            approveUser=process.get("dataApproveHospitalUser");
        }
        //零售
        if (ObjectUtil.equal(DataApprovePlateEnum.PHARMACY.getCode(),plate.getPlate())){
            approveUser=process.get("dataApprovePharmacyUser");
        }
        var.put(ApproveConstant.USERS,approveUser);
        var.put(ApproveConstant.HOM_DEPUTY_DIRECTOR,process.get(ApproveConstant.HOM_DEPUTY_DIRECTOR));
        var.put(ApproveConstant.COMMERCE_DEPT_DIRECTOR,process.get(ApproveConstant.COMMERCE_DEPT_DIRECTOR));
        var.put(ApproveConstant.BUSINESS_DATA_SECTION,process.get(ApproveConstant.BUSINESS_DATA_SECTION));
        var.put(ApproveConstant.MC_FINANCE_DIRECTOR,process.get(ApproveConstant.MC_FINANCE_DIRECTOR));
        var.put(ApproveConstant.MARKETING_COMPANY_GENERAL_MANAGER,process.get(ApproveConstant.MARKETING_COMPANY_GENERAL_MANAGER));
        var.put(ApproveConstant.EXECUTIVE_DEPUTY_GENERAL_MANAGER,process.get(ApproveConstant.EXECUTIVE_DEPUTY_GENERAL_MANAGER));
        var.put(ApproveConstant.CHIEF_ADMINISTRATIVE_OPERATING_OFFICER,process.get(ApproveConstant.CHIEF_ADMINISTRATIVE_OPERATING_OFFICER));
        var.put(ApproveConstant.CMD_GENERAL_MANAGER,process.get(ApproveConstant.CMD_GENERAL_MANAGER));
        workFlowBaseRequest.setVariables(var);
        //首次提交
        if(formDO.getStatus().equals(FormStatusEnum.UNSUBMIT.getCode())){
            workFlowService.sendSubmitMsg(workFlowBaseRequest);
        }else if(formDO.getStatus().equals(FormStatusEnum.REJECT.getCode())){
            // 驳回重新提交
            workFlowService.resubmitMsg(workFlowBaseRequest);
        }
        return true;
    }

}
