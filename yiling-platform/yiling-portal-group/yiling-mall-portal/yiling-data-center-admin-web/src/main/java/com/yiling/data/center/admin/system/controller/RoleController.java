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
import com.yiling.data.center.admin.system.form.CreateRoleForm;
import com.yiling.data.center.admin.system.form.MoveRoleUsersForm;
import com.yiling.data.center.admin.system.form.QueryRolePageListForm;
import com.yiling.data.center.admin.system.form.UpdateRoleForm;
import com.yiling.data.center.admin.system.form.UpdateRoleStatusForm;
import com.yiling.data.center.admin.system.vo.RoleListItemVO;
import com.yiling.data.center.admin.system.vo.RoleVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterprisePlatformDTO;
import com.yiling.user.system.api.MenuApi;
import com.yiling.user.system.api.RoleApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.MenuListItemDTO;
import com.yiling.user.system.dto.RoleDTO;
import com.yiling.user.system.dto.RoleDataScopeDTO;
import com.yiling.user.system.dto.RoleListItemDTO;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.request.CreateMallCustomRoleRequest;
import com.yiling.user.system.dto.request.MoveRoleUsersRequest;
import com.yiling.user.system.dto.request.QueryRolePageListRequest;
import com.yiling.user.system.dto.request.UpdateMallCustomRoleRequest;
import com.yiling.user.system.enums.MenuTypeEnum;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 角色管理 Controller
 *
 * @author xuan.zhou
 * @date 2021/7/7
 */
@RestController
@RequestMapping("/role")
@Api(tags = "角色管理模块接口")
@Slf4j
public class RoleController {

	@DubboReference
	MenuApi menuApi;
	@DubboReference
	RoleApi roleApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    UserApi userApi;

	@ApiOperation(value = "角色管理分页列表")
	@PostMapping("/pageList")
	public Result<Page<RoleListItemVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryRolePageListForm form) {
		QueryRolePageListRequest request = PojoUtils.map(form, QueryRolePageListRequest.class);
		request.setEid(staffInfo.getCurrentEid());
		request.setAppEnum(PermissionAppEnum.MALL_ADMIN);

		Page<RoleListItemDTO> pageList = roleApi.queryRolePageList(request);
        if (CollUtil.isEmpty(pageList.getRecords())) {
            return Result.success(form.getPage());
        }

        List<Long> userIds = pageList.getRecords().stream().map(RoleListItemDTO::getCreateUser).distinct().collect(Collectors.toList());
        List<UserDTO> userDTOList = userApi.listByIds(userIds);
        Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));

        Page<RoleListItemVO> voPageList = PojoUtils.map(pageList, RoleListItemVO.class);
        voPageList.getRecords().forEach(e -> e.setCreateUserName(userDTOMap.getOrDefault(e.getCreateUser(), new UserDTO()).getName()));
		return Result.success(voPageList);
	}

	@ApiOperation(value = "创建角色")
	@PostMapping("/create")
    @Log(title = "创建角色", businessType = BusinessTypeEnum.INSERT)
	public Result create(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid CreateRoleForm form) {
		CreateMallCustomRoleRequest request = PojoUtils.map(form, CreateMallCustomRoleRequest.class);
		request.setEid(staffInfo.getCurrentEid());
		request.setOpUserId(staffInfo.getCurrentUserId());

		boolean result = roleApi.createMallCustomRole(request);
		return result ? Result.success() : Result.failed(ResultCode.FAILED);
	}

	@ApiOperation(value = "修改角色")
	@PostMapping("/update")
    @Log(title = "修改角色", businessType = BusinessTypeEnum.UPDATE)
	public Result update(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateRoleForm form) {
		UpdateMallCustomRoleRequest request = PojoUtils.map(form, UpdateMallCustomRoleRequest.class);
		request.setOpUserId(staffInfo.getCurrentUserId());

		boolean result = roleApi.updateMallCustomRole(request);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
	}

	@ApiOperation(value = "修改角色状态")
	@PostMapping("/updateStatus")
    @Log(title = "修改角色状态", businessType = BusinessTypeEnum.UPDATE)
	public Result updateStatus(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateRoleStatusForm form) {
		boolean result = roleApi.updateStatus(form.getId(), form.getStatus() == 1 ? EnableStatusEnum.ENABLED : EnableStatusEnum.DISABLED, staffInfo.getCurrentUserId());
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
	}

	@ApiOperation(value = "转移角色人员")
	@PostMapping("/moveRoleUsers")
    @Log(title = "转移角色人员", businessType = BusinessTypeEnum.UPDATE)
	public Result moveRoleUsers(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid MoveRoleUsersForm form) {
		MoveRoleUsersRequest request = PojoUtils.map(form, MoveRoleUsersRequest.class);
		request.setOpUserId(staffInfo.getCurrentUserId());

		Boolean result = roleApi.moveRoleUsers(request);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
	}

    @ApiOperation(value = "获取新增角色页面数据")
    @GetMapping("/getNewRolePageInfo")
    public Result<CollectionObject<RoleVO.SubsystemPermissionVO>> getNewRolePageInfo(@CurrentUser CurrentStaffInfo staffInfo) {
        // 企业开通的平台
        List<RoleVO.SubsystemPermissionVO> subsystemPermissionVOList = this.getSubsystemPermissionVO(staffInfo.getCurrentEid());

        // 获取用户菜单树
        List<MenuListItemDTO> userMenuTree = menuApi.listMenuTreeByUser(PermissionAppEnum.MALL_ADMIN, MenuTypeEnum.all(), staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
        // 按appId进行分组
        Map<Integer, List<MenuListItemDTO>> userMenuTreeGroupByAppId = userMenuTree.stream().collect(Collectors.groupingBy(MenuListItemDTO::getAppId));

        subsystemPermissionVOList.forEach(e -> {
            List<MenuListItemDTO> menuListItemDTOS;
            if (staffInfo.isAdminFlag()) {
                menuListItemDTOS = menuApi.listMenuTreeByUser(PermissionAppEnum.getByCode(e.getAppId()), MenuTypeEnum.all(), staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
            } else {
                menuListItemDTOS = userMenuTreeGroupByAppId.getOrDefault(e.getAppId(), ListUtil.empty());
            }

            List<RoleVO.MenuTreeVO> menuTreeVOList = PojoUtils.map(menuListItemDTOS, RoleVO.MenuTreeVO.class);
            // 操作权限（菜单）
            e.setMenuList(menuTreeVOList);
        });

        // 过滤掉菜单集合为空的子系统
        subsystemPermissionVOList = subsystemPermissionVOList.stream().filter(e -> CollUtil.isNotEmpty(e.getMenuList())).collect(Collectors.toList());

        return Result.success(new CollectionObject<>(subsystemPermissionVOList));
    }

	@ApiOperation(value = "获取角色详情")
	@GetMapping("/get")
	public Result<RoleVO> get(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam @ApiParam(value = "角色ID", required = true) Long id) {
		RoleDTO roleDTO = roleApi.getById(id);
		if (ObjectUtil.isNull(roleDTO)) {
			return Result.failed(ResultCode.FAILED);
		}

        // 企业开通的平台
        List<RoleVO.SubsystemPermissionVO> subsystemPermissionVOList = this.getSubsystemPermissionVO(staffInfo.getCurrentEid());

        // 获取角色绑定的菜单ID列表
        List<Long> roleMenuIds = menuApi.listMenuIdsByRoleIds(ListUtil.toList(id));
        // 获取用户菜单树
        List<MenuListItemDTO> userMenuTree = menuApi.listMenuTreeByUser(PermissionAppEnum.MALL_ADMIN, MenuTypeEnum.all(), staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
        // 按appId进行分组
        Map<Integer, List<MenuListItemDTO>> userMenuTreeGroupByAppId = userMenuTree.stream().collect(Collectors.groupingBy(MenuListItemDTO::getAppId));

        Map<Integer, RoleDataScopeDTO> roleDataScopeDTOMap = roleApi.getRoleDataScopeMap(id);
        subsystemPermissionVOList.forEach(e -> {
            List<MenuListItemDTO> menuListItemDTOS;

            if (staffInfo.isAdminFlag()) {
                menuListItemDTOS = menuApi.listMenuTreeByUser(PermissionAppEnum.getByCode(e.getAppId()), MenuTypeEnum.all(), staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
            } else {
                menuListItemDTOS = userMenuTreeGroupByAppId.getOrDefault(e.getAppId(), ListUtil.empty());
            }

            List<RoleVO.MenuTreeVO> menuTreeVOList = PojoUtils.map(menuListItemDTOS, RoleVO.MenuTreeVO.class);
            this.setSelected(menuTreeVOList, roleMenuIds);

            // 操作权限（菜单）
            e.setMenuList(menuTreeVOList);

            // 数据权限
            RoleDataScopeDTO roleDataScopeDTO = roleDataScopeDTOMap.get(e.getAppId());
            e.setDataScope(roleDataScopeDTO != null ? roleDataScopeDTO.getDataScope() : 0);
        });

        // 过滤掉菜单集合为空的子系统
        subsystemPermissionVOList = subsystemPermissionVOList.stream().filter(e -> CollUtil.isNotEmpty(e.getMenuList())).collect(Collectors.toList());

        RoleVO roleVO = PojoUtils.map(roleDTO, RoleVO.class);
        roleVO.setSystemPermissionList(subsystemPermissionVOList);
		return Result.success(roleVO);
	}

    private List<RoleVO.SubsystemPermissionVO> getSubsystemPermissionVO (Long eid) {
        EnterprisePlatformDTO enterprisePlatformDTO = enterpriseApi.getEnterprisePlatform(eid);

        List<RoleVO.SubsystemPermissionVO> subsystemPermissionVOList = CollUtil.newArrayList();
        if (enterprisePlatformDTO.getDataCenterFlag() == 1) {
            RoleVO.SubsystemPermissionVO subsystemPermissionVO = new RoleVO.SubsystemPermissionVO();
            subsystemPermissionVO.setAppId(PermissionAppEnum.MALL_ADMIN_DATA_CENTER.getCode());
            subsystemPermissionVO.setDataScopeFlag(false);
            subsystemPermissionVOList.add(subsystemPermissionVO);
        }

        if (enterprisePlatformDTO.getPopFlag() == 1) {
            RoleVO.SubsystemPermissionVO subsystemPermissionVO = new RoleVO.SubsystemPermissionVO();
            subsystemPermissionVO.setAppId(PermissionAppEnum.MALL_ADMIN_POP.getCode());
            subsystemPermissionVO.setDataScopeFlag(true);
            subsystemPermissionVOList.add(subsystemPermissionVO);
        }

        if (enterprisePlatformDTO.getMallFlag() == 1) {
            RoleVO.SubsystemPermissionVO subsystemPermissionVO = new RoleVO.SubsystemPermissionVO();
            subsystemPermissionVO.setAppId(PermissionAppEnum.MALL_ADMIN_B2B.getCode());
            subsystemPermissionVO.setDataScopeFlag(false);
            subsystemPermissionVOList.add(subsystemPermissionVO);
        }

        if (enterprisePlatformDTO.getSalesAssistFlag() == 1) {
            RoleVO.SubsystemPermissionVO subsystemPermissionVO = new RoleVO.SubsystemPermissionVO();
            subsystemPermissionVO.setAppId(PermissionAppEnum.MALL_ADMIN_SALES_ASSISTANT.getCode());
            subsystemPermissionVO.setDataScopeFlag(true);
            subsystemPermissionVOList.add(subsystemPermissionVO);
        }

        if (enterprisePlatformDTO.getHmcFlag() == 1) {
            RoleVO.SubsystemPermissionVO subsystemPermissionVO = new RoleVO.SubsystemPermissionVO();
            subsystemPermissionVO.setAppId(PermissionAppEnum.MALL_ADMIN_HMC.getCode());
            subsystemPermissionVO.setDataScopeFlag(false);
            subsystemPermissionVOList.add(subsystemPermissionVO);
        }

        return subsystemPermissionVOList;
    }

	private void setSelected(List<RoleVO.MenuTreeVO> menuTreeVOList, List<Long> roleMenuIds) {
		if (CollUtil.isEmpty(roleMenuIds)) {
			return;
		}

		menuTreeVOList.forEach(e -> {
			if (roleMenuIds.contains(e.getId())) {
				e.setSelected(true);
			} else {
				e.setSelected(false);
			}

			this.setSelected(e.getChildren(), roleMenuIds);
		});
	}
}
