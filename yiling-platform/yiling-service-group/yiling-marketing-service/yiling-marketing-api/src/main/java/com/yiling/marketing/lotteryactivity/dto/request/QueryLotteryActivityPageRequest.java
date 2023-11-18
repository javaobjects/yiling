package com.yiling.marketing.lotteryactivity.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询抽奖活动分页列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryLotteryActivityPageRequest extends QueryPageListRequest {

    /**
     * 抽奖活动ID
     */
    private Long lotteryActivityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 活动进度：1-未开始 2-进行中 3-已结束 （动态状态，枚举：lottery_activity_progress ，字典：LotteryActivityProgressEnums）
     */
    private Integer progress;

    /**
     * 不等于该活动进度：1-未开始 2-进行中 3-已结束
     */
    private Integer neProgress;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 创建人手机号
     */
    private String mobile;

    /**
     * 开始创建时间
     */
    private Date startCreateTime;

    /**
     * 结束创建时间
     */
    private Date endCreateTime;

    /**
     * 活动分类：1-平台活动 2-商家活动
     */
    private Integer category;

    /**
     * 运营备注
     */
    private String opRemark;

    /**
     * 活动平台：1-B2B 2-健康管理中心公众号 3-健康管理中心患者端 4-以岭互联网医院患者端 5-以岭互联网医院医生端 6-医药代表端 7-店员端
     */
    private Integer platform;

    /**
     * 不在活动平台：1-B2B 2-健康管理中心公众号 3-健康管理中心患者端 4-以岭互联网医院患者端 5-以岭互联网医院医生端 6-医药代表端 7-店员端
     */
    private List<Integer> notInPlatformList;

}
