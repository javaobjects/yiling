package com.yiling.ih.question.feign.dto.request;

import java.util.Date;

import com.yiling.ih.common.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 第一次回复发送消息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuestionReplySendMessageRequest extends BaseRequest {

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
