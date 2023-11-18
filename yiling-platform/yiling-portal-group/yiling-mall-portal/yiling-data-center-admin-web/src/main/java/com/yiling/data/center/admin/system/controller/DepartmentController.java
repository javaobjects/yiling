package com.yiling.data.center.admin.system.controller;

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
import com.yiling.data.center.admin.system.form.AddDepartmentForm;
import com.yiling.data.center.admin.system.form.MoveDepartmentEmployeeForm;
import com.yiling.data.center.admin.system.form.QueryDepartmentPageListForm;
import com.yiling.data.center.admin.system.form.UpdateDepartmentForm;
import com.yiling.data.center.admin.system.form.UpdateDepartmentStatusForm;
import com.yiling.data.center.admin.system.vo.DepartmentVO;
import com.yiling.data.center.admin.system.vo.SimpleDepartmentVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.user.enterprise.api.DepartmentApi;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.dto.EnterpriseDepartmentDTO;
import com.yiling.user.enterprise.dto.request.AddDepartmentRequest;
import com.yiling.user.enterprise.dto.request.MoveDepartmentEmployeeRequest;
import com.yiling.user.enterprise.dto.request.QueryDepartmentPageListRequest;
import com.yiling.user.enterprise.dto.request.UpdateDepartmentRequest;
import com.yiling.user.enterprise.dto.request.UpdateDepartmentStatusRequest;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 部门管理
 * @author: gxl
 * @date: 2021/5/27
 */
@RestController
@RequestMapping("/department")
@Api(tags = "部门模块接口")
@Slf4j
public class DepartmentController extends BaseController {
    @DubboReference
    DepartmentApi departmentApi;
    @DubboReference
    EmployeeApi   employeeApi;
    @DubboReference
    UserApi       userApi;

    @ApiOperation(value = "部门分页列表")
    @PostMapping("/pageList")
    public Result<Page<DepartmentVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryDepartmentPageListForm form) {
        QueryDepartmentPageListRequest request = PojoUtils.map(form, QueryDepartmentPageListRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        Page<EnterpriseDepartmentDTO> page = departmentApi.pageList(request);

        List<EnterpriseDepartmentDTO> list = page.getRecords();
        if (CollUtil.isEmpty(list)) {
            return Result.success(new Page<>());
        }

        // 创建人、负责人字典
        List<Long> createUserIds = list.stream().map(EnterpriseDepartmentDTO::getCreateUser).distinct().collect(Collectors.toList());
        List<Long> managerUserIds = list.stream().map(EnterpriseDepartmentDTO::getManagerId).distinct().collect(Collectors.toList());
        List<Long> allUserIds = CollUtil.addAllIfNotContains(createUserIds, managerUserIds);
        List<UserDTO> userDTOList = userApi.listByIds(allUserIds);
        Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));

        // 上级部门字典
        List<Long> parentIds = list.stream().map(EnterpriseDepartmentDTO::getParentId).distinct().collect(Collectors.toList());
        List<EnterpriseDepartmentDTO> departmentDTOList = departmentApi.listByIds(parentIds);
        Map<Long, EnterpriseDepartmentDTO> departmentDTOMap = departmentDTOList.stream().collect(Collectors.toMap(EnterpriseDepartmentDTO::getId, Function.identity()));

        // 部门人数
        List<Long> departmentIds = list.stream().map(EnterpriseDepartmentDTO::getId).collect(Collectors.toList());
        Map<Long, Long> departmentEmployeeNumMap = employeeApi.countByDepartmentIds(departmentIds);

        List<DepartmentVO> recordList = CollUtil.newArrayList();
        page.getRecords().forEach(e -> {
            DepartmentVO itemVO = new DepartmentVO();
            PojoUtils.map(e, itemVO);
            itemVO.setManagerName(MapUtil.get(userDTOMap, e.getManagerId(), UserDTO.class, new UserDTO()).getName());
            itemVO.setEmployeeNum(MapUtil.get(departmentEmployeeNumMap, e.getId(), Long.class, 0L));
            itemVO.setParentName(MapUtil.get(departmentDTOMap, e.getParentId(), EnterpriseDepartmentDTO.class, new EnterpriseDepartmentDTO()).getName());
            itemVO.setCreateUserName(MapUtil.get(userDTOMap, e.getCreateUser(), UserDTO.class, new UserDTO()).getName());
            recordList.add(itemVO);
        });

        Page<DepartmentVO> pageVO = PojoUtils.map(page, DepartmentVO.class);
        pageVO.setRecords(recordList);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "获取部门详情")
    @GetMapping("/get")
    public Result<DepartmentVO> get(@RequestParam @ApiParam(value = "部门ID", required = true) Long id) {
        EnterpriseDepartmentDTO departmentDTO = departmentApi.getById(id);
        if (departmentDTO == null) {
            return Result.failed("部门信息不存在");
        }

        DepartmentVO departmentVO = PojoUtils.map(departmentDTO, DepartmentVO.class);
        // 部门人数
        int departmentEmployeeNum = employeeApi.countByDepartmentId(id);
        departmentVO.setEmployeeNum(Convert.toLong(departmentEmployeeNum));
        // 上级部门
        Long parentId = departmentVO.getParentId();
        if (parentId != null && parentId != 0L) {
            EnterpriseDepartmentDTO parentDepartmentDTO = departmentApi.getById(parentId);
            departmentVO.setParentName(parentDepartmentDTO != null ? parentDepartmentDTO.getName() : null);
        }
        // 部分负责人
        Long managerId = departmentVO.getManagerId();
        if (managerId != null && managerId != 0L) {
            UserDTO userDTO = userApi.getById(managerId);
            departmentVO.setManagerName(userDTO != null ? userDTO.getName() : null);
        }

        return Result.success(departmentVO);
    }

    @ApiOperation(value = "添加部门信息")
    @PostMapping("/add")
    @Log(title = "添加部门信息", businessType = BusinessTypeEnum.INSERT)
    public Result add(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AddDepartmentForm form){
        AddDepartmentRequest request = PojoUtils.map(form, AddDepartmentRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean result = departmentApi.add(request);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "修改部门信息")
    @PostMapping("/update")
    @Log(title = "修改部门信息", businessType = BusinessTypeEnum.UPDATE)
    public Result update(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateDepartmentForm form){
        UpdateDepartmentRequest request = PojoUtils.map(form, UpdateDepartmentRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean result = departmentApi.update(request);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "修改部门状态")
    @PostMapping("/updateStatus")
    @Log(title = "修改部门状态", businessType = BusinessTypeEnum.UPDATE)
    public Result updateStatus(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateDepartmentStatusForm form){
        int departmentEmployeeNum = employeeApi.countByDepartmentId(form.getId());
        if (departmentEmployeeNum > 0) {
            return Result.failed("请移除员工后再操作停用");
        }

        UpdateDepartmentStatusRequest request = PojoUtils.map(form, UpdateDepartmentStatusRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean result = departmentApi.updateStatus(request);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "批量转移部门员工")
    @PostMapping("/moveEmployee")
    @Log(title = "批量转移部门员工", businessType = BusinessTypeEnum.UPDATE)
    public Result moveEmployee(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid MoveDepartmentEmployeeForm form){
        EnterpriseDepartmentDTO sourceDepartmentDTO = departmentApi.getById(form.getSourceId());
        if (sourceDepartmentDTO == null || !sourceDepartmentDTO.getEid().equals(staffInfo.getCurrentEid())) {
            return Result.failed("参数错误：来源部门ID不正确");
        }

        EnterpriseDepartmentDTO targetDepartmentDTO = departmentApi.getById(form.getTargetId());
        if (targetDepartmentDTO == null || !targetDepartmentDTO.getEid().equals(staffInfo.getCurrentEid())) {
            return Result.failed("参数错误：目标部门ID不正确");
        }

        if (EnableStatusEnum.getByCode(targetDepartmentDTO.getStatus()).equals(EnableStatusEnum.DISABLED)) {
            return Result.failed("目标部门已停用");
        }

        int count = employeeApi.countByDepartmentId(form.getSourceId());
        if (count == 0) {
            return Result.failed("该部门下没有员工，无需转移");
        }

        MoveDepartmentEmployeeRequest request = PojoUtils.map(form, MoveDepartmentEmployeeRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        boolean result = departmentApi.moveEmployee(request);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "获取下级部门列表")
    @GetMapping("/listByParentId")
    public Result<CollectionObject<SimpleDepartmentVO>> listByParentId(@CurrentUser CurrentStaffInfo staffInfo,
                                                                       @RequestParam @ApiParam(value = "上级部门ID", defaultValue = "0") Long parentId){
        parentId = Convert.toLong(parentId, 0L);
        if (parentId != 0L) {
            EnterpriseDepartmentDTO enterpriseDepartmentDTO = departmentApi.getById(parentId);
            if (enterpriseDepartmentDTO == null) {
                return Result.failed("上级部门ID对应的信息未找到");
            } else if (!enterpriseDepartmentDTO.getEid().equals(staffInfo.getCurrentEid())) {
                return Result.failed("上级部门ID不正确");
            }
        }

        List<EnterpriseDepartmentDTO> list = departmentApi.listByParentId(staffInfo.getCurrentEid(), parentId);
        // 只显示可用部门
        list = list.stream().filter(e -> e.getStatus().equals(EnableStatusEnum.ENABLED.getCode())).collect(Collectors.toList());
        return Result.success(new CollectionObject<>(PojoUtils.map(list, SimpleDepartmentVO.class)));
    }

}