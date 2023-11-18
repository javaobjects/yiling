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
 * 抽奖活动随机生成中奖名单 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryActivityHitRandomGenerateDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 抽奖活动ID
     */
    private Long lotteryActivityId;

    /**
     * 平台类型：1-B端 2-C端
     */
    private Integer platformType;

    /**
     * 用户名称
     */
    private String uname;

    /**
     * 等级：1-一等奖 2-二等奖 3-三等奖 (以此类推)
     */
    private Integer level;

    /**
     * 奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会
     */
    private Integer rewardType;

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;


}
