package com.yiling.admin.b2b.lotteryactivity.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 获取抽奖机会明细 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryActivityGetVO extends BaseVO {

    /**
     * 抽奖活动ID
     */
    @ApiModelProperty("抽奖活动ID")
    private Long lotteryActivityId;

    /**
     * 抽奖活动名称
     */
    @ApiModelProperty("抽奖活动名称")
    private String activityName;

    /**
     * 平台类型：1-B端 2-C端
     */
    @ApiModelProperty("平台类型：1-B端 2-C端")
    private Integer platformType;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long uid;

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String uname;

    /**
     * 获取方式：1-签到、2-抽奖赠送、3-活动每天赠送、4-分享、5-活动开始赠送、6-购买会员、7-订单累计金额赠送、8-时间周期
     */
    @ApiModelProperty("获取方式：1-签到、2-抽奖赠送、3-活动每天赠送、4-分享、5-活动开始赠送、6-购买会员、7-订单累计金额赠送、8-时间周期")
    private Integer getType;

    /**
     * 获取次数
     */
    @ApiModelProperty("获取次数")
    private Integer getTimes;

    /**
     * 创建时间
     */
    @ApiModelProperty("时间")
    private Date createTime;

}
