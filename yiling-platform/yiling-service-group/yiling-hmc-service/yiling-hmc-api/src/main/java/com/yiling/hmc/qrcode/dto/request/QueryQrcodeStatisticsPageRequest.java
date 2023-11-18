package com.yiling.hmc.qrcode.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

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
public class QueryQrcodeStatisticsPageRequest extends QueryPageListRequest {
    private static final long serialVersionUID = 1503020836893707349L;

    private Date startTime;

    private Date endTime;
}