package com.yiling.payment.channel.service.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2022/7/22
 */
@Data
@Accessors(chain = true)
public class RetryRequest<T>  extends BaseRequest {

    /**
     * 重试最大次数
     */
    private Integer totalCount;

    /**
     * 当前重试次数
     */
    private Integer currentCount;

    /**
     * 重试topic
     */
    private String topic;

    /**
     * 重试级别
     */
    private String delayLevels;

    /**
     * 消息内容
     */
    private T data;


}
