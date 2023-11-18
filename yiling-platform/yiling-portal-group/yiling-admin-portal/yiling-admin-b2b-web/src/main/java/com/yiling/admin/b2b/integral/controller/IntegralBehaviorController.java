package com.yiling.admin.b2b.integral.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.integral.form.QueryIntegralBehaviorForm;
import com.yiling.admin.b2b.integral.vo.IntegralBehaviorVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.api.IntegralBehaviorApi;
import com.yiling.user.integral.dto.request.QueryIntegralBehaviorRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 积分行为 Controller
 *
 * @author: lun.yu
 * @date: 2022-12-30
 */
@Slf4j
@RestController
@RequestMapping("/integralBehavior")
@Api(tags = "积分行为接口")
public class IntegralBehaviorController extends BaseController {

    @DubboReference
    IntegralBehaviorApi integralBehaviorApi;

    @ApiOperation(value = "行为分页列表")
    @PostMapping("/queryListPage")
    public Result<Page<IntegralBehaviorVO>> queryListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryIntegralBehaviorForm form) {
        QueryIntegralBehaviorRequest request = PojoUtils.map(form, QueryIntegralBehaviorRequest.class);
        Page<IntegralBehaviorVO> page = PojoUtils.map(integralBehaviorApi.queryListPage(request), IntegralBehaviorVO.class);
        return Result.success(page);
    }

}
