package com.yiling.pricing.goods.entity;

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
 * 客户分组定价
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_price_customer_group")
public class GoodsPriceCustomerGroupDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商企业ID
     */
    private Long eid;

    /**
     * 产品线
     */
    private Integer goodsLine;

    /**
     * 客户分组ID
     */
    private Long customerGroupId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 定价规则：1-浮动点位 2-具体价格
     */
    private Integer priceRule;

    /**
     * 浮动点位/价格
     */
    private BigDecimal priceValue;

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
