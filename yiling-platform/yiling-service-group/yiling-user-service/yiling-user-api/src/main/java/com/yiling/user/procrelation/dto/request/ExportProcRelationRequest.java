package com.yiling.user.procrelation.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-05-23
 */
@Data
@Accessors(chain = true)
public class ExportProcRelationRequest extends BaseRequest {

    /**
     * 采购关系编号
     */
    private String procRelationNumber;

    /**
     * 工业主体eid
     */
    private Long factoryEid;

    /**
     * 配送商eid
     */
    private Long deliveryEid;

    /**
     * 渠道商eid
     */
    private Long channelPartnerEid;

    /**
     * 渠道ID
     */
    private Long channelId;

    /**
     * 配送类型：1-工业直配 2-三方配送
     */
    private Integer deliveryType;

    /**
     * 采购关系状态：1-未开始 2-进行中 3-已停用 4-已过期
     */
    private Integer procRelationStatus;

}
