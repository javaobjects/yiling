package com.yiling.marketing.lotteryactivity.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryActivityDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 生效时间
     */
    private Date startTime;

    /**
     * 失效时间
     */
    private Date endTime;

    /**
     * 活动分类：1-平台活动 2-商家活动
     */
    private Integer category;

    /**
     * 活动平台：1-B2B 2-健康管理中心公众号 3-健康管理中心患者端 4-以岭互联网医院患者端 5-以岭互联网医院医生端 6-医药代表端 7-店员端
     */
    private Integer platform;

    /**
     * 运营备注
     */
    private String opRemark;

    /**
     * 预算金额
     */
    private BigDecimal budgetAmount;

    /**
     * 分享背景图
     */
    private String bgPicture;

    /**
     * 活动状态：1-启用 2-停用
     */
    private Integer status;

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
