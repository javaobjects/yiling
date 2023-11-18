package com.yiling.user.enterprise.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.After;
import com.yiling.user.common.util.bean.Before;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.Like;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询积分兑换订单分页 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralExchangeOrderPageRequest extends QueryPageListRequest {

    /**
     * 开始兑换提交时间
     */
    @Before(name = "submit_time")
    private Date startSubmitTime;

    /**
     * 结束兑换提交时间
     */
    @After(name = "submit_time")
    private Date endSubmitTime;

    /**
     * 开始订单兑付时间
     */
    @Before(name = "exchange_time")
    private Date startExchangeTime;

    /**
     * 结束订单兑付时间
     */
    @After(name = "exchange_time")
    private Date endExchangeTime;

    /**
     * 用户ID
     */
    @Eq
    private Long uid;

    /**
     * 用户名称
     */
    @Like
    private String uname;

    /**
     * 兑换订单号
     */
    @Eq
    private String orderNo;

    /**
     * 物品名称
     */
    @Like
    private String goodsName;

    /**
     * 兑换状态：1-未兑换 2-已兑换
     */
    @Eq
    private Integer status;

    /**
     * 提交人手机号
     */
    private String mobile;

}
