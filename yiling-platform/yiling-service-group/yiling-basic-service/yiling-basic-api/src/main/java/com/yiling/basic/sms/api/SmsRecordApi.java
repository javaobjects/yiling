package com.yiling.basic.sms.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.sms.dto.SmsRecordDTO;
import com.yiling.basic.sms.dto.request.QuerySmsRecordPageListRequest;

/**
 * @author: shuang.zhang
 * @date: 2021/6/9
 */
public interface SmsRecordApi {

    /**
     * 短息日志列表
     * @param request
     * @return
     */
    Page<SmsRecordDTO> pageList(QuerySmsRecordPageListRequest request);

}
