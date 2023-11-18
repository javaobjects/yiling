package com.yiling.open.erp.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/4/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteErpFlowGoodsConfigRequest extends BaseRequest {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 企业id
     */
    private Long eid;

}
