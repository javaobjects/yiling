package com.yiling.marketing.promotion.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 促销活动商品表
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_promotion_goods_limit")
public class PromotionGoodsLimitDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 促销活动ID
     */
    private Long promotionActivityId;

    /**
     * 商品所属企业ID
     */
    private Long eid;

    /**
     * 商品所属企业名称
     */
    private String ename;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 满赠金额
     */
    private BigDecimal giftAmountLimit;

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

    /**
     * 销售价格
     */
    private BigDecimal price;

    /**
     * 活动价格
     */
    private BigDecimal promotionPrice;

    /**
     * 允许购买数量
     */
    private Integer allowBuyCount;

    /**
     * 活动库存
     */
    private Integer promotionStock;

    /**
     * 组合包商品总价
     */
    private BigDecimal packageTotalPrice;

    /**
     * 商品关联skuid
     */
    private Long goodsSkuId;
}
