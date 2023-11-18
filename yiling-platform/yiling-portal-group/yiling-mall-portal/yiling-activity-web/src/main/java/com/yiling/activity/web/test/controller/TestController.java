package com.yiling.activity.web.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/12/31
 */
@Slf4j
@RestController
@RequestMapping("/test")
@Api(tags = "测试模块接口")
public class TestController extends BaseController {

    @ApiOperation(value = "测试接口")
    @GetMapping("/test1")
    public Result test1(@CurrentUser CurrentStaffInfo staffInfo) {
        return Result.success();
    }
}
