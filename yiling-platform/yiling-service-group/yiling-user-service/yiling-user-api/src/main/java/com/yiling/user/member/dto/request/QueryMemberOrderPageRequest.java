package com.yiling.user.member.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.After;
import com.yiling.user.common.util.bean.Before;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.Like;
import com.yiling.user.common.util.bean.Order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-查询会员订单 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMemberOrderPageRequest extends QueryPageListRequest {

    /**
     * 企业ID
     */
    @Eq
    private Long eid;

    /**
     * 企业名称
     */
    @Like
    private String ename;

    /**
     * 订单状态：10-待支付 20-支付成功 30-支付失败
     */
    @Eq
    private Integer status;

    /**
     * 推广方ID
     */
    @Eq
    private String promoterId;

    /**
     * 推广方名称
     */
    @Like
    private String promoterName;

    /**
     * 推广人ID
     */
    @Eq
    private Long promoterUserId;

    /**
     * 推广方人名称
     */
    @Like
    private String promoterUserName;

    /**
     * 创建开始时间
     */
    @Before(name = "create_time")
    private Date buyStartTime;

    /**
     * 创建结束时间
     */
    @After(name = "create_time")
    private Date buyEndTime;

}
