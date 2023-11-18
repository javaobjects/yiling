package com.yiling.sjms.system.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.system.vo.MyDetailsVO;
import com.yiling.user.system.api.SjmsUserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.bo.SjmsUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户信息 Controller
 *
 * @author: xuan.zhou
 * @date: 2022/11/24
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController {

    @DubboReference
    SjmsUserApi sjmsUserApi;

    @ApiOperation(value = "获取当前用户信息")
    @GetMapping("/getMyDetails")
    public Result<MyDetailsVO> getMyDetails(@CurrentUser CurrentSjmsUserInfo userInfo) {
        SjmsUser sjmsUser = sjmsUserApi.getById(userInfo.getCurrentUserId());
        return Result.success(PojoUtils.map(sjmsUser, MyDetailsVO.class));
    }
}
