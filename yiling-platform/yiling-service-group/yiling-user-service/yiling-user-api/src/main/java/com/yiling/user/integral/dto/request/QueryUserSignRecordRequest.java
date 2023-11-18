package com.yiling.user.integral.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.user.common.util.bean.After;
import com.yiling.user.common.util.bean.Before;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.Like;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询用户积分签到记录 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUserSignRecordRequest extends BaseRequest {

    /**
     * 开始签到日期
     */
    @Before(name = "sign_time")
    private Date startSignTime;

    /**
     * 结束签到日期
     */
    @After(name = "sign_time")
    private Date endSignTime;

    /**
     * 发放规则ID
     */
    @Eq
    private Long giveRuleId;

    /**
     * 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手
     */
    @Eq
    private Integer platform;

    /**
     * 用户ID
     */
    @Eq
    private Long uid;

}
