package com.yiling.mall.openposition.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 更新B2B-开屏位状态 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-05-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateOpenPositionStatusRequest extends BaseRequest {

    /**
     * ID
     */
    @NotNull
    private Long id;

    /**
     * 状态：1-未发布 2-已发布
     */
    @NotNull
    private Integer status;

    /**
     * 平台：1-大运河 2-销售助手
     */
    @NotNull
    private Integer platform;

}
