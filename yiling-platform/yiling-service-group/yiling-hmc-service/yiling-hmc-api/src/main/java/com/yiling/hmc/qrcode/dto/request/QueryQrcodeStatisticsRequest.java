package com.yiling.hmc.qrcode.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/4/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryQrcodeStatisticsRequest extends BaseRequest {

    private static final long serialVersionUID = -813720326871187724L;
    private Date startTime;

    private Date endTime;
}