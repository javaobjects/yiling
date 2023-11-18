package com.yiling.user.member.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.After;
import com.yiling.user.common.util.bean.Before;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.Like;
import com.yiling.user.common.util.bean.Order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员退款审核列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMemberReturnPageRequest extends QueryPageListRequest {

    /**
     * 终端ID
     */
    @Eq
    private Long eid;

    /**
     * 终端名称
     */
    @Like
    private String ename;

    /**
     * 订单编号
     */
    @Like
    private String orderNo;

    /**
     * 申请开始时间
     */
    @Before(name = "apply_time")
    private Date applyStartTime;

    /**
     * 申请结束时间
     */
    @After(name = "apply_time")
    private Date applyEndTime;

    /**
     * 排序：申请时间倒序
     */
    @Order(asc = false)
    private Date applyTime;

    /**
     * 审核状态：1-待审核 2-已审核 3-已驳回
     */
    @Eq
    private Integer authStatus;

}
