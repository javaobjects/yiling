package com.yiling.marketing.lotteryactivity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动参与明细表
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_lottery_activity_join_detail")
public class LotteryActivityJoinDetailDO extends BaseDO {

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
     * 奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会
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
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
