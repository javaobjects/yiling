package com.yiling.dataflow.statistics.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/7/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StatisticsFlowBalanceRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 重跑月份
     */
    private String monthTime;
}
