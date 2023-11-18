package com.yiling.framework.common.base.request;

import java.util.Date;

import lombok.Data;

/**
 * Base Request
 *
 * @author: xuan.zhou
 * @date: 2021/5/14
 */
@Data
public class BaseRequest implements java.io.Serializable {

    private static final long serialVersionUID = -949266048834856477L;

    /**
     * 操作人ID
     */
    private Long opUserId;

    /**
     * 操作时间
     */
    private Date opTime = new Date();
}
