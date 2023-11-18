package com.yiling.user.member.dto.request;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.After;
import com.yiling.user.common.util.bean.Before;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.In;
import com.yiling.user.common.util.bean.Like;
import com.yiling.user.common.util.bean.Order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员优惠券使用订单查询 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMemberCouponPageRequest extends QueryPageListRequest {

    /**
     * 优惠券ID
     */
    @NotNull
    private Long couponActivityId;

}
