package com.yiling.open.erp.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/1/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateMonitorStatusRequest extends BaseRequest {

    /**
     * 企业id
     */
    @NotNull
    private Long rkSuId;

    /**
     * 父类企业id
     */
    @NotNull
    private Long suId;

    /**
     * 监控状态：0-未开启 1-开启
     */
    @NotNull
    private Integer monitorStatus;

}
