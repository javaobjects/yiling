package com.yiling.sjms.agency.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 根据id更新归档状态
 *
 * @author: dexi.yao
 * @date: 2023/2/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateAgencyLockArchiveRequest extends BaseRequest {

    private static final long serialVersionUID = -7865460597856242347L;

    private Long id;

    /**
     * 数据归档：1-开启 2-关闭
     */
    private Integer archiveStatus;
}