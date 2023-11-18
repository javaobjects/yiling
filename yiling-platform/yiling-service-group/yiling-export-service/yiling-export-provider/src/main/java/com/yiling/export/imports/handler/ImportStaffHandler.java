package com.yiling.export.imports.handler;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.export.excel.handler.BaseImportHandler;
import com.yiling.export.imports.model.ImportStaffModel;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.DepartmentApi;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.PositionApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseDepartmentDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.EnterprisePositionDTO;
import com.yiling.user.enterprise.dto.request.CreateEmployeeRequest;
import com.yiling.user.enterprise.enums.EmployeeTypeEnum;
import com.yiling.user.system.api.RoleApi;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.RoleDTO;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入员工账号数据验证处理器
 *
 * @author: xuan.zhou
 * @date: 2021/5/27
 */
@Slf4j
@Service
public class ImportStaffHandler extends BaseImportHandler<ImportStaffModel> {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    StaffApi staffApi;
    @DubboReference
    EmployeeApi employeeApi;
    @DubboReference
    PositionApi positionApi;
    @DubboReference
    DepartmentApi departmentApi;
    @DubboReference
    RoleApi roleApi;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportStaffModel model) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

        {
            // 企业ID
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(model.getEid());
            if (enterpriseDTO == null) {
                return this.error("未找到企业ID对应的企业信息");
            }
        }

        {
            // 手机号是否重复
            Staff staff = staffApi.getByMobile(model.getMobile());
            if (staff != null) {
                boolean isExists = employeeApi.exists(model.getEid(), staff.getId());
                if (isExists) {
                    return this.error("手机号对应的员工已存在");
                }
            }
        }

        {
            String employeeTypeStr = model.getEmployeeTypeStr();
            if (StrUtil.isNotBlank(employeeTypeStr)) {
                EmployeeTypeEnum employeeTypeEnum = EmployeeTypeEnum.getByName(employeeTypeStr);
                if (employeeTypeEnum == null) {
                    return this.error("用户类型未定义");
                } else {
                    model.setEmployeeTypeEnum(employeeTypeEnum);
                }
            } else {
                model.setEmployeeTypeEnum(EmployeeTypeEnum.OTHER);
            }
        }

        {
            // 部门ID
            String departmentIdsStr = model.getDepartmentIdsStr();
            if (StrUtil.isNotBlank(departmentIdsStr)) {
                long[] departmentIds = StrUtil.splitToLong(departmentIdsStr, ",");
                if (ArrayUtil.isNotEmpty(departmentIds)) {
                    List<Long> deaprtmentIdList = CollUtil.newArrayList();
                    for (long departmentId : departmentIds) {
                        deaprtmentIdList.add(Long.valueOf(departmentId));
                    }
                    List<EnterpriseDepartmentDTO> enterpriseDepartmentDTOList = departmentApi.listByIds(deaprtmentIdList);
                    if (CollUtil.isEmpty(enterpriseDepartmentDTOList) || enterpriseDepartmentDTOList.size() != deaprtmentIdList.size()) {
                        return this.error("存在部门ID对应的信息未找到情况");
                    }

                    boolean isPresent = enterpriseDepartmentDTOList.stream().filter(e -> !e.getEid().equals(model.getEid())).findFirst().isPresent();
                    if (isPresent) {
                        return this.error("部门ID值中存在错误数据");
                    }

                    model.setDepartmentIds(deaprtmentIdList);
                }
            }
        }

        {
            // 职位ID
            Long positionId = model.getPositionId();
            if (positionId != null && positionId != 0L) {
                EnterprisePositionDTO enterprisePositionDTO = positionApi.getById(positionId);
                if (enterprisePositionDTO == null || !enterprisePositionDTO.getEid().equals(model.getEid())) {
                    return this.error("未找到职位ID对应的职位信息");
                }
            }
        }

        {
            // 上级领导工号
            String parentCode = model.getParentCode();
            if (StrUtil.isNotBlank(parentCode)) {
                EnterpriseEmployeeDTO enterpriseEmployeeDTO = employeeApi.getByCode(model.getEid(), parentCode);
                if (enterpriseEmployeeDTO == null) {
                    return this.error("上级领导工号对应的信息不存在");
                }
                model.setParentId(enterpriseEmployeeDTO.getUserId());
            }
        }

        {
            RoleDTO roleDTO = roleApi.getById(model.getRoleId());
            if (roleDTO == null || !roleDTO.getEid().equals(model.getEid())) {
                return this.error("未找到角色ID对应的角色信息");
            }
        }

        return result;
    }

    @Override
    public List<ImportStaffModel> execute(List<ImportStaffModel> object, Map<String, Object> paramMap) {
        for (ImportStaffModel form : object) {
            try {
                CreateEmployeeRequest request = PojoUtils.map(form, CreateEmployeeRequest.class);
                request.setType(form.getEmployeeTypeEnum().getCode());
                request.setRoleIds(ListUtil.toList(form.getRoleId()));
                request.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID, 0));

                employeeApi.create(request);
            } catch (BusinessException be) {
                form.setErrorMsg(be.getMessage());
            } catch (Exception e) {
                form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                log.error("数据保存出错：{}", e.getMessage(), e);
            }
        }
        return object;
    }
}
