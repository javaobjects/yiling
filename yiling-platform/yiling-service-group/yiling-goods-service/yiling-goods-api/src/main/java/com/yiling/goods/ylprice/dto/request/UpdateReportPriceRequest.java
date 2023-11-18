package com.yiling.goods.ylprice.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/2/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateReportPriceRequest extends BaseRequest {

    private static final long serialVersionUID = 3719366181321274343L;

    private Long id;

    private Date endTime;
}
