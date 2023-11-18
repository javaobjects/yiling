package com.yiling.admin.system.system.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.system.system.form.QuerySysLoginLogPageListForm;
import com.yiling.admin.system.system.vo.SysLoginLogVO;
import com.yiling.basic.log.api.SysLoginLogApi;
import com.yiling.basic.log.dto.request.QuerySysLoginLogPageListRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统登录日志 Controller
 *
 * @author: lun.yu
 * @date: 2021/12/31
 */
@Slf4j
@RestController
@RequestMapping("/sysLoginLog")
@Api(tags = "系统登录日志接口")
public class SysLoginLogController extends BaseController {

    @DubboReference
    SysLoginLogApi sysLoginLogApi;

    @ApiOperation(value = "分页查询")
    @PostMapping("/queryListPage")
    public Result<Page<SysLoginLogVO>> getDictTypePage(@CurrentUser CurrentAdminInfo adminInfo , @RequestBody @Valid QuerySysLoginLogPageListForm form) {
        QuerySysLoginLogPageListRequest request = PojoUtils.map(form, QuerySysLoginLogPageListRequest.class);
        Page<SysLoginLogVO> page = PojoUtils.map(sysLoginLogApi.queryListPage(request), SysLoginLogVO.class);

        return Result.success(page);
    }

}
