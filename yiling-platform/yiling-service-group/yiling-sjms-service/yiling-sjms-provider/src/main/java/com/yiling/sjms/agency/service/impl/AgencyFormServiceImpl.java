package com.yiling.sjms.agency.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyCountRequest;
import com.yiling.dataflow.agency.enums.AgencySupplyChainRoleEnum;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.request.SaveAgencyEnterpriseRequest;
import com.yiling.dataflow.crm.dto.request.UpdateAgencyEnterpriseRequest;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.dao.AgencyFormMapper;
import com.yiling.sjms.agency.dto.request.ApproveAgencyAddFormRequest;
import com.yiling.sjms.agency.dto.request.ApproveAgencyUpdateFormRequest;
import com.yiling.sjms.agency.dto.request.QueryAgencyFormPageRequest;
import com.yiling.sjms.agency.dto.request.QueryFirstAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.RemoveAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.SubmitAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.UpdateAgencyFormArchiveRequest;
import com.yiling.sjms.agency.entity.AgencyFormDO;
import com.yiling.sjms.agency.enums.AgencyFormChangeItemEnum;
import com.yiling.sjms.agency.enums.AgencyFormErrorCode;
import com.yiling.sjms.agency.enums.ArchiveStatusEnum;
import com.yiling.sjms.agency.service.AgencyFormService;
import com.yiling.sjms.constant.ApproveConstant;
import com.yiling.sjms.form.dto.request.CreateFormRequest;
import com.yiling.sjms.form.entity.FormDO;
import com.yiling.sjms.form.enums.FormNoEnum;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.form.service.FormService;
import com.yiling.sjms.gb.service.NoService;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.sjms.sjsp.api.DataApproveDeptApi;
import com.yiling.sjms.sjsp.dto.DataApproveDeptDTO;
import com.yiling.sjms.sjsp.enums.DataApprovePlateEnum;
import com.yiling.sjms.sjsp.enums.SjshErrorCode;
import com.yiling.sjms.util.OaTodoUtils;
import com.yiling.sjms.workflow.WorkFlowService;
import com.yiling.sjms.workflow.dto.request.WorkFlowBaseRequest;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 机构新增修改表单 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-22
 */
@Slf4j
@Service
@RefreshScope
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AgencyFormServiceImpl extends BaseServiceImpl<AgencyFormMapper, AgencyFormDO> implements AgencyFormService {

    @DubboReference
    EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    LocationApi locationApi;

    @DubboReference(timeout = 30 * 1000)
    BusinessDepartmentApi businessDepartmentApi;

    @DubboReference
    DataApproveDeptApi dataApproveDeptApi;

    private final FormService formService;

    private final NoService noService;

    private final WorkFlowService workFlowService;

    @Value("#{${common.gb.process-parameter}}")
    Map<String, String> process;

    @Override
    @GlobalTransactional
    public Long saveAgencyForm(SaveAgencyFormRequest request) {
        AgencyFormDO agencyFormDO = PojoUtils.map(request, AgencyFormDO.class);
        if (Objects.isNull(agencyFormDO)) {
            return null;
        }
        // 如果传入id则说明是修改(这个id是表单表的id，不是流程表的id)
        // 里面的optType才代表表单的新增还是修改
        if (Objects.isNull(request.getFormId()) || 0L == request.getFormId()) {
            // 如果formId已经存在，则说明是第一个表单，需要新增流程信息
            CreateFormRequest createFormRequest = new CreateFormRequest();
            createFormRequest.setOpUserId(request.getOpUserId());
            createFormRequest.setOpTime(request.getOpTime());
            // 表单操作:1-新增,2-修改
            if (FormTypeEnum.ENTERPRISE_ADD == request.getFormTypeEnum()) {
                createFormRequest.setCode(noService.genNo(FormNoEnum.ENTERPRISE_ADD));
            } else if (FormTypeEnum.ENTERPRISE_UPDATE == request.getFormTypeEnum()) {
                createFormRequest.setCode(noService.genNo(FormNoEnum.ENTERPRISE_UPDATE));
            }
            createFormRequest.setType(request.getFormTypeEnum().getCode());
            createFormRequest.setName(OaTodoUtils.genTitle(request.getFormTypeEnum().getName(), request.getEmpName(), createFormRequest.getCode(), new Date()));
            Long formId = formService.create(createFormRequest);
            agencyFormDO.setFormId(formId);
        }
        this.saveOrUpdate(agencyFormDO);

        return agencyFormDO.getFormId();
    }

    @Override
    public Page<AgencyFormDO> pageList(QueryAgencyFormPageRequest request) {
        LambdaQueryWrapper<AgencyFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AgencyFormDO::getFormId, request.getFormId()).orderByDesc(AgencyFormDO::getCreateTime);
        return this.page(request.getPage(), wrapper);
    }

    @Override
    public List<AgencyFormDO> listByFormIdAndCrmEnterpriseId(Long formId, Long crmEnterpriseId) {
        LambdaQueryWrapper<AgencyFormDO> wrapper = Wrappers.lambdaQuery();
        if (Objects.nonNull(formId) && 0 != formId) {
            wrapper.eq(AgencyFormDO::getFormId, formId);
        }
        if (Objects.nonNull(crmEnterpriseId) && 0 != crmEnterpriseId) {
            wrapper.eq(AgencyFormDO::getCrmEnterpriseId, crmEnterpriseId);
        }
        wrapper.orderByDesc(AgencyFormDO::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public AgencyFormDO getFirstInfo(QueryFirstAgencyFormRequest request) {
        QueryWrapper<AgencyFormDO> wrapper = new QueryWrapper<>();
        if (Objects.nonNull(request.getNotId()) && 0 != request.getNotId()) {
            wrapper.lambda().ne(AgencyFormDO::getId, request.getNotId());
        }
        if (Objects.nonNull(request.getFormId()) && 0 != request.getFormId()) {
            wrapper.lambda().eq(AgencyFormDO::getFormId, request.getFormId());
        }
        if (ObjectUtil.isNotEmpty(request.getLicenseNumber())) {
            wrapper.lambda().eq(AgencyFormDO::getLicenseNumber, request.getLicenseNumber());
        }
        if (ObjectUtil.isNotEmpty(request.getName())) {
            wrapper.lambda().eq(AgencyFormDO::getName, request.getName());
        }
        wrapper.lambda().last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public boolean removeById(RemoveAgencyFormRequest request) {
        if (Objects.isNull(request.getId())) {
            return false;
        }

        //        // 如果删除的是最后一个机构表单，则同时删除表单基础表
        //        AgencyFormDO entity = this.getById(request.getId());
        //        LambdaQueryWrapper<AgencyFormDO> wrapper = new LambdaQueryWrapper<>();
        //        wrapper.eq(AgencyFormDO::getFormId, entity.getFormId());
        //        if (this.count(wrapper) < 1) {
        //            return formService.delete(entity.getFormId(), request.getOpUserId());
        //        }

        AgencyFormDO agencyFormDO = PojoUtils.map(request, AgencyFormDO.class);
        this.deleteByIdWithFill(agencyFormDO);
        return true;
    }

    @Override
    public boolean submit(SubmitAgencyFormRequest request) {
        FormDO formDO = formService.getById(request.getFormId());
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(request.getEmpId());
        String procDefId;
        String tag;
        Map<String, Object> var = Maps.newHashMap();

        if (FormTypeEnum.ENTERPRISE_ADD == request.getFormTypeEnum()) {
            procDefId = process.get("agencyAdd");
            tag = Constants.TAG_ADD_AGENCY;
        } else if (FormTypeEnum.ENTERPRISE_UPDATE == request.getFormTypeEnum()) {
            procDefId = process.get("agencyUpdate");
            tag = Constants.TAG_UPDATE_AGENCY;
        } else {
            log.error("机构新增或修改流程发时formType非法，参数={}", request.getFormTypeEnum());
            throw new BusinessException(SjshErrorCode.FORM_TYPE_INVALID);
        }

        var.put(ApproveConstant.START_USER_ID, request.getEmpId());
        //查询部门
        EsbOrganizationDTO organizationDTO = businessDepartmentApi.getByEmpId(request.getEmpId());
        if (ObjectUtil.isNull(organizationDTO) || StrUtil.isBlank(organizationDTO.getOrgName())) {
            log.error("机构新增或修改流程发起人的部门信息不存在，参数={}", organizationDTO);
            throw new BusinessException(SjshErrorCode.EMP_DEPT_NOT_FIND);
        }
        //查询板块信息
        DataApproveDeptDTO plate = dataApproveDeptApi.getDataApproveDeptByDeptId(String.valueOf(organizationDTO.getOrgId()));
        if (ObjectUtil.isNull(plate)) {
            log.error("机构新增或修改流程发起人部门对应的板块信息不存在，部门={}", organizationDTO);
            throw new BusinessException(SjshErrorCode.EMP_PLATE_NOT_FIND);
        }
        var.put(ApproveConstant.USER_PLATE, plate.getPlate());
        //查询对应审批人
        String approveUser = "";
        //商业
        if (ObjectUtil.equal(DataApprovePlateEnum.SUPPLIER.getCode(), plate.getPlate())) {
            approveUser = process.get("dataApproveDistributorUser");
        }
        //医疗
        if (ObjectUtil.equal(DataApprovePlateEnum.HOSPITAL.getCode(), plate.getPlate())) {
            approveUser = process.get("dataApproveHospitalUser");
        }
        //零售
        if (ObjectUtil.equal(DataApprovePlateEnum.PHARMACY.getCode(), plate.getPlate())) {
            approveUser = process.get("dataApprovePharmacyUser");
        }
        if (StrUtil.isBlank(approveUser)) {
            log.error("机构新增或修改流程发起时没有找到审批人，ProcDefId={},approveUser={},发起人所属板块={}", procDefId, approveUser, DataApprovePlateEnum.getByCode(plate.getPlate()).getName());
            throw new BusinessException(SjshErrorCode.APPROVE_USER_NOT_FIND);
        }
        var.put(ApproveConstant.USERS, approveUser);
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
        return true;
    }

    @Override
    public void updateArchiveStatusById(UpdateAgencyFormArchiveRequest request) {
        //判断form状态
        AgencyFormDO agencyFormDO = getById(request.getId());
        if (ObjectUtil.isNull(agencyFormDO)) {
            log.warn("机构信息锁定表数据不存在，id={}", request.getId());
            throw new BusinessException(AgencyFormErrorCode.AGENCY_NOT_FIND);
        }
        if (ObjectUtil.equal(agencyFormDO.getArchiveStatus(), request.getArchiveStatus())) {
            log.warn("当前状态与修改状态一致，无需修改，id={}", request.getId());
            throw new ServiceException(ResultCode.FAILED);
        }
        FormDO formDO = formService.getById(agencyFormDO.getFormId());

        if (ObjectUtil.isNull(formDO)) {
            log.warn("更新数据归档状态时表单信息不存在，formId={}", agencyFormDO.getFormId());
            throw new BusinessException(AgencyFormErrorCode.FORM_NOT_FIND);
        }
        Integer formStatus = formDO.getStatus();
        if (ObjectUtil.equal(FormStatusEnum.APPROVE.getCode(), formStatus)) {
            log.warn("当前流程状态不允许修改操作，formId={},状态={}", agencyFormDO.getFormId(), FormStatusEnum.getByCode(formDO.getStatus()).getName());
            throw new BusinessException(AgencyFormErrorCode.STATUS_NOT_ALLOW_ARCHIVE);
        }
        AgencyFormDO agencyForm = PojoUtils.map(request, AgencyFormDO.class);
        boolean isSuccess = this.updateById(agencyForm);
        if (!isSuccess) {
            log.error("更新归档状态失败，id={}", request.getId());
            throw new ServiceException(ResultCode.FAILED);
        }
    }

    @Override
    @GlobalTransactional
    public boolean approveToAdd(ApproveAgencyAddFormRequest request) {
        LambdaQueryWrapper<AgencyFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AgencyFormDO::getFormId, request.getId());
        wrapper.eq(AgencyFormDO::getArchiveStatus, ArchiveStatusEnum.OPEN.getCode());
        List<AgencyFormDO> agencyFormDOList = this.list(wrapper);
        if (CollUtil.isEmpty(agencyFormDOList)) {
            return false;
        }
        List<SaveAgencyEnterpriseRequest> requestList = new ArrayList<>();
        List<AgencyFormDO> updateList = new ArrayList<>();
        for (AgencyFormDO agencyFormDO : agencyFormDOList) {
            SaveAgencyEnterpriseRequest agencyEnterpriseRequest = new SaveAgencyEnterpriseRequest();
            PojoUtils.map(agencyFormDO, agencyEnterpriseRequest);
            String[] namesByCodes = locationApi.getNamesByCodes(agencyFormDO.getProvinceCode(), agencyFormDO.getCityCode(), agencyFormDO.getRegionCode());
            agencyEnterpriseRequest.setProvinceName(namesByCodes[0]);
            agencyEnterpriseRequest.setCityName(namesByCodes[1]);
            agencyEnterpriseRequest.setRegionName(namesByCodes[2]);
            {
                QueryCrmAgencyCountRequest checkNameRequest = new QueryCrmAgencyCountRequest();
                checkNameRequest.setName(agencyFormDO.getName());
                CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getFirstCrmEnterpriseInfo(checkNameRequest);
                if (Objects.nonNull(crmEnterpriseDTO)) {
                    AgencyFormDO updateForm = new AgencyFormDO();
                    updateForm.setId(agencyFormDO.getId());
                    updateForm.setEnterDatabase(1);
                    updateForm.setFailEnterMessage("输入的机构名称" + crmEnterpriseDTO.getSupplyChainRole() + "已存在");
                    updateForm.setOpUserId(1L);
                    updateList.add(updateForm);
                    continue;
                }
            }
            {
                AgencySupplyChainRoleEnum supplyChainRoleEnum = AgencySupplyChainRoleEnum.getByCode(agencyFormDO.getSupplyChainRole());
                // 需要校验
                // 1.如果是商业公司
                // 2.非商业公司,信用代码不为0时
                // 不需要校验的：非商业公司，信用代码为0
                if (!(AgencySupplyChainRoleEnum.SUPPLIER != supplyChainRoleEnum && "0".equals(agencyFormDO.getLicenseNumber()))) {
                    QueryCrmAgencyCountRequest checkNameRequest = new QueryCrmAgencyCountRequest();
                    checkNameRequest.setLicenseNumber(agencyFormDO.getLicenseNumber());
                    CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getFirstCrmEnterpriseInfo(checkNameRequest);
                    if (Objects.nonNull(crmEnterpriseDTO)) {
                        AgencyFormDO updateForm = new AgencyFormDO();
                        updateForm.setId(agencyFormDO.getId());
                        updateForm.setEnterDatabase(1);
                        updateForm.setFailEnterMessage("输入的统一社会信用代码在" + crmEnterpriseDTO.getSupplyChainRole() + "已存在");
                        updateForm.setOpUserId(1L);
                        updateList.add(updateForm);
                        continue;
                    }
                }
            }
            agencyEnterpriseRequest.setBusinessCode(1);
            agencyEnterpriseRequest.setOpUserId(1L);
            requestList.add(agencyEnterpriseRequest);
        }
        if (CollUtil.isNotEmpty(requestList)) {
            log.info("approveToAdd 开始入库:[{}]", requestList);
            crmEnterpriseApi.saveAgencyEnterpriseList(requestList);
        }
        if (CollUtil.isNotEmpty(updateList)) {
            log.info("approveToAdd 入库失败:[{}]", updateList);
            this.updateBatchById(updateList);
        }
        return true;
    }

    @Override
    @GlobalTransactional
    public boolean approveToUpdate(ApproveAgencyUpdateFormRequest request) {
        LambdaQueryWrapper<AgencyFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AgencyFormDO::getFormId, request.getId());
        wrapper.eq(AgencyFormDO::getArchiveStatus, ArchiveStatusEnum.OPEN.getCode());
        List<AgencyFormDO> agencyFormDOList = this.list(wrapper);
        if (CollUtil.isEmpty(agencyFormDOList)) {
            return false;
        }
        List<UpdateAgencyEnterpriseRequest> requestList = new ArrayList<>();
        List<AgencyFormDO> updateList = new ArrayList<>();
        for (AgencyFormDO agencyFormDO : agencyFormDOList) {
            List<Integer> changeItemList = JSON.parseArray(agencyFormDO.getChangeItem(), Integer.class);
            UpdateAgencyEnterpriseRequest updateAgencyEnterpriseRequest = new UpdateAgencyEnterpriseRequest();
            updateAgencyEnterpriseRequest.setId(agencyFormDO.getCrmEnterpriseId());
            if (changeItemList.contains(AgencyFormChangeItemEnum.SUPPLY_CHAIN_ROLE.getCode())) {
                updateAgencyEnterpriseRequest.setSupplyChainRole(agencyFormDO.getSupplyChainRole());
            }
            if (changeItemList.contains(AgencyFormChangeItemEnum.NAME.getCode())) {
                {
                    QueryCrmAgencyCountRequest checkNameRequest = new QueryCrmAgencyCountRequest();
                    checkNameRequest.setNotId(agencyFormDO.getCrmEnterpriseId());
                    checkNameRequest.setName(agencyFormDO.getName());
                    CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getFirstCrmEnterpriseInfo(checkNameRequest);
                    if (Objects.nonNull(crmEnterpriseDTO)) {
                        AgencyFormDO updateForm = new AgencyFormDO();
                        updateForm.setId(agencyFormDO.getId());
                        updateForm.setEnterDatabase(1);
                        updateForm.setFailEnterMessage("输入的机构名称" + crmEnterpriseDTO.getSupplyChainRole() + "已存在");
                        updateForm.setOpUserId(1L);
                        updateList.add(updateForm);
                        continue;
                    }
                }
                updateAgencyEnterpriseRequest.setName(agencyFormDO.getName());
            }
            if (changeItemList.contains(AgencyFormChangeItemEnum.LICENSE_NUMBER.getCode())) {
                {
                    QueryCrmAgencyCountRequest checkNameRequest = new QueryCrmAgencyCountRequest();
                    checkNameRequest.setNotId(agencyFormDO.getCrmEnterpriseId());
                    checkNameRequest.setLicenseNumber(agencyFormDO.getLicenseNumber());
                    CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getFirstCrmEnterpriseInfo(checkNameRequest);
                    if (Objects.nonNull(crmEnterpriseDTO)) {
                        AgencyFormDO updateForm = new AgencyFormDO();
                        updateForm.setId(agencyFormDO.getId());
                        updateForm.setEnterDatabase(1);
                        updateForm.setFailEnterMessage("输入的统一社会信用代码在" + crmEnterpriseDTO.getSupplyChainRole() + "已存在");
                        updateForm.setOpUserId(1L);
                        updateList.add(updateForm);
                        continue;
                    }
                }
                updateAgencyEnterpriseRequest.setLicenseNumber(agencyFormDO.getLicenseNumber());
            }
            if (changeItemList.contains(AgencyFormChangeItemEnum.PHONE.getCode())) {
                updateAgencyEnterpriseRequest.setPhone(agencyFormDO.getPhone());
            }
            if (changeItemList.contains(AgencyFormChangeItemEnum.AREA.getCode())) {
                String[] namesByCodes = locationApi.getNamesByCodes(agencyFormDO.getProvinceCode(), agencyFormDO.getCityCode(), agencyFormDO.getRegionCode());
                updateAgencyEnterpriseRequest.setProvinceCode(agencyFormDO.getProvinceCode());
                updateAgencyEnterpriseRequest.setCityCode(agencyFormDO.getCityCode());
                updateAgencyEnterpriseRequest.setRegionCode(agencyFormDO.getRegionCode());
                updateAgencyEnterpriseRequest.setProvinceName(namesByCodes[0]);
                updateAgencyEnterpriseRequest.setCityName(namesByCodes[1]);
                updateAgencyEnterpriseRequest.setRegionName(namesByCodes[2]);
            }
            if (changeItemList.contains(AgencyFormChangeItemEnum.ADDRESS.getCode())) {
                updateAgencyEnterpriseRequest.setAddress(agencyFormDO.getAddress());
            }
            updateAgencyEnterpriseRequest.setOpUserId(1L);
            requestList.add(updateAgencyEnterpriseRequest);
        }
        if (CollUtil.isNotEmpty(requestList)) {
            log.info("approveToUpdate 开始入库:[{}]", requestList);
            crmEnterpriseApi.updateAgencyEnterpriseList(requestList);
        }
        if (CollUtil.isNotEmpty(updateList)) {
            log.info("approveToUpdate 入库失败:[{}]", updateList);
            this.updateBatchById(updateList);
        }
        return true;
    }
}
