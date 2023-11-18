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
 * 促销活动购买记录表
 *
 * @author fan.shen
 * @date 2022-2-8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_promotion_buy_record")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionBuyRecordDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 订单表主键
     */
    private String            orderId;

    /**
     * 购买数量
     */
    private Integer           goodsQuantity;

    /**
     * 促销活动ID
     */
    private Long              promotionActivityId;

    /**
     * 企业ID
     */
    private Long              eid;

    /**
     * 商品ID
     */
    private Long              goodsId;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer           delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long              createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date              createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long              updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date              updateTime;

    /**
     * 备注
     */
    private String            remark;

}
