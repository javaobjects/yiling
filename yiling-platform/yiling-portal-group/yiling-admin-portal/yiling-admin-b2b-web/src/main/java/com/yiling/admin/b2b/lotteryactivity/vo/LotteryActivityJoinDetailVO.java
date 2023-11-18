package com.yiling.admin.b2b.lotteryactivity.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseVO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityReceiptInfoDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动参与明细 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryActivityJoinDetailVO extends BaseVO {

    /**
     * 抽奖活动ID
     */
    @ApiModelProperty("抽奖活动ID")
    private Long lotteryActivityId;

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;

    /**
     * 抽奖时间
     */
    @ApiModelProperty("抽奖时间")
    private Date lotteryTime;

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
     * 店铺企业名称
     */
    @ApiModelProperty("商家")
    private String shopEname;

    /**
     * 奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会 (见字典：lottery_activity_reward_type)
     */
    @ApiModelProperty("奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会 (见字典：lottery_activity_reward_type)")
    private Integer rewardType;

    /**
     * 奖品名称
     */
    @ApiModelProperty("奖品名称")
    private String rewardName;

    /**
     * 奖品数量
     */
    @ApiModelProperty("奖品数量")
    private Integer rewardNumber;

    /**
     * 兑付状态：1-已兑付 2-未兑付
     */
    @ApiModelProperty("兑付状态：1-已兑付 2-未兑付")
    private Integer status;

    /**
     * 兑付时间
     */
    @ApiModelProperty("兑付时间")
    private Date cashDate;

    /**
     * 收货人
     */
    @ApiModelProperty("收货人")
    private String contactor;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private String contactorPhone;

    /**
     * 所属省份编码
     */
    @ApiModelProperty("收货地址-省编码")
    private String provinceCode;

    /**
     * 所属省份名称
     */
    @ApiModelProperty("收货地址-省名称")
    private String provinceName;

    /**
     * 所属城市编码
     */
    @ApiModelProperty("收货地址-市编码")
    private String cityCode;

    /**
     * 所属城市名称
     */
    @ApiModelProperty("收货地址-市名称")
    private String cityName;

    /**
     * 所属区域编码
     */
    @ApiModelProperty("收货地址-区编码")
    private String regionCode;

    /**
     * 所属区域名称
     */
    @ApiModelProperty("收货地址-区名称")
    private String regionName;

    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    private String address;

    /**
     * 快递公司（见字典）
     */
    @ApiModelProperty("快递公司")
    private String expressCompany;

    /**
     * 快递单号
     */
    @ApiModelProperty("快递单号")
    private String expressOrderNo;

}
