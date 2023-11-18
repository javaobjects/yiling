package com.yiling.basic.mail.service;



import com.alibaba.fastjson.JSONArray;
import com.yiling.basic.mail.enums.MailEnum;
import com.yiling.basic.mail.enums.MailSendStatusEnum;

import java.util.List;

/**
 * @author shichen
 * @类名 MailSendRecordService
 * @描述
 * @创建时间 2022/2/10
 * @修改人 shichen
 * @修改时间 2022/2/10
 **/
public interface MailSendRecordService {
    /**
     * 获取发送邮件成功的业务id
     * @param mailEnum
     * @param businessIds
     * @return
     */
    public List<Long> getSendSuccessBusinessIds(MailEnum mailEnum, List<Long> businessIds);

    /**
     * 批量保存邮件发送记录
     * @param mailEnum
     * @param mailSendStatusEnum
     * @param jsonArray
     * @param sendMsg
     */
    public void saveSendMail(MailEnum mailEnum, MailSendStatusEnum mailSendStatusEnum, JSONArray jsonArray, String sendMsg, String mailUid);
}
