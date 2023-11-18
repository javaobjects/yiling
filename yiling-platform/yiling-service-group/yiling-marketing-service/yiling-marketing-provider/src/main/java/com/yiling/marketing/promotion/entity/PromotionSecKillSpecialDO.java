package com.yiling.marketing.promotion.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 促销活动-秒杀特价表
 * </p>
 *
 * @author fan.shen
 * @date 2022-01-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_promotion_sec_kill_special")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionSecKillSpecialDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 促销活动ID
     */
    private Long promotionActivityId;

    /**
     * 终端身份 1-会员，2-非会员
     */
    private Integer terminalType;

    /**
     * 允许购买区域 1-全部，2-部分
     */
    private Integer permittedAreaType;

    /**
     * 允许购买区域明细json
     */
    private String permittedAreaDetail;

    /**
     * 企业类型 1-全部，2-部分
     */
    private Integer permittedEnterpriseType;

    /**
     * 企业类型json
     */
    private String permittedEnterpriseDetail;

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
