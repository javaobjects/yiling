package com.yiling.user.member.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.After;
import com.yiling.user.common.util.bean.Before;
import com.yiling.user.common.util.bean.Eq;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-查询会员购买记录 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-10-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMemberBuyRecordRequest extends QueryPageListRequest {

    /**
     * 企业ID
     */
    @Eq
    private Long eid;

    /**
     * 开始创建时间
     */
    @Before(name = "create_time", beginOfDay = false)
    private Date startCreateTime;

    /**
     * 结束创建时间
     */
    @After(name = "create_time", endOfDay = false)
    private Date endCreateTime;

}
