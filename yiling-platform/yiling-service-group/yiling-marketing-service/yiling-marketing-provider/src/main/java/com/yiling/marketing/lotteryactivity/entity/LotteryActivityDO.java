package com.yiling.marketing.lotteryactivity.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动表
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_lottery_activity")
public class LotteryActivityDO extends BaseDO {

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
