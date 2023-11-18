package com.yiling.basic.log.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统操作日志 Request
 *
 * @author: lun.yu
 * @date: 2021/11/27
 */
@Data
@Accessors(chain = true)
public class QuerySysOperLogPageListRequest extends QueryPageListRequest {

    /**
     * 系统标识
     */
    private String systemId;

    /**
     * traceId
     */
    private String requestId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 请求标题
     */
    private String title;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求数据
     */
    private String requestData;

    /**
     * 请求url
     */
    private String requestUrl;

    /**
     * 响应结果
     */
    private String responseData;

    /**
     * 操作错误消息
     */
    private String errorMsg;

    /**
     * 操作状态：1-正常 2-异常
     */
    private Integer status;

    /**
     * 操作人ID
     */
    private Long operId;

    /**
     * 开始操作时间
     */
    private Date startOpTime;

    /**
     * 结束操作时间
     */
    private Date endOpTime;

}
