package com.yiling.marketing.promotion.entity;

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
 * 促销活动组合包表
 * </p>
 *
 * @author shixing.sun
 * @date 2022-04-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_promotion_combination_package")
public class PromotionCombinationPackageDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 促销活动表主键
     */
    private Long promotionActivityId;

    /**
     * 组合包名称
     */
    private String packageName;

    /**
     * 组合包起购数量
     */
    private Integer initialNum;

    /**
     * 退货要求
     */
    private String returnRequirement;

    /**
     * 组合包商品简称
     */
    private String packageShortName;

    /**
     * 总数量
     */
    private Integer totalNum;

    /**
     * 每人最大数量
     */
    private Integer perPersonNum;

    /**
     * 每人每天数量
     */
    private Integer perDayNum;

    /**
     * 活动图片
     */
    private String pic;

    /**
     * 组合包与其他营销活动说明
     */
    private String descriptionOfOtherActivity;

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
