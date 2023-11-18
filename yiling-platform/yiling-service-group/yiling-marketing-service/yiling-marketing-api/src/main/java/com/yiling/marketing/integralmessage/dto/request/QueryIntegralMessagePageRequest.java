package com.yiling.marketing.integralmessage.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.After;
import com.yiling.user.common.util.bean.Before;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.Like;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询积分兑换消息配置分页 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralMessagePageRequest extends QueryPageListRequest {

    /**
     * 标题
     */
    @Like
    private String title;


    /**
     * 状态：1-启用 2-禁用
     */
    @Eq
    private Integer status;

    /**
     * 开始投放开始时间
     */
    @Before(name = "start_time")
    private Date startTimeStart;

    /**
     * 结束投放开始时间
     */
    @After(name = "start_time")
    private Date startTimeEnd;

    /**
     * 创建时间开始
     */
    @Before(name = "create_time")
    private Date startCreateTime;

    /**
     * 创建时间结束
     */
    @After(name = "create_time")
    private Date EndCreateTime;

}
