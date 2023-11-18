package com.yiling.dataflow.crm.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/2/16 0016
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RemoveCrmEnterpriseRelationShipRequest extends BaseRequest {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 是否代跑
     */
    private Integer substituteRunning;
}
