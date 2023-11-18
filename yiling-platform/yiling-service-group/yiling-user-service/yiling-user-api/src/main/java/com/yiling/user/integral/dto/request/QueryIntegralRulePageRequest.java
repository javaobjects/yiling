package com.yiling.user.integral.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;
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
 * 查询积分规则分页 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralRulePageRequest extends QueryPageListRequest {

    /**
     * 规则名称
     */
    @Like
    private String name;

    /**
     * 执行状态：1-启用 2-停用
     */
    @Eq
    private Integer status;

    /**
     * 执行进度：1-未开始 2-进行中 3-已结束
     */
    private Integer progress;

    /**
     * 规则生效时间
     */
    @Before(name = "start_time")
    private Date startTime;

    /**
     * 规则失效时间
     */
    @After(name = "end_time")
    private Date endTime;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 创建人手机号
     */
    private String mobile;

}
