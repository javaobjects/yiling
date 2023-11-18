package com.yiling.open.erp.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/2/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ErpMonitorCountStatisticsRequest extends BaseRequest {

    ///**
    // * 终端激活状态：-1:全部，0:未激活
    // * 超过请求次数关闭对接数量
    // */
    //private Integer clientStatus;
    //
    ///**
    // * 最后心跳时间查询类型：-1:全部，0:1小时内无心跳
    // * 1小时内无心跳对接数量
    // */
    //private Integer hartBeatType;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date    endTime;

}
