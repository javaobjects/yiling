package com.yiling.basic.shortlink.dto.request;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 短链接 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-01-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveShortLinkRequest extends BaseRequest {

    /**
     * 长地址
     */
    @NotEmpty
    private String longUrl;

}
