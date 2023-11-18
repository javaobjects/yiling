package com.yiling.marketing.promotion.entity;

import java.math.BigDecimal;
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
 * 促销活动赠品表
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_promotion_goods_gift_limit")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionGoodsGiftLimitDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 促销活动ID
     */
    private Long promotionActivityId;

    /**
     * 满赠金额
     */
    private BigDecimal promotionAmount;

    /**
     * 赠送商品ID
     */
    private Long goodsGiftId;

    /**
     * 参与活动商品数量
     */
    private Integer promotionStock;

    /**
     * 已经参与活动商品数量
     */
    private Integer usedStock;

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
