package com.yiling.basic.sms.service;

import com.yiling.framework.common.pojo.ServiceResult;

/**
 * 腾信短信渠道
 * @author: fei.wu <br>
 * @date: 2021/7/12 <br>
 */
public interface TxSmsChannelService {

    /**
     * 发送短信
     * @param mobile 手机号
     * @param content 短信内容
     * @param signature 短信签名
     * @return
     */
    ServiceResult sendSms(String mobile, String content, String signature);
}
