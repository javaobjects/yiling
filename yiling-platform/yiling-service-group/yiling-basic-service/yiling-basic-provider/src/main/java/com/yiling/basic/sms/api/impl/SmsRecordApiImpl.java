package com.yiling.basic.sms.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.sms.api.SmsRecordApi;
import com.yiling.basic.sms.dto.SmsRecordDTO;
import com.yiling.basic.sms.dto.request.QuerySmsRecordPageListRequest;
import com.yiling.basic.sms.service.SmsRecordService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author: shuang.zhang
 * @date: 2021/6/9
 */
@DubboService
public class SmsRecordApiImpl implements SmsRecordApi {

    @Autowired
    SmsRecordService smsRecordService;

    @Override
    public Page<SmsRecordDTO> pageList(QuerySmsRecordPageListRequest request) {
        return PojoUtils.map(smsRecordService.pageList(request), SmsRecordDTO.class);
    }
}
