package com.yiling.marketing.lotteryactivity.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 获取抽奖机会明细导出 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-10-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryActivityGetExportDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 抽奖活动ID
     */
    private Long lotteryActivityId;

    /**
     * 抽奖活动名称
     */
    private String activityName;

    /**
     * 省
     */
    private String provinceName;

    /**
     * 市
     */
    private String cityName;

    /**
     * 区
     */
    private String regionName;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 用户名称
     */
    private String uname;

    /**
     * 获取方式：1-签到、2-抽奖赠送、3-活动每天赠送、4-分享、5-活动开始赠送、6-购买会员、7-订单累计金额赠送、8-时间周期
     */
    private Integer getType;

    /**
     * 获取方式名称
     */
    private String getTypeName;

    /**
     * 获取次数
     */
    private Integer getTimes;

    /**
     * 创建时间
     */
    private Date createTime;

}
