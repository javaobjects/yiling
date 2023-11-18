package com.yiling.user.procrelation.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateRelationStatusRequest extends BaseRequest {
    private static final long serialVersionUID = -7440301575814123620L;

    /**
     * ID
     */
    private Long id;

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
     * 客户关系表id
     */
    private Long enterpriseCustomerId;

    /**
     * 配送类型：1-工业直配 2-三方配送
     */
    private Integer deliveryType;

    /**
     * 采购关系状态：1-未开始 2-进行中 3-已停用 4-已过期
     */
    private Integer procRelationStatus;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;
}
