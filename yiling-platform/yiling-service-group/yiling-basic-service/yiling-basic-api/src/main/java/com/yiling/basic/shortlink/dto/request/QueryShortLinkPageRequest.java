package com.yiling.basic.shortlink.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 分页查询短链接 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-01-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryShortLinkPageRequest extends QueryPageListRequest {

    /**
     * 短地址key
     */
    private String urlKey;

    /**
     * 短地址
     */
    private String shortUrl;

    /**
     * 长地址
     */
    private String longUrl;

}
