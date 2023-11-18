package com.yiling.admin.data.center.enterprise.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.admin.data.center.enterprise.form.QueryUserPageListForm;
import com.yiling.admin.data.center.enterprise.form.UnbindStaffEnterpriseForm;
import com.yiling.admin.data.center.enterprise.form.UpdateEmployeeStatusForm;
import com.yiling.admin.data.center.enterprise.form.UpdateStaffMobileForm;
import com.yiling.admin.data.center.enterprise.form.UpdateStaffNameForm;
import com.yiling.admin.data.center.enterprise.form.UpdateUserStatusForm;
import com.yiling.admin.data.center.enterprise.vo.UserDetaiPageVO;
import com.yiling.admin.data.center.enterprise.vo.UserPageListItemVO;
import com.yiling.admin.data.center.enterprise.vo.UserVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.request.RemoveEmployeeRequest;
import com.yiling.user.enterprise.dto.request.UpdateEmployeeStatusRequest;
import com.yiling.user.system.api.RoleApi;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.RoleDTO;
import com.yiling.user.system.dto.UserAttachmentDTO;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.request.QueryStaffPageListRequest;
import com.yiling.user.system.dto.request.UpdateUserRequest;
import com.yiling.user.system.dto.request.UpdateUserStatusRequest;
import com.yiling.user.system.enums.PermissionAppEnum;
import com.yiling.user.system.enums.UserAttachmentTypeEnum;
import com.yiling.user.system.enums.UserAuthsAppIdEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 员工账号模块 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
@Slf4j
@RestController
@RequestMapping("/staff")
@Api(tags = "员工账号模块接口")
public class StaffController extends BaseController {

    @DubboReference
    UserApi            userApi;
    @DubboReference
    StaffApi staffApi;
    @DubboReference
    EnterpriseApi      enterpriseApi;
    @DubboReference
    EmployeeApi        employeeApi;
    @DubboReference
    RoleApi            roleApi;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "用户信息分页列表")
    @PostMapping("/pageList")
    public Result<Page<UserPageListItemVO>> pageList(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody QueryUserPageListForm form) {
        QueryStaffPageListRequest request = PojoUtils.map(form, QueryStaffPageListRequest.class);

        Page<Staff> page = staffApi.pageList(request);
        List<Staff> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Result.success(request.getPage());
        }

        List<Long> userIds = records.stream().map(Staff::getId).distinct().collect(Collectors.toList());
        Map<Long, List<EnterpriseDTO>> userEnterpriseDTOListMap = enterpriseApi.listByUserIds(userIds);

        List<Long> createUserIds = records.stream().map(Staff::getCreateUser).distinct().collect(Collectors.toList());
        List<Long> updateUserIds = records.stream().map(Staff::getUpdateUser).distinct().collect(Collectors.toList());
        List<Long> opUserIds = CollUtil.union(createUserIds, updateUserIds).stream().distinct().collect(Collectors.toList());
        List<UserDTO> userDTOList = userApi.listByIds(opUserIds);
        Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));

        List<UserPageListItemVO> list = Lists.newArrayList();
        records.forEach(e -> {
            UserPageListItemVO item = new UserPageListItemVO();
            UserVO staffVO = PojoUtils.map(e, UserVO.class);
            staffVO.setCreateUserName(userDTOMap.getOrDefault(e.getCreateUser(), new UserDTO()).getName());
            staffVO.setUpdateUserName(userDTOMap.getOrDefault(e.getUpdateUser(), new UserDTO()).getName());
            item.setStaffInfo(staffVO);
            // 获取账号所属的企业名称列表
            List<EnterpriseDTO> userEnterpriseDTOList = userEnterpriseDTOListMap.get(e.getId());
            if (CollUtil.isNotEmpty(userEnterpriseDTOList)) {
                item.setEnterpriseNames(userEnterpriseDTOList.stream().map(EnterpriseDTO::getName).collect(Collectors.toList()));
            } else {
                item.setEnterpriseNames(ListUtil.empty());
            }

            list.add(item);
        });

        Page<UserPageListItemVO> pageVO = PojoUtils.map(page, UserPageListItemVO.class);
        pageVO.setRecords(list);

        return Result.success(pageVO);
    }

    @ApiOperation(value = "获取用户详情")
    @GetMapping("/get")
    public Result<UserDetaiPageVO> get(@RequestParam @ApiParam(name = "id", value = "用户ID", required = true) Long id) {
        UserDetaiPageVO userDetaiPageVO = new UserDetaiPageVO();

        // 用户信息
        UserDTO userDTO = userApi.getById(id);
        userDetaiPageVO.setStaffVO(PojoUtils.map(userDTO, UserVO.class));

        // 用户身份证照片
        {
            List<UserAttachmentDTO> userAttachmentDTOList = userApi.listUserAttachmentsByType(id, ListUtil.toList(UserAttachmentTypeEnum.ID_CARD_FRONT_PHOTO.getCode(), UserAttachmentTypeEnum.ID_CARD_BACK_PHOTO.getCode()));
            // 填充VO
            UserDetaiPageVO.IdCardPhotoVO idCardPhotoVO = new UserDetaiPageVO.IdCardPhotoVO();
            UserAttachmentDTO idCartFrontPhotoDTO = userAttachmentDTOList.stream().filter(e -> UserAttachmentTypeEnum.getByCode(e.getFileType()) == UserAttachmentTypeEnum.ID_CARD_FRONT_PHOTO).findFirst().orElse(null);
            if (idCartFrontPhotoDTO != null) {
                idCardPhotoVO.setFrontPhoto(fileService.getUrl(idCartFrontPhotoDTO.getFileKey(), FileTypeEnum.ID_CARD_FRONT_PHOTO));
            }
            UserAttachmentDTO idCartBackPhotoDTO = userAttachmentDTOList.stream().filter(e -> UserAttachmentTypeEnum.getByCode(e.getFileType()) == UserAttachmentTypeEnum.ID_CARD_BACK_PHOTO).findFirst().orElse(null);
            if (idCartBackPhotoDTO != null) {
                idCardPhotoVO.setBackPhoto(fileService.getUrl(idCartBackPhotoDTO.getFileKey(), FileTypeEnum.ID_CARD_BACK_PHOTO));
            }
            userDetaiPageVO.setIdCardPhotoVO(idCardPhotoVO);
        }

        // 企业及所属角色
        {
            List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByUserId(id, EnableStatusEnum.ALL);
            if (CollUtil.isEmpty(enterpriseDTOList)) {
                userDetaiPageVO.setEnterpriseList(ListUtil.empty());
                return Result.success(userDetaiPageVO);
            }

            // 所属在企业的员工信息
            List<EnterpriseEmployeeDTO> enterpriseEmployeeDTOList = employeeApi.listByUserId(id, EnableStatusEnum.ALL);
            Map<Long, EnterpriseEmployeeDTO> enterpriseEmployeeDTOMap = enterpriseEmployeeDTOList.stream().collect(Collectors.toMap(EnterpriseEmployeeDTO::getEid, Function.identity()));

            // 所属企业ID集合
            List<Long> eids = enterpriseDTOList.stream().map(EnterpriseDTO::getId).distinct().collect(Collectors.toList());
            // 所属企业对应的角色集合Map
            Map<Long, List<RoleDTO>> roleInfoDTOMap = roleApi.listByUserIdEids(PermissionAppEnum.MALL_ADMIN, id, eids);

            List<UserDetaiPageVO.SimpleEnterpriseVO> enterpriseVOList = PojoUtils.map(enterpriseDTOList, UserDetaiPageVO.SimpleEnterpriseVO.class);
            enterpriseVOList.forEach(e -> {
                EnterpriseEmployeeDTO enterpriseEmployeeDTO = enterpriseEmployeeDTOMap.get(e.getId());
                if (enterpriseEmployeeDTO != null) {
                    if (enterpriseEmployeeDTO.getAdminFlag() == 1) {
                        e.setRoleName("企业管理员");
                    } else {
                        List<RoleDTO> roleDTOList = roleInfoDTOMap.get(e.getId());
                        if (CollUtil.isNotEmpty(roleDTOList)) {
                            e.setRoleName(StrUtil.join(" ", roleDTOList.stream().map(RoleDTO::getName).collect(Collectors.toList())));
                        } else {
                            e.setRoleName("无");
                        }
                    }
                    e.setStatus(enterpriseEmployeeDTO.getStatus());
                }
            });
            userDetaiPageVO.setEnterpriseList(enterpriseVOList);
        }

        return Result.success(userDetaiPageVO);
    }

    @ApiOperation(value = "修改用户状态")
    @PostMapping("/updateStatus")
    @Log(title = "修改用户状态", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> updateStatus(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateUserStatusForm form) {
        UpdateUserStatusRequest request = PojoUtils.map(form, UpdateUserStatusRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());

        boolean result = userApi.updateStatus(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "修改姓名")
    @PostMapping("/updateName")
    @Log(title = "修改姓名", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> updateName(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateStaffNameForm form) {
        UpdateUserRequest request = PojoUtils.map(form, UpdateUserRequest.class);
        request.setAppId(UserAuthsAppIdEnum.MALL.getCode());
        request.setOpUserId(adminInfo.getCurrentUserId());

        boolean result = userApi.updateUserInfo(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "修改手机号")
    @PostMapping("/updateMobile")
    @Log(title = "修改手机号", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> updateMobile(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateStaffMobileForm form) {
        String mobile = form.getMobile();
        Staff staff = staffApi.getByMobile(mobile);
        if (staff != null && staff.getId().equals(form.getId())) {
            return Result.failed("手机号已存在");
        }

        UpdateUserRequest request = PojoUtils.map(form, UpdateUserRequest.class);
        request.setAppId(UserAuthsAppIdEnum.MALL.getCode());
        request.setOpUserId(adminInfo.getCurrentUserId());

        boolean result = userApi.updateUserInfo(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "解除账号与企业的绑定关系")
    @PostMapping("/unbindEnterprise")
    @Log(title = "解除账号与企业的绑定关系", businessType = BusinessTypeEnum.DELETE)
    public Result<BoolObject> unbindEnterprise(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UnbindStaffEnterpriseForm form) {
        RemoveEmployeeRequest request = PojoUtils.map(form, RemoveEmployeeRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());

        boolean result = employeeApi.remove(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "修改企业员工账号状态")
    @PostMapping("/updateEmployeeStatus")
    @Log(title = "修改企业员工账号状态", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> updateEmployeeStatus(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateEmployeeStatusForm form) {
        UpdateEmployeeStatusRequest request = PojoUtils.map(form, UpdateEmployeeStatusRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());

        boolean result = employeeApi.updateStatus(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "重置密码")
    @GetMapping("/resetPassword")
    @Log(title = "重置密码", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> resetPassword(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam @ApiParam(name = "mobile", value = "用户手机号", required = true) String mobile) {
        Staff staff = staffApi.getByMobile(mobile);
        if (staff == null) {
            return Result.failed("手机号对应的账号不存在");
        }

        boolean result = staffApi.resetPassword(staff.getId(), adminInfo.getCurrentUserId());
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "根据手机号获取用户信息")
    @GetMapping("/getByMobile")
    public Result<UserVO> getByMobile(@RequestParam(required = true) @ApiParam(value = "手机号", required = true) String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return Result.failed("手机号不能为空");
        }

        Staff staff = staffApi.getByMobile(mobile);
        return Result.success(PojoUtils.map(staff, UserVO.class));
    }
}
