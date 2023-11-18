package com.yiling.admin.system.system.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.system.system.form.QuerySysOperLogPageListForm;
import com.yiling.admin.system.system.vo.SysOperLogVO;
import com.yiling.basic.log.api.SysOperLogApi;
import com.yiling.basic.log.dto.SysOperLogDTO;
import com.yiling.basic.log.dto.request.QuerySysOperLogPageListRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.log.enums.SystemEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统操作日志 Controller
 *
 * @author: lun.yu
 * @date: 2021/11/27
 */
@Slf4j
@RestController
@RequestMapping("/sysOperLog")
@Api(tags = "系统操作日志接口")
public class SysOperLogController extends BaseController {

    @DubboReference
    SysOperLogApi sysOperLogApi;
    @DubboReference
    UserApi userApi;

    @ApiOperation(value = "分页查询")
    @PostMapping("/queryListPage")
    public Result<Page<SysOperLogVO>> getDictTypePage(@CurrentUser CurrentAdminInfo adminInfo , @RequestBody @Valid QuerySysOperLogPageListForm form) {
        QuerySysOperLogPageListRequest request = PojoUtils.map(form, QuerySysOperLogPageListRequest.class);
        Page<SysOperLogDTO> result = sysOperLogApi.queryListPage(request);
        Page<SysOperLogVO> operLogVoPage = PojoUtils.map(result, SysOperLogVO.class);

        Map<Long, String> userNameMap = MapUtil.newHashMap();
        List<Long> operIdList = operLogVoPage.getRecords().stream().map(SysOperLogVO::getOperId).distinct().collect(Collectors.toList());
        if (CollUtil.isNotEmpty(operIdList)) {
            userNameMap = userApi.listByIds(operIdList).stream().collect(Collectors.toMap(UserDTO::getId,UserDTO::getName));
        }

        Map<Long, String> finalUserNameMap = userNameMap;
        operLogVoPage.getRecords().forEach(logVO -> {
            SystemEnum systemEnum = SystemEnum.getByCode(logVO.getSystemId());
            logVO.setSystemId(systemEnum != null ? systemEnum.getName() : logVO.getSystemId());
            BusinessTypeEnum businessTypeEnum = BusinessTypeEnum.getByCode(logVO.getBusinessType());
            logVO.setBusinessType(businessTypeEnum != null ? businessTypeEnum.getName() : logVO.getBusinessType());
            logVO.setOperName(finalUserNameMap.get(logVO.getOperId()));
        });

        return Result.success(operLogVoPage);
    }

}
