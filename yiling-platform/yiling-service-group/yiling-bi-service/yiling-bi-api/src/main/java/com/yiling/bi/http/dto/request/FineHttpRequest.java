package com.yiling.bi.http.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FineHttpRequest extends BaseRequest {
    private static final long serialVersionUID = -3337103042833235608L;

    /**
     * 请求连
     */
    private String traceId;

    /**
     * 请方法
     */
    private Integer fineRequestCode;

    /**
     * 请求字符串
     */
    private String requestBody;

    /**
     * 请求的时间
     */
    private Date requestTime;

    /**
     * 响应字符串
     */
    private String responseBody;

    /**
     * 返回的时间
     */
    private Date responseTime;

    /**
     * 状态
     */
    private Integer status;

}
