package com.yiling.cms.question.dto;

import java.util.Date;

import lombok.Data;

/**
 * 第一次回复发送消息对象
 */
@Data
public class QuestionReplySendMessageDTO implements java.io.Serializable{

    /**
     * 医生Id
     */
    private Long doctorId;

    /**
     * 问题Id
     */
    private Long questionId;

    /**
     * 时间
     */
    private Date time;

    /**
     * 标题
     */
    private String title;

}
