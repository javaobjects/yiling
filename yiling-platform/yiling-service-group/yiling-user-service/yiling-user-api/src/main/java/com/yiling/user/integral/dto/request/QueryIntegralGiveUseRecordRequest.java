package com.yiling.user.integral.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.user.common.util.bean.After;
import com.yiling.user.common.util.bean.Before;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.In;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询积分发放/扣减记录 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralGiveUseRecordRequest extends BaseRequest {

    /**
     * 规则ID
     */
    @Eq
    private Long ruleId;

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

    /**
     * 类型：1-订单送积分 2-签到送积分 3-参与活动消耗 4-兑换消耗 5-退货扣减 6-过期作废
     */
    @Eq
    private Integer changeType;

    /**
     * 类型：1-订单送积分 2-签到送积分 3-参与活动消耗 4-兑换消耗 5-退货扣减 6-过期作废
     */
    @In(name = "change_type")
    private List<Integer> changeTypeList;

    /**
     * 发放/扣减开始时间
     */
    @Before(name = "oper_time")
    private Date startOperTime;

    /**
     * 发放/扣减结束时间
     */
    @After(name = "oper_time")
    private Date endOperTime;

}
