package com.yiling.sjms.agency.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/1 0001
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateAgencyFormArchiveRequest extends BaseRequest {

    private Long id;

    /**
     * 数据归档：1-开启 2-关闭
     */
    private Integer archiveStatus;
}
