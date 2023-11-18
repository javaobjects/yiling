package com.yiling.user.integral.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.user.common.util.bean.Eq;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询用户签到翻页 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUserSignRecordTurnPageRequest extends BaseRequest {

    /**
     * 发放规则ID
     */
    @Eq
    private Long giveRuleId;

    /**
     * 当前的年份
     */
    private Integer year;

    /**
     * 当前的月份
     */
    private Integer month;

    /**
     * 翻页类型：1-上一页 2-下一页
     */
    private Integer turnType;

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
