package com.yiling.marketing.promotion.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动主表新增请求参数
 *
 * @author: yong.zhang
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SpecialActivityAppointmentAddRequest extends QueryPageListRequest {

    /**
     * 专场活动企业表id
     */
    private Long specialActivityEnterpriseId;

    /**
     * 专场活动ID
     */
    private Long specialActivityId;

    /**
     * 店铺Eid
     */
    private Long shopEid;

    /**
     * 用户id
     */
    private Long appointmentUserId;

    /**
     * 用户所在企业id
     */
    private Long appointmentUserEid;
}
