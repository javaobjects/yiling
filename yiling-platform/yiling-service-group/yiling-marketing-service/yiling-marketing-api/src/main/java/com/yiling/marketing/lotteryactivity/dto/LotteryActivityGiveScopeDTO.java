package com.yiling.marketing.lotteryactivity.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动B端赠送范围 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryActivityGiveScopeDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 抽奖活动ID
     */
    private Long lotteryActivityId;

    /**
     * 每人赠送次数
     */
    private Integer giveTimes;

    /**
     * 重复执行：1-关闭 2-每天重复执行
     */
    private Integer loopGive;

    /**
     * 赠送范围：1-全部客户 2-指定客户 3-指定范围客户
     */
    private Integer giveScope;

    /**
     * 指定范围客户企业类型：1-全部类型 2-指定类型
     */
    private Integer giveEnterpriseType;

    /**
     * 指定范围客户用户类型：1-全部用户 2-普通用户 3-全部会员 4-指定方案会员 5-指定推广方会员
     */
    private Integer giveUserType;

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
