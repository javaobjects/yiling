package com.yiling.sjms.flee.dto.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/13 0013
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateSalesAppealRequest extends BaseRequest {
    /**
     * 列表id
     */
    private Long id;

    /**
     * 补传的月流向
     */
    private String appealMonth;
}
