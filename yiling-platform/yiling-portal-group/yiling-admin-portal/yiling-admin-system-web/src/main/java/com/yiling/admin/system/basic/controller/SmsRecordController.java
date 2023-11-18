package com.yiling.admin.system.basic.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.system.basic.form.QuerySmsRecordPageForm;
import com.yiling.admin.system.basic.vo.SmsRecordVO;
import com.yiling.basic.sms.api.SmsRecordApi;
import com.yiling.basic.sms.dto.SmsRecordDTO;
import com.yiling.basic.sms.dto.request.QuerySmsRecordPageListRequest;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.util.PrivacyUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/6/9
 */
@Slf4j
@RestController
@RequestMapping("/sms/record")
@Api(tags = "短信日志模块")
public class SmsRecordController extends BaseController {

    @DubboReference
    SmsRecordApi smsRecordApi;

    @ApiOperation("短信日志列表")
    @PostMapping("/pageList")
    public Result<Page<SmsRecordVO>> pageList(@RequestBody @Valid QuerySmsRecordPageForm form) {
        Page<SmsRecordDTO> page = smsRecordApi.pageList(PojoUtils.map(form, QuerySmsRecordPageListRequest.class));
        page.getRecords().forEach(smsRecordDTO -> smsRecordDTO.setMobile(PrivacyUtils.encryptPhoneNo(smsRecordDTO.getMobile())));
        return Result.success(PojoUtils.map(page, SmsRecordVO.class));
    }
}
