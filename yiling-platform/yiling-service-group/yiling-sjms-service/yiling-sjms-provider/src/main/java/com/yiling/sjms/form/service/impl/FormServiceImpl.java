package com.yiling.sjms.form.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.flowcollect.enums.TransferTypeEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.form.dao.FormMapper;
import com.yiling.sjms.form.dto.request.ApproveFormRequest;
import com.yiling.sjms.form.dto.request.CreateFormRequest;
import com.yiling.sjms.form.dto.request.QueryFormPageListRequest;
import com.yiling.sjms.form.dto.request.RejectFormRequest;
import com.yiling.sjms.form.dto.request.ResubmitFormRequest;
import com.yiling.sjms.form.dto.request.SubmitFormRequest;
import com.yiling.sjms.form.dto.request.UpdateRemarkRequest;
import com.yiling.sjms.form.entity.FormDO;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.form.service.FormService;
import com.yiling.sjms.gb.service.BusinessDepartmentService;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.system.api.SjmsUserApi;
import com.yiling.user.system.bo.SjmsUser;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 表单基础信息 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2023-2-24
 */
@Slf4j
@Service
public class FormServiceImpl implements FormService {

    @Autowired
    FormMapper formMapper;
    @Autowired
    BusinessDepartmentService businessDepartmentService;

    @DubboReference
    EsbEmployeeApi esbEmployeeApi;
    @DubboReference
    SjmsUserApi sjmsUserApi;
    @DubboReference
    CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;

    private Boolean save(FormDO formDO) {
        return this.formMapper.insert(formDO) > 0;
    }

    private Boolean update(FormDO entity, Wrapper<FormDO> queryWrapper) {
        return this.formMapper.update(entity, queryWrapper) > 0;
    }

    @Override
    public Page<FormDO> pageList(QueryFormPageListRequest request) {
        LambdaQueryWrapper<FormDO> lambdaQueryWrapper = Wrappers.lambdaQuery();

        String code = request.getCode();
        if (StrUtil.isNotEmpty(code)) {
            lambdaQueryWrapper.eq(FormDO::getCode, code);
        }

        Integer type = request.getType();
        if (type != null && type != 0) {
            lambdaQueryWrapper.eq(FormDO::getType, type);
        }

        String empId = request.getEmpId();
        if (StrUtil.isNotEmpty(empId)) {
            lambdaQueryWrapper.eq(FormDO::getEmpId, empId);
        }

        Integer status = request.getStatus();
        if (status != null && status != 0) {
            lambdaQueryWrapper.eq(FormDO::getStatus, status);
        }
        if(CollUtil.isNotEmpty(request.getExcludeStatusList())){
            lambdaQueryWrapper.notIn(FormDO::getStatus,request.getExcludeStatusList());
        }
        Date createTimeBegin = request.getCreateTimeBegin();
        if (createTimeBegin != null) {
            lambdaQueryWrapper.ge(FormDO::getCreateTime, DateUtil.beginOfDay(createTimeBegin));
        }

        Date createTimeEnd = request.getCreateTimeEnd();
        if (createTimeEnd != null) {
            lambdaQueryWrapper.le(FormDO::getCreateTime, DateUtil.endOfDay(createTimeEnd));
        }

        Date submitTimeBegin = request.getSubmitTimeBegin();
        if (submitTimeBegin != null) {
            lambdaQueryWrapper.ge(FormDO::getSubmitTime, DateUtil.beginOfDay(submitTimeBegin));
        }

        Date submitTimeEnd = request.getSubmitTimeEnd();
        if (submitTimeEnd != null) {
            lambdaQueryWrapper.le(FormDO::getSubmitTime, DateUtil.endOfDay(submitTimeEnd));
        }
        lambdaQueryWrapper.orderByDesc(FormDO::getId);
        return this.formMapper.selectPage(request.getPage(), lambdaQueryWrapper);
    }

    @Override
    public FormDO getById(Long id) {
        return this.formMapper.selectById(id);
    }

    @Override
    public FormDO getByCode(String code) {
        QueryWrapper<FormDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FormDO::getCode, code).last("limit 1");
        return this.formMapper.selectOne(queryWrapper);
    }

    @Override
    public FormDO getByFlowId(String flowId) {
        QueryWrapper<FormDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FormDO::getFlowId, flowId)
                .last("limit 1");
        return this.formMapper.selectOne(queryWrapper);
    }

    @Override
    public List<FormDO> listByIds(List<Long> ids) {
        List<FormDO> list = this.formMapper.selectBatchIds(ids);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public List<FormDO> listUnsubmitFormsByUser(FormTypeEnum formTypeEnum, Long userId) {
        QueryWrapper<FormDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FormDO::getType, formTypeEnum.getCode())
                .eq(FormDO::getCreateUser, userId)
                .eq(FormDO::getStatus, FormStatusEnum.UNSUBMIT.getCode());

        List<FormDO> list = this.formMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public Long create(CreateFormRequest request) {
        log.info("准备创建基础表单信息 -> {}", JSONUtil.toJsonStr(request));

        FormDO entity = new FormDO();
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setType(request.getType());
        entity.setStatus(FormStatusEnum.UNSUBMIT.getCode());
        entity.setRemark(request.getRemark());
        entity.setOpUserId(request.getOpUserId());
        entity.setTransferType(request.getTransferType());

        // 用户信息
        SjmsUser user = sjmsUserApi.getById(request.getOpUserId());
        // 员工信息
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(user.getEmpId());
        entity.setBizArea(esbEmployeeDTO.getYxArea());
        entity.setBizDept(esbEmployeeDTO.getYxDept());
        entity.setBizProvince(esbEmployeeDTO.getYxProvince());
        entity.setEmpId(esbEmployeeDTO.getEmpId());
        entity.setEmpName(esbEmployeeDTO.getEmpName());
        entity.setDeptId(esbEmployeeDTO.getDeptId());
        entity.setDeptName(esbEmployeeDTO.getDeptName());
        // 获取申请人对应的事业部信息
        EsbOrganizationDTO esbOrganizationDTO = businessDepartmentService.getByOrgId(esbEmployeeDTO.getDeptId());
        if (esbOrganizationDTO != null) {
            entity.setBdDeptId(esbOrganizationDTO.getOrgId());
            entity.setBdDeptName(esbOrganizationDTO.getOrgName());
        }
        // 获取省份信息
        String province = crmEnterpriseRelationShipApi.getProvinceAreaByThreeParms(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince());
        entity.setProvince(province);

        boolean result = this.save(entity);
        if (!result) {
            log.error("创建表单基础信息失败");
            throw new BusinessException(ResultCode.FAILED, "创建表单基础信息失败");
        }

        log.info("创建表单基础信息成功 -> {}", entity);
        return entity.getId();
    }

    @Override
    public Boolean submit(SubmitFormRequest request) {
        log.info("准备提交基础表单信息 -> {}", JSONUtil.toJsonStr(request));

        FormDO entity = new FormDO();
        entity.setFlowId(request.getFlowId());
        entity.setFlowTplId(request.getFlowTplId());
        entity.setFlowTplName(request.getFlowTplName());
        entity.setFlowVersion(request.getFlowVersion());
        entity.setStatus(FormStatusEnum.AUDITING.getCode());
        entity.setSubmitTime(request.getOpTime());
        entity.setOpUserId(request.getOpUserId());

        QueryWrapper<FormDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FormDO::getId, request.getId())
                .in(FormDO::getStatus, ListUtil.toList(FormStatusEnum.UNSUBMIT.getCode(), FormStatusEnum.REJECT.getCode()));

        boolean result = this.update(entity, queryWrapper);
        if (!result) {
            log.error("提交基础表单信息失败，单据状态已发生变更");
            throw new BusinessException(ResultCode.FAILED, "提交基础表单信息失败，单据状态已发生变更");
        }

        log.info("提交基础表单信息成功");
        return true;
    }

    @Override
    public Boolean reject(RejectFormRequest request) {
        log.info("准备驳回基础表单信息 -> {}", JSONUtil.toJsonStr(request));

        FormDO entity = new FormDO();
        entity.setStatus(FormStatusEnum.REJECT.getCode());
        entity.setOpUserId(request.getOpUserId());

        QueryWrapper<FormDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FormDO::getId, request.getId())
                .eq(FormDO::getStatus, FormStatusEnum.AUDITING.getCode());

        boolean result = this.update(entity, queryWrapper);
        if (!result) {
            log.error("驳回基础表单信息失败，单据状态已发生变更");
            throw new BusinessException(ResultCode.FAILED, "驳回基础表单信息失败，单据状态已发生变更");
        }

        log.info("驳回基础表单信息成功");
        return true;
    }

    @Override
    public Boolean approve(ApproveFormRequest request) {
        log.info("准备审批通过基础表单信息 -> {}", JSONUtil.toJsonStr(request));

        FormDO entity = new FormDO();
        entity.setStatus(FormStatusEnum.APPROVE.getCode());
        entity.setApproveTime(request.getOpTime());
        entity.setOpUserId(request.getOpUserId());

        QueryWrapper<FormDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FormDO::getId, request.getId())
                .eq(FormDO::getStatus, FormStatusEnum.AUDITING.getCode());

        boolean result = this.update(entity, queryWrapper);
        if (!result) {
            log.error("审批通过基础表单信息失败，单据状态已发生变更");
            throw new BusinessException(ResultCode.FAILED, "审批通过基础表单信息失败，单据状态已发生变更");
        }

        log.info("审批通过基础表单信息成功");
        return true;
    }

    @Override
    public Boolean resubmit(ResubmitFormRequest request) {
        log.info("准备重新提交基础表单信息 -> {}", JSONUtil.toJsonStr(request));

        FormDO entity = new FormDO();
        entity.setStatus(FormStatusEnum.AUDITING.getCode());
        entity.setSubmitTime(request.getOpTime());
        entity.setOpUserId(request.getOpUserId());

        QueryWrapper<FormDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FormDO::getId, request.getId())
                .eq(FormDO::getStatus, FormStatusEnum.REJECT.getCode());

        boolean result = this.update(entity, queryWrapper);
        if (!result) {
            log.error("重新提交基础表单信息失败，单据状态已发生变更");
            throw new BusinessException(ResultCode.FAILED, "重新提交基础表单信息失败，单据状态已发生变更");
        }

        log.info("重新提交基础表单信息成功");
        return true;
    }

    @Override
    public Boolean delete(Long id, Long opUserId) {
        log.info("准备删除基础表单信息 -> id={}, opUserId={}", id, opUserId);

        FormDO entity = new FormDO();
        entity.setOpUserId(opUserId);

        QueryWrapper<FormDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FormDO::getId, id)
                .eq(FormDO::getStatus, FormStatusEnum.UNSUBMIT.getCode());

        int n = this.formMapper.batchDeleteWithFill(entity, queryWrapper);
        if (n == 0) {
            log.error("删除基础表单信息失败，单据状态已发生变更");
            throw new BusinessException(ResultCode.FAILED, "删除基础表单信息失败，单据状态已发生变更");
        }

        log.info("删除基础表单信息成功");
        return true;
    }

    @Override
    public Boolean updateRemark(UpdateRemarkRequest remarkRequest) {
        FormDO formDO = new FormDO();
        PojoUtils.map(remarkRequest,formDO);
        int result = this.formMapper.updateById(formDO);
        if(result>0){
            return true;
        }
        return false;
    }

    @Override
    public List<FormDO> listByCodes(List<String> codes) {

        QueryWrapper<FormDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(FormDO :: getCode,codes);
        return this.formMapper.selectList(queryWrapper);
    }

    @Override
    public Boolean updateTransferType(Long id, Integer transferType) {
        FormDO formDO = new FormDO();
        formDO.setTransferType(transferType);
        formDO.setId(id);
        int result = this.formMapper.updateById(formDO);
        if(result>0){
            return true;
        }
        return false;
    }

    @Override
    public List<Long> formIdByTransferType(Integer transferType, List<Long> formIds) {
        LambdaQueryWrapper<FormDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FormDO::getTransferType, transferType).in(FormDO::getId, formIds);
        return this.formMapper.selectList(queryWrapper).stream().map(FormDO::getId).collect(Collectors.toList());
    }

}
