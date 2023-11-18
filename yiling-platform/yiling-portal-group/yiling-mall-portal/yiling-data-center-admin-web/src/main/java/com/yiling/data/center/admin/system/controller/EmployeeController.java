package com.yiling.data.center.admin.system.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.data.center.admin.system.form.CreateEmployeeForm;
import com.yiling.data.center.admin.system.form.QueryEmployeePageListForm;
import com.yiling.data.center.admin.system.form.UpdateEmployeeForm;
import com.yiling.data.center.admin.system.form.UpdateEmployeeStatusForm;
import com.yiling.data.center.admin.system.vo.CheckNewEmployeeMobileResultVO;
import com.yiling.data.center.admin.system.vo.EmployeePageListItemVO;
import com.yiling.data.center.admin.system.vo.EmployeeVO;
import com.yiling.data.center.admin.system.vo.SimpleDepartmentVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.util.ValidateUtils;
import com.yiling.user.enterprise.api.DepartmentApi;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.PositionApi;
import com.yiling.user.enterprise.dto.EnterpriseDepartmentDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.EnterprisePositionDTO;
import com.yiling.user.enterprise.dto.request.CreateEmployeeRequest;
import com.yiling.user.enterprise.dto.request.QueryEmployeePageListRequest;
import com.yiling.user.enterprise.dto.request.UpdateEmployeeRequest;
import com.yiling.user.enterprise.dto.request.UpdateEmployeeStatusRequest;
import com.yiling.user.system.api.RoleApi;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.RoleDTO;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 员工模块 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
@RestController
@RequestMapping("/employee")
@Api(tags = "员工模块接口")
@Slf4j
public class EmployeeController extends BaseController {

    @DubboReference
    EmployeeApi   employeeApi;
    @DubboReference
    UserApi       userApi;
    @DubboReference
    StaffApi staffApi;
    @DubboReference
    DepartmentApi departmentApi;
    @DubboReference
    PositionApi   positionApi;
    @DubboReference
    RoleApi       roleApi;

    @ApiOperation(value = "员工分页列表")
    @PostMapping("/pageList")
    public Result<Page<EmployeePageListItemVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryEmployeePageListForm form) {
        QueryEmployeePageListRequest request = PojoUtils.map(form, QueryEmployeePageListRequest.class);
        request.setEid(staffInfo.getCurrentEid());

        Page<EnterpriseEmployeeDTO> page = employeeApi.pageList(request);
        List<EnterpriseEmployeeDTO> list = page.getRecords();
        if (CollUtil.isEmpty(list)) {
            return Result.success(request.getPage());
        }

        // 获取用户字典
        List<Long> userIds = list.stream().map(EnterpriseEmployeeDTO::getUserId).collect(Collectors.toList());
        List<Long> createUserIds = list.stream().map(EnterpriseEmployeeDTO::getCreateUser).distinct().collect(Collectors.toList());
        List<Long> leaderUserIds = list.stream().map(EnterpriseEmployeeDTO::getParentId).distinct().collect(Collectors.toList());
        List<Long> allUserIds = CollUtil.union(userIds, createUserIds, leaderUserIds).stream().distinct().collect(Collectors.toList());
        List<UserDTO> userDTOList = userApi.listByIds(allUserIds);
        Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));

        // 获取部门字典
        List<Long> employeeIds = list.stream().map(EnterpriseEmployeeDTO::getId).distinct().collect(Collectors.toList());
        Map<Long, List<Long>> employeeDepartmentIdsMap = employeeApi.listDepartmentIdsByEmployeeIds(employeeIds);
        List<Long> departmentIds = employeeDepartmentIdsMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<EnterpriseDepartmentDTO> departmentDTOList = departmentApi.listByIds(departmentIds);
        Map<Long, EnterpriseDepartmentDTO> departmentDTOMap = departmentDTOList.stream().collect(Collectors.toMap(EnterpriseDepartmentDTO::getId, Function.identity()));

        // 获取角色字典
        Map<Long, List<RoleDTO>> userRolesMap = roleApi.listByEidUserIds(PermissionAppEnum.MALL_ADMIN, staffInfo.getCurrentEid(), userIds);

        List<EmployeePageListItemVO> voList = CollUtil.newArrayList();
        for (EnterpriseEmployeeDTO item : list) {
            EmployeePageListItemVO vo = PojoUtils.map(item, EmployeePageListItemVO.class);
            vo.setId(item.getUserId());
            // 用户信息
            UserDTO userDTO = userDTOMap.get(item.getUserId());
            if (userDTO != null) {
                vo.setName(userDTO.getName());
                vo.setMobile(userDTO.getMobile());
            }
            vo.setParentName(userDTOMap.getOrDefault(item.getParentId(), new UserDTO()).getName());
            vo.setCreateUserName(userDTOMap.getOrDefault(item.getCreateUser(), new UserDTO()).getName());
            // 部门名
            List<Long> userDepartmentIds = employeeDepartmentIdsMap.get(item.getId());
            if (CollUtil.isNotEmpty(userDepartmentIds)) {
                StringBuilder departmentNameBuilder = new StringBuilder();
                userDepartmentIds.forEach(departmentId -> {
                    EnterpriseDepartmentDTO enterpriseDepartmentDTO = departmentDTOMap.get(departmentId);
                    if (enterpriseDepartmentDTO != null) {
                        departmentNameBuilder.append(enterpriseDepartmentDTO.getName()).append("/");
                    }
                });
                vo.setDepartmentName(StrUtil.removeSuffix(departmentNameBuilder.toString(), "/"));
            }

            // 角色名
            List<RoleDTO> userRoles = userRolesMap.get(item.getUserId());
            if (CollUtil.isNotEmpty(userRoles)) {
                vo.setRoleName(userRoles.stream().map(RoleDTO::getName).collect(Collectors.joining("/")));
            }
            voList.add(vo);
        }

        Page<EmployeePageListItemVO> pageVO = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        pageVO.setRecords(voList);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "获取员工详情")
    @GetMapping("/get")
    public Result<EmployeeVO> get(@CurrentUser CurrentStaffInfo staffInfo,
                                  @ApiParam(value = "用户ID", required = true) @RequestParam("userId") Long userId) {
        UserDTO userDTO = userApi.getById(userId);
        if (userDTO == null) {
            return Result.failed("用户信息不存在");
        }

        EnterpriseEmployeeDTO enterpriseEmployeeDTO = employeeApi.getByEidUserId(staffInfo.getCurrentEid(), userId);
        if (enterpriseEmployeeDTO == null) {
            return Result.failed("员工信息不存在");
        }

        EmployeeVO employeeVO = new EmployeeVO();
        employeeVO.setUserId(userId);
        employeeVO.setName(userDTO.getName());
        employeeVO.setMobile(userDTO.getMobile());
        employeeVO.setCode(enterpriseEmployeeDTO.getCode());
        employeeVO.setType(enterpriseEmployeeDTO.getType());

        // 职位信息
        Long positionId = enterpriseEmployeeDTO.getPositionId();
        if (positionId != null && positionId != 0L) {
            EnterprisePositionDTO enterprisePositionDTO = positionApi.getById(positionId);
            employeeVO.setPositionId(positionId);
            employeeVO.setPositionName(enterprisePositionDTO.getName());
        }

        // 角色信息
        List<RoleDTO> roleDTOList = roleApi.listByEidUserId(PermissionAppEnum.MALL_ADMIN, staffInfo.getCurrentEid(), userId);
        if (CollUtil.isNotEmpty(roleDTOList)) {
            employeeVO.setRoleId(roleDTOList.get(0).getId());
            employeeVO.setRoleName(roleDTOList.get(0).getName());
        }

        // 上级领导信息
        Long parentId = enterpriseEmployeeDTO.getParentId();
        if (parentId != null && parentId != 0L) {
            UserDTO parentUserDTO = userApi.getById(enterpriseEmployeeDTO.getParentId());
            employeeVO.setParentId(parentUserDTO.getId());
            employeeVO.setParentName(parentUserDTO.getName());
        }

        // 所属部门信息
        List<Long> departmentIds = employeeApi.listDepartmentIdsByUser(enterpriseEmployeeDTO.getEid(), enterpriseEmployeeDTO.getUserId());
        List<EnterpriseDepartmentDTO> departmentDTOList = departmentApi.listByIds(departmentIds);
        employeeVO.setDepartmentList(PojoUtils.map(departmentDTOList, SimpleDepartmentVO.class));

        return Result.success(employeeVO);
    }

    @ApiOperation(value = "创建员工信息")
    @PostMapping("/create")
    @Log(title = "创建员工信息", businessType = BusinessTypeEnum.INSERT)
    public Result create(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody CreateEmployeeForm form) {
        // 当前企业是否为以岭
        boolean isYiling = staffInfo.getYilingFlag();

        Class[] groups;
        if (isYiling) {
            groups = new Class[] { CreateEmployeeForm.YilingEmployeeCreateValidateGroup.class };
        } else {
            groups = new Class[] { CreateEmployeeForm.CommonEmployeeCreateValidateGroup.class };
        }
        String errorMessage = ValidateUtils.failFastValidate(form, groups);
        if (StrUtil.isNotEmpty(errorMessage)) {
            return Result.validateFailed(errorMessage);
        }

        CreateEmployeeRequest request = PojoUtils.map(form, CreateEmployeeRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setRoleIds(ListUtil.toList(form.getRoleId()));
        request.setOpUserId(staffInfo.getCurrentUserId());
        employeeApi.create(request);
        return Result.success();
    }

    @ApiOperation(value = "修改员工信息")
    @PostMapping("/update")
    @Log(title = "修改员工信息", businessType = BusinessTypeEnum.UPDATE)
    public Result update(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody UpdateEmployeeForm form) {
        // 当前企业是否为以岭
        boolean isYiling = staffInfo.getYilingFlag();

        Class[] groups;
        if (isYiling) {
            groups = new Class[] { UpdateEmployeeForm.YilingEmployeeUpdateValidateGroup.class };
        } else {
            groups = new Class[] { UpdateEmployeeForm.CommonEmployeeUpdateValidateGroup.class };
        }
        String errorMessage = ValidateUtils.failFastValidate(form, groups);
        if (StrUtil.isNotEmpty(errorMessage)) {
            return Result.validateFailed(errorMessage);
        }

        UpdateEmployeeRequest request = PojoUtils.map(form, UpdateEmployeeRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());

        boolean result = employeeApi.update(request);

        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "检查新员工手机号")
    @GetMapping("/checkNewEmployeeMobileNumber")
    public Result<CheckNewEmployeeMobileResultVO> checkNewEmployeeMobileNumber(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam(value = "手机号", required = true) @RequestParam String mobile) {
        Staff staff = staffApi.getByMobile(mobile);
        if (staff == null) {
            return Result.success(new CheckNewEmployeeMobileResultVO());
        }

        boolean exists = employeeApi.exists(staffInfo.getCurrentEid(), staff.getId());
        if (exists) {
            return Result.failed("手机号已存在于员工列表内，请勿重复添加");
        }

        CheckNewEmployeeMobileResultVO resultVO = new CheckNewEmployeeMobileResultVO();
        resultVO.setName(staff.getName());
        return Result.success(resultVO);
    }

    @ApiOperation(value = "修改员工状态")
    @PostMapping("/updateStatus")
    @Log(title = "修改员工状态", businessType = BusinessTypeEnum.UPDATE)
    public Result updateStatus(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateEmployeeStatusForm form) {
        UpdateEmployeeStatusRequest request = PojoUtils.map(form, UpdateEmployeeStatusRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());

        boolean result = employeeApi.updateStatus(request);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }
}
