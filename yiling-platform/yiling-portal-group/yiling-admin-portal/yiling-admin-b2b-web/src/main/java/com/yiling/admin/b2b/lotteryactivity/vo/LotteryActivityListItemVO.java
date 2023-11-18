package com.yiling.admin.b2b.lotteryactivity.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动列表项 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryActivityListItemVO extends BaseVO {

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 活动分类：1-平台活动 2-商家活动
     */
    @ApiModelProperty("活动分类：1-平台活动 2-商家活动")
    private Integer category;

    /**
     * 运营备注
     */
    @ApiModelProperty("运营备注")
    private String opRemark;

    /**
     * 预算金额
     */
    @ApiModelProperty("预算金额")
    private BigDecimal budgetAmount;

    /**
     * 活动状态：1-启用 2-停用
     */
    @ApiModelProperty("活动状态：1-启用 2-停用")
    private Integer status;

    /**
     * 活动进度：1-未开始 2-进行中 3-已结束 （动态状态，枚举：lottery_activity_progress ，字典：LotteryActivityProgressEnums）
     */
    @ApiModelProperty("活动进度：1-未开始 2-进行中 3-已结束 （动态状态，枚举：lottery_activity_progress ，字典：LotteryActivityProgressEnums）")
    private Integer progress;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建人名称
     */
    @ApiModelProperty("创建人名称")
    private String createUserName;

    /**
     * 创建人手机号
     */
    @ApiModelProperty("创建人手机号")
    private String mobile;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 关联活动数量
     */
    @ApiModelProperty("关联活动数量")
    private Integer unionActivityNum;

    /**
     * 抽奖机会
     */
    @ApiModelProperty("抽奖机会")
    private Integer getJoinNum;

    /**
     * 抽奖次数
     */
    @ApiModelProperty("抽奖次数")
    private Integer joinNum;

    /**
     * 中奖次数
     */
    @ApiModelProperty("中奖次数")
    private Integer hitNum;

    /**
     * 是否展示修改按钮
     */
    @ApiModelProperty("是否展示修改按钮")
    private Boolean showUpdateButton;

    /**
     * 是否展示停用按钮
     */
    @ApiModelProperty("是否展示停用按钮")
    private Boolean showStopButton;

}
