package com.yiling.settlement.report.dto.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

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
public class UpdateCategoryGoodsRequest extends BaseRequest {
    private static final long serialVersionUID = -5209966011551795006L;
    @NotNull
    private Long id;
    @NotNull
    private Date endTime;
}