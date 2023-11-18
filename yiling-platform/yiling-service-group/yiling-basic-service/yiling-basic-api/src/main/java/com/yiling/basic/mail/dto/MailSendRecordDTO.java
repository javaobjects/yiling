package com.yiling.basic.mail.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;

/**
 * @author shichen
 * @类名 MailSendRecordDTO
 * @描述
 * @创建时间 2022/2/10
 * @修改人 shichen
 * @修改时间 2022/2/10
 **/
@Data
public class MailSendRecordDTO extends BaseDTO {
    /**
     * 邮件所属code
     */
    private String mailCode;

    /**
     * 邮件绑定业务id
     */
    private Long businessId;

    /**
     * 邮件唯一代码（多个发送记录由一个邮件发送，这个邮件的标识）
     */
    private String mailUid;

    /**
     * 发送内容json对象
     */
    private String sendDataJson;

    /**
     * 发送状态
     */
    private Integer sendStatus;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 发送结果信息
     */
    private String sendResultMsg;
}
