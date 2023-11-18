package com.yiling.basic.sms.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySmsRecordPageListRequest extends QueryPageListRequest {

    /**
     * 接收人手机号
     */
    private String mobile;

    /**
     * 发送状态：1-待发送 2-发送成功 3-发送失败
     */
    private Integer status;

    /**
     * 开始创建时间
     */
    private Date startCreateTime;

    /**
     * 结束创建时间
     */
    private Date endCreateTime;

}
