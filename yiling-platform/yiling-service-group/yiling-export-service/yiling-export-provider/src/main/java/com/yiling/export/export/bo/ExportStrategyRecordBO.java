package com.yiling.export.export.bo;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * <p>
 * 营销活动记录表
 * </p>
 *
 * @author zhangy
 * @date 2022-09-06
 */
@Data
public class ExportStrategyRecordBO {

    /**
     * 策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员
     */
    private Integer strategyType;

    /**
     * 时间
     */
    private Date createTime;

    /**
     * 促销活动ID
     */
    private Long marketingStrategyId;

    /**
     * 促销规则名称
     */
    private String marketingStrategyName;

    /**
     * 买家企业ID
     */
    private Long eid;

    /**
     * 买家企业名称
     */
    private String ename;

    /**
     * 生效条件
     */
    private String condition;

    /**
     * 促销结果
     */
    private List<String> strategyResult;

    /**
     * 执行结果: 1-成功 2-失败
     */
    private String implementResult;

    /**
     * 备注
     */
    private String remark;


    /**
     * 订单id
     */
    private Long orderId;


    /**
     * 活动阶梯id
     */
    private Long ladderId;


    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 是否赠送成功：1-是 2-否
     */
    private Integer sendStatus;
}
