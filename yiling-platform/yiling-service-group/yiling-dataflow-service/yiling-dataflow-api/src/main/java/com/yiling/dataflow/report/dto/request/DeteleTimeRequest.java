package com.yiling.dataflow.report.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/6/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeteleTimeRequest extends BaseRequest {
    private Date time;
}
