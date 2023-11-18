package com.yiling.open.cms.activity.controller;

import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.activity.api.HMCActivityPatientEducateApi;
import com.yiling.hmc.activity.dto.ActivityPatientEducateDTO;
import com.yiling.open.cms.activity.form.QueryActivityPatientEducateForm;
import com.yiling.open.cms.activity.vo.ActivityPatientEducateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 互联网医院-患教活动控制器
 *
 * @author: fan.shen
 * @date: 2022/9/9
 */
@Api(tags = "患教活动控制器")
@RestController
@RequestMapping("/ihActivityPatientEducate")
public class IHPatientEducateController extends BaseController {

    @DubboReference
    HMCActivityPatientEducateApi hmcActivityPatientEducateApi;

    @ApiOperation(value = "患教活动列表")
    @PostMapping("/pageList")
    @Log(title = "患教活动列表", businessType = BusinessTypeEnum.OTHER)
    public Result<List<ActivityPatientEducateVO>> getActivityPatientEducate(@RequestBody QueryActivityPatientEducateForm form) {
        List<ActivityPatientEducateDTO> list = hmcActivityPatientEducateApi.getActivityPatientEducate(form.getIdList());
        List<ActivityPatientEducateVO> result = PojoUtils.map(list, ActivityPatientEducateVO.class);
        return Result.success(result);
    }

}