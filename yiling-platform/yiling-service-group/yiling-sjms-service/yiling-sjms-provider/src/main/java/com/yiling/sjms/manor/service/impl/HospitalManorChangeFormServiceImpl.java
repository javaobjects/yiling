package com.yiling.sjms.manor.service.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationManorApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.api.CrmManorApi;
import com.yiling.dataflow.crm.bo.CrmRelationManorBO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.CrmManorDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorParamRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateManorRelationRequest;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.enums.AgencyLockErrorCode;
import com.yiling.sjms.agency.enums.ArchiveStatusEnum;
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
import com.yiling.sjms.manor.bo.ManorChangeBO;
import com.yiling.sjms.manor.dao.HospitalManorChangeFormMapper;
import com.yiling.sjms.manor.dto.HospitalManorChangeFormDTO;
import com.yiling.sjms.manor.dto.ManorRelationDTO;
import com.yiling.sjms.manor.dto.request.ApproveManorChangeFormRequest;
import com.yiling.sjms.manor.dto.request.DeleteManorChangeFormRequest;
import com.yiling.sjms.manor.dto.request.HospitalManorChangeRequest;
import com.yiling.sjms.manor.dto.request.ManorChangeFormRequest;
import com.yiling.sjms.manor.dto.request.QueryChangePageRequest;
import com.yiling.sjms.manor.dto.request.UpdateArchiveRequest;
import com.yiling.sjms.manor.entity.HospitalManorChangeFormDO;
import com.yiling.sjms.manor.service.HospitalManorChangeFormService;
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
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 医院辖区变更表单 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2023-05-09
 */
@Slf4j
@Service
@RefreshScope
public class HospitalManorChangeFormServiceImpl extends BaseServiceImpl<HospitalManorChangeFormMapper, HospitalManorChangeFormDO> implements HospitalManorChangeFormService {
    @Autowired
    private WorkFlowService workFlowService;

    @Autowired
    private BusinessDepartmentService businessDepartmentService;
    @Autowired
    private FormService formService;

    @Autowired
    private NoService noService;
    @DubboReference
    private FormApi formApi;

    @DubboReference
    private CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    private CrmGoodsCategoryApi crmGoodsCategoryApi;

    @DubboReference
    private CrmManorApi crmManorApi;

    @DubboReference
    private GbWorkflowProcessorApi gbWorkflowProcessorApi;

    @DubboReference
    private DataApproveDeptApi dataApproveDeptApi;

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @DubboReference(timeout = 60 * 1000)
    private CrmEnterpriseRelationShipApi relationShipApi;

    @DubboReference
    CrmEnterpriseRelationManorApi crmEnterpriseRelationManorApi;

    @Value("#{${common.gb.process-parameter}}")
    Map<String, String> process ;

    @Override
    public Long save(ManorChangeFormRequest request) {
        //机构唯一性校验
        if (ObjectUtil.isNotNull(request.getFormId())&&ObjectUtil.notEqual(request.getFormId(), 0L) && !request.getIsUpdate()) {
            LambdaQueryWrapper<HospitalManorChangeFormDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(HospitalManorChangeFormDO::getFormId, request.getFormId()).eq(HospitalManorChangeFormDO::getCrmEnterpriseId,request.getCrmEnterpriseId()).last("limit 1");
            HospitalManorChangeFormDO one = this.getOne(wrapper);
            if(Objects.nonNull(one)){
                throw new BusinessException(AgencyLockErrorCode.SAVE_FORM_FAIL);
            }
        }
        List<HospitalManorChangeRequest> reqList = request.getList();
        Long formId = 0L;
        if(Objects.isNull(request.getFormId()) || request.getFormId()==0) {
            //获取当前人的待提交的数据
            List<FormDTO> formDTOS = formApi.listUnsubmitFormsByUser(FormTypeEnum.HOSPITAL_MANOR_CHANGE, request.getOpUserId());
            if (CollUtil.isEmpty(formDTOS)) {
                CreateFormRequest createFormRequest = new CreateFormRequest();
                createFormRequest.setCode(noService.genNo(FormNoEnum.HOSPITAL_MANOR_CHANGE)).setType(FormTypeEnum.HOSPITAL_MANOR_CHANGE.getCode()).setRemark(request.getAdjustReason()).setName(OaTodoUtils.genTitle(FormTypeEnum.HOSPITAL_MANOR_CHANGE.getName(), request.getEmpName(), createFormRequest.getCode(), new Date())).setOpUserId(request.getOpUserId());
                 formId = formService.create(createFormRequest);
            } else {
                formId = formDTOS.get(0).getId();
            }
        }else{
            formId = request.getFormId();
        }
        Long finalFormId = formId;
        //查询已选
        LambdaQueryWrapper<HospitalManorChangeFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(HospitalManorChangeFormDO::getFormId, request.getFormId()).eq(HospitalManorChangeFormDO::getCrmEnterpriseId,request.getCrmEnterpriseId());
        List<HospitalManorChangeFormDO> changeFormDOS = this.list(wrapper);
        if(CollUtil.isNotEmpty(changeFormDOS)){
            List<Long> categoryIds = reqList.stream().map(HospitalManorChangeRequest::getCategoryId).collect(Collectors.toList());
            List<HospitalManorChangeFormDO> removeList = changeFormDOS.stream().filter(h -> !categoryIds.contains(h.getCategoryId())).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(removeList)){
                HospitalManorChangeFormDO remove = new HospitalManorChangeFormDO();
                remove.setOpUserId(request.getOpUserId());
                List<Long> idList = removeList.stream().map(HospitalManorChangeFormDO::getId).collect(Collectors.toList());
                //删除页面取消勾选的数据
                this.batchDeleteWithFill(remove,new LambdaQueryWrapper<HospitalManorChangeFormDO>().in(BaseDO::getId,idList));
            }
        }
        List<HospitalManorChangeFormDO> saveList = Lists.newArrayList();
        reqList.forEach(req->{
            HospitalManorChangeFormDO hospitalManorChangeFormDO = new HospitalManorChangeFormDO();
            saveList.add(hospitalManorChangeFormDO);
            PojoUtils.map(req,hospitalManorChangeFormDO);
            hospitalManorChangeFormDO.setCreateUser(request.getOpUserId()).setRemark(request.getRemark()).setCrmEnterpriseId(request.getCrmEnterpriseId());
            if(finalFormId >0){
                hospitalManorChangeFormDO.setFormId(finalFormId);
            }else{
                hospitalManorChangeFormDO.setFormId(request.getFormId());
            }
        });
        this.saveOrUpdateBatch(saveList);
        return formId;
    }

    @Override
    public Page<HospitalManorChangeFormDTO> listPage(QueryChangePageRequest request) {
        LambdaQueryWrapper<HospitalManorChangeFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(HospitalManorChangeFormDO::getFormId,request.getFormId());
        Page<HospitalManorChangeFormDO> page = this.page(request.getPage(), wrapper);
        if(page.getTotal()==0){
          return request.getPage();
        }
        Page<HospitalManorChangeFormDTO>  result = PojoUtils.map(page,HospitalManorChangeFormDTO.class);
        List<Long> entIds = result.getRecords().stream().map(HospitalManorChangeFormDTO::getCrmEnterpriseId).distinct().collect(Collectors.toList());
        List<CrmEnterpriseDTO> crmEnterpriseListById = crmEnterpriseApi.getCrmEnterpriseListById(entIds);
        //企业信息
        Map<Long, CrmEnterpriseDTO> crmEnterpriseDTOMap = crmEnterpriseListById.stream().collect(Collectors.toMap(CrmEnterpriseDTO::getId, Function.identity()));
        List<Long> categoryIds = result.getRecords().stream().map(HospitalManorChangeFormDTO::getCategoryId).distinct().collect(Collectors.toList());
        List<CrmGoodsCategoryDTO> goodsCategoryDTOS = crmGoodsCategoryApi.findByIds(categoryIds);
        //品种信息
        Map<Long, CrmGoodsCategoryDTO> goodsCategoryDTOMap = goodsCategoryDTOS.stream().collect(Collectors.toMap(CrmGoodsCategoryDTO::getId, Function.identity()));
        QueryCrmManorParamRequest crmManorParamRequest = new QueryCrmManorParamRequest();
        List<Long> manorIds = result.getRecords().stream().map(HospitalManorChangeFormDTO::getManorId).distinct().collect(Collectors.toList());
        List<Long> newManorIds = result.getRecords().stream().map(HospitalManorChangeFormDTO::getNewManorId).distinct().collect(Collectors.toList());
        List<Long> manorIdList = CollUtil.union(manorIds, newManorIds).stream().collect(Collectors.toList());
        crmManorParamRequest.setIdList(manorIdList);
        List<CrmManorDTO> crmManorDTOS = crmManorApi.listByParam(crmManorParamRequest);
        Map<Long, CrmManorDTO> manorDTOMap = crmManorDTOS.stream().collect(Collectors.toMap(CrmManorDTO::getId, Function.identity()));
        result.getRecords().forEach(form->{
            form.setEnterpriseName(crmEnterpriseDTOMap.get(form.getCrmEnterpriseId()).getName());
            form.setCategoryName(goodsCategoryDTOMap.get(form.getCategoryId()).getName());
            CrmManorDTO crmManorDTO = manorDTOMap.get(form.getManorId());
            if(Objects.nonNull(crmManorDTO)){
                form.setManorName(crmManorDTO.getName()).setManorNo(crmManorDTO.getManorNo());
            }
            CrmManorDTO newManorDTO = manorDTOMap.get(form.getNewManorId());
            if(Objects.nonNull(newManorDTO)){
                form.setNewManorName(newManorDTO.getName()).setNewManorNo(newManorDTO.getManorNo());
            }
        });
        return result;
    }

    @Override
    public void deleteById(DeleteManorChangeFormRequest request) {
        // 流程发起后不允许删除
        HospitalManorChangeFormDO changeFormDO = new HospitalManorChangeFormDO();
        HospitalManorChangeFormDO manorChangeFormDO = this.getById(request.getId());
        if(Objects.isNull(manorChangeFormDO)){
            return;
        }
        FormDO formDO = formService.getById(manorChangeFormDO.getFormId());
        if(formDO.getStatus().equals(FormStatusEnum.APPROVE.getCode()) || formDO.getStatus().equals(FormStatusEnum.AUDITING.getCode())){
            return;
        }
        PojoUtils.map(request,changeFormDO);
        this.deleteByIdWithFill(changeFormDO);
    }

    @Override
    public Boolean submit(SubmitFormBaseRequest request) {
        FormDO formDO = formService.getById(request.getId());
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(request.getEmpId());
        // 获取省份信息
        String province = relationShipApi.getProvinceAreaByThreeParms(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince());
        if(Objects.isNull(province)){
            log.error("获取省区信息失败esbEmployeeDTO={}",esbEmployeeDTO.toString());
            throw new BusinessException(ResultCode.FAILED,"获取省区信息失败");
        }
        WorkFlowBaseRequest workFlowBaseRequest = new WorkFlowBaseRequest();
        // 从配置文件获取ProcDefId
        workFlowBaseRequest.setBusinessKey(formDO.getCode()).setEmpName(esbEmployeeDTO.getEmpName()).setStartUserId(request.getEmpId()).setProcDefId(process.get("manorChange")).setTag(Constants.TAG_MANOR_CHANGE);
        workFlowBaseRequest.setTitle(formDO.getName()).setId(request.getId()).setFormType(FormTypeEnum.HOSPITAL_MANOR_CHANGE.getCode());
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
        log.info(JSONUtil.toJsonStr(workFlowBaseRequest));
        if(formDO.getStatus().equals(FormStatusEnum.UNSUBMIT.getCode())){
            workFlowService.sendSubmitMsg(workFlowBaseRequest);
        }else if(formDO.getStatus().equals(FormStatusEnum.REJECT.getCode())){
            // 驳回重新提交
            workFlowService.resubmitMsg(workFlowBaseRequest);
        }
        return true;
    }

    @Override
    public Boolean updateArchiveStatusById(UpdateArchiveRequest request) {
        //判断form状态
        HospitalManorChangeFormDO hospitalManorChangeFormDO = this.getById(request.getId());
        if (ObjectUtil.isNull(hospitalManorChangeFormDO)){
            log.warn("机构信息锁定表数据不存在，id={}",request.getId());
            throw  new BusinessException(AgencyLockErrorCode.DATA_NOT_FIND);
        }
        if (ObjectUtil.equal(hospitalManorChangeFormDO.getArchiveStatus(),request.getArchiveStatus())){
            log.warn("当前状态与修改状态一致，无需修改，id={}",request.getId());
            throw  new ServiceException(ResultCode.FAILED);
        }
        FormDO formDO = formService.getById(hospitalManorChangeFormDO.getFormId());

        if (ObjectUtil.isNull(formDO)){
            log.warn("更新数据归档状态时表单信息不存在，formId={}",hospitalManorChangeFormDO.getFormId());
            throw new BusinessException(AgencyLockErrorCode.FORM_NOT_FIND);
        }
        Integer formStatus = formDO.getStatus();
        if (ObjectUtil.equal(FormStatusEnum.APPROVE.getCode(), formStatus)) {
            log.warn("当前流程状态不允许修改操作，formId={},状态={}",hospitalManorChangeFormDO.getFormId(),FormStatusEnum.getByCode(formDO.getStatus()).getName());
            throw  new BusinessException(AgencyLockErrorCode.PROHIBIT_UPDATE);
        }
        HospitalManorChangeFormDO aDo = PojoUtils.map(request, HospitalManorChangeFormDO.class);
        aDo.setOpTime(new Date());
        boolean isSuccess = this.updateById(aDo);
        if (!isSuccess){
            log.error("更新归档状态失败，id={}",request.getId());
            throw  new ServiceException(ResultCode.FAILED);
        }
        return true;
    }

    @Override
    public ManorChangeBO queryByFormId(Long formId) {
        HospitalManorChangeFormDO changeFormDO = this.getById(formId);
        LambdaQueryWrapper<HospitalManorChangeFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(HospitalManorChangeFormDO::getFormId,changeFormDO.getFormId()).eq(HospitalManorChangeFormDO::getCrmEnterpriseId,changeFormDO.getCrmEnterpriseId());
        List<HospitalManorChangeFormDO> list = this.list(wrapper);
        if(CollUtil.isEmpty(list)){
           return null;
        }
        Map<Long, HospitalManorChangeFormDO> changeFormDOMap = list.stream().collect(Collectors.toMap(HospitalManorChangeFormDO::getCategoryId, Function.identity()));
        //查询机构信息
        CrmEnterpriseDTO enterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(list.get(0).getCrmEnterpriseId());
        ManorChangeBO manorChangeBO = new ManorChangeBO();
        manorChangeBO.setEnterpriseName(enterpriseDTO.getName()).setCityName(enterpriseDTO.getCityName()).setCityCode(enterpriseDTO.getCityCode()).setProvinceName(enterpriseDTO.getProvinceName())
                .setProvinceCode(enterpriseDTO.getProvinceCode()).setRegionName(enterpriseDTO.getRegionName()).setRegionCode(enterpriseDTO.getRegionCode()).setRemark(changeFormDO.getRemark())
                .setLicenseNumber(enterpriseDTO.getLicenseNumber()).setSupplyChainRole(enterpriseDTO.getSupplyChainRole()).setId(enterpriseDTO.getId())
        ;
        List<CrmRelationManorBO> relationManorBOS = crmEnterpriseRelationManorApi.queryList(list.get(0).getCrmEnterpriseId());
        List<ManorRelationDTO> manorRelationDTOS = Lists.newArrayListWithExpectedSize(relationManorBOS.size());

        List<Long> categoryIds =relationManorBOS.stream().map(CrmRelationManorBO::getCategoryId).distinct().collect(Collectors.toList());
        List<CrmGoodsCategoryDTO> goodsCategoryDTOS = crmGoodsCategoryApi.findByIds(categoryIds);
        //品种信息
        Map<Long, CrmGoodsCategoryDTO> goodsCategoryDTOMap = goodsCategoryDTOS.stream().collect(Collectors.toMap(CrmGoodsCategoryDTO::getId, Function.identity()));


        relationManorBOS.forEach(r->{
            ManorRelationDTO manorRelationDTO = new ManorRelationDTO();
            manorRelationDTOS.add(manorRelationDTO);
            manorRelationDTO.setManorId(r.getCrmManorId()).setManorName(r.getManorName()).setManorNo(r.getManorNo());
            manorRelationDTO.setCategoryName(goodsCategoryDTOMap.get(r.getCategoryId()).getName()).setCategoryId(r.getCategoryId());
            HospitalManorChangeFormDO hospitalManorChangeFormDO = changeFormDOMap.get(r.getCategoryId());
            if(Objects.nonNull(hospitalManorChangeFormDO)){
                manorRelationDTO.setId(hospitalManorChangeFormDO.getId()).setNewManorId(hospitalManorChangeFormDO.getNewManorId()).setIsChecked(true);
            }else{
                manorRelationDTO.setIsChecked(false);
            }
        });
        List<Long> newManorIds = manorRelationDTOS.stream().map(ManorRelationDTO::getNewManorId).distinct().collect(Collectors.toList());
        QueryCrmManorParamRequest crmManorParamRequest = new QueryCrmManorParamRequest();
        crmManorParamRequest.setIdList(newManorIds);
        List<CrmManorDTO> crmManorDTOS = crmManorApi.listByParam(crmManorParamRequest);
        Map<Long, CrmManorDTO> manorDTOMap = crmManorDTOS.stream().collect(Collectors.toMap(CrmManorDTO::getId, Function.identity()));
        manorRelationDTOS.forEach(m->{
            if(Objects.nonNull(m.getNewManorId())){
                CrmManorDTO crmManorDTO = manorDTOMap.get(m.getNewManorId());
                m.setNewManorName(crmManorDTO.getName()).setNewManorNo(crmManorDTO.getManorNo());
            }
        });
        manorChangeBO.setRelation(manorRelationDTOS);
        return manorChangeBO;
    }

    @Override
    public Boolean approveToChange(ApproveManorChangeFormRequest request) {
        LambdaQueryWrapper<HospitalManorChangeFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(HospitalManorChangeFormDO::getFormId,request.getId()).eq(HospitalManorChangeFormDO::getArchiveStatus, ArchiveStatusEnum.OPEN.getCode());
        List<HospitalManorChangeFormDO> formDOS = this.list(wrapper);
        if(CollUtil.isEmpty(formDOS)){
            return false;
        }
        List<SaveOrUpdateManorRelationRequest> updateRequest = Lists.newArrayList();
        formDOS.forEach(f->{
            SaveOrUpdateManorRelationRequest update = new SaveOrUpdateManorRelationRequest();
            update.setCategoryId(f.getCategoryId()).setCrmManorId(f.getManorId()).setNewManorId(f.getNewManorId()).setCrmEnterpriseId(f.getCrmEnterpriseId()).setOpUserId(1L);
            updateRequest.add(update);
        });
        return crmEnterpriseRelationManorApi.updateBatch(updateRequest);
    }
}
