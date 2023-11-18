package com.yiling.marketing.lotteryactivity.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动参与明细 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryActivityJoinDetailDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 抽奖活动ID
     */
    private Long lotteryActivityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 抽奖时间
     */
    private Date lotteryTime;

    /**
     * 平台类型：1-B端 2-C端
     */
    private Integer platformType;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 用户名称
     */
    private String uname;

    /**
     * 店铺企业ID
     */
    private Long shopEid;

    /**
     * 店铺企业名称
     */
    private String shopEname;

    /**
     * 等级：1-一等奖 2-二等奖 3-三等奖 (以此类推)
     */
    private Integer level;

    /**
     * 奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会 (见字典：lottery_activity_reward_type)
     */
    private Integer rewardType;

    /**
     * 关联奖品ID
     */
    private Long rewardId;

    /**
     * 奖品名称
     */
    private String rewardName;

    /**
     * 展示名称
     */
    private String showName;

    /**
     * 奖品数量
     */
    private Integer rewardNumber;

    /**
     * 奖品图片
     */
    private String rewardImg;

    /**
     * 兑付状态：1-已兑付 2-未兑付
     */
    private Integer status;

    /**
     * 兑付时间
     */
    private Date cashDate;

    /**
     * 收货信息
     */
    private LotteryActivityReceiptInfoDTO lotteryActivityReceiptInfo;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
