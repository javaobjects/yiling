package com.yiling.sjms.agency.service.impl;

import java.util.ArrayList;
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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.dto.request.ChangeRelationShipDetailRequest;
import com.yiling.dataflow.crm.dto.request.ChangeRelationShipRequest;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.dao.AgencyRelationShipChangeFormMapper;
import com.yiling.sjms.agency.dto.AgencyRelationShipChangeFormDTO;
import com.yiling.sjms.agency.dto.request.EnterpriseUnLockApproveRequest;
import com.yiling.sjms.agency.dto.request.QueryAgencyFormPageRequest;
import com.yiling.sjms.agency.dto.request.RemoveAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyRelationChangeFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyRelationShipChangeRequest;
import com.yiling.sjms.agency.dto.request.UpdateAgencyLockArchiveRequest;
import com.yiling.sjms.agency.entity.AgencyChangeRelationShipDO;
import com.yiling.sjms.agency.entity.AgencyRelationShipChangeFormDO;
import com.yiling.sjms.agency.enums.AgencyLockErrorCode;
import com.yiling.sjms.agency.service.AgencyChangeRelationShipService;
import com.yiling.sjms.agency.service.AgencyRelationShipChangeFormService;
import com.yiling.sjms.constant.ApproveConstant;
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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import static com.yiling.sjms.agency.enums.AgencyRelationShipChangeErrorCode.LIST_ALREADY_EXIST;
import static com.yiling.sjms.agency.enums.AgencyRelationShipChangeErrorCode.SAVE_FORM_FAIL;

/**
 * <p>
 * 机构锁定三者关系变更表单 服务实现类
 * </p>
 *
 * @author handy
 * @date 2023-02-22
 */
@Service
@Slf4j
@RefreshScope
public class AgencyRelationShipChangeFormServiceImpl extends BaseServiceImpl<AgencyRelationShipChangeFormMapper, AgencyRelationShipChangeFormDO> implements AgencyRelationShipChangeFormService {

    @Autowired
    private AgencyChangeRelationShipService agencyChangeRelationShipService;

    @Autowired
    private FormService formService;

    @Autowired
    private WorkFlowService workFlowService;

    @Autowired
    private BusinessDepartmentService businessDepartmentService;

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @DubboReference(timeout = 60 * 1000)
    private CrmEnterpriseRelationShipApi relationShipApi;


    @DubboReference
    private GbWorkflowProcessorApi gbWorkflowProcessorApi;

    @DubboReference
    private DataApproveDeptApi dataApproveDeptApi;

    @Autowired
    private NoService noService;

    @Value("#{${common.gb.process-parameter}}")
    Map<String, String> process ;

    @Override
    public Long save(SaveAgencyRelationChangeFormRequest request) {

        AgencyRelationShipChangeFormDO agencyLockFormDO = new AgencyRelationShipChangeFormDO();
        if(Objects.isNull(request.getFormId()) || request.getFormId()==0){
            //保存草稿 todo 唯一性校验
            CreateFormRequest createFormRequest = new CreateFormRequest();
            createFormRequest.setCode(noService.genNo(FormNoEnum.ENTERPRISE_REL_SHIP_CHANGE)).setType(FormTypeEnum.ENTERPRISE_REL_SHIP_CHANGE.getCode()).setRemark(request.getAdjustReason())
                    .setName(OaTodoUtils.genTitle(FormTypeEnum.ENTERPRISE_REL_SHIP_CHANGE.getName(), request.getEmpName(), createFormRequest.getCode(), new Date())).setOpUserId(request.getOpUserId());
            Long formId = formService.create(createFormRequest);
            agencyLockFormDO.setFormId(formId);
            request.setFormId(formId);
        }
        // 一个表单只能有一种供应链角色
        if (ObjectUtil.isNotNull(request.getFormId()) && ObjectUtil.isNull(request.getId())) {
            QueryWrapper<AgencyRelationShipChangeFormDO> changeFormDOQueryWrapper = new QueryWrapper<>();
            changeFormDOQueryWrapper.lambda().eq(AgencyRelationShipChangeFormDO::getFormId, request.getFormId());
            List<AgencyRelationShipChangeFormDO> list = this.list(changeFormDOQueryWrapper);
            if (CollectionUtil.isNotEmpty(list)) {
                if (!request.getSupplyChainRole().equals(list.get(0).getSupplyChainRole())) {
                    log.warn("当前机构列表中存在重复的机构,fomId={}", request.getFormId());
                    throw new BusinessException(SAVE_FORM_FAIL);
                }
                boolean exist = list.stream().anyMatch(t -> {
                   return t.getName().equals(request.getName());
                });
                if (exist) {
                    log.warn("表单中机构的供应链角色必须一致,fomId={},request.chainRole={}", request.getFormId(),request.getSupplyChainRole());
                    throw new BusinessException(LIST_ALREADY_EXIST);
                }
            }
        }
        PojoUtils.map(request, agencyLockFormDO);
        agencyLockFormDO.setFormId(request.getFormId());
        this.saveOrUpdate(agencyLockFormDO);
        //先删除，再加入。还要增加关联表
        List<SaveAgencyRelationShipChangeRequest> relationShip = request.getRelationShip();
        if (CollectionUtil.isNotEmpty(relationShip)) {
            LambdaQueryWrapper<AgencyChangeRelationShipDO> unlockRelation = new LambdaQueryWrapper<>();
            unlockRelation.eq(AgencyChangeRelationShipDO::getChangeRelationShipId, agencyLockFormDO.getId());
            AgencyChangeRelationShipDO flowCrmSaleDO = new AgencyChangeRelationShipDO();
            flowCrmSaleDO.setOpUserId(request.getOpUserId());
            flowCrmSaleDO.setDelFlag(1);
            agencyChangeRelationShipService.batchDeleteWithFill(flowCrmSaleDO,unlockRelation);
            relationShip.forEach(item -> {
                item.setChangeRelationShipId(agencyLockFormDO.getId());
            });
            agencyChangeRelationShipService.saveBatch(PojoUtils.map(relationShip, AgencyChangeRelationShipDO.class));
        }
        return request.getFormId();
    }

   /* @Override
    public AgencyLockFormDTO getAgencyLockForm(Long id) {
        AgencyUnlockFormDO agencyLockFormDO = this.getById(id);
        return PojoUtils.map(agencyLockFormDO, AgencyLockFormDTO.class);
    }*/

    @Override
    public boolean deleteById(RemoveAgencyFormRequest request) {
        //todo 流程发起后不允许删除,还要删除关联表
        AgencyRelationShipChangeFormDO agencyLockFormDO=PojoUtils.map(request, AgencyRelationShipChangeFormDO.class);
        this.deleteByIdWithFill(agencyLockFormDO);
        LambdaUpdateWrapper<AgencyChangeRelationShipDO> unlockRelation = new LambdaUpdateWrapper<>();
        unlockRelation.eq(AgencyChangeRelationShipDO::getChangeRelationShipId, agencyLockFormDO.getId());
        AgencyChangeRelationShipDO flowCrmSaleDO = new AgencyChangeRelationShipDO();
        flowCrmSaleDO.setOpUserId(request.getOpUserId());
        flowCrmSaleDO.setDelFlag(1);
        agencyChangeRelationShipService.update(flowCrmSaleDO, unlockRelation);
        return true;
    }

    @Override
    public Page<AgencyRelationShipChangeFormDTO> pageList(QueryAgencyFormPageRequest request) {
        LambdaQueryWrapper<AgencyRelationShipChangeFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AgencyRelationShipChangeFormDO::getFormId, request.getFormId()).orderByDesc(AgencyRelationShipChangeFormDO::getId);
        Page<AgencyRelationShipChangeFormDO> page = this.page(request.getPage(), wrapper);
        if(CollectionUtil.isNotEmpty(page.getRecords())){
            // jobId对应的数据要实时取
            List<Long> jobIds = page.getRecords().stream().map(AgencyRelationShipChangeFormDO::getPostCode).collect(Collectors.toList());
            List<EsbEmployeeDTO> employeeDTOS = esbEmployeeApi.listByJobIdsForAgency(jobIds);
            if(CollectionUtil.isNotEmpty(employeeDTOS)){
                Map<Long, EsbEmployeeDTO> esbEmployeeDTOMap = employeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getJobId, Function.identity()));
                page.getRecords().forEach(item->{
                    EsbEmployeeDTO esbEmployeeDTO = esbEmployeeDTOMap.get(item.getPostCode());
                    if(ObjectUtil.isNotNull(esbEmployeeDTO)){
                        item.setPostName(esbEmployeeDTO.getJobName());
                        item.setRepresentativeCode(esbEmployeeDTO.getEmpId());
                        item.setRepresentativeName(esbEmployeeDTO.getEmpName());
                    }
                });
            }
        }
        return PojoUtils.map(page, AgencyRelationShipChangeFormDTO.class);
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
        workFlowBaseRequest.setBusinessKey(formDO.getCode()).setEmpName(esbEmployeeDTO.getEmpName()).setStartUserId(request.getEmpId()).setProcDefId(process.get("relationShipChange")).setTag(Constants.TAG_EXTEND_CHANGE);
        workFlowBaseRequest.setTitle(formDO.getName()).setId(request.getId()).setFormType(FormTypeEnum.ENTERPRISE_REL_SHIP_CHANGE.getCode());
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
    public Boolean approved(EnterpriseUnLockApproveRequest request) {
        // 机构解锁，就是解除三者关系的锁定，删除三者关系表数据
        LambdaQueryWrapper<AgencyRelationShipChangeFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AgencyRelationShipChangeFormDO::getFormId, request.getFormId());
        wrapper.eq(AgencyRelationShipChangeFormDO::getArchiveStatus, 1);
        List<AgencyRelationShipChangeFormDO> list = this.list(wrapper);
        if (CollectionUtil.isEmpty(list)) {
            return false;
        }
        List<Long> ids = list.stream().map(AgencyRelationShipChangeFormDO::getId).collect(Collectors.toList());
        LambdaQueryWrapper<AgencyChangeRelationShipDO> unlockQuery = Wrappers.lambdaQuery();
        unlockQuery.in(AgencyChangeRelationShipDO::getChangeRelationShipId, ids);
        List<AgencyChangeRelationShipDO> relationShipDOS = agencyChangeRelationShipService.list(unlockQuery);
        if (CollectionUtil.isEmpty(relationShipDOS)) {
            return false;
        }
        Map<Long, List<AgencyChangeRelationShipDO>> collect = relationShipDOS.stream().collect(Collectors.groupingBy(AgencyChangeRelationShipDO::getChangeRelationShipId));
        ChangeRelationShipRequest changeRelationShipRequest = new ChangeRelationShipRequest();
        List<ChangeRelationShipDetailRequest>requestList=new ArrayList<>();
        list.forEach(item->{
            List<AgencyChangeRelationShipDO> agencyChangeRelationShipDOS = collect.get(item.getId());
            if(CollectionUtil.isNotEmpty(agencyChangeRelationShipDOS)){
                ChangeRelationShipDetailRequest relationShipDetailRequest = new ChangeRelationShipDetailRequest();
                List<Long> srcRelationShipIps = agencyChangeRelationShipDOS.stream().map(AgencyChangeRelationShipDO::getSrcRelationShipIp).collect(Collectors.toList());
                relationShipDetailRequest.setIds(srcRelationShipIps);
                relationShipDetailRequest.setPostCode(item.getPostCode());
                relationShipDetailRequest.setPostName(item.getPostName());
                relationShipDetailRequest.setOpUserId(1L);
                requestList.add(relationShipDetailRequest);
            }
        });
        changeRelationShipRequest.setRequestList(requestList);
        Boolean result = relationShipApi.changeRelationShip(changeRelationShipRequest);
        return result;
    }

    @Override
    public Boolean updateArchiveStatusById(UpdateAgencyLockArchiveRequest request) {
        //判断form状态
        AgencyRelationShipChangeFormDO lockFormDO = this.getById(request.getId());
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
        AgencyRelationShipChangeFormDO aDo = PojoUtils.map(request, AgencyRelationShipChangeFormDO.class);
        aDo.setOpTime(new Date());
        boolean isSuccess = this.updateById(aDo);
        if (!isSuccess){
            log.error("更新归档状态失败，id={}",request.getId());
            throw  new ServiceException(ResultCode.FAILED);
        }
        return true;
    }

    @Override
    public List<String> getNameByFormId(Long id) {
        LambdaQueryWrapper<AgencyRelationShipChangeFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AgencyRelationShipChangeFormDO::getFormId, id);
        List<AgencyRelationShipChangeFormDO> list = this.list(wrapper);
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().map(AgencyRelationShipChangeFormDO::getName).collect(Collectors.toList());
    }
}