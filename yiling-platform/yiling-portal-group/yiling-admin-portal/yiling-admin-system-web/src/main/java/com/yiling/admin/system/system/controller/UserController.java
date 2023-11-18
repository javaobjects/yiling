package com.yiling.admin.system.system.controller;

import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.admin.system.system.form.UpdatePasswordForm;
import com.yiling.admin.system.system.vo.UserVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PasswordUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.system.api.AdminApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.Admin;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.enums.UserAuthsAppIdEnum;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/6/10
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = "用户模块接口")
public class UserController extends BaseController {

    @DubboReference
    UserApi userApi;
    @DubboReference
    AdminApi adminApi;

    @ApiOperation(value = "重置密码")
    @GetMapping("/resetPassword")
    @Log(title = "重置密码", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> resetPassword(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam @ApiParam(name = "mobile", value = "用户手机号", required = true) String mobile) {
        Admin admin = adminApi.getByMobile(mobile);
        if (admin == null) {
            return Result.failed("手机号对应的账号不存在");
        }

        if(Objects.nonNull(admin) && admin.isAdmin()){
            if(admin.getId().compareTo(adminInfo.getCurrentUserId()) != 0){
                throw new BusinessException(UserErrorCode.AUTH_ILLEGAL);
            }
        }

        boolean result = adminApi.resetPassword(admin.getId(), adminInfo.getCurrentUserId());
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/updatePassword")
    @Log(title = "修改密码", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> resetPassword(@CurrentUser CurrentAdminInfo adminInfo,@RequestBody @Valid UpdatePasswordForm form) {
        boolean result = adminApi.checkPassword(adminInfo.getCurrentUserId(), form.getOldPassword());
        if(!result){
            throw new BusinessException(UserErrorCode.USER_OLD_PASSWORD_ERROR);
        }

        result = adminApi.updatePassword(adminInfo.getCurrentUserId(), form.getPassword(), adminInfo.getCurrentUserId());
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "根据手机号获取用户信息")
    @GetMapping("/getByMobile")
    public Result<UserVO> getByMobile(@RequestParam(required = true) @ApiParam(value = "手机号", required = true) String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return Result.failed("手机号不能为空");
        }

        Admin admin = adminApi.getByMobile(mobile);
        return Result.success(PojoUtils.map(admin, UserVO.class));
    }
}
