package com.yiling.mall.cart.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 进货单信息
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("cart")
public class CartDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 买家企业ID
     */
    private Long buyerEid;

    /**
     * 卖家企业ID
     */
    private Long sellerEid;

    /**
     * 配送商企业ID
     */
    private Long distributorEid;

    /**
     * 配送商商品ID
     */
    private Long distributorGoodsId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品SKUid
     */
    private Long goodsSkuId;

  /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 平台类型：1-POP 2-B2B
     */
    private Integer platformType;

    /**
     * 购物车来源：1-POP,2-B2B,3-ERP,4-互联网医院 @see com.yiling.goods.medicine.enums.GoodsSourceEnum
     */
    private Integer goodSource;

    /**
     * 促销活动类型
     * {@link com.yiling.order.order.enums.PromotionActivityTypeEnum}
     */
    private Integer promotionActivityType;

    /**
     * 促销活动ID
     */
    private Long promotionActivityId;

    /**
     * 是否选中：0-否 1-是
     */
    private Integer selectedFlag;

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
