package com.yiling.marketing.strategy.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryStrategyActivityPageRequest extends QueryPageListRequest {

    /**
     * 活动类型（1-策略满赠,2-支付促销）
     */
    private Integer type;

    /**
     * 活动名称
     */
    private String strategyActivityName;

    /**
     * 状态：0-全部 1-启用 2-停用 3-废弃
     */
    private Integer status;

    /**
     * 活动进度 0-全部 1-未开始 2-进行中 3-已结束
     */
    private Integer progress;

    /**
     * 分类 0-全部 1-平台活动；2-商家活动；
     */
    private Integer sponsorType;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 创建人
     */
    private String createUserName;

    /**
     * 创建人手机号
     */
    private String createTel;

    /**
     * 创建开始时间
     */
    private Date startTime;

    /**
     * 创建截止时间
     */
    private Date stopTime;

    /**
     * 策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员
     */
    private Integer strategyType;

    /**
     * 运营备注
     */
    private String operatingRemark;

    /**
     * 活动名称
     */
    private String name;
}
