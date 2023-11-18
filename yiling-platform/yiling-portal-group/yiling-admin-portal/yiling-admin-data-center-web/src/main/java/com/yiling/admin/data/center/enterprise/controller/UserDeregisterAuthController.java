package com.yiling.admin.data.center.enterprise.controller;

import java.util.List;
import java.util.Map;
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
import com.yiling.admin.data.center.enterprise.form.QueryUserDeregisterPageListForm;
import com.yiling.admin.data.center.enterprise.form.UpdateUserDeregisterForm;
import com.yiling.admin.data.center.enterprise.vo.EnterpriseEmployeeSimpleVO;
import com.yiling.admin.data.center.enterprise.vo.UserDeregisterAuthListItemVO;
import com.yiling.admin.data.center.enterprise.vo.UserDeregisterDetailsVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.api.UserDeregisterAccountApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.UserDeregisterAccountDTO;
import com.yiling.user.system.dto.request.QueryUserDeregisterPageListRequest;
import com.yiling.user.system.dto.request.UpdateUserDeregisterRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 账号注销审核模块 Controller
 *
 * @author: lun.yu
 * @date: 2022-06-27
 */
@RestController
@RequestMapping("/userDeregisterAuth")
@Api(tags = "账号注销审核模块接口")
@Slf4j
public class UserDeregisterAuthController extends BaseController {

    @DubboReference
    UserDeregisterAccountApi userDeregisterAccountApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    EmployeeApi employeeApi;
    @DubboReference
    UserApi userApi;

    @ApiOperation(value = "获取账号注销审核列表")
    @PostMapping("/queryPageList")
    public Result<Page<UserDeregisterAuthListItemVO>> queryPageList(@CurrentUser CurrentAdminInfo adminInfo , @RequestBody @Valid QueryUserDeregisterPageListForm form) {
        QueryUserDeregisterPageListRequest request = PojoUtils.map(form, QueryUserDeregisterPageListRequest.class);
        Page<UserDeregisterAccountDTO> accountDTOPage = userDeregisterAccountApi.queryPageList(request);
        if (CollUtil.isEmpty(accountDTOPage.getRecords())) {
            return Result.success(PojoUtils.map(accountDTOPage, UserDeregisterAuthListItemVO.class));
        }

        // 获取企业数量
        Map<Long, Integer> enterpriseNumMap = accountDTOPage.getRecords().stream().collect(Collectors.toMap(UserDeregisterAccountDTO::getUserId, userDeregisterAccountDTO -> {
            // 根据用户Id获取用户所属的企业
            return enterpriseApi.listByUserId(userDeregisterAccountDTO.getUserId(), EnableStatusEnum.ALL).size();
        }, (k1, k2) -> k2));

        Page<UserDeregisterAuthListItemVO> voPage = PojoUtils.map(accountDTOPage, UserDeregisterAuthListItemVO.class);
        // 获取审核用户信息
        List<Long> userIdList = voPage.getRecords().stream().map(UserDeregisterAuthListItemVO::getAuthUser).collect(Collectors.toList());
        Map<Long, String> usernameMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(userIdList)) {
            usernameMap = userApi.listByIds(userIdList).stream().collect(Collectors.toMap(BaseDTO::getId, UserDTO::getName));
        }
        Map<Long, String> finalUsernameMap = usernameMap;

        voPage.getRecords().forEach(userDeregisterAuthListItemVO -> {
            userDeregisterAuthListItemVO.setAuthUserName(finalUsernameMap.get(userDeregisterAuthListItemVO.getAuthUser()));
            userDeregisterAuthListItemVO.setEnterpriseNum(enterpriseNumMap.get(userDeregisterAuthListItemVO.getUserId()));
        });

        return Result.success(voPage);
    }

    @ApiOperation(value = "获取审核详情")
    @GetMapping("/getById")
    public Result<UserDeregisterDetailsVO> getById(@CurrentUser CurrentAdminInfo adminInfo, @ApiParam(value = "ID", required = true) @RequestParam("id") Long id) {
        UserDeregisterAccountDTO deregisterAccountDTO = userDeregisterAccountApi.getById(id);
        UserDeregisterDetailsVO deregisterDetailsVO = PojoUtils.map(deregisterAccountDTO, UserDeregisterDetailsVO.class);
        deregisterDetailsVO.setRejectReason(deregisterAccountDTO.getApplyReason());

        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByUserId(deregisterAccountDTO.getUserId(), EnableStatusEnum.ALL);
        List<EnterpriseEmployeeSimpleVO> enterpriseList = enterpriseDTOList.stream().map(enterpriseDTO -> {
            EnterpriseEmployeeSimpleVO employeeSimpleVO = new EnterpriseEmployeeSimpleVO();
            employeeSimpleVO.setId(enterpriseDTO.getId());
            employeeSimpleVO.setName(enterpriseDTO.getName());
            boolean adminFlag = employeeApi.isAdmin(enterpriseDTO.getId(), deregisterAccountDTO.getUserId());
            if (adminFlag) {
                employeeSimpleVO.setIdentity("企业管理员");
            } else {
                employeeSimpleVO.setIdentity("员工");
            }
            return employeeSimpleVO;
        }).collect(Collectors.toList());
        deregisterDetailsVO.setEnterpriseList(enterpriseList);

        return Result.success(deregisterDetailsVO);
    }

    @ApiOperation(value = "确认审核")
    @PostMapping("/updateAuth")
    @Log(title = "确认审核",businessType = BusinessTypeEnum.UPDATE)
    public Result<Void> updateAuth(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateUserDeregisterForm form) {
        UpdateUserDeregisterRequest request = PojoUtils.map(form, UpdateUserDeregisterRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        userDeregisterAccountApi.deregisterAccountAuth(request);

        return Result.success();
    }


}
